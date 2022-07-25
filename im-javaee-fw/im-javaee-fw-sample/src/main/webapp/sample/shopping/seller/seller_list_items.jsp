<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="/error/error.jsp" %>
<%@ taglib prefix="imartj2ee" uri="http://www.intra-mart.org/taglib/core/framework" %>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Locale"%>
<%@ page import="org.intra_mart.framework.base.message.MessageManager"%>
<%@ page import="org.intra_mart.framework.base.service.ServiceManager"%>
<%@ page import="sample.shopping.view.object.SampleItemViewObject"%>

<imartj2ee:HelperBean id="item_bean" class="sample.shopping.view.bean.SellerListItemsBean" />

<HTML>
	<HEAD>
		<TITLE>
			<imartj2ee:Message application="sample.shopping.conf.shopping" key="seller_list_items.title" />
		</TITLE>

		<SCRIPT LANGUAGE="JavaScript">
		function delConfirm(item_cd){
			var ret = confirm('<imartj2ee:Message application="sample.shopping.conf.shopping" key="confirm.delete" />');
			if(ret == false){
				return false;
			}else{
				document.delete_form.item_cd.value = item_cd;
				document.delete_form.submit();
			}
			return true;
		}
		</SCRIPT>
	</HEAD>
	<BODY>

		<%@ include file="/sample/shopping/common/include/header.jsp" %>

		<BR>

		<%
			Locale locale = ServiceManager.getServiceManager().getLocale(request, response);
			String listTitle = MessageManager.getMessageManager().getMessage("sample.shopping.conf.shopping", "seller_list_items.title", null, locale);
			request.setAttribute("title", listTitle);
		%>
		<jsp:include page="/sample/shopping/common/include/title.jsp" />

		<BR>

		<DIV align="right">
			<imartj2ee:Link application="sample.shopping.conf.shopping" service="seller_list_insert_item">
				<imartj2ee:Message application="sample.shopping.conf.shopping" key="item.new.caption" />
			</imartj2ee:Link>
		</DIV>

		<BR>

		<CENTER>

		<% 	Iterator iter = item_bean.getItems().iterator();
					while(iter.hasNext()){
						SampleItemViewObject item = (SampleItemViewObject)iter.next();
		%>


			<imartj2ee:Form application="sample.shopping.conf.shopping" service="seller_list_update_item" method="POST"  name="update_form">
				<INPUT type="hidden" name="item_cd" value="<%= item.getCode("value") %>" >
				<TABLE  border="0" cellpadding="1" cellspacing="1" width="90%">
					<TBODY>
						<TR>
							<TD rowspan="4"align="center">
								<IMG SRC="<%= item.getImagePath("value") %>" BORDER="0" WIDTH="100" HEIGHT="100">
							</TD>
							<TD width="60%" colspan="2" bgcolor="#CCCCFF">
								<imartj2ee:Message application="sample.shopping.conf.shopping" key="item.name.caption" />&nbsp;:&nbsp;
								<FONT size="+2">
									<%= item.getName("caption") %>
								</FONT>
							</TD>
						</TR>
						<TR bgcolor="aliceblue">
							<TD width="60%" colspan="2">
								<imartj2ee:Message application="sample.shopping.conf.shopping" key="item.price.caption" />&nbsp;:&nbsp;<imartj2ee:Message application="sample.shopping.conf.shopping" key="item.price_unit.caption" />&nbsp;
								<FONT size="+1">
									<%= item.getPriceView("caption") %>
								</FONT>
							</TD>
						</TR>
						<TR bgcolor="aliceblue">
							<TD width="80%" colspan="2">
								<FONT size="-1">
									<%= item.getSimpleNote("caption") %>
								</FONT>
							</TD>
						</TR>
						<TR bgcolor="aliceblue">
							<TD width="60%" >
								<imartj2ee:Link application="sample.shopping.conf.shopping" service="seller_list_detail_item" >
									<imartj2ee:Param name="key" value="seller_list"/>
									<imartj2ee:Param name="item_cd" value="<%= item.getCode()%>"/>
									<FONT size="-1">
										<imartj2ee:Message application="sample.shopping.conf.shopping" key="common.detail.caption" />
									</FONT>
								</imartj2ee:Link>
							</TD>
							<TD  align="right">
								<TABLE border=0>
									<TR>
									<TD>
											<INPUT type="submit" value="<imartj2ee:Message application="sample.shopping.conf.shopping" key="common.update.caption" />">
									</TD>
									<TD>
											<INPUT type="button" value="<imartj2ee:Message application="sample.shopping.conf.shopping" key="common.delete.caption" />" onClick="return delConfirm('<%= item.getCode() %>');">
									</TD>
									</TR>
								</TABLE>
							</TD>
						</TR>
					</TBODY>
				</TABLE>
			</imartj2ee:Form>


		<%
		}
		%>

			<imartj2ee:Form application="sample.shopping.conf.shopping" service="seller_list_delete_item" method="POST"  name="delete_form">
				<INPUT type="hidden" name="item_cd" value="" >
			</imartj2ee:Form>

			<%@ include file="/sample/shopping/common/include/footer.jsp" %>

		</CENTER>

	</BODY>
</HTML>