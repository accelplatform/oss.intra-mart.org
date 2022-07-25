<?xml version="1.0" encoding="Shift_JIS" ?>

<!--
  - <license>
  - Copyright (c) 2003-2004, Sun Microsystems, Inc.
  - All rights reserved.
  - 
  - Redistribution and use in source and binary forms, with or without 
  - modification, are permitted provided that the following conditions are met:
  - 
  -     * Redistributions of source code must retain the above copyright 
  -       notice, this list of conditions and the following disclaimer.
  -     * Redistributions in binary form must reproduce the above copyright 
  -       notice, this list of conditions and the following disclaimer in the
  -       documentation and/or other materials provided with the distribution.
  -     * Neither the name of Sun Microsystems, Inc. nor the names of its 
  -       contributors may be used to endorse or promote products derived from
  -       this software without specific prior written permission.
  - 
  - THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS 
  - "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
  - TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A 
  - PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
  - CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
  - EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
  - ROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
  - PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
  - LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
  - NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
  - SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
  - </license>
  -->

<!--
    Document   : tld-summary.html.xsl
    Created on : December 18, 2002, 3:46 PM
    Author     : mroth
    説明:
        Creates the TLD summary (right frame), listing the tags
        and functions that are in this particular tag library and 
        their descriptions.
-->

<xsl:stylesheet version="1.0"
    xmlns:j2ee="http://java.sun.com/xml/ns/j2ee" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:fo="http://www.w3.org/1999/XSL/Format">
    
    <xsl:output method="html" indent="yes"/>
    
    <xsl:param name="tlddoc-shortName">default</xsl:param>

    <!-- template rule matching source root element -->
    <xsl:template match="/">
      <xsl:apply-templates select="j2ee:tlds/j2ee:taglib"/>
    </xsl:template>
    
    <xsl:template match="j2ee:taglib">
      <xsl:if test="j2ee:short-name=$tlddoc-shortName">
        <xsl:variable name="tldname">
          <xsl:choose>
            <xsl:when test="j2ee:display-name!=''">
              <xsl:value-of select="j2ee:display-name"/>
            </xsl:when>
            <xsl:when test="j2ee:short-name!=''">
              <xsl:value-of select="j2ee:short-name"/>
            </xsl:when>
            <xsl:otherwise>
              Unnamed TLD
            </xsl:otherwise>
          </xsl:choose>
        </xsl:variable>
        <xsl:variable name="title">
          <xsl:value-of select="$tldname"/>
          (<xsl:value-of select="/j2ee:tlds/j2ee:config/j2ee:window-title"/>)
        </xsl:variable>
        <html>
          <head>
            <title><xsl:value-of select="$title"/></title>
            <link rel="stylesheet" type="text/css" href="../stylesheet.css" 
                 title="styie"/>
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
                    <td BGCOLOR="#FFFFFF" CLASS="NavBarCell1Rev">    &#160;<font CLASS="NavBarFont1Rev">&#160;ライブラリ&#160;</font>&#160;</td>
                    <td BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    <font CLASS="NavBarFont1">&#160;タグ&#160;</font></td>
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
                &#160;<a HREF="tld-summary.html" TARGET="_top"><b>フレームなし</b></a>&#160;
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
            <h2><xsl:value-of select="$tldname"/></h2>
            <hr/>
            <xsl:if test="(j2ee:uri!='') and (j2ee:short-name!='')">
              <b>標準構文:</b><br/>
              <code>
                &#160;&#160;&#160;&#160;
                <xsl:choose>
                  <xsl:when test='starts-with(j2ee:uri,"/WEB-INF/tags")'>
                    &lt;%@ taglib prefix="<xsl:value-of select="j2ee:short-name"/>" tagdir="<xsl:value-of select="j2ee:uri"/>" %&gt;<br/>
                  </xsl:when>
                  <xsl:otherwise>
                    &lt;%@ taglib prefix="<xsl:value-of select="j2ee:short-name"/>" uri="<xsl:value-of select="j2ee:uri"/>" %&gt;<br/>
                  </xsl:otherwise>
                </xsl:choose>
              </code>
              <br/>
              <b>XML構文:</b><br/>
              <code>
                &#160;&#160;&#160;&#160;
                <xsl:choose>
                  <xsl:when test='starts-with(j2ee:uri,"/WEB-INF/tags")'>
                    &lt;anyxmlelement xmlns:<xsl:value-of select="j2ee:short-name"/>="urn:jsptagdir:<xsl:value-of select="j2ee:uri"/>" /&gt;<br/>
                  </xsl:when>
                  <xsl:when test='starts-with(j2ee:uri,"/")'>
                    &lt;anyxmlelement xmlns:<xsl:value-of select="j2ee:short-name"/>="urn:jsptld:<xsl:value-of select="j2ee:uri"/>" /&gt;<br/>
                  </xsl:when>
                  <xsl:otherwise>
                    &lt;anyxmlelement xmlns:<xsl:value-of select="j2ee:short-name"/>="<xsl:value-of select="j2ee:uri"/>" /&gt;<br/>
                  </xsl:otherwise>
                </xsl:choose>
              </code>
              <hr/>
            </xsl:if>
            <xsl:choose>
              <xsl:when test="j2ee:description!=''">
				<blockquote>
                 <xsl:apply-templates select="j2ee:description"/>
				</blockquote>
              </xsl:when>
              <xsl:otherwise>
                説明なし
              </xsl:otherwise>
            </xsl:choose>
            <p/>
            <table border="1" cellpadding="3" cellspacing="0" width="100%">
              <tr bgcolor="#CCCCFF" class="TableHeadingColor">
                <td colspan="2">
                  <font size="+2"><b>タグライブラリ情報</b></font>
                </td>
              </tr>
              <tr>
                <td>表示名</td>
                <xsl:choose>
                  <xsl:when test="j2ee:display-name!=''">
                    <td><xsl:value-of select="j2ee:display-name"/></td>
                  </xsl:when>
                  <xsl:otherwise>
                    <td><i>なし</i></td>
                  </xsl:otherwise>
                </xsl:choose>
              </tr>
              <tr>
                <td>Version</td>
                <xsl:choose>
                  <xsl:when test="j2ee:tlib-version!=''">
                    <td><xsl:value-of select="j2ee:tlib-version"/></td>
                  </xsl:when>
                  <xsl:otherwise>
                    <td><i>なし</i></td>
                  </xsl:otherwise>
                </xsl:choose>
              </tr>
              <tr>
                <td>Short Name</td>
                <xsl:choose>
                  <xsl:when test="j2ee:short-name!=''">
                    <td><xsl:value-of select="j2ee:short-name"/></td>
                  </xsl:when>
                  <xsl:otherwise>
                    <td><i>なし</i></td>
                  </xsl:otherwise>
                </xsl:choose>
              </tr>
              <tr>
                <td>URI</td>
                <xsl:choose>
                  <xsl:when test="j2ee:uri!=''">
                    <td><xsl:value-of select="j2ee:uri"/></td>
                  </xsl:when>
                  <xsl:otherwise>
                    <td><i>なし</i></td>
                  </xsl:otherwise>
                </xsl:choose>
              </tr>
            </table>
            &#160;
            <p/>
            <!-- tags and tag files -->
            <xsl:if test="(count(j2ee:tag)+count(j2ee:tag-file)) > 0">
              <table border="1" cellpadding="3" cellspacing="0" width="100%">
                <tr bgcolor="#CCCCFF" class="TableHeadingColor">
                  <td colspan="2">
                    <font size="+2"><b>タグ 一覧</b></font>
                  </td>
                </tr>
                <xsl:apply-templates select="j2ee:tag|j2ee:tag-file">
                  <xsl:sort select="count(j2ee:description/deprecated)"/>
                  <xsl:sort select="../j2ee:short-name"/>
                  <xsl:sort select="j2ee:name"/>
                </xsl:apply-templates>
              </table>
              &#160;
              <p/>
            </xsl:if>
            <!-- functions -->
            <xsl:if test="count(j2ee:function) > 0">
              <table border="1" cellpadding="3" cellspacing="0" width="100%">
                <tr bgcolor="#CCCCFF" class="TableHeadingColor">
                  <td colspan="3">
                    <font size="+2"><b>Function Summary</b></font>
                  </td>
                </tr>
                <xsl:apply-templates select="j2ee:function">
                  <xsl:sort select="count(j2ee:description/deprecated)"/>
                  <xsl:sort select="../j2ee:short-name"/>
                  <xsl:sort select="j2ee:name"/>
                </xsl:apply-templates>
              </table>
              &#160;
              <p/>
            </xsl:if>
            <!-- validators -->
            <xsl:if test="count(j2ee:validator) > 0">
              <table border="1" cellpadding="3" cellspacing="0" width="100%">
                <tr bgcolor="#CCCCFF" class="TableHeadingColor">
                  <td colspan="2">
                    <font size="+2"><b>タグライブラリ検証</b></font>
                  </td>
                </tr>
                <xsl:apply-templates select="j2ee:validator"/>
              </table>
              &#160;
              <p/>
            </xsl:if>
            <!-- listeners -->
            <xsl:if test="count(j2ee:listener) > 0">
              <table border="1" cellpadding="3" cellspacing="0" width="100%">
                <tr bgcolor="#CCCCFF" class="TableHeadingColor">
                  <td>
                    <font size="+2"><b>Listeners</b></font>
                  </td>
                </tr>
                <xsl:apply-templates select="j2ee:listener"/>
              </table>
              &#160;
              <p/>
            </xsl:if>
            <!-- taglib-extensions -->

            <!-- =========== START OF NAVBAR =========== -->
            <a name="navbar_bottom"><!-- --></a>
            <table border="0" width="100%" cellpadding="1" cellspacing="0">
            <tr>
              <td COLSPAN="3" BGCOLOR="#EEEEFF" CLASS="NavBarCell1">
                <a NAME="navbar_bottom_firstrow"><!-- --></a>
                <table BORDER="0" CELLPADDING="0" CELLSPACING="3">
                  <tr ALIGN="center" VALIGN="top">
                    <td BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    &#160;<a href="../overview-summary.html"><font CLASS="NavBarFont1"><b>概要</b></font></a>&#160;</td>
                    <td BGCOLOR="#FFFFFF" CLASS="NavBarCell1Rev">    &#160;<font CLASS="NavBarFont1Rev">&#160;ライブラリ&#160;</font>&#160;</td>
                    <td BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    <font CLASS="NavBarFont1">&#160;タグ&#160;</font></td>
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
                &#160;<a HREF="tld-summary.html" TARGET="_top"><b>フレームなし</b></a>&#160;
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
    
    <xsl:template match="j2ee:tag|j2ee:tag-file">
      <tr bgcolor="white" class="TableRowColor">
        <td width="15%">
          <b>
            <xsl:element name="a">
              <xsl:attribute name="href"><xsl:value-of select="file"/>.html</xsl:attribute>
              <xsl:value-of select="j2ee:name"/>
            </xsl:element>
          </b>
        </td>
        <td>
          <xsl:choose>
            <xsl:when test="j2ee:description!=''">
              <xsl:call-template name="abstract"/>
            </xsl:when>
            <xsl:otherwise>
              <i>説明なし</i>
            </xsl:otherwise>
          </xsl:choose>
        </td>
      </tr>
    </xsl:template>

    <xsl:template match="j2ee:function">
      <tr bgcolor="white" class="TableRowColor">
        <td width="15%" nowrap="" align="right">
          <code><xsl:value-of select='substring-before(normalize-space(j2ee:function-signature)," ")'/></code>
        </td>
        <td width="15%" nowrap="">
          <code><b>
            <xsl:element name="a">
              <xsl:attribute name="href"><xsl:value-of select="file"/>.fn.html</xsl:attribute>
              <xsl:value-of select="j2ee:name"/>
            </xsl:element>
            </b>( <xsl:value-of select='substring-after(normalize-space(j2ee:function-signature),"(")'/>            
          </code>
        </td>
        <td>
          <xsl:choose>
            <xsl:when test="j2ee:description!=''">
				<xsl:call-template name="abstract"/>
            </xsl:when>
            <xsl:otherwise>
              <i>説明なし</i>
            </xsl:otherwise>
          </xsl:choose>
        </td>
      </tr>
    </xsl:template>
        
    <xsl:template match="j2ee:validator">
      <tr valign="top" bgcolor="white" class="TableRowColor">
        <td width="15%">
          <b><xsl:value-of select="j2ee:validator-class"/></b>
        </td>
        <td>
          <xsl:choose>
            <xsl:when test="j2ee:description!=''">
              <xsl:value-of select="substring-before(j2ee:description,'。')" disable-output-escaping="yes"/>。
            </xsl:when>
            <xsl:otherwise>
              <i>説明なし</i>
            </xsl:otherwise>
          </xsl:choose>
          <xsl:if test="count(j2ee:init-param)>0">
            <blockquote>
              <b>Initialization Parameters:</b><br/>
              <table border="1">
                <tr>
                  <td><b>Name</b></td>
                  <td><b>Value</b></td>
                  <td><b>説明</b></td>
                </tr>
                <xsl:apply-templates select="j2ee:init-param"/>
              </table>
            </blockquote>
          </xsl:if>
        </td>
      </tr>
    </xsl:template>
    
    <xsl:template match="j2ee:init-param">
      <tr valign="top">
        <td><xsl:value-of select="j2ee:param-name"/></td>
        <td><xsl:value-of select="j2ee:param-value"/></td>
        <td>
          <xsl:choose>
            <xsl:when test="j2ee:param-description!=''">
              <xsl:value-of select="j2ee:param-description"/>
            </xsl:when>
            <xsl:otherwise>
              <i>説明なし</i>
            </xsl:otherwise>
          </xsl:choose>
        </td>
      </tr>
    </xsl:template>
    
    <xsl:template match="j2ee:listener">
      <tr valign="top" bgcolor="white" class="TableRowColor">
        <td>
          <b><xsl:value-of select="j2ee:listener-class"/></b>
        </td>
      </tr>
    </xsl:template>

    <xsl:template name="abstract">
    	<xsl:choose>
    	 <xsl:when test="count(j2ee:description/deprecated)>0">
    	 	<xsl:apply-templates select="j2ee:description/deprecated"/>
         </xsl:when>
         <xsl:otherwise>
	        <xsl:value-of select="substring-before(j2ee:description,'。')" disable-output-escaping="yes"/>。
         </xsl:otherwise>
        </xsl:choose>
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
