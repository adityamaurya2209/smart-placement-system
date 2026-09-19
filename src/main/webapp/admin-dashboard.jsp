<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard | Smart Placement System</title>
    <link rel="stylesheet" href="css/style.css?v=5">
</head>
<body class="dashboard-page admin-dashboard-page">
<header class="app-nav">
  <div class="app-nav-inner">
    <a class="app-brand" href="admin-dashboard"><span class="brand-mark">SP</span><span>Smart Placement</span></a>
    <nav class="app-nav-links">
      <a class="active" href="admin-dashboard">Dashboard</a>
      <a href="admin-students">Students</a>
      <a href="admin-recruiters">Recruiters</a>
      <a href="admin-companies">Companies</a>
      <a href="admin-jobs">Jobs</a>
      <a href="admin-applications">Applications</a>
      <a href="admin-interviews">Interviews</a>
      <a class="logout-link" href="admin-logout">Logout</a>
    </nav>
  </div>
</header>
<main class="app-main">
  <section class="dashboard-hero admin-hero">
    <div>
      <span class="eyebrow">ADMINISTRATION</span>
      <h1>Welcome, ${sessionScope.name}</h1>
      <p>Manage the complete placement ecosystem from one centralized dashboard.</p>
    </div>
    <div class="hero-badge">Placement Admin</div>
  </section>
  <section class="stats-grid admin-stats">
    <a class="stat-card" href="admin-students"><span class="stat-icon">ST</span><span class="stat-label">Students</span><strong>${studentCount}</strong><span class="stat-link">Manage students →</span></a>
    <a class="stat-card" href="admin-recruiters"><span class="stat-icon">RC</span><span class="stat-label">Recruiters</span><strong>${recruiterCount}</strong><span class="stat-link">Manage recruiters →</span></a>
    <a class="stat-card" href="admin-companies"><span class="stat-icon">CO</span><span class="stat-label">Companies</span><strong>${companyCount}</strong><span class="stat-link">View companies →</span></a>
    <a class="stat-card" href="admin-jobs"><span class="stat-icon">JB</span><span class="stat-label">Jobs</span><strong>${jobCount}</strong><span class="stat-link">Manage jobs →</span></a>
    <a class="stat-card" href="admin-applications"><span class="stat-icon">AP</span><span class="stat-label">Applications</span><strong>${applicationCount}</strong><span class="stat-link">View applications →</span></a>
    <a class="stat-card" href="admin-interviews"><span class="stat-icon">IV</span><span class="stat-label">Interviews</span><strong>${interviewCount}</strong><span class="stat-link">View interviews →</span></a>
  </section>
  <section class="section-heading"><div><span class="eyebrow dark">QUICK ACCESS</span><h2>Admin Controls</h2></div></section>
  <section class="quick-grid">
    <a class="quick-card" href="admin-students"><span class="quick-icon">01</span><h3>Manage Students</h3><p>Review student records and profile information.</p></a>
    <a class="quick-card" href="admin-recruiters"><span class="quick-icon">02</span><h3>Manage Recruiters</h3><p>Manage recruiter accounts and access.</p></a>
    <a class="quick-card" href="admin-companies"><span class="quick-icon">03</span><h3>Manage Companies</h3><p>Review registered companies and details.</p></a>
    <a class="quick-card" href="admin-jobs"><span class="quick-icon">04</span><h3>Manage Jobs</h3><p>Monitor job postings and job details.</p></a>
    <a class="quick-card" href="admin-applications"><span class="quick-icon">05</span><h3>Applications</h3><p>Track applications across the placement process.</p></a>
    <a class="quick-card" href="admin-interviews"><span class="quick-icon">06</span><h3>Interviews</h3><p>Review scheduled and completed interviews.</p></a>
  </section>
</main>
<footer class="app-footer">Smart Placement System · Administration Panel</footer>
</body>
</html>
