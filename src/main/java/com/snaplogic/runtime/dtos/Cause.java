package com.snaplogic.runtime.dtos;

public class Cause {
	
	private String message;
	private String reason;
	private String resolution;
	private String ruuid;
	
	public String getMessage() {
		return message;
	}
	public String getReason() {
		return reason;
	}
	public String getResolution() {
		return resolution;
	}
	public String getRuuid() {
		return ruuid;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public void setResolution(String resolution) {
		this.resolution = resolution;
	}
	public void setRuuid(String ruuid) {
		this.ruuid = ruuid;
	}
	
	public String toString()
	{
		return (toString(""));
	}
	
	public String toString(String tabs)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(tabs + "message: " + this.getMessage() + "\n");
		sb.append(tabs + "reason " + this.getReason() + "\n");
		sb.append(tabs + "resolution: " + this.getResolution() + "\n");
		sb.append(tabs + "ruuid: " + this.getRuuid() + "\n");
		return (sb.toString());
	}
}
