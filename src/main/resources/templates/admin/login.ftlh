<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SpringbootCreator登录</title>

    <!-- 引入 Bootstrap 5.3 CSS -->
    <link href="/plugins/bootstrap5.3.8/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous">

    <style>
        body {
            /* 背景从蓝色色调改为绿色色调 */
            background: linear-gradient(135deg, #100c0c 0%, #545e6a 100%);
            min-height: 100vh;
        }
        .login-card {
            max-width: 420px;
            padding: 2.5rem;
            border-radius: 1rem;
        }
        .form-control:focus {
            box-shadow: 0 0 0 0.25rem rgba(59, 130, 246, 0.25);
            border-color: #3b82f6;
        }
    </style>


</head>
<body class="d-flex align-items-center justify-content-center">

<div class="card login-card shadow-lg border-0">
    <div class="card-body">
        <h2 class="text-center mb-4 text-primary fw-bold">SpringbootCreator系统登录</h2>
        <#if message??>
            <div class="alert alert-<#if success?? && success == 1>info<#else>danger</#if> text-center" role="alert">
                ${message!""}
            </div>
        </#if>

<form method="post" action="/admin/login">
    <input type="hidden" name="redirect" value="${redirect!''}">
    <div class="mb-3">
        <label for="username" class="form-label fw-semibold">用户名</label>
        <input type="text" id="username" name="username" class="form-control" placeholder="请输入用户名" required value="${username!""}">
    </div>
    <div class="mb-4">
        <label for="password" class="form-label fw-semibold">密码</label>
        <input type="password" id="password" name="password" class="form-control" placeholder="请输入密码" required>
    </div>

    <button type="submit" class="btn btn-primary w-100 py-2 fw-semibold">登录</button>
</form>

<p class="mt-4 text-center text-sm text-muted">
</p>
</div>
</div>

<script src="/plugins/bootstrap5.3.8/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>

</body>
</html>