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

@WebServlet("/admin-applications")
public class AdminApplicationsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // Admin authentication
        if (session == null ||
                session.getAttribute("userId") == null ||
                !"ADMIN".equalsIgnoreCase(
                        String.valueOf(
                                session.getAttribute("role")))) {

            response.sendRedirect("login.html");
            return;
        }

        List<Application> applications =
                new ArrayList<>();

        String sql = """
                SELECT a.id,
                       a.student_id,
                       u.name AS student_name,
                       u.email AS student_email,
                       a.job_id,
                       j.title AS job_title,
                       c.company_name,
                       a.application_date,
                       a.status,
                       a.match_score
                FROM applications a
                JOIN students s
                ON a.student_id = s.id
                JOIN users u
                ON s.user_id = u.id
                JOIN jobs j
                ON a.job_id = j.id
                JOIN companies c
                ON j.company_id = c.id
                ORDER BY a.id
                """;

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                Application application =
                        new Application();

                application.setId(
                        resultSet.getLong("id")
                );

                application.setStudentId(
                        resultSet.getLong("student_id")
                );

                application.setStudentName(
                        resultSet.getString("student_name")
                );

                application.setStudentEmail(
                        resultSet.getString("student_email")
                );

                application.setJobId(
                        resultSet.getLong("job_id")
                );

                application.setJobTitle(
                        resultSet.getString("job_title")
                );

                application.setCompanyName(
                        resultSet.getString("company_name")
                );

                application.setApplicationDate(
                        resultSet.getTimestamp(
                                "application_date")
                );

                application.setStatus(
                        resultSet.getString("status")
                );

                application.setMatchScore(
                        resultSet.getBigDecimal("match_score")
                );

                applications.add(application);
            }

            request.setAttribute(
                    "applications",
                    applications
            );

            request.getRequestDispatcher(
                    "/admin-applications.jsp"
            ).forward(request, response);

        } catch (Exception e) {

            e.printStackTrace();

            response.setContentType("text/html");

            response.getWriter().println(
                    "<h2>Database error occurred.</h2>"
            );
        }
    }

    public static class Application {

        private long id;
        private long studentId;
        private String studentName;
        private String studentEmail;
        private long jobId;
        private String jobTitle;
        private String companyName;
        private java.sql.Timestamp applicationDate;
        private String status;
        private java.math.BigDecimal matchScore;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public long getStudentId() {
            return studentId;
        }

        public void setStudentId(long studentId) {
            this.studentId = studentId;
        }

        public String getStudentName() {
            return studentName;
        }

        public void setStudentName(String studentName) {
            this.studentName = studentName;
        }

        public String getStudentEmail() {
            return studentEmail;
        }

        public void setStudentEmail(String studentEmail) {
            this.studentEmail = studentEmail;
        }

        public long getJobId() {
            return jobId;
        }

        public void setJobId(long jobId) {
            this.jobId = jobId;
        }

        public String getJobTitle() {
            return jobTitle;
        }

        public void setJobTitle(String jobTitle) {
            this.jobTitle = jobTitle;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public java.sql.Timestamp getApplicationDate() {
            return applicationDate;
        }

        public void setApplicationDate(
                java.sql.Timestamp applicationDate) {
            this.applicationDate = applicationDate;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public java.math.BigDecimal getMatchScore() {
            return matchScore;
        }

        public void setMatchScore(
                java.math.BigDecimal matchScore) {
            this.matchScore = matchScore;
        }
    }
}