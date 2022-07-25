<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="imartj2ee" uri="http://www.intra-mart.org/taglib/core/framework" %>
<%@ page errorPage="/error/error.jsp" %>
<%@ page import="org.intra_mart.framework.system.exception.ApplicationException" %>
<%@ page import="java.io.PrintWriter" %>

<imartj2ee:HelperBean id="bean" class="org.intra_mart.framework.base.web.bean.ErrorHelperBean" />

<%
    ApplicationException exception = (ApplicationException)bean.getException();
%>

<HTML>
<HEAD>
    <TITLE>サービス処理エラーページ</TITLE>
		<STYLE type="text/css">
		<!--
		.contntClass {
			font-size: 12px;
		}
		-->
		</STYLE>
</HEAD>

<BODY bgcolor="#FFFFFF" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<TABLE width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
	<TR>
		<TD width="1%" height="1%"><IMG src="images/j2ee/exp/explanation_01.gif" width="64" height="22"></TD>
		<TD width="99%" height="1%" background="images/j2ee/exp/explanation_02.gif" >&nbsp;</TD>
	</TR>
	<TR>
		<TD background="images/j2ee/exp/explanation_04.gif" height="99%" width="1%">&nbsp;</TD>
		<TD valign="top"><br>
			<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
				<TR> 
					<TD bgcolor="#000000" height="1" width="100%"></TD>
				</TR>
			</TABLE>
			<TABLE width="100%" height="37" border="0" cellpadding="0" cellspacing="0">
				<TR>
					<TD width="5%"><IMG src="images/j2ee/mark.gif" width="48" height="37"></TD>
					<TD width="5%">&nbsp;</TD>
					<TD width="95%"><STRONG>
						サービス処理エラーが発生しました。
						</IMART>
					</STRONG></TD>
				</TR>
			</TABLE>
			<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
				<TR> 
					<TD bgcolor="#000000" height="1" width="100%"></TD>
				</TR>
			</TABLE><br>
			<TABLE width="800" border="0" cellpadding="0" cellspacing="0">
				<TR> 
					<TD width="23"><IMG src="images/j2ee/spacer.gif" width="23" height="8"></TD>
					<TD align="left"><span class="contntClass">
						<%= exception.getMessage() %>
						</span>
					</TD>
				</TR>
				<TR>
					<TD>
						&nbsp;
					</TD>
					<TD>
						<PRE><% exception.printStackTrace(new PrintWriter(out)); %></PRE>
						<BR>
						<BR>
					</TD>
				</TR>
				<TR>
					<TD width="23"><IMG src="images/j2ee/spacer.gif" width="23" height="8"></TD>
					<TD align="left">
						<FORM name="login">
							<INPUT type="button" value=" B A C K " onClick="history.back()">
						</FORM>
					</TD>
				</TR>
				<TR>
					<TD width="23"><IMG src="images/j2ee/spacer.gif" width="23" height="8"></TD>
					<TD align="left">
						<span class="contntClass">
						</span>
					</TD>
				</TR>
			</TABLE>
			<BR>
			<BR>
			<BR>
			<BR>
			<BR>
			<BR>
			<BR>
			<br>
			
<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
	<TR>
		<TD bgcolor="#CCCCCC" height="1" width="100%"></TD>
	</TR>
</TABLE>

			<span style="font-size:11px"><BR>
			　　Copyright(C)NTT DATA INTRAMART CO.,LTD 2000-2005 All Rights Reserved.<BR>
			</span></TD>
	</TR>
</TABLE>


</HTML>

