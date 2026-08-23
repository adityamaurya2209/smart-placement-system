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

@WebServlet("/admin-application-details")
public class AdminApplicationDetailsServlet
        extends HttpServlet {


    // =========================================================
    // GET
    // Display application details
    // =========================================================

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {


        HttpSession session =
                request.getSession(false);


        // Admin authentication

        if (session == null ||
                session.getAttribute("userId") == null ||
                !"ADMIN".equalsIgnoreCase(
                        String.valueOf(
                                session.getAttribute("role")))) {

            response.sendRedirect("login.html");

            return;
        }


        // Get application ID

        String id =
                request.getParameter("id");


        System.out.println(
                "Application ID received: " + id
        );


        if (id == null || id.isBlank()) {

            response.sendRedirect(
                    "admin-applications"
            );

            return;
        }


        long applicationId;


        try {

            applicationId =
                    Long.parseLong(id);

        } catch (NumberFormatException e) {

            response.sendRedirect(
                    "admin-applications"
            );

            return;
        }


        String sql = """
                SELECT a.id,
                    a.student_id,
                    u.name AS student_name,
                    u.email AS student_email,
                    a.job_id,
                    j.title AS job_title,
                    j.location AS job_location,
                    c.company_name,
                    a.application_date,
                    a.status,
                    a.match_score
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
                """;


        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {


            statement.setLong(
                    1,
                    applicationId
            );


            try (
                    ResultSet rs =
                            statement.executeQuery()
            ) {


                if (rs.next()) {


                    request.setAttribute(
                            "applicationId",
                            rs.getLong("id")
                    );


                    request.setAttribute(
                            "studentId",
                            rs.getLong("student_id")
                    );


                    request.setAttribute(
                            "studentName",
                            rs.getString(
                                    "student_name")
                    );


                    request.setAttribute(
                            "studentEmail",
                            rs.getString(
                                    "student_email")
                    );


                    request.setAttribute(
                            "jobId",
                            rs.getLong("job_id")
                    );


                    request.setAttribute(
                            "jobTitle",
                            rs.getString(
                                    "job_title")
                    );


                    request.setAttribute(
                            "jobLocation",
                            rs.getString(
                                    "job_location")
                    );


                    request.setAttribute(
                            "companyName",
                            rs.getString(
                                    "company_name")
                    );


                    request.setAttribute(
                            "applicationDate",
                            rs.getTimestamp(
                                    "application_date")
                    );


                    request.setAttribute(
                            "status",
                            rs.getString("status")
                    );


                    request.setAttribute(
                            "matchScore",
                            rs.getBigDecimal(
                                    "match_score")
                    );


                    request.getRequestDispatcher(
                            "/admin-application-details.jsp"
                    ).forward(
                            request,
                            response
                    );


                } else {


                    System.out.println(
                            "No application found with ID: "
                                    + applicationId
                    );


                    response.sendRedirect(
                            "admin-applications"
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


    // =========================================================
    // POST
    // Update application status
    // =========================================================

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {


        HttpSession session =
                request.getSession(false);


        // Admin authentication

        if (session == null ||
                session.getAttribute("userId") == null ||
                !"ADMIN".equalsIgnoreCase(
                        String.valueOf(
                                session.getAttribute("role")))) {

            response.sendRedirect("login.html");

            return;
        }


        // Get application ID

        String id =
                request.getParameter("id");


        // Get new status

        String status =
                request.getParameter("status");


        System.out.println(
                "Updating application ID: "
                        + id
                        + " to status: "
                        + status
        );


        // Validate ID

        if (id == null || id.isBlank()) {

            response.sendRedirect(
                    "admin-applications"
            );

            return;
        }


        long applicationId;


        try {

            applicationId =
                    Long.parseLong(id);

        } catch (NumberFormatException e) {

            response.sendRedirect(
                    "admin-applications"
            );

            return;
        }


        // Validate status

        if (status == null ||
                !(status.equals("APPLIED") ||
                  status.equals("SHORTLISTED") ||
                  status.equals("INTERVIEW") ||
                  status.equals("SELECTED") ||
                  status.equals("REJECTED"))) {


            response.sendRedirect(
                    "admin-application-details?id="
                            + applicationId
                            + "&error=invalid-status"
            );

            return;
        }


        String sql = """
                UPDATE applications
                SET status = ?
                WHERE id = ?
                """;


        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {


            statement.setString(
                    1,
                    status
            );


            statement.setLong(
                    2,
                    applicationId
            );


            int rowsUpdated =
                    statement.executeUpdate();


            if (rowsUpdated > 0) {


                System.out.println(
                        "Application status updated successfully."
                );


                response.sendRedirect(
                        "admin-application-details?id="
                                + applicationId
                                + "&success=updated"
                );


            } else {


                System.out.println(
                        "No application found with ID: "
                                + applicationId
                );


                response.sendRedirect(
                        "admin-application-details?id="
                                + applicationId
                                + "&error=update-failed"
                );
            }


        } catch (Exception e) {


            e.printStackTrace();


            response.sendRedirect(
                    "admin-application-details?id="
                            + applicationId
                            + "&error=update-failed"
            );
        }
    }
}