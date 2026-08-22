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

@WebServlet("/my-applications")
public class MyApplicationsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        // Check whether the student is logged in
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login.html");
            return;
        }

        long userId = (Long) session.getAttribute("userId");

        String sql = """
                SELECT
                    a.id,
                    j.title,
                    c.company_name,
                    a.application_date,
                    a.status,
                    a.match_score
                FROM applications a
                JOIN students s ON a.student_id = s.id
                JOIN jobs j ON a.job_id = j.id
                JOIN companies c ON j.company_id = c.id
                WHERE s.user_id = ?
                ORDER BY a.application_date DESC
                """;

        List<Application> applications = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {

                    Application application = new Application();

                    application.setId(resultSet.getLong("id"));
                    application.setJobTitle(resultSet.getString("title"));
                    application.setCompanyName(
                            resultSet.getString("company_name")
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

            request.getRequestDispatcher("/my-applications.jsp")
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