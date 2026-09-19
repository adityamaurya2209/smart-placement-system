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

@WebServlet("/update-application-status")
public class UpdateApplicationStatusServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null
                || session.getAttribute("userId") == null
                || !"RECRUITER".equals(session.getAttribute("role"))) {

            response.sendRedirect("login.html");
            return;
        }

        String applicationIdParameter =
                request.getParameter("applicationId");

        String newStatus =
                request.getParameter("status");

        if (applicationIdParameter == null
                || applicationIdParameter.isBlank()
                || newStatus == null
                || newStatus.isBlank()) {

            response.sendRedirect("applicants?error=invalid");
            return;
        }

        long applicationId;

        try {

            applicationId =
                    Long.parseLong(applicationIdParameter);

        } catch (NumberFormatException e) {

            response.sendRedirect("applicants?error=invalid");
            return;
        }

        newStatus = newStatus.trim().toUpperCase();

        if (!isValidStatus(newStatus)) {

            response.sendRedirect("applicants?error=invalid-status");
            return;
        }

        long userId =
                (Long) session.getAttribute("userId");

        String currentStatus = null;

        String ownershipQuery = """
                SELECT a.status
                FROM applications a

                JOIN jobs j
                    ON a.job_id = j.id

                JOIN companies c
                    ON j.company_id = c.id

                WHERE a.id = ?
                  AND c.user_id = ?
                """;

        try (Connection connection =
                     DBConnection.getConnection()) {

            // ------------------------------------------
            // Verify recruiter ownership
            // ------------------------------------------

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 ownershipQuery)) {

                statement.setLong(1, applicationId);
                statement.setLong(2, userId);

                try (ResultSet resultSet =
                             statement.executeQuery()) {

                    if (!resultSet.next()) {

                        response.setStatus(
                                HttpServletResponse.SC_FORBIDDEN
                        );

                        response.setContentType(
                                "text/html;charset=UTF-8"
                        );

                        response.getWriter().println(
                                "<h2>You are not authorized to update this application.</h2>"
                        );

                        return;
                    }

                    currentStatus =
                            resultSet.getString("status");
                }
            }

            // ------------------------------------------
            // Validate workflow transition
            // ------------------------------------------

            if (!isAllowedTransition(
                    currentStatus,
                    newStatus
            )) {

                response.sendRedirect(
                        "applicants?error=invalid-transition"
                );

                return;
            }

            // ------------------------------------------
            // Update status
            // ------------------------------------------

            String updateQuery = """
                    UPDATE applications
                    SET status = ?
                    WHERE id = ?
                    """;

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 updateQuery)) {

                statement.setString(1, newStatus);
                statement.setLong(2, applicationId);

                int rows =
                        statement.executeUpdate();

                if (rows == 0) {

                    response.sendRedirect(
                            "applicants?error=update"
                    );

                    return;
                }
            }

            response.sendRedirect(
                    "applicants?updated=true"
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.setContentType(
                    "text/html;charset=UTF-8"
            );

            response.getWriter().println(
                    "<h2>Database error occurred.</h2>"
            );
        }
    }

    private boolean isValidStatus(String status) {

        return status.equals("APPLIED")
                || status.equals("SHORTLISTED")
                || status.equals("INTERVIEW")
                || status.equals("SELECTED")
                || status.equals("REJECTED");
    }

    private boolean isAllowedTransition(String current,
                                        String next) {

        if (current == null) {
            return false;
        }

        // No change
        if (current.equals(next)) {
            return true;
        }

        return switch (current) {

            case "APPLIED" ->
                    next.equals("SHORTLISTED")
                    || next.equals("REJECTED");

            case "SHORTLISTED" ->
                    next.equals("INTERVIEW")
                    || next.equals("REJECTED");

            case "INTERVIEW" ->
                    next.equals("SELECTED")
                    || next.equals("REJECTED");

            // Final states
            case "SELECTED",
                 "REJECTED" -> false;

            default -> false;
        };
    }
}