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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/applicants")
public class ApplicantsServlet extends HttpServlet {

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

        long userId = (Long) session.getAttribute("userId");

        String search = request.getParameter("search");
        String statusFilter = request.getParameter("status");

        if (search == null) {
            search = "";
        }

        if (statusFilter == null) {
            statusFilter = "";
        }

        search = search.trim();
        statusFilter = statusFilter.trim().toUpperCase();

        List<Application> applications = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
                SELECT
                    a.id,
                    a.application_date,
                    a.status,
                    a.match_score,

                    j.title,
                    j.minimum_cgpa,
                    j.eligible_branch,
                    j.required_skills,

                    c.company_name,

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

                WHERE c.user_id = ?
                """);

        List<Object> parameters = new ArrayList<>();
        parameters.add(userId);

        if (!statusFilter.isEmpty()
                && isValidStatus(statusFilter)) {

            sql.append(" AND a.status = ? ");
            parameters.add(statusFilter);
        }

        if (!search.isEmpty()) {

            sql.append("""
                    AND (
                        LOWER(u.name) LIKE ?
                        OR LOWER(u.email) LIKE ?
                        OR LOWER(s.roll_number) LIKE ?
                        OR LOWER(s.branch) LIKE ?
                        OR LOWER(j.title) LIKE ?
                    )
                    """);

            String searchValue = "%" + search.toLowerCase() + "%";

            parameters.add(searchValue);
            parameters.add(searchValue);
            parameters.add(searchValue);
            parameters.add(searchValue);
            parameters.add(searchValue);
        }

        sql.append("""
                ORDER BY
                    CASE
                        WHEN a.match_score IS NULL THEN 0
                        ELSE a.match_score
                    END DESC,
                    a.application_date DESC
                """);

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql.toString())) {

            for (int i = 0; i < parameters.size(); i++) {
                statement.setObject(i + 1, parameters.get(i));
            }

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {

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

                    application.setMinimumCgpa(
                            resultSet.getBigDecimal("minimum_cgpa")
                    );

                    application.setEligibleBranch(
                            resultSet.getString("eligible_branch")
                    );

                    application.setRequiredSkills(
                            resultSet.getString("required_skills")
                    );

                    // ------------------------------------------
                    // Eligibility calculation
                    // ------------------------------------------

                    boolean cgpaEligible =
                            application.getCgpa() != null
                            && application.getMinimumCgpa() != null
                            && application.getCgpa()
                                    .compareTo(application.getMinimumCgpa()) >= 0;

                    boolean branchEligible =
                            isBranchEligible(
                                    application.getBranch(),
                                    application.getEligibleBranch()
                            );

                    boolean eligible =
                            cgpaEligible && branchEligible;

                    application.setEligible(eligible);

                    // ------------------------------------------
                    // Match score calculation
                    // ------------------------------------------

                    BigDecimal calculatedScore =
                            calculateMatchScore(
                                    application.getSkills(),
                                    application.getRequiredSkills()
                            );

                    application.setMatchScore(calculatedScore);

                    // Save calculated score to database
                    updateMatchScore(
                            connection,
                            application.getId(),
                            calculatedScore
                    );

                    applications.add(application);
                }
            }

            request.setAttribute("applications", applications);
            request.setAttribute("search", search);
            request.setAttribute("statusFilter", statusFilter);

            request.getRequestDispatcher(
                    "/applicants.jsp"
            ).forward(request, response);

        } catch (Exception e) {

            e.printStackTrace();

            response.setContentType("text/html;charset=UTF-8");

            response.getWriter().println(
                    "<h2>Database error occurred.</h2>"
            );
        }
    }

    private boolean isValidStatus(String status) {

        return status.equals("APPLIED")
                || status.equals("SHORTLISTED")
                || status.equals("INTERVIEW")
                || status.equals("SELECTED")
                || status.equals("REJECTED");
    }

    private boolean isBranchEligible(String studentBranch,
                                      String eligibleBranches) {

        if (studentBranch == null
                || eligibleBranches == null
                || eligibleBranches.isBlank()) {

            return false;
        }

        String normalizedStudentBranch =
                studentBranch.trim().toLowerCase();

        String[] branches =
                eligibleBranches.split("[,/|]");

        for (String branch : branches) {

            String normalizedBranch =
                    branch.trim().toLowerCase();

            if (normalizedBranch.equals("all")
                    || normalizedBranch.equals("*")
                    || normalizedBranch.equals(normalizedStudentBranch)) {

                return true;
            }
        }

        return false;
    }

    private BigDecimal calculateMatchScore(String studentSkills,
                                           String requiredSkills) {

        if (requiredSkills == null
                || requiredSkills.isBlank()) {

            return BigDecimal.ZERO.setScale(
                    2,
                    RoundingMode.HALF_UP
            );
        }

        if (studentSkills == null
                || studentSkills.isBlank()) {

            return BigDecimal.ZERO.setScale(
                    2,
                    RoundingMode.HALF_UP
            );
        }

        String[] required =
                requiredSkills
                        .toLowerCase()
                        .split("[,;/|]+");

        String studentSkillText =
                studentSkills.toLowerCase();

        int totalRequired = 0;
        int matched = 0;

        for (String skill : required) {

            String cleanSkill =
                    skill.trim();

            if (cleanSkill.isEmpty()) {
                continue;
            }

            totalRequired++;

            if (containsSkill(
                    studentSkillText,
                    cleanSkill
            )) {
                matched++;
            }
        }

        if (totalRequired == 0) {

            return BigDecimal.ZERO.setScale(
                    2,
                    RoundingMode.HALF_UP
            );
        }

        return BigDecimal.valueOf(matched)
                .multiply(BigDecimal.valueOf(100))
                .divide(
                        BigDecimal.valueOf(totalRequired),
                        2,
                        RoundingMode.HALF_UP
                );
    }

    private boolean containsSkill(String studentSkills,
                                  String requiredSkill) {

        String normalizedStudentSkills =
                studentSkills
                        .replace(",", " ")
                        .replace(";", " ")
                        .replace("/", " ")
                        .replace("|", " ");

        String[] studentSkillArray =
                normalizedStudentSkills.split("\\s+");

        // Exact token comparison for single-word skills
        for (String skill : studentSkillArray) {

            if (skill.equals(requiredSkill)) {
                return true;
            }
        }

        // Also support multi-word skills
        return normalizedStudentSkills
                .contains(requiredSkill);
    }

    private void updateMatchScore(Connection connection,
                                  long applicationId,
                                  BigDecimal score) {

        String sql = """
                UPDATE applications
                SET match_score = ?
                WHERE id = ?
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setBigDecimal(1, score);
            statement.setLong(2, applicationId);

            statement.executeUpdate();

        } catch (Exception e) {

            // Score calculation should not prevent applicants page
            // from loading.
            e.printStackTrace();
        }
    }
}