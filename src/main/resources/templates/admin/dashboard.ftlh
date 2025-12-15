<#include "/admin/layouts/_header.ftlh">
<h2 class="mb-4 text-dark">系统概览</h2>

<div class="row g-4">
    <div class="col-sm-4 col-lg-3">
        <div class="card border-0 shadow-sm">
            <div class="card-body">
                <h5 class="card-title text-muted">文章总数</h5>
                <p class="card-text fs-3 fw-bold text-success">${articleCount!0}</p>
                <p class="text-sm text-muted mb-0"> <span class="text-success"></span></p>
            </div>
        </div>
    </div>
    <div class="col-sm-4 col-lg-3">
        <div class="card border-0 shadow-sm">
            <div class="card-body">
                <h5 class="card-title text-muted">图片总数</h5>
                <p class="card-text fs-3 fw-bold text-success">${attachCount!0}</p>
                <p class="text-sm text-muted mb-0"> <span class="text-success"></span></p>
            </div>
        </div>
    </div>
    <div class="col-sm-4 col-lg-3">
        <div class="card border-0 shadow-sm">
            <div class="card-body">
                <h5 class="card-title text-muted">用户总数</h5>
                <p class="card-text fs-3 fw-bold text-success">${adminCount!0}</p>
                <p class="text-sm text-muted mb-0"> <span class="text-success"></span></p>
            </div>
        </div>
    </div>
</div>

<div class="row mt-4">
    <div class="col-12">
        <div class="card border-0 shadow-sm">
            <div class="card-body">
                <div class="d-flex justify-content-between align-items-center mb-3">
                    <h3 class="card-title text-secondary mb-0">最近系统操作日志</h3>
                    <span><a href="/admin/syslog">查看全部日志</a></span>
                </div>


                <div class="table-responsive">
                    <table class="table table-hover align-middle">
                        <thead class="table-light">
                        <tr>
                            <th>日志类型</th>
                            <th>日志内容</th>
                            <th>用户名</th>
                            <th>IP地址</th>
                            <th>日志时间</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list syslogs as syslog>
                            <tr>
                                <td>${syslog.type!''}: ${syslog.typeName!''}</td>
                                <td>${syslog.content!''}</td>
                                <td>${syslog.username!''}</td>
                                <td>${syslog.ipAddress!''}</td>
                                <td><#if syslog.createTime??>${FU.formatDateTime(syslog.createTime)}</#if></td>
                            </tr>
                        </#list>
                        <#if syslogs?size == 0>
                            <tr>
                                <td colspan="4" class="text-center text-muted py-4">暂无日志数据</td>
                            </tr>
                        </#if>
                        </tbody>
                    </table>

                </div>


            </div>
        </div>
    </div>
</div>

<p class="mt-4 text-muted text-end">
    <small>当前用户: ${currentAdmin.username!""}</small>
</p>

<#include "/admin/layouts/_footer.ftlh">