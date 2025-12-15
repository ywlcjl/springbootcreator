<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <title>404 - 页面未找到</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            text-align: center;
            padding: 50px;
        }
        .container {
            max-width: 800px;
            margin: 0 auto;
            border: 1px solid #ddd;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
        }
        h1 {
            color: #ff6600;
            font-size: 60px;
            margin-bottom: 10px;
        }
        p {
            font-size: 18px;
            color: #333;
        }
        a {
            color: #007bff;
            text-decoration: none;
            font-weight: bold;
        }
        a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>${status!404}</h1>
    <p>${error!"Not Found"}</p>
    <p>抱歉，您访问的页面不存在。</p>

    <#if message??>
        <p>
            **详细信息:** ${message}
        </p>
    </#if>

    <p>
        <a href="/">返回首页</a> 或 检查 URL 是否正确.
    </p>
</div>
</body>
</html>