package com.smartplacement.servlet;

import com.smartplacement.model.Interview;
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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/recruiter-interviews")
public class RecruiterInterviewsServlet extends HttpServlet {

    private boolean isRecruiter(HttpSession session) {

        return session != null
                && session.getAttribute("userId") != null
                && "RECRUITER".equals(session.getAttribute("role"));
    }

    @Override
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (!isRecruiter(session)) {
            response.sendRedirect("login.html");
            return;
        }

        long recruiterUserId;

        try {
            recruiterUserId =
                    ((Number) session.getAttribute("userId")).longValue();

        } catch (Exception e) {

            response.sendRedirect("login.html");
            return;
        }

        List<Interview> interviews = new ArrayList<>();

        String sql =
                "SELECT " +
                "i.id, " +
                "i.application_id, " +
                "i.interview_date, " +
                "i.interview_time, " +
                "i.mode, " +
                "i.meeting_link, " +
                "i.venue, " +
                "i.status, " +
                "j.title AS job_title, " +
                "c.company_name, " +
                "u.name AS student_name, " +
                "u.email AS student_email, " +
                "s.roll_number " +
                "FROM interviews i " +
                "JOIN applications a ON i.application_id = a.id " +
                "JOIN students s ON a.student_id = s.id " +
                "JOIN users u ON s.user_id = u.id " +
                "JOIN jobs j ON a.job_id = j.id " +
                "JOIN companies c ON j.company_id = c.id " +
                "WHERE c.user_id = ? " +
                "ORDER BY " +
                "i.interview_date DESC, " +
                "i.interview_time DESC, " +
                "i.id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, recruiterUserId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Interview interview = new Interview();

                    interview.setId(rs.getLong("id"));
                    interview.setApplicationId(
                            rs.getLong("application_id")
                    );

                    interview.setInterviewDate(
                            rs.getDate("interview_date")
                    );

                    interview.setInterviewTime(
                            rs.getTime("interview_time")
                    );

                    interview.setMode(
                            rs.getString("mode")
                    );

                    interview.setMeetingLink(
                            rs.getString("meeting_link")
                    );

                    interview.setVenue(
                            rs.getString("venue")
                    );

                    interview.setStatus(
                            rs.getString("status")
                    );

                    interview.setJobTitle(
                            rs.getString("job_title")
                    );

                    interview.setCompanyName(
                            rs.getString("company_name")
                    );

                    interview.setStudentName(
                            rs.getString("student_name")
                    );

                    interview.setStudentEmail(
                            rs.getString("student_email")
                    );

                    interview.setRollNumber(
                            rs.getString("roll_number")
                    );

                    interviews.add(interview);
                }
            }

        } catch (Exception e) {

            e.printStackTrace();

            request.setAttribute(
                    "error",
                    "Unable to load interview schedule."
            );
        }

        request.setAttribute("interviews", interviews);

        request.getRequestDispatcher("/recruiter-interviews.jsp")
                .forward(request, response);
    }
}