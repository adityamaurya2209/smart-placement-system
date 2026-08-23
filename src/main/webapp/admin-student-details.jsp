<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Student Details</title>

    <style>
        body {
            margin: 0;
            font-family: Arial, sans-serif;
            background: #f4f6f8;
        }

        .header {
            background: #1f2937;
            color: white;
            padding: 25px 35px;
        }

        .header h1 {
            margin: 0;
        }

        .container {
            padding: 35px;
        }

        .card {
            background: white;
            max-width: 700px;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 3px 12px rgba(0,0,0,.08);
        }

        .row {
            padding: 18px 0;
            border-bottom: 1px solid #e5e7eb;
        }

        .label {
            font-weight: bold;
            color: #374151;
        }

        .value {
            margin-top: 6px;
            font-size: 18px;
        }

        .button {
            display: inline-block;
            margin-top: 25px;
            padding: 11px 18px;
            background: #2563eb;
            color: white;
            text-decoration: none;
            border-radius: 6px;
        }
    </style>
</head>

<body>

<div class="header">
    <h1>Student Details</h1>
</div>

<div class="container">

    <div class="card">

        <div class="row">
            <div class="label">Student ID</div>
            <div class="value"><%= request.getAttribute("studentId") %></div>
        </div>

        <div class="row">
            <div class="label">Name</div>
            <div class="value"><%= request.getAttribute("studentName") %></div>
        </div>

        <div class="row">
            <div class="label">Email</div>
            <div class="value"><%= request.getAttribute("studentEmail") %></div>
        </div>

        <div class="row">
            <div class="label">Registered On</div>
            <div class="value"><%= request.getAttribute("studentCreatedAt") %></div>
        </div>

        <a href="admin-students" class="button">
            ← Back to Students
        </a>

    </div>

</div>

</body>
</html>