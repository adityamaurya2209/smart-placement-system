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

@WebServlet("/admin-recruiter-update")
public class AdminRecruiterUpdateServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
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
        String name = request.getParameter("name");
        String email = request.getParameter("email");

        if (id == null ||
                id.isBlank() ||
                name == null ||
                name.isBlank() ||
                email == null ||
                email.isBlank()) {

            response.sendRedirect(
                    "admin-recruiters"
            );

            return;
        }

        long recruiterId;

        try {

            recruiterId =
                    Long.parseLong(id);

        } catch (NumberFormatException e) {

            response.sendRedirect(
                    "admin-recruiters"
            );

            return;
        }

        name = name.trim();
        email = email.trim();

        try (Connection connection =
                     DBConnection.getConnection()) {


            // Check whether email belongs
            // to another user

            String checkEmailSql = """
                    SELECT id
                    FROM users
                    WHERE email = ?
                    AND id <> ?
                    """;

            try (PreparedStatement checkStatement =
                         connection.prepareStatement(
                                 checkEmailSql)) {

                checkStatement.setString(
                        1,
                        email
                );

                checkStatement.setLong(
                        2,
                        recruiterId
                );

                try (ResultSet rs =
                             checkStatement.executeQuery()) {

                    if (rs.next()) {

                        response.sendRedirect(
                                "admin-recruiter-edit?id="
                                        + recruiterId
                                        + "&error=email-exists"
                        );

                        return;
                    }
                }
            }


            // Update recruiter

            String updateSql = """
                    UPDATE users
                    SET name = ?,
                        email = ?
                    WHERE id = ?
                    AND role = 'RECRUITER'
                    """;

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 updateSql)) {

                statement.setString(
                        1,
                        name
                );

                statement.setString(
                        2,
                        email
                );

                statement.setLong(
                        3,
                        recruiterId
                );

                int rowsUpdated =
                        statement.executeUpdate();

                if (rowsUpdated > 0) {

                    response.sendRedirect(
                            "admin-recruiter-details?id="
                                    + recruiterId
                                    + "&success=updated"
                    );

                } else {

                    response.sendRedirect(
                            "admin-recruiter-edit?id="
                                    + recruiterId
                                    + "&error=update-failed"
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