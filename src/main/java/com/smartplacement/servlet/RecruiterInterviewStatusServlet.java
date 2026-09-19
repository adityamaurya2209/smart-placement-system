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

@WebServlet("/recruiter-interview-status")
public class RecruiterInterviewStatusServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || !"RECRUITER".equals(session.getAttribute("role"))) {
            response.sendRedirect(request.getContextPath() + "/login.html");
            return;
        }

        String interviewIdParam = request.getParameter("interviewId");
        String status = request.getParameter("status");

        if (interviewIdParam == null || interviewIdParam.trim().isEmpty()
                || status == null || status.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath()
                    + "/recruiter-interviews?error=invalid-data");
            return;
        }

        long interviewId;

        try {
            interviewId = Long.parseLong(interviewIdParam);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath()
                    + "/recruiter-interviews?error=invalid-id");
            return;
        }

        status = status.trim().toUpperCase();

        if (!"COMPLETED".equals(status) && !"CANCELLED".equals(status)) {
            response.sendRedirect(request.getContextPath()
                    + "/recruiter-interviews?error=invalid-status");
            return;
        }

        long recruiterUserId = (Long) session.getAttribute("userId");

        String verifySql =
                "SELECT i.status " +
                "FROM interviews i " +
                "JOIN applications a ON i.application_id = a.id " +
                "JOIN jobs j ON a.job_id = j.id " +
                "JOIN companies c ON j.company_id = c.id " +
                "WHERE i.id = ? AND c.user_id = ?";

        String updateSql =
                "UPDATE interviews " +
                "SET status = ? " +
                "WHERE id = ? AND status = 'SCHEDULED'";

        try (Connection conn = DBConnection.getConnection()) {

            String currentStatus = null;

            try (PreparedStatement ps = conn.prepareStatement(verifySql)) {

                ps.setLong(1, interviewId);
                ps.setLong(2, recruiterUserId);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        currentStatus = rs.getString("status");
                    }
                }
            }

            if (currentStatus == null) {
                response.sendRedirect(request.getContextPath()
                        + "/recruiter-interviews?error=not-found");
                return;
            }

            if (!"SCHEDULED".equals(currentStatus)) {
                response.sendRedirect(request.getContextPath()
                        + "/recruiter-interviews?error=already-processed");
                return;
            }

            try (PreparedStatement ps = conn.prepareStatement(updateSql)) {

                ps.setString(1, status);
                ps.setLong(2, interviewId);

                int updated = ps.executeUpdate();

                if (updated == 1) {

                    if ("COMPLETED".equals(status)) {
                        response.sendRedirect(request.getContextPath()
                                + "/recruiter-interviews?completed=true");
                    } else {
                        response.sendRedirect(request.getContextPath()
                                + "/recruiter-interviews?cancelled=true");
                    }

                } else {
                    response.sendRedirect(request.getContextPath()
                            + "/recruiter-interviews?error=update-failed");
                }
            }

        } catch (Exception e) {

            e.printStackTrace();

            response.sendRedirect(request.getContextPath()
                    + "/recruiter-interviews?error=database");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED,
                "POST method required");
    }
}
