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

@WebServlet("/create-job")
public class CreateJobServlet extends HttpServlet {

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

        request.getRequestDispatcher(
                "/create-job.jsp"
        ).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null
                || session.getAttribute("userId") == null
                || !"RECRUITER".equals(session.getAttribute("role"))) {

            response.sendRedirect("login.html");
            return;
        }

        long userId = (Long) session.getAttribute("userId");

        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String location = request.getParameter("location");
        String minimumCgpaParameter =
                request.getParameter("minimumCgpa");
        String eligibleBranch =
                request.getParameter("eligibleBranch");
        String requiredSkills =
                request.getParameter("requiredSkills");
        String salary = request.getParameter("salary");
        String applicationDeadline =
                request.getParameter("applicationDeadline");

        if (title == null || title.isBlank()
                || minimumCgpaParameter == null
                || minimumCgpaParameter.isBlank()
                || eligibleBranch == null
                || eligibleBranch.isBlank()) {

            response.sendRedirect(
                    "create-job?error=missing"
            );

            return;
        }

        double minimumCgpa;

        try {

            minimumCgpa =
                    Double.parseDouble(minimumCgpaParameter);

            if (minimumCgpa < 0 || minimumCgpa > 10) {

                response.sendRedirect(
                        "create-job?error=cgpa"
                );

                return;
            }

        } catch (NumberFormatException e) {

            response.sendRedirect(
                    "create-job?error=cgpa"
            );

            return;
        }

        String companyQuery = """
                SELECT id
                FROM companies
                WHERE user_id = ?
                """;

        String insertQuery = """
                INSERT INTO jobs
                (
                    company_id,
                    title,
                    description,
                    location,
                    minimum_cgpa,
                    eligible_branch,
                    required_skills,
                    salary,
                    application_deadline,
                    status
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 'OPEN')
                """;

        try (Connection connection =
                     DBConnection.getConnection()) {

            long companyId;

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 companyQuery)) {

                statement.setLong(1, userId);

                try (ResultSet resultSet =
                             statement.executeQuery()) {

                    if (!resultSet.next()) {

                        response.sendRedirect(
                                "company-profile?error=missing"
                        );

                        return;
                    }

                    companyId =
                            resultSet.getLong("id");
                }
            }

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 insertQuery)) {

                statement.setLong(1, companyId);
                statement.setString(2, title.trim());
                statement.setString(3, description);
                statement.setString(4, location);
                statement.setDouble(5, minimumCgpa);
                statement.setString(6, eligibleBranch);
                statement.setString(7, requiredSkills);
                statement.setString(8, salary);

                if (applicationDeadline == null
                        || applicationDeadline.isBlank()) {

                    statement.setNull(
                            9,
                            java.sql.Types.DATE
                    );

                } else {

                    statement.setDate(
                            9,
                            java.sql.Date.valueOf(
                                    applicationDeadline
                            )
                    );
                }

                statement.executeUpdate();
            }

            response.sendRedirect(
                    "recruiter-jobs?success=true"
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.setContentType("text/html");

            response.getWriter().println(
                    "<h2>Database error occurred.</h2>"
            );
        }
    }
}