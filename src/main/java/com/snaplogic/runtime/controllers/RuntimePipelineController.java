package com.snaplogic.runtime.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.snaplogic.runtime.dtos.PmPipelineRtItem;
import com.snaplogic.runtime.dtos.SearchItem;
import com.snaplogic.runtime.repositories.SearchRepository;
import com.snaplogic.runtime.utils.DateUtils;


/**
 * Controller used to show the Runtime Pipelines dash board queries.
 * @author bgoff
 * @since 23 Sep 2024
 */
@RestController
public class RuntimePipelineController {

	private static final Logger logger = LogManager.getLogger(RuntimePipelineController.class);
	
    private ExecutorService executorService = Executors.newFixedThreadPool(10); // Create a thread pool with 10 threads

	
	@Autowired
	private MongoOperations mongoOperations;
	
	@Autowired
	private SearchRepository searchRepo;
	//private PmPipelineRtRepository runtimeRepo;

	/**
	 * (U) This method is used to build the projection used by the queries.
	 * @return ProjectionOperation Object used as the projection of the queries.
	 */
	private ProjectionOperation buildRuntimeProjection()
	{
		ProjectionOperation projection = Aggregation.project("flow_map", "state_log", "duration_ms", 
        		"kickoff_timestamp", "instance_id", "label", "user_id", "snaplex_label", "state", "create_time", 
        		"duration", "documents_count", "error_documents_count", "parent_ruuid", "nested_pipeline", 
        		"has_lints", "child_has_lints", "class_id", "class_fqid", "pipe_invoker", "invoker_name", 
        		"invoker_path_id", "invoker_snode_id", "mode", "has_errors", "has_warnings", "snap_map", 
        		"parent_pipeline_ruuid");
		return projection;
	}
	
	/**
	 * This method is used to build the date range criteria.
	 * @return Criteria, the date range criteria
	 */
	private Criteria buildDateRangeCriteria()
	{
		//Create a date to 23 Sept 2023.
		LocalDate ldStart = LocalDate.of(2023, 9, 23);
		//Create a date to 24 Sept 2024.
		LocalDate ldEnd = LocalDate.of(2024, 9, 24);
		
		Criteria dateRangeCriteria = Criteria.where("create_time").gte(ldStart).lte(ldEnd);

		return dateRangeCriteria;
	}
	
	/**
	 * This method is used to run the two queries.  First query will do the regex against the search collection, and 
	 * return all values that match it.  The second query will do a concrete lookup with the orgSnodeId and those values,
	 * along with the default date.
	 * 
	 * @param orgSnodeId String value of the org snode id to use for the query.
	 * @param search String value to use for the regex query against the search collection.
	 * @param projectSnodes list of Strings that are the project_snode_ids to include in the query.
	 * @param states list of strings that are the states to include in the query.
	 * @return List of PmPipelineRtItems that match the queries.
	 * @throws Exception in the event either query fails.
	 */
	private List<PmPipelineRtItem> runImprovedDashbardQuery(String orgSnodeId, String search, 
			String projectSnodesString, String statesString) throws Exception
	{
		Date startDate = DateUtils.rightNowDate();
		boolean failed = false;

		List<PmPipelineRtItem> pipelineResults = new ArrayList<PmPipelineRtItem>();
		
		try
		{	
			List<Criteria> queryCriteria = new ArrayList<Criteria>();
			
			queryCriteria.add(Criteria.where("org_snode_id").is(orgSnodeId));
			queryCriteria.add(buildDateRangeCriteria());
			
			LimitOperation limitOp = new LimitOperation(100);
						
			if ((projectSnodesString != null) && (projectSnodesString.trim().length() > 0))
			{
				queryCriteria.add(Criteria.where("project_snode_id").in(Arrays.asList(projectSnodesString.split(","))));
			}
			if ((statesString != null) && (statesString.trim().length() > 0))
			{
				queryCriteria.add(Criteria.where("state").in(Arrays.asList(statesString.split(","))));
			}

			if ((search != null) && (search.length() > 0))
			{
				List<SearchItem> items = searchRepo.findByIdRegex(search);
		
				logger.debug("It took " + DateUtils.computeDiff(startDate,
						DateUtils.rightNowDate()) + " to do the regex lookup (<" + search + ">!");
			
				List<String> searchValues = new ArrayList<>();
	
				for(SearchItem item: items)
					searchValues.add(item.getId());

				queryCriteria.add(Criteria.where("search").in(searchValues));
			}
			
			MatchOperation match = new MatchOperation(new Criteria().andOperator(queryCriteria));
			
			Aggregation agg = Aggregation.newAggregation(match, buildRuntimeProjection(), limitOp);
			
			logger.info("Running Improved Query: " + agg.toString());
			
			AggregationResults<PmPipelineRtItem> results = mongoOperations.aggregate(agg, 
					"pm.pipeline_rt", PmPipelineRtItem.class);
									
			pipelineResults = results.getMappedResults();	
		}
		catch(Exception e)
		{
			failed = true;
			logger.error("Failed to run the new imporved query/index!", e);
			throw e;
		}
		finally
		{
			StringBuilder msg = new StringBuilder("It took " + DateUtils.computeDiff(startDate,
					DateUtils.rightNowDate()));
			if (failed)
				msg.append(" to fail to ");
			else
				msg.append(" to successfully ");
	
			
			msg.append("lookup RunTime using the new query/index.");
	
			logger.info(msg.toString());
		}
		return (pipelineResults);
	}
	
	/**
	 * This method shows how the current query with the reqex is working.  
	 * 
	 * @param orgSnodeId String value of the org snode id to use for the query.
	 * @param search String value that we will use the regex on.
	 * @param projectSnodesString a comma separated value of projectSnodeIds.
	 * @param statesString a comma separated value of states.
	 * @return List of PmPipelineRtItems that match the query.
	 * @throws Exception in the event the query fails.
	 */
	private List<PmPipelineRtItem> runRegexDashboardQuery(String orgSnodeId, String search, String projectSnodesString,
			String statesString) throws Exception
	{
		Date startDate = DateUtils.rightNowDate();
		boolean failed = false;

		List<PmPipelineRtItem> pipelineResults = new ArrayList<PmPipelineRtItem>();
		
		try
		{			
//			boolean exists = mongoOperations.collectionExists("pm.pipeline_rt");
//			System.out.println("exists: " + exists);
			
			List<Criteria> queryCriteria = new ArrayList<Criteria>();

			queryCriteria.add(Criteria.where("org_snode_id").is(orgSnodeId));
			queryCriteria.add(buildDateRangeCriteria());
			
			
			LimitOperation limitOp = new LimitOperation(100);
			
			if ((projectSnodesString != null) && (!projectSnodesString.isEmpty()))
				queryCriteria.add(Criteria.where("project_snode_id").in(projectSnodesString));

			if ((statesString != null) && (!statesString.isEmpty()))
				queryCriteria.add(Criteria.where("state").in(statesString));
			
			if((search != null) && (search.length() > 0))
				queryCriteria.add(Criteria.where("search").regex(search));
			
			MatchOperation match = new MatchOperation(new Criteria().andOperator(queryCriteria));

			Aggregation agg = Aggregation.newAggregation(match, buildRuntimeProjection(), limitOp);
			
			logger.info("Orginal query: " + agg.toString());
			
			AggregationResults<PmPipelineRtItem> results = mongoOperations.aggregate(agg, 
					"pm.pipeline_rt", PmPipelineRtItem.class);
									
			pipelineResults = results.getMappedResults();
		}
		catch(Exception e)
		{
			logger.error("Failed to run the original query/index!", e);
			throw e;
		}
		finally
		{
			StringBuilder msg = new StringBuilder("It took " + DateUtils.computeDiff(startDate,
					DateUtils.rightNowDate()));
			if (failed)
				msg.append(" to fail to ");
			else
				msg.append(" to successfully ");

			msg.append("lookup RunTime using the original query.");

			logger.info(msg.toString());
		}
		return pipelineResults;
	}
	
	/**
	 *  This end point is used to run the dashboard query.
	 * 
	 * @param orgSnodeId String value of the Org Snode Id to use in the query.
	 * @param projectSnodeIdes a comma separated value of projectSnodeIds.
	 * @param states a comma separated value of states.
	 * @param search String value of the regex to apply to the search part of the query.
	 * @return String the json of the results returned.
	 */
	@GetMapping(path = "/runtime/dashboard", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<PmPipelineRtItem> runtimeDashboard(@RequestParam("orgSnodeId") String orgSnodeId, 
			@RequestParam(required = false, name = "projectSnodeIds") String projectSnodeIds,
			@RequestParam(required = false, name = "states") String statesString,
			@RequestParam(required = false, name = "search") String search)
	{
		logger.debug("Attempting to lookup orgSnodeId: <" + orgSnodeId + ">");
		
		List<PmPipelineRtItem> results1 = new ArrayList<PmPipelineRtItem>();
		try
		{
			executorService.submit(() -> {
				try {
					List<PmPipelineRtItem> results = runRegexDashboardQuery(orgSnodeId, search, 
							projectSnodeIds, statesString);
					logger.debug("results size: " + results.size());
				}
				catch(Exception e)
				{
					logger.error("Original regex Dashboard query failed!", e);
				}
			});
			
			results1 = runImprovedDashbardQuery(orgSnodeId, search, projectSnodeIds, statesString);
			
			logger.debug("results1 size: " + results1.size());
		}
		catch(Exception e)
		{
			logger.error("Unable to run dashboard lookup!:", e);
		}
		return (results1);
	}
	
	/**
	 * Example:  52b32bdbf17d831ac1750257_7d9320c5-c6e8-4cb3-88a6-b10a24a2c1a4
	 * @param id String value of the document "_id"  we are search for.
	 * @return 
	 */
	@GetMapping(path = "/runtime/findById", produces = MediaType.APPLICATION_JSON_VALUE)
	public PmPipelineRtItem runtimeFindById(@RequestParam("id") String id)
	{
		logger.info("Attempting to find a runtime by its Id <" + id + ">");
		
		Date startDate = DateUtils.rightNowDate();
		boolean failed = false;

		PmPipelineRtItem result = null;
		try
		{			
//			boolean exists = mongoOperations.collectionExists("pm.pipeline_rt");
//			System.out.println("exists: " + exists);

			startDate = DateUtils.rightNowDate();
			
			MatchOperation match = new MatchOperation(Criteria.where("_id").is(id));
	        
			Aggregation agg = Aggregation.newAggregation(match, buildRuntimeProjection());
			
			AggregationResults<PmPipelineRtItem> results = mongoOperations.aggregate(agg, 
					"pm.pipeline_rt", PmPipelineRtItem.class);
			
			List<PmPipelineRtItem> pipelineResults = results.getMappedResults();
			
			for(PmPipelineRtItem item: pipelineResults)
				result = item;
		}
		catch(Exception e)
		{
			failed = true;
			logger.error("Unable to lookup a runtime by its Id <" + id + ">", e);
		}
		finally
		{
			StringBuilder msg = new StringBuilder("It took " + DateUtils.computeDiff(startDate,
					DateUtils.rightNowDate()));
			if (failed)
				msg.append(" to fail to ");
			else
				msg.append(" to successfully ");
			
			msg.append("lookup RunTime by its Id");
			
			logger.info(msg);
		}
		return (result);
	}
	
	/**
	 * This method does a lookup on the search collection for the id of the value we want.  This is NOT a regex query.
	 * 
	 * @param id String value of the record we want.
	 * @return SearchItem that is the value of the record we were searching for.
	 */
	@GetMapping(path = "/search/findById", produces = MediaType.APPLICATION_JSON_VALUE)
	public SearchItem searchFindById(@RequestParam("id") String id)
	{
		logger.info("Attempting to lookup <" + id + ">");
		
		Date startDate = DateUtils.rightNowDate();
		boolean failed = false;
		SearchItem searchItem = null;
		try
		{
			Optional<SearchItem> sio = searchRepo.findById(id);
			searchItem = sio.get();
			logger.debug("Found " + searchItem.getId());
		}
		catch(Exception e)
		{
			failed = true;
			logger.error("Unable to lookup a serach by its Id <" + id + ">", e);
		}
		finally
		{
			StringBuilder msg = new StringBuilder("It took " + DateUtils.computeDiff(startDate,
					DateUtils.rightNowDate()));
			if (failed)
				msg.append(" to fail to ");
			else
				msg.append(" to successfully ");

			
			msg.append("lookup Search by its Id");

			logger.info(msg);
		}
		return (searchItem);
	}
	
	/**
	 * This method applies the regex query to the String of the ID passed in.
	 * 
	 * @param id String value to do the regex query on.
	 * @return List of SearchItems that match the regex (id) passed in.
	 */
	@GetMapping(path = "/search/findByIdRegex", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<SearchItem> searchFindByIdRegex(@RequestParam("id") String id)
	{	
		logger.info("Attempting to lookup search via regex <" + id + ">");
		
		Date startDate = DateUtils.rightNowDate();
		boolean failed = false;

		List<SearchItem> items = new ArrayList<SearchItem>();
		
		try
		{
			items = searchRepo.findByIdRegex(id);
			logger.debug("Found " + items.size());
		}
		catch(Exception e)
		{
			failed = true;
			logger.error("Unable to lookup a serach via regex <" + id + ">", e);			
		}
		finally
		{
			StringBuilder msg = new StringBuilder("It took " + DateUtils.computeDiff(startDate,
					DateUtils.rightNowDate()));
			if (failed)
				msg.append(" to fail to ");
			else
				msg.append(" to successfully ");

			
			msg.append("query regex on id!");

			logger.info(msg);
		}
		return (items);
	}
}
