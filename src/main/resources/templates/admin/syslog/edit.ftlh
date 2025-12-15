<#include "/admin/layouts/_header.ftlh">
<div class="card shadow-lg mx-auto p-4 p-md-5">
    <#if message?? && message != "">
        <div class="alert alert-<#if success?? && success==1>success<#else>danger</#if> alert-dismissible fade show"
             role="alert">
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </#if>

    <h2 class="card-title h3 mb-4 text-center">
        <#if syslog.id??>编辑日志<#else>新增日志</#if>
    </h2>

    <form action="/admin/syslog/save" method="POST">

        <#if syslog.id??>
            <input type="hidden" name="id" value="${syslog.id}">
        </#if>

        <div class="mb-3">
            <label for="type" class="form-label">type <span class="text-danger">*</span></label>
            <input type="number" id="type" name="type" value="${(syslog.type)!''}" required
                   class="form-control" placeholder="请输入type">
        </div>

        <div class="mb-3">
            <label for="typeName" class="form-label">typeName <span class="text-danger"></span></label>
            <input type="text" id="typeName" name="typeName" value="${(syslog.typeName)!''}"
                   class="form-control" placeholder="typeName">
        </div>

        <div class="mb-3">
            <label for="content" class="form-label">content <span class="text-danger"></span></label>
            <textarea id="content" name="content" rows="5"
                      class="form-control" placeholder="content">${(syslog.content)!''}</textarea>
        </div>

        <div class="mb-3">
            <label for="adminId" class="form-label">adminId <span class="text-danger"></span></label>
            <input type="number" id="adminId" name="adminId" value="${(syslog.adminId)!''}"
                   class="form-control" placeholder="adminId">
        </div>

        <div class="mb-3">
            <label for="username" class="form-label">username <span class="text-danger"></span></label>
            <input type="text" id="username" name="username" value="${(syslog.username)!''}"
                   maxlength="10" class="form-control" placeholder="username">
        </div>

        <div class="mb-3">
            <label for="ipAddress" class="form-label">ipAddress <span class="text-danger"></span></label>
            <input type="text" id="ipAddress" name="ipAddress" value="${(syslog.ipAddress)!''}"
                    class="form-control" placeholder="ipAddress">
        </div>

        <div class="pt-3 d-flex justify-content-end">
            <a href="/admin/syslog" class="btn btn-secondary me-2">返回列表</a>
            <button type="submit" class="btn btn-primary">
                <#if syslog.id??>更新日志<#else>发布日志</#if>
            </button>
        </div>
    </form>
</div>

<#include "/admin/layouts/_footer.ftlh">