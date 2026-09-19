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

@WebServlet("/recruiter-application-details")
public class RecruiterApplicationDetailsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null
                || session.getAttribute("userId") == null
                || !"RECRUITER".equals(session.getAttribute("role"))) {

            response.sendRedirect("login.html");
            return;
        }

        String idParameter = request.getParameter("id");

        if (idParameter == null || idParameter.isBlank()) {
            response.sendRedirect("applicants");
            return;
        }

        long applicationId;

        try {
            applicationId = Long.parseLong(idParameter);
        } catch (NumberFormatException e) {
            response.sendRedirect("applicants");
            return;
        }

        long userId = (Long) session.getAttribute("userId");

        String sql = """
                SELECT a.id,
                       j.title,
                       c.company_name,
                       a.application_date,
                       a.status,
                       a.match_score,
                       u.name,
                       u.email,
                       s.roll_number,
                       s.branch,
                       s.cgpa,
                       s.phone,
                       s.skills,
                       s.certifications,
                       s.resume_path
                FROM applications a
                JOIN students s
                    ON a.student_id = s.id
                JOIN users u
                    ON s.user_id = u.id
                JOIN jobs j
                    ON a.job_id = j.id
                JOIN companies c
                    ON j.company_id = c.id
                WHERE a.id = ?
                  AND c.user_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, applicationId);
            statement.setLong(2, userId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (!resultSet.next()) {

                    response.setStatus(
                            HttpServletResponse.SC_NOT_FOUND
                    );

                    response.setContentType("text/html");

                    response.getWriter().println(
                            "<h2>Application not found.</h2>"
                    );

                    response.getWriter().println(
                            "<a href='applicants'>Back to Applicants</a>"
                    );

                    return;
                }

                Application application = new Application();

                application.setId(
                        resultSet.getLong("id")
                );

                application.setJobTitle(
                        resultSet.getString("title")
                );

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

                application.setStudentName(
                        resultSet.getString("name")
                );

                application.setStudentEmail(
                        resultSet.getString("email")
                );

                application.setRollNumber(
                        resultSet.getString("roll_number")
                );

                application.setBranch(
                        resultSet.getString("branch")
                );

                application.setCgpa(
                        resultSet.getBigDecimal("cgpa")
                );

                application.setPhone(
                        resultSet.getString("phone")
                );

                application.setSkills(
                        resultSet.getString("skills")
                );

                application.setCertifications(
                        resultSet.getString("certifications")
                );

                application.setResumePath(
                        resultSet.getString("resume_path")
                );

                request.setAttribute(
                        "application",
                        application
                );

                request.getRequestDispatcher(
                        "/recruiter-application-details.jsp"
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