<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="/error/error.jsp" %>
<%@ taglib prefix="imartj2ee" uri="http://www.intra-mart.org/taglib/core/framework" %>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Locale"%>
<%@ page import="org.intra_mart.framework.base.message.MessageManager"%>
<%@ page import="org.intra_mart.framework.base.service.ServiceManager"%>
<%@ page import="sample.shopping.view.object.SampleItemViewObject"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<imartj2ee:HelperBean id="item_bean" class="sample.shopping.view.bean.BuyerListItemsBean"/>

<HTML>
<HEAD>

<TITLE><imartj2ee:Message application="sample.shopping.conf.shopping" key="buyer_list_items.title" /></TITLE>

<SCRIPT language="JavaScript">

function add_item(cd){
	var amount = document.mainform["nm_" + cd].value;
	var amount_num = parseFloat(amount);
	
	if(amount.search(/\D/) != -1){
		alert("<imartj2ee:Message application="sample.shopping.conf.shopping" key="caution.quantity.numeric" />");
		return false;
	}

	if(amount_num % 1 != 0 || amount_num < 1){
		alert("<imartj2ee:Message application="sample.shopping.conf.shopping" key="caution.quantity.numeric" />");
		return false;
	}

	document.mainform.item_cd.value = cd;

	document.mainform.submit();

	return false;
}

</SCRIPT>

</HEAD>


<BODY>

<%@ include file="/sample/shopping/common/include/header.jsp" %>

<BR>

<%
	Locale locale = ServiceManager.getServiceManager().getLocale(request, response);
	String listTitle = MessageManager.getMessageManager().getMessage("sample.shopping.conf.shopping", "buyer_list_items.title", null, locale);
	request.setAttribute("title", listTitle);
%>
<jsp:include page="/sample/shopping/common/include/title.jsp" />

<BR>

<DIV align="right">
	<imartj2ee:Link application="sample.shopping.conf.shopping" service="buyer_list_detail_cart">
		<IMG src="images/j2ee/sample/image/decoration/view_cart.gif">&nbsp;&nbsp;<FONT size="-1"><imartj2ee:Message application="sample.shopping.conf.shopping" key="link.cart" /></FONT>
	</imartj2ee:Link>
</DIV>

<BR>

<CENTER>
	<imartj2ee:Form name="mainform" application="sample.shopping.conf.shopping" service="buyer_list_add_item" method="POST">
		<INPUT type="hidden" name="item_cd" value="">

	<%
		Iterator iter = item_bean.getItems().iterator();
		while(iter.hasNext()){
			SampleItemViewObject item = (SampleItemViewObject)iter.next();
	%>

	<TABLE  border="0" cellpadding="1" cellspacing="1" width="90%">
		<TBODY>
		<TR >
			<TD rowspan="4" align="center">
				<imartj2ee:Link application="sample.shopping.conf.shopping" service="buyer_list_detail_item">
					<imartj2ee:Param name="key" value="buyer_list"/>
					<imartj2ee:Param name="item_cd" value="<%= item.getCode(\"value\")%>"/>
					<IMG SRC="<%= item.getImagePath("value")%>" BORDER="0" WIDTH="100" HEIGHT="100">
				</imartj2ee:Link>
			</TD>
			<TD width="60%" colspan="2" bgcolor="#CCCCFF"><imartj2ee:Message application="sample.shopping.conf.shopping" key="item.name.caption" />&nbsp;:&nbsp;
				<FONT size="+2"><%= item.getName("caption")%></FONT>
			</TD>
		</TR>
		<TR bgcolor="aliceblue">
			<TD width="60%" colspan="2">
				<imartj2ee:Message application="sample.shopping.conf.shopping" key="item.price.caption" />&nbsp;:&nbsp;<imartj2ee:Message application="sample.shopping.conf.shopping" key="item.price_unit.caption" />&nbsp;<FONT size="+1"><%= item.getPriceView()%></FONT>
			</TD>
		</TR>
		<TR bgcolor="aliceblue">
			<TD width="80%" colspan="2">
				<FONT size="-1"><%= item.getSimpleNote("caption")%></FONT>
			</TD>
		</TR>
		<TR bgcolor="aliceblue">
			<TD width="60%" >
				<FONT size="-1">
					<imartj2ee:Link application="sample.shopping.conf.shopping" service="buyer_list_detail_item" >
						<imartj2ee:Param name="key" value="buyer_list"></imartj2ee:Param>
						<imartj2ee:Param name="item_cd" value="<%= item.getCode(\"value\")%>"></imartj2ee:Param>
						<imartj2ee:Message application="sample.shopping.conf.shopping" key="common.detail.caption" />
					</imartj2ee:Link>
				</FONT>
			</TD>
			<TD  align="right">
				<TABLE border=0>
					<TR>
						<TD>
							<imartj2ee:Message application="sample.shopping.conf.shopping" key="item.quantity.caption" />&nbsp;: <INPUT type="text" name="<%= "nm_" + item.getCode("value")%>" value="0" size="5">
						</TD>
						<TD>
							<INPUT type="button" value="<imartj2ee:Message application="sample.shopping.conf.shopping" key="item.put.caption" />" onClick="return add_item('<%= item.getCode("value")%>');">
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
		</TBODY>
	</TABLE>

	<%
		}
	%>

	</imartj2ee:Form>



<%@ include file="/sample/shopping/common/include/footer.jsp" %>

</CENTER>


</BODY>
</HTML>