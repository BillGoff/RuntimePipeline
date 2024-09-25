package com.snaplogic.runtime.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.snaplogic.runtime.dtos.SearchItem;

public interface SearchRepository extends MongoRepository<SearchItem, String>
{
	public Optional<SearchItem> findById(String id);
	
	public List<SearchItem> findByIdRegex(String regex);

}
