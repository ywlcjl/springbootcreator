<#include "/admin/layouts/_header.ftlh">
<div class="d-flex justify-content-between align-items-center mb-4">
    <h2>${title}</h2>
    <a href="/admin/syslog/edit" class="btn btn-primary">
        <i class="bi bi-plus-circle"></i> 新增日志
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
                <th>type</th>
                <th>content</th>
                <th>userId</th>
                <th>username</th>
                <th>ipAddress</th>
                <th>更新时间</th>
                <th>创建时间</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <#list resultPage.records as syslog>
                <tr>
                    <td>${syslog.id!''}</td>
                    <td>${syslog.type!0} ${syslog.typeName!''}</td>
                    <td>${syslog.content!''}</td>
                    <td>${syslog.userId!0}</td>
                    <td>${syslog.username!''}</td>
                    <td>${syslog.ipAddress!''}</td>
                    <td><#if syslog.updateTime??>${FU.formatDateTime(syslog.updateTime)}</#if></td>
                    <td><#if syslog.createTime??>${FU.formatDateTime(syslog.createTime)}</#if></td>
                    <td>
                        <a href="/admin/syslog/edit?id=${syslog.id}"
                           class="btn btn-sm btn-info text-white me-2">编辑</a>
                        <form action="/admin/syslog/delete?id=${syslog.id}" method="POST" class="d-inline">
                            <button type="submit" class="btn btn-sm btn-danger"
                                    onclick="return confirm('确定要删除这个日志吗？')">删除
                            </button>
                        </form>
                    </td>
                </tr>
            </#list>
            <#if resultPage.records?size == 0>
                <tr>
                    <td colspan="9" class="text-center text-muted py-4">暂无日志数据。请点击 "新增附件" 创建。</td>
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
        <form method="get" action="/admin/syslog">
            <div class="mb-3">
            <select name="type" class="form-select">
                <option value="">所有类型</option>
                <#list typeMap?keys as typeValue>
                    <#assign typeName = typeMap[typeValue]>
                    <option value="${typeValue}"
                            <#if type?? && type == typeValue>selected</#if> >
                        ${typeValue} - ${typeName}
                    </option>
                </#list>
            </select>
            </div>
            <button type="submit" class="btn btn-primary">
                筛选
            </button>
        </form>
    </div>
</div>

<#include "/admin/layouts/_footer.ftlh">