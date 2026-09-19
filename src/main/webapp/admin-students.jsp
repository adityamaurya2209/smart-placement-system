<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.smartplacement.servlet.AdminStudentsServlet.Student" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/inner-ui.css">
    <meta charset="UTF-8">
    <title>Manage Students - Smart Placement System</title>

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

        .table-container {
            background-color: white;
            border-radius: 10px;
            overflow: hidden;
            box-shadow: 0 3px 12px rgba(0, 0, 0, 0.08);
        }

        table {
            width: 100%;
            border-collapse: collapse;
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

        .empty {
            text-align: center;
            padding: 40px;
            color: #6b7280;
        }

        .student-count {
            margin-bottom: 15px;
            color: #4b5563;
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
    </style>
</head>

<body class="sp-inner-page">
<nav class="sp-inner-nav"><div class="nav-inner"><a class="sp-brand" href="admin-dashboard"><span class="sp-brand-mark">SP</span>Smart Placement</a><div class="sp-nav-links"><a href="admin-dashboard">Dashboard</a><a href="admin-students">Students</a><a href="admin-recruiters">Recruiters</a><a href="admin-companies">Companies</a><a href="admin-jobs">Jobs</a><a href="admin-applications">Applications</a><a href="admin-interviews">Interviews</a><a class="logout" href="admin-logout">Logout</a></div></div></nav>


<header>
    <h1>Manage Students</h1>
</header>

<div class="container">

    <div class="top-bar">

        <div>
            <h2>Student List</h2>
        </div>

        <a href="admin-dashboard" class="back-button">
            ← Back to Dashboard
        </a>

    </div>

    <%
        List<Student> students =
                (List<Student>) request.getAttribute("students");
    %>

    <%
        if (students != null && !students.isEmpty()) {
    %>

        <div class="student-count">
            Total Students:
            <strong><%= students.size() %></strong>
        </div>

        <div class="table-container">

            <table>

                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Registered On</th>
                        <th>Action</th>
                    </tr>
                </thead>

                <tbody>

                <%
                    for (Student student : students) {
                %>

                    <tr>

                        <td>
                            <%= student.getId() %>
                        </td>

                        <td>
                            <strong>
                                <%= student.getName() %>
                            </strong>
                        </td>

                        <td>
                            <%= student.getEmail() %>
                        </td>

                        <td>
                            <%= student.getCreatedAt() %>
                        </td>

                        <td>
                            <a
                                href="admin-student-details?id=<%= student.getId() %>"
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
                No students found.
            </div>

        </div>

    <%
        }
    %>

</div>

</body>
</html>