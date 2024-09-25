package com.snaplogic.runtime.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.snaplogic.runtime.dtos.PmPipelineRtItem;

public interface PmPipelineRtRepository extends MongoRepository<PmPipelineRtItem, String> //, RuntimeRepositoryCustom
{
	public Optional<PmPipelineRtItem> findById(String id);
	
}
