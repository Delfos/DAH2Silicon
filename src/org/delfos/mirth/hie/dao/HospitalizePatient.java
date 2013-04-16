package org.delfos.mirth.hie.dao;

import java.util.Date;

public class HospitalizePatient {

	private int icu;
	private Date startHosp;
	private Date endHosp;
	
	public int getIcu() {
		return icu;
	}
	public void setIcu(int icu) {
		this.icu = icu;
	}
	public Date getStartHosp() {
		return startHosp;
	}
	public void setStartHosp(Date startHosp) {
		this.startHosp = startHosp;
	}
	public Date getEndHosp() {
		return endHosp;
	}
	public void setEndHosp(Date endHosp) {
		this.endHosp = endHosp;
	}	
	
}
