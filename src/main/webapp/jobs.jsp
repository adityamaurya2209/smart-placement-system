<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Available Jobs | Smart Placement System</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/inner-ui.css">
</head>

<body class="sp-inner-page">

<header class="topbar">
    <div class="topbar-inner">
        <a class="brand" href="dashboard.html">
            <span class="brand-mark">SP</span>
            <span>Smart Placement</span>
        </a>

        <nav class="nav-links">
            <a href="student-profile">Profile</a>
            <a href="jobs">Jobs</a>
            <a href="my-applications">Applications</a>
            <a href="interview-schedule">Interviews</a>
            <a class="nav-logout" href="student-logout">Logout</a>
        </nav>
    </div>
</header>

<main class="page-shell">

    <div class="page-header">
        <div>
            <h1 class="page-title">Available Jobs</h1>
            <p class="page-subtitle">Find placement opportunities that match your profile.</p>
        </div>
    </div>

    <c:choose>

        <c:when test="${empty jobs}">
            <div class="empty-state">
                <h2>No Jobs Available</h2>
                <p>There are currently no open placement opportunities.</p>
            </div>
        </c:when>

        <c:otherwise>

            <c:forEach var="job" items="${jobs}">

                <article class="job-card">

                    <h2>${job.title}</h2>

                    <div class="company">${job.companyName}</div>

                    <div class="job-meta">

                        <div class="job-meta-item">
                            <strong>Location</strong><br>
                            ${job.location}
                        </div>

                        <div class="job-meta-item">
                            <strong>Minimum CGPA</strong><br>
                            ${job.minimumCgpa}
                        </div>

                        <div class="job-meta-item">
                            <strong>Eligible Branch</strong><br>
                            ${job.eligibleBranch}
                        </div>

                        <div class="job-meta-item">
                            <strong>Salary</strong><br>
                            ${job.salary}
                        </div>

                        <div class="job-meta-item">
                            <strong>Required Skills</strong><br>
                            ${job.requiredSkills}
                        </div>

                        <div class="job-meta-item">
                            <strong>Application Deadline</strong><br>
                            ${job.applicationDeadline}
                        </div>

                    </div>

                    <a class="button" href="job-details?id=${job.id}">
                        View Job Details
                    </a>

                </article>

            </c:forEach>

        </c:otherwise>

    </c:choose>

    <a class="back" href="dashboard.html">&larr; Back to Dashboard</a>

</main>

</body>
</html>
