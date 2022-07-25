<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="/error/error.jsp" %>
<%@ taglib prefix="imartj2ee" uri="http://www.intra-mart.org/taglib/core/framework" %>
<%@ page import="java.util.Locale"%>
<%@ page import="org.intra_mart.framework.base.message.MessageManager"%>
<%@ page import="org.intra_mart.framework.base.service.ServiceManager"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<HTML>
	<HEAD>
		<TITLE><imartj2ee:Message application="sample.shopping.conf.shopping" key="seller_insert_item.title" /></TITLE>
        <SCRIPT language="JavaScript">

			function insert_item_check(){
				var item_cd		= document.insertForm.item_cd.value;
				var name		= document.insertForm.name.value;
				var price		= document.insertForm.price.value;
				var image_path	= document.insertForm.image_path.value;

				// check for NotNull
				if((item_cd == "") || (name == "") || (price == "") || (image_path == "")){
					alert("<imartj2ee:Message application="sample.shopping.conf.shopping" key="alert.requied" />");
					return false;
				}

				// check for item_cd
				str = escape(item_cd);
				if (str.indexOf('%') != -1){
					alert("<imartj2ee:Message application="sample.shopping.conf.shopping" key="alert.item_code_ascii" />");
					return false;
				}

				// check for image_path
				str = escape(image_path);
				if (str.indexOf('%') != -1){
					alert("<imartj2ee:Message application="sample.shopping.conf.shopping" key="alert.item_image_ascii" />");
					return false;
				}

				// check for price
				if(price.search(/\D/) != -1){
					alert("<imartj2ee:Message application="sample.shopping.conf.shopping" key="alert.item_price_numeric" />");
					return false;
				}

				if(price % 1 != 0 || price < 1){
					alert("<imartj2ee:Message application="sample.shopping.conf.shopping" key="alert.item_price_numeric" />");
					return false;
				}

				if(price.indexOf('0', 0) == 0){
					alert("<imartj2ee:Message application="sample.shopping.conf.shopping" key="alert.item_price_format" />");
					return false;
				}

				return confirm('<imartj2ee:Message application="sample.shopping.conf.shopping" key="confirm.insert" />');
			}

        </SCRIPT>

    </HEAD>


	<BODY>

		<%@ include file="/sample/shopping/common/include/header.jsp" %>

		<BR>

		<%
			Locale locale = ServiceManager.getServiceManager().getLocale(request, response);
			String pageTitle = MessageManager.getMessageManager().getMessage("sample.shopping.conf.shopping", "seller_insert_item.title", null, locale);
			request.setAttribute("title", pageTitle);
		%>
		<jsp:include page="/sample/shopping/common/include/title.jsp" />

		<BR>

		<DIV align="right">
			<imartj2ee:Link application="sample.shopping.conf.shopping" service="seller_insert_back">
				<imartj2ee:Message application="sample.shopping.conf.shopping" key="common.link.back_to_list" />
			</imartj2ee:Link>
		</DIV>

		<CENTER>
			<imartj2ee:Form application="sample.shopping.conf.shopping" service="seller_insert_submit" method="POST" name="insertForm">
				<TABLE border="0" cellpadding="1" callspacing="1">
					<TR>
						<TD bgcolor="#CCCCFF" align="center">
							&nbsp;<imartj2ee:Message application="sample.shopping.conf.shopping" key="item.code.caption" /> (*)&nbsp;
						</TD>
						<TD>
							<INPUT type="text" name="item_cd" size="30" value="">
						</TD>
					</TR>
					<TR>
						<TD bgcolor="#CCCCFF" align="center">
							&nbsp;<imartj2ee:Message application="sample.shopping.conf.shopping" key="item.name.caption" /> (*)&nbsp;
						</TD>
						<TD>
							<INPUT type="text" name="name" size="30" value="">
						</TD>
					</TR>
					<TR>
						<TD bgcolor="#CCCCFF" align="center">
							&nbsp;<imartj2ee:Message application="sample.shopping.conf.shopping" key="item.price.caption" /> (*)&nbsp;
						</TD>
						<TD>
							<imartj2ee:Message application="sample.shopping.conf.shopping" key="item.price_unit.caption" />
							<INPUT type="text" name="price" size="30" value="">
						</TD>
					</TR>
					<TR>
						<TD bgcolor="#CCCCFF" align="center">
							&nbsp;<imartj2ee:Message application="sample.shopping.conf.shopping" key="item.short_description.caption" />&nbsp;
							<BR>
							<FONT size="-1">
								<imartj2ee:Message application="sample.shopping.conf.shopping" key="item.short_description.note" />
							</FONT>
						</TD>
						<TD>
							<TEXTAREA name="simple_note" cols="60%" rows="5"></TEXTAREA>
						</TD>
					</TR>
					<TR>
						<TD bgcolor="#CCCCFF" align="center">
							&nbsp;<imartj2ee:Message application="sample.shopping.conf.shopping" key="item.detail_description.caption" />&nbsp;
							<BR>
							<FONT size="-1">
								<imartj2ee:Message application="sample.shopping.conf.shopping" key="item.detail_description.note" />
							</FONT>
						</TD>
						<TD>
							<TEXTAREA name="detail_note" cols="60%" rows="15"></TEXTAREA>
						</TD>
					</TR>
					<TR>
						<TD bgcolor="#CCCCFF" align="center">
							&nbsp;<imartj2ee:Message application="sample.shopping.conf.shopping" key="item.image_path.caption" /> (*)&nbsp;
						</TD>
						<TD>
							<INPUT type="text" name="image_path" size="60%" value="images/j2ee/sample/image/item/now_printing.gif"><BR>
						</TD>
					</TR>
				</TABLE>
				<BR>
				<imartj2ee:Message application="sample.shopping.conf.shopping" key="form.note.required"><imartj2ee:MessageParam value="(*)" /></imartj2ee:Message>
				<BR>
				<BR>
				<INPUT type="submit" value="<imartj2ee:Message application="sample.shopping.conf.shopping" key="common.insert.caption" />" onClick="return insert_item_check()">
				<INPUT type="reset" value="<imartj2ee:Message application="sample.shopping.conf.shopping" key="common.clear.caption" />">
			</imartj2ee:Form>

			<%@ include file="/sample/shopping/common/include/footer.jsp" %>

		</CENTER>

	</BODY>
</HTML>