package org.hyperledger.fabric.samples.movieticketsappcc;

import java.util.Date;

public class TicketInfo {

	private String ticketNumber;
	
	private String buyerName;
	
	private Integer numberOfTicketsBooked;
	
	private Integer randomNumberForSodaExchange;
	
	private boolean exchangedWithSoda;
	
	private Integer numberOfSodasExchanged;	// there is a possibility that partial number of Sodas available and allowed to exchange
	
	private Date showDate;

	public TicketInfo() {
	}

	public String getTicketNumber() {
		return ticketNumber;
	}

	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	public Integer getNumberOfTicketsBooked() {
		return numberOfTicketsBooked;
	}

	public void setNumberOfTicketsBooked(Integer numberOfTicketsBooked) {
		this.numberOfTicketsBooked = numberOfTicketsBooked;
	}

	public Integer getRandomNumberForSodaExchange() {
		return randomNumberForSodaExchange;
	}

	public void setRandomNumberForSodaExchange(Integer randomNumberForSodaExchange) {
		this.randomNumberForSodaExchange = randomNumberForSodaExchange;
	}

	public boolean isExchangedWithSoda() {
		return exchangedWithSoda;
	}

	public void setExchangedWithSoda(boolean exchangedWithSoda) {
		this.exchangedWithSoda = exchangedWithSoda;
	}
	
	public Date getShowDate() {
		return showDate;
	}

	public void setShowDate(Date showDate) {
		this.showDate = showDate;
	}

	public Integer getNumberOfSodasExchanged() {
		return numberOfSodasExchanged;
	}

	public void setNumberOfSodasExchanged(Integer numberOfSodasExchanged) {
		this.numberOfSodasExchanged = numberOfSodasExchanged;
	}
}
