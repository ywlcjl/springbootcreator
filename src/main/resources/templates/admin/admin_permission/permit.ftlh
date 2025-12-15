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
                <#if permitAdmin.id??>授予用户权限: ${(permitAdmin.username)!''}<#else>给未知用户授予权限</#if>
            </h2>

            <form action="/admin/adminPermission/savePermit" method="POST">

                <input type="hidden" name="adminId" value="${permitAdmin.id}">

                <#list adminPermissions as adminPermission>
                    <div class="checkbox">
                        <label>
                            <input type="checkbox" name="permissionIds[]" id="checkbox${adminPermission.id!''}" value="${adminPermission.id!''}"
                                    <#if permissionIdList?seq_contains(adminPermission.id)>
                                        checked="checked"
                                    </#if>
                            >
                            ${adminPermission.name!''}
                        </label>
                    </div>
                </#list>

                <div class="pt-3 d-flex justify-content-end">
                    <a href="/admin/admin" class="btn btn-secondary me-2">返回列表</a>
                    <button type="submit" class="btn btn-primary">
                        授予权限
                    </button>
                </div>
            </form>
        </div>
    </div>
<#include "/admin/layouts/_footer.ftlh">