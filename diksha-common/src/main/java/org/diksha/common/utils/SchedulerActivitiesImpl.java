
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

package org.diksha.common.utils;

import com.amazonaws.services.simpleworkflow.flow.ActivityExecutionContext;
import com.amazonaws.services.simpleworkflow.flow.ActivityExecutionContextProvider;
import com.amazonaws.services.simpleworkflow.flow.ActivityExecutionContextProviderImpl;
import com.amazonaws.services.simpleworkflow.flow.generic.GenericWorkflowClientExternal;
import com.amazonaws.services.simpleworkflow.flow.generic.TerminateWorkflowExecutionParameters;
import com.amazonaws.services.simpleworkflow.flow.worker.GenericWorkflowClientExternalImpl;
import com.amazonaws.services.simpleworkflow.model.ActivityTask;
import com.amazonaws.services.simpleworkflow.model.WorkflowExecution;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.lambda.AWSLambdaClient;
import com.amazonaws.services.lambda.model.InvokeRequest;

public class SchedulerActivitiesImpl implements SchedulerActivities {

	private static final Logger logger = LogManager.getLogger(SchedulerActivitiesImpl.class);

	@Override
	public void runPeriodicActivity(String functionName, String functionContext) {

		ActivityExecutionContextProvider provider = new ActivityExecutionContextProviderImpl();
		ActivityExecutionContext aec = provider.getActivityExecutionContext();
		// System.out.println("NOw invoking lambda");

		String swfAccessId = System.getenv("AWS_ACCESS_KEY_ID");
		String swfSecretKey = System.getenv("AWS_SECRET_ACCESS_KEY");
		AWSCredentials awsCredentials = new BasicAWSCredentials(swfAccessId, swfSecretKey);

		AWSLambdaClient alc = new AWSLambdaClient(awsCredentials);
		InvokeRequest invokeRequest = new InvokeRequest();
		System.out.println("invoking function " + functionName + " for executionId " + aec.getWorkflowExecution().getWorkflowId());
		logger.info(
				"invoking function " + functionName + " for executionId " + aec.getWorkflowExecution().getWorkflowId());
		invokeRequest.setFunctionName(functionName);
		invokeRequest.setInvocationType("Event");
		invokeRequest.setClientContext(functionContext);
		alc.invoke(invokeRequest);

	}

	@Override
	public void terminateScheduler() {

		logger.info("Started terminate process");

		try {
			ActivityExecutionContextProvider provider = new ActivityExecutionContextProviderImpl();
			ActivityExecutionContext aec = provider.getActivityExecutionContext();

			ActivityTask at = aec.getTask();
			WorkflowExecution we = at.getWorkflowExecution();

			GenericWorkflowClientExternal gwcei = new GenericWorkflowClientExternalImpl(aec.getService(),
					aec.getDomain());
			TerminateWorkflowExecutionParameters terminateParameters = new TerminateWorkflowExecutionParameters(we,
					com.amazonaws.services.simpleworkflow.model.ChildPolicy.TERMINATE, "limits", "limits");
			gwcei.terminateWorkflowExecution(terminateParameters);
			logger.info("Terminated");
		} catch (Exception e) {
			logger.error("Terminate was not successful because of " + e.getMessage());
		}
	}

}
