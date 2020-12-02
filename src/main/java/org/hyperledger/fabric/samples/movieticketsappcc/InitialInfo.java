package org.hyperledger.fabric.samples.movieticketsappcc;

public class InitialInfo {

	private Integer theatresCount;
	
	private Integer screensCount;
	
	private Integer showsCount;
	
	private Integer maxTicketPerShow;
	
	private Integer maxSodasAvailable;

	public InitialInfo() {
	}

	public Integer getTheatresCount() {
		return theatresCount;
	}

	public void setTheatresCount(Integer theatresCount) {
		this.theatresCount = theatresCount;
	}

	public Integer getScreensCount() {
		return screensCount;
	}

	public void setScreensCount(Integer screensCount) {
		this.screensCount = screensCount;
	}

	public Integer getShowsCount() {
		return showsCount;
	}

	public void setShowsCount(Integer showsCount) {
		this.showsCount = showsCount;
	}

	public Integer getMaxTicketPerShow() {
		return maxTicketPerShow;
	}

	public void setMaxTicketPerShow(Integer maxTicketPerShow) {
		this.maxTicketPerShow = maxTicketPerShow;
	}

	public Integer getMaxSodasAvailable() {
		return maxSodasAvailable;
	}

	public void setMaxSodasAvailable(Integer maxSodasAvailable) {
		this.maxSodasAvailable = maxSodasAvailable;
	}
}
