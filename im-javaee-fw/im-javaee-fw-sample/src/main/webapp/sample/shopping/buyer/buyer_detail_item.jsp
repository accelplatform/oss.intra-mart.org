<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="/sample/shopping/common/error/detail_item_error.jsp" %>
<%@ taglib prefix="imartj2ee" uri="http://www.intra-mart.org/taglib/core/framework" %>
<%@ page import="java.util.Locale" %>
<%@ page import="org.intra_mart.framework.base.message.MessageManager"%>
<%@ page import="org.intra_mart.framework.base.service.ServiceManager"%>
<%@ page import="sample.shopping.view.object.SampleItemViewObject"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<imartj2ee:HelperBean id="item_bean" class="sample.shopping.view.bean.BuyerDetailItemBean"/>

<%
	SampleItemViewObject item = item_bean.getItem();
%>

<HTML>
<HEAD>
	<TITLE><imartj2ee:Message application="sample.shopping.conf.shopping" key="buyer_detail_item.title" /></TITLE>
</HEAD>


<BODY>

<%@ include file="/sample/shopping/common/include/header.jsp" %>

<BR>

<%
	Locale locale = ServiceManager.getServiceManager().getLocale(request, response);
	String detailTitle = MessageManager.getMessageManager().getMessage("sample.shopping.conf.shopping", "buyer_detail_item.title", null, locale);
	request.setAttribute("title", detailTitle);
%>
<jsp:include page="/sample/shopping/common/include/title.jsp" />

<BR>

<CENTER>
	<FORM>
	<TABLE  border="0" cellpadding="1" cellspacing="1" width="">
		<TBODY>
		<TR >
			<TD rowspan="4"align="center"><IMG SRC="<%= item.getImagePath("value")%>" BORDER="0"></TD>
			<TD width="60%" colspan="2" bgcolor="#CCCCFF">
				<imartj2ee:Message application="sample.shopping.conf.shopping" key="item.name.caption" />&nbsp;:&nbsp;<FONT size="+2"><%= item.getName("caption")%></FONT>
			</TD>
		</TR>
		<TR bgcolor="aliceblue">
			<TD width="60%" colspan="2"><imartj2ee:Message application="sample.shopping.conf.shopping" key="item.price.caption" />&nbsp;:&nbsp;<FONT size="+1"><imartj2ee:Message application="sample.shopping.conf.shopping" key="item.price_unit.caption" />&nbsp;<%= item.getPriceView("caption")%></FONT></TD>
		</TR>
		<TR bgcolor="aliceblue">
			<TD width="80%" colspan="2"><FONT size="-1">
				<%= item.getDetailNote("caption")%>
				</FONT></TD>
		</TR>
		<TR bgcolor="aliceblue">
			<TD width="80%" colspan="2" align="center">
				<FONT size="-1">
					<imartj2ee:Link application="sample.shopping.conf.shopping" service="buyer_detail_back">
						<imartj2ee:Param name="key" value="<%= item_bean.getKey() %>" />
						<imartj2ee:Message application="sample.shopping.conf.shopping" key="common.link.back" />
					</imartj2ee:Link>
				</FONT>
		</TR>
		</TBODY>
	</TABLE>
	</FORM>

<%@ include file="/sample/shopping/common/include/footer.jsp" %>

</CENTER>


</BODY>
</HTML>