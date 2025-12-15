<nav aria-label="分页导航">
    <ul class="pure-pagination">

        <#-- 上一页按钮 -->
        <li class="pure-page-item <#if currentPage == 1>pure-disabled</#if>">
            <a class="pure-page-link" href="${pageUrl}?page=${currentPage - 1}&size=${pageSize}${pageSuffix}" aria-label="上一页">
                <span aria-hidden="true">&laquo;</span>
            </a>
        </li>

        <#-- 页码循环前的省略号和首页 -->
        <#assign startPage = ((currentPage - 2) > 1)?then(currentPage - 2, 1)>
        <#assign endPage = ((currentPage + 2) < totalPages)?then(currentPage + 2, totalPages)>

        <#if (startPage != 1)>
            <li class="pure-page-item"><a class="pure-page-link" href="${pageUrl}?page=1&size=${pageSize}${pageSuffix}">1</a></li>
            <#if (startPage > 2)>
                <li class="pure-page-item pure-disabled"><span class="pure-page-link">...</span></li>
            </#if>
        </#if>

        <#-- 主要页码循环 -->
        <#list startPage..endPage as pageNum>
            <li class="pure-page-item <#if pageNum == currentPage>pure-active</#if>">
                <a class="pure-page-link" href="${pageUrl}?page=${pageNum}&size=${pageSize}${pageSuffix}">${pageNum}</a>
            </li>
        </#list>

        <#-- 页码循环后的省略号和尾页 -->
        <#if (endPage < totalPages)>
            <#if (endPage < (totalPages - 1))>
                <li class="pure-page-item pure-disabled"><span class="pure-page-link">...</span></li>
            </#if>
            <li class="pure-page-item">
                <a class="pure-page-link" href="${pageUrl}?page=${totalPages}&size=${pageSize}${pageSuffix}">${totalPages}</a>
            </li>
        </#if>

        <#-- 下一页按钮 -->
        <li class="pure-page-item <#if currentPage == (totalPages)>pure-disabled</#if>">
            <a class="pure-page-link" href="${pageUrl}?page=${currentPage + 1}&size=${pageSize}${pageSuffix}" aria-label="下一页">
                <span aria-hidden="true">&raquo;</span>
            </a>
        </li>
    </ul>
</nav>