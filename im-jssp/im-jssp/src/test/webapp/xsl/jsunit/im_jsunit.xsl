<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template match="js-unit">
  <html>
  <head>
    <title>im-JsUnit Result</title>
    <style type="text/css">
     .tree { line-height: 94%; }
     .tree a { text-decoration: none; color: #000080; }
     .tree a:hover { color: red; }
    </style>
    <script type="text/javascript" src="csjs/im_jsunit.js"></script>
  </head>
  <body onload="init()">
    <h1>im-JsUnit Result</h1>
    <hr/>
    <div class="tree">
      <table border="1" cellpadding="2" >
          <xsl:call-template name="tree_header" />
      </table>
      <br/>
      <xsl:apply-templates />
    </div>
  </body>
  </html>
</xsl:template>

<xsl:template match="js-test-suite">
    <table border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td rowspan="2">
          <a href="#">
            <xsl:attribute name="onclick">return func_suite('tree_item_<xsl:value-of select="@path"/>')</xsl:attribute>
            <img src="images/jsunit/jsunit_suite_plus.gif" border="0" width="16" height="16" hspace="5">
              <xsl:attribute name="id">image_tree_item_<xsl:value-of select="@path"/></xsl:attribute>
            </img>
          </a>
        </td>
      </tr> 
      <tr>
        <td colspan="2">
          <div>
            <table border="0">
              <tr>
                <td>
                  <a href="#">
                    <xsl:attribute name="onclick">return func_suite('tree_item_<xsl:value-of select="@path"/>')</xsl:attribute>
                    <b><font size="4"><xsl:value-of select="@comment"/> [<xsl:value-of select="@path"/>]</font></b>
                  </a>
                </td>
                <td>
                  <table border="1" cellspacing="0" cellpadding="2">
                    <tr>
                      <xsl:call-template name="status" />
                    </tr>
                  </table>
                </td>
              </tr>
           </table>
          </div>
        </td>
      </tr>
      <tr>
        <td width="20"></td>
        <td>
          <div class="titem">
            <xsl:attribute name="id">tree_item_<xsl:value-of select="@path"/></xsl:attribute>
            <xsl:apply-templates />
          </div>
        </td>
      </tr>
    </table>
</xsl:template>

<xsl:template match="js-test">
    <table border="0" cellspacing="0" cellpadding="0">
       <tr>
        <td rowspan="2">
          <a href="#">
            <xsl:attribute name="onclick">return func('tree_item_<xsl:value-of select="@path"/>')</xsl:attribute>
            <img src="images/jsunit/jsunit_plus.gif" border="0" width="16" height="16" hspace="5">
              <xsl:attribute name="id">image_tree_item_<xsl:value-of select="@path"/></xsl:attribute>
            </img>
          </a>
        </td>
      </tr> 
     <tr>
        <td colspan="2">
          <div>
            <table border="0">
              <tr>
                <td>
                  <a href="#">
                    <xsl:attribute name="onclick">return func('tree_item_<xsl:value-of select="@path"/>')</xsl:attribute>
                    <b><font size="4"><xsl:value-of select="@comment"/> [<xsl:value-of select="@path"/>]</font></b>
                  </a>
                </td>
                <td>
                  <table border="1" cellspacing="0" cellpadding="2">
                    <tr>
                      <xsl:call-template name="status" />
                    </tr>
                  </table>
                </td>
              </tr>
           </table>
          </div>
        </td>
      </tr>
      <tr>
        <td width="20"></td>
        <td>
         <div class="titem">
           <xsl:attribute name="id">tree_item_<xsl:value-of select="@path"/></xsl:attribute>
           <table border="1" cellspacing="0" cellpadding="2">
             <xsl:call-template name="function_header" />
             <xsl:apply-templates />
          </table>
        </div>
      </td></tr>
    </table>
</xsl:template>

<xsl:template match="test-function">
  <tr>
    <td>
      <xsl:if test="@error + @failure > 0">
        <a href="#">
          <xsl:attribute name="onclick">return func('tree_item_<xsl:value-of select="../@path"/>_<xsl:value-of select="@name"/>')</xsl:attribute>
          <img src="images/jsunit/jsunit_plus.gif" border="0" hspace="5">
            <xsl:attribute name="id">image_tree_item_<xsl:value-of select="../@path"/>_<xsl:value-of select="@name"/></xsl:attribute>
          </img>
          <b><xsl:value-of select="@name"/></b>
        </a>
      </xsl:if>
      <xsl:if test="@error + @failure = 0">
          <img src="images/jsunit/jsunit_circle.gif" border="0" hspace="5"/><b><xsl:value-of select="@name"/></b>
      </xsl:if>
    </td>
    <xsl:call-template name="status" />
  </tr>
  <tr>
    <td colspan="7">
    <table border="0">
      <tr>
        <td width="20"></td>
        <td>
          <div class="titem">
            <xsl:attribute name="id">tree_item_<xsl:value-of select="../@path"/>_<xsl:value-of select="@name"/></xsl:attribute>
            <xsl:apply-templates />
          </div>
        </td>
      </tr>
    </table>
    </td>
  </tr>
</xsl:template>

<xsl:template match="assertion">
    <b><xsl:value-of select="@comment"/></b>
    <table border="0">
      <tr><td width="20"></td><td><xsl:value-of select="."/>(<xsl:value-of select="@line"/>)</td></tr>
    </table>
</xsl:template>

<xsl:template match="error-assertion">
    <font color="red"><b><xsl:value-of select="@comment"/></b></font>
    <table border="0">
      <tr><td width="20"></td><td><font color="red"><xsl:value-of select="."/>(<xsl:value-of select="@line"/>)</font></td></tr>
    </table>
</xsl:template>

<xsl:template name="status">
    <td align="right" ><b><font color="blue"><xsl:value-of select="@error"/></font></b></td>
    <td align="right" ><b><font color="red"><xsl:value-of select="@failure"/></font></b></td>
    <td align="right" ><b><font color="green"><xsl:value-of select="@total - @failure - @error"/></font></b></td>
    <td align="right" ><b><xsl:value-of select="@total"/></b></td>
    <td align="right" ><xsl:value-of select="@time"/></td>
    <td>
      <table border="0" height="16">
      <tr>
      <xsl:if test='@error > 0' >
      <td bgcolor="blue">
        <xsl:attribute name="width">
          <xsl:value-of select="(@error * 100) div @total"/>
        </xsl:attribute>
      </td>
      </xsl:if>
      <xsl:if test='@failure > 0' >
      <td bgcolor="red">
        <xsl:attribute name="width">
          <xsl:value-of select="(@failure * 100) div @total"/>
        </xsl:attribute>
      </td>
      </xsl:if>
      <xsl:if test='(@total - @failure - @error) > 0' >
      <td bgcolor="green">
        <xsl:attribute name="width">
          <xsl:value-of select="((@total - @failure - @error) * 100) div @total"/>
        </xsl:attribute>
      </td>
      </xsl:if>
      </tr>
      </table>
   </td>
</xsl:template>

<xsl:template name="function_header">
  <tr>
    <td align="center"  nowarp="true"><b>name</b></td>
    <td align="center" width="30" nowarp="true"><b><font color="blue">error</font></b></td>
    <td align="center" width="30" nowarp="true"><b><font color="red">failure</font></b></td>
    <td align="center" width="30" nowarp="true"><b><font color="green">success</font></b></td>
    <td align="center" width="30" nowarp="true"><b>total</b></td>
    <td align="center" width="30" nowarp="true">time</td>
    <td align="center" nowarp="true">graph</td>
  </tr>
</xsl:template>

<xsl:template name="tree_header">
  <tr>
    <td align="center" width="30" nowarp="true"><b><font color="blue">error</font></b></td>
    <td align="center" width="30" nowarp="true"><b><font color="red">failure</font></b></td>
    <td align="center" width="30" nowarp="true"><b><font color="green">success</font></b></td>
    <td align="center" width="30" nowarp="true"><b>total</b></td>
    <td align="center" width="30" nowarp="true">time</td>
    <td align="center" >graph</td>
  </tr>
</xsl:template>

</xsl:stylesheet>

