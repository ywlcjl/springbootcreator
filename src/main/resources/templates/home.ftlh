<#include "/layouts/_header.ftlh">

<header class="hero-section">
    <div class="hero-content">
        <h1>${title!""}</h1>
        <p>
            欢迎使用 Spring Boot Creator. 我们为您提供快速, 稳定, 可定制的后台管理解决方案.
        </p>
        <a href="/admin/login" class="btn-primary" style="padding: 12px 30px; font-size: 1.1rem; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);">
            立即登录控制台 &rarr;
        </a>
        <p>管理员账号:admin 密码:123456</p>
        <p>测试账号:test 密码:123456</p>
    </div>
</header>

<#include "/layouts/_footer.ftlh">