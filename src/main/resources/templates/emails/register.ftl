<#import "macros.ftl" as macros>
<!doctype html>
<html>
<body>
<p>
    Hello ${name} ${surname}! You have created account in <b>Tasky</b>! <br/>
    You can start by create your own project or join to other people's projects<br/>
    You can activate your account by clicking that hyperlink: <br/>
    <a href="${macros.server}${token}">Activation link</a>
</p>
<@macros.signature />
</body>
</html>