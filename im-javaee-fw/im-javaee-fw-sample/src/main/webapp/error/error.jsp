<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isErrorPage="true" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="javax.servlet.jsp.JspException" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
    <HEAD>
        <TITLE>エラーページ</TITLE>
    </HEAD>
    <BODY>
        以下のエラーが発生しました。<BR>
        <TABLE border="1">
            <TR>
                <TD><PRE><%
                             if (exception == null) {
                         %>例外が取得できませんでした。<%
                             } else if (exception instanceof JspException) {
                                 Throwable rootCause = ((JspException)exception).getRootCause();
                                 if (rootCause != null) {
                                     rootCause.printStackTrace(new PrintWriter(out));
                                 } else {
                                     exception.printStackTrace(new PrintWriter(out));
                                 }
                             } else {
                                 exception.printStackTrace(new PrintWriter(out));
                             }
                         %></PRE></TD>
            </TR>
        </TABLE>
    </BODY>
</HTML>

