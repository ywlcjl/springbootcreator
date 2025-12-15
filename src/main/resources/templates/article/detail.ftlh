<#include "/layouts/_header.ftlh">
<div class="breadcrumb-container">
    <ul class="breadcrumb-list">
        <li class="breadcrumb-item">
            <a href="/">首页</a>
        </li>
        <li class="breadcrumb-separator">/</li>
        <li class="breadcrumb-item">
            <a href="/article/list">文章列表</a>
        </li>
        <li class="breadcrumb-separator">/</li>
        <li class="breadcrumb-item active">
            ${article.title}
        </li>
    </ul>
</div>

<main class="article-detail-container">
    <h1 class="article-title-detail">${article.title}</h1>

    <div class="article-meta-detail">
        <span><i class="fas fa-user"></i> ${article.author!''}</span>
        <span><i class="fas fa-clock"></i> <#if article.postTime??>${FU.formatDateTime(article.postTime, 'yyyy-MM-dd HH:mm')}</#if></span>
        <span><i class="fas fa-tag"></i> ${category.name!''}</span>
    </div>

    <div class="article-body">
        ${article.content!''}
    </div>
</main>

<#include "/layouts/_footer.ftlh">