<#include "/admin/layouts/_header.ftlh">
<div class="d-flex justify-content-between align-items-center mb-4">
    <h2>${title}</h2>
    <a href="/admin/article/edit" class="btn btn-primary">
        <i class="bi bi-plus-circle"></i> 新增文章
    </a>
</div>

<#if message??>
    <div class="alert alert-<#if success?? && success==1>success<#else>danger</#if> alert-dismissible fade show"
         role="alert">
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
</#if>

<div class="bg-white shadow-sm rounded-3 p-4">
    <div class="table-responsive">
        <table class="table table-hover align-middle">
            <thead class="table-light">
            <tr>
                <th>ID</th>
                <th>标题</th>
                <th>分类</th>
                <th>发布时间</th>
                <th>状态</th>
                <th>创建者ID</th>
                <th>创建时间</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <#list resultPage.records as article>
                <tr>
                    <td>${article.id!''}</td>
                    <td class="text-truncate" style="max-width: 300px;">
                        <a href="/article/${article.id}" target="_blank" class="text-decoration-none">
                            ${article.title!''}</a>
                    </td>
                    <td>
                        <#assign categoryName = "未分类">
                        <#list allArticleCategories as cat>
                            <#if cat.id == article.categoryId>
                                <#assign categoryName = cat.name>
                                <#break>
                            </#if>
                        </#list>
                        <span class="badge bg-secondary">${categoryName}</span>
                    </td>
                    <td><#if article.postTime??>${FU.formatDateTime(article.postTime)}</#if></td>
                    <td>
                        <#if article.status == 1>
                            <span class="badge bg-success">已发布</span>
                        <#else>
                            <span class="badge bg-warning text-dark">草稿</span>
                        </#if>
                    </td>
                    <td>${article.adminId!''}</td>
                    <td><#if article.createTime??>${FU.formatDateTime(article.createTime,'yyyy-MM-dd HH:mm:ss')}</#if></td>
                    <td>
                        <a href="/admin/article/edit?id=${article.id}"
                           class="btn btn-sm btn-info text-white me-2">编辑</a>
                        <form action="/admin/article/delete?id=${article.id}" method="POST" class="d-inline">
                            <button type="submit" class="btn btn-sm btn-danger"
                                    onclick="return confirm('确定要删除这篇文章吗？')">删除
                            </button>
                        </form>
                    </td>
                </tr>
            </#list>
            <#if resultPage.records?size == 0>
                <tr>
                    <td colspan="7" class="text-center text-muted py-4">暂无文章数据。请点击 "新增文章" 创建。</td>
                </tr>
            </#if>
            </tbody>
        </table>

    </div>

    <#-- 分页导航 -->
    <#if totalPages gt 1>
        <#include "/admin/layouts/_page_nav.ftlh">
    </#if>

    <div>
        <form method="get" action="/admin/article">
            <div class="mb-3">
                <label for="status" class="form-label">状态</label>
                <select class="form-select" id="status" name="status">
                    <option value="">请选择</option>
                    <option value="1" <#if status?? && status == 1>selected</#if>>已发布</option>
                    <option value="0" <#if status?? && status == 0>selected</#if>>草稿</option>
                </select>
            </div>
            <button type="submit" class="btn btn-primary">
                筛选
            </button>
        </form>
    </div>
</div>

<#include "/admin/layouts/_footer.ftlh">