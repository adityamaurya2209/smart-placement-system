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

@WebServlet("/apply-job")
public class ApplyJobServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        // Check login session
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login.html");
            return;
        }

        // Get logged-in user's ID
        long userId = (Long) session.getAttribute("userId");

        // Get job ID from the form
        String jobIdParameter = request.getParameter("jobId");

        if (jobIdParameter == null || jobIdParameter.isEmpty()) {
            response.sendRedirect("jobs");
            return;
        }

        long jobId;

        try {
            jobId = Long.parseLong(jobIdParameter);
        } catch (NumberFormatException e) {
            response.sendRedirect("jobs");
            return;
        }

        String studentQuery =
                "SELECT id FROM students WHERE user_id = ?";

        String duplicateQuery =
                "SELECT id FROM applications " +
                "WHERE student_id = ? AND job_id = ?";

        String insertQuery =
                "INSERT INTO applications " +
                "(student_id, job_id) VALUES (?, ?)";

        try (Connection connection = DBConnection.getConnection()) {

            // Find student ID
            long studentId;

            try (PreparedStatement statement =
                         connection.prepareStatement(studentQuery)) {

                statement.setLong(1, userId);

                try (ResultSet resultSet = statement.executeQuery()) {

                    if (!resultSet.next()) {
                        response.setContentType("text/html");
                        response.getWriter().println(
                                "<h2>Student profile not found.</h2>"
                        );
                        return;
                    }

                    studentId = resultSet.getLong("id");
                }
            }

            // Check if already applied
            try (PreparedStatement statement =
                         connection.prepareStatement(duplicateQuery)) {

                statement.setLong(1, studentId);
                statement.setLong(2, jobId);

                try (ResultSet resultSet = statement.executeQuery()) {

                    if (resultSet.next()) {

                        response.setContentType("text/html");

                        response.getWriter().println(
                                "<h2>You have already applied for this job.</h2>"
                        );

                        response.getWriter().println(
                                "<a href='jobs'>Back to Jobs</a>"
                        );

                        return;
                    }
                }
            }

            // Insert application
            try (PreparedStatement statement =
                         connection.prepareStatement(insertQuery)) {

                statement.setLong(1, studentId);
                statement.setLong(2, jobId);

                statement.executeUpdate();
            }

            // Application successful
            response.setContentType("text/html");

            response.getWriter().println(
                    "<h1>Application Submitted Successfully!</h1>"
            );

            response.getWriter().println(
                    "<p>Your application has been submitted.</p>"
            );

            response.getWriter().println(
                    "<a href='jobs'>Back to Jobs</a>"
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