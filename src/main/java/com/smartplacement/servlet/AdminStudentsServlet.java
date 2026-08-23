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

@WebServlet("/admin-students")
public class AdminStudentsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // Check whether admin is logged in
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login.html");
            return;
        }

        // Check admin role
        String role = (String) session.getAttribute("role");

        if (role == null || !"ADMIN".equalsIgnoreCase(role)) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Access denied. Admin access required."
            );
            return;
        }

        List<Student> students = new ArrayList<>();

        String sql = """
                SELECT id, name, email, created_at
                FROM users
                WHERE role = 'STUDENT'
                ORDER BY id
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Student student = new Student();

                student.setId(resultSet.getLong("id"));
                student.setName(resultSet.getString("name"));
                student.setEmail(resultSet.getString("email"));
                student.setCreatedAt(
                        resultSet.getTimestamp("created_at")
                );

                students.add(student);
            }

            request.setAttribute("students", students);

            request.getRequestDispatcher(
                    "/admin-students.jsp"
            ).forward(request, response);

        } catch (Exception e) {

            e.printStackTrace();

            response.setContentType("text/html");

            response.getWriter().println(
                    "<h2>Database error occurred.</h2>"
            );
        }
    }

    // Simple inner class for displaying student information
    public static class Student {

        private long id;
        private String name;
        private String email;
        private java.sql.Timestamp createdAt;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public java.sql.Timestamp getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(java.sql.Timestamp createdAt) {
            this.createdAt = createdAt;
        }
    }
}