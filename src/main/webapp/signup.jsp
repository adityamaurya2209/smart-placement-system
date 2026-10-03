<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="selectedRole" value="${requestScope.role == 'RECRUITER' or param.role == 'RECRUITER' ? 'RECRUITER' : 'STUDENT'}" />

<!DOCTYPE html>
<html lang="en">
<head>
    <link rel="icon" href="favicon.ico" sizes="any">
    <link rel="icon" type="image/svg+xml" href="images/brand/smart-placement-icon.svg">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Account | Smart Placement System</title>
    <link rel="stylesheet" href="css/style.css">
</head>

<body class="login-page">

    <main class="login-box signup-box">
        <img
            class="login-logo"
            src="images/brand/smart-placement-logo.svg"
            alt="Smart Placement"
        >

        <h1>Create your account</h1>
        <p class="login-subtitle">
            Join as a student to apply for jobs, or as a recruiter to hire.
        </p>

        <c:if test="${not empty error}">
            <p class="auth-alert error" role="alert"><c:out value="${error}" /></p>
        </c:if>

        <form action="signup" method="post" id="signup-form">

            <div class="role-switch" role="radiogroup" aria-label="Account type">
                <input type="radio" id="role-student" name="role" value="STUDENT"
                       ${selectedRole == 'STUDENT' ? 'checked' : ''}>
                <label for="role-student">I'm a Student</label>

                <input type="radio" id="role-recruiter" name="role" value="RECRUITER"
                       ${selectedRole == 'RECRUITER' ? 'checked' : ''}>
                <label for="role-recruiter">I'm a Recruiter</label>
            </div>

            <fieldset class="signup-section">
                <legend>Account details</legend>

                <div class="signup-grid">
                    <div class="full">
                        <label for="name">Full name</label>
                        <input type="text" id="name" name="name" maxlength="100"
                               autocomplete="name" placeholder="Enter your full name"
                               value="<c:out value='${param.name}' />" required>
                    </div>

                    <div class="full">
                        <label for="email">Email</label>
                        <input type="email" id="email" name="email" maxlength="100"
                               autocomplete="email" placeholder="you@example.com"
                               value="<c:out value='${param.email}' />" required>
                    </div>

                    <div>
                        <label for="password">Password</label>
                        <input type="password" id="password" name="password"
                               minlength="8" maxlength="128" autocomplete="new-password"
                               placeholder="At least 8 characters" required>
                    </div>

                    <div>
                        <label for="confirmPassword">Confirm password</label>
                        <input type="password" id="confirmPassword" name="confirmPassword"
                               minlength="8" maxlength="128" autocomplete="new-password"
                               placeholder="Re-enter password" required>
                    </div>
                </div>
            </fieldset>

            <fieldset class="signup-section" data-role="STUDENT"
                      ${selectedRole == 'STUDENT' ? '' : 'hidden disabled'}>
                <legend>Academic details</legend>

                <div class="signup-grid">
                    <div>
                        <label for="rollNumber">Roll number</label>
                        <input type="text" id="rollNumber" name="rollNumber" maxlength="50"
                               placeholder="e.g. MCA2024001"
                               value="<c:out value='${param.rollNumber}' />" required>
                    </div>

                    <div>
                        <label for="branch">Branch</label>
                        <input type="text" id="branch" name="branch" maxlength="100"
                               list="branch-options" placeholder="e.g. MCA"
                               value="<c:out value='${param.branch}' />" required>
                        <datalist id="branch-options">
                            <option value="MCA">
                            <option value="BCA">
                            <option value="CSE">
                            <option value="IT">
                            <option value="ECE">
                            <option value="EE">
                            <option value="ME">
                            <option value="CE">
                        </datalist>
                    </div>

                    <div>
                        <label for="cgpa">CGPA</label>
                        <input type="number" id="cgpa" name="cgpa" min="0" max="9.99"
                               step="0.01" placeholder="e.g. 8.25"
                               value="<c:out value='${param.cgpa}' />" required>
                    </div>

                    <div>
                        <label for="phone">Phone <span class="optional">(optional)</span></label>
                        <input type="tel" id="phone" name="phone" maxlength="15"
                               pattern="\+?[0-9]{7,14}" autocomplete="tel"
                               placeholder="e.g. 9876543210"
                               value="<c:out value='${param.phone}' />">
                    </div>

                    <div class="full">
                        <label for="skills">Skills <span class="optional">(optional)</span></label>
                        <input type="text" id="skills" name="skills"
                               placeholder="e.g. Java, SQL, HTML, CSS"
                               value="<c:out value='${param.skills}' />">
                        <p class="field-hint">Separate skills with commas. They are used for job skill matching.</p>
                    </div>

                    <div class="full">
                        <label for="certifications">Certifications <span class="optional">(optional)</span></label>
                        <input type="text" id="certifications" name="certifications"
                               placeholder="e.g. Oracle Java SE, AWS Cloud Practitioner"
                               value="<c:out value='${param.certifications}' />">
                    </div>
                </div>
            </fieldset>

            <fieldset class="signup-section" data-role="RECRUITER"
                      ${selectedRole == 'RECRUITER' ? '' : 'hidden disabled'}>
                <legend>Company details</legend>

                <div class="signup-grid">
                    <div>
                        <label for="companyName">Company name</label>
                        <input type="text" id="companyName" name="companyName" maxlength="150"
                               autocomplete="organization" placeholder="e.g. TCS"
                               value="<c:out value='${param.companyName}' />" required>
                    </div>

                    <div>
                        <label for="location">Location <span class="optional">(optional)</span></label>
                        <input type="text" id="location" name="location" maxlength="100"
                               placeholder="e.g. Bangalore"
                               value="<c:out value='${param.location}' />">
                    </div>

                    <div class="full">
                        <label for="website">Website <span class="optional">(optional)</span></label>
                        <input type="url" id="website" name="website" maxlength="255"
                               placeholder="https://company.com"
                               value="<c:out value='${param.website}' />">
                    </div>

                    <div class="full">
                        <label for="description">About the company <span class="optional">(optional)</span></label>
                        <textarea id="description" name="description"
                                  placeholder="A short description of your company"><c:out value="${param.description}" /></textarea>
                    </div>
                </div>
            </fieldset>

            <button type="submit">Create Account</button>
        </form>

        <p class="auth-switch">
            Already have an account?
            <a href="login.html">Sign in</a>
        </p>
    </main>

    <script>
        (function () {
            const form = document.getElementById("signup-form");
            const sections = form.querySelectorAll("fieldset[data-role]");
            const password = document.getElementById("password");
            const confirm = document.getElementById("confirmPassword");

            function showRole(role) {
                sections.forEach(section => {
                    const active = section.dataset.role === role;
                    section.hidden = !active;
                    section.disabled = !active;
                });
            }

            form.querySelectorAll("input[name='role']").forEach(radio => {
                radio.addEventListener("change", () => showRole(radio.value));
            });

            function checkMatch() {
                confirm.setCustomValidity(
                    confirm.value && confirm.value !== password.value
                        ? "Passwords do not match."
                        : ""
                );
            }

            password.addEventListener("input", checkMatch);
            confirm.addEventListener("input", checkMatch);
        })();
    </script>

</body>
</html>
