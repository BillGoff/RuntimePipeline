package com.snaplogic.runtime.dtos;


import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

public class PmPipelineRtItem {
	
	@Field("child_has_lints")
	private boolean childHasLints;
	
	@Field("class_fqid")
	private String classFqId;
	
	@Field("class_id")
	private String classId;
	
	@Field("create_time")
	private Date createTime;
	
	@Field("documents_count")
	private long documentsCount;

	private long duration;
	
	@Field("duration_ms")
	private long durationMs;
	
	@Field("error_documents_count")
	private long errorDocumentsCount;
	
	@Field("flow_map")
	private String flowMap;
	
	@Field("has_errors")
	private boolean hasErrors;
	
	@Field("has_lints")
	private boolean hasLints;
	
	@Field("has_warnings")
	private boolean hasWarnings;
	
	@Id
    private String id;
	
	@Field("instance_id")
	private String instanceId;
	
	@Field("invoker_name")
	private String invokerName;
	
	@Field("invoker_path_id")
	private String invokerPathId;
	
	@Field("invoker_snode_id")
	private String invokerSnodeId;
	
	@Field("kickoff_timestamp")
	private long kickoffTimestamp;
	
	private String label;
	
	private String mode;
	
	@Field("nested_pipeline")
	private boolean nestedPipeline;
	
	@Field("parent_pipeline_ruuid")
	private String parentPipelineRuuid;
	
	@Field("parent_ruuid")
	private String parentRuuid;
	
	//I don't know what a Snap Map is.  So I am ignoring it here.  It is an array of something.
	
	@Field("pipe_invoker")
	private String pipeInvoker;
	
	@Field("snaplex_label")
	private String snapplexLabel;
	
	private String state;
	
	@Field("state_log")
	private List<StateLog> stateLogs;
		
	@Field("user_id")
	private String userId;
	
	public String getClassFqId() {
		return classFqId;
	}

	public String getClassId() {
		return classId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public long getDocumentsCount() {
		return documentsCount;
	}

	public long getDuration() {
		return duration;
	}

	public long getDurationMs() {
		return durationMs;
	}

	public long getErrorDocumentsCount() {
		return errorDocumentsCount;
	}

	public String getFlowMap() {
		return flowMap;
	}

	public String getId() {
		return id;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public String getInvokerName() {
		return invokerName;
	}

	public String getInvokerPathId() {
		return invokerPathId;
	}

	public String getInvokerSnodeId() {
		return invokerSnodeId;
	}

	public long getKickoffTimestamp() {
		return kickoffTimestamp;
	}

	public String getLabel() {
		return label;
	}

	public String getMode() {
		return mode;
	}

	public String getParentPipelineRuuid() {
		return parentPipelineRuuid;
	}

	public String getParentRuuid() {
		return parentRuuid;
	}

	public String getPipeInvoker() {
		return pipeInvoker;
	}

	public String getSnapplexLabel() {
		return snapplexLabel;
	}

	public String getState() {
		return state;
	}

	public List<StateLog> getStateLogs() {
		return stateLogs;
	}

	public String getUserId() {
		return userId;
	}

	public boolean isChildHasLints() {
		return childHasLints;
	}

	public boolean isHasErrors() {
		return hasErrors;
	}

	public boolean isHasLints() {
		return hasLints;
	}

	public boolean isHasWarnings() {
		return hasWarnings;
	}

	public boolean isNestedPipeline() {
		return nestedPipeline;
	}

	public void setChildHasLints(boolean childHasLints) {
		this.childHasLints = childHasLints;
	}

	public void setClassFqId(String classFqId) {
		this.classFqId = classFqId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setDocumentsCount(long documentsCount) {
		this.documentsCount = documentsCount;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public void setDurationMs(long durationMs) {
		this.durationMs = durationMs;
	}

	public void setErrorDocumentsCount(long errorDocumentsCount) {
		this.errorDocumentsCount = errorDocumentsCount;
	}

	public void setFlowMap(String flowMap) {
		this.flowMap = flowMap;
	}

	public void setHasErrors(boolean hasErrors) {
		this.hasErrors = hasErrors;
	}

	public void setHasLints(boolean hasLints) {
		this.hasLints = hasLints;
	}

	public void setHasWarnings(boolean hasWarnings) {
		this.hasWarnings = hasWarnings;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public void setInvokerName(String invokerName) {
		this.invokerName = invokerName;
	}

	public void setInvokerPathId(String invokerPathId) {
		this.invokerPathId = invokerPathId;
	}

	public void setInvokerSnodeId(String invokerSnodeId) {
		this.invokerSnodeId = invokerSnodeId;
	}

	public void setKickoffTimestamp(long kickoffTimestamp) {
		this.kickoffTimestamp = kickoffTimestamp;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public void setNestedPipeline(boolean nestedPipeline) {
		this.nestedPipeline = nestedPipeline;
	}
	
	public void setParentPipelineRuuid(String parentPipelineRuuid) {
		this.parentPipelineRuuid = parentPipelineRuuid;
	}
	
	public void setParentRuuid(String parentRuuid) {
		this.parentRuuid = parentRuuid;
	}
	
	public void setPipeInvoker(String pipeInvoker) {
		this.pipeInvoker = pipeInvoker;
	}
	
	public void setSnapplexLabel(String snapplexLabel) {
		this.snapplexLabel = snapplexLabel;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	
	public void setStateLogs(List<StateLog> stateLogs) {
		this.stateLogs = stateLogs;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
}
