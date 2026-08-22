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

@WebServlet("/admin-dashboard")
public class AdminDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // Check whether user is logged in
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login.html");
            return;
        }

        // Check whether logged-in user is an ADMIN
        String role = (String) session.getAttribute("role");

        if (role == null || !"ADMIN".equalsIgnoreCase(role)) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Access denied. Admin access required."
            );
            return;
        }

        int studentCount = 0;
        int recruiterCount = 0;
        int companyCount = 0;
        int jobCount = 0;
        int applicationCount = 0;
        int interviewCount = 0;

        try (Connection connection = DBConnection.getConnection()) {

            studentCount = getCount(
                connection,
                "SELECT COUNT(*) FROM users WHERE role = 'STUDENT'"
        );

        recruiterCount = getCount(
                connection,
                "SELECT COUNT(*) FROM users WHERE role = 'RECRUITER'"
        );

        companyCount = getCount(
                connection,
                "SELECT COUNT(*) FROM companies"
        );

        jobCount = getCount(
                connection,
                "SELECT COUNT(*) FROM jobs"
        );

        applicationCount = getCount(
                connection,
                "SELECT COUNT(*) FROM applications"
        );

        interviewCount = getCount(
                connection,
                "SELECT COUNT(*) FROM interviews"
        );

            request.setAttribute("studentCount", studentCount);
            request.setAttribute("recruiterCount", recruiterCount);
            request.setAttribute("companyCount", companyCount);
            request.setAttribute("jobCount", jobCount);
            request.setAttribute("applicationCount", applicationCount);
            request.setAttribute("interviewCount", interviewCount);

            request.getRequestDispatcher(
                    "/admin-dashboard.jsp"
            ).forward(request, response);

        } catch (Exception e) {

            e.printStackTrace();

            response.setContentType("text/html");

            response.getWriter().println(
                    "<h2>Database error occurred.</h2>"
            );
        }
    }

    private int getCount(Connection connection, String sql)
            throws Exception {

        try (PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }

        return 0;
    }
}