<?xml version="1.0" encoding="Shift_JIS" ?>


<xsl:stylesheet version="1.0"
		xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="xml" indent="yes"
      doctype-system="http://java.sun.com/dtd/web-jsptaglibrary_1_2.dtd"
      doctype-public="-//Sun Microsystems, Inc.//DTD JSP Tag Library 1.2//EN"/>

  <xsl:template match="/taglib">
    <taglib>
      <xsl:apply-templates select="tlibversion"/>
      <jsp-version>1.2</jsp-version>
      <xsl:apply-templates select="shortname"/>
      <xsl:apply-templates select="uri"/>
      <xsl:apply-templates select="info"/>
      <xsl:apply-templates select="tag"/>
    </taglib>
  </xsl:template>

  <!-- Rename to tlib-version -->
  <xsl:template match="tlibversion">
    <tlib-version><xsl:apply-templates/></tlib-version>
  </xsl:template>

  <!-- Rename to jsp-version -->
  <xsl:template match="jspversion">
    <jsp-version><xsl:apply-templates/></jsp-version>
  </xsl:template>

  <!-- Rename to short-name -->
  <xsl:template match="shortname">
    <short-name><xsl:apply-templates/></short-name>
  </xsl:template>

  <!-- Preserve uri -->
  <xsl:template match="uri">
    <uri><xsl:apply-templates/></uri>
  </xsl:template>

  <!-- Rename to description -->
  <xsl:template match="info">
    <description><xsl:apply-templates/></description>
  </xsl:template>

  <xsl:template match="tag">
    <tag>
      <xsl:apply-templates select="name"/>
      <xsl:apply-templates select="tagclass"/>
      <xsl:apply-templates select="teiclass"/>
      <xsl:apply-templates select="bodycontent"/>
      <xsl:apply-templates select="info"/>
      <xsl:apply-templates select="attribute"/>
    </tag>
  </xsl:template>

  <!-- Preserve name -->
  <xsl:template match="name">
    <name><xsl:apply-templates/></name>
  </xsl:template>

  <!-- Rename to tag-class -->
  <xsl:template match="tagclass">
    <tag-class><xsl:apply-templates/></tag-class>
  </xsl:template>

  <!-- Rename to tei-class -->
  <xsl:template match="teiclass">
    <tei-class><xsl:apply-templates/></tei-class>
  </xsl:template>

  <!-- Rename to body-content -->
  <xsl:template match="bodycontent">
    <body-content><xsl:apply-templates/></body-content>
  </xsl:template>

  <!-- Rename to description -->
  <xsl:template match="info">
    <description><xsl:apply-templates/></description>
  </xsl:template>

  <!-- Preserve attribute -->
  <xsl:template match="attribute">
    <attribute> 
      <xsl:apply-templates/>
    </attribute>
  </xsl:template>

  <xsl:template match="@*|node()">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()"/>
    </xsl:copy>
  </xsl:template>

</xsl:stylesheet>
