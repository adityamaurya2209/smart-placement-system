package com.smartplacement.servlet;

import com.smartplacement.model.Application;
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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/applicants")
public class ApplicantsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
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

        // Get job ID
        String jobIdParameter = request.getParameter("jobId");

        if (jobIdParameter == null || jobIdParameter.isEmpty()) {
            response.sendRedirect("recruiter-dashboard");
            return;
        }

        long jobId;

        try {
            jobId = Long.parseLong(jobIdParameter);
        } catch (NumberFormatException e) {
            response.sendRedirect("recruiter-dashboard");
            return;
        }

        String sql = """
                SELECT
                    a.id,
                    u.name,
                    u.email,
                    s.roll_number,
                    s.branch,
                    s.cgpa,
                    s.phone,
                    s.skills,
                    s.certifications,
                    s.resume_path,
                    a.application_date,
                    a.status,
                    a.match_score
                FROM applications a
                JOIN students s ON a.student_id = s.id
                JOIN users u ON s.user_id = u.id
                JOIN jobs j ON a.job_id = j.id
                JOIN companies c ON j.company_id = c.id
                WHERE a.job_id = ?
                  AND c.user_id = ?
                ORDER BY a.application_date DESC
                """;

        List<Application> applications = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, jobId);
            statement.setLong(2, recruiterUserId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {

                    Application application = new Application();

                    application.setId(
                            resultSet.getLong("id")
                    );

                    application.setStudentName(
                            resultSet.getString("name")
                    );

                    application.setStudentEmail(
                            resultSet.getString("email")
                    );

                    application.setRollNumber(
                            resultSet.getString("roll_number")
                    );

                    application.setBranch(
                            resultSet.getString("branch")
                    );

                    application.setCgpa(
                            resultSet.getBigDecimal("cgpa")
                    );

                    application.setPhone(
                            resultSet.getString("phone")
                    );

                    application.setSkills(
                            resultSet.getString("skills")
                    );

                    application.setCertifications(
                            resultSet.getString("certifications")
                    );

                    application.setResumePath(
                            resultSet.getString("resume_path")
                    );

                    application.setApplicationDate(
                            resultSet.getTimestamp("application_date")
                    );

                    application.setStatus(
                            resultSet.getString("status")
                    );

                    application.setMatchScore(
                            resultSet.getBigDecimal("match_score")
                    );

                    applications.add(application);
                }
            }

            request.setAttribute("applications", applications);
            request.setAttribute("jobId", jobId);

            request.getRequestDispatcher(
                    "/applicants.jsp"
            ).forward(request, response);

        } catch (Exception e) {

            e.printStackTrace();

            response.setContentType("text/html");

            response.getWriter().println(
                    "<h2>Database error occurred.</h2>"
            );
        }
    }
}