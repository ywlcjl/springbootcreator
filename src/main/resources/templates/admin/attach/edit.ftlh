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
        <#if attach.id??>编辑文章图片<#else>新增文章图片</#if>
    </h2>

    <form action="/admin/attach/save" method="POST" enctype="multipart/form-data">

        <#if attach.id??>
            <input type="hidden" name="id" value="${attach.id}">
        </#if>

        <div class="mb-3">
            <#if (attach.path?? && attach.path != '')>
                <#assign thumbPath = FU.getImageThumb(attach.path, 'thumb')>
                <a href="/${attach.path!''}" target="_blank" class="bd_attach_img">
                    <img src="/${thumbPath!''}" class="img-thumbnail" style="min-width: 300px;">
                </a>
            </#if>
        </div>

        <div class="mb-3">
            <label for="inputFile">替换图片</label>
            <p>
                <input type="file" id="inputFile" name="userfile">
            </p>
            <p><span id="helpBlock" class="help-block">支持jpg, jpeg, png, 最大文件10mb</span></p>
        </div>

        <div class="mb-3">
            <label for="name" class="form-label">name <span class="text-danger">*</span></label>
            <input type="text" id="name" name="name" value="${(attach.name)!''}" required
                   class="form-control" placeholder="请输入name">
        </div>

        <div class="mb-3">
            <label for="origName" class="form-label">origName <span class="text-danger"></span></label>
            <input type="text" id="origName" name="origName" value="${(attach.origName)!''}"
                   class="form-control" placeholder="origName">
        </div>

        <div class="mb-3">
            <label for="path" class="form-label">path <span class="text-danger"></span></label>
            <input type="text" id="path" name="path" value="${(attach.path)!''}"
                   class="form-control" placeholder="path">
        </div>

        <div class="mb-3">
            <label for="type" class="form-label">type <span class="text-danger"></span></label>
            <input type="text" id="type" name="type" value="${(attach.type)!''}"
                   maxlength="10" class="form-control" placeholder="type">
        </div>

        <div class="mb-3">
            <label for="articleId" class="form-label">articleId <span class="text-danger"></span></label>
            <input type="number" id="articleId" name="articleId" value="${(attach.articleId)!''}"
                   class="form-control" placeholder="articleId">
        </div>

        <div class="pt-3 d-flex justify-content-end">
            <a href="/admin/attach" class="btn btn-secondary me-2">返回列表</a>
            <button type="submit" class="btn btn-primary">
                <#if attach.id??>更新文章图片<#else>发布文章图片</#if>
            </button>
        </div>
    </form>
</div>

<#include "/admin/layouts/_footer.ftlh">