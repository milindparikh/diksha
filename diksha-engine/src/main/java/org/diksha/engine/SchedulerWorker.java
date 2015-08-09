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

package org.diksha.engine;

import org.diksha.common.dyutils.DyDBUtils;
import org.diksha.common.dyutils.SchedulerConfig;
import org.diksha.common.utils.SchedulerActivitiesImpl;
import org.diksha.common.utils.SchedulerWorkflowImpl;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowClient;
import com.amazonaws.services.simpleworkflow.flow.ActivityWorker;
import com.amazonaws.services.simpleworkflow.flow.WorkflowWorker;


public class SchedulerWorker {

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
	    	 ActivityWorker aw = new ActivityWorker(service, domain, taskListToPoll);
	    	 aw.addActivitiesImplementation(new SchedulerActivitiesImpl());
	    	 aw.start();
	    	 
		     WorkflowWorker wfw = new WorkflowWorker(service, domain, taskListToPoll);
		     wfw.addWorkflowImplementationType(SchedulerWorkflowImpl.class);
		     wfw.start();

	     }
	     catch (Exception e) {
	    	 System.out.println("should have caught it");
	     }
	    
	     


	}

}
