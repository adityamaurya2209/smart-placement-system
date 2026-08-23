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

@WebServlet("/admin-interviews")
public class AdminInterviewsServlet extends HttpServlet {

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

        List<Interview> interviews = new ArrayList<>();

        String sql = """
                SELECT i.id,
                       i.application_id,
                       u.name AS student_name,
                       u.email AS student_email,
                       j.title AS job_title,
                       c.company_name,
                       i.interview_date,
                       i.interview_time,
                       i.mode,
                       i.meeting_link,
                       i.venue,
                       i.status
                FROM interviews i
                JOIN applications a
                  ON i.application_id = a.id
                JOIN students s
                  ON a.student_id = s.id
                JOIN users u
                  ON s.user_id = u.id
                JOIN jobs j
                  ON a.job_id = j.id
                JOIN companies c
                  ON j.company_id = c.id
                ORDER BY i.interview_date, i.interview_time
                """;

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                Interview interview = new Interview();

                interview.setId(
                        resultSet.getLong("id")
                );

                interview.setApplicationId(
                        resultSet.getLong("application_id")
                );

                interview.setStudentName(
                        resultSet.getString("student_name")
                );

                interview.setStudentEmail(
                        resultSet.getString("student_email")
                );

                interview.setJobTitle(
                        resultSet.getString("job_title")
                );

                interview.setCompanyName(
                        resultSet.getString("company_name")
                );

                interview.setInterviewDate(
                        resultSet.getDate("interview_date")
                );

                interview.setInterviewTime(
                        resultSet.getTime("interview_time")
                );

                interview.setMode(
                        resultSet.getString("mode")
                );

                interview.setMeetingLink(
                        resultSet.getString("meeting_link")
                );

                interview.setVenue(
                        resultSet.getString("venue")
                );

                interview.setStatus(
                        resultSet.getString("status")
                );

                interviews.add(interview);
            }

            request.setAttribute(
                    "interviews",
                    interviews
            );

            request.getRequestDispatcher(
                    "/admin-interviews.jsp"
            ).forward(request, response);

        } catch (Exception e) {

            e.printStackTrace();

            response.setContentType("text/html");

            response.getWriter().println(
                    "<h2>Database error occurred.</h2>"
            );
        }
    }

    public static class Interview {

        private long id;
        private long applicationId;
        private String studentName;
        private String studentEmail;
        private String jobTitle;
        private String companyName;
        private java.sql.Date interviewDate;
        private java.sql.Time interviewTime;
        private String mode;
        private String meetingLink;
        private String venue;
        private String status;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public long getApplicationId() {
            return applicationId;
        }

        public void setApplicationId(long applicationId) {
            this.applicationId = applicationId;
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

        public java.sql.Date getInterviewDate() {
            return interviewDate;
        }

        public void setInterviewDate(
                java.sql.Date interviewDate) {
            this.interviewDate = interviewDate;
        }

        public java.sql.Time getInterviewTime() {
            return interviewTime;
        }

        public void setInterviewTime(
                java.sql.Time interviewTime) {
            this.interviewTime = interviewTime;
        }

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public String getMeetingLink() {
            return meetingLink;
        }

        public void setMeetingLink(String meetingLink) {
            this.meetingLink = meetingLink;
        }

        public String getVenue() {
            return venue;
        }

        public void setVenue(String venue) {
            this.venue = venue;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}