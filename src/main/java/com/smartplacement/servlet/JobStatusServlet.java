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

@WebServlet("/job-status")
public class JobStatusServlet extends HttpServlet {

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

        long userId = (Long) session.getAttribute("userId");

        String idParam = request.getParameter("id");
        String status = request.getParameter("status");

        long jobId;

        try {
            jobId = Long.parseLong(idParam);
        } catch (Exception e) {
            response.sendRedirect("recruiter-jobs?error=invalid-id");
            return;
        }

        if (!"OPEN".equals(status) && !"CLOSED".equals(status)) {
            response.sendRedirect("recruiter-jobs?error=invalid-status");
            return;
        }

        String verifySql = """
                SELECT j.status
                FROM jobs j
                JOIN companies c ON j.company_id = c.id
                WHERE j.id = ?
                  AND c.user_id = ?
                """;

        String updateSql = """
                UPDATE jobs j
                JOIN companies c ON j.company_id = c.id
                SET j.status = ?
                WHERE j.id = ?
                  AND c.user_id = ?
                """;

        try (Connection connection = DBConnection.getConnection()) {

            try (PreparedStatement statement =
                         connection.prepareStatement(verifySql)) {

                statement.setLong(1, jobId);
                statement.setLong(2, userId);

                try (ResultSet rs = statement.executeQuery()) {

                    if (!rs.next()) {
                        response.sendRedirect(
                                "recruiter-jobs?error=not-found"
                        );
                        return;
                    }
                }
            }

            try (PreparedStatement statement =
                         connection.prepareStatement(updateSql)) {

                statement.setString(1, status);
                statement.setLong(2, jobId);
                statement.setLong(3, userId);

                int updated = statement.executeUpdate();

                if (updated == 1) {

                    if ("CLOSED".equals(status)) {
                        response.sendRedirect(
                                "recruiter-jobs?closed=true"
                        );
                    } else {
                        response.sendRedirect(
                                "recruiter-jobs?reopened=true"
                        );
                    }

                } else {
                    response.sendRedirect(
                            "recruiter-jobs?error=update-failed"
                    );
                }
            }

        } catch (Exception e) {

            e.printStackTrace();

            response.sendRedirect(
                    "recruiter-jobs?error=database"
            );
        }
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.sendError(
                HttpServletResponse.SC_METHOD_NOT_ALLOWED,
                "POST method required"
        );
    }
}
