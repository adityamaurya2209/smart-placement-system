<%@ page isELIgnored="false" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard</title>

    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            background: #f4f6f8;
        }

        .header {
            background: #1f2937;
            color: white;
            padding: 20px 40px;
        }

        .header h1 {
            margin: 0;
        }

        .container {
            padding: 30px 40px;
        }

        .welcome {
            margin-bottom: 30px;
        }

        .cards {
            display: grid;
            grid-template-columns: repeat(3, 1fr);
            gap: 20px;
        }

        .card {
            background: white;
            padding: 25px;
            border-radius: 10px;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
        }

        .card h3 {
            margin-top: 0;
            color: #374151;
        }

        .count {
            font-size: 32px;
            font-weight: bold;
            color: #2563eb;
        }

        .menu {
            margin-top: 30px;
            background: white;
            padding: 25px;
            border-radius: 10px;
        }

        .menu a {
            display: inline-block;
            margin-right: 20px;
            margin-bottom: 10px;
            text-decoration: none;
            color: #2563eb;
            font-weight: bold;
        }

        .logout {
            color: #dc2626 !important;
        }
    </style>
</head>

<body>

<div class="header">
    <h1>Admin Dashboard</h1>
</div>

<div class="container">

    <div class="welcome">

        <h2>
            Welcome, ${sessionScope.name}
        </h2>

        <p>
            Smart Placement System Administration Panel
        </p>

    </div>

    <div class="cards">

        <div class="card">
            <h3>Students</h3>

            <div class="count">
                ${studentCount}
            </div>
        </div>

        <div class="card">
            <h3>Recruiters</h3>

            <div class="count">
                ${recruiterCount}
            </div>
        </div>

        <div class="card">
            <h3>Companies</h3>

            <div class="count">
                ${companyCount}
            </div>
        </div>

        <div class="card">
            <h3>Jobs</h3>

            <div class="count">
                ${jobCount}
            </div>
        </div>

        <div class="card">
            <h3>Applications</h3>

            <div class="count">
                ${applicationCount}
            </div>
        </div>

        <div class="card">
            <h3>Interviews</h3>

            <div class="count">
                ${interviewCount}
            </div>
        </div>

    </div>

    <div class="menu">

        <h2>Admin Controls</h2>

        <a href="admin-students">Manage Students</a>

        <a href="admin-recruiters">
            Manage Recruiters
        </a>

        <a href="admin-companies">
            Manage Companies
        </a>

        <a href="admin-jobs">
            Manage Jobs
        </a>

        <a href="admin-applications">
            View Applications
        </a>

        <a href="admin-interviews">
            View Interviews
        </a>

        <a class="logout" href="admin-logout">
            Logout
        </a>

    </div>

</div>

</body>
</html>