<?xml version="1.0" encoding="Shift_JIS" ?>

<xsl:stylesheet version="1.0"
    xmlns:j2ee="http://java.sun.com/xml/ns/j2ee" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:fo="http://www.w3.org/1999/XSL/Format">
    
    <xsl:output method="html" indent="yes"/>

    <!-- template rule matching source root element -->
    <xsl:template match="/">
      <html>
        <head>
          <title>すべてのタグおよび関数</title>
          <link rel="stylesheet" type="text/css" href="stylesheet.css" title="Style"/>
        </head>
        <script>
          function asd()
          {
            parent.document.title="すべてのタグおよび関数";
          }
        </script>
        <body bgcolor="white" onload="asd();">
          <font size="+1" class="FrameHeadingFont">
          <b>すべてのタグおよび関数</b></font>
          <br/>
          <table border="0" width="100%">
            <tr>
              <td nowrap="true"><font class="FrameItemFont">
                <xsl:apply-templates 
                    select="j2ee:tlds/j2ee:taglib/j2ee:tag|j2ee:tlds/j2ee:taglib/j2ee:tag-file|j2ee:tlds/j2ee:taglib/j2ee:function">
                  <xsl:sort select="count(j2ee:description/deprecated)"/>
                  <xsl:sort select="../j2ee:short-name"/>
                  <xsl:sort select="j2ee:name"/>
                </xsl:apply-templates>
              </font></td>
            </tr>
          </table>
        </body>
      </html>    
    </xsl:template>
    
    <xsl:template match="j2ee:tag|j2ee:tag-file">
      <xsl:element name="a">
        <xsl:attribute name="href"><xsl:value-of select="../j2ee:short-name"/>/<xsl:value-of select="file"/>.html</xsl:attribute>
        <xsl:attribute name="target">tagFrame</xsl:attribute>
        <xsl:value-of select="../j2ee:short-name"/>:<xsl:value-of select="j2ee:name"/>
      </xsl:element>
      <xsl:if test="count(j2ee:description/deprecated)>0">
  	 	<strong><i>非推奨</i></strong>
      </xsl:if>
      <br/>
    </xsl:template>
    
    <!-- 
      - Same as above, but add the () to indicate it's a function 
      - and change the HTML to .fn.html
      -->
    <xsl:template match="j2ee:function">
      <xsl:element name="a">
        <xsl:attribute name="href"><xsl:value-of select="../j2ee:short-name"/>/<xsl:value-of select="file"/>.fn.html</xsl:attribute>
        <xsl:attribute name="target">tagFrame</xsl:attribute>
        <i><xsl:value-of select="../j2ee:short-name"/>:<xsl:value-of select="j2ee:name"/>()</i>
      </xsl:element>
      <xsl:if test="count(j2ee:description/deprecated)>0">
  	 	<strong><i>非推奨</i></strong>
      </xsl:if>
      <br/>
    </xsl:template>
</xsl:stylesheet> 
