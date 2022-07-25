<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="imartj2ee" uri="http://www.intra-mart.org/taglib/core/framework" %>
<%@ page isErrorPage="true" %>
<%@ page errorPage="/error/error.jsp" %>
<%@ page import="sample.shopping.model.event.AlreadyDeletedException" %>

<imartj2ee:HelperBean id="bean" class="org.intra_mart.framework.base.web.bean.ErrorHelperBean" />

<%
	AlreadyDeletedException alreadyDeletedException = null;
	if (exception instanceof AlreadyDeletedException) {
		alreadyDeletedException = (AlreadyDeletedException)exception;
	} else {
		throw exception;
	}
%>

<HTML>
<HEAD>
</HEAD>

<BODY bgcolor="WhiteSmoke">
<CENTER>
<BR>
<TABLE border="0" width="100%">
	<TR bgcolor="MediumSeaGreen">
		<TD>
			<TABLE border="0" cellpadding="0" cellspacing="0" width="100%">
				<TR>
					<TD bgcolor="#008000">
						<TABLE border="0" cellpadding="0" cellspacing="0" height="3"><TR><TD></TD></TR></TABLE>
					</TD>
				</TR>
				<TR>
					<TD bgcolor="#00cc00">
						<TABLE border="0" cellpadding="0" cellspacing="0" height="2"><TR><TD></TD></TR></TABLE>
					</TD>
				</TR>
				<TR>
					<TD bgcolor="#00ff00">
						<TABLE border="0" cellpadding="0" cellspacing="0" height="1"><TR><TD></TD></TR></TABLE>
					</TD>
				</TR>
				<TR>
					<TD bgcolor="#ffffff">
						<TABLE border="0" cellpadding="0" cellspacing="0" height="2"><TR><TD></TD></TR></TABLE>
					</TD>
				</TR>
				<TR>
					<TD bgcolor="#00ff00">
						<TABLE border="0" cellpadding="0" cellspacing="0" height="1"><TR><TD></TD></TR></TABLE>
					</TD>
				</TR>
				<TR>
					<TD bgcolor="#00cc00">
						<TABLE border="0" cellpadding="0" cellspacing="0" height="2"><TR><TD></TD></TR></TABLE>
					</TD>
				</TR>
				<TR>
					<TD bgcolor="#008000">
						<TABLE border="0" cellpadding="0" cellspacing="0" height="3"><TR><TD></TD></TR></TABLE>
					</TD>
				</TR>
			</TABLE>
		</TD>
	</TR>
	<TR><TD bgcolor="Lavender"></TD></TR>
	<TR>
		<TD align="center" bgcolor="Lavender">
			<BR>
			<TABLE border="4" cellpadding="2" cellspacing="1">
				<TR>
					<TH nowrap bgcolor="Pink">
						&nbsp;&nbsp;&nbsp;&nbsp;
							<FONT size="4" color="Crimson">
								<%= alreadyDeletedException.getMessage() %>
							</FONT>
						&nbsp;&nbsp;&nbsp;&nbsp;
					</TH>
				</TR>
			</TABLE>
			<BR>
		</TD>
	</TR>
	<TR><TD bgcolor="Lavender"></TD></TR>
	<TR bgcolor="MediumSeaGreen">
		<TD>
			<TABLE border="0" cellpadding="0" cellspacing="0" width="100%">
				<TR>
					<TD bgcolor="#008000">
						<TABLE border="0" cellpadding="0" cellspacing="0" height="3"><TR><TD></TD></TR></TABLE>
					</TD>
				</TR>
				<TR>
					<TD bgcolor="#00cc00">
						<TABLE border="0" cellpadding="0" cellspacing="0" height="2"><TR><TD></TD></TR></TABLE>
					</TD>
				</TR>
				<TR>
					<TD bgcolor="#00ff00">
						<TABLE border="0" cellpadding="0" cellspacing="0" height="1"><TR><TD></TD></TR></TABLE>
					</TD>
				</TR>
				<TR>
					<TD bgcolor="#ffffff">
						<TABLE border="0" cellpadding="0" cellspacing="0" height="2"><TR><TD></TD></TR></TABLE>
					</TD>
				</TR>
				<TR>
					<TD bgcolor="#00ff00">
						<TABLE border="0" cellpadding="0" cellspacing="0" height="1"><TR><TD></TD></TR></TABLE>
					</TD>
				</TR>
				<TR>
					<TD bgcolor="#00cc00">
						<TABLE border="0" cellpadding="0" cellspacing="0" height="2"><TR><TD></TD></TR></TABLE>
					</TD>
				</TR>
				<TR>
					<TD bgcolor="#008000">
						<TABLE border="0" cellpadding="0" cellspacing="0" height="3"><TR><TD></TD></TR></TABLE>
					</TD>
				</TR>
			</TABLE>
		</TD>
	</TR>
</TABLE>
<BR>
<!-- エラー内容 -->
<imartj2ee:Message application="sample.shopping.conf.shopping" key="item.code.caption" />&nbsp;&nbsp;:&nbsp;&nbsp;<%= alreadyDeletedException.getCode() %>

<!-- 再チャレンジのためのコード -->
<imartj2ee:Form application="sample.shopping.conf.shopping" service="detail_error_next" method="POST" name="nextpage">
	<INPUT type="hidden" name="key" value="<%= alreadyDeletedException.getKey() %>">
	<TABLE width="100%" bgcolor="MediumAquamarine">
		<TR>
			<TD align="center" nowrap>
				<HR>
				<INPUT type="submit" value=" *** N E X T *** ">
				<HR>
			</TD>
		</TR>
	</TABLE>
</imartj2ee:Form>
</CENTER>
</BODY>
</HTML>


<!-- End of File -->
