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

@WebServlet("/admin-recruiter-delete")
public class AdminRecruiterDeleteServlet extends HttpServlet {

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

        if (id == null || id.isBlank()) {
            response.sendRedirect("admin-recruiters");
            return;
        }

        long recruiterId;

        try {
            recruiterId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            response.sendRedirect("admin-recruiters");
            return;
        }

        try (Connection connection =
                     DBConnection.getConnection()) {

            // Check whether recruiter owns a company
            String checkSql = """
                    SELECT company_name
                    FROM companies
                    WHERE user_id = ?
                    """;

            try (PreparedStatement checkStatement =
                         connection.prepareStatement(checkSql)) {

                checkStatement.setLong(1, recruiterId);

                try (ResultSet rs =
                             checkStatement.executeQuery()) {

                    if (rs.next()) {

                        String companyName =
                                rs.getString("company_name");

                        response.sendRedirect(
                                "admin-recruiter-details?id="
                                        + recruiterId
                                        + "&error=company-linked"
                        );

                        System.out.println(
                                "Cannot delete recruiter "
                                        + recruiterId
                                        + ". Linked company: "
                                        + companyName
                        );

                        return;
                    }
                }
            }

            // Delete only recruiter account
            String deleteSql = """
                    DELETE FROM users
                    WHERE id = ?
                    AND role = 'RECRUITER'
                    """;

            try (PreparedStatement deleteStatement =
                         connection.prepareStatement(deleteSql)) {

                deleteStatement.setLong(1, recruiterId);

                int rowsDeleted =
                        deleteStatement.executeUpdate();

                if (rowsDeleted > 0) {

                    System.out.println(
                            "Recruiter deleted: "
                                    + recruiterId
                    );

                    response.sendRedirect(
                            "admin-recruiters?success=deleted"
                    );

                } else {

                    response.sendRedirect(
                            "admin-recruiters?error=delete-failed"
                    );
                }
            }

        } catch (Exception e) {

            e.printStackTrace();

            response.sendRedirect(
                    "admin-recruiters?error=delete-failed"
            );
        }
    }
}