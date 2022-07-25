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
          <title>
            <xsl:value-of select="/j2ee:tlds/j2ee:config/j2ee:window-title"/>
          </title>
        </head>
        <frameset cols="20%,80%">
          <frameset rows="30%,70%">
            <frame src="overview-frame.html" name="tldListFrame"/>
            <frame src="alltags-frame.html" name="tldFrame"/>
          </frameset>
          <frame src="overview-summary.html" name="tagFrame"/>
        </frameset>
        <noframes>
          <h2>Frame Alert</h2>
          <p/>
          This document is designed to be viewed using the frames feature.  
          If you see this message, you are using a non-frame-capable web 
          client.
          <br/>
          Link to <a href="overview-summary.html">Non-frame version.</a>
        </noframes>
      </html>
    </xsl:template>
</xsl:stylesheet> 
