<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template name="service">
		<xsl:param name="service"/>
		<xsl:variable name="substring-length" select="string-length($service)"/>
		
		<xsl:choose>
			<xsl:when test="substring($service, $substring-length)='0'">
				<xsl:text>UCI</xsl:text>
			</xsl:when>
			<xsl:when test="substring($service, $substring-length)='1'">
				<xsl:text>URG</xsl:text>
			</xsl:when>		
			<xsl:when test="substring($service, $substring-length)='2'">
				<xsl:text>URO</xsl:text>
			</xsl:when>
			<xsl:when test="substring($service, $substring-length)='3'">
				<xsl:text>RES</xsl:text>
			</xsl:when>		
			<xsl:when test="substring($service, $substring-length)='4'">
				<xsl:text>ORL</xsl:text>
			</xsl:when>		
			<xsl:when test="substring($service, $substring-length)='5'">
				<xsl:text>MED</xsl:text>
			</xsl:when>			
			<xsl:otherwise>
				<xsl:text>DES</xsl:text>
			</xsl:otherwise>		
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>