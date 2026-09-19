<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>

<head>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/inner-ui.css">

    <meta charset="UTF-8">

    <title>Applicant Details</title>

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

        .container {
            max-width: 900px;
            margin: auto;
        }

        .card {
            background: white;
            padding: 30px;
            border-radius: 15px;
            box-shadow: 0 3px 14px rgba(0,0,0,0.08);
            margin-bottom: 20px;
        }

        .header {
            display: flex;
            justify-content: space-between;
            gap: 20px;
            align-items: flex-start;
        }

        h1 {
            margin: 0 0 8px;
        }

        .subtitle {
            color: #6b7280;
        }

        .status {
            padding: 8px 14px;
            border-radius: 20px;
            font-size: 13px;
            font-weight: bold;
        }

        .APPLIED {
            background: #dbeafe;
            color: #1d4ed8;
        }

        .SHORTLISTED {
            background: #fef3c7;
            color: #92400e;
        }

        .INTERVIEW {
            background: #ede9fe;
            color: #6d28d9;
        }

        .SELECTED {
            background: #dcfce7;
            color: #166534;
        }

        .REJECTED {
            background: #fee2e2;
            color: #991b1b;
        }

        .section-title {
            margin-top: 0;
            margin-bottom: 20px;
        }

        .grid {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 15px;
        }

        .field {
            background: #f8fafc;
            padding: 15px;
            border-radius: 9px;
        }

        .label {
            display: block;
            color: #6b7280;
            font-size: 13px;
            margin-bottom: 6px;
        }

        .value {
            font-weight: bold;
            word-break: break-word;
        }

        .full {
            grid-column: 1 / -1;
        }

        .eligibility {
            padding: 15px;
            border-radius: 9px;
            margin-top: 20px;
        }

        .eligible {
            background: #dcfce7;
            color: #166534;
        }

        .not-eligible {
            background: #fee2e2;
            color: #991b1b;
        }

        .match-box {
            margin-top: 20px;
        }

        .match-header {
            display: flex;
            justify-content: space-between;
            font-weight: bold;
            margin-bottom: 8px;
        }

        .progress {
            height: 12px;
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
            gap: 10px;
            flex-wrap: wrap;
        }

        .button {
            display: inline-block;
            padding: 11px 17px;
            border-radius: 8px;
            text-decoration: none;
            font-weight: bold;
        }

        .primary {
            background: #2563eb;
            color: white;
        }

        .success {
            background: #16a34a;
            color: white;
        }

        .secondary {
            background: #e5e7eb;
            color: #374151;
        }

        @media (max-width: 700px) {

            body {
                padding: 20px;
            }

            .grid {
                grid-template-columns: 1fr;
            }

            .full {
                grid-column: auto;
            }

            .header {
                flex-direction: column;
            }
        }

    </style>

</head>

<body class="sp-inner-page">
<nav class="sp-inner-nav"><div class="nav-inner"><a class="sp-brand" href="recruiter-dashboard"><span class="sp-brand-mark">SP</span>Smart Placement</a><div class="sp-nav-links"><a href="recruiter-dashboard">Dashboard</a><a href="applicants">Applicants</a><a href="recruiter-jobs">Jobs</a><a href="company-profile">Company</a><a href="recruiter-interviews">Interviews</a><a class="logout" href="recruiter-logout">Logout</a></div></div></nav>


<div class="container">

    <div class="card">

        <div class="header">

            <div>

                <h1>
                    ${application.studentName}
                </h1>

                <div class="subtitle">
                    ${application.jobTitle}
                    |
                    ${application.companyName}
                </div>

            </div>

            <span class="status ${application.status}">
                ${application.status}
            </span>

        </div>

    </div>

    <div class="card">

        <h2 class="section-title">
            Student Information
        </h2>

        <div class="grid">

            <div class="field">

                <span class="label">
                    Full Name
                </span>

                <span class="value">
                    ${application.studentName}
                </span>

            </div>

            <div class="field">

                <span class="label">
                    Email
                </span>

                <span class="value">
                    ${application.studentEmail}
                </span>

            </div>

            <div class="field">

                <span class="label">
                    Roll Number
                </span>

                <span class="value">
                    ${application.rollNumber}
                </span>

            </div>

            <div class="field">

                <span class="label">
                    Branch
                </span>

                <span class="value">
                    ${application.branch}
                </span>

            </div>

            <div class="field">

                <span class="label">
                    CGPA
                </span>

                <span class="value">
                    ${application.cgpa}
                </span>

            </div>

            <div class="field">

                <span class="label">
                    Phone
                </span>

                <span class="value">
                    ${application.phone}
                </span>

            </div>

            <div class="field full">

                <span class="label">
                    Skills
                </span>

                <span class="value">
                    ${application.skills}
                </span>

            </div>

            <div class="field full">

                <span class="label">
                    Certifications
                </span>

                <span class="value">
                    ${application.certifications}
                </span>

            </div>

            <div class="field full">

                <span class="label">
                    Resume
                </span>

                <span class="value">

                    <c:choose>

                        <c:when test="${not empty application.resumePath}">
                            ${application.resumePath}
                        </c:when>

                        <c:otherwise>
                            Not uploaded
                        </c:otherwise>

                    </c:choose>

                </span>

            </div>

        </div>

    </div>

    <div class="card">

        <h2 class="section-title">
            Job & Application
        </h2>

        <div class="grid">

            <div class="field">

                <span class="label">
                    Applied For
                </span>

                <span class="value">
                    ${application.jobTitle}
                </span>

            </div>

            <div class="field">

                <span class="label">
                    Company
                </span>

                <span class="value">
                    ${application.companyName}
                </span>

            </div>

            <div class="field">

                <span class="label">
                    Application Date
                </span>

                <span class="value">
                    ${application.applicationDate}
                </span>

            </div>

            <div class="field">

                <span class="label">
                    Minimum CGPA
                </span>

                <span class="value">
                    ${application.minimumCgpa}
                </span>

            </div>

            <div class="field full">

                <span class="label">
                    Eligible Branches
                </span>

                <span class="value">
                    ${application.eligibleBranch}
                </span>

            </div>

            <div class="field full">

                <span class="label">
                    Required Skills
                </span>

                <span class="value">
                    ${application.requiredSkills}
                </span>

            </div>

        </div>

        <c:choose>

            <c:when test="${application.eligible}">

                <div class="eligibility eligible">
                    ✓ Candidate meets the CGPA and branch eligibility criteria.
                </div>

            </c:when>

            <c:otherwise>

                <div class="eligibility not-eligible">
                    ✕ Candidate does not meet the CGPA and/or branch eligibility criteria.
                </div>

            </c:otherwise>

        </c:choose>

        <div class="match-box">

            <div class="match-header">

                <span>
                    Skill Match
                </span>

                <span>
                    ${application.matchScore}%
                </span>

            </div>

            <div class="progress">

                <div
                        class="progress-bar"
                        style="width:${application.matchScore}%;">
                </div>

            </div>

        </div>

    </div>

    <div class="card">

        <div class="actions">

            <c:if test="${application.status == 'SHORTLISTED'
                         || application.status == 'INTERVIEW'}">

                <a
                        class="button success"
                        href="schedule-interview?applicationId=${application.id}">

                    Schedule Interview

                </a>

            </c:if>

            <a
                    class="button secondary"
                    href="applicants">

                &larr; Back to Applicants

            </a>

        </div>

    </div>

</div>

</body>

</html>