<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
	<!-- Obtiene la dirección del paciente formateada -->
	<xsl:template name="address">
		<xsl:param name="street_type" />
		<xsl:param name="street_name" />
		<xsl:param name="build_number" />
		<!-- Formato numero_puerta@piso@escalera -->
		<xsl:param name="apartment" />
		
		<!-- Parseado del parámetro $apartment -->
		<xsl:variable name="gate_number" select = "substring-before($apartment, '@')" />
		<xsl:variable name="aux_1" select = "substring-after($apartment, '@')" />
		<xsl:variable name="apartment_number" select = "substring-before($aux_1, '@')" />
		<xsl:variable name="stair" select = "substring-after($aux_1, '@')" />
		
		<xsl:text>
			<xsl:if test="string-length($street_type) != 0">
				<xsl:text><xsl:value-of select="$street_type" /></xsl:text><xsl:text>. </xsl:text>				
			</xsl:if>
			<xsl:if test="string-length($street_name) != 0">
				<xsl:text><xsl:value-of select="$street_name" /></xsl:text>				
			</xsl:if>					
			<xsl:text>, </xsl:text>
			<xsl:if test="string-length($build_number) != 0">
				<xsl:text><xsl:value-of select="$build_number" /></xsl:text>				
			</xsl:if>								
			<xsl:if test="string-length($gate_number) != 0">
				<xsl:text>-</xsl:text><xsl:text><xsl:value-of select="$gate_number" /></xsl:text>				
			</xsl:if>
			<xsl:text>, </xsl:text>
			<xsl:if test="string-length($apartment_number) != 0">
				<xsl:text><xsl:value-of select="$apartment_number" /></xsl:text>				
			</xsl:if>											
			<xsl:if test="string-length($stair) != 0">
				<xsl:text>-</xsl:text><xsl:text><xsl:value-of select="$stair" /></xsl:text>				
			</xsl:if>
		</xsl:text>		
	</xsl:template>
	
	<!-- Obtiene el tipo de ingreso del paciente para Silicon -->
	<xsl:template name="patient_class">
		<xsl:param name="dae_patient_class"/>
		
		<xsl:choose>
			<xsl:when test="E">
				<xsl:text>U</xsl:text>
			</xsl:when>
			<xsl:when test="I">
				<xsl:text>I</xsl:text>
			</xsl:when>
			<xsl:when test="O">
				<xsl:text>A</xsl:text>
			</xsl:when>
			<!-- En caso de no reconocer el tipo de ingreso de DAE, se hace un ingreso hospitalario del paciente -->
			<xsl:otherwise>
				<xsl:text>I</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
	
	</xsl:template>
</xsl:stylesheet>