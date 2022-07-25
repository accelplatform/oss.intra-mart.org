<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isErrorPage="true" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="javax.servlet.jsp.JspException" %>

<html>
	<head>
		<title>エラーページ</title>
	</head>
	<body>
		以下のエラーが発生しました。<br>
		<table border="1">
			<tr>
				<td>
				<pre><%
					if (exception == null) {
						out.println("例外が取得できませんでした。");
					}
					else if (exception instanceof JspException) {
					
						Throwable rootCause = ((JspException) exception).getRootCause();
						
						if (rootCause != null) {
							rootCause.printStackTrace(new PrintWriter(out));
						}
						else {
							exception.printStackTrace(new PrintWriter(out));
						}
					}
					else {
						exception.printStackTrace(new PrintWriter(out));
					}
				%></pre>
				</td>
			</tr>
		</table>
	</body>
</html>

