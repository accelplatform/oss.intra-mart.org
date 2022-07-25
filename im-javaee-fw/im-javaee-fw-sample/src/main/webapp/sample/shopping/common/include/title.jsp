<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%
	String title = (String)request.getAttribute("title");
%>

<TABLE border="0" width="200" cellpadding="0" cellspacing="0" height="30">
	<TR>
		<TD width="15" height="30"><img src="images/j2ee/sample/image/decoration/title_bar_1.gif" width="15" height="30"></TD>
		<TD width="185" background="images/j2ee/sample/image/decoration/title_bar_2.gif" height="30"><b><%= title%></b></TD>
	</TR>
</TABLE>
