package com.smartplacement.servlet;

import com.smartplacement.util.DBConnection;
import com.smartplacement.util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Pattern;

/**
 * Self-registration for students and recruiters.
 * Admin accounts cannot be created through this servlet.
 */
@WebServlet("/signup")
public class SignupServlet extends HttpServlet {

    private static final Pattern EMAIL =
            Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    private static final Pattern PHONE =
            Pattern.compile("^\\+?[0-9]{7,14}$");

    private static final BigDecimal MAX_CGPA = new BigDecimal("9.99");

    @Override
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/signup.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String role = param(request, "role");
        String name = param(request, "name");
        String email = param(request, "email").toLowerCase();
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        // Student fields
        String rollNumber = param(request, "rollNumber");
        String branch = param(request, "branch");
        String cgpaText = param(request, "cgpa");
        String phone = param(request, "phone");
        String skills = param(request, "skills");
        String certifications = param(request, "certifications");

        // Recruiter / company fields
        String companyName = param(request, "companyName");
        String location = param(request, "location");
        String website = param(request, "website");
        String description = param(request, "description");

        if (!"STUDENT".equals(role) && !"RECRUITER".equals(role)) {
            role = "STUDENT";
        }

        request.setAttribute("role", role);

        // ---------- Validation ----------

        String error = null;
        BigDecimal cgpa = null;

        if (name.isEmpty() || name.length() > 100) {
            error = "Please enter your full name (up to 100 characters).";

        } else if (!EMAIL.matcher(email).matches() || email.length() > 100) {
            error = "Please enter a valid email address.";

        } else if (password == null || password.length() < 8) {
            error = "Password must be at least 8 characters long.";

        } else if (password.length() > 128) {
            error = "Password must be at most 128 characters long.";

        } else if (!password.equals(confirmPassword)) {
            error = "Passwords do not match.";

        } else if ("STUDENT".equals(role)) {

            if (rollNumber.isEmpty() || rollNumber.length() > 50) {
                error = "Please enter your roll number (up to 50 characters).";

            } else if (branch.isEmpty() || branch.length() > 100) {
                error = "Please enter your branch.";

            } else if (!phone.isEmpty() && !PHONE.matcher(phone).matches()) {
                error = "Please enter a valid phone number (digits only, optional +).";

            } else {
                try {
                    cgpa = new BigDecimal(cgpaText);

                    if (cgpa.signum() < 0 || cgpa.compareTo(MAX_CGPA) > 0) {
                        error = "CGPA must be between 0.00 and 9.99.";
                    } else if (cgpa.scale() > 2) {
                        error = "CGPA can have at most 2 decimal places.";
                    }

                } catch (NumberFormatException e) {
                    error = "Please enter a valid CGPA.";
                }
            }

        } else {

            if (companyName.isEmpty() || companyName.length() > 150) {
                error = "Please enter your company name (up to 150 characters).";

            } else if (location.length() > 100) {
                error = "Location must be at most 100 characters.";

            } else if (!website.isEmpty()
                    && (website.length() > 255
                        || !(website.startsWith("http://")
                             || website.startsWith("https://")))) {
                error = "Website must start with http:// or https://";
            }
        }

        if (error != null) {
            fail(request, response, error);
            return;
        }

        // ---------- Save ----------

        try (Connection connection = DBConnection.getConnection()) {

            if (exists(connection,
                    "SELECT 1 FROM users WHERE email = ?", email)) {
                fail(request, response,
                        "An account with this email already exists.");
                return;
            }

            if ("STUDENT".equals(role)
                    && exists(connection,
                        "SELECT 1 FROM students WHERE roll_number = ?",
                        rollNumber)) {
                fail(request, response,
                        "A student with this roll number is already registered.");
                return;
            }

            connection.setAutoCommit(false);

            try {
                long userId = insertUser(
                        connection, name, email, password, role
                );

                if ("STUDENT".equals(role)) {
                    insertStudent(connection, userId, rollNumber, branch,
                            cgpa, phone, skills, certifications);
                } else {
                    insertCompany(connection, userId, companyName,
                            description, website, location);
                }

                connection.commit();

            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {

            e.printStackTrace();

            // Unique-key race (same email / roll number submitted twice)
            if ("23000".equals(e.getSQLState())) {
                fail(request, response,
                        "An account with these details already exists.");
            } else {
                fail(request, response,
                        "Could not create your account. Please try again later.");
            }
            return;
        }

        response.sendRedirect("login.html?registered=1");
    }

    private long insertUser(Connection connection,
                            String name,
                            String email,
                            String password,
                            String role) throws SQLException {

        String sql = """
                INSERT INTO users (name, email, password, role)
                VALUES (?, ?, ?, ?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, PasswordUtil.hash(password));
            statement.setString(4, role);
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (!keys.next()) {
                    throw new SQLException("User id was not generated.");
                }
                return keys.getLong(1);
            }
        }
    }

    private void insertStudent(Connection connection,
                               long userId,
                               String rollNumber,
                               String branch,
                               BigDecimal cgpa,
                               String phone,
                               String skills,
                               String certifications) throws SQLException {

        String sql = """
                INSERT INTO students
                    (user_id, roll_number, branch, cgpa,
                     phone, skills, certifications)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, userId);
            statement.setString(2, rollNumber);
            statement.setString(3, branch);
            statement.setBigDecimal(4, cgpa);
            statement.setString(5, emptyToNull(phone));
            statement.setString(6, emptyToNull(skills));
            statement.setString(7, emptyToNull(certifications));
            statement.executeUpdate();
        }
    }

    private void insertCompany(Connection connection,
                               long userId,
                               String companyName,
                               String description,
                               String website,
                               String location) throws SQLException {

        String sql = """
                INSERT INTO companies
                    (user_id, company_name, description, website, location)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, userId);
            statement.setString(2, companyName);
            statement.setString(3, emptyToNull(description));
            statement.setString(4, emptyToNull(website));
            statement.setString(5, emptyToNull(location));
            statement.executeUpdate();
        }
    }

    private boolean exists(Connection connection,
                           String sql,
                           String value) throws SQLException {

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, value);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    private void fail(HttpServletRequest request,
                      HttpServletResponse response,
                      String message) throws ServletException, IOException {

        request.setAttribute("error", message);
        request.getRequestDispatcher("/signup.jsp")
                .forward(request, response);
    }

    private static String param(HttpServletRequest request, String key) {
        String value = request.getParameter(key);
        return value == null ? "" : value.trim();
    }

    private static String emptyToNull(String value) {
        return value == null || value.isEmpty() ? null : value;
    }
}
