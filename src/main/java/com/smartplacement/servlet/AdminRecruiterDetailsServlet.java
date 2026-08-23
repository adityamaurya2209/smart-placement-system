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

@WebServlet("/admin-recruiter-details")
public class AdminRecruiterDetailsServlet extends HttpServlet {

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

        String id = request.getParameter("id");

        System.out.println(
                "Recruiter ID received: " + id
        );

        if (id == null || id.isBlank()) {

            response.sendRedirect(
                    "admin-recruiters"
            );

            return;
        }

        long recruiterId;

        try {

            recruiterId = Long.parseLong(id);

        } catch (NumberFormatException e) {

            response.sendRedirect(
                    "admin-recruiters"
            );

            return;
        }

        String sql = """
                SELECT id, name, email, created_at
                FROM users
                WHERE id = ?
                AND role = 'RECRUITER'
                """;

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(
                    1,
                    recruiterId
            );

            try (ResultSet rs =
                         statement.executeQuery()) {

                if (rs.next()) {

                    request.setAttribute(
                            "recruiterId",
                            rs.getLong("id")
                    );

                    request.setAttribute(
                            "recruiterName",
                            rs.getString("name")
                    );

                    request.setAttribute(
                            "recruiterEmail",
                            rs.getString("email")
                    );

                    request.setAttribute(
                            "recruiterCreatedAt",
                            rs.getTimestamp("created_at")
                    );

                    System.out.println(
                            "Recruiter found: "
                                    + rs.getString("name")
                    );

                    request.getRequestDispatcher(
                            "/admin-recruiter-details.jsp"
                    ).forward(
                            request,
                            response
                    );

                } else {

                    System.out.println(
                            "No recruiter found with ID: "
                                    + recruiterId
                    );

                    response.sendRedirect(
                            "admin-recruiters"
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