package org.hyperledger.fabric.samples.movieticketsappcc;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.apache.commons.lang3.time.DateUtils;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contact;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Default;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.annotation.License;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ChaincodeStub;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Java implementation of the MovieTicketsBookingsApp Contract 
 */
@Contract(
        name = "movieticketsapp",
        info = @Info(
                title = "MovieTicketsBookingApp contract",
                description = "The MovieTicketsApp contract",
                version = "1.0",
                license = @License(
                        name = "Apache 2.0 License",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"),
                contact = @Contact(
                        email = "konda.kalyan@gmail.com",
                        name = "MovieTicketsApp",
                        url = "https://github.com/konda-kalyan")))

@Default
public final class MovieTicketsAppCC implements ContractInterface {
	
	@Transaction()
    public void init(final Context ctx, String theatresCount, String screensCount, String showsCount, String maxTicketPerShow, String maxSodasAvailable) throws Exception {
		
		InitialInfo initialInfo = new InitialInfo();
		
		initialInfo.setTheatresCount(Integer.parseInt(theatresCount));
		initialInfo.setScreensCount(Integer.parseInt(screensCount));
		initialInfo.setShowsCount(Integer.parseInt(showsCount));
		initialInfo.setMaxTicketPerShow(Integer.parseInt(maxTicketPerShow));
		initialInfo.setMaxSodasAvailable(Integer.parseInt(maxSodasAvailable));
		
		// Store this initial info in ledger
		ChaincodeStub stub = ctx.getStub();
		ObjectMapper mapper = new ObjectMapper();
		String initialInfoString = mapper.writeValueAsString(initialInfo);
		stub.putStringState("initialInfo", initialInfoString);
		
		// Slso put sodas available for today
		stub.putStringState("sodasAvailableForToday", maxSodasAvailable);
	}
	
	
	@Transaction()
    public String IssueTickets(final Context ctx, String ticketsRequestString) throws Exception {
    	
		// In the interest of time, I am avoiding some sanity checks on input parameters validation.
		// Assuming that we got right set of inputs
		
    	ChaincodeStub stub = ctx.getStub();
    	
    	ObjectMapper mapper = new ObjectMapper();
    	TicketsRequest ticketsRequest = mapper.readValue(ticketsRequestString, TicketsRequest.class);
    	
    	Integer ticketAvailable = 0;
    	Integer numberOfTicketsRequested = ticketsRequest.getNumberOfTicketsBuying();

    	/*
    	 * Tickets can be booked for future date as well.
    	 * But, do remember that following two actions are allowed only on show date only
    	 * 1. Grabbing Water bottle and Popcorn
    	 * 2. Exchange Water bottle with Soda 
    	 */
    	
    	String showDate = new SimpleDateFormat("yyyyMMdd").format(ticketsRequest.getShowDate());
    	
    	// example key = theatre1screen4show2yyyyMMdd
    	String key = ticketsRequest.getTheatre() + ticketsRequest.getScreen() + ticketsRequest.getShow() + showDate;
    	String ticketAvailableString = stub.getStringState(key);
        
    	if (ticketAvailableString == null || ticketAvailableString.isEmpty()) {	// Means, this is the first tickets booking request we have got for this particular theatre, screen, show and date. So, available tickets = maxTicketPerShow
    		String initialInfoString = stub.getStringState("initialInfo");
    		InitialInfo initialInfo = mapper.readValue(initialInfoString, InitialInfo.class);
    		
        	ticketAvailable = initialInfo.getMaxTicketPerShow();
        	
        } else { // means, already some tickets were booked for this particular theatre, screen, show and date.
        	ticketAvailable = Integer.parseInt(ticketAvailableString);
        }
        
        TicketInfo ticketInfo = new TicketInfo();
        String ticketInfoString = null;
        
	    // Check whether sufficient number of tickets are available or not.
        // If available,
        // 		Book tickets and update remaining tickets info for this particular theatre, screen, show and date
        //		Generate tickets info, store it ledger and send the same to clients
        
	    if(numberOfTicketsRequested <= ticketAvailable) { // go ahead and book tickets
	    	////// Book tickets and update remaining tickets info for this particular theatre, screen, show and date
	    	Integer ticketsRemaining = ticketAvailable - numberOfTicketsRequested;
			stub.putStringState(key, Integer.toString(ticketsRemaining));
			
			////// Create Ticket info, Save into ledger and then to client  
	        
			// Now, generate UNIQUE ticket number which will be used as reference in later flows (issuing Water bottle, exchange with Soda)
	        String ticketNumber = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()); // using date in milliseconds so that we get unique number
	        ticketInfo.setTicketNumber(ticketNumber);
	        ticketInfo.setBuyerName(ticketsRequest.getBuyerName());
	        ticketInfo.setNumberOfTicketsBooked(numberOfTicketsRequested);
	        ticketInfo.setRandomNumberForSodaExchange(null);
	        ticketInfo.setExchangedWithSoda(false);
	        ticketInfo.setShowDate(ticketsRequest.getShowDate());
	        
	        ticketInfoString = mapper.writeValueAsString(ticketInfo);
	        stub.putStringState(ticketNumber, ticketInfoString);	// save ticket info in ledger
	        
		} else {	// sorry, requested number of tickets are not available
			// just pass empty ticketInfo
			ticketInfo.setBuyerName(ticketsRequest.getBuyerName());
			ticketInfoString = mapper.writeValueAsString(ticketInfo);
		}

        return ticketInfoString;
        
    } // IssueTickets transaction ends
	
	
	@Transaction()
    public Integer GetNoOfAvailableTickets(final Context ctx, String ticketsAvailabilityRequestString) throws Exception {
    	
		// In the interest of time, I am avoiding some sanity checks on input parameters validation. Assuming that we got right set of inputs
		
    	ChaincodeStub stub = ctx.getStub();
    	
    	ObjectMapper mapper = new ObjectMapper();
    	TicketsAvailabilityRequest ticketsAvailabilityRequest = mapper.readValue(ticketsAvailabilityRequestString, TicketsAvailabilityRequest.class);
    	
    	Integer ticketAvailable = 0;
    	
    	String showDate = new SimpleDateFormat("yyyyMMdd").format(ticketsAvailabilityRequest.getShowDate());
    	
    	// example key = theatre1screen4show2yyyyMMdd
    	String key = ticketsAvailabilityRequest.getTheatre() + ticketsAvailabilityRequest.getScreen() + ticketsAvailabilityRequest.getShow() + showDate;
    	
    	String ticketAvailableString = stub.getStringState(key);
        
    	if (ticketAvailableString == null || ticketAvailableString.isEmpty()) {	// Means, not even single ticket is booked for this particular one
    		String initialInfoString = stub.getStringState("initialInfo");
    		InitialInfo initialInfo = mapper.readValue(initialInfoString, InitialInfo.class);
    		
        	ticketAvailable = initialInfo.getMaxTicketPerShow();
        	
        } else { // means, already some tickets were booked for this particular theatre, screen, show and date.
        	ticketAvailable = Integer.parseInt(ticketAvailableString);
        }

        return ticketAvailable;
        
    } // GetNoOfAvailableTickets transaction ends
	
	
	@Transaction()
    public String GrabWaterBottleAndPopcorn(final Context ctx, String ticketNumber) throws Exception {
 		
    	ChaincodeStub stub = ctx.getStub();

    	TicketInfo ticketInfo = new TicketInfo();
    	ObjectMapper mapper = new ObjectMapper();
    	
    	String ticketInfoString = stub.getStringState(ticketNumber);
        
    	if (ticketInfoString == null || ticketInfoString.isEmpty()) { // means, invalid ticket number has been passed.
    		ticketInfoString = mapper.writeValueAsString(ticketInfo);
    		
    	} else {	// valid ticket. So, do the necessary things for next actions
    		ticketInfo = mapper.readValue(ticketInfoString, TicketInfo.class);
    		
    		// If ticket is not for today, then ask them to come on that day
    		if(DateUtils.isSameDay(Calendar.getInstance().getTime(), ticketInfo.getShowDate())) {

    			// Generate random number which will be used for Soda exchange
    			Random rand = new Random();
    			ticketInfo.setRandomNumberForSodaExchange(Math.abs(rand.nextInt()));
    			ticketInfoString = mapper.writeValueAsString(ticketInfo);
    	        stub.putStringState(ticketNumber, ticketInfoString);	// update ticket info in ledger
    		} else {
    			// no action is required.
    		}
    	}

        return ticketInfoString;
        
    } // GrabWaterBottleAndPopcorn transaction ends
	
	
	@Transaction()
    public String ExchangeWithSoda(final Context ctx, String ticketNumber) throws Exception {
 		
    	ChaincodeStub stub = ctx.getStub();
    	
    	TicketInfo ticketInfo = new TicketInfo();
    	ObjectMapper mapper = new ObjectMapper();
    	
    	String ticketInfoString = stub.getStringState(ticketNumber);
    	
    	if (ticketInfoString == null || ticketInfoString.isEmpty()) { // means, invalid ticket info has been passed.
    		ticketInfoString = mapper.writeValueAsString(ticketInfo);
    		
    		return ticketInfoString;
    	}
    	
    	// valid ticket. So, do the necessary checks and allow to exchange
    		
		ticketInfo = mapper.readValue(ticketInfoString, TicketInfo.class);
		
		// If ticket is not for today, then ask them to come on that particular day
		if(! DateUtils.isSameDay(Calendar.getInstance().getTime(), ticketInfo.getShowDate())) {
			return ticketInfoString;
		}
			
		// *** Whoever has random number with "even" value, they are only eligible for exchange.
		if(ticketInfo.getRandomNumberForSodaExchange() % 2 != 0) {
			return ticketInfoString;
		}
			
		String key = "sodasAvailableForToday";
		String sodasAvailableString = stub.getStringState(key);
		Integer sodasAvaiable = Integer.parseInt(sodasAvailableString);
    	Integer sodasRemaining = 0;
		
		Integer numberOfSodasToBeexchanged = ticketInfo.getNumberOfTicketsBooked();
		
		if(numberOfSodasToBeexchanged < sodasAvaiable) { // good to go for exchange
			sodasRemaining = sodasAvaiable - numberOfSodasToBeexchanged;
			ticketInfo.setNumberOfSodasExchanged(numberOfSodasToBeexchanged);
		} else if ((sodasAvaiable > 0) && (sodasAvaiable < numberOfSodasToBeexchanged)) { // Special case: Particla amount Sodas available than requested number. So, just give them whatever number of Sodas are available. For example, only 5 sodas are available, but number of booked tickets are 10. In that case, just exchange 5 Sodas.
			sodasRemaining = 0;
			ticketInfo.setNumberOfSodasExchanged(sodasAvaiable);
		} else {	// Sodas are not available
			return ticketInfoString;
		}
		
		ticketInfo.setExchangedWithSoda(true);
		
		// Update ledger with the remaining number of Sodas are available
		stub.putStringState(key, Integer.toString(sodasRemaining));
        
		// finally, update the ticket info as well
        ticketInfoString = mapper.writeValueAsString(ticketInfo);
        stub.putStringState(ticketNumber, ticketInfoString);	// update ticket info in ledger
	
	    return ticketInfoString;
        
    } // ExchangeWithSoda transaction ends
	
	
	@Transaction()
    public Integer GetNoOfAvailableSodas(final Context ctx) throws Exception {
    	
    	ChaincodeStub stub = ctx.getStub();
    	
		String sodasAvailableString = stub.getStringState("sodasAvailableForToday");
		
    	return Integer.parseInt(sodasAvailableString);
        
    } // GetNoOfAvailableTickets transaction ends
}
