package com.smartplacement.servlet;
import com.smartplacement.model.Job;

import com.smartplacement.util.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/jobs")
public class JobsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

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
                WHERE j.status = 'OPEN'
                ORDER BY j.created_at DESC
                """;

        List<Job> jobs = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Job job = new Job();

                job.setId(resultSet.getLong("id"));
                job.setTitle(resultSet.getString("title"));
                job.setDescription(resultSet.getString("description"));
                job.setLocation(resultSet.getString("location"));
                job.setMinimumCgpa(resultSet.getBigDecimal("minimum_cgpa"));
                job.setEligibleBranch(resultSet.getString("eligible_branch"));
                job.setRequiredSkills(resultSet.getString("required_skills"));
                job.setSalary(resultSet.getString("salary"));
                job.setApplicationDeadline(
                        resultSet.getDate("application_deadline")
                );
                job.setStatus(resultSet.getString("status"));
                job.setCompanyName(resultSet.getString("company_name"));

                jobs.add(job);
            }

            request.setAttribute("jobs", jobs);

            request.getRequestDispatcher("/jobs.jsp")
                   .forward(request, response);

        } catch (Exception e) {

            e.printStackTrace();

            response.setContentType("text/html");

            response.getWriter().println(
                "<h2>Database error occurred.</h2>"
            );
        }
    }
}