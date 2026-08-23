<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>

<html lang="en">

<head>

    <meta charset="UTF-8">

    <title>
        Recruiter Details
    </title>

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

        .button {
            display: inline-block;
            margin-top: 30px;
            margin-right: 10px;
            padding: 12px 20px;
            background: #2563eb;
            color: white;
            text-decoration: none;
            border-radius: 6px;
            border: none;
            font-size: 16px;
            cursor: pointer;
        }

        .button:hover {
            background: #1d4ed8;
        }

        .delete-button {
            display: inline-block;
            margin-top: 30px;
            padding: 12px 20px;
            background: #dc2626;
            color: white;
            border: none;
            border-radius: 6px;
            font-size: 16px;
            cursor: pointer;
        }

        .delete-button:hover {
            background: #b91c1c;
        }

        .success-message {
            background: #dcfce7;
            color: #166534;
            border: 1px solid #86efac;
            padding: 15px 18px;
            border-radius: 6px;
            margin-bottom: 25px;
            font-weight: bold;
        }

        .error-message {
            background: #fee2e2;
            color: #991b1b;
            border: 1px solid #fca5a5;
            padding: 15px 18px;
            border-radius: 6px;
            margin-bottom: 25px;
            font-weight: bold;
        }

        .button-container {
            margin-top: 5px;
        }

    </style>

</head>

<body>


<div class="header">

    <h1>
        Recruiter Details
    </h1>

</div>


<div class="container">

    <div class="card">


        <!-- Success Message -->

        <%
            String success =
                    request.getParameter("success");

            if ("updated".equals(success)) {
        %>

            <div class="success-message">

                Recruiter updated successfully.

            </div>

        <%
            }
        %>


        <!-- Error Message -->

        <%
            String error =
                    request.getParameter("error");

            if ("company-linked".equals(error)) {
        %>

            <div class="error-message">

                This recruiter cannot be deleted because
                a company is currently associated with
                this recruiter.

            </div>

        <%
            } else if ("delete-failed".equals(error)) {
        %>

            <div class="error-message">

                Failed to delete recruiter.

            </div>

        <%
            }
        %>


        <!-- Recruiter ID -->

        <div class="row">

            <div class="label">

                Recruiter ID

            </div>

            <div class="value">

                <%= request.getAttribute(
                        "recruiterId") %>

            </div>

        </div>


        <!-- Recruiter Name -->

        <div class="row">

            <div class="label">

                Name

            </div>

            <div class="value">

                <%= request.getAttribute(
                        "recruiterName") %>

            </div>

        </div>


        <!-- Recruiter Email -->

        <div class="row">

            <div class="label">

                Email

            </div>

            <div class="value">

                <%= request.getAttribute(
                        "recruiterEmail") %>

            </div>

        </div>


        <!-- Registered Date -->

        <div class="row">

            <div class="label">

                Registered On

            </div>

            <div class="value">

                <%= request.getAttribute(
                        "recruiterCreatedAt") %>

            </div>

        </div>


        <!-- Buttons -->

        <div class="button-container">


            <!-- Edit -->

            <a
                    href="admin-recruiter-edit?id=<%= request.getAttribute("recruiterId") %>"
                    class="button">

                Edit Recruiter

            </a>


            <!-- Delete -->

            <form
                    method="post"
                    action="admin-recruiter-delete"
                    style="display:inline;"
                    onsubmit="return confirm('Are you sure you want to delete this recruiter?');">

                <input
                        type="hidden"
                        name="id"
                        value="<%= request.getAttribute("recruiterId") %>"
                >

                <button
                        type="submit"
                        class="delete-button">

                    Delete Recruiter

                </button>

            </form>


            <!-- Back -->

            <a
                    href="admin-recruiters"
                    class="button">

                ← Back to Recruiters

            </a>


        </div>


    </div>

</div>


</body>

</html>