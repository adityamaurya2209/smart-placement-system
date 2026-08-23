<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">

    <title>Application Details</title>

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
            max-width: 900px;
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

        .status {
            font-weight: bold;
        }

        .applied {
            color: #2563eb;
        }

        .shortlisted {
            color: #ca8a04;
        }

        .interview {
            color: #7c3aed;
        }

        .selected {
            color: #15803d;
        }

        .rejected {
            color: #dc2626;
        }

        .status-form {
            margin-top: 30px;
            padding: 25px;
            background: #f9fafb;
            border-radius: 8px;
            border: 1px solid #e5e7eb;
        }

        .status-form h3 {
            margin-top: 0;
            margin-bottom: 15px;
        }

        .status-form select {
            padding: 11px 14px;
            font-size: 16px;
            border: 1px solid #d1d5db;
            border-radius: 6px;
            min-width: 220px;
        }

        .update-button {
            margin-left: 10px;
            padding: 11px 20px;
            background: #2563eb;
            color: white;
            border: none;
            border-radius: 6px;
            font-size: 16px;
            cursor: pointer;
        }

        .update-button:hover {
            background: #1d4ed8;
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

        .success-message {
            margin-bottom: 20px;
            padding: 14px 18px;
            background: #dcfce7;
            color: #166534;
            border: 1px solid #86efac;
            border-radius: 6px;
            font-weight: bold;
        }

        .error-message {
            margin-bottom: 20px;
            padding: 14px 18px;
            background: #fee2e2;
            color: #991b1b;
            border: 1px solid #fca5a5;
            border-radius: 6px;
            font-weight: bold;
        }

    </style>

</head>

<body>

<div class="header">

    <h1>Application Details</h1>

</div>


<div class="container">

    <div class="card">


        <!-- Success message -->

        <%
            String success =
                    request.getParameter("success");

            if ("updated".equals(success)) {
        %>

            <div class="success-message">
                Application status updated successfully.
            </div>

        <%
            }
        %>


        <!-- Error message -->

        <%
            String error =
                    request.getParameter("error");

            if ("invalid-status".equals(error)) {
        %>

            <div class="error-message">
                Invalid application status.
            </div>

        <%
            } else if ("update-failed".equals(error)) {
        %>

            <div class="error-message">
                Failed to update application status.
            </div>

        <%
            }
        %>


        <!-- Application ID -->

        <div class="row">

            <div class="label">
                Application ID
            </div>

            <div class="value">

                <%= request.getAttribute(
                        "applicationId") %>

            </div>

        </div>


        <!-- Student ID -->

        <div class="row">

            <div class="label">
                Student ID
            </div>

            <div class="value">

                <%= request.getAttribute(
                        "studentId") %>

            </div>

        </div>


        <!-- Student Name -->

        <div class="row">

            <div class="label">
                Student Name
            </div>

            <div class="value">

                <%= request.getAttribute(
                        "studentName") %>

            </div>

        </div>


        <!-- Student Email -->

        <div class="row">

            <div class="label">
                Student Email
            </div>

            <div class="value">

                <%= request.getAttribute(
                        "studentEmail") %>

            </div>

        </div>


        <!-- Job ID -->

        <div class="row">

            <div class="label">
                Job ID
            </div>

            <div class="value">

                <%= request.getAttribute(
                        "jobId") %>

            </div>

        </div>


        <!-- Job Title -->

        <div class="row">

            <div class="label">
                Job Title
            </div>

            <div class="value">

                <%= request.getAttribute(
                        "jobTitle") %>

            </div>

        </div>


        <!-- Company -->

        <div class="row">

            <div class="label">
                Company
            </div>

            <div class="value">

                <%= request.getAttribute(
                        "companyName") %>

            </div>

        </div>


        <!-- Job Location -->

        <div class="row">

            <div class="label">
                Job Location
            </div>

            <div class="value">

                <%= request.getAttribute(
                        "jobLocation") %>

            </div>

        </div>


        <!-- Application Date -->

        <div class="row">

            <div class="label">
                Application Date
            </div>

            <div class="value">

                <%= request.getAttribute(
                        "applicationDate") %>

            </div>

        </div>


        <!-- Current Status -->

        <div class="row">

            <div class="label">
                Status
            </div>

            <div class="value">

                <%
                    String status =
                            String.valueOf(
                                    request.getAttribute(
                                            "status"));

                    String statusClass =
                            status.toLowerCase();
                %>

                <span class="status <%= statusClass %>">

                    <%= status %>

                </span>

            </div>

        </div>


        <!-- Match Score -->

        <div class="row">

            <div class="label">
                Match Score
            </div>

            <div class="value">

                <%
                    Object score =
                            request.getAttribute(
                                    "matchScore");

                    if (score != null) {
                %>

                    <%= score %>%

                <%
                    } else {
                %>

                    N/A

                <%
                    }
                %>

            </div>

        </div>


        <!-- CHANGE STATUS -->

        <div class="status-form">

            <h3>
                Change Application Status
            </h3>

            <form
                    method="post"
                    action="admin-application-details">

                <!-- Application ID -->

                <input
                        type="hidden"
                        name="id"
                        value="<%= request.getAttribute(
                                "applicationId") %>"
                >


                <select name="status" required>

                    <option
                            value="APPLIED"
                            <%= "APPLIED".equals(status)
                                    ? "selected" : "" %>>
                        APPLIED
                    </option>

                    <option
                            value="SHORTLISTED"
                            <%= "SHORTLISTED".equals(status)
                                    ? "selected" : "" %>>
                        SHORTLISTED
                    </option>

                    <option
                            value="INTERVIEW"
                            <%= "INTERVIEW".equals(status)
                                    ? "selected" : "" %>>
                        INTERVIEW
                    </option>

                    <option
                            value="SELECTED"
                            <%= "SELECTED".equals(status)
                                    ? "selected" : "" %>>
                        SELECTED
                    </option>

                    <option
                            value="REJECTED"
                            <%= "REJECTED".equals(status)
                                    ? "selected" : "" %>>
                        REJECTED
                    </option>

                </select>


                <button
                        type="submit"
                        class="update-button">

                    Update Status

                </button>

            </form>

        </div>


        <!-- Back button -->

        <a
                href="admin-applications"
                class="button">

            ← Back to Applications

        </a>


    </div>

</div>

</body>

</html>