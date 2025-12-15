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
                <#if articleCategory.id??>编辑分类: ${(articleCategory.name)!''}<#else>新增文章分类</#if>
            </h2>

            <form action="/admin/articleCategory/save" method="POST">

                <#if articleCategory.id??>
                    <input type="hidden" name="id" value="${articleCategory.id}">
                </#if>

                <#-- 分类名称 -->
                <div class="mb-3">
                    <label for="name" class="form-label">分类名称 <span class="text-danger">*</span></label>
                    <input type="text" id="name" name="name" value="${(articleCategory.name)!''}" required
                           class="form-control" placeholder="请输入分类名称">
                </div>

                <div class="mb-3">
                    <label for="parentId" class="form-label">父分类</label>
                    <select id="parentId" name="parentId"
                            class="form-select">
                        <option value="0" <#if articleCategory.parentId?? && articleCategory.parentId == 0>selected</#if>>-- 根分类 (无父级) --</option>
                        <#list allArticleCategories as cat>
                            <#if articleCategory.id?? && articleCategory.id == cat.id>
                            <#else>
                                <option value="${cat.id}"
                                        <#if articleCategory.parentId?? && articleCategory.parentId == cat.id>selected</#if>>
                                    [ID:${cat.id}] ${cat.name}
                                </option>
                            </#if>
                        </#list>
                    </select>
                </div>

                <div class="mb-3">
                    <label for="sort" class="form-label">排序值 <span class="text-danger">*</span></label>
                    <input type="number" id="sort" name="sort" value="${(articleCategory.sort)!0}" required
                           class="form-control">
                    <div class="form-text text-muted">数字越小，排序越靠前。</div>
                </div>

                <div class="mb-3">
                    <label for="hopLink" class="form-label">跳转链接 (可选)</label>
                    <input type="url" id="hopLink" name="hopLink" value="${(articleCategory.hopLink)!''}"
                           class="form-control" placeholder="https://">
                </div>

                <div class="mb-3">
                    <label class="form-label d-block">状态</label>
                    <div class="d-flex flex-row">
                        <div class="form-check form-check-inline">
                            <input type="radio" name="status" id="status1" value="1"
                                   <#if !articleCategory.status?? || articleCategory.status == 1>checked</#if>
                                   class="form-check-input">
                            <label class="form-check-label" for="status1">启用 (显示)</label>
                        </div>
                        <div class="form-check form-check-inline">
                            <input type="radio" name="status" id="status0" value="0"
                                   <#if articleCategory.status?? && articleCategory.status == 0>checked</#if>
                                   class="form-check-input">
                            <label class="form-check-label" for="status0">禁用 (隐藏)</label>
                        </div>
                    </div>
                </div>

                <div class="pt-3 d-flex justify-content-end">
                    <a href="/admin/articleCategory" class="btn btn-secondary me-2">返回列表</a>
                    <button type="submit" class="btn btn-primary">
                        <#if articleCategory.id??>更新分类<#else>新增分类</#if>
                    </button>
                </div>
            </form>
        </div>
    </div>
<#include "/admin/layouts/_footer.ftlh">