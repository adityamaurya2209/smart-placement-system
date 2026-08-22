package com.smartplacement.servlet;
import com.smartplacement.model.Interview;
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

@WebServlet("/interview-schedule")
public class InterviewScheduleServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        // Check whether student is logged in
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login.html");
            return;
        }

        long userId = (Long) session.getAttribute("userId");

        String sql = """
                SELECT
                    i.id,
                    j.title,
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
                JOIN jobs j
                    ON a.job_id = j.id
                JOIN companies c
                    ON j.company_id = c.id
                WHERE s.user_id = ?
                ORDER BY i.interview_date ASC,
                         i.interview_time ASC
                """;

        List<Interview> interviews = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {

                    Interview interview = new Interview();

                    interview.setId(
                            resultSet.getLong("id")
                    );

                    interview.setJobTitle(
                            resultSet.getString("title")
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
            }

            request.setAttribute("interviews", interviews);

            request.getRequestDispatcher(
                    "/interview-schedule.jsp"
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