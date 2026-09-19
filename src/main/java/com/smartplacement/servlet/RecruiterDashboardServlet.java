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

@WebServlet("/recruiter-dashboard")
public class RecruiterDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // Only recruiters can access this dashboard
        if (session == null
                || session.getAttribute("userId") == null
                || !"RECRUITER".equals(session.getAttribute("role"))) {

            response.sendRedirect("login.html");
            return;
        }

        long userId = (Long) session.getAttribute("userId");

        String companyQuery = """
                SELECT id, company_name
                FROM companies
                WHERE user_id = ?
                """;

        String jobsQuery = """
                SELECT COUNT(*)
                FROM jobs j
                JOIN companies c
                    ON j.company_id = c.id
                WHERE c.user_id = ?
                """;

        String applicantsQuery = """
                SELECT COUNT(*)
                FROM applications a
                JOIN jobs j
                    ON a.job_id = j.id
                JOIN companies c
                    ON j.company_id = c.id
                WHERE c.user_id = ?
                """;

        String interviewsQuery = """
                SELECT COUNT(*)
                FROM interviews i
                JOIN applications a
                    ON i.application_id = a.id
                JOIN jobs j
                    ON a.job_id = j.id
                JOIN companies c
                    ON j.company_id = c.id
                WHERE c.user_id = ?
                """;

        try (Connection connection = DBConnection.getConnection()) {

            // Company information
            try (PreparedStatement statement =
                         connection.prepareStatement(companyQuery)) {

                statement.setLong(1, userId);

                try (ResultSet resultSet =
                             statement.executeQuery()) {

                    if (resultSet.next()) {

                        request.setAttribute(
                                "companyId",
                                resultSet.getLong("id")
                        );

                        request.setAttribute(
                                "companyName",
                                resultSet.getString("company_name")
                        );

                    } else {

                        request.setAttribute(
                                "companyName",
                                "Company profile not created"
                        );
                    }
                }
            }

            // Total jobs
            try (PreparedStatement statement =
                         connection.prepareStatement(jobsQuery)) {

                statement.setLong(1, userId);

                try (ResultSet resultSet =
                             statement.executeQuery()) {

                    if (resultSet.next()) {
                        request.setAttribute(
                                "jobCount",
                                resultSet.getInt(1)
                        );
                    }
                }
            }

            // Total applicants
            try (PreparedStatement statement =
                         connection.prepareStatement(applicantsQuery)) {

                statement.setLong(1, userId);

                try (ResultSet resultSet =
                             statement.executeQuery()) {

                    if (resultSet.next()) {
                        request.setAttribute(
                                "applicantCount",
                                resultSet.getInt(1)
                        );
                    }
                }
            }

            // Total interviews
            try (PreparedStatement statement =
                         connection.prepareStatement(interviewsQuery)) {

                statement.setLong(1, userId);

                try (ResultSet resultSet =
                             statement.executeQuery()) {

                    if (resultSet.next()) {
                        request.setAttribute(
                                "interviewCount",
                                resultSet.getInt(1)
                        );
                    }
                }
            }

            request.getRequestDispatcher(
                    "/recruiter-dashboard.jsp"
            ).forward(request, response);

        } catch (Exception e) {

            e.printStackTrace();

            response.setContentType("text/html");

            response.getWriter().println(
                    "<h2>Database error occurred.</h2>"
            );
        }
    }
}