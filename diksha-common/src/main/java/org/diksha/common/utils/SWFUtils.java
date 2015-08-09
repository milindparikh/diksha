package org.diksha.common.utils;

import org.diksha.common.dyutils.DyDBUtils;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowClient;
import com.amazonaws.services.simpleworkflow.model.DeprecateDomainRequest;
import com.amazonaws.services.simpleworkflow.model.RegisterDomainRequest;

public class SWFUtils {

	

	
	public static void createDomain(String [] args) {

		
		
		
		AWSCredentials awsCredentials = DyDBUtils.getAwsCredentials();
		
		AmazonSimpleWorkflowClient amazonSimpleWorkflowClient = new AmazonSimpleWorkflowClient(awsCredentials);
		amazonSimpleWorkflowClient.registerDomain(new RegisterDomainRequest()
				.withName(args[0])
				.withDescription(args[1])
				.withWorkflowExecutionRetentionPeriodInDays(args[2])
				);
		
	
	}

	public static void deprecateDomain(String domainName) {

			
		
		AWSCredentials awsCredentials = DyDBUtils.getAwsCredentials();
		
		AmazonSimpleWorkflowClient amazonSimpleWorkflowClient = new AmazonSimpleWorkflowClient(awsCredentials);
		amazonSimpleWorkflowClient.deprecateDomain(new DeprecateDomainRequest()
				.withName(domainName)
				);
		
	
	}



}
