package com.smartplacement.servlet;

import com.smartplacement.util.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.sql.Time;

@WebServlet("/schedule-interview")
public class ScheduleInterviewServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        // Check recruiter login
        HttpSession session = request.getSession(false);

        if (session == null ||
            session.getAttribute("userId") == null) {

            response.sendRedirect("login.html");
            return;
        }

        long recruiterUserId =
                (Long) session.getAttribute("userId");

        String applicationIdParameter =
                request.getParameter("applicationId");

        String jobIdParameter =
                request.getParameter("jobId");

        String interviewDate =
                request.getParameter("interviewDate");

        String interviewTime =
                request.getParameter("interviewTime");

        String mode =
                request.getParameter("mode");

        String meetingLink =
                request.getParameter("meetingLink");

        String venue =
                request.getParameter("venue");

        // Basic validation
        if (applicationIdParameter == null ||
            jobIdParameter == null ||
            interviewDate == null ||
            interviewTime == null ||
            mode == null) {

            response.sendRedirect(
                    "applicants?jobId=" + jobIdParameter
            );

            return;
        }

        long applicationId;
        long jobId;

        try {

            applicationId =
                    Long.parseLong(applicationIdParameter);

            jobId =
                    Long.parseLong(jobIdParameter);

        } catch (NumberFormatException e) {

            response.sendRedirect(
                    "recruiter-dashboard"
            );

            return;
        }

        // Only allow valid interview modes
        if (!mode.equals("ONLINE") &&
            !mode.equals("OFFLINE")) {

            response.sendRedirect(
                    "applicants?jobId=" + jobId
            );

            return;
        }

        String checkApplicationSql = """
                SELECT a.status
                FROM applications a
                JOIN jobs j ON a.job_id = j.id
                JOIN companies c ON j.company_id = c.id
                WHERE a.id = ?
                  AND a.job_id = ?
                  AND c.user_id = ?
                """;

        String checkInterviewSql = """
                SELECT id
                FROM interviews
                WHERE application_id = ?
                """;

        String insertInterviewSql = """
                INSERT INTO interviews
                (
                    application_id,
                    interview_date,
                    interview_time,
                    mode,
                    meeting_link,
                    venue
                )
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        String updateApplicationSql = """
                UPDATE applications
                SET status = 'INTERVIEW'
                WHERE id = ?
                """;

        try (Connection connection =
                     DBConnection.getConnection()) {

            /*
             * 1. Verify that this application belongs
             *    to the recruiter's company.
             */
            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 checkApplicationSql)) {

                statement.setLong(1, applicationId);
                statement.setLong(2, jobId);
                statement.setLong(3, recruiterUserId);

                try (ResultSet resultSet =
                             statement.executeQuery()) {

                    if (!resultSet.next()) {

                        response.setContentType("text/html");

                        response.getWriter().println(
                                "<h2>Application not found.</h2>"
                        );

                        return;
                    }

                    String applicationStatus =
                            resultSet.getString("status");

                    /*
                     * Interview should only be scheduled
                     * for a shortlisted student.
                     */
                    if (!"SHORTLISTED".equals(
                            applicationStatus)) {

                        response.setContentType("text/html");

                        response.getWriter().println(
                                "<h2>Only shortlisted students can be scheduled for an interview.</h2>"
                        );

                        response.getWriter().println(
                                "<a href='applicants?jobId="
                                + jobId
                                + "'>Back to Applicants</a>"
                        );

                        return;
                    }
                }
            }

            /*
             * 2. Check whether an interview already exists.
             */
            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 checkInterviewSql)) {

                statement.setLong(1, applicationId);

                try (ResultSet resultSet =
                             statement.executeQuery()) {

                    if (resultSet.next()) {

                        response.setContentType("text/html");

                        response.getWriter().println(
                                "<h2>An interview is already scheduled for this application.</h2>"
                        );

                        response.getWriter().println(
                                "<a href='applicants?jobId="
                                + jobId
                                + "'>Back to Applicants</a>"
                        );

                        return;
                    }
                }
            }

            /*
             * 3. Insert interview.
             */
            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 insertInterviewSql)) {

                statement.setLong(1, applicationId);

                statement.setDate(
                        2,
                        Date.valueOf(interviewDate)
                );

                statement.setTime(
                        3,
                        Time.valueOf(
                                interviewTime + ":00"
                        )
                );

                statement.setString(4, mode);

                if (meetingLink == null ||
                    meetingLink.trim().isEmpty()) {

                    statement.setNull(
                            5,
                            java.sql.Types.VARCHAR
                    );

                } else {

                    statement.setString(
                            5,
                            meetingLink
                    );
                }

                if (venue == null ||
                    venue.trim().isEmpty()) {

                    statement.setNull(
                            6,
                            java.sql.Types.VARCHAR
                    );

                } else {

                    statement.setString(
                            6,
                            venue
                    );
                }

                statement.executeUpdate();
            }

            /*
             * 4. Change application status
             *    from SHORTLISTED to INTERVIEW.
             */
            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 updateApplicationSql)) {

                statement.setLong(1, applicationId);

                statement.executeUpdate();
            }

            /*
             * 5. Go back to applicants page.
             */
            response.sendRedirect(
                    "applicants?jobId=" + jobId
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.setContentType("text/html");

            response.getWriter().println(
                    "<h2>Database error occurred.</h2>"
            );
        }
    }
}