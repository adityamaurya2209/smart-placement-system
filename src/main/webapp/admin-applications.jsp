<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.smartplacement.servlet.AdminApplicationsServlet.Application" %>

<!DOCTYPE html>

<html lang="en">

<head>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/inner-ui.css">

    <meta charset="UTF-8">

    <title>View Applications - Smart Placement System</title>

    <style>

        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            font-family: Arial, sans-serif;
            background-color: #f4f6f8;
            color: #1f2937;
        }

        header {
            background-color: #1f2937;
            color: white;
            padding: 25px 35px;
        }

        header h1 {
            margin: 0;
            font-size: 32px;
        }

        .container {
            padding: 35px;
        }

        .top-bar {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 25px;
        }

        .top-bar h2 {
            margin: 0;
        }

        .back-button {
            text-decoration: none;
            background-color: #2563eb;
            color: white;
            padding: 10px 18px;
            border-radius: 6px;
        }

        .back-button:hover {
            background-color: #1d4ed8;
        }

        .application-count {
            margin-bottom: 15px;
            color: #4b5563;
        }

        .table-container {
            background-color: white;
            border-radius: 10px;
            overflow-x: auto;
            box-shadow: 0 3px 12px rgba(0, 0, 0, 0.08);
        }

        table {
            width: 100%;
            border-collapse: collapse;
            min-width: 1100px;
        }

        th {
            background-color: #1f2937;
            color: white;
            text-align: left;
            padding: 16px;
        }

        td {
            padding: 16px;
            border-bottom: 1px solid #e5e7eb;
        }

        tr:hover {
            background-color: #f9fafb;
        }

        .view-button {
            text-decoration: none;
            background-color: #2563eb;
            color: white;
            padding: 7px 14px;
            border-radius: 5px;
        }

        .view-button:hover {
            background-color: #1d4ed8;
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

        .empty {
            text-align: center;
            padding: 40px;
            color: #6b7280;
        }

    </style>

</head>

<body class="sp-inner-page">
<nav class="sp-inner-nav"><div class="nav-inner"><a class="sp-brand" href="admin-dashboard"><span class="sp-brand-mark">SP</span>Smart Placement</a><div class="sp-nav-links"><a href="admin-dashboard">Dashboard</a><a href="admin-students">Students</a><a href="admin-recruiters">Recruiters</a><a href="admin-companies">Companies</a><a href="admin-jobs">Jobs</a><a href="admin-applications">Applications</a><a href="admin-interviews">Interviews</a><a class="logout" href="admin-logout">Logout</a></div></div></nav>


<header>

    <h1>View Applications</h1>

</header>

<div class="container">

    <div class="top-bar">

        <h2>Application List</h2>

        <a href="admin-dashboard"
           class="back-button">

            ← Back to Dashboard

        </a>

    </div>


    <%
        List<Application> applications =
                (List<Application>)
                        request.getAttribute("applications");
    %>


    <%
        if (applications != null &&
                !applications.isEmpty()) {
    %>


        <div class="application-count">

            Total Applications:
            <strong>
                <%= applications.size() %>
            </strong>

        </div>


        <div class="table-container">

            <table>

                <thead>

                    <tr>

                        <th>ID</th>

                        <th>Student</th>

                        <th>Email</th>

                        <th>Job</th>

                        <th>Company</th>

                        <th>Applied On</th>

                        <th>Status</th>

                        <th>Match Score</th>

                        <th>Action</th>

                    </tr>

                </thead>


                <tbody>


                <%
                    /*
                     * IMPORTANT:
                     * Use "app" instead of "application".
                     * "application" is already a built-in JSP object.
                     */

                    for (Application app : applications) {
                %>


                    <tr>


                        <!-- Application ID -->

                        <td>
                            <%= app.getId() %>
                        </td>


                        <!-- Student Name -->

                        <td>

                            <strong>
                                <%= app.getStudentName() %>
                            </strong>

                        </td>


                        <!-- Student Email -->

                        <td>
                            <%= app.getStudentEmail() %>
                        </td>


                        <!-- Job Title -->

                        <td>
                            <%= app.getJobTitle() %>
                        </td>


                        <!-- Company -->

                        <td>
                            <%= app.getCompanyName() %>
                        </td>


                        <!-- Application Date -->

                        <td>
                            <%= app.getApplicationDate() %>
                        </td>


                        <!-- Status -->

                        <td>

                            <%
                                String status =
                                        app.getStatus();

                                String statusClass =
                                        status == null
                                                ? ""
                                                : status.toLowerCase();
                            %>


                            <span class="status <%= statusClass %>">

                                <%= status %>

                            </span>

                        </td>


                        <!-- Match Score -->

                        <td>

                            <%
                                if (app.getMatchScore() != null) {
                            %>

                                <%= app.getMatchScore() %>%

                            <%
                                } else {
                            %>

                                N/A

                            <%
                                }
                            %>

                        </td>


                        <!-- View Button -->

                        <td>

                            <a
                                href="admin-application-details?id=<%= app.getId() %>"
                                class="view-button">

                                View

                            </a>

                        </td>


                    </tr>


                <%
                    }
                %>


                </tbody>

            </table>

        </div>


    <%
        } else {
    %>


        <div class="table-container">

            <div class="empty">

                No applications found.

            </div>

        </div>


    <%
        }
    %>


</div>

</body>

</html>