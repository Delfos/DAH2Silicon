<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!-- Mapeo provisional para las pruebas -->
	<xsl:template name="patient_bed">
		<xsl:param name="bed"/>	
		<xsl:variable name="substring-length" select="string-length($bed) - 1"></xsl:variable>
		
		<xsl:choose>
			<xsl:when test="substring($bed, $substring-length)='00'">
				<xsl:text>401-1</xsl:text>
			</xsl:when>
			<xsl:when test="substring($bed, $substring-length)='01'">
				<xsl:text>401-2</xsl:text>
			</xsl:when>
			<xsl:when test="substring($bed, $substring-length)='02'">
				<xsl:text>402-1</xsl:text>
			</xsl:when>
			<xsl:when test="substring($bed, $substring-length)='03'">
				<xsl:text>402-2</xsl:text>
			</xsl:when>
			<xsl:when test="substring($bed, $substring-length)='04'">
				<xsl:text>403-1</xsl:text>
			</xsl:when>			
			<xsl:when test="substring($bed, $substring-length)='05'">
				<xsl:text>403-2</xsl:text>
			</xsl:when>
			<xsl:when test="substring($bed, $substring-length)='06'">
				<xsl:text>404-1</xsl:text>
			</xsl:when>
			<xsl:when test="substring($bed, $substring-length)='07'">
				<xsl:text>404-2</xsl:text>
			</xsl:when>
			<xsl:when test="substring($bed, $substring-length)='08'">
				<xsl:text>405-1</xsl:text>
			</xsl:when>
			<xsl:when test="substring($bed, $substring-length)='09'">
				<xsl:text>405-2</xsl:text>
			</xsl:when>
			<xsl:when test="substring($bed, $substring-length)='10'">
				<xsl:text>406-1</xsl:text>
			</xsl:when>						
			<xsl:when test="substring($bed, $substring-length)='11'">
				<xsl:text>406-2</xsl:text>
			</xsl:when>
			<xsl:when test="substring($bed, $substring-length)='12'">
				<xsl:text>407-1</xsl:text>
			</xsl:when>		
			<xsl:when test="substring($bed, $substring-length)='13'">
				<xsl:text>407-2</xsl:text>
			</xsl:when>		
			<xsl:when test="substring($bed, $substring-length)='14'">
				<xsl:text>408-1</xsl:text>
			</xsl:when>
			<xsl:when test="substring($bed, $substring-length)='15'">
				<xsl:text>408-2</xsl:text>
			</xsl:when>			
			<xsl:when test="substring($bed, $substring-length)='16'">
				<xsl:text>409-1</xsl:text>
			</xsl:when>		
			<xsl:when test="substring($bed, $substring-length)='17'">
				<xsl:text>409-2</xsl:text>
			</xsl:when>
			<xsl:when test="substring($bed, $substring-length)='18'">
				<xsl:text>410-1</xsl:text>
			</xsl:when>		
			<xsl:when test="substring($bed, $substring-length)='19'">
				<xsl:text>410-2</xsl:text>
			</xsl:when>	
			<xsl:when test="substring($bed, $substring-length)='20'">
				<xsl:text>411-1</xsl:text>
			</xsl:when>
			<xsl:when test="substring($bed, $substring-length)='21'">
				<xsl:text>411-2</xsl:text>
			</xsl:when>		
			<xsl:when test="substring($bed, $substring-length)='22'">
				<xsl:text>412-1</xsl:text>
			</xsl:when>		
			<xsl:when test="substring($bed, $substring-length)='23'">
				<xsl:text>412-2</xsl:text>
			</xsl:when>			
			<xsl:when test="substring($bed, $substring-length)='24'">
				<xsl:text>413-1</xsl:text>
			</xsl:when>		
			<xsl:when test="substring($bed, $substring-length)='25'">
				<xsl:text>413-2</xsl:text>
			</xsl:when>		
			<xsl:when test="substring($bed, $substring-length)='26'">
				<xsl:text>414-1</xsl:text>
			</xsl:when>		
			<xsl:when test="substring($bed, $substring-length)='27'">
				<xsl:text>414-2</xsl:text>
			</xsl:when>			
			<xsl:when test="substring($bed, $substring-length)='28'">
				<xsl:text>415-1</xsl:text>
			</xsl:when>		
			<xsl:when test="substring($bed, $substring-length)='29'">
				<xsl:text>415-2</xsl:text>
			</xsl:when>			
			<xsl:when test="substring($bed, $substring-length)='30'">
				<xsl:text>416-1</xsl:text>
			</xsl:when>		
			<xsl:when test="substring($bed, $substring-length)='31'">
				<xsl:text>416-2</xsl:text>
			</xsl:when>			
			<xsl:when test="substring($bed, $substring-length)='32'">
				<xsl:text>417-1</xsl:text>
			</xsl:when>		
			<xsl:when test="substring($bed, $substring-length)='33'">
				<xsl:text>417-2</xsl:text>
			</xsl:when>			
			<xsl:when test="substring($bed, $substring-length)='34'">
				<xsl:text>418-1</xsl:text>
			</xsl:when>		
			<xsl:when test="substring($bed, $substring-length)='35'">
				<xsl:text>418-2</xsl:text>
			</xsl:when>			
			<xsl:when test="substring($bed, $substring-length)='36'">
				<xsl:text>419-1</xsl:text>
			</xsl:when>		
			<xsl:when test="substring($bed, $substring-length)='37'">
				<xsl:text>419-2</xsl:text>
			</xsl:when>			
			<xsl:when test="substring($bed, $substring-length)='38'">
				<xsl:text>421-1</xsl:text>
			</xsl:when>			
			<xsl:when test="substring($bed, $substring-length)='39'">
				<xsl:text>421-2</xsl:text>
			</xsl:when>			
			<xsl:when test="substring($bed, $substring-length)='40'">
				<xsl:text>423-1</xsl:text>
			</xsl:when>		
			<xsl:when test="substring($bed, $substring-length)='41'">
				<xsl:text>423-2</xsl:text>
			</xsl:when>	
			<xsl:when test="substring($bed, $substring-length)='42'">
				<xsl:text>425-1</xsl:text>
			</xsl:when>		
			<xsl:when test="substring($bed, $substring-length)='43'">
				<xsl:text>425-2</xsl:text>
			</xsl:when>	
			<xsl:when test="substring($bed, $substring-length)='44'">
				<xsl:text>427-1</xsl:text>
			</xsl:when>		
			<xsl:when test="substring($bed, $substring-length)='45'">
				<xsl:text>427-2</xsl:text>
			</xsl:when>
			<xsl:when test="substring($bed, $substring-length)='46'">
				<xsl:text>429-1</xsl:text>
			</xsl:when>		
			<xsl:when test="substring($bed, $substring-length)='47'">
				<xsl:text>429-2</xsl:text>
			</xsl:when>																																																																																																																																																																																																																																																																																										
			<xsl:otherwise>
				<xsl:text>401-X</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
		
	</xsl:template>
</xsl:stylesheet>