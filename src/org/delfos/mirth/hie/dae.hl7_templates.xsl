<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:xalan="http://xml.apache.org/xalan" exclude-result-prefixes="xalan">	
	
	<xsl:import href="utils.xsl"/>
	
	<xsl:template match = "MSH">			
		<MSH>
			<!-- Sistema que genera el mensaje -->
			<MSH.3>
				<HD.1>DAE</HD.1>
			</MSH.3>
			<!-- Código del hospital -->
			<MSH.4>
				<HD.1>1</HD.1>
			</MSH.4>
			<!-- Código del sistema destino del mensaje -->
			<MSH.5>
				<HD.1>SILICON</HD.1>
			</MSH.5>
			<!-- Código del hospital -->
			<MSH.6>
				<HD.1>1</HD.1>
			</MSH.6>
			<xsl:copy-of select="MSH.9" />
			<xsl:copy-of select="MSH.10" />
		</MSH>
	</xsl:template>
	
	<xsl:template match = "PID" >
		
		<PID>
			<!-- NUHSA -->
			<PID.3>
				<xsl:copy-of select="PID.3[1]/CX.1" />
			</PID.3>
			<!-- DNI -->
			<PID.4>
				<xsl:copy-of select="PID.3[3]/CX.1" />
			</PID.4>
			<PID.5>
				<xsl:copy-of select="PID.5/XPN.1" />
				<XPN.2>
					<xsl:value-of select="PID.5/XPN.2" />
				</XPN.2>		
			</PID.5>
			<xsl:copy-of select="PID.6" />
			<xsl:copy-of select="PID.7" />
			<xsl:copy-of select="PID.8"/>
			<PID.11>
				<XAD.1>
					<SAD.1>						
						<xsl:call-template name="address">
							<xsl:with-param name="street_type" select="PID.11/XAD.1/SAD.1" />
							<xsl:with-param name="street_name" select="PID.11/XAD.1/SAD.2" />
							<xsl:with-param name="build_number" select="PID.11/XAD.1/SAD.3" />
							<xsl:with-param name="apartment" select="PID.11/XAD.2" />
						</xsl:call-template>
					</SAD.1>
				</XAD.1>
				<!-- TODO - Pendiente de incidencia: http://localhost/show_bug.cgi?id=5 -->
				<xsl:copy-of select="PID.11/XAD.3" />
				<!-- TODO - Pendiente de la incidencia: http://localhost/show_bug.cgi?id=6 -->
				<xsl:copy-of select="PID.11/XAD.4" />
				<xsl:copy-of select="PID.11/XAD.5" />	
				<xsl:copy-of select="PID.13/XTN.1" />		
			</PID.11>		
		</PID>
	</xsl:template>
	
	<xsl:template match="PV1">					
		<PV1>
			<!-- TODO - Pendiente de resolver: http://localhost/show_bug.cgi?id=7 -->
			<PV1.2>
				<xsl:call-template name="patient_class">
					<xsl:with-param name="dae_patient_class" select="PV1.2" />
				</xsl:call-template>
			</PV1.2>			
			<PV1.3>
				<!-- TODO - Pendiente de resolver: http://localhost/show_bug.cgi?id=8 -->
				<xsl:copy-of select="PV1.3/PL.1"/>
				<!-- TODO - Pendiente de resolver: http://localhost/show_bug.cgi?id=9 -->
				<PL.3>
					<xsl:call-template name="patient_bed" >
						<xsl:with-param name="room" select="PV1.3/PL.2"/>
						<xsl:with-param name="bed" select="PV1.3/PL.3"/>
					</xsl:call-template>
				</PL.3>
			</PV1.3>
			
		
		</PV1>
	
	</xsl:template>
		
</xsl:stylesheet>