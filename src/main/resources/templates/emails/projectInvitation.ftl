<#import "macros.ftl" as macros>
<!doctype html>
<html>
<body>
<p>
    Hello ${name} ${surname}! You have been invited to be part of ${projectName} <br/>
    You can accept this invitation by clicking that hyperlink: <br/>
    <a href="${macros.server}${token}">Accept invitation</a>
</p>
<@macros.signature />
</body>
</html>