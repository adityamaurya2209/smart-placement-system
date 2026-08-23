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

@WebServlet("/admin-student-details")
public class AdminStudentDetailsServlet extends HttpServlet {

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

        // Get student ID from URL
        String id = request.getParameter("id");

        System.out.println("Student ID received: " + id);

        if (id == null || id.isBlank()) {
            response.sendRedirect("admin-students");
            return;
        }

        long studentId;

        try {
            studentId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            response.sendRedirect("admin-students");
            return;
        }

        String sql = """
                SELECT id, name, email, created_at
                FROM users
                WHERE id = ?
                AND role = 'STUDENT'
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, studentId);

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {

                    request.setAttribute(
                            "studentId",
                            rs.getLong("id")
                    );

                    request.setAttribute(
                            "studentName",
                            rs.getString("name")
                    );

                    request.setAttribute(
                            "studentEmail",
                            rs.getString("email")
                    );

                    request.setAttribute(
                            "studentCreatedAt",
                            rs.getTimestamp("created_at")
                    );

                    System.out.println(
                            "Student found: "
                                    + rs.getString("name")
                    );

                    request.getRequestDispatcher(
                            "/admin-student-details.jsp"
                    ).forward(request, response);

                } else {

                    System.out.println(
                            "No student found with ID: "
                                    + studentId
                    );

                    response.sendRedirect("admin-students");
                }
            }

        } catch (Exception e) {

            e.printStackTrace();

            response.setContentType("text/html");

            response.getWriter().println(
                    "<h2>Database error occurred.</h2>"
            );
        }
    }
}