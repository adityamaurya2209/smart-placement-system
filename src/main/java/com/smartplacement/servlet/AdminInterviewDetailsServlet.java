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

@WebServlet("/admin-interview-details")
public class AdminInterviewDetailsServlet
        extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);

        // Admin authentication
        if (session == null ||
                session.getAttribute("userId") == null ||
                !"ADMIN".equalsIgnoreCase(
                        String.valueOf(
                                session.getAttribute("role")))) {

            response.sendRedirect("login.html");
            return;
        }

        String id =
                request.getParameter("id");

        System.out.println(
                "Interview ID received: " + id
        );

        if (id == null || id.isBlank()) {

            response.sendRedirect(
                    "admin-interviews"
            );

            return;
        }

        long interviewId;

        try {

            interviewId =
                    Long.parseLong(id);

        } catch (NumberFormatException e) {

            response.sendRedirect(
                    "admin-interviews"
            );

            return;
        }

        String sql = """
                SELECT i.id,
                       i.application_id,
                       u.name AS student_name,
                       u.email AS student_email,
                       j.title AS job_title,
                       c.company_name,
                       i.interview_date,
                       i.interview_time,
                       i.mode,
                       i.meeting_link,
                       i.venue,
                       i.status
                FROM interviews i
                JOIN applications a
                  ON i.application_id = a.id
                JOIN students s
                  ON a.student_id = s.id
                JOIN users u
                  ON s.user_id = u.id
                JOIN jobs j
                  ON a.job_id = j.id
                JOIN companies c
                  ON j.company_id = c.id
                WHERE i.id = ?
                """;

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(
                    1,
                    interviewId
            );

            try (ResultSet rs =
                         statement.executeQuery()) {

                if (rs.next()) {

                    request.setAttribute(
                            "interviewId",
                            rs.getLong("id")
                    );

                    request.setAttribute(
                            "applicationId",
                            rs.getLong("application_id")
                    );

                    request.setAttribute(
                            "studentName",
                            rs.getString(
                                    "student_name")
                    );

                    request.setAttribute(
                            "studentEmail",
                            rs.getString(
                                    "student_email")
                    );

                    request.setAttribute(
                            "jobTitle",
                            rs.getString(
                                    "job_title")
                    );

                    request.setAttribute(
                            "companyName",
                            rs.getString(
                                    "company_name")
                    );

                    request.setAttribute(
                            "interviewDate",
                            rs.getDate(
                                    "interview_date")
                    );

                    request.setAttribute(
                            "interviewTime",
                            rs.getTime(
                                    "interview_time")
                    );

                    request.setAttribute(
                            "mode",
                            rs.getString("mode")
                    );

                    request.setAttribute(
                            "meetingLink",
                            rs.getString(
                                    "meeting_link")
                    );

                    request.setAttribute(
                            "venue",
                            rs.getString("venue")
                    );

                    request.setAttribute(
                            "status",
                            rs.getString("status")
                    );

                    request.getRequestDispatcher(
                            "/admin-interview-details.jsp"
                    ).forward(
                            request,
                            response
                    );

                } else {

                    response.sendRedirect(
                            "admin-interviews"
                    );
                }
            }

        } catch (Exception e) {

            e.printStackTrace();

            response.setContentType(
                    "text/html"
            );

            response.getWriter().println(
                    "<h2>Database error occurred.</h2>"
            );
        }
    }
}