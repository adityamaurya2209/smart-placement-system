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

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        String sql = "SELECT id, name, role FROM users " +
                     "WHERE email = ? AND password = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                // Login successful
                HttpSession session = request.getSession();

                session.setAttribute("userId", resultSet.getLong("id"));
                session.setAttribute("name", resultSet.getString("name"));
                session.setAttribute("role", resultSet.getString("role"));

                String role = resultSet.getString("role");

            if ("STUDENT".equals(role)) {

                response.sendRedirect("dashboard.html");

            } else if ("RECRUITER".equals(role)) {

                response.sendRedirect("recruiter-dashboard");

            } else if ("ADMIN".equals(role)) {

                response.sendRedirect("admin-dashboard");

            } else {

                response.sendRedirect("login.html");
            }

            } else {

                // Login failed
                response.setContentType("text/html");

                response.getWriter().println(
                    "<h2>Invalid email or password</h2>"
                );
                response.getWriter().println(
                    "<a href='login.html'>Try Again</a>"
                );
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