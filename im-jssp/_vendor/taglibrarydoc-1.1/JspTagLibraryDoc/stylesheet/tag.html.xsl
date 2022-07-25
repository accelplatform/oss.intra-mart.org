<?xml version="1.0" encoding="Shift_JIS" ?>


<xsl:stylesheet version="1.0"
    xmlns:j2ee="http://java.sun.com/xml/ns/j2ee" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:fo="http://www.w3.org/1999/XSL/Format">
    
    <xsl:output method="html" indent="yes"/>
    
    <xsl:param name="tlddoc-shortName">default</xsl:param>
    <xsl:param name="tlddoc-tagName">default</xsl:param>

    <!-- template rule matching source root element -->
    <xsl:template match="/">
      <xsl:apply-templates select="j2ee:tlds/j2ee:taglib"/>
    </xsl:template>
    
    <xsl:template match="j2ee:taglib">
      <xsl:if test="j2ee:short-name=$tlddoc-shortName">
        <xsl:apply-templates select="j2ee:tag|j2ee:tag-file"/>
      </xsl:if>
    </xsl:template>
    
    <xsl:template match="j2ee:tag|j2ee:tag-file">
      <xsl:if test="j2ee:name=$tlddoc-tagName">
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
                  <xsl:attribute name="href"><xsl:value-of select="file"/>.html</xsl:attribute>
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
            タグ <xsl:value-of select="j2ee:name"/></h2>
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
            
            <!-- タグ情報 -->
            <table border="1" cellpadding="3" cellspacing="0" width="100%">
              <tr bgcolor="#CCCCFF" class="TableHeadingColor">
                <td colspan="2">
                  <font size="+2">
                    <b>タグ情報</b>
                  </font>
                </td>
              </tr>
<!--
              <tr>
                <td>タグ クラス</td>
                <td>
                  <xsl:choose>
                    <xsl:when test="j2ee:tag-class!=''">
                      <xsl:value-of select="j2ee:tag-class"/>
                    </xsl:when>
                    <xsl:otherwise>
                      <i>なし</i>
                    </xsl:otherwise>
                  </xsl:choose>
                </td>
              </tr>
              <tr>
                <td>タグ拡張情報 クラス</td>
                <td>
                  <xsl:choose>
                    <xsl:when test="j2ee:tei-class!=''">
                      <xsl:value-of select="j2ee:tei-class"/>
                    </xsl:when>
                    <xsl:otherwise>
                      <i>なし</i>
                    </xsl:otherwise>
                  </xsl:choose>
                </td>
              </tr>
-->
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
              <tr>
                <td>ボディ コンテンツ</td>
                <td>
                  <xsl:choose>
                    <xsl:when test="j2ee:body-content!=''">
                      <xsl:value-of select="j2ee:body-content"/>
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
            
            <!-- Attribute Information -->
            <table border="1" cellpadding="3" cellspacing="0" width="100%">
              <tr bgcolor="#CCCCFF" class="TableHeadingColor">
                <td colspan="5">
                  <font size="+2">
                    <b>属性一覧</b>
                  </font>
                </td>
              </tr>
              <xsl:choose>
                <xsl:when test="count(j2ee:attribute)>0">
                  <tr>
                    <td><b>名称</b></td>
                    <td><b>必須</b></td>
                    <td><b>実行時評価</b></td>
                    <td><b>型</b></td>
                    <td><b>説明</b></td>
                  </tr>
                  <xsl:apply-templates select="j2ee:attribute"/>
                </xsl:when>
                <xsl:otherwise>
                  <td colspan="5"><i>属性定義なし</i></td>
                </xsl:otherwise>
              </xsl:choose>
            </table>
            <br/>
            <p/>
<!--
            Variable Information 
            <table border="1" cellpadding="3" cellspacing="0" width="100%">
              <tr bgcolor="#CCCCFF" class="TableHeadingColor">
                <td colspan="5">
                  <font size="+2">
                    <b>変数一覧</b>
                  </font>
                </td>
              </tr>
              <xsl:choose>
                <xsl:when test="count(j2ee:variable)>0">
                  <tr>
                    <td><b>名称</b></td>
                    <td><b>型</b></td>
                    <td><b>Declare</b></td>
                    <td><b>Scope</b></td>
                    <td><b>説明</b></td>
                  </tr>
                  <xsl:apply-templates select="j2ee:variable"/>
                </xsl:when>
                <xsl:otherwise>
                  <td colspan="2"><i>変数定義なし</i></td>
                </xsl:otherwise>
              </xsl:choose>
            </table>
            <br/>
            <p/>
-->            
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
                  <xsl:attribute name="href"><xsl:value-of select="file"/>.html</xsl:attribute>
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

    <xsl:template match="j2ee:attribute">
      <tr valign="top">
        <td><xsl:apply-templates select="j2ee:name"/></td>
        <td>
          <xsl:choose>
            <xsl:when test="j2ee:required!=''">
              <xsl:value-of select="j2ee:required"/>
            </xsl:when>
            <xsl:otherwise>false</xsl:otherwise>
          </xsl:choose>
        </td>
        <td>
          <xsl:choose>
            <xsl:when test="j2ee:rtexprvalue!=''">
              <xsl:value-of select="j2ee:rtexprvalue"/>
            </xsl:when>
            <xsl:otherwise>false</xsl:otherwise>
          </xsl:choose>
        </td>
        <td>
          <xsl:choose>
            <xsl:when test="type!=''">
              <code><xsl:value-of select="type"/></code>
            </xsl:when>
            <xsl:otherwise><code>java.lang.String</code></xsl:otherwise>
          </xsl:choose>
        </td>
        <td>
          <xsl:choose>
            <xsl:when test="j2ee:description!=''">
			  	<xsl:apply-templates select="j2ee:description"/>
            </xsl:when>
            <xsl:otherwise><i>説明なし</i></xsl:otherwise>
          </xsl:choose>
        </td>
      </tr>
    </xsl:template>
    
    <xsl:template match="j2ee:variable">
      <tr>
        <td>
          <xsl:choose>
            <xsl:when test="j2ee:name-given!=''">
              <xsl:value-of select="j2ee:name-given"/>
            </xsl:when>
            <xsl:when test="j2ee:name-from-attribute!=''">
              <i>From attribute '<xsl:value-of select="j2ee:name-from-attribute"/>'</i>
            </xsl:when>
            <xsl:otherwise>
              <i>Unknown</i>
            </xsl:otherwise>
          </xsl:choose>
        </td>
        <td>
          <xsl:choose>
            <xsl:when test="j2ee:variable-class!=''">
              <code><xsl:value-of select="j2ee:variable-class"/></code>
            </xsl:when>
            <xsl:otherwise><code>java.lang.String</code></xsl:otherwise>
          </xsl:choose>
        </td>
        <td>
          <xsl:choose>
            <xsl:when test="j2ee:declare!=''">
              <xsl:value-of select="j2ee:declare"/>
            </xsl:when>
            <xsl:otherwise>true</xsl:otherwise>
          </xsl:choose>
        </td>
        <td>
          <xsl:choose>
            <xsl:when test="j2ee:scope!=''">
              <xsl:value-of select="j2ee:scope"/>
            </xsl:when>
            <xsl:otherwise>NESTED</xsl:otherwise>
          </xsl:choose>
        </td>
        <td>
          <xsl:choose>
            <xsl:when test="j2ee:description!=''">
				<xsl:apply-templates select="j2ee:description"/>
            </xsl:when>
            <xsl:otherwise><i>説明なし</i></xsl:otherwise>
          </xsl:choose>
        </td>
      </tr>
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
