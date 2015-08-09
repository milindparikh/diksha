/*
 * Copyright (C) 2015 MILIND PARIKH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package org.diksha.common.dyutils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.diksha.common.utils.SchedulerWorkflowClientExternal;
import org.diksha.common.utils.SchedulerWorkflowClientExternalFactory;
import org.diksha.common.utils.SchedulerWorkflowClientExternalFactoryImpl;
import org.diksha.common.utils.SchedulerWorkflowState;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowClient;
import com.amazonaws.services.simpleworkflow.model.CountOpenWorkflowExecutionsRequest;
import com.amazonaws.services.simpleworkflow.model.ExecutionTimeFilter;
import com.amazonaws.services.simpleworkflow.model.GetWorkflowExecutionHistoryRequest;
import com.amazonaws.services.simpleworkflow.model.History;
import com.amazonaws.services.simpleworkflow.model.HistoryEvent;
import com.amazonaws.services.simpleworkflow.model.ListClosedWorkflowExecutionsRequest;
import com.amazonaws.services.simpleworkflow.model.ListOpenWorkflowExecutionsRequest;
import com.amazonaws.services.simpleworkflow.model.WorkflowExecution;
import com.amazonaws.services.simpleworkflow.model.WorkflowExecutionCount;
import com.amazonaws.services.simpleworkflow.model.WorkflowExecutionFilter;
import com.amazonaws.services.simpleworkflow.model.WorkflowExecutionInfo;
import com.amazonaws.services.simpleworkflow.model.WorkflowExecutionInfos;




@DynamoDBTable(tableName = "SchedulerUDE")
public class SchedulerUDE {
	private String executionId;
	private String jobName;
	private String functionType;
	private String functionName;
	private String functionContext;
    private String cronExpression;
    private int repeatTimes;
    private String timeZone;
    
    private Date startTimeDate = null;
    private Date endTimeDate = null ;
    
    
  
    
    public SchedulerUDE() {
    
    }
	public SchedulerUDE(String executionId, SchedulerUDF schedulerUDF, SchedulerUDJ schedulerUDJ ) {
		this.executionId = executionId;
		this.functionType = schedulerUDF.getFunctionType();
		this.functionName = schedulerUDF.getFunctionName();
		this.functionContext = schedulerUDJ.getFunctionContext();
		this.cronExpression = schedulerUDJ.getCronExpression();
		this.repeatTimes = schedulerUDJ.getRepeatTimes();
		this.startTimeDate = schedulerUDJ.getStartTimeDate();
		this.endTimeDate = schedulerUDJ.getEndTimeDate();
		this.timeZone = schedulerUDJ.getTimeZone();
		this.jobName = schedulerUDJ.getJobName();
	     
	    	
	}
	@DynamoDBHashKey	
	public String getExecutionId () {
		return executionId;
	}
	
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public void setExecutionId(String executionId) {
		this.executionId = executionId;
	}
	
	
	 public String getFunctionType() {
			return functionType;
	 }
		    
	 public void setFunctionType(String functionType) {
		 this.functionType = functionType;
	 }

	 public String getFunctionName() {
		 return functionName;
	 }
		    
	 public void setFunctionName(String functionName) {
		 this.functionName = functionName;
	 }
	 
	    public String getFunctionContext() {
	    	return functionContext;
	        	}
	    public void setFunctionContext(String functionContext) {
	    	this.functionContext = functionContext;
	    }

	    public String getCronExpression() {
	    	return cronExpression;
	    }
	    public void setCronExpression(String cronExpression) {
	    	this.cronExpression = cronExpression;
	    }
	        
	    public int getRepeatTimes() {
	    	return repeatTimes;
	    }
	    public void setRepeatTimes(int repeatTimes) {
	    	this.repeatTimes = repeatTimes;
	    }


	    public Date getStartTimeDate() {
	    	return startTimeDate;
	    }
	    public void setStartTimeDate(Date startTimeDate) {
	    	this.startTimeDate = startTimeDate;
	    }


	    public Date getEndTimeDate() {
	    	return endTimeDate;
	    }
	    public void setEndTimeDate(Date endTimeDate) {
	    	this.endTimeDate = endTimeDate;
	    }

	    public String getTimeZone() {
	    		return timeZone;
	    }
	
	    public void setTimeZone(String timeZone) {
	    	this.timeZone = timeZone;
	    }
	    
	    public void cancelExecution(String clientId, String reason, SchedulerConfig schedulerConfig) {
	    	  SchedulerWorkflowClientExternal scheduler = getScheduler(clientId, schedulerConfig);
			   scheduler.cancelWorkflowExecution(reason);
			   
	    	
	    }
	    public void listStatus(String clientId, SchedulerConfig schedulerConfig) {
	    	
	    	
	    	
		  
			DynamoDBMapper mapper = DyDBUtils.getDynamoDBMapper();
			SchedulerWorkflowState schedulerWorkflowState = mapper.load(SchedulerWorkflowState.class, clientId);
		
	       
	       
	     	
	     	System.out.println("clientID : " + clientId);
	     	System.out.println("     Launch Parameters");
	     	System.out.println("           Function: (" + this.functionName + ") with context = " + this.functionContext);
	     	System.out.println("                 CronExpression  : " + this.cronExpression);
	     	System.out.println("                 RepeatTimes     : " + this.repeatTimes);
	     	System.out.println("                 StartTimeDate   : " + this.startTimeDate);
	     	System.out.println("                 EndTimeDate     : " + this.endTimeDate);
	     	System.out.println("      Current State");
	     	
	     	int cnt = schedulerWorkflowState.loopCount - 1;
	     	
	     	System.out.println("            status of loop       : " + schedulerWorkflowState.getLoopState());
	     	
	     	System.out.println("            # of times executed  : " + cnt);
	     	
	     	System.out.println("            Last Executed @      : " + schedulerWorkflowState.lastExecutionTimeDate);
	     	System.out.println("            Next Proposed Time @ : " + schedulerWorkflowState.lastProposedTimeDate);
	     	

	    	
	    	AmazonSimpleWorkflowClient amazonSimpleWorkflowClient = new AmazonSimpleWorkflowClient(DyDBUtils.getAwsCredentials());
	    	amazonSimpleWorkflowClient.setEndpoint(schedulerConfig.getEndPoint());
	    	
	    	
	    	
	    	
	    	ListOpenWorkflowExecutionsRequest listOpenWorkflowExecutionsRequest = 
	    			new ListOpenWorkflowExecutionsRequest()
	    				.withDomain(schedulerConfig.getDomain())
	    				.withExecutionFilter(new WorkflowExecutionFilter().withWorkflowId(clientId))
	    				.withReverseOrder(new Boolean("true"))
	    				.withStartTimeFilter(new ExecutionTimeFilter().withOldestDate(new Date(0L)))
	    			;
	    	
	    	
	    	WorkflowExecutionInfos openWorkflowExecutionInfos = amazonSimpleWorkflowClient.listOpenWorkflowExecutions(listOpenWorkflowExecutionsRequest);
	    	
	    	List<WorkflowExecutionInfo> listOpenWorkflowExecutionInfo =  openWorkflowExecutionInfos.getExecutionInfos();
	    	
	    	
	    	
	    	
	    	
	    	
	    	
	    	
	    	ListClosedWorkflowExecutionsRequest listClosedWorkflowExecutionsRequest = 
	    			new ListClosedWorkflowExecutionsRequest()
	    				.withDomain(schedulerConfig.getDomain())
	    				.withExecutionFilter(new WorkflowExecutionFilter().withWorkflowId(clientId))
	    				.withReverseOrder(new Boolean("true"))
	    				.withStartTimeFilter(new ExecutionTimeFilter().withOldestDate(new Date(0L)))
	    			;
	    	
	    	
	    	WorkflowExecutionInfos closedWorkflowExecutionInfos = amazonSimpleWorkflowClient.listClosedWorkflowExecutions(listClosedWorkflowExecutionsRequest);
	    	
	    	List<WorkflowExecutionInfo> listClosedWorkflowExecutionInfo =  closedWorkflowExecutionInfos.getExecutionInfos();
	    	
	    	
	    	
	    	listActivitiesInWEI(schedulerConfig,listOpenWorkflowExecutionInfo );
	    	listActivitiesInWEI(schedulerConfig,listClosedWorkflowExecutionInfo );
	    	
			System.out.println("\n");
	    	
	    	
	    	
	    	
	    	
	   }
	    
	    public static void listActivitiesInWEI(SchedulerConfig schedulerConfig, List<WorkflowExecutionInfo> listWorkflowExecutionInfo) {
	    	
	    	AmazonSimpleWorkflowClient amazonSimpleWorkflowClient = new AmazonSimpleWorkflowClient(DyDBUtils.getAwsCredentials());
	    	amazonSimpleWorkflowClient.setEndpoint(schedulerConfig.getEndPoint());
	    	
	    	
	    	
	    	

	    	for (int cnt2 = 0; cnt2 < listWorkflowExecutionInfo.size(); cnt2++) {
	    		
	    			WorkflowExecutionInfo workflowExecutionInfo = listWorkflowExecutionInfo.get(cnt2);
	    			WorkflowExecution workflowExecution = workflowExecutionInfo.getExecution();
	//    			System.out.println(workflowExecution);


	    			GetWorkflowExecutionHistoryRequest getWorkflowExecutionHistoryRequest = 
	    					new GetWorkflowExecutionHistoryRequest()
		    					.withDomain(schedulerConfig.getDomain())
		    					.withExecution(workflowExecution);
		    	
	    			History history = 
	    					amazonSimpleWorkflowClient.getWorkflowExecutionHistory(getWorkflowExecutionHistoryRequest);
		    			
	    			List<HistoryEvent> historyEvents ;
	    			String nextPageToken;
		    	
		    		do {
	    				historyEvents = history.getEvents();
	    				for (int cnt3 = 0; cnt3 < historyEvents.size(); cnt3++) {
		    			
		    			
		    			// , , , , , , 
		    			
	    					HistoryEvent he = historyEvents.get(cnt3);
	    					String eventType = historyEvents.get(cnt3).getEventType();
	    					if (eventType.equals("ActivityTaskStarted") 
	    							|| eventType.equals("ActivityTaskCompleted")
	    							|| eventType.equals("ActivityTaskFailed")
	    							|| eventType.equals("ActivityTaskTimedOut")
	    							|| eventType.equals("ActivityTaskCanceled")
	    							|| eventType.equals("ActivityTaskCancelRequested")

	    						) {
	    						
	    						if (he.getEventType().equals("ActivityTaskCompleted"))
	    							System.out.format("\n         %30s       %20s      ", he.getEventType(), he.getEventTimestamp());
	    						
	    						
	    					}
	    				}
	    				nextPageToken = history.getNextPageToken();
	    				history.setNextPageToken(nextPageToken);
	    			} while (nextPageToken != null);   	    			
	    		
	    		
	    	}	     	
	 	   

	    	
	    	
	    	
	    }
	    
	    
	    public static int countOpenExecutionsFromSWF(String config) {
	    
	    	SchedulerConfig schedulerConfig = DyDBUtils.getSchedulerConfig(config);
	    	
	    	AmazonSimpleWorkflowClient amazonSimpleWorkflowClient = new AmazonSimpleWorkflowClient(DyDBUtils.getAwsCredentials());
	    	amazonSimpleWorkflowClient.setEndpoint(schedulerConfig.getEndPoint());
	    	
	    	CountOpenWorkflowExecutionsRequest countOpenWorkflowExecutionsRequest =
	    			new CountOpenWorkflowExecutionsRequest()
	    				.withDomain(schedulerConfig.getDomain()    								
	    						);
	    	countOpenWorkflowExecutionsRequest.setStartTimeFilter(new ExecutionTimeFilter().withOldestDate(new Date(0L)));    	
	    	WorkflowExecutionCount workflowExecutionCount  = amazonSimpleWorkflowClient.countOpenWorkflowExecutions(countOpenWorkflowExecutionsRequest);

	    	return workflowExecutionCount.getCount();
	    }
	    
	    public static ArrayList<String> listOpenExecutionsFromSWF(String config) {
			ArrayList<String> retValue = new ArrayList<String> ();
			

	    	SchedulerConfig schedulerConfig = DyDBUtils.getSchedulerConfig(config);
	    	
	    	AmazonSimpleWorkflowClient amazonSimpleWorkflowClient = new AmazonSimpleWorkflowClient(DyDBUtils.getAwsCredentials());
	    	amazonSimpleWorkflowClient.setEndpoint(schedulerConfig.getEndPoint());

	    	
	    	ListOpenWorkflowExecutionsRequest listOpenWorkflowExecutionsRequest = 
	    			new ListOpenWorkflowExecutionsRequest()
	    			.withDomain(schedulerConfig.getDomain())
	    			.withStartTimeFilter(new ExecutionTimeFilter().withOldestDate(new Date(0L)));
	    	
	    	WorkflowExecutionInfos workflowExecutionInfos = amazonSimpleWorkflowClient.listOpenWorkflowExecutions(listOpenWorkflowExecutionsRequest);
	    	
	    	String nextPageToken;
	    	
	    	do {
	    		java.util.List<WorkflowExecutionInfo> listWorkflowExecutionInfos = workflowExecutionInfos.getExecutionInfos();
	    		for (int cnt = 0; cnt < listWorkflowExecutionInfos.size(); cnt++) {
	    			retValue.add(listWorkflowExecutionInfos.get(cnt).getExecution().getWorkflowId());
	    		}
	    		
	    		nextPageToken = workflowExecutionInfos.getNextPageToken();
	    	} while (nextPageToken != null);
	    	
			

			return  retValue;
		}
	    
	    
	    
	    public static SchedulerWorkflowClientExternal getScheduler(String clientId, SchedulerConfig schedulerConfig) {
	    	AWSCredentials awsCredentials = DyDBUtils.getAwsCredentials();
			 
			 
			 ClientConfiguration config = new ClientConfiguration().withSocketTimeout(Integer.parseInt(schedulerConfig.getSocketTimeout()));
		     AmazonSimpleWorkflow service = new AmazonSimpleWorkflowClient(awsCredentials, config);
		     
		     
		     service.setEndpoint(schedulerConfig.getEndPoint());
		     String domain = schedulerConfig.getDomain();

		     SchedulerWorkflowClientExternalFactory factory = new SchedulerWorkflowClientExternalFactoryImpl(service, domain);
		   
		     SchedulerWorkflowClientExternal scheduler = factory.getClient(clientId);
		     
		     return scheduler;
	    }
	    public void execute (String clientId, SchedulerConfig schedulerConfig) {
	    	
	    	SchedulerWorkflowClientExternal scheduler = getScheduler(clientId, schedulerConfig);
	    		    		
		     scheduler.invoke(  
						new SchedulerWorkflowState(
													this.jobName,
													this.cronExpression, 
													this.repeatTimes, 
													this.startTimeDate,
													this.endTimeDate,
							   						this.timeZone,
							   						clientId,
							   						new Date()),
						this.functionType,
						this.functionName,
						this.functionContext
						
						);
				
		     System.out.println(clientId);
		     
							
	    	
	    	
	    }
	    
	    
	    
}
