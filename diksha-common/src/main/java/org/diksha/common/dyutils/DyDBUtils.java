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
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.diksha.common.utils.ScheduleFunction;
import org.diksha.common.utils.SchedulerWorkflowState;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Index;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowClient;
import com.amazonaws.services.simpleworkflow.model.DeprecateDomainRequest;
import com.amazonaws.services.simpleworkflow.model.RegisterDomainRequest;

public class DyDBUtils {

	
	public static ArrayList<String> listOpenExecutionsFromDynamo(String config) {
		ArrayList<String> retValue = new ArrayList<String> ();
		

    	DynamoDB dynamoDB = getDynamoDB();
    	Table table = dynamoDB.getTable("SchedulerWorkflowState");
    	Index index = table.getIndex("loopStateIndex");

        ItemCollection<QueryOutcome> items = null;
        QuerySpec querySpec = new QuerySpec();


        querySpec.withKeyConditionExpression("loopState = :v_state")
        .withValueMap(new ValueMap()
        		.withString(":v_state","PROCESSING")
        		);

    	items = index.query(querySpec);
    

    	
    	Iterator<Item> iterator = items.iterator();
    	int totalActualCount = 0;
        Item item;
        
        while (iterator.hasNext()) {
        	
        	item = iterator.next();
        	
        	retValue.add(item.getString("clientId"));
        	
        }    	
		
		
		return retValue;
		
	}
 	
	public static int countOpenExecutionsFromDynamo(String config) {
		
		return listOpenExecutionsFromDynamo(config).size();

	}
	
	
	
	public static void reconcileFix(String configId) { 
		ArrayList<String> listOpenExecutionsFromDynamo = listOpenExecutionsFromDynamo(configId);
		ArrayList<String> listOpenExecutionsFromSWF = SchedulerUDE.listOpenExecutionsFromSWF(configId);
		
		listOpenExecutionsFromDynamo.removeAll(listOpenExecutionsFromSWF);
		for (int cnt = 0; cnt < listOpenExecutionsFromDynamo.size(); cnt++) {
			System.out.println("   Now fixing...    " + listOpenExecutionsFromDynamo.get(cnt));
			
			DynamoDBMapper mapper = DyDBUtils.getDynamoDBMapper();
			SchedulerWorkflowState schedulerWorkflowState = mapper.load(SchedulerWorkflowState.class, listOpenExecutionsFromDynamo.get(cnt));

			schedulerWorkflowState.setLoopState("CLOSED");
			mapper.save(schedulerWorkflowState);
		}
		
	}
	public static void reconcileReport(String configId) {
		ArrayList<String> listOpenExecutionsFromDynamo = listOpenExecutionsFromDynamo(configId);
		ArrayList<String> listOpenExecutionsFromSWF = SchedulerUDE.listOpenExecutionsFromSWF(configId);
		
		if (listOpenExecutionsFromSWF.size() > listOpenExecutionsFromDynamo.size()) { 
			System.out.println("use -cane  to attempt canceling executions... this shoudl NOT occur in normal processing");
	
			listOpenExecutionsFromSWF.removeAll(listOpenExecutionsFromDynamo);
			for (int cnt = 0; cnt < listOpenExecutionsFromDynamo.size(); cnt++) {
				System.out.println("     " + listOpenExecutionsFromSWF.get(cnt));
			}
			
		}
		else {
			System.out.println("use -rfix  to fix entries in db for the following executions");	
			listOpenExecutionsFromDynamo.removeAll(listOpenExecutionsFromSWF);
			for (int cnt = 0; cnt < listOpenExecutionsFromDynamo.size(); cnt++) {
				System.out.println("     " + listOpenExecutionsFromDynamo.get(cnt));
			}
		}
		
	}
 	
	
	
	
	
    public static void reconcile(String config) {

    			int countOpenExecutionsfromDynamo = countOpenExecutionsFromDynamo(config);
    			int countOpenExecutionsfromSWF = SchedulerUDE.countOpenExecutionsFromSWF(config);
    			
    	    	
    	    	if ( countOpenExecutionsfromDynamo == countOpenExecutionsfromSWF) {
    	    		System.out.println("The actual versus reported counts of open executions matches!");
    	    		
    	    	}
    	    	else {
    	    		System.out.println("The actual versus reported counts of open executions DO NOT match!");
    	    		System.out.println("     Actual   open executions : " + countOpenExecutionsfromSWF);
    	    		System.out.println("     Reported open executions : " + countOpenExecutionsfromDynamo);
    	    		
    	    	}
    	
    	

    }



    public static  AWSCredentials getAwsCredentials() {
    	String swfAccessId = System.getenv("AWS_ACCESS_KEY_ID");	     
    	String swfSecretKey = System.getenv("AWS_SECRET_ACCESS_KEY");
    	AWSCredentials awsCredentials = new BasicAWSCredentials(swfAccessId, swfSecretKey);
    	return awsCredentials;
    }

    public static  DynamoDB getDynamoDB() {
    	AWSCredentials awsCredentials = getAwsCredentials();
    	DynamoDB dynamoDB = new DynamoDB(new AmazonDynamoDBClient(awsCredentials));
    	return dynamoDB;
    }


    public static DynamoDBMapper getDynamoDBMapper() {
		AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(getAwsCredentials());
		return new DynamoDBMapper(dynamoDBClient);
    }

    
	
    

    public static void deleteDynamoTables(String [] tables) {

    	DynamoDB dynamoDB =  getDynamoDB();
	
    	for (int cnt = 0; cnt< tables.length; cnt++) {
    		SchedulerDynamoTable schedulerDynamoTable = new SchedulerDynamoTable(tables[cnt]);
    		schedulerDynamoTable.deleteDynamoTable( dynamoDB);
    	}

	
    }

    
    
    public static void createDynamoTables(String [] tables) {
    		DynamoDB dynamoDB =  getDynamoDB();
	
	
			for (int cnt = 0; cnt< tables.length; cnt++) {
				SchedulerDynamoTable schedulerDynamoTable = new SchedulerDynamoTable(tables[cnt]);
			schedulerDynamoTable.createDynamoTable(dynamoDB);
			}
    }


    

	
	public static void createDomain(String [] args) {

		
		
		
		AWSCredentials awsCredentials = getAwsCredentials();
		
		AmazonSimpleWorkflowClient amazonSimpleWorkflowClient = new AmazonSimpleWorkflowClient(awsCredentials);
		amazonSimpleWorkflowClient.registerDomain(new RegisterDomainRequest()
				.withName(args[0])
				.withDescription(args[1])
				.withWorkflowExecutionRetentionPeriodInDays(args[2])
				);
		
	
	}

	public static void deprecateDomain(String domainName) {

			
		
		AWSCredentials awsCredentials = getAwsCredentials();
		
		AmazonSimpleWorkflowClient amazonSimpleWorkflowClient = new AmazonSimpleWorkflowClient(awsCredentials);
		amazonSimpleWorkflowClient.deprecateDomain(new DeprecateDomainRequest()
				.withName(domainName)
				);
		
	
	}


	
	public static SchedulerConfig getSchedulerConfig(String configId) {
		DynamoDBMapper mapper = getDynamoDBMapper();
		SchedulerConfig schedulerConfig = mapper.load(SchedulerConfig.class, configId);
		return schedulerConfig;
	}
	
	public static void createSchedulerConfig(String [] args) {

		DynamoDBMapper mapper = getDynamoDBMapper();
		SchedulerConfig schedulerConfig = new SchedulerConfig(args);
    	mapper.save(schedulerConfig);

	}
	

	
	
	
	
	
    
    public static void createFunction (String [] functionArgs) {
    	SchedulerUDF schedulerUDF = new SchedulerUDF(functionArgs[0], functionArgs[1], functionArgs[2]);
    	DynamoDBMapper mapper = getDynamoDBMapper();
    	mapper.save(schedulerUDF);
    }

    
    public static void deleteFunction(String functionAlias) {
    	DynamoDBMapper mapper = getDynamoDBMapper();

    	SchedulerUDF schedulerUDF = mapper.load(SchedulerUDF.class, functionAlias);
    	mapper.delete(schedulerUDF);
	
    }

    
    public static void listFunctions() {
    	DynamoDBMapper mapper = getDynamoDBMapper();
    	
    	DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
    	List <SchedulerUDF> scanResults = mapper.scan(SchedulerUDF.class, scanExpression);
    	
    	for (int cnt = 0; cnt < scanResults.size(); cnt++) {
    		
    		System.out.println(scanResults.get(cnt).getFunctionAlias() + " --> " + scanResults.get(cnt).getFunctionName());
    	}
    	
    	
    }
    
    public static void executeFunction(String config, String [] scheduleFunctionOptions) {
    
    	String clientId = UUID.randomUUID().toString();
	     String jobName = UUID.randomUUID().toString();
	     SchedulerConfig schedulerConfig = DyDBUtils.getSchedulerConfig(config);
	     
	     
	     ScheduleFunction scheduleFunction = new ScheduleFunction(scheduleFunctionOptions);
	     
	     
	     
	     SchedulerUDF schedulerUDF = new SchedulerUDF(scheduleFunction.functionName, scheduleFunction.functionType, scheduleFunction.functionName );
	     
	     
	     SchedulerUDJ schedulerUDJ = new SchedulerUDJ(jobName,
	    		 scheduleFunction.functionName,	
	    		 scheduleFunction.functionContext,
	    		 scheduleFunction.cronExpression,
	    		 scheduleFunction.repeatTimes,
	    		 scheduleFunction.startTime,
	    		 scheduleFunction.endTime,
	    		 scheduleFunction.timeZone);
	     
	    		 
	    SchedulerUDE schedulerUDE = new SchedulerUDE(clientId,schedulerUDF,schedulerUDJ)	;
	    DyDBUtils.createSchedulerUDE(schedulerUDE);
	    
	    schedulerUDE.execute(clientId, schedulerConfig);
    }


    public static void createJob (String [] jobArgs) {
    	SchedulerUDJ schedulerUDJ = new SchedulerUDJ(jobArgs);
    	DynamoDBMapper mapper = getDynamoDBMapper();
    	mapper.save(schedulerUDJ);

    }

    
    public static void deleteJob(String jobName) {
    	DynamoDBMapper mapper = getDynamoDBMapper();

		SchedulerUDJ schedulerUDJ = mapper.load(SchedulerUDJ.class, jobName);
		mapper.delete(schedulerUDJ);
	
    }
    

    public static void listJobs() {
    	DynamoDBMapper mapper = getDynamoDBMapper();
    	
    	DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
    	List <SchedulerUDJ> scanResults = mapper.scan(SchedulerUDJ.class, scanExpression);
    	
    	for (int cnt = 0; cnt < scanResults.size(); cnt++) {
    		
    		System.out.println(scanResults.get(cnt).getJobName() + " -->  (" 
    				+ scanResults.get(cnt).getFunctionAlias() 
    				+ " , " 
    				+ scanResults.get(cnt).getFunctionContext() 
    				+ " , "
    				+ scanResults.get(cnt).getCronExpression() 
    				+ " , " 
    				+ scanResults.get(cnt).getRepeatTimes() 
    				+ " , " 
    				+ scanResults.get(cnt).getStartTimeDate()
    				+ " , "
    				+ scanResults.get(cnt).getEndTimeDate()
    				+ " )"
    				);
    	}
    	
    	
    }

    
    public static void executeJob(String config, String jobName) {
    	
    	
	    SchedulerConfig schedulerConfig = DyDBUtils.getSchedulerConfig(config);

    	String clientId = UUID.randomUUID().toString();

    	
    	DynamoDBMapper mapper = getDynamoDBMapper();

    	SchedulerUDJ schedulerUDJ = mapper.load(SchedulerUDJ.class, jobName);
    	SchedulerUDF schedulerUDF = mapper.load(SchedulerUDF.class, schedulerUDJ.getFunctionAlias());

        SchedulerUDE schedulerUDE = new SchedulerUDE(clientId,schedulerUDF,schedulerUDJ)	;
	    DyDBUtils.createSchedulerUDE(schedulerUDE);
	    
	    schedulerUDE.execute(clientId, schedulerConfig);
    	
    

    }
    
    
    
    
    
    
    
    
    
    
    
	public static int countActiveJobs() {
		DynamoDB dynamoDB = getDynamoDB();

    	Table table = dynamoDB.getTable("SchedulerWorkflowState");
    	Index index = table.getIndex("loopStateIndex");

        ItemCollection<QueryOutcome> items = null;
        QuerySpec querySpec = new QuerySpec();
        
        int count = 0;
    	
        querySpec.withKeyConditionExpression("loopState = :v_state")
    	.withValueMap(new ValueMap()
    			.withString(":v_state","PROCESSING")
		      );
    	items = index.query(querySpec);
    	Iterator<Item> iterator = items.iterator();

        
        while (iterator.hasNext()) {

    	    iterator.next();
    	    count++;
        }    	
        
        return count;
		
	}

    public static void listActiveJobs(String optionalExecutionId) {
    	DynamoDB dynamoDB = getDynamoDB();

    	Table table = dynamoDB.getTable("SchedulerWorkflowState");
    	Index index = table.getIndex("loopStateIndex");

        ItemCollection<QueryOutcome> items = null;
        QuerySpec querySpec = new QuerySpec();


        if ( (optionalExecutionId != null)  && !optionalExecutionId.isEmpty()) {
        	querySpec.withKeyConditionExpression("loopState = :v_state and begins_with(clientId, :v_eid)")
        	.withValueMap(new ValueMap()
        			.withString(":v_state","PROCESSING")
        			.withString(":v_eid",optionalExecutionId)
        			);
	    
        }
        else {

        	querySpec.withKeyConditionExpression("loopState = :v_state")
        	.withValueMap(new ValueMap()
        			.withString(":v_state","PROCESSING")
			      );
	}
	
	items = index.query(querySpec);
	Iterator<Item> iterator = items.iterator();



	System.out.format("%20s %7s %28s %40s\n", "CronExpression", "Loop Count", "Next Scheduled Time       ", " ExecutionId");
        while (iterator.hasNext()) {

	    Item item = iterator.next();
	    
	    System.out.format("%20s %7s %28s %40s\n", item.get("cronExpression"), item.get("loopCount"), item.get("lastProposedTimeDate"),   item.get("clientId"));
	    

        }

    }

    

    public static void listStatusExecution(String config, String clientId) {
    	SchedulerConfig schedulerConfig = DyDBUtils.getSchedulerConfig(config);

	     SchedulerUDE schedulerUDE = DyDBUtils.getSchedulerUDE(clientId);
	     schedulerUDE.listStatus(clientId, schedulerConfig);
    }
    public static void cancelExecution(String config, String [] cancelArgs) {
    	 			 
	     SchedulerConfig schedulerConfig = DyDBUtils.getSchedulerConfig(config);
	     SchedulerUDE schedulerUDE = DyDBUtils.getSchedulerUDE(cancelArgs[0]);
	     schedulerUDE.cancelExecution(cancelArgs[0], cancelArgs[1], schedulerConfig);
    }
	

	public static void createSchedulerUDE(SchedulerUDE schedulerUDE) {
		DynamoDBMapper mapper = getDynamoDBMapper();
    	mapper.save(schedulerUDE);
	}

	
	
	public static SchedulerUDE getSchedulerUDE(String executionId) {
		DynamoDBMapper mapper = getDynamoDBMapper();
		SchedulerUDE schedulerUDE = mapper.load(SchedulerUDE.class, executionId);
		return schedulerUDE;
	}
	
	

  
    
    

    
    



    
    
       
}
