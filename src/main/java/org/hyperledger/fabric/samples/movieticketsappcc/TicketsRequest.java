package org.hyperledger.fabric.samples.movieticketsappcc;

import java.util.Date;

public class TicketsRequest {
	
	private String theatre;
	
	private String screen;
	
	private String show;
	
	private String buyerName;
	
	private Integer numberOfTicketsBuying;
	
	private Date showDate;

	public TicketsRequest() {
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

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	public Integer getNumberOfTicketsBuying() {
		return numberOfTicketsBuying;
	}

	public void setNumberOfTicketsBuying(Integer numberOfTicketsBuying) {
		this.numberOfTicketsBuying = numberOfTicketsBuying;
	}

	public Date getShowDate() {
		return showDate;
	}

	public void setShowDate(Date showDate) {
		this.showDate = showDate;
	}
}
