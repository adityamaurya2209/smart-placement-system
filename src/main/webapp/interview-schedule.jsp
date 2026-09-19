<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Interview Schedule | Smart Placement System</title>
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
            <h1 class="page-title">Interview Schedule</h1>
            <p class="page-subtitle">Keep track of your placement interviews and meeting details.</p>
        </div>
    </div>

    <c:choose>

        <c:when test="${empty interviews}">
            <div class="empty-state">
                <h2>No Interviews Scheduled</h2>
                <p>No interviews have been scheduled for your applications yet.</p>
            </div>
        </c:when>

        <c:otherwise>

            <c:forEach var="interview" items="${interviews}">

                <article class="interview">

                    <div class="interview-head">

                        <div>
                            <h2>${interview.jobTitle}</h2>
                            <div class="company">${interview.companyName}</div>
                        </div>

                        <span class="status ${interview.status}">
                            ${interview.status}
                        </span>

                    </div>

                    <div class="job-meta">

                        <div class="job-meta-item">
                            <strong>Date</strong><br>
                            ${interview.interviewDate}
                        </div>

                        <div class="job-meta-item">
                            <strong>Time</strong><br>
                            ${interview.interviewTime}
                        </div>

                        <div class="job-meta-item">
                            <strong>Mode</strong><br>
                            ${interview.mode}
                        </div>

                        <c:if test="${interview.mode == 'OFFLINE' && not empty interview.venue}">
                            <div class="job-meta-item">
                                <strong>Venue</strong><br>
                                ${interview.venue}
                            </div>
                        </c:if>

                    </div>

                    <c:if test="${interview.mode == 'ONLINE' && not empty interview.meetingLink}">
                        <a class="meeting"
                           href="${interview.meetingLink}"
                           target="_blank"
                           rel="noopener noreferrer">
                            Join Interview
                        </a>
                    </c:if>

                </article>

            </c:forEach>

        </c:otherwise>

    </c:choose>

    <a class="back" href="dashboard.html">&larr; Back to Dashboard</a>

</main>

</body>
</html>
