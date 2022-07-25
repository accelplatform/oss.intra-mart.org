<?xml version="1.0" encoding="Shift_JIS" ?>

<xsl:stylesheet version="1.0"
		xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns="http://java.sun.com/xml/ns/j2ee">
  <xsl:output method="xml" indent="yes"/>

  <xsl:template match="/taglib">
    <xsl:element name="taglib" namespace="http://java.sun.com/xml/ns/j2ee">
      <xsl:attribute name="xsi:schemaLocation" namespace="http://www.w3.org/2001/XMLSchema-instance">http://java.sun.com/xml/ns/j2ee http://java.sun.com/xml/ns/j2ee/web-jsptaglibrary_2_0.xsd</xsl:attribute>
      <xsl:attribute name="version">2.0</xsl:attribute>
      <xsl:apply-templates select="*"/>
    </xsl:element>
  </xsl:template>
  
  <xsl:template match="tag-extension">
  </xsl:template>
  
  <xsl:template match="function-extension">
  </xsl:template>
  
  <xsl:template match="taglib-extension">
  </xsl:template>

  <xsl:template match="@*|node()">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()"/>
    </xsl:copy>
  </xsl:template>

</xsl:stylesheet>
