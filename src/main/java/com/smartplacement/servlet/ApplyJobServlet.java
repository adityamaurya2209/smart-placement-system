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

        HttpSession session = request.getSession(false);

        // Check whether a student is logged in
        if (session == null
                || session.getAttribute("userId") == null
                || !"STUDENT".equals(session.getAttribute("role"))) {

            response.sendRedirect("login.html");
            return;
        }

        long userId = (Long) session.getAttribute("userId");

        String jobIdParameter = request.getParameter("jobId");

        if (jobIdParameter == null || jobIdParameter.isBlank()) {
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

        String jobQuery =
                "SELECT id FROM jobs WHERE id = ? AND status = 'OPEN'";

        String duplicateQuery =
                "SELECT id FROM applications " +
                "WHERE student_id = ? AND job_id = ?";

        String insertQuery =
                "INSERT INTO applications (student_id, job_id) " +
                "VALUES (?, ?)";

        try (Connection connection = DBConnection.getConnection()) {

            long studentId;

            // Find the student's database ID
            try (PreparedStatement statement =
                         connection.prepareStatement(studentQuery)) {

                statement.setLong(1, userId);

                try (ResultSet resultSet =
                             statement.executeQuery()) {

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

            // Check whether the job exists and is still open
            try (PreparedStatement statement =
                         connection.prepareStatement(jobQuery)) {

                statement.setLong(1, jobId);

                try (ResultSet resultSet =
                             statement.executeQuery()) {

                    if (!resultSet.next()) {

                        response.setContentType("text/html");

                        response.getWriter().println(
                                "<h2>This job is not available for application.</h2>"
                        );

                        response.getWriter().println(
                                "<br><a href='jobs'>Back to Jobs</a>"
                        );

                        return;
                    }
                }
            }

            // Check whether the student has already applied
            try (PreparedStatement statement =
                         connection.prepareStatement(duplicateQuery)) {

                statement.setLong(1, studentId);
                statement.setLong(2, jobId);

                try (ResultSet resultSet =
                             statement.executeQuery()) {

                    if (resultSet.next()) {

                        response.setContentType("text/html");

                        response.getWriter().println(
                                "<h2>You have already applied for this job.</h2>"
                        );

                        response.getWriter().println(
                                "<br><a href='my-applications'>View My Applications</a>"
                        );

                        return;
                    }
                }
            }

            // Submit the application
            try (PreparedStatement statement =
                         connection.prepareStatement(insertQuery)) {

                statement.setLong(1, studentId);
                statement.setLong(2, jobId);

                statement.executeUpdate();
            }

            // Redirect after successful application
            response.sendRedirect(
                    "my-applications?success=true"
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