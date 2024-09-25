package com.snaplogic.runtime.dtos;

import org.springframework.data.mongodb.core.mapping.Field;

public class StateLog {

	private Cause cause;
	
	@Field ("new_state")
	private String newState;

	private Long timestamp;

	public Cause getCause() {
		return cause;
	}

	public String getNewState() {
		return newState;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setCause(Cause cause) {
		this.cause = cause;
	}

	public void setNewState(String newState) {
		this.newState = newState;
	}
	
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	
}
