<#include "/admin/layouts/_header.ftlh">
<div class="row justify-content-center">
    <div class="card shadow-lg border-0">
        <#if message?? && message != "">
            <div class="alert alert-<#if success?? && success==1>success<#else>danger</#if> alert-dismissible fade show"
                 role="alert">
                ${message}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </#if>

        <div class="card-header bg-primary text-white">
            <h4 class="mb-0">${title}</h4>
        </div>
        <div class="card-body">
            <form action="/admin/admin/save" method="post">
                <#if admin.id??>
                    <input type="hidden" name="id" value="${admin.id}">
                </#if>

                <div class="mb-3">
                    <label for="username" class="form-label">用户名</label>
                    <#assign nowTime = .now?datetime?long>
                    <#assign randomPart = (nowTime % 100000000)?string("00000000")>
                    <#assign defaultUsername = "user${randomPart}">
                    <input type="text" class="form-control" id="username" name="username"
                           value="${(admin.username)!defaultUsername}" required>
                </div>

                <div class="mb-3">
                    <label for="rawPassword" class="form-label">密码</label>
                    <input type="password" class="form-control" id="rawPassword" name="rawPassword"
                           placeholder="<#if admin.id??>留空则不修改密码<#else>请输入密码</#if>"
                           <#if !admin.id??>required</#if>>
                    <#if admin.id??>
                        <div class="form-text">
                            编辑用户时，留空表示不修改当前密码。
                        </div>
                    </#if>
                </div>

                <div class="mb-3">
                    <label for="status" class="form-label">状态</label>
                    <select class="form-select" id="status" name="status">
                        <option value="1" <#if admin.status?? && admin.status == 1>selected</#if>>启用</option>
                        <option value="0" <#if admin.status?? && admin.status == 0>selected</#if>>停用</option>
                    </select>
                </div>

                <div class="d-flex justify-content-end mt-4">
                    <a href="/admin/admin" class="btn btn-secondary me-2">返回列表</a>
                    <button type="submit" class="btn btn-success">
                        <#if admin.id??>保存修改<#else>新增用户</#if>
                    </button>
                </div>
            </form>

        </div>
    </div>
</div>
<#include "/admin/layouts/_footer.ftlh">