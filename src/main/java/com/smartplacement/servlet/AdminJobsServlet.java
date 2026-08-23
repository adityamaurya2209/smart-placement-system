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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/admin-jobs")
public class AdminJobsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // Admin authentication
        if (session == null ||
                session.getAttribute("userId") == null ||
                !"ADMIN".equalsIgnoreCase(
                        String.valueOf(session.getAttribute("role")))) {

            response.sendRedirect("login.html");
            return;
        }

        List<Job> jobs = new ArrayList<>();

        String sql = """
                SELECT j.id,
                       j.company_id,
                       c.company_name,
                       j.title,
                       j.description,
                       j.location,
                       j.minimum_cgpa,
                       j.eligible_branch,
                       j.required_skills,
                       j.salary,
                       j.application_deadline,
                       j.status,
                       j.created_at
                FROM jobs j
                JOIN companies c
                  ON j.company_id = c.id
                ORDER BY j.id
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                Job job = new Job();

                job.setId(resultSet.getLong("id"));
                job.setCompanyId(
                        resultSet.getLong("company_id")
                );
                job.setCompanyName(
                        resultSet.getString("company_name")
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
                job.setCreatedAt(
                        resultSet.getTimestamp("created_at")
                );

                jobs.add(job);
            }

            request.setAttribute("jobs", jobs);

            request.getRequestDispatcher(
                    "/admin-jobs.jsp"
            ).forward(request, response);

        } catch (Exception e) {

            e.printStackTrace();

            response.setContentType("text/html");

            response.getWriter().println(
                    "<h2>Database error occurred.</h2>"
            );
        }
    }

    public static class Job {

        private long id;
        private long companyId;
        private String companyName;
        private String title;
        private String description;
        private String location;
        private java.math.BigDecimal minimumCgpa;
        private String eligibleBranch;
        private String requiredSkills;
        private String salary;
        private java.sql.Date applicationDeadline;
        private String status;
        private java.sql.Timestamp createdAt;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public long getCompanyId() {
            return companyId;
        }

        public void setCompanyId(long companyId) {
            this.companyId = companyId;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public java.math.BigDecimal getMinimumCgpa() {
            return minimumCgpa;
        }

        public void setMinimumCgpa(
                java.math.BigDecimal minimumCgpa) {
            this.minimumCgpa = minimumCgpa;
        }

        public String getEligibleBranch() {
            return eligibleBranch;
        }

        public void setEligibleBranch(String eligibleBranch) {
            this.eligibleBranch = eligibleBranch;
        }

        public String getRequiredSkills() {
            return requiredSkills;
        }

        public void setRequiredSkills(String requiredSkills) {
            this.requiredSkills = requiredSkills;
        }

        public String getSalary() {
            return salary;
        }

        public void setSalary(String salary) {
            this.salary = salary;
        }

        public java.sql.Date getApplicationDeadline() {
            return applicationDeadline;
        }

        public void setApplicationDeadline(
                java.sql.Date applicationDeadline) {
            this.applicationDeadline =
                    applicationDeadline;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public java.sql.Timestamp getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(
                java.sql.Timestamp createdAt) {
            this.createdAt = createdAt;
        }
    }
}