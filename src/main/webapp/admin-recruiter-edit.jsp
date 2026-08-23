<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>

<html lang="en">

<head>

    <meta charset="UTF-8">

    <title>Edit Recruiter</title>

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
            max-width: 700px;
            padding: 35px;
            border-radius: 10px;
            box-shadow: 0 3px 12px rgba(0, 0, 0, 0.08);
        }

        .form-group {
            margin-bottom: 25px;
        }

        label {
            display: block;
            font-weight: bold;
            color: #374151;
            font-size: 18px;
            margin-bottom: 8px;
        }

        input {
            width: 100%;
            padding: 12px 14px;
            font-size: 17px;
            border: 1px solid #d1d5db;
            border-radius: 6px;
            outline: none;
        }

        input:focus {
            border-color: #2563eb;
        }

        .button {
            display: inline-block;
            padding: 12px 20px;
            background: #2563eb;
            color: white;
            text-decoration: none;
            border: none;
            border-radius: 6px;
            font-size: 16px;
            cursor: pointer;
            margin-right: 10px;
        }

        .button:hover {
            background: #1d4ed8;
        }

        .cancel-button {
            background: #6b7280;
        }

        .cancel-button:hover {
            background: #4b5563;
        }

        .error-message {
            background: #fee2e2;
            color: #991b1b;
            border: 1px solid #fca5a5;
            padding: 15px;
            border-radius: 6px;
            margin-bottom: 25px;
            font-weight: bold;
        }

    </style>

</head>

<body>

<div class="header">

    <h1>
        Edit Recruiter
    </h1>

</div>


<div class="container">

    <div class="card">


        <%
            String error = request.getParameter("error");

            if ("email-exists".equals(error)) {
        %>

            <div class="error-message">

                This email is already registered
                with another account.

            </div>

        <%
            } else if ("update-failed".equals(error)) {
        %>

            <div class="error-message">

                Failed to update recruiter.

            </div>

        <%
            }
        %>


        <form
                method="post"
                action="admin-recruiter-update">


            <!-- Recruiter ID -->

            <input
                    type="hidden"
                    name="id"
                    value="<%= request.getAttribute("recruiterId") %>"
            >


            <!-- Name -->

            <div class="form-group">

                <label for="name">
                    Recruiter Name
                </label>

                <input
                        type="text"
                        id="name"
                        name="name"
                        value="<%= request.getAttribute("recruiterName") %>"
                        required
                >

            </div>


            <!-- Email -->

            <div class="form-group">

                <label for="email">
                    Email
                </label>

                <input
                        type="email"
                        id="email"
                        name="email"
                        value="<%= request.getAttribute("recruiterEmail") %>"
                        required
                >

            </div>


            <!-- Buttons -->

            <button
                    type="submit"
                    class="button">

                Update Recruiter

            </button>


            <a
                    href="admin-recruiter-details?id=<%= request.getAttribute("recruiterId") %>"
                    class="button cancel-button">

                Cancel

            </a>


        </form>

    </div>

</div>

</body>

</html>