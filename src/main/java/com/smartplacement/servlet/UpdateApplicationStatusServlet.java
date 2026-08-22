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

@WebServlet("/update-application-status")
public class UpdateApplicationStatusServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        // Check recruiter login
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login.html");
            return;
        }

        long recruiterUserId =
                (Long) session.getAttribute("userId");

        String applicationIdParameter =
                request.getParameter("applicationId");

        String status =
                request.getParameter("status");

        String jobIdParameter =
                request.getParameter("jobId");

        if (applicationIdParameter == null ||
            status == null ||
            jobIdParameter == null) {

            response.sendRedirect("recruiter-dashboard");
            return;
        }

        long applicationId;
        long jobId;

        try {

            applicationId =
                    Long.parseLong(applicationIdParameter);

            jobId =
                    Long.parseLong(jobIdParameter);

        } catch (NumberFormatException e) {

            response.sendRedirect("recruiter-dashboard");
            return;
        }

        // Only allow valid application statuses
        if (!status.equals("SHORTLISTED") &&
            !status.equals("REJECTED") &&
            !status.equals("INTERVIEW") &&
            !status.equals("SELECTED")) {

            response.sendRedirect(
                    "applicants?jobId=" + jobId
            );

            return;
        }

        String sql = """
                UPDATE applications a
                JOIN jobs j ON a.job_id = j.id
                JOIN companies c ON j.company_id = c.id
                SET a.status = ?
                WHERE a.id = ?
                  AND a.job_id = ?
                  AND c.user_id = ?
                """;

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, status);
            statement.setLong(2, applicationId);
            statement.setLong(3, jobId);
            statement.setLong(4, recruiterUserId);

            statement.executeUpdate();

            response.sendRedirect(
                    "applicants?jobId=" + jobId
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.setContentType("text/html");

            response.getWriter().println(
                    "<h2>Database error occurred.</h2>"
            );
        }
    }
}