<?xml version="1.0" encoding="Shift_JIS" ?>


<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:fo="http://www.w3.org/1999/XSL/Format"
    xmlns:j2ee="http://java.sun.com/xml/ns/j2ee">
    
    <xsl:output method="html" indent="yes"/>

    <!-- template rule matching source root element -->
    <xsl:template match="/">
      <html>
        <head>
          <title>
            概要 (<xsl:value-of select="/j2ee:tlds/j2ee:config/j2ee:window-title"/>)
          </title>
          <link rel="stylesheet" type="text/css" href="stylesheet.css" title="Style"/>
        </head>
        <script>
          function asd() {
            parent.document.title="概要 (<xsl:value-of select="normalize-space(/j2ee:tlds/j2ee:config/j2ee:window-title)"/>)";
          }
        </script>
        <body bgcolor="white" onload="asd();">
          <table border="0" width="100%">
            <tr>
              <td nowrap="true">
                <font size="+1" class="FrameTitleFont">
                  <b>タグライブラリ ドキュメント</b>
                </font>
              </td>
            </tr>
          </table>
          <table border="0" width="100%">
            <tr>
              <td nowrap="true">
                <font class="FrameItemFont">
                  <a href="alltags-frame.html" target="tldFrame"><xsl:text>すべてのタグおよび関数</xsl:text></a>
                </font>
                <p/>
                <font size="+1" class="FrameHeadingFont">
                  タグライブラリ
                </font>
                <br/>
                <xsl:apply-templates select="j2ee:tlds/j2ee:taglib"/>
              </td>
            </tr>
          </table>
          <p/>
        </body>
      </html>
    </xsl:template>
    
    <xsl:template match="j2ee:taglib">
      <font class="FrameItemFont">
        <xsl:element name="a">
          <xsl:attribute name="href"><xsl:value-of select="j2ee:short-name"/>/tld-frame.html</xsl:attribute>
          <xsl:attribute name="target">tldFrame</xsl:attribute>
          <xsl:choose>
            <xsl:when test="j2ee:display-name!=''">
              <xsl:value-of select="j2ee:display-name"/>
            </xsl:when>
            <xsl:when test="j2ee:short-name!=''">
              <xsl:value-of select="j2ee:short-name"/>
            </xsl:when>
            <xsl:otherwise>
              タグライブラリ名なし
            </xsl:otherwise>
          </xsl:choose>
        </xsl:element>
      </font>
      <br/>
    </xsl:template>
</xsl:stylesheet> 
