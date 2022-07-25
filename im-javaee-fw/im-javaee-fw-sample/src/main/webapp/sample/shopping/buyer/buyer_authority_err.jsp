<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="/error/error.jsp" %>
<%@ taglib prefix="imartj2ee" uri="http://www.intra-mart.org/taglib/core/framework" %>
<%@ page import="java.util.Locale"%>
<%@ page import="org.intra_mart.framework.base.message.MessageManager"%>
<%@ page import="org.intra_mart.framework.base.service.ServiceManager"%>

<%
	Locale locale = ServiceManager.getServiceManager().getLocale(request, response);
%>

<HTML>
<HEAD>
	<TITLE><%= MessageManager.getMessageManager().getMessage("sample.shopping.conf.shopping", "authority.title", null, locale) %></TITLE>
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
								<%= MessageManager.getMessageManager().getMessage("sample.shopping.conf.shopping", "authority.notes", null, locale) %>
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
