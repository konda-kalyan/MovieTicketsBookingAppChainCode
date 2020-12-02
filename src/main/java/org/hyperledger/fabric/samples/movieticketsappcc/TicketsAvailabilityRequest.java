package org.hyperledger.fabric.samples.movieticketsappcc;

import java.util.Date;

public class TicketsAvailabilityRequest {
	
	private String theatre;
	
	private String screen;
	
	private String show;
	
	private Date showDate;

	public TicketsAvailabilityRequest() {
	}

	public String getTheatre() {
		return theatre;
	}

	public void setTheatre(String theatre) {
		this.theatre = theatre;
	}

	public String getScreen() {
		return screen;
	}

	public void setScreen(String screen) {
		this.screen = screen;
	}

	public String getShow() {
		return show;
	}

	public void setShow(String show) {
		this.show = show;
	}
		
	public Date getShowDate() {
		return showDate;
	}

	public void setShowDate(Date showDate) {
		this.showDate = showDate;
	}
}
