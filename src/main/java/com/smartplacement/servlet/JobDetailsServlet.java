package com.smartplacement.servlet;

import com.smartplacement.model.Job;
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

@WebServlet("/job-details")
public class JobDetailsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null
                || session.getAttribute("userId") == null
                || !"STUDENT".equals(session.getAttribute("role"))) {

            response.sendRedirect("login.html");
            return;
        }

        String jobIdParameter = request.getParameter("id");

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

        String sql = """
                SELECT j.id,
                       j.title,
                       j.description,
                       j.location,
                       j.minimum_cgpa,
                       j.eligible_branch,
                       j.required_skills,
                       j.salary,
                       j.application_deadline,
                       j.status,
                       c.company_name
                FROM jobs j
                JOIN companies c ON j.company_id = c.id
                WHERE j.id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, jobId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (!resultSet.next()) {

                    response.setContentType("text/html");

                    response.getWriter().println(
                            "<h2>Job not found.</h2>"
                    );

                    response.getWriter().println(
                            "<a href='jobs'>Back to Jobs</a>"
                    );

                    return;
                }

                Job job = new Job();

                job.setId(
                        resultSet.getLong("id")
                );

                job.setTitle(
                        resultSet.getString("title")
                );

                job.setDescription(
                        resultSet.getString("description")
                );

                job.setLocation(
                        resultSet.getString("location")
                );

                job.setMinimumCgpa(
                        resultSet.getBigDecimal("minimum_cgpa")
                );

                job.setEligibleBranch(
                        resultSet.getString("eligible_branch")
                );

                job.setRequiredSkills(
                        resultSet.getString("required_skills")
                );

                job.setSalary(
                        resultSet.getString("salary")
                );

                job.setApplicationDeadline(
                        resultSet.getDate("application_deadline")
                );

                job.setStatus(
                        resultSet.getString("status")
                );

                job.setCompanyName(
                        resultSet.getString("company_name")
                );

                request.setAttribute("job", job);

                request.getRequestDispatcher(
                        "/job-details.jsp"
                ).forward(request, response);
            }

        } catch (Exception e) {

            e.printStackTrace();

            response.setContentType("text/html");

            response.getWriter().println(
                    "<h2>Database error occurred.</h2>"
            );
        }
    }
}