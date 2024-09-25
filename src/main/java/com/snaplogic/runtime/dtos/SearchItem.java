package com.snaplogic.runtime.dtos;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("search")
public class SearchItem {
	
	@Id
	private String id;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public SearchItem(String id)
	{
		super();
		this.id = id;
	}

}
