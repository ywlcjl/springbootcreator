<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>控制台 | ${title!""}</title>
    <script type="text/javascript" src="/plugins/jquery.min.js"></script>
    <!-- 无需static目录 引入 Bootstrap 5.3 CSS -->
    <link href="/plugins/bootstrap5.3.8/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous">
    <!-- 引入 Bootstrap 5 JS Bundle (包含 Popper) -->
    <script src="/plugins/bootstrap5.3.8/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
    <style>
        body {
            background-color: #f8f9fa;
        }

        .card {
            transition: all 0.3s ease;
        }

        .card:hover {
            transform: none;
            box-shadow: 0 4px 12px rgba(0, 0, 0, .1);
        }
    </style>


</head>
<body>
<!-- 顶栏导航 (Navbar) -->
<nav class="navbar navbar-expand-lg navbar-light bg-white shadow-sm">
    <div class="container-fluid">
        <a class="navbar-brand fw-bold text-primary" href="/admin/dashboard">SpringbootCreator系统</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
                aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link active" aria-current="page" href="/admin/dashboard">控制台</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/admin/article">文章</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/admin/attach">图片</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/admin/articleCategory">分类</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/admin/admin">用户</a>
                </li>
            </ul>
            <div class="d-flex align-items-center">
                <a href="/">网站首页</a>&nbsp;&nbsp;
                <span class="navbar-text me-3 text-muted">
                    欢迎, <strong>${currentAdmin.username!""}</strong>!
                </span>
                <a href="/admin/logout" class="btn btn-outline-danger btn-sm">
                    <i class="bi bi-box-arrow-right"></i> 退出登录
                </a>
            </div>
        </div>
    </div>
</nav>

<!-- 主内容区 -->
<main class="container-fluid py-4">
