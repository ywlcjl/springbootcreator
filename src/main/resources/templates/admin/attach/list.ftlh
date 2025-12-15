<#include "/admin/layouts/_header.ftlh">
<div class="d-flex justify-content-between align-items-center mb-4">
    <h2>${title}</h2>
    <a href="/admin/attach/edit" class="btn btn-primary">
        <i class="bi bi-plus-circle"></i> 新增文章图片
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
                <th>缩略图</th>
                <th>name</th>
                <th>origName</th>
                <th>path</th>
                <th>type</th>
                <th>文章ID</th>
                <th>管理员ID</th>
                <th>创建时间</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <#list resultPage.records as attach>
                <tr>
                    <td>${attach.id!''}</td>
                    <#if (attach.path?? && attach.path != '')>
                        <#assign thumbPath = FU.getImageThumb(attach.path, 'thumb')>
                    </#if>
                    <td><a href="/${attach.path!''}" target="_blank" class="bd_attach_img"><img
                                    src="/${thumbPath!''}" class="img-thumbnail" style="min-width: 200px;"></a></td>
                    <td>${attach.name!''}</td>
                    <td>${attach.origName!''}</td>
                    <td>${attach.path!''}</td>
                    <td>${attach.type!''}</td>
                    <td>${attach.articleId!''}</td>
                    <td>${attach.adminId!''}</td>
                    <td><#if attach.createTime??>${FU.formatDateTime(attach.createTime)}</#if></td>
                    <td>
                        <a href="/admin/attach/edit?id=${attach.id}"
                           class="btn btn-sm btn-info text-white me-2">编辑</a>
                        <form action="/admin/attach/delete?id=${attach.id}" method="POST" class="d-inline">
                            <button type="submit" class="btn btn-sm btn-danger"
                                    onclick="return confirm('确定要删除这个文章图片吗？')">删除
                            </button>
                        </form>
                    </td>
                </tr>
            </#list>
            <#if resultPage.records?size == 0>
                <tr>
                    <td colspan="10" class="text-center text-muted py-4">暂无文章数据。请点击 "新增附件" 创建。</td>
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
        <form method="get" action="/admin/attach">
            <div class="mb-3">
                <label for="status" class="form-label">type</label>
                <select class="form-select" id="type" name="type">
                    <option value="">请选择</option>
                    <option value="jpg" <#if type?? && type == "jpg">selected</#if>>jpg</option>
                    <option value="jpeg" <#if type?? && type == "jpeg">selected</#if>>jpeg</option>
                    <option value="png" <#if type?? && type == "png">selected</#if>>png</option>
                    <option value="gif" <#if type?? && type == "gif">selected</#if>>gif</option>
                </select>
            </div>
            <button type="submit" class="btn btn-primary">
                筛选
            </button>
        </form>
    </div>
</div>

<#include "/admin/layouts/_footer.ftlh">