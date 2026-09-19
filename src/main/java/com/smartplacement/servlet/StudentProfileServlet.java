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

@WebServlet("/student-profile")
public class StudentProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null
                || session.getAttribute("userId") == null
                || !"STUDENT".equals(session.getAttribute("role"))) {

            response.sendRedirect("login.html");
            return;
        }

        long userId = (Long) session.getAttribute("userId");

        String sql = """
                SELECT u.name,
                       u.email,
                       s.roll_number,
                       s.branch,
                       s.cgpa,
                       s.phone,
                       s.skills,
                       s.certifications,
                       s.resume_path
                FROM users u
                JOIN students s
                    ON u.id = s.user_id
                WHERE u.id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, userId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (!resultSet.next()) {

                    response.setContentType("text/html");

                    response.getWriter().println(
                            "<h2>Student profile not found.</h2>"
                    );

                    return;
                }

                request.setAttribute(
                        "name",
                        resultSet.getString("name")
                );

                request.setAttribute(
                        "email",
                        resultSet.getString("email")
                );

                request.setAttribute(
                        "rollNumber",
                        resultSet.getString("roll_number")
                );

                request.setAttribute(
                        "branch",
                        resultSet.getString("branch")
                );

                request.setAttribute(
                        "cgpa",
                        resultSet.getBigDecimal("cgpa")
                );

                request.setAttribute(
                        "phone",
                        resultSet.getString("phone")
                );

                request.setAttribute(
                        "skills",
                        resultSet.getString("skills")
                );

                request.setAttribute(
                        "certifications",
                        resultSet.getString("certifications")
                );

                request.setAttribute(
                        "resumePath",
                        resultSet.getString("resume_path")
                );

                request.getRequestDispatcher(
                        "/profile.jsp"
                ).forward(request, response);
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