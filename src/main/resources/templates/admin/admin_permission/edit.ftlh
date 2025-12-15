<#include "/admin/layouts/_header.ftlh">
    <div class="card shadow-lg mx-auto">
        <div class="card-body p-4 p-md-5">
            <#if message?? && message != "">
                <div class="alert alert-<#if success?? && success==1>success<#else>danger</#if> alert-dismissible fade show"
                     role="alert">
                    ${message}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </#if>

            <h2 class="card-title h3 mb-4 text-center">
                <#if adminPermission.id??>编辑权限: ${(adminPermission.name)!''}<#else>新增权限</#if>
            </h2>

            <form action="/admin/adminPermission/save" method="POST">

                <#if adminPermission.id??>
                    <input type="hidden" name="id" value="${adminPermission.id}">
                </#if>

                <div class="mb-3">
                    <label for="name" class="form-label">名称 <span class="text-danger">*</span></label>
                    <input type="text" id="name" name="name" value="${(adminPermission.name)!''}" required
                           class="form-control" placeholder="请输入权限名称">
                </div>

                <div class="mb-3">
                    <label for="summary" class="form-label">描述</label>
                    <input type="text" id="summary" name="summary" value="${(adminPermission.summary)!''}"
                           class="form-control" placeholder="">
                </div>

                <div class="mb-3">
                    <label class="form-label d-block">状态</label>
                    <div class="d-flex flex-row">
                        <div class="form-check form-check-inline">
                            <input type="radio" name="status" id="status1" value="1"
                                   <#if !adminPermission.status?? || adminPermission.status == 1>checked</#if>
                                   class="form-check-input">
                            <label class="form-check-label" for="status1">启用</label>
                        </div>
                        <div class="form-check form-check-inline">
                            <input type="radio" name="status" id="status0" value="0"
                                   <#if adminPermission.status?? && adminPermission.status == 0>checked</#if>
                                   class="form-check-input">
                            <label class="form-check-label" for="status0">禁用</label>
                        </div>
                    </div>
                </div>

                <div class="pt-3 d-flex justify-content-end">
                    <a href="/admin/adminPermission" class="btn btn-secondary me-2">返回列表</a>
                    <button type="submit" class="btn btn-primary">
                        <#if adminPermission.id??>更新权限<#else>新增权限</#if>
                    </button>
                </div>
            </form>
        </div>
    </div>
<#include "/admin/layouts/_footer.ftlh">