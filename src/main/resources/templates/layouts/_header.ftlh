<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${title!''}</title>
    <link href="/css/style.css" rel="stylesheet" crossorigin="anonymous">
</head>
<body>
<nav class="navbar">
    <div class="container">
        <div class="header-links">
            <span class="navbar-brand">
                <a href="/">🚀 Spring Boot Creator</a>
            </span>

            <a href="/article/list">示例文章列表</a>
        </div>
        <div class="nav-links">
            <#if (currentAdmin.username)??>
                欢迎回来, <b>${currentAdmin.username}</b>
                <a href="/admin/dashboard">控制台</a>
                <a href="/admin/logout">登出</a>
            <#else>
                <a href="/admin/login" class="btn-primary">登录</a>
            </#if>
        </div>
    </div>
</nav>
