<#include "/admin/layouts/_header.ftlh">
<div class="d-flex justify-content-between align-items-center mb-3">
    <h2>${title}</h2>
    <a href="/admin/articleCategory/edit" class="btn btn-primary">
        <i class="bi bi-plus-circle"></i> 新增分类
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
        <table class="table table-striped table-hover">
            <thead class="table-light">
            <tr>
                <th>ID</th>
                <th>名称</th>
                <th>父ID</th>
                <th>跳转链接</th>
                <th>排序</th>
                <th>状态</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <#list resultPage.records as articleCategory>
                <tr>
                    <td>${articleCategory.id}</td>
                    <td>${articleCategory.name}</td>
                    <td>
                        <#if articleCategory.parentId == 0>
                            <span class="badge bg-secondary">根分类</span>
                        <#else>
                            <#list allArticleCategories as selectCategory>
                                <#if selectCategory.id == articleCategory.parentId>
                                    [ID:${selectCategory.id}] ${selectCategory.name}
                                </#if>
                            </#list>
                        </#if>
                    </td>
                    <td>
                        <#if articleCategory.hopLink?? && articleCategory.hopLink != "">
                            <a href="${articleCategory.hopLink}" target="_blank"
                               class="text-primary text-decoration-underline">链接</a>
                        <#else>
                            -
                        </#if>
                    </td>
                    <td>${articleCategory.sort}</td>
                    <td>
                        <#if articleCategory.status == 1>
                            <span class="badge bg-success">启用</span>
                        <#else>
                            <span class="badge bg-danger">禁用</span>
                        </#if>
                    </td>
                    <td>
                        <a href="/admin/articleCategory/edit?id=${articleCategory.id}" class="btn btn-sm btn-info text-white me-2">编辑</a>

                        <form action="/admin/articleCategory/delete?id=${articleCategory.id}" method="POST" class="d-inline">
                            <button type="submit" class="btn btn-sm btn-danger"
                                    onclick="return confirm('确定要删除这分类吗？删除操作不可逆！')">删除
                            </button>
                        </form>
                    </td>
                </tr>
            </#list>
            <#if resultPage.records?size == 0>
                <tr>
                    <td colspan="7" class="text-center text-muted py-4">没有找到任何分类数据。</td>
                </tr>
            </#if>
            </tbody>
        </table>
    </div>
    <#-- 分页导航 -->
    <#if totalPages gt 1>
        <#include "/admin/layouts/_page_nav.ftlh">
    </#if>
</div>

<#include "/admin/layouts/_footer.ftlh">