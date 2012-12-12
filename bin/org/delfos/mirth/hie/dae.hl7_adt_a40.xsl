<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="dae.hl7_templates.xsl"/>
	<xsl:output method="xml" indent="yes" />

	<xsl:template match="/ADT_A39">
		<ADT_A18>
			<MSH>
				<xsl:apply-templates  select = "MSH" />
			</MSH>			
			<PID>
				<xsl:apply-templates  select = "ADT_A39.PATIENT/PID" />
			</PID>
			<MRG>
				<xsl:apply-templates select = "ADT_A39.PATIENT/MRG" />
			</MRG>
		</ADT_A18>
	</xsl:template>
</xsl:stylesheet>