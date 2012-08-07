<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="dae.hl7_templates.xsl"/>
	<xsl:output method="xml" indent="yes" />

	<xsl:template match="/ADT_A02">
		<ADT_A02>
			<MSH>
				<xsl:apply-templates  select = "MSH" />
			</MSH>			
			<PID>
				<xsl:apply-templates  select = "PID" />
			</PID>
			<PV1>
				<xsl:apply-templates select = "PV1" />
			</PV1>
		</ADT_A02>
	</xsl:template>
</xsl:stylesheet>