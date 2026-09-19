package com.smartplacement.servlet;

import com.smartplacement.model.Job;
import com.smartplacement.util.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/edit-job")
public class EditJobServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null
                || session.getAttribute("userId") == null
                || !"RECRUITER".equals(session.getAttribute("role"))) {

            response.sendRedirect("login.html");
            return;
        }

        String idParam = request.getParameter("id");

        long jobId;

        try {
            jobId = Long.parseLong(idParam);
        } catch (Exception e) {
            response.sendRedirect("recruiter-jobs?error=invalid-id");
            return;
        }

        long userId = (Long) session.getAttribute("userId");

        String sql = """
                SELECT j.id,
                       j.title,
                       j.description,
                       j.location,
                       j.minimum_cgpa,
                       j.eligible_branch,
                       j.required_skills,
                       j.salary,
                       j.application_deadline,
                       j.status,
                       c.company_name
                FROM jobs j
                JOIN companies c ON j.company_id = c.id
                WHERE j.id = ?
                  AND c.user_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, jobId);
            statement.setLong(2, userId);

            try (ResultSet rs = statement.executeQuery()) {

                if (!rs.next()) {
                    response.sendRedirect("recruiter-jobs?error=not-found");
                    return;
                }

                Job job = new Job();

                job.setId(rs.getLong("id"));
                job.setTitle(rs.getString("title"));
                job.setDescription(rs.getString("description"));
                job.setLocation(rs.getString("location"));
                job.setMinimumCgpa(rs.getBigDecimal("minimum_cgpa"));
                job.setEligibleBranch(rs.getString("eligible_branch"));
                job.setRequiredSkills(rs.getString("required_skills"));
                job.setSalary(rs.getString("salary"));
                job.setApplicationDeadline(rs.getDate("application_deadline"));
                job.setStatus(rs.getString("status"));
                job.setCompanyName(rs.getString("company_name"));

                request.setAttribute("job", job);

                request.getRequestDispatcher("/edit-job.jsp")
                        .forward(request, response);
            }

        } catch (Exception e) {

            e.printStackTrace();

            response.sendRedirect("recruiter-jobs?error=database");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null
                || session.getAttribute("userId") == null
                || !"RECRUITER".equals(session.getAttribute("role"))) {

            response.sendRedirect("login.html");
            return;
        }

        String idParam = request.getParameter("id");

        long jobId;

        try {
            jobId = Long.parseLong(idParam);
        } catch (Exception e) {
            response.sendRedirect("recruiter-jobs?error=invalid-id");
            return;
        }

        long userId = (Long) session.getAttribute("userId");

        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String location = request.getParameter("location");
        String cgpaParam = request.getParameter("minimumCgpa");
        String eligibleBranch = request.getParameter("eligibleBranch");
        String requiredSkills = request.getParameter("requiredSkills");
        String salary = request.getParameter("salary");
        String deadlineParam = request.getParameter("applicationDeadline");

        if (title == null || title.isBlank()
                || cgpaParam == null || cgpaParam.isBlank()
                || eligibleBranch == null || eligibleBranch.isBlank()) {

            response.sendRedirect("edit-job?id=" + jobId + "&error=missing");
            return;
        }

        BigDecimal cgpa;

        try {

            cgpa = new BigDecimal(cgpaParam);

            if (cgpa.compareTo(BigDecimal.ZERO) < 0
                    || cgpa.compareTo(BigDecimal.TEN) > 0) {

                response.sendRedirect("edit-job?id=" + jobId + "&error=cgpa");
                return;
            }

        } catch (NumberFormatException e) {

            response.sendRedirect("edit-job?id=" + jobId + "&error=cgpa");
            return;
        }

        Date deadline = null;

        if (deadlineParam != null && !deadlineParam.isBlank()) {

            try {

                deadline = Date.valueOf(deadlineParam);

                if (deadline.toLocalDate().isBefore(
                        java.time.LocalDate.now())) {

                    response.sendRedirect(
                            "edit-job?id=" + jobId + "&error=deadline"
                    );
                    return;
                }

            } catch (IllegalArgumentException e) {

                response.sendRedirect(
                        "edit-job?id=" + jobId + "&error=deadline"
                );
                return;
            }
        }

        String sql = """
                UPDATE jobs j
                JOIN companies c ON j.company_id = c.id
                SET j.title = ?,
                    j.description = ?,
                    j.location = ?,
                    j.minimum_cgpa = ?,
                    j.eligible_branch = ?,
                    j.required_skills = ?,
                    j.salary = ?,
                    j.application_deadline = ?
                WHERE j.id = ?
                  AND c.user_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, title.trim());
            statement.setString(2, description);
            statement.setString(3, location);
            statement.setBigDecimal(4, cgpa);
            statement.setString(5, eligibleBranch.trim());
            statement.setString(6, requiredSkills);
            statement.setString(7, salary);

            if (deadline == null) {
                statement.setNull(8, java.sql.Types.DATE);
            } else {
                statement.setDate(8, deadline);
            }

            statement.setLong(9, jobId);
            statement.setLong(10, userId);

            int updated = statement.executeUpdate();

            if (updated == 1) {
                response.sendRedirect("recruiter-jobs?updated=true");
            } else {
                response.sendRedirect("recruiter-jobs?error=not-found");
            }

        } catch (Exception e) {

            e.printStackTrace();

            response.sendRedirect("recruiter-jobs?error=database");
        }
    }
}
