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

@WebServlet("/admin-job-details")
public class AdminJobDetailsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // Admin authentication
        if (session == null ||
                session.getAttribute("userId") == null ||
                !"ADMIN".equalsIgnoreCase(
                        String.valueOf(
                                session.getAttribute("role")))) {

            response.sendRedirect("login.html");
            return;
        }

        String id = request.getParameter("id");

        System.out.println(
                "Job ID received: " + id
        );

        if (id == null || id.isBlank()) {

            response.sendRedirect("admin-jobs");
            return;
        }

        long jobId;

        try {

            jobId = Long.parseLong(id);

        } catch (NumberFormatException e) {

            response.sendRedirect("admin-jobs");
            return;
        }

        String sql = """
                SELECT j.id,
                       j.company_id,
                       c.company_name,
                       j.title,
                       j.description,
                       j.location,
                       j.minimum_cgpa,
                       j.eligible_branch,
                       j.required_skills,
                       j.salary,
                       j.application_deadline,
                       j.status,
                       j.created_at
                FROM jobs j
                JOIN companies c
                  ON j.company_id = c.id
                WHERE j.id = ?
                """;

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, jobId);

            try (ResultSet rs =
                         statement.executeQuery()) {

                if (rs.next()) {

                    request.setAttribute(
                            "jobId",
                            rs.getLong("id")
                    );

                    request.setAttribute(
                            "companyId",
                            rs.getLong("company_id")
                    );

                    request.setAttribute(
                            "companyName",
                            rs.getString("company_name")
                    );

                    request.setAttribute(
                            "jobTitle",
                            rs.getString("title")
                    );

                    request.setAttribute(
                            "jobDescription",
                            rs.getString("description")
                    );

                    request.setAttribute(
                            "jobLocation",
                            rs.getString("location")
                    );

                    request.setAttribute(
                            "minimumCgpa",
                            rs.getBigDecimal("minimum_cgpa")
                    );

                    request.setAttribute(
                            "eligibleBranch",
                            rs.getString("eligible_branch")
                    );

                    request.setAttribute(
                            "requiredSkills",
                            rs.getString("required_skills")
                    );

                    request.setAttribute(
                            "salary",
                            rs.getString("salary")
                    );

                    request.setAttribute(
                            "applicationDeadline",
                            rs.getDate("application_deadline")
                    );

                    request.setAttribute(
                            "status",
                            rs.getString("status")
                    );

                    request.setAttribute(
                            "jobCreatedAt",
                            rs.getTimestamp("created_at")
                    );

                    request.getRequestDispatcher(
                            "/admin-job-details.jsp"
                    ).forward(
                            request,
                            response
                    );

                } else {

                    System.out.println(
                            "No job found with ID: "
                                    + jobId
                    );

                    response.sendRedirect(
                            "admin-jobs"
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