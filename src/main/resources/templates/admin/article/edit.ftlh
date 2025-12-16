<#include "/admin/layouts/_header.ftlh">
<link rel="stylesheet" href="/plugins/jqueryui/themes/base/jquery-ui.css">
<script src="/plugins/jqueryui/jquery-ui.min.js"></script>

<!-- include summernote css/js-->
<link href="/plugins/summernote0.9.0/summernote-bs5.min.css" rel="stylesheet">
<script src="/plugins/summernote0.9.0/summernote-bs5.min.js"></script>
<script src="/plugins/summernote0.9.0/lang/summernote-zh-CN.min.js"></script>

    <script>
        function attachInsertEditor(obj) {
            var imgUrl = $(obj).attr('href');
            // Summernote API: 插入图片到编辑器中
            $('#summernote').summernote('insertImage', imgUrl);
            return false;
        }

        function attachInsertEditorImgUrl(obj) {
            //插入缩略图和原图的链接图片
            var imgThumbUrl = $(obj).attr('href');
            var imgSourceUrl = imgThumbUrl.replace('_thumb', '');
            var htmlContent = '<a href="' + imgSourceUrl + '" target="_blank">' +
                '<img src="' + imgThumbUrl + '"></a>';
            $('#summernote').summernote('editor.insertNode', $(htmlContent)[0]);
            return false;
        }

        function setCoverPic(obj) {
            var imgUrl = $(obj).attr('href');
            $('#coverPic').val(imgUrl);
            return false;
        }

        function deleteAttach(obj) {
            var result = confirm('确认删除吗?');
            if (result) {
                var id = $(obj).attr('href');

                $.post("/admin/attach/ajaxDelete", {
                    'id': id
                }, function (data, textStatus) {
                    var message = data.message;
                    var success = data.success;

                    $('#attachMessage').html(message);

                    if (success) {
                        $(obj).parent().remove();
                    }
                }, "json");
            }

            return false;
        }

        function articleUpload() {
            $('#buttonUpload').click(function () {
                var inputFileObj = $("#inputFile").get(0);
                if (inputFileObj.files.length === 0 || !inputFileObj.files[0]) {
                    $("#attachMessage").html("上传的图片为空");
                    return false;
                }

                var formData;
                formData = new FormData();
                formData.append('userfile', inputFileObj.files[0]);

                var articleId = $('#articleId').val();
                if(articleId !='' && articleId > 0) {
                    formData.append('articleId', articleId);
                }

                $.ajax({
                    url: '/admin/attach/ajaxUpload',
                    data: formData,
                    contentType: false,
                    processData: false,
                    type: 'POST',
                    dataType: 'json',
                    success: function (data) {
                        if (data.success) {
                            $("#inputFile").val("");
                            $("#attachMessage").html("上传成功");

                            var str = '<li class="list-group-item">';
                            if (data.articleId == null || data.articleId === '') {
                                str += '<input type="hidden" name="attachIds[]" value="' + data.attachId + '">';
                            }

                            str += '<a href="/' + data.picUrl + '" target="_blank" class="bd_attach_img"><img src="/' + data.picUrlThumb + '" class="img-thumbnail"></a> ';
                            str += '<a href="/' + data.picUrl + '" onclick="return attachInsertEditor(this)">插入文章</a>&nbsp;&nbsp;';
                            str += '<a href="/' + data.picUrlThumb + '" onclick="return attachInsertEditorImgUrl(this)">插入缩略图</a>&nbsp;&nbsp;';
                            str += '<a href="' + data.picUrl + '" onclick="return setCoverPic(this);">设为封面</a>&nbsp;&nbsp;';
                            str += '<a href="' + data.picUrlThumb + '" onclick="return setCoverPic(this);">缩略图为封面</a>&nbsp;&nbsp;';
                            str += '<a href="' + data.attachId + '" onclick="return deleteAttach(this);">删除</a>';
                            str += '</li>';

                            $("#attachList").append(str);

                        } else {
                            $("#attachMessage").html(data.message);
                        }

                    },
                    error: function (data) {
                        $("#attachMessage").html("ajax error");
                    }
                });

                return false;
            });
        }

    </script>


<div class="card shadow-lg mx-auto p-4 p-md-5">
    <#if message?? && message != "">
        <div class="alert alert-<#if success?? && success==1>success<#else>danger</#if> alert-dismissible fade show"
             role="alert">
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </#if>

    <h2 class="card-title h3 mb-4 text-center">
        <#if article.id??>编辑文章<#else>新增文章</#if>
    </h2>

    <form action="/admin/article/save" method="POST">

        <#if article.id??>
            <input type="hidden" id="articleId" name="id" value="${article.id}">
        </#if>

        <div class="mb-3">
            <label for="title" class="form-label">文章标题 <span class="text-danger">*</span></label>
            <input type="text" id="title" name="title" value="${(article.title)!''}" required
                   class="form-control" placeholder="请输入文章标题">
        </div>

        <div class="mb-3">
            <label for="categoryId" class="form-label">所属分类 <span class="text-danger">*</span></label>
            <select id="categoryId" name="categoryId" required
                    class="form-select">
                <option value="" disabled <#if !article.categoryId??>selected</#if>>-- 请选择文章所属分类 --</option>
                <#list allArticleCategories as cat>
                    <option value="${cat.id}"
                            <#if article.categoryId?? && article.categoryId == cat.id>selected</#if>>
                        ${cat.name}
                    </option>
                </#list>
            </select>
            <div class="form-text text-muted">分类在"分类管理"中创建。</div>
        </div>

        <div class="mb-3">
            <label for="content" class="form-label">文章内容 <span class="text-danger">*</span></label>
            <textarea id="summernote" name="content" rows="15" required
                      class="form-control" placeholder="在此输入文章正文">${(article.content)!''}</textarea>
        </div>

        <div class="mb-3">
            <h5>文章图片列表</h5>
            <ul class="list-group" id="attachList">
                <#if (attachs?? && attachs?size > 0) >
                    <#list attachs as attach>
                        <li class="list-group-item">
                            <#if (attach.path?? && attach.path != '')>
                                <#assign thumbPath = FU.getImageThumb(attach.path, 'thumb')>
                            </#if>
                            <a href="/${attach.path!''}" target="_blank" class="bd_attach_img"><img
                                        src="/${thumbPath!''}" class="img-thumbnail"></a>
                            <a onclick="return attachInsertEditor(this)" href="/${attach.path!''}">插入文章</a>&nbsp;&nbsp;
                            <a onclick="return attachInsertEditorImgUrl(this);" href="/${thumbPath!''}">插入缩略图</a>&nbsp;&nbsp;
                            <a onclick="return setCoverPic(this);" href="${attach.path!''}">设为封面</a>&nbsp;&nbsp;
                            <a onclick="return setCoverPic(this);" href="${thumbPath!''}">封面缩略图</a>&nbsp;&nbsp;
                            <a onclick="return deleteAttach(this);" href="${attach.id!''}">删除</a>
                        </li>
                    </#list>
                </#if>

                <!--提交未保存文章id图片-->
                <#if (formAttachs?? && formAttachs?size > 0) >
                    <#list formAttachs as attach>
                        <li class="list-group-item">
                            <input type="hidden" name="attachIds[]" value="${attach.id!''}"/>
                            <#if (attach.path?? && attach.path != '')>
                                <#assign thumbPath = FU.getImageThumb(attach.path, 'thumb')>
                            </#if>
                            <a href="/${attach.path!''}" target="_blank" class="bd_attach_img"><img
                                        src="/${thumbPath!''}" class="img-thumbnail"></a>
                            <a onclick="return attachInsertEditor(this)" href="/${attach.path!''}">插入文章</a>&nbsp;&nbsp;
                            <a onclick="return attachInsertEditorImgUrl(this);" href="/${thumbPath!''}">插入缩略图</a>&nbsp;&nbsp;
                            <a onclick="return setCoverPic(this);" href="${attach.path!''}">设为封面</a>&nbsp;&nbsp;
                            <a onclick="return setCoverPic(this);" href="${thumbPath!''}">封面缩略图</a>&nbsp;&nbsp;
                            <a onclick="return deleteAttach(this);" href="${attach.id!''}">删除</a>
                        </li>
                    </#list>
                </#if>
            </ul>

            <label for="inputFile">图片上传</label>
            <p>
                <input type="file" id="inputFile"> <a class="btn btn-secondary" href="#" role="button" id="buttonUpload">上传</a>
            </p>
            <p><span id="helpBlock" class="help-block">支持jpg, jpeg, png, 最大文件10mb</span></p>
            <p class="bg-info-subtle" id="attachMessage"></p>
        </div>


        <div class="mb-4">
            <label class="form-label d-block">状态</label>
            <div class="d-flex flex-row">
                <div class="form-check form-check-inline">
                    <input type="radio" name="status" id="status1" value="1"
                           <#if !article.status?? || article.status == 1>checked</#if>
                           class="form-check-input">
                    <label class="form-check-label" for="status1">发布 (显示)</label>
                </div>
                <div class="form-check form-check-inline">
                    <input type="radio" name="status" id="status0" value="0"
                           <#if article.status?? && article.status == 0>checked</#if>
                           class="form-check-input">
                    <label class="form-check-label" for="status0">草稿 (隐藏)</label>
                </div>
            </div>
        </div>

        <div class="mb-3">
            <label for="author" class="form-label">作者 <span class="text-danger"></span></label>
            <input type="text" id="author" name="author" value="${(article.author)!''}"
                   class="form-control" placeholder="作者">
        </div>

        <div class="mb-3">
            <label for="source" class="form-label">来源 <span class="text-danger"></span></label>
            <input type="text" id="source" name="source" value="${(article.source)!''}"
                   class="form-control" placeholder="来源">
        </div>

        <div class="mb-3">
            <label for="coverPic" class="form-label">封面图 <span class="text-danger"></span></label>
            <input type="text" id="coverPic" name="coverPic" value="${(article.coverPic)!''}"
                   class="form-control" placeholder="封面图">
        </div>

        <div class="mb-3">
            <label for="descTxt" class="form-label">文章描述 <span class="text-danger"></span></label>
            <textarea id="descTxt" name="descTxt" rows="5"
                      class="form-control" placeholder="文章描述">${(article.descTxt)!''}</textarea>
        </div>

        <div class="mb-3">
            <label for="hopLink" class="form-label">外部链接 <span class="text-danger"></span></label>
            <input type="text" id="hopLink" name="hopLink" value="${(article.hopLink)!''}"
                   class="form-control" placeholder="外部链接">
        </div>

        <div class="mb-4">
            <label class="form-label d-block">推荐</label>
            <div class="d-flex flex-row">
                <div class="form-check form-check-inline">
                    <input type="radio" name="top" id="top1" value="1"
                           <#if !article.top?? || article.top == 1>checked</#if>
                           class="form-check-input">
                    <label class="form-check-label" for="top1">已推荐</label>
                </div>
                <div class="form-check form-check-inline">
                    <input type="radio" name="top" id="top0" value="0"
                           <#if article.top?? && article.top == 0>checked</#if>
                           class="form-check-input">
                    <label class="form-check-label" for="top0">未推荐</label>
                </div>
            </div>
        </div>

        <div class="mb-3">
            <label for="postTime" class="form-label">发布时间 <span class="text-danger"></span></label>
            <input type="text" id="datepicker" name="postTime"
                   value="<#if article.postTime??>${FU.formatDateTime(article.postTime)}</#if>"
                   class="form-control" placeholder="发布时间">
        </div>

        <div class="pt-3 d-flex justify-content-end">
            <a href="/admin/article" class="btn btn-secondary me-2">返回列表</a>
            <button type="submit" class="btn btn-primary">
                <#if article.id??>更新文章<#else>发布文章</#if>
            </button>
        </div>
    </form>
</div>
<style>
    /*隐藏图片本地上传的功能*/
    .note-group-select-from-files {
        display: none;
    }
</style>
<script>
    $(document).ready(function () {
        $('#summernote').summernote({
            placeholder: '请输入文章内容',
            tabsize: 2,
            height: 400,
            lang: 'zh-CN'
        });

        $("#datepicker").datepicker({
            dateFormat: "yy-mm-dd 10:00:00",
        });

        //上传图片事件
        articleUpload();
    });
</script>
<#include "/admin/layouts/_footer.ftlh">