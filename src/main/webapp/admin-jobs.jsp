<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.smartplacement.servlet.AdminJobsServlet.Job" %>

<!DOCTYPE html>

<html lang="en">

<head>

    <meta charset="UTF-8">

    <title>Manage Jobs - Smart Placement System</title>

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

        .job-count {
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

        .status-open {
            color: #15803d;
            font-weight: bold;
        }

        .status-closed {
            color: #dc2626;
            font-weight: bold;
        }

        .empty {
            text-align: center;
            padding: 40px;
            color: #6b7280;
        }

    </style>

</head>

<body>

<header>

    <h1>Manage Jobs</h1>

</header>

<div class="container">

    <div class="top-bar">

        <h2>Job List</h2>

        <a href="admin-dashboard"
           class="back-button">
            ← Back to Dashboard
        </a>

    </div>

    <%
        List<Job> jobs =
                (List<Job>) request.getAttribute("jobs");
    %>

    <%
        if (jobs != null && !jobs.isEmpty()) {
    %>

        <div class="job-count">

            Total Jobs:
            <strong><%= jobs.size() %></strong>

        </div>

        <div class="table-container">

            <table>

                <thead>

                    <tr>
                        <th>ID</th>
                        <th>Company</th>
                        <th>Job Title</th>
                        <th>Location</th>
                        <th>Min CGPA</th>
                        <th>Branch</th>
                        <th>Salary</th>
                        <th>Deadline</th>
                        <th>Status</th>
                        <th>Action</th>
                    </tr>

                </thead>

                <tbody>

                <%
                    for (Job job : jobs) {
                %>

                    <tr>

                        <td>
                            <%= job.getId() %>
                        </td>

                        <td>
                            <strong>
                                <%= job.getCompanyName() %>
                            </strong>
                        </td>

                        <td>
                            <%= job.getTitle() %>
                        </td>

                        <td>
                            <%= job.getLocation() %>
                        </td>

                        <td>
                            <%= job.getMinimumCgpa() %>
                        </td>

                        <td>
                            <%= job.getEligibleBranch() %>
                        </td>

                        <td>
                            <%= job.getSalary() %>
                        </td>

                        <td>
                            <%= job.getApplicationDeadline() %>
                        </td>

                        <td>

                            <%
                                if ("OPEN".equalsIgnoreCase(
                                        job.getStatus())) {
                            %>

                                <span class="status-open">
                                    OPEN
                                </span>

                            <%
                                } else {
                            %>

                                <span class="status-closed">
                                    CLOSED
                                </span>

                            <%
                                }
                            %>

                        </td>

                        <td>

                            <a
                                href="admin-job-details?id=<%= job.getId() %>"
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
                No jobs found.
            </div>

        </div>

    <%
        }
    %>

</div>

</body>

</html>