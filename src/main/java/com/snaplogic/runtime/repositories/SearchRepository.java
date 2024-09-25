package com.snaplogic.runtime.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReadPreference;

import com.snaplogic.runtime.dtos.SearchItem;

/**
 * This interface defines the connection to the Our MongoDB's "search" collection.
 * @author bgoff
 * @since 25 Sep 2024
 */
public interface SearchRepository extends MongoRepository<SearchItem, String>
{
	@ReadPreference("secondaryPreferred")
	public Optional<SearchItem> findById(String id);

	@ReadPreference("secondaryPreferred")
	public List<SearchItem> findByIdRegex(String regex);

}
