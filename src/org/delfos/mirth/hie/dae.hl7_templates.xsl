<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match = "MSH">			
		<xsl:copy-of select="MSH.1" />			
		<xsl:copy-of select="MSH.2" />
		<xsl:copy-of select="MSH.3" />		
		<xsl:copy-of select="MSH.4" />
		<xsl:copy-of select="MSH.5" />
		<xsl:copy-of select="MSH.6" />
		<xsl:copy-of select="MSH.7" />
		<xsl:copy-of select="MSH.9" />
		<xsl:copy-of select="MSH.10" />
		<xsl:copy-of select="MSH.11" />
		<MSH.12>
			<VID.1>2.5</VID.1>
		</MSH.12>
	</xsl:template>
	
	<xsl:template match= "EVN" >
		<xsl:copy-of select="." />		
	</xsl:template>
	
	<xsl:template match= "PID" >
		<xsl:copy-of select="PID.2" />
		<xsl:copy-of select="PID.3" />
		<PID.4>
			<CX.1><xsl:value-of select="PID.20/DLN.1" /></CX.1>
		</PID.4>
		<PID.5>
			<xsl:copy-of select="PID.5/XPN.1" />
			<XPN.2>
				<xsl:value-of select="PID.5/XPN.3" />
			</XPN.2>		
		</PID.5>
		<PID.6>
			<XPN.1>
				<FN.1>
					<xsl:value-of select="PID.5/XPN.2" />
				</FN.1>
			</XPN.1>
		</PID.6>	
		<xsl:copy-of select="PID.7" />
		<PID.8>	
			<xsl:choose>
				<xsl:when test="PID.8='H'">M</xsl:when>
				<xsl:when test="PID.8='M'">F</xsl:when>
				<xsl:otherwise>O</xsl:otherwise>
			</xsl:choose>
		</PID.8>	
		<PID.11>
			<XAD.1>
				<SAD.1>
					<xsl:value-of select="PID.11/XAD.1" />
				</SAD.1>
			</XAD.1>
			<xsl:copy-of select="PID.11/XAD.3" />
			<xsl:copy-of select="PID.11/XAD.4" />
			<xsl:copy-of select="PID.11/XAD.5" />			
		</PID.11>		
		<xsl:copy-of select="PID.13" />
		<xsl:copy-of select="PID.15" />
		<xsl:copy-of select="PID.19" />
		<!-- 
			Estos campos: cuidad de nacimiento y nacionalidad, no se donde están en el XML
			de adminisiones
		 -->
		<xsl:copy-of select="PID.23" />
		<xsl:copy-of select="PID.28" />
		
	</xsl:template>
	
	<xsl:template match="PV1">
		<PV1.1>
			<xsl:value-of select="PV1.19/CX.1" />
		</PV1.1>
		<xsl:copy-of select="PV1.2" />
		<!--<xsl:copy-of select="PV1.3" />-->
		<PV1.3>
			<xsl:copy-of select="PV1.3/PL.1" />
			<xsl:copy-of select="PV1.3/PL.2" />
			<xsl:copy-of select="PV1.3/PL.3" />
			<xsl:choose>
				<xsl:when test="PV1.3/PL.4/HD.1">
					<xsl:copy-of select="PV1.3/PL.4" />
				</xsl:when>
				<xsl:otherwise>
					<PL.4>
						<HD.1>1</HD.1> <!-- Si admisiones no informa el hospital, lo informamos nosotros -->
					</PL.4>
				</xsl:otherwise>			
			</xsl:choose>			
			<PL.7>
				<xsl:value-of select="PV1.3/PL.4/HD.1" />
			</PL.7>
		</PV1.3>
		<!-- The Origin ICU field is not use yet  -->
		<!-- <xsl:copy-of select="PV1.5" /> -->
		<PV1.7>
			<XCN.1><xsl:value-of select="PV1.17/XCN.1" /></XCN.1>
			<XCN.2>
				<FN.1><xsl:value-of select="PV1.17/XCN.3" /><xsl:text> </xsl:text><xsl:value-of select="PV1.17/XCN.2/FN.1" /></FN.1>
			</XCN.2>
			<XCN.11><xsl:value-of select="PV1.17/XCN.1" /></XCN.11>
		</PV1.7>
		<xsl:copy-of select="PV1.10" />
		<!-- El elemento /PV1.12 se ha eliminado -->
		<xsl:copy-of select="PV1.14" />
		<PV1.19>
			<xsl:copy-of select="PV1.19/CX.1" />
		</PV1.19>
		<xsl:copy-of select="PV1.44" />
	</xsl:template>	
	
	

</xsl:stylesheet>