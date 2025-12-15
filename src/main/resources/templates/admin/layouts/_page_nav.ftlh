<nav aria-label="用户分页">
    <ul class="pagination justify-content-center">
        <li class="page-item <#if currentPage == 1>disabled</#if>">
            <a class="page-link" href="${pageUrl}?page=${currentPage - 1}&size=${pageSize}${pageSuffix}" aria-label="Previous">
                <span aria-hidden="true">&laquo;</span>
            </a>
        </li>

        <#-- 页码循环 (只显示当前页附近的部分页码) -->
        <#assign startPage = ((currentPage - 2) > 1)?then(currentPage - 2, 1)>
        <#assign endPage = ((currentPage + 2) < totalPages)?then(currentPage + 2, totalPages)>
        <#if (startPage != 1)>
            <li class="page-item"><a class="page-link" href="${pageUrl}?page=1&size=${pageSize}${pageSuffix}">1</a></li>
            <#if (startPage > 2)>
                <li class="page-item disabled"><span class="page-link">...</span></li>
            </#if>
        </#if>

        <#list startPage..endPage as pageNum>
            <li class="page-item <#if pageNum == currentPage>active</#if>">
                <a class="page-link" href="${pageUrl}?page=${pageNum}&size=${pageSize}${pageSuffix}">${pageNum}</a>
            </li>
        </#list>

        <#if (endPage < totalPages)>
            <#if (endPage < (totalPages - 1))>
                <li class="page-item disabled"><span class="page-link">...</span></li>
            </#if>
            <li class="page-item">
                <a class="page-link" href="${pageUrl}?page=${totalPages}&size=${pageSize}${pageSuffix}">${totalPages}</a>
            </li>
        </#if>


        <li class="page-item <#if currentPage == (totalPages)>disabled</#if>">
            <a class="page-link" href="${pageUrl}?page=${currentPage + 1}&size=${pageSize}${pageSuffix}" aria-label="Next">
                <span aria-hidden="true">&raquo;</span>
            </a>
        </li>
    </ul>
</nav>