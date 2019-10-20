<#import "macros.ftl" as macros>
<!doctype html>
<html>
<body>
<p>
    Reset password for this account has been requested.
    Use hyperlink below to reset your password, otherwise ignore this email.
    <a href="${macros.server}${token}">Reset password</a>
</p>
<@macros.signature />
</body>
</html>