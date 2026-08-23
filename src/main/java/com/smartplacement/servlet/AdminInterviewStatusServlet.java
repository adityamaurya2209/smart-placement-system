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

@WebServlet("/admin-update-interview-status")
public class AdminInterviewStatusServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        // Check admin session
        HttpSession session = request.getSession(false);

        if (session == null ||
                session.getAttribute("userId") == null ||
                !"ADMIN".equalsIgnoreCase(
                        String.valueOf(
                                session.getAttribute("role")))) {

            response.sendRedirect("login.html");
            return;
        }

        // Get interview ID
        String id = request.getParameter("id");

        // Get new status
        String status = request.getParameter("status");

        System.out.println(
                "Interview ID received: " + id
        );

        System.out.println(
                "New interview status: " + status
        );

        if (id == null || id.isBlank() ||
                status == null || status.isBlank()) {

            response.sendRedirect(
                    "admin-interviews"
            );

            return;
        }

        long interviewId;

        try {

            interviewId = Long.parseLong(id);

        } catch (NumberFormatException e) {

            response.sendRedirect(
                    "admin-interviews"
            );

            return;
        }

        // Allow only valid interview statuses
        if (!status.equals("SCHEDULED") &&
                !status.equals("COMPLETED") &&
                !status.equals("CANCELLED")) {

            response.sendRedirect(
                    "admin-interviews"
            );

            return;
        }

        String sql = """
                UPDATE interviews
                SET status = ?
                WHERE id = ?
                """;

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, status);
            statement.setLong(2, interviewId);

            int rowsUpdated =
                    statement.executeUpdate();

            if (rowsUpdated > 0) {

                System.out.println(
                        "Interview status updated successfully."
                );

                response.sendRedirect(
                        "admin-interview-details?id="
                                + interviewId
                                + "&success=updated"
                );

            } else {

                System.out.println(
                        "No interview found with ID: "
                                + interviewId
                );

                response.sendRedirect(
                        "admin-interviews"
                );
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
