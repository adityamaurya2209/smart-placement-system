<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>

<head>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/inner-ui.css">

    <meta charset="UTF-8">

    <title>Applicants</title>

    <style>

        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            padding: 35px;
            font-family: Arial, sans-serif;
            background: #f4f7fb;
            color: #1f2937;
        }

        .page {
            max-width: 1200px;
            margin: auto;
        }

        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 20px;
            margin-bottom: 25px;
        }

        h1 {
            margin: 0;
            font-size: 32px;
        }

        .back-btn {
            text-decoration: none;
            color: #2563eb;
            font-weight: bold;
        }

        .filters {
            background: white;
            padding: 20px;
            border-radius: 14px;
            box-shadow: 0 3px 12px rgba(0,0,0,0.07);
            margin-bottom: 25px;
        }

        .filter-form {
            display: flex;
            gap: 12px;
            flex-wrap: wrap;
        }

        .search-input,
        .status-select {
            padding: 12px 14px;
            border: 1px solid #d1d5db;
            border-radius: 8px;
            font-size: 15px;
        }

        .search-input {
            flex: 1;
            min-width: 250px;
        }

        .status-select {
            min-width: 170px;
        }

        .filter-btn {
            border: none;
            background: #2563eb;
            color: white;
            padding: 12px 20px;
            border-radius: 8px;
            cursor: pointer;
            font-weight: bold;
        }

        .clear-btn {
            text-decoration: none;
            padding: 12px 20px;
            border-radius: 8px;
            background: #e5e7eb;
            color: #374151;
            font-weight: bold;
        }

        .alert {
            padding: 13px 16px;
            border-radius: 8px;
            margin-bottom: 20px;
        }

        .success {
            background: #dcfce7;
            color: #166534;
        }

        .error {
            background: #fee2e2;
            color: #991b1b;
        }

        .applicant-card {
            background: white;
            padding: 25px;
            margin-bottom: 20px;
            border-radius: 14px;
            box-shadow: 0 3px 12px rgba(0,0,0,0.07);
        }

        .top {
            display: flex;
            justify-content: space-between;
            gap: 15px;
            align-items: flex-start;
        }

        .top h2 {
            margin: 0 0 7px;
        }

        .job {
            color: #6b7280;
        }

        .status {
            padding: 7px 12px;
            border-radius: 20px;
            font-size: 13px;
            font-weight: bold;
        }

        .status-APPLIED {
            background: #dbeafe;
            color: #1d4ed8;
        }

        .status-SHORTLISTED {
            background: #fef3c7;
            color: #92400e;
        }

        .status-INTERVIEW {
            background: #ede9fe;
            color: #6d28d9;
        }

        .status-SELECTED {
            background: #dcfce7;
            color: #166534;
        }

        .status-REJECTED {
            background: #fee2e2;
            color: #991b1b;
        }

        .info-grid {
            display: grid;
            grid-template-columns: repeat(3, 1fr);
            gap: 15px;
            margin-top: 22px;
        }

        .info {
            background: #f8fafc;
            padding: 13px;
            border-radius: 8px;
        }

        .label {
            display: block;
            color: #6b7280;
            font-size: 13px;
            margin-bottom: 5px;
        }

        .value {
            font-weight: bold;
        }

        .eligibility {
            margin-top: 18px;
            padding: 15px;
            border-radius: 9px;
        }

        .eligible {
            background: #dcfce7;
            color: #166534;
        }

        .not-eligible {
            background: #fee2e2;
            color: #991b1b;
        }

        .match {
            margin-top: 18px;
        }

        .match-label {
            display: flex;
            justify-content: space-between;
            margin-bottom: 7px;
            font-weight: bold;
        }

        .progress {
            height: 10px;
            background: #e5e7eb;
            border-radius: 10px;
            overflow: hidden;
        }

        .progress-bar {
            height: 100%;
            background: #2563eb;
        }

        .actions {
            display: flex;
            flex-wrap: wrap;
            gap: 10px;
            margin-top: 22px;
            padding-top: 18px;
            border-top: 1px solid #e5e7eb;
        }

        .btn {
            display: inline-block;
            padding: 10px 15px;
            border-radius: 7px;
            text-decoration: none;
            border: none;
            cursor: pointer;
            font-weight: bold;
        }

        .details {
            background: #2563eb;
            color: white;
        }

        .interview {
            background: #16a34a;
            color: white;
        }

        .status-form {
            display: flex;
            gap: 8px;
            flex-wrap: wrap;
        }

        .status-form select {
            padding: 9px;
            border: 1px solid #d1d5db;
            border-radius: 7px;
        }

        .update {
            background: #374151;
            color: white;
        }

        .empty {
            background: white;
            padding: 40px;
            text-align: center;
            border-radius: 14px;
            box-shadow: 0 3px 12px rgba(0,0,0,0.07);
        }

        @media (max-width: 800px) {

            body {
                padding: 20px;
            }

            .info-grid {
                grid-template-columns: 1fr;
            }

            .top {
                flex-direction: column;
            }
        }

    </style>

</head>

<body class="sp-inner-page">
<nav class="sp-inner-nav"><div class="nav-inner"><a class="sp-brand" href="recruiter-dashboard"><span class="sp-brand-mark">SP</span>Smart Placement</a><div class="sp-nav-links"><a href="recruiter-dashboard">Dashboard</a><a href="applicants">Applicants</a><a href="recruiter-jobs">Jobs</a><a href="company-profile">Company</a><a href="recruiter-interviews">Interviews</a><a class="logout" href="recruiter-logout">Logout</a></div></div></nav>


<div class="page">

    <div class="header">

        <div>
            <h1>Applicants</h1>
            <p>Review candidates and manage applications.</p>
        </div>

        <a href="recruiter-dashboard" class="back-btn">
            &larr; Dashboard
        </a>

    </div>

    <c:if test="${param.updated == 'true'}">
        <div class="alert success">
            Application status updated successfully.
        </div>
    </c:if>

    <c:if test="${param.error == 'invalid-transition'}">
        <div class="alert error">
            Invalid application status transition.
        </div>
    </c:if>

    <c:if test="${param.error == 'invalid-status'}">
        <div class="alert error">
            Invalid application status.
        </div>
    </c:if>

    <c:if test="${param.error == 'invalid'}">
        <div class="alert error">
            Invalid request.
        </div>
    </c:if>

    <div class="filters">

        <form class="filter-form" action="applicants" method="get">

            <input
                    class="search-input"
                    type="text"
                    name="search"
                    placeholder="Search name, email, roll number, branch or job..."
                    value="${search}"
            >

            <select class="status-select" name="status">

                <option value="">All Statuses</option>

                <option value="APPLIED"
                    <c:if test="${statusFilter == 'APPLIED'}">selected</c:if>>
                    Applied
                </option>

                <option value="SHORTLISTED"
                    <c:if test="${statusFilter == 'SHORTLISTED'}">selected</c:if>>
                    Shortlisted
                </option>

                <option value="INTERVIEW"
                    <c:if test="${statusFilter == 'INTERVIEW'}">selected</c:if>>
                    Interview
                </option>

                <option value="SELECTED"
                    <c:if test="${statusFilter == 'SELECTED'}">selected</c:if>>
                    Selected
                </option>

                <option value="REJECTED"
                    <c:if test="${statusFilter == 'REJECTED'}">selected</c:if>>
                    Rejected
                </option>

            </select>

            <button type="submit" class="filter-btn">
                Search
            </button>

            <a href="applicants" class="clear-btn">
                Clear
            </a>

        </form>

    </div>

    <c:choose>

        <c:when test="${not empty applications}">

            <c:forEach var="application"
                       items="${applications}">

                <div class="applicant-card">

                    <div class="top">

                        <div>

                            <h2>
                                ${application.studentName}
                            </h2>

                            <div class="job">
                                ${application.jobTitle}
                                &nbsp; | &nbsp;
                                ${application.companyName}
                            </div>

                        </div>

                        <span class="status status-${application.status}">
                            ${application.status}
                        </span>

                    </div>

                    <div class="info-grid">

                        <div class="info">
                            <span class="label">Email</span>
                            <span class="value">
                                ${application.studentEmail}
                            </span>
                        </div>

                        <div class="info">
                            <span class="label">Roll Number</span>
                            <span class="value">
                                ${application.rollNumber}
                            </span>
                        </div>

                        <div class="info">
                            <span class="label">Branch</span>
                            <span class="value">
                                ${application.branch}
                            </span>
                        </div>

                        <div class="info">
                            <span class="label">CGPA</span>
                            <span class="value">
                                ${application.cgpa}
                            </span>
                        </div>

                        <div class="info">
                            <span class="label">Minimum CGPA</span>
                            <span class="value">
                                ${application.minimumCgpa}
                            </span>
                        </div>

                        <div class="info">
                            <span class="label">Application Date</span>
                            <span class="value">
                                ${application.applicationDate}
                            </span>
                        </div>

                    </div>

                    <c:choose>

                        <c:when test="${application.eligible}">

                            <div class="eligibility eligible">
                                ✓ Eligible for this job
                                <br>
                                Required branch:
                                ${application.eligibleBranch}
                            </div>

                        </c:when>

                        <c:otherwise>

                            <div class="eligibility not-eligible">
                                ✕ Not eligible for this job
                                <br>
                                Required branch:
                                ${application.eligibleBranch}
                                |
                                Minimum CGPA:
                                ${application.minimumCgpa}
                            </div>

                        </c:otherwise>

                    </c:choose>

                    <div class="match">

                        <div class="match-label">

                            <span>
                                Skill Match
                            </span>

                            <span>
                                ${application.matchScore}%
                            </span>

                        </div>

                        <div class="progress">

                            <div class="progress-bar"
                                 style="width:${application.matchScore}%;">
                            </div>

                        </div>

                        <small>
                            Required:
                            ${application.requiredSkills}
                        </small>

                    </div>

                    <div class="actions">

                        <form
                                class="status-form"
                                action="update-application-status"
                                method="post">

                            <input
                                    type="hidden"
                                    name="applicationId"
                                    value="${application.id}"
                            >

                            <select name="status">

                                <option value="${application.status}">
                                    Keep: ${application.status}
                                </option>

                                <c:if test="${application.status == 'APPLIED'}">

                                    <option value="SHORTLISTED">
                                        Shortlisted
                                    </option>

                                    <option value="REJECTED">
                                        Rejected
                                    </option>

                                </c:if>

                                <c:if test="${application.status == 'SHORTLISTED'}">

                                    <option value="INTERVIEW">
                                        Interview
                                    </option>

                                    <option value="REJECTED">
                                        Rejected
                                    </option>

                                </c:if>

                                <c:if test="${application.status == 'INTERVIEW'}">

                                    <option value="SELECTED">
                                        Selected
                                    </option>

                                    <option value="REJECTED">
                                        Rejected
                                    </option>

                                </c:if>

                            </select>

                            <button
                                    type="submit"
                                    class="btn update">

                                Update Status

                            </button>

                        </form>

                        <a
                                href="recruiter-application-details?id=${application.id}"
                                class="btn details">

                            View Details

                        </a>

                        <c:if test="${application.status == 'SHORTLISTED'
                                     || application.status == 'INTERVIEW'}">

                            <a
                                    href="schedule-interview?applicationId=${application.id}"
                                    class="btn interview">

                                Schedule Interview

                            </a>

                        </c:if>

                    </div>

                </div>

            </c:forEach>

        </c:when>

        <c:otherwise>

            <div class="empty">

                <h2>No Applicants Found</h2>

                <p>
                    No applications match your current search/filter.
                </p>

            </div>

        </c:otherwise>

    </c:choose>

</div>

</body>

</html>