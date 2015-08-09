package org.diksha.common.utils;

import org.diksha.common.dyutils.DyDBUtils;
import org.diksha.common.dyutils.SchedulerConfig;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowClient;
import com.amazonaws.services.simpleworkflow.flow.WorkflowWorker;

public class SchedulerWorkflowWorker {

	
	public static void main(String[] args) throws Exception {
		
		
		 ClientConfiguration config = new ClientConfiguration().withSocketTimeout(70*1000);

	     String swfAccessId = System.getenv("AWS_ACCESS_KEY_ID");
	     String swfSecretKey = System.getenv("AWS_SECRET_ACCESS_KEY");
	     AWSCredentials awsCredentials = new BasicAWSCredentials(swfAccessId, swfSecretKey);

	     
	     
	     AmazonSimpleWorkflow service = new AmazonSimpleWorkflowClient(awsCredentials, config);
	     
	     String configParam ;
	     
	     if (args.length == 0 ) {
	    	 configParam = "cf1";
	     }
	     else {
	    	 	configParam = args[0];
	     }
	     
	     SchedulerConfig schedulerConfig = DyDBUtils.getSchedulerConfig(configParam);
	     
	     service.setEndpoint(schedulerConfig.getEndPoint());

	     String domain = schedulerConfig.getDomain();
	     String taskListToPoll = schedulerConfig.getTaskList();
	     

	     try {	    	 
		     WorkflowWorker wfw = new WorkflowWorker(service, domain, taskListToPoll);
		     wfw.addWorkflowImplementationType(SchedulerWorkflowImpl.class);
		     wfw.start();

	     }
	     catch (Exception e) {
	    	 System.out.println("shpuld have caught it");
	     }
	    
	     


	}

}
