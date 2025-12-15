<#include "/admin/layouts/_header.ftlh">
<h2 class="mb-4 text-danger">操作权限不足</h2>
<div class="row mt-4">
    <div class="col-12">
        <div class="card border-0 shadow-sm">
            <div class="card-body">
                <div class="mb-4 text-dark">
                    <p>${message!''}</p>
                    <p><a href="/admin/dashboard">返回控制台</a></p>
                </div>

            </div>
        </div>
    </div>
</div>

<p class="mt-4 text-muted text-end">
    <small>当前用户: ${currentAdmin.username!""}</small>
</p>

<#include "/admin/layouts/_footer.ftlh">