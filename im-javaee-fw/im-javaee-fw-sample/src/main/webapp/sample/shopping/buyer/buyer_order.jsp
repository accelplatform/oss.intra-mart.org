<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="/error/error.jsp" %>
<%@ taglib prefix="imartj2ee" uri="http://www.intra-mart.org/taglib/core/framework" %>
<%@ page import="java.util.Locale" %>
<%@ page import="org.intra_mart.framework.base.message.MessageManager"%>
<%@ page import="org.intra_mart.framework.base.service.ServiceManager"%>
<HTML>
<HEAD>
<TITLE><imartj2ee:Message application="sample.shopping.conf.shopping" key="order.title" /></TITLE>

<SCRIPT language="JavaScript">

function inp_chk(){
	var famiry_name = document.mainform.famiry_name.value;
	var first_name = document.mainform.first_name.value;
	var post_first = document.mainform.post_first.value;
	var post_second = document.mainform.post_second.value;
	var address1 = document.mainform.address1.value;
	var address2 = document.mainform.address2.value;
	var tel_no = document.mainform.tel_no.value;
	
	if(famiry_name == ""){
		alert("<imartj2ee:Message application="sample.shopping.conf.shopping" key="check.famiry_name" />");
		return;
	}
	if(first_name == ""){
		alert("<imartj2ee:Message application="sample.shopping.conf.shopping" key="check.first_name" />");
		return;
	}
	if(post_first == "" || post_second == ""){
		alert("<imartj2ee:Message application="sample.shopping.conf.shopping" key="check.post" />");
		return;
	}
	if(post_first.search(/\D/) != -1 || post_second.search(/\D/) != -1){
		alert("<imartj2ee:Message application="sample.shopping.conf.shopping" key="check.post_search" />");
		return;
	}
	if(address1 == "" && address2 == ""){
		alert("<imartj2ee:Message application="sample.shopping.conf.shopping" key="check.address" />");
		return;
	}
	if(tel_no == ""){
		alert("<imartj2ee:Message application="sample.shopping.conf.shopping" key="check.tel_no" />");
		return;
	}

	bChk = confirm("<imartj2ee:Message application="sample.shopping.conf.shopping" key="check.success" />");

	if(bChk){
		document.mainform.submit();
	}
}

</SCRIPT>

</HEAD>


<BODY>

<%@ include file="/sample/shopping/common/include/header.jsp" %>

<BR>

<%
	Locale locale = ServiceManager.getServiceManager().getLocale(request, response);
	String detailTitle = MessageManager.getMessageManager().getMessage("sample.shopping.conf.shopping", "order.title", null, locale);
	request.setAttribute("title", detailTitle);
%>
<jsp:include page="/sample/shopping/common/include/title.jsp" />

<BR><BR>

<imartj2ee:Form application="sample.shopping.conf.shopping" service="buyer_order_submit" name="mainform" method="POST">

<CENTER>

<TABLE border="0" cellspacing="2" cellpadding="1" width="85%">
	<TR>
		<TH bgcolor="#CCCCFF" rowspan="2" width="20%" nowrap>
			<imartj2ee:Message application="sample.shopping.conf.shopping" key="order.full_name" />
		</TH>
		<TD bgcolor="#CCCCFF" nowrap>
			<imartj2ee:Message application="sample.shopping.conf.shopping" key="order.famiry_name" />
		</TD>
		<TD bgcolor="aliceblue" nowrap>
			<INPUT type="text" name="famiry_name" size="30">
		</TD>
	</TR>
	<TR>
		<TD bgcolor="#CCCCFF" nowrap>
			<imartj2ee:Message application="sample.shopping.conf.shopping" key="order.first_name" />
		</TD>
		<TD bgcolor="aliceblue" nowrap>
			<INPUT type="text" name="first_name" size="30">
		</TD>
	</TR>
	<TR>
		<TH bgcolor="#CCCCFF" width="20%" nowrap>
			<imartj2ee:Message application="sample.shopping.conf.shopping" key="order.post" />
		</TH>
		<TD bgcolor="aliceblue" colspan="2" nowrap>
			<INPUT type="text" name="post_first" size="3">
			-
			<INPUT type="text" name="post_second" size="5">
		</TD>
	</TR>
	<TR>
		<TH bgcolor="#CCCCFF" width="20%" nowrap>
			<imartj2ee:Message application="sample.shopping.conf.shopping" key="order.department" />
		</TH>
		<TD bgcolor="aliceblue" colspan="2" nowrap>
			<SELECT name="department">
			<%
				String[] departmentList = MessageManager.getMessageManager().getMessage("sample.shopping.conf.shopping", "list.department", null, locale).split(",");
				for(int i = 0; i < departmentList.length; i++) {
					out.println("<OPTION value=\"" + i + "\">" + departmentList[i]);
				}
			%>
			</SELECT>
		</TD>
	</TR>
	<TR>
		<TH bgcolor="#CCCCFF" width="20%" nowrap>
			<imartj2ee:Message application="sample.shopping.conf.shopping" key="order.address1" />
		</TH>
		<TD bgcolor="aliceblue" colspan="2" nowrap>
			<INPUT type="text" name="address1" size="70">
		</TD>
	</TR>
	<TR>
		<TH bgcolor="#CCCCFF" width="20%" nowrap>
			<imartj2ee:Message application="sample.shopping.conf.shopping" key="order.address2" />
		</TH>
		<TD bgcolor="aliceblue" colspan="2" nowrap>
			<INPUT type="text" name="address2" size="70">
		</TD>
	</TR>
	<TR>
		<TH bgcolor="#CCCCFF" width="20%" nowrap>
			<imartj2ee:Message application="sample.shopping.conf.shopping" key="order.tel_no" />
		</TH>
		<TD bgcolor="aliceblue" colspan="2" nowrap>
			<INPUT type="text" name="tel_no" size="30">
		</TD>
	</TR>
	<TR>
		<TH bgcolor="#CCCCFF" width="20%" nowrap>
			<imartj2ee:Message application="sample.shopping.conf.shopping" key="order.birth_day" />
		</TH>
		<TD bgcolor="aliceblue" colspan="2" nowrap>
			<SELECT name="birth_year">
				<OPTION value="1970">1970
				<OPTION value="1971">1971
				<OPTION value="1972">1972
				<OPTION value="1973">1973
				<OPTION value="1974">1974
				<OPTION value="1975">1975
				<OPTION value="1976">1976
				<OPTION value="1977">1977
				<OPTION value="1978">1978
				<OPTION value="1979">1979
				<OPTION value="1980">1980
				<OPTION value="1981">1981
				<OPTION value="1982">1982
				<OPTION value="1983">1983
				<OPTION value="1984">1984
				<OPTION value="1985">1985
				<OPTION value="1986">1986
				<OPTION value="1987">1987
				<OPTION value="1988">1988
				<OPTION value="1989">1989
				<OPTION value="1990">1990
			</SELECT>
			<imartj2ee:Message application="sample.shopping.conf.shopping" key="order.year" />
			<SELECT name="birth_month">
				<OPTION value="1">1
				<OPTION value="2">2
				<OPTION value="3">3
				<OPTION value="4">4
				<OPTION value="5">5
				<OPTION value="6">6
				<OPTION value="7">7
				<OPTION value="8">8
				<OPTION value="9">9
				<OPTION value="10">10
				<OPTION value="11">11
				<OPTION value="12">12
			</SELECT>
			<imartj2ee:Message application="sample.shopping.conf.shopping" key="order.month" />
			<SELECT name="birth_date">
				<OPTION value="1">1
				<OPTION value="2">2
				<OPTION value="3">3
				<OPTION value="4">4
				<OPTION value="5">5
				<OPTION value="6">6
				<OPTION value="7">7
				<OPTION value="8">8
				<OPTION value="9">9
				<OPTION value="10">10
				<OPTION value="11">11
				<OPTION value="12">12
				<OPTION value="13">13
				<OPTION value="14">14
				<OPTION value="15">15
				<OPTION value="16">16
				<OPTION value="17">17
				<OPTION value="18">18
				<OPTION value="19">19
				<OPTION value="20">20
				<OPTION value="21">21
				<OPTION value="22">22
				<OPTION value="23">23
				<OPTION value="24">24
				<OPTION value="25">25
				<OPTION value="26">26
				<OPTION value="27">27
				<OPTION value="28">28
				<OPTION value="29">29
				<OPTION value="30">30
				<OPTION value="31">31
			</SELECT>
			<imartj2ee:Message application="sample.shopping.conf.shopping" key="order.day" />
		</TD>
	</TR>
	<TR>
		<TH bgcolor="#CCCCFF" width="20%" nowrap>
			<imartj2ee:Message application="sample.shopping.conf.shopping" key="order.pay" />
		</TH>
		<TD bgcolor="aliceblue" colspan="2" nowrap>
			<INPUT type="radio" name="pay_method" value ="single" checked>
			<imartj2ee:Message application="sample.shopping.conf.shopping" key="order.pay_single" />
			&nbsp;&nbsp;
			<INPUT type="radio" name="pay_method" value="installment">
			<imartj2ee:Message application="sample.shopping.conf.shopping" key="order.pay_installment" />
		</TD>
	</TR>
</TABLE>

<BR>

<DIV align="right">
	<FONT size="-1"><imartj2ee:Message application="sample.shopping.conf.shopping" key="order.notes" /></FONT>
</DIV>

<BR>
<BR>

<TABLE border="0" width="85%">
	<TR>
		<TD align="right">
			<imartj2ee:Link application="sample.shopping.conf.shopping" service="buyer_order_back">
				<imartj2ee:Message application="sample.shopping.conf.shopping" key="link.back_to_cart" />
			</imartj2ee:Link>
		</TD>
	<TR>
		<TD align="right">
			<A href="javascript:inp_chk();">
				<imartj2ee:Message application="sample.shopping.conf.shopping" key="link.order_decide" />
			</A>
		</TD>
	</TR>
</TABLE>

</imartj2ee:Form>

<%@ include file="/sample/shopping/common/include/footer.jsp" %>

</BODY>
</HTML>
