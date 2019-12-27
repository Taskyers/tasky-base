<#import "macros.ftl" as macros>
<!doctype html>
<html>
<body>
<a href="${macros.server}secure/tasks/${taskKey}">${taskKey} - ${taskName}</a>
${personals} has made changes
<@macros.signature />
</body>
</html>