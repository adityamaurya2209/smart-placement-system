<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>

<html lang="en">

<head>

    <meta charset="UTF-8">

    <title>Interview Details</title>

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
            padding: 25px 40px;
        }

        .header h1 {
            margin: 0;
            font-size: 32px;
        }

        .container {
            padding: 40px;
        }

        .card {
            background: white;
            max-width: 1000px;
            padding: 40px;
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

        .scheduled {
            color: #2563eb;
        }

        .completed {
            color: #15803d;
        }

        .cancelled {
            color: #dc2626;
        }

        .success {
            background: #dcfce7;
            color: #166534;
            border: 1px solid #86efac;
            padding: 15px 20px;
            border-radius: 6px;
            margin-bottom: 30px;
            font-weight: bold;
            font-size: 17px;
        }

        .update-box {
            margin-top: 35px;
            padding: 25px;
            background: #f9fafb;
            border: 1px solid #e5e7eb;
            border-radius: 8px;
        }

        .update-box h2 {
            margin-top: 0;
            margin-bottom: 20px;
        }

        .form-row {
            display: flex;
            align-items: center;
            gap: 20px;
        }

        select {
            padding: 12px;
            font-size: 16px;
            border: 1px solid #d1d5db;
            border-radius: 6px;
            min-width: 220px;
        }

        .update-button {
            padding: 12px 22px;
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

        .back-button {
            display: inline-block;
            margin-top: 30px;
            padding: 12px 20px;
            background: #2563eb;
            color: white;
            text-decoration: none;
            border-radius: 6px;
        }

        .back-button:hover {
            background: #1d4ed8;
        }

    </style>

</head>

<body>

<div class="header">

    <h1>Interview Details</h1>

</div>

<div class="container">

    <div class="card">

        <%
            String success =
                    request.getParameter("success");

            if ("updated".equals(success)) {
        %>

            <div class="success">
                Interview status updated successfully.
            </div>

        <%
            }
        %>


        <div class="row">

            <div class="label">
                Interview ID
            </div>

            <div class="value">
                <%= request.getAttribute("interviewId") %>
            </div>

        </div>


        <div class="row">

            <div class="label">
                Application ID
            </div>

            <div class="value">
                <%= request.getAttribute("applicationId") %>
            </div>

        </div>


        <div class="row">

            <div class="label">
                Student Name
            </div>

            <div class="value">
                <%= request.getAttribute("studentName") %>
            </div>

        </div>


        <div class="row">

            <div class="label">
                Student Email
            </div>

            <div class="value">
                <%= request.getAttribute("studentEmail") %>
            </div>

        </div>


        <div class="row">

            <div class="label">
                Job Title
            </div>

            <div class="value">
                <%= request.getAttribute("jobTitle") %>
            </div>

        </div>


        <div class="row">

            <div class="label">
                Company
            </div>

            <div class="value">
                <%= request.getAttribute("companyName") %>
            </div>

        </div>


        <div class="row">

            <div class="label">
                Interview Date
            </div>

            <div class="value">
                <%= request.getAttribute("interviewDate") %>
            </div>

        </div>


        <div class="row">

            <div class="label">
                Interview Time
            </div>

            <div class="value">
                <%= request.getAttribute("interviewTime") %>
            </div>

        </div>


        <div class="row">

            <div class="label">
                Mode
            </div>

            <div class="value">
                <%= request.getAttribute("mode") %>
            </div>

        </div>


        <div class="row">

            <div class="label">
                Meeting Link
            </div>

            <div class="value">

                <%
                    Object meetingLink =
                            request.getAttribute("meetingLink");

                    if (meetingLink != null &&
                            !String.valueOf(meetingLink).isBlank()) {
                %>

                    <a href="<%= meetingLink %>"
                       target="_blank">
                        <%= meetingLink %>
                    </a>

                <%
                    } else {
                %>

                    N/A

                <%
                    }
                %>

            </div>

        </div>


        <div class="row">

            <div class="label">
                Venue
            </div>

            <div class="value">

                <%
                    Object venue =
                            request.getAttribute("venue");

                    if (venue != null &&
                            !String.valueOf(venue).isBlank()) {
                %>

                    <%= venue %>

                <%
                    } else {
                %>

                    N/A

                <%
                    }
                %>

            </div>

        </div>


        <div class="row">

            <div class="label">
                Status
            </div>

            <div class="value">

                <%
                    String status =
                            String.valueOf(
                                    request.getAttribute("status")
                            );

                    String statusClass =
                            status.toLowerCase();
                %>

                <span class="status <%= statusClass %>">
                    <%= status %>
                </span>

            </div>

        </div>


        <!-- UPDATE INTERVIEW STATUS -->

        <div class="update-box">

            <h2>
                Change Interview Status
            </h2>

            <form method="post"
                  action="admin-update-interview-status">

                <input type="hidden"
                       name="id"
                       value="<%= request.getAttribute("interviewId") %>">

                <div class="form-row">

                    <select name="status">

                        <option value="SCHEDULED"
                            <%= "SCHEDULED".equals(status)
                                ? "selected"
                                : "" %>>
                            SCHEDULED
                        </option>

                        <option value="COMPLETED"
                            <%= "COMPLETED".equals(status)
                                ? "selected"
                                : "" %>>
                            COMPLETED
                        </option>

                        <option value="CANCELLED"
                            <%= "CANCELLED".equals(status)
                                ? "selected"
                                : "" %>>
                            CANCELLED
                        </option>

                    </select>


                    <button type="submit"
                            class="update-button">
                        Update Status
                    </button>

                </div>

            </form>

        </div>


        <a href="admin-interviews"
           class="back-button">

            ← Back to Interviews

        </a>

    </div>

</div>

</body>

</html>