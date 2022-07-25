<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="/error/error.jsp" %>
<%@ taglib prefix="imartj2ee" uri="http://www.intra-mart.org/taglib/core/framework" %>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Locale"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="org.intra_mart.framework.base.message.MessageManager"%>
<%@ page import="org.intra_mart.framework.base.service.ServiceManager"%>
<%@ page import="sample.shopping.view.object.SampleItemViewObject"%>
<%@ page import="sample.shopping.view.object.SampleCartViewObject"%>

<imartj2ee:HelperBean id="cart_bean" class="sample.shopping.view.bean.CartBean"/>
<imartj2ee:HelperBean id="confirmBean" class="sample.shopping.view.bean.BuyerOrderConfirmBean"/>
<%
	HashMap infos = confirmBean.getInfos();
%>

<HTML>
<HEAD>
	<TITLE><imartj2ee:Message application="sample.shopping.conf.shopping" key="buyer_order_confirm.title" /></TITLE>
</HEAD>


<BODY>

<%@ include file="/sample/shopping/common/include/header.jsp" %>

<BR>

<%
	Locale locale = ServiceManager.getServiceManager().getLocale(request, response);
	String confirmTitle = MessageManager.getMessageManager().getMessage("sample.shopping.conf.shopping", "buyer_order_confirm.title", null, locale);
	request.setAttribute("title", confirmTitle);
%>
<jsp:include page="/sample/shopping/common/include/title.jsp" />

<BR><BR>

<CENTER>

<%
	Collection carts = cart_bean.getCarts();
	Iterator iter = carts.iterator();

	while(iter.hasNext()){
		SampleCartViewObject cart = (SampleCartViewObject)iter.next();
		SampleItemViewObject item = cart.getItem();
		int amount = cart.getAmount();
%>

<TABLE border="0" cellpadding="0" cellspacing="0" width="80%">
	<TR>
		<TD width="40" align="left" rowspan="3" nowrap>
			<IMG src="<%= item.getImagePath()%>" width="40" height="40"></IMG>
		</TD>
		<TD width="5%" rowspan="3">
			&nbsp;
		</TD>
		<TD width="70%" bgcolor="#CCCCFF" nowrap>
			<imartj2ee:Message application="sample.shopping.conf.shopping" key="item.name.caption" />：
			<FONT size="+2">
				<%= item.getName()%>
			</FONT>
		</TD>
		<TD width="20%" bgcolor="aliceblue" rowspan="3" align="center" valign="middle" nowrap>
			&nbsp;<imartj2ee:Message application="sample.shopping.conf.shopping" key="item.price_unit.caption" />&nbsp;<%= cart.getSumPriceView()%>
		</TD>
	</TR>
	<TR>
		<TD bgcolor="aliceblue">
			<imartj2ee:Message application="sample.shopping.conf.shopping" key="item.price.caption" />：&nbsp;<imartj2ee:Message application="sample.shopping.conf.shopping" key="item.price_unit.caption" />&nbsp;<%= item.getPriceView()%>
		</TD>
	</TR>
	<TR>
		<TD bgcolor="aliceblue" nowrap>
			<imartj2ee:Message application="sample.shopping.conf.shopping" key="item.quantity.caption" />：&nbsp;<%= amount%>&nbsp;<imartj2ee:Message application="sample.shopping.conf.shopping" key="item.quantity_unit.caption" />
		</TD>
	</TR>
</TABLE>

<BR>

<%
	}
%>

<BR>

<TABLE cellpaddint="0" cellspacing="1" width="85%">
	<TR>
		<TD width="70%">
			&nbsp;
		</TD>
		<TD bgcolor="#CCCCFF" width="15%" nowrap>
			<FONT size="+1"><B><imartj2ee:Message application="sample.shopping.conf.shopping" key="item.sum.caption" /></B></FONT>
		</TD>
		<TD bgcolor="aliceblue" width="15%" align="right" nowrap>
			&nbsp;<imartj2ee:Message application="sample.shopping.conf.shopping" key="item.price_unit.caption" />&nbsp;<%= cart_bean.getGlobalSumView()%>
		</TD>
	</TR>
</TABLE>

<BR>

<HR width="85%">

<BR>

<imartj2ee:Form application="sample.shopping.conf.shopping" service="buyer_confirm_submit" name="confirm" method="POST">
<TABLE border="1" width="85%">
	<TR><TD>
		<TABLE width="100%">
			<TR>
				<TH bgcolor="#CCCCFF" rowspan="2" width="20%" nowrap>
					<imartj2ee:Message application="sample.shopping.conf.shopping" key="order.full_name" />
				</TH>
				<TH bgcolor="#CCCCFF" width="5%" nowrap>
					<imartj2ee:Message application="sample.shopping.conf.shopping" key="order.famiry_name" />
				</TH>
				<TD bgcolor="aliceblue" width="75%" nowrap>
					<%= infos.get("famiry_name")%><INPUT type="hidden" name="famiry_name" value="<%= infos.get("famiry_name") %>">
				</TD>
			</TR>
			<TR>
				<TH bgcolor="#CCCCFF" width="5%" nowrap>
					<imartj2ee:Message application="sample.shopping.conf.shopping" key="order.first_name" />
				</TH>
				<TD bgcolor="aliceblue" nowrap>
					<%= infos.get("first_name")%><INPUT type="hidden" name="first_name" value="<%= infos.get("first_name") %>">
				</TD>
			</TR>
			<TR>
				<TH bgcolor="#CCCCFF" width="20%" nowrap>
					<imartj2ee:Message application="sample.shopping.conf.shopping" key="order.post" />
				</TH>
				<TD bgcolor="aliceblue" colspan="2" nowrap>
					<%= infos.get("post")%><INPUT type="hidden" name="post" value="<%= infos.get("post") %>">
				</TD>
			</TR>
			<TR>
				<TH bgcolor="#CCCCFF" width="20%" nowrap>
					<imartj2ee:Message application="sample.shopping.conf.shopping" key="order.department" />
				</TH>
				<TD bgcolor="aliceblue" colspan="2" nowrap>
					<%= infos.get("department")%><INPUT type="hidden" name="department" value="<%= infos.get("department") %>">
				</TD>
			</TR>
			<TR>
				<TH bgcolor="#CCCCFF" width="20%" nowrap>
					<imartj2ee:Message application="sample.shopping.conf.shopping" key="order.address1" />
				</TH>
				<TD bgcolor="aliceblue" colspan="2" nowrap>
					<%= infos.get("address1")%><INPUT type="hidden" name="address1" value="<%= infos.get("address1") %>">
				</TD>
			</TR>
			<TR>
				<TH bgcolor="#CCCCFF" width="20%" nowrap>
					<imartj2ee:Message application="sample.shopping.conf.shopping" key="order.address2" />
				</TH>
				<TD bgcolor="aliceblue" colspan="2" nowrap>
					<%= infos.get("address2")%><INPUT type="hidden" name="address2" value="<%= infos.get("address2") %>">
				</TD>
			</TR>
			<TR>
				<TH bgcolor="#CCCCFF" width="20%" nowrap>
					<imartj2ee:Message application="sample.shopping.conf.shopping" key="order.tel_no" />
				</TH>
				<TD bgcolor="aliceblue" colspan="2" nowrap>
					<%= infos.get("tel_no")%><INPUT type="hidden" name="tel_no" value="<%= infos.get("tel_no") %>">
				</TD>
			</TR>
			<TR>
				<TH bgcolor="#CCCCFF" width="20%" nowrap>
					<imartj2ee:Message application="sample.shopping.conf.shopping" key="order.birth_day" />
				</TH>
				<TD bgcolor="aliceblue" colspan="2" nowrap>
					<%= infos.get("birth")%><INPUT type="hidden" name="birth" value="<%= infos.get("birth") %>">
				</TD>
			</TR>
			<TR>
				<TH bgcolor="#CCCCFF" width="20%" nowrap>
					<imartj2ee:Message application="sample.shopping.conf.shopping" key="order.pay" />
				</TH>
				<TD bgcolor="aliceblue" colspan="2" nowrap>
					<%= infos.get("pay_method")%><INPUT type="hidden" name="pay_method" value="<%= infos.get("pay_method") %>">
				</TD>
			</TR>
		</TABLE>
	</TD></TR>
</TABLE>

<BR><BR>

<BR>

<TABLE width="85%">
	<TR>
		<TD align="right">
			<imartj2ee:Submit service="buyer_confirm_submit" value="<%= MessageManager.getMessageManager().getMessage(\"sample.shopping.conf.shopping\", \"item.submit\", null, locale) %>" />
		</TD>
	</TR>
	<TR>
		<TD align="right">
			<imartj2ee:Submit service="buyer_confirm_back_to_order" value="<%= MessageManager.getMessageManager().getMessage(\"sample.shopping.conf.shopping\", \"link.back_to_order\", null, locale) %>" />
		</TD>
	</TR>
	<TR>
		<TD align="right">
			<imartj2ee:Submit service="buyer_confirm_back_to_list" value="<%= MessageManager.getMessageManager().getMessage(\"sample.shopping.conf.shopping\", \"link.back_to_product_list\", null, locale) %>" />
		</TD>
	</TR>
</TABLE>
</imartj2ee:Form>

<%@ include file="/sample/shopping/common/include/footer.jsp" %>

</CENTER>

</BODY>
</HTML>
