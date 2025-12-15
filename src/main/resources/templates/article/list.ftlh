<#include "/layouts/_header.ftlh">

<h1 class="page-header">${title!""}</h1>

<div class="article-list">
    <#list resultPage.records as article>
        <div class="article-item">
            <div class="article-thumb">
                <a href="/article/${article.id}">
                    <img src="/
                    <#if article.coverPic?? && article.coverPic != ''>
                        ${article.coverPic}
                    <#else>images/default_cover.png</#if>" style="max-width: 480px;">
                </a>
            </div>
            <div class="article-content">
                <div>
                    <h2 class="article-title">
                        <a href="/article/${article.id}" class="text-decoration-none">${article.title!''}</a>
                    </h2>
                    <p class="text-secondary d-none d-sm-block">${article.descTxt!'&nbsp;'}</p>
                </div>
                <div class="article-meta">
                        <span class="meta-category">
                        <#assign categoryName = "未分类">
                            <#list allCategories as cat>
                                <#if cat.id == article.categoryId>
                                    <#assign categoryName = cat.name>
                                    <#break>
                                </#if>
                            </#list>
                        <span class="badge">${categoryName}</span>
                        </span>
                    <div class="meta-info">
                        <span class="text-muted me-3">
                            <i class="fas fa-user"></i>${article.author!'佚名'}
                        </span>
                        <span class="text-muted">
                            <i class="fas fa-clock"></i><#if article.postTime??>${FU.formatDateTime(article.postTime, 'yyyy-MM-dd HH:mm')}</#if>
                        </span>
                    </div>
                </div>
            </div>
        </div>
    </#list>
    <#if resultPage.records?size == 0>
        <div class="article-item">暂无文章。</div>
    </#if>
</div>
<#if totalPages gt 1>
    <#include "/layouts/_page_nav.ftlh">
</#if>

<#include "/layouts/_footer.ftlh">