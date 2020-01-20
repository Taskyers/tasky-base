<#import "macros.ftl" as macros>
<!doctype html>
<html>
<body>
<p>
    Email update for this account has been requested.
    Use hyperlink below to update your email, otherwise ignore this email.
    <a href="${macros.server}${token}">Update email</a>
</p>
<@macros.signature />
</body>
</html>