<#include "/admin/layouts/_header.ftlh">
<div class="d-flex justify-content-between align-items-center mb-3">
    <h2>${title}</h2>
    <a href="/admin/adminPermission/edit" class="btn btn-primary">
        <i class="bi bi-plus-circle"></i> 新增权限
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
                <th>描述</th>
                <th>受保护权限</th>
                <th>状态</th>
                <th>更新</th>
                <th>创建</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <#list resultPage.records as adminPermission>
                <tr>
                    <td>${adminPermission.id}</td>
                    <td>${adminPermission.name!''}</td>
                    <td>${adminPermission.summary!''}</td>
                    <td>
                        <#if adminPermission.isProtected == 1>
                            <span class="badge bg-success">是</span>
                        <#else>
                            <span class="badge bg-info-subtle">否</span>
                        </#if>
                    </td>
                    <td>
                        <#if adminPermission.status == 1>
                            <span class="badge bg-success">启用</span>
                        <#else>
                            <span class="badge bg-danger">禁用</span>
                        </#if>
                    </td>
                    <td><#if adminPermission.updateTime??>${FU.formatDateTime(adminPermission.updateTime)}</#if></td>
                    <td><#if adminPermission.createTime??>${FU.formatDateTime(adminPermission.createTime)}</#if></td>
                    <td>
                        <a href="/admin/adminPermission/edit?id=${adminPermission.id}" class="btn btn-sm btn-info text-white me-2">编辑</a>
                    </td>
                </tr>
            </#list>
            <#if resultPage.records?size == 0>
                <tr>
                    <td colspan="6" class="text-center text-muted py-4">没有找到任何数据。</td>
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