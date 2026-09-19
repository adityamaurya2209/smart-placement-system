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
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@WebServlet("/schedule-interview")
public class ScheduleInterviewServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || !"RECRUITER".equals(session.getAttribute("role"))) {
            response.sendRedirect(request.getContextPath() + "/login.html");
            return;
        }

        String applicationId = request.getParameter("applicationId");

        if (applicationId == null || applicationId.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/applicants?error=invalid-id");
            return;
        }

        try {
            Long.parseLong(applicationId);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/applicants?error=invalid-id");
            return;
        }

        request.setAttribute("applicationId", applicationId);
        request.getRequestDispatcher("/schedule-interview.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || !"RECRUITER".equals(session.getAttribute("role"))) {
            response.sendRedirect(request.getContextPath() + "/login.html");
            return;
        }

        String applicationIdParam = request.getParameter("applicationId");
        String interviewDateParam = request.getParameter("interviewDate");
        String interviewTimeParam = request.getParameter("interviewTime");
        String mode = request.getParameter("mode");
        String meetingLink = request.getParameter("meetingLink");
        String venue = request.getParameter("venue");

        if (applicationIdParam == null || applicationIdParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/applicants?error=invalid-id");
            return;
        }

        long applicationId;

        try {
            applicationId = Long.parseLong(applicationIdParam);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/applicants?error=invalid-id");
            return;
        }

        mode = mode == null ? "" : mode.trim().toUpperCase();
        meetingLink = meetingLink == null ? "" : meetingLink.trim();
        venue = venue == null ? "" : venue.trim();

        if (interviewDateParam == null || interviewDateParam.trim().isEmpty()
                || interviewTimeParam == null || interviewTimeParam.trim().isEmpty()) {
            redirectError(response, applicationIdParam, "invalid-date-time");
            return;
        }

        if (!"ONLINE".equals(mode) && !"OFFLINE".equals(mode)) {
            redirectError(response, applicationIdParam, "invalid-mode");
            return;
        }

        Date interviewDate;
        Time interviewTime;

        try {
            interviewDate = Date.valueOf(interviewDateParam);
            interviewTime = Time.valueOf(interviewTimeParam.length() == 5
                    ? interviewTimeParam + ":00"
                    : interviewTimeParam);
        } catch (IllegalArgumentException e) {
            redirectError(response, applicationIdParam, "invalid-date-time");
            return;
        }

        LocalDate date = interviewDate.toLocalDate();
        LocalTime time = interviewTime.toLocalTime();

        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime interviewDateTime = LocalDateTime.of(date, time);

        if (date.isBefore(today)) {
            redirectError(response, applicationIdParam, "past-date");
            return;
        }

        if (interviewDateTime.isBefore(now)) {
            redirectError(response, applicationIdParam, "past-time");
            return;
        }

        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(18, 0);

        if (time.isBefore(startTime) || time.isAfter(endTime)) {
            redirectError(response, applicationIdParam, "outside-hours");
            return;
        }

        if ("ONLINE".equals(mode) && meetingLink.isEmpty()) {
            redirectError(response, applicationIdParam, "online-link");
            return;
        }

        if ("OFFLINE".equals(mode) && venue.isEmpty()) {
            redirectError(response, applicationIdParam, "offline-venue");
            return;
        }

        if ("ONLINE".equals(mode)) {
            venue = "";
        } else {
            meetingLink = "";
        }

        Object userIdObject = session.getAttribute("userId");

        if (userIdObject == null) {
            response.sendRedirect(request.getContextPath() + "/login.html");
            return;
        }

        long recruiterUserId;

        try {
            recruiterUserId = Long.parseLong(userIdObject.toString());
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/login.html");
            return;
        }

        String verifyApplicationSql =
                "SELECT a.id " +
                "FROM applications a " +
                "JOIN jobs j ON a.job_id = j.id " +
                "JOIN companies c ON j.company_id = c.id " +
                "WHERE a.id = ? AND c.user_id = ?";

        String latestInterviewSql =
                "SELECT status " +
                "FROM interviews " +
                "WHERE application_id = ? " +
                "ORDER BY id DESC " +
                "LIMIT 1";

        String insertInterviewSql =
                "INSERT INTO interviews " +
                "(application_id, interview_date, interview_time, mode, meeting_link, venue, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, 'SCHEDULED')";

        String updateApplicationSql =
                "UPDATE applications " +
                "SET status = 'INTERVIEW' " +
                "WHERE id = ?";

        try (Connection con = DBConnection.getConnection()) {

            /*
             * Verify that this application belongs to the logged-in recruiter.
             */
            try (PreparedStatement ps = con.prepareStatement(verifyApplicationSql)) {

                ps.setLong(1, applicationId);
                ps.setLong(2, recruiterUserId);

                try (ResultSet rs = ps.executeQuery()) {

                    if (!rs.next()) {
                        redirectError(response, applicationIdParam, "unauthorized");
                        return;
                    }
                }
            }

            /*
             * Check the latest interview.
             *
             * SCHEDULED  -> do not create duplicate.
             * CANCELLED  -> new interview allowed.
             * COMPLETED  -> new interview allowed.
             */
            try (PreparedStatement ps = con.prepareStatement(latestInterviewSql)) {

                ps.setLong(1, applicationId);

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {

                        String status = rs.getString("status");

                        if ("SCHEDULED".equals(status)) {
                            redirectError(
                                    response,
                                    applicationIdParam,
                                    "already-scheduled"
                            );
                            return;
                        }
                    }
                }
            }

            con.setAutoCommit(false);

            try {

                try (PreparedStatement ps = con.prepareStatement(insertInterviewSql)) {

                    ps.setLong(1, applicationId);
                    ps.setDate(2, interviewDate);
                    ps.setTime(3, interviewTime);
                    ps.setString(4, mode);
                    ps.setString(5, meetingLink.isEmpty() ? null : meetingLink);
                    ps.setString(6, venue.isEmpty() ? null : venue);

                    ps.executeUpdate();
                }

                try (PreparedStatement ps = con.prepareStatement(updateApplicationSql)) {

                    ps.setLong(1, applicationId);
                    ps.executeUpdate();
                }

                con.commit();

                response.sendRedirect(
                        request.getContextPath()
                                + "/recruiter-interviews?success=true"
                );

            } catch (Exception e) {

                try {
                    con.rollback();
                } catch (Exception rollbackException) {
                    rollbackException.printStackTrace();
                }

                throw e;
            }

        } catch (Exception e) {

            e.printStackTrace();

            redirectError(
                    response,
                    applicationIdParam,
                    "database"
            );
        }
    }

    private void redirectError(
            HttpServletResponse response,
            String applicationId,
            String error
    ) throws IOException {

        response.sendRedirect(
                "schedule-interview?applicationId="
                        + applicationId
                        + "&error="
                        + error
        );
    }
}
