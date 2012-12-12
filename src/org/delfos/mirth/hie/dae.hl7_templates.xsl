<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:xalan="http://xml.apache.org/xalan" exclude-result-prefixes="xalan">	
	
	<xsl:import href="utils.xsl"/>
	<xsl:import href="dae-silicon_bed_mapping.xsl"/>
	<xsl:import href="dae-silicon_services_mapping.xsl"/>
	
	<xsl:variable name="message_type" select="//MSH/MSH.9/MSG.2"/>
	
	<xsl:param name="silicon.bed.code" select="XXX" />
	<xsl:param name="silicon.old.bed.code" select="XXX" />
	<xsl:param name="silicon.nurse.unit" select="XXX"/>
	<xsl:param name="silicon.old.nurse.unit" select="XXX"/>
	<xsl:param name="silicon.service" select="XXX"/>	
	
	<xsl:template match = "MSH">
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
		<MSH.9>
			<xsl:copy-of select="MSH.9/MSG.1"/>
			<MSG.2>
				<xsl:call-template name="message_type">
					<xsl:with-param name="type" select="MSH.9/MSG.2"/>
				</xsl:call-template>
			</MSG.2>
		</MSH.9>		
		<xsl:copy-of select="MSH.10" />
	</xsl:template>
	
	<xsl:template match = "PID" >
		<!-- NHC -->
		<PID.2>
			<xsl:copy-of select="PID.3[2]/CX.1" />
		</PID.2>
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
		<PID.8>
			<xsl:call-template name="patient_sex">
				<xsl:with-param name="sex" select="PID.8"/>
			</xsl:call-template>
		</PID.8>
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
	</xsl:template>
	
	<xsl:template match = "ADT_A39.PATIENT/PID" >
		<!-- NHC -->
		<PID.2>
			<xsl:copy-of select="PID.3[2]/CX.1" />
		</PID.2>
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
		<PID.8>
			<xsl:call-template name="patient_sex">
				<xsl:with-param name="sex" select="PID.8"/>
			</xsl:call-template>
		</PID.8>
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
	</xsl:template>
	
	
	<xsl:template match="ADT_A39.PATIENT/MRG">
		<MRG.1>
			<xsl:copy-of select="MRG.1[1]/CX.1"/>
		</MRG.1>
	</xsl:template>
	
	<xsl:template match="PV1">					
		<!-- TODO - Pendiente de resolver: http://localhost/show_bug.cgi?id=7 -->
		<PV1.2>
			<xsl:call-template name="patient_class">
				<xsl:with-param name="dae_patient_class" select="PV1.2" />
			</xsl:call-template>
		</PV1.2>			
		<PV1.3>
			<!-- TODO - Pendiente de resolver: http://localhost/show_bug.cgi?id=8 -->
			<!-- <xsl:copy-of select="PV1.3/PL.1"/> -->
			<PL.1>
				<xsl:value-of select="$silicon.nurse.unit"/>
			</PL.1>
			<!-- TODO - Pendiente de resolver: http://localhost/show_bug.cgi?id=9 -->
			<PL.3>
				<xsl:value-of select="$silicon.bed.code"/>				
			</PL.3>
			<!-- 
			<PL.3>
				<xsl:call-template name="patient_bed" >
					<xsl:with-param name="bed" select="PV1.3/PL.3"/>
				</xsl:call-template>
			</PL.3>
			-->
			<PL.4>
				<HD.1>
					<xsl:call-template name="hospital" >
						<xsl:with-param name="hospital_code" select="PV1.3/PL.4/HD.1"></xsl:with-param>
					</xsl:call-template>
				</HD.1>
			</PL.4>
		</PV1.3>
		<PV1.6>
			<!-- TODO - Pendiente de resolver: http://localhost/show_bug.cgi?id=8 -->
			<PL.1>
				<xsl:value-of select="$silicon.old.nurse.unit"/>
			</PL.1>
			<!-- <xsl:copy-of select="PV1.6/PL.1"/> -->
			<PL.3>
				<xsl:value-of select="$silicon.old.bed.code"/>
				<!--  
				<xsl:call-template name="patient_bed" >
					<xsl:with-param name="bed" select="PV1.6/PL.3"/>
				</xsl:call-template>
				-->				
			</PL.3>
			<PL.4>
				<HD.1>
					<xsl:call-template name="hospital" />
				</HD.1>
			</PL.4>
		</PV1.6>			
		<PV1.7>
			<!-- TODO - mapear el valor enviado por DAE al esperado por Silicon -->
			<XCN.1>
				<xsl:call-template name="doctor_code">
					<xsl:with-param name="message_type" select="$message_type"/>
					<xsl:with-param name="code" select="PV1.7/XCN.1"/>
				</xsl:call-template>
			</XCN.1>
			<XCN.2>
				<FN.1>
					<xsl:call-template name="doctor_full_name">
						<xsl:with-param name="name" select="PV1.7/XCN.3"/>
						<xsl:with-param name="first_surname" select="PV1.7/XCN.2"/>
						<xsl:with-param name="second_surname" select="PV1.7/XCN.4"/>
					</xsl:call-template>
				</FN.1>
			</XCN.2>			
		</PV1.7>		
		<PV1.10>
			<xsl:value-of select="$silicon.service"/>
			<!--  
			<xsl:call-template name="service">
				<xsl:with-param name="service" select="PV1.10"/>
			</xsl:call-template>
			-->
		</PV1.10>
		<PV1.19>
			<xsl:copy-of select="PV1.19/CX.1"/>
		</PV1.19>
		<PV1.44>
			<xsl:copy-of select="PV1.44/TS.1"/>
		</PV1.44>
		<PV1.45>
			<xsl:copy-of select="PV1.45/TS.1"/>
		</PV1.45>		
	</xsl:template>
	
	<xsl:template match="DG1">		
		<DG1.3>
			<!-- TODO - mapear el código del diagnóstico al de Silicon -->
			<xsl:copy-of select="DG1.3/CE.1"/>
		</DG1.3>		
		<DG1.4>			
			<xsl:text><xsl:value-of select="DG1.3/CE.2"/></xsl:text>
			<xsl:text>. </xsl:text>
			<xsl:text><xsl:value-of select="DG1.4"/></xsl:text>			
		</DG1.4>		
	</xsl:template>
		
</xsl:stylesheet>