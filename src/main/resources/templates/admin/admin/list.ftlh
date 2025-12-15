<#include "/admin/layouts/_header.ftlh">
<div class="d-flex justify-content-between align-items-center mb-3">
    <h2>${title}</h2>
    <div>
        <a href="/admin/adminPermission" class="btn btn-secondary">
            <i class="bi bi-plus-circle"></i> 权限列表
        </a>&nbsp;
        <a href="/admin/admin/edit" class="btn btn-primary">
            <i class="bi bi-plus-circle"></i> 新增用户
        </a>
    </div>
</div>

<#if message??>
    <div class="alert alert-<#if success?? && success==1>success<#else>danger</#if> alert-dismissible fade show"
         role="alert">
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
</#if>

<div class="bg-white rounded border-0 p-3">
    <div class="table-responsive">
        <table class="table table-striped table-hover">
            <thead class="table-light">
            <tr>
                <th>ID</th>
                <th>用户名</th>
                <th>最高管理员</th>
                <th>状态</th>
                <th>更新时间</th>
                <th>创建时间</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <#list resultPage.records as admin>
                <tr>
                    <td>${admin.id}</td>
                    <td>${admin.username}</td>
                    <td>
                        <#if admin.isRoot == 1>
                            是
                        <#else>
                            否
                        </#if>
                    </td>
                    <td>
                        <#if admin.status == 1>
                            <span class="badge bg-success">启用</span>
                        <#else>
                            <span class="badge bg-danger">停用</span>
                        </#if>
                    </td>
                    <td><#if admin.updateTime??>${FU.formatDateTime(admin.updateTime)}</#if></td>
                    <td><#if admin.createTime??>${FU.formatDateTime(admin.createTime)}</#if></td>
                    <td>
                        <a href="/admin/admin/edit?id=${admin.id}" class="btn btn-sm btn-info text-white">编辑</a>
                        <a href="/admin/adminPermission/permit?adminId=${admin.id}" class="btn btn-sm btn-secondary text-white">授予权限</a>
                    </td>
                </tr>
            </#list>
            </tbody>
        </table>
    </div>

    <#if totalPages gt 1>
        <#include "/admin/layouts/_page_nav.ftlh">
    </#if>

    <div>
        <form method="get" action="/admin/admin">
            <div class="mb-3">
                <label for="status" class="form-label">状态</label>
                <select class="form-select" id="status" name="status">
                    <option value="">请选择</option>
                    <option value="1" <#if status?? && status == 1>selected</#if>>启用</option>
                    <option value="0" <#if status?? && status == 0>selected</#if>>停用</option>
                </select>
            </div>
            <button type="submit" class="btn btn-primary">
                筛选
            </button>
        </form>
    </div>
</div>

<#include "/admin/layouts/_footer.ftlh">