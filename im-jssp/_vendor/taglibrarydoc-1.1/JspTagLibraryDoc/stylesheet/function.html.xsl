<?xml version="1.0" encoding="Shift_JIS" ?>


<xsl:stylesheet version="1.0"
    xmlns:j2ee="http://java.sun.com/xml/ns/j2ee" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:fo="http://www.w3.org/1999/XSL/Format">
    
    <xsl:output method="html" indent="yes"/>
    
    <xsl:param name="tlddoc-shortName">default</xsl:param>
    <xsl:param name="tlddoc-functionName">default</xsl:param>

    <!-- template rule matching source root element -->
    <xsl:template match="/">
      <xsl:apply-templates select="j2ee:tlds/j2ee:taglib"/>
    </xsl:template>
    
    <xsl:template match="j2ee:taglib">
      <xsl:if test="j2ee:short-name=$tlddoc-shortName">
        <xsl:apply-templates select="j2ee:function"/>
      </xsl:if>
    </xsl:template>
    
    <xsl:template match="j2ee:function">
      <xsl:if test="j2ee:name=$tlddoc-functionName">
        <xsl:variable name="tldname">
          <xsl:choose>
            <xsl:when test="../j2ee:display-name!=''">
              <xsl:value-of select="../j2ee:display-name"/>
            </xsl:when>
            <xsl:when test="../j2ee:short-name!=''">
              <xsl:value-of select="../j2ee:short-name"/>
            </xsl:when>
            <xsl:otherwise>
              Unnamed TLD
            </xsl:otherwise>
          </xsl:choose>
        </xsl:variable>
        <xsl:variable name="title">
          <xsl:value-of select="j2ee:name"/>
          (<xsl:value-of select="/j2ee:tlds/j2ee:config/j2ee:window-title"/>)
        </xsl:variable>
        <html>
          <head>
            <title><xsl:value-of select="$title"/></title>
            <meta name="keywords" content="$title"/>
            <link rel="stylesheet" type="text/css" href="../stylesheet.css" 
                  title="Style"/>
          </head>
          <script>
            function asd()
            {
            parent.document.title="<xsl:value-of select="normalize-space($title)"/>";
            }
          </script>
          <body bgcolor="white" onload="asd();">
            <!-- =========== START OF NAVBAR =========== -->
            <a name="navbar_top"><!-- --></a>
            <table border="0" width="100%" cellpadding="1" cellspacing="0">
            <tr>
              <td COLSPAN="3" BGCOLOR="#EEEEFF" CLASS="NavBarCell1">
                <a NAME="navbar_top_firstrow"><!-- --></a>
                <table BORDER="0" CELLPADDING="0" CELLSPACING="3">
                  <tr ALIGN="center" VALIGN="top">
                    <td BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    &#160;<a href="../overview-summary.html"><font CLASS="NavBarFont1"><b>概要</b></font></a>&#160;</td>
                    <td BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    &#160;<a href="tld-summary.html"><font CLASS="NavBarFont1"><b>ライブラリ</b></font></a>&#160;</td>
                    <td BGCOLOR="#FFFFFF" CLASS="NavBarCell1Rev"> &#160;<font CLASS="NavBarFont1Rev">&#160;タグ&#160;</font>&#160;</td>
                    <td BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    &#160;<a HREF="../help-doc.html"><font CLASS="NavBarFont1"><b>ヘルプ</b></font></a>&#160;</td>
                  </tr>
                </table>
              </td>
              <td ALIGN="right" VALIGN="top" ROWSPAN="3"><em>
                </em>
              </td>
            </tr>
            <tr>
              <td BGCOLOR="white" CLASS="NavBarCell2"><font SIZE="-2">
                <!--&#160;PREV TAGLIB&#160;-->
                <!--&#160;NEXT TAGLIB&#160;-->
              </font></td>
              <td BGCOLOR="white" CLASS="NavBarCell2"><font SIZE="-2">
                &#160;<a HREF="../index.html" TARGET="_top"><b>フレームあり</b></a>&#160;
                &#160;<xsl:element name="a">
                  <xsl:attribute name="href"><xsl:value-of select="file"/>.fn.html</xsl:attribute>
                  <xsl:attribute name="target">_top</xsl:attribute>
                  <b>フレームなし</b>
                </xsl:element>&#160;
                <script>
                  <!--
                  if(window==top) {
                    document.writeln('<A HREF="alltags-noframe.html" TARGET=""><B>すべてのタグ</B></A>');
                  }
                  //-->
                </script>
                <noscript>
                  <a HREF="../alltags-noframe.html" TARGET=""><b>すべてのタグ</b></a>
                </noscript>
              </font></td>
            </tr>
            </table>
            <!-- =========== END OF NAVBAR =========== -->
            
            <hr/>
            <h2><font size="-1"><xsl:value-of select="$tldname"/></font><br/>
            Function <xsl:value-of select="j2ee:name"/></h2>
            <code>
              <xsl:value-of select='substring-before(normalize-space(j2ee:function-signature)," ")'/>
              <b>&#160;<xsl:value-of select="j2ee:name"/></b>(<xsl:value-of 
              select='substring-after(normalize-space(j2ee:function-signature),"(")'/>
            </code>
            <hr/>
       			<blockquote>
            	<xsl:apply-templates select="j2ee:description"/><br/>
				</blockquote>
            <xsl:if test="j2ee:example!=''">
              <b>サンプル:</b><br/>
				<blockquote>
				<xsl:apply-templates select="j2ee:example"/>              
				</blockquote>
            </xsl:if>
            <hr/>
            
            <!-- Function Information -->
            <table border="1" cellpadding="3" cellspacing="0" width="100%">
              <tr bgcolor="#CCCCFF" class="TableHeadingColor">
                <td colspan="2">
                  <font size="+2">
                    <b>Function Information</b>
                  </font>
                </td>
              </tr>
              <tr>
                <td>Function Class</td>
                <td>
                  <xsl:choose>
                    <xsl:when test="j2ee:function-class!=''">
                      <xsl:value-of select="j2ee:function-class"/>
                    </xsl:when>
                    <xsl:otherwise>
                      <i>なし</i>
                    </xsl:otherwise>
                  </xsl:choose>
                </td>
              </tr>
              <tr>
                <td>Function Signature</td>
                <td>
                  <xsl:choose>
                    <xsl:when test="j2ee:function-signature!=''">
                      <xsl:value-of select="j2ee:function-signature"/>
                    </xsl:when>
                    <xsl:otherwise>
                      <i>なし</i>
                    </xsl:otherwise>
                  </xsl:choose>
                </td>
              </tr>
              <tr>
                <td>表示名</td>
                <td>
                  <xsl:choose>
                    <xsl:when test="j2ee:display-name!=''">
                      <xsl:value-of select="j2ee:display-name"/>
                    </xsl:when>
                    <xsl:otherwise>
                      <i>なし</i>
                    </xsl:otherwise>
                  </xsl:choose>
                </td>
              </tr>
            </table>
            <br/>
            <p/>
            
            <!-- =========== START OF NAVBAR =========== -->
            <a name="navbar_bottom"><!-- --></a>
            <table border="0" width="100%" cellpadding="1" cellspacing="0">
            <tr>
              <td COLSPAN="3" BGCOLOR="#EEEEFF" CLASS="NavBarCell1">
                <a NAME="navbar_bottom_firstrow"><!-- --></a>
                <table BORDER="0" CELLPADDING="0" CELLSPACING="3">
                  <tr ALIGN="center" VALIGN="top">
                    <td BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    &#160;<a href="../overview-summary.html"><font CLASS="NavBarFont1"><b>概要</b></font></a>&#160;</td>
                    <td BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    &#160;<a href="tld-summary.html"><font CLASS="NavBarFont1"><b>ライブラリ</b></font></a>&#160;</td>
                    <td BGCOLOR="#FFFFFF" CLASS="NavBarCell1Rev"> &#160;<font CLASS="NavBarFont1Rev">&#160;タグ&#160;</font>&#160;</td>
                    <td BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    &#160;<a HREF="../help-doc.html"><font CLASS="NavBarFont1"><b>ヘルプ</b></font></a>&#160;</td>
                  </tr>
                </table>
              </td>
              <td ALIGN="right" VALIGN="top" ROWSPAN="3"><em>
                </em>
              </td>
            </tr>
            <tr>
              <td BGCOLOR="white" CLASS="NavBarCell2"><font SIZE="-2">
                <!--&#160;PREV TAGLIB&#160;-->
                <!--&#160;NEXT TAGLIB&#160;-->
              </font></td>
              <td BGCOLOR="white" CLASS="NavBarCell2"><font SIZE="-2">
                &#160;<a HREF="../index.html" TARGET="_top"><b>フレームあり</b></a>&#160;
                &#160;<xsl:element name="a">
                  <xsl:attribute name="href"><xsl:value-of select="file"/>.fn.html</xsl:attribute>
                  <xsl:attribute name="target">_top</xsl:attribute>
                  <b>フレームなし</b>
                </xsl:element>&#160;
                <script>
                  <!--
                  if(window==top) {
                    document.writeln('<A HREF="alltags-noframe.html" TARGET=""><B>すべてのタグ</B></A>');
                  }
                  //-->
                </script>
                <noscript>
                  <a HREF="../alltags-noframe.html" TARGET=""><b>すべてのタグ</b></a>
                </noscript>
              </font></td>
            </tr>
            </table>
            <!-- =========== END OF NAVBAR =========== -->
            <hr/>            
          </body>
        </html>
      </xsl:if>
    </xsl:template>

    <xsl:template match="deprecated">
    	<strong><i>奨励されていません。<xsl:apply-templates/></i></strong>
	</xsl:template>

	<xsl:template match="node()">
	    <xsl:copy>
	        <xsl:for-each select="@*">
	            <xsl:copy />
	        </xsl:for-each>
	        <xsl:apply-templates />
	    </xsl:copy>
	</xsl:template>

</xsl:stylesheet> 
