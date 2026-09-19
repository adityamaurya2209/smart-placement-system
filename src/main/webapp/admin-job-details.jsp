<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>

<html lang="en">

<head>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/inner-ui.css">

    <meta charset="UTF-8">

    <title>Job Details</title>

    <style>

        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            font-family: Arial, sans-serif;
            background: #f4f6f8;
            color: #1f2937;
        }

        .header {
            background: #1f2937;
            color: white;
            padding: 25px 35px;
        }

        .header h1 {
            margin: 0;
            font-size: 32px;
        }

        .container {
            padding: 35px;
        }

        .card {
            background: white;
            max-width: 950px;
            padding: 35px;
            border-radius: 10px;
            box-shadow: 0 3px 12px rgba(0, 0, 0, 0.08);
        }

        .row {
            padding: 20px 0;
            border-bottom: 1px solid #e5e7eb;
        }

        .label {
            font-weight: bold;
            color: #374151;
            font-size: 18px;
        }

        .value {
            margin-top: 8px;
            font-size: 20px;
            color: #111827;
        }

        .description {
            line-height: 1.6;
        }

        .open {
            color: #15803d;
            font-weight: bold;
        }

        .closed {
            color: #dc2626;
            font-weight: bold;
        }

        .button {
            display: inline-block;
            margin-top: 30px;
            padding: 12px 20px;
            background: #2563eb;
            color: white;
            text-decoration: none;
            border-radius: 6px;
        }

        .button:hover {
            background: #1d4ed8;
        }

    </style>

</head>

<body class="sp-inner-page">
<nav class="sp-inner-nav"><div class="nav-inner"><a class="sp-brand" href="admin-dashboard"><span class="sp-brand-mark">SP</span>Smart Placement</a><div class="sp-nav-links"><a href="admin-dashboard">Dashboard</a><a href="admin-students">Students</a><a href="admin-recruiters">Recruiters</a><a href="admin-companies">Companies</a><a href="admin-jobs">Jobs</a><a href="admin-applications">Applications</a><a href="admin-interviews">Interviews</a><a class="logout" href="admin-logout">Logout</a></div></div></nav>


<div class="header">

    <h1>Job Details</h1>

</div>

<div class="container">

    <div class="card">

        <div class="row">

            <div class="label">Job ID</div>

            <div class="value">
                <%= request.getAttribute("jobId") %>
            </div>

        </div>

        <div class="row">

            <div class="label">Company</div>

            <div class="value">
                <%= request.getAttribute("companyName") %>
            </div>

        </div>

        <div class="row">

            <div class="label">Job Title</div>

            <div class="value">
                <%= request.getAttribute("jobTitle") %>
            </div>

        </div>

        <div class="row">

            <div class="label">Description</div>

            <div class="value description">
                <%= request.getAttribute("jobDescription") %>
            </div>

        </div>

        <div class="row">

            <div class="label">Location</div>

            <div class="value">
                <%= request.getAttribute("jobLocation") %>
            </div>

        </div>

        <div class="row">

            <div class="label">Minimum CGPA</div>

            <div class="value">
                <%= request.getAttribute("minimumCgpa") %>
            </div>

        </div>

        <div class="row">

            <div class="label">Eligible Branch</div>

            <div class="value">
                <%= request.getAttribute("eligibleBranch") %>
            </div>

        </div>

        <div class="row">

            <div class="label">Required Skills</div>

            <div class="value">
                <%= request.getAttribute("requiredSkills") %>
            </div>

        </div>

        <div class="row">

            <div class="label">Salary</div>

            <div class="value">
                <%= request.getAttribute("salary") %>
            </div>

        </div>

        <div class="row">

            <div class="label">Application Deadline</div>

            <div class="value">
                <%= request.getAttribute("applicationDeadline") %>
            </div>

        </div>

        <div class="row">

            <div class="label">Status</div>

            <div class="value">

                <%
                    String status =
                            String.valueOf(
                                    request.getAttribute("status"));

                    if ("OPEN".equalsIgnoreCase(status)) {
                %>

                    <span class="open">OPEN</span>

                <%
                    } else {
                %>

                    <span class="closed">CLOSED</span>

                <%
                    }
                %>

            </div>

        </div>

        <div class="row">

            <div class="label">Created On</div>

            <div class="value">
                <%= request.getAttribute("jobCreatedAt") %>
            </div>

        </div>

        <a href="admin-jobs"
           class="button">

            ← Back to Jobs

        </a>

    </div>

</div>

</body>

</html>