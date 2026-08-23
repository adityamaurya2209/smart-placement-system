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

@WebServlet("/admin-company-details")
public class AdminCompanyDetailsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // Admin authentication
        if (session == null ||
                session.getAttribute("userId") == null ||
                !"ADMIN".equalsIgnoreCase(
                        String.valueOf(
                                session.getAttribute("role")))) {

            response.sendRedirect("login.html");
            return;
        }

        String id = request.getParameter("id");

        System.out.println(
                "Company ID received: " + id
        );

        if (id == null || id.isBlank()) {

            response.sendRedirect(
                    "admin-companies"
            );

            return;
        }

        long companyId;

        try {

            companyId = Long.parseLong(id);

        } catch (NumberFormatException e) {

            response.sendRedirect(
                    "admin-companies"
            );

            return;
        }

        String sql = """
                SELECT id, user_id, company_name,
                       description, website, location
                FROM companies
                WHERE id = ?
                """;

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(
                    1,
                    companyId
            );

            try (ResultSet rs =
                         statement.executeQuery()) {

                if (rs.next()) {

                    request.setAttribute(
                            "companyId",
                            rs.getLong("id")
                    );

                    request.setAttribute(
                            "companyUserId",
                            rs.getLong("user_id")
                    );

                    request.setAttribute(
                            "companyName",
                            rs.getString("company_name")
                    );

                    request.setAttribute(
                            "companyDescription",
                            rs.getString("description")
                    );

                    request.setAttribute(
                            "companyWebsite",
                            rs.getString("website")
                    );

                    request.setAttribute(
                            "companyLocation",
                            rs.getString("location")
                    );

                    System.out.println(
                            "Company found: "
                                    + rs.getString(
                                            "company_name")
                    );

                    request.getRequestDispatcher(
                            "/admin-company-details.jsp"
                    ).forward(
                            request,
                            response
                    );

                } else {

                    System.out.println(
                            "No company found with ID: "
                                    + companyId
                    );

                    response.sendRedirect(
                            "admin-companies"
                    );
                }
            }

        } catch (Exception e) {

            e.printStackTrace();

            response.setContentType(
                    "text/html"
            );

            response.getWriter().println(
                    "<h2>Database error occurred.</h2>"
            );
        }
    }
}