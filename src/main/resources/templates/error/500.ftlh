<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <title>500 - 服务器内部错误</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            text-align: center;
            padding: 50px;
        }
        .container {
            max-width: 800px;
            margin: 0 auto;
            border: 1px solid #e74c3c; /* 红色边框强调错误 */
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
        }
        h1 {
            color: #e74c3c;
            font-size: 60px;
            margin-bottom: 10px;
        }
        .note {
            color: #888;
            font-size: 14px;
            margin-top: 20px;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>${status!500}</h1>
    <p>${error!"Internal Server Error"}</p>

    <h2>服务器出错.</h2>
    <p>请您稍后再试.</p>

    <p>
        <a href="/">返回首页</a>
    </p>

    <#if trace??>
        <div class="note">
            <p>错误提示:</p>
            <pre style="text-align: left; white-space: pre-wrap; word-wrap: break-word; font-size: 12px;">${trace}</pre>
        </div>
    </#if>
</div>
</body>
</html>