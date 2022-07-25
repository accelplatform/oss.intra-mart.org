<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="/error/error.jsp" %>
<%@ taglib prefix="imartj2ee" uri="http://www.intra-mart.org/taglib/core/framework" %>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Locale" %>
<%@ page import="org.intra_mart.framework.base.message.MessageManager"%>
<%@ page import="org.intra_mart.framework.base.service.ServiceManager"%>
<%@ page import="sample.shopping.view.object.SampleItemViewObject"%>
<%@ page import="sample.shopping.view.object.SampleCartViewObject"%>

<imartj2ee:HelperBean id="cart_bean" class="sample.shopping.view.bean.CartBean"/>

<HTML>
<HEAD>
	<TITLE><imartj2ee:Message application="sample.shopping.conf.shopping" key="buyer_cart.title" /></TITLE>
</HEAD>


<BODY>

<%@ include file="/sample/shopping/common/include/header.jsp" %>

<BR>

<%
	Locale locale = ServiceManager.getServiceManager().getLocale(request, response);
	String cartTitle = MessageManager.getMessageManager().getMessage("sample.shopping.conf.shopping", "buyer_cart.title", null, locale);
	request.setAttribute("title", cartTitle);
%>
<jsp:include page="/sample/shopping/common/include/title.jsp" />

<BR><BR>

<%
	Collection carts = cart_bean.getCarts();
	Iterator iter = carts.iterator();

	if(carts.isEmpty()){
%>
<FONT color="gray"><imartj2ee:Message application="sample.shopping.conf.shopping" key="caution.cart_empty" /></FONT>
<%
	} else{
%>
<FONT color="gray"><imartj2ee:Message application="sample.shopping.conf.shopping" key="info.cart" /></FONT>
<%
	}
%>
<HR>
<BR>

<CENTER>

<%
	while(iter.hasNext()){
		SampleCartViewObject cart = (SampleCartViewObject)iter.next();
		SampleItemViewObject item = cart.getItem();
		int amount = cart.getAmount();
		String sum = cart.getSumPrice();
%>

<TABLE border="0" cellpadding="0" cellspacing="0" width="80%">
	<TR>
		<TD width="40" align="left" rowspan="3" nowrap>
<%
		if(cart.getIsValid()){
%>
			<imartj2ee:Link application="sample.shopping.conf.shopping" service="buyer_cart_detail_item">
				<imartj2ee:Param name="key" value="cart"/>
				<imartj2ee:Param name="item_cd" value="<%= item.getCode()%>"/>
				<IMG src="<%= item.getImagePath()%>" width="40" height="40"></IMG>
			</imartj2ee:Link>
<%
		} else {
%>
			<IMG src="<%= item.getImagePath()%>" width="40" height="40"></IMG>
<%
		}
%>
		</TD>
		<TD width="5%" rowspan="3">
			&nbsp;
		</TD>
		<TD width="70%" bgcolor="#CCCCFF" nowrap>
			<imartj2ee:Message application="sample.shopping.conf.shopping" key="item.name.caption" /> :<FONT size="+2"><%= item.getName()%></FONT>
		</TD>
		<TD width="20%" bgcolor="aliceblue" rowspan="3" align="center" valign="middle" nowrap>
			<imartj2ee:Message application="sample.shopping.conf.shopping" key="item.price_unit.caption" /><%= cart.getSumPriceView()%>
		</TD>
		<TD bgcolor="aliceblue" rowspan="3" align="center" valign="middle" nowrap>
			<imartj2ee:Link application="sample.shopping.conf.shopping" service="buyer_cart_cancel">
				<imartj2ee:Param name="item_cd" value="<%= item.getCode()%>"/>
				[<imartj2ee:Message application="sample.shopping.conf.shopping" key="item.cancel" />]
			</imartj2ee:Link>
		</TD>
	</TR>
	<TR>
		<TD bgcolor="aliceblue">
			<imartj2ee:Message application="sample.shopping.conf.shopping" key="item.price.caption" />&nbsp;:&nbsp;<imartj2ee:Message application="sample.shopping.conf.shopping" key="item.price_unit.caption" />&nbsp;<%= item.getPriceView()%>
		</TD>
	</TR>
	<TR>
		<TD bgcolor="aliceblue" nowrap>
			<imartj2ee:Message application="sample.shopping.conf.shopping" key="item.quantity.caption" />&nbsp;:&nbsp;<%= amount%>&nbsp;<imartj2ee:Message application="sample.shopping.conf.shopping" key="item.quantity_unit.caption" />
		</TD>
	</TR>
</TABLE>
<BR>

<%
	}
%>

<TABLE width="80%">
	<TR>
		<TD align="right">
			<BR>
			<imartj2ee:Link application="sample.shopping.conf.shopping" service="buyer_cart_back">
				<imartj2ee:Message application="sample.shopping.conf.shopping" key="link.back_to_list" />
			</imartj2ee:Link>
		</TD>
	</TR>
	<TR>
		<TD align="right">
			<imartj2ee:Link application="sample.shopping.conf.shopping" service="buyer_cart_order">
				<imartj2ee:Message application="sample.shopping.conf.shopping" key="link.order" />
			</imartj2ee:Link>
		</TD>
	</TR>
</TABLE>

<BR>

<%@ include file="/sample/shopping/common/include/footer.jsp" %>

</CENTER>



</BODY>
</HTML>
