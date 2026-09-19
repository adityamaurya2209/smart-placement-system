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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/recruiter-jobs")
public class RecruiterJobsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
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
                       c.company_name,
                       COUNT(a.id) AS applicant_count
                FROM jobs j
                JOIN companies c
                    ON j.company_id = c.id
                LEFT JOIN applications a
                    ON j.id = a.job_id
                WHERE c.user_id = ?
                GROUP BY j.id,
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
                ORDER BY j.created_at DESC
                """;

        List<Job> jobs = new ArrayList<>();

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, userId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

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
                            resultSet.getBigDecimal(
                                    "minimum_cgpa"
                            )
                    );

                    job.setEligibleBranch(
                            resultSet.getString(
                                    "eligible_branch"
                            )
                    );

                    job.setRequiredSkills(
                            resultSet.getString(
                                    "required_skills"
                            )
                    );

                    job.setSalary(
                            resultSet.getString("salary")
                    );

                    job.setApplicationDeadline(
                            resultSet.getDate(
                                    "application_deadline"
                            )
                    );

                    job.setStatus(
                            resultSet.getString("status")
                    );

                    job.setCompanyName(
                            resultSet.getString(
                                    "company_name"
                            )
                    );

                    job.setApplicantCount(
                            resultSet.getInt(
                                    "applicant_count"
                            )
                    );

                    jobs.add(job);
                }
            }

            request.setAttribute("jobs", jobs);

            request.getRequestDispatcher(
                    "/recruiter-jobs.jsp"
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