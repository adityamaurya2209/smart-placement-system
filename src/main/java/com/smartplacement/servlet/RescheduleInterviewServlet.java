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
import java.time.LocalTime;

@WebServlet("/reschedule-interview")
public class RescheduleInterviewServlet extends HttpServlet {

    private boolean isRecruiter(HttpSession session) {
        return session != null
                && session.getAttribute("userId") != null
                && "RECRUITER".equals(session.getAttribute("role"));
    }

    @Override
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (!isRecruiter(session)) {
            response.sendRedirect("login.html");
            return;
        }

        String interviewId = request.getParameter("interviewId");

        if (interviewId == null || interviewId.trim().isEmpty()) {
            response.sendRedirect(
                    "recruiter-interviews?error=invalid-id"
            );
            return;
        }

        try {
            long id = Long.parseLong(interviewId);

            request.setAttribute("interviewId", id);

            request.getRequestDispatcher("/reschedule-interview.jsp")
                    .forward(request, response);

        } catch (NumberFormatException e) {

            response.sendRedirect(
                    "recruiter-interviews?error=invalid-id"
            );
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (!isRecruiter(session)) {
            response.sendRedirect("login.html");
            return;
        }

        String interviewIdParam = request.getParameter("interviewId");
        String interviewDateParam = request.getParameter("interviewDate");
        String interviewTimeParam = request.getParameter("interviewTime");
        String mode = request.getParameter("mode");
        String meetingLink = request.getParameter("meetingLink");
        String venue = request.getParameter("venue");

        long interviewId;

        try {
            interviewId = Long.parseLong(interviewIdParam);
        } catch (Exception e) {
            response.sendRedirect(
                    "recruiter-interviews?error=invalid-id"
            );
            return;
        }

        if (interviewDateParam == null
                || interviewDateParam.trim().isEmpty()
                || interviewTimeParam == null
                || interviewTimeParam.trim().isEmpty()
                || mode == null
                || mode.trim().isEmpty()) {

            redirectError(response, interviewId, "missing");
            return;
        }

        if (!"ONLINE".equals(mode)
                && !"OFFLINE".equals(mode)) {

            redirectError(response, interviewId, "invalid-mode");
            return;
        }

        LocalDate date;
        LocalTime time;

        try {
            date = LocalDate.parse(interviewDateParam);
            time = LocalTime.parse(interviewTimeParam);
        } catch (Exception e) {
            redirectError(response, interviewId, "invalid-date-time");
            return;
        }

        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        if (date.isBefore(today)) {
            redirectError(response, interviewId, "past-date");
            return;
        }

        if (date.isEqual(today) && !time.isAfter(now)) {
            redirectError(response, interviewId, "past-time");
            return;
        }

        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(18, 0);

        if (time.isBefore(startTime) || time.isAfter(endTime)) {
            redirectError(response, interviewId, "outside-hours");
            return;
        }

        if ("ONLINE".equals(mode)
                && (meetingLink == null
                || meetingLink.trim().isEmpty())) {

            redirectError(response, interviewId, "online-link");
            return;
        }

        if ("OFFLINE".equals(mode)
                && (venue == null
                || venue.trim().isEmpty())) {

            redirectError(response, interviewId, "offline-venue");
            return;
        }

        try {
            long recruiterUserId =
                    ((Number) session.getAttribute("userId")).longValue();

            try (Connection con = DBConnection.getConnection()) {

                con.setAutoCommit(false);

                try {

                    /*
                     * Get application ID and verify ownership.
                     */
                    String findSql =
                            "SELECT i.application_id, i.status " +
                            "FROM interviews i " +
                            "JOIN applications a ON i.application_id = a.id " +
                            "JOIN jobs j ON a.job_id = j.id " +
                            "JOIN companies c ON j.company_id = c.id " +
                            "WHERE i.id = ? AND c.user_id = ?";

                    long applicationId;
                    String currentStatus;

                    try (PreparedStatement ps =
                                 con.prepareStatement(findSql)) {

                        ps.setLong(1, interviewId);
                        ps.setLong(2, recruiterUserId);

                        try (ResultSet rs = ps.executeQuery()) {

                            if (!rs.next()) {
                                con.rollback();

                                response.sendRedirect(
                                        "recruiter-interviews?error=unauthorized"
                                );
                                return;
                            }

                            applicationId =
                                    rs.getLong("application_id");

                            currentStatus =
                                    rs.getString("status");
                        }
                    }

                    /*
                     * Only SCHEDULED interviews can be rescheduled.
                     */
                    if (!"SCHEDULED".equals(currentStatus)) {

                        con.rollback();

                        response.sendRedirect(
                                "recruiter-interviews?error=not-scheduled"
                        );
                        return;
                    }

                    /*
                     * Cancel old interview.
                     */
                    String cancelSql =
                            "UPDATE interviews " +
                            "SET status = 'CANCELLED' " +
                            "WHERE id = ?";

                    try (PreparedStatement ps =
                                 con.prepareStatement(cancelSql)) {

                        ps.setLong(1, interviewId);
                        ps.executeUpdate();
                    }

                    /*
                     * Create new interview.
                     */
                    String insertSql =
                            "INSERT INTO interviews " +
                            "(application_id, interview_date, " +
                            "interview_time, mode, meeting_link, " +
                            "venue, status) " +
                            "VALUES (?, ?, ?, ?, ?, ?, 'SCHEDULED')";

                    try (PreparedStatement ps =
                                 con.prepareStatement(insertSql)) {

                        ps.setLong(1, applicationId);
                        ps.setDate(2, Date.valueOf(date));
                        ps.setTime(3, Time.valueOf(time));
                        ps.setString(4, mode);

                        if ("ONLINE".equals(mode)) {
                            ps.setString(5, meetingLink.trim());
                            ps.setNull(6, java.sql.Types.VARCHAR);
                        } else {
                            ps.setNull(5, java.sql.Types.VARCHAR);
                            ps.setString(6, venue.trim());
                        }

                        ps.executeUpdate();
                    }

                    con.commit();

                } catch (Exception e) {

                    con.rollback();
                    throw e;
                }
            }

            response.sendRedirect(
                    "recruiter-interviews?rescheduled=true"
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.sendRedirect(
                    "recruiter-interviews?error=database"
            );
        }
    }

    private void redirectError(HttpServletResponse response,
                               long interviewId,
                               String error)
            throws IOException {

        response.sendRedirect(
                "reschedule-interview?interviewId="
                        + interviewId
                        + "&error="
                        + error
        );
    }
}