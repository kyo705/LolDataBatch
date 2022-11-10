package com.quartz.timer;

public class Cronner {
	private String cronExpression;
	private String callbackData;
	
	public Cronner() {}
	
	public Cronner(String cronExpression, String callbackData) {
		this.cronExpression = cronExpression;
		this.callbackData = callbackData;
	}
	

	public String getCronExpression() {
		return cronExpression;
	}
	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}
	public String getCallbackData() {
		return callbackData;
	}
	public void setCallbackData(String callbackData) {
		this.callbackData = callbackData;
	}
}
