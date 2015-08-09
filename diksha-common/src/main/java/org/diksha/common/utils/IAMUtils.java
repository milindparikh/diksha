package org.diksha.common.utils;

import java.util.List;

import org.diksha.common.dyutils.DyDBUtils;

import com.amazonaws.services.identitymanagement.AmazonIdentityManagementClient;
import com.amazonaws.services.identitymanagement.model.AttachGroupPolicyRequest;
import com.amazonaws.services.identitymanagement.model.AttachedPolicy;
import com.amazonaws.services.identitymanagement.model.CreateGroupRequest;
import com.amazonaws.services.identitymanagement.model.CreatePolicyRequest;
import com.amazonaws.services.identitymanagement.model.DeleteGroupRequest;
import com.amazonaws.services.identitymanagement.model.DeletePolicyRequest;
import com.amazonaws.services.identitymanagement.model.DetachGroupPolicyRequest;
import com.amazonaws.services.identitymanagement.model.ListAttachedGroupPoliciesRequest;
import com.amazonaws.services.identitymanagement.model.Policy;

public class IAMUtils {

	public static void deleteSecurityPolicy(String arg) {
		AmazonIdentityManagementClient amazonIdentityManagementClient = new AmazonIdentityManagementClient(
				DyDBUtils.getAwsCredentials());
		List<Policy> listPolicies = amazonIdentityManagementClient.listPolicies().getPolicies();

		for (int cnt = 0; cnt < listPolicies.size(); cnt++) {
			if (listPolicies.get(cnt).getPolicyName().equals(arg)) {
				amazonIdentityManagementClient
						.deletePolicy(new DeletePolicyRequest().withPolicyArn(listPolicies.get(cnt).getArn()));
			}
		}
	}

	public static void createSecurityPolicy(String policyName, String description, String policyDocument) {
		AmazonIdentityManagementClient amazonIdentityManagementClient = new AmazonIdentityManagementClient(
				DyDBUtils.getAwsCredentials());
		amazonIdentityManagementClient.createPolicy(new CreatePolicyRequest().withPolicyName(policyName)
				.withDescription(description).withPolicyDocument(policyDocument));

	}

	public static void deleteSecurityGroup(String groupName) {
		AmazonIdentityManagementClient amazonIdentityManagementClient = new AmazonIdentityManagementClient(
				DyDBUtils.getAwsCredentials());
		List<AttachedPolicy> attachedPolicies = amazonIdentityManagementClient
				.listAttachedGroupPolicies(new ListAttachedGroupPoliciesRequest().withGroupName(groupName))
				.getAttachedPolicies();
		for (int cnt = 0; cnt < attachedPolicies.size(); cnt++) {
			amazonIdentityManagementClient.detachGroupPolicy(new DetachGroupPolicyRequest().withGroupName(groupName)
					.withPolicyArn(attachedPolicies.get(cnt).getPolicyArn()));
		}

		amazonIdentityManagementClient.deleteGroup(new DeleteGroupRequest().withGroupName(groupName));

	}

	public static void createSecurityGroup(String groupName, String policyName) {
		String policyArn = null;
		AmazonIdentityManagementClient amazonIdentityManagementClient = new AmazonIdentityManagementClient(
				DyDBUtils.getAwsCredentials());

		List<Policy> listPolicies = amazonIdentityManagementClient.listPolicies().getPolicies();

		for (int cnt = 0; cnt < listPolicies.size(); cnt++) {
			if (listPolicies.get(cnt).getPolicyName().equals(policyName)) {
				policyArn = listPolicies.get(cnt).getArn();
			}
		}

		if (policyArn != null) {

			amazonIdentityManagementClient.createGroup(new CreateGroupRequest().withGroupName(groupName));
			amazonIdentityManagementClient.attachGroupPolicy(
					new AttachGroupPolicyRequest().withGroupName(groupName).withPolicyArn(policyArn));

		}
	}

	public static void createSecurityPolicyAdmin(String arg) {

		/*
		 * AmazonIdentityManagementClient amazonIdentityManagementClient = new
		 * AmazonIdentityManagementClient();
		 * amazonIdentityManagementClient.createPolicy("");
		 */

		String[] args = arg.split(",");

		String policyName = args[0];
		String accountNumber = args[1];
		String domainName = args[2];

		IAMSecurityPolicy iamSecurityPolicy = new IAMSecurityPolicy();

		IAMSecurityPolicyStatement iamSecurityPolicyStatement = new IAMSecurityPolicyStatement();

		IAMSecurityPolicyStatementPermission iamSecurityPolicyStatementPermission1 = new IAMSecurityPolicyStatementPermission();

		iamSecurityPolicyStatementPermission1.setEffect("Allow");
		iamSecurityPolicyStatementPermission1.setAction("swf:*");
		iamSecurityPolicyStatementPermission1.setResource("arn:aws:swf:*:" + accountNumber + ":/domain/" + domainName);

		iamSecurityPolicyStatement.add(iamSecurityPolicyStatementPermission1);

		IAMSecurityPolicyStatementPermission iamSecurityPolicyStatementPermission2 = new IAMSecurityPolicyStatementPermission();
		iamSecurityPolicyStatementPermission2.setEffect("Allow");
		iamSecurityPolicyStatementPermission2.setAction("dynamodb:*");
		iamSecurityPolicyStatementPermission2.setResource("arn:aws:dynamodb:*:" + accountNumber + ":*");

		iamSecurityPolicyStatement.add(iamSecurityPolicyStatementPermission2);

		iamSecurityPolicy.add(iamSecurityPolicyStatement);

		System.out.println(iamSecurityPolicy.toSecurityPolicyDocument());

		createSecurityPolicy(policyName, policyName, iamSecurityPolicy.toSecurityPolicyDocument());

	}

	public static void deleteSecurityPolicyAdmin(String arg) {
		deleteSecurityPolicy(arg);
	}

	public static void createSecurityPolicyDesigner(String arg) {

		String[] args = arg.split(",");

		String policyName = args[0];
		String accountNumber = args[1];
		String domainName = args[2];

		IAMSecurityPolicy iamSecurityPolicy = new IAMSecurityPolicy();

		IAMSecurityPolicyStatement iamSecurityPolicyStatement = new IAMSecurityPolicyStatement();

		IAMSecurityPolicyStatementPermission iamSecurityPolicyStatementPermission2 = new IAMSecurityPolicyStatementPermission();
		iamSecurityPolicyStatementPermission2.setEffect("Allow");

		iamSecurityPolicyStatementPermission2.addAction("dynamodb:GetItem");
		iamSecurityPolicyStatementPermission2.addAction("dynamodb:BatchGetItem");
		iamSecurityPolicyStatementPermission2.addAction("dynamodb:Query");
		iamSecurityPolicyStatementPermission2.addAction("dynamodb:Scan");
		iamSecurityPolicyStatementPermission2.addAction("dynamodb:PutItem");
		iamSecurityPolicyStatementPermission2.addAction("dynamodb:UpdateItem");
		iamSecurityPolicyStatementPermission2.addAction("dynamodb:DeleteItem");
		iamSecurityPolicyStatementPermission2.addAction("dynamodb:BatchWriteItem");

		iamSecurityPolicyStatementPermission2
				.addResource("arn:aws:dynamodb:*:" + accountNumber + ":table/SchedulerUDF");
		iamSecurityPolicyStatementPermission2
				.addResource("arn:aws:dynamodb:*:" + accountNumber + ":table/SchedulerUDF/index/*");
		iamSecurityPolicyStatementPermission2
				.addResource("arn:aws:dynamodb:*:" + accountNumber + ":table/SchedulerUDJ");
		iamSecurityPolicyStatementPermission2
				.addResource("arn:aws:dynamodb:*:" + accountNumber + ":table/SchedulerUDJ/index/*");

		iamSecurityPolicyStatement.add(iamSecurityPolicyStatementPermission2);

		iamSecurityPolicy.add(iamSecurityPolicyStatement);

		System.out.println(iamSecurityPolicy.toSecurityPolicyDocument());

		createSecurityPolicy(policyName, policyName, iamSecurityPolicy.toSecurityPolicyDocument());

	}

	public static void deleteSecurityPolicyDesigner(String arg) {
		deleteSecurityPolicy(arg);
	}

	public static void createSecurityPolicyUser(String arg) {

		String[] args = arg.split(",");

		String policyName = args[0];
		String accountNumber = args[1];
		String domainName = args[2];

		IAMSecurityPolicy iamSecurityPolicy = new IAMSecurityPolicy();

		IAMSecurityPolicyStatement iamSecurityPolicyStatement = new IAMSecurityPolicyStatement();

		IAMSecurityPolicyStatementPermission iamSecurityPolicyStatementPermission1 = new IAMSecurityPolicyStatementPermission();

		iamSecurityPolicyStatementPermission1.setEffect("Allow");

		iamSecurityPolicyStatementPermission1.addAction("swf:CountOpenWorkflowExecutions");
		iamSecurityPolicyStatementPermission1.addAction("swf:CountClosedWorkflowExecutions");
		iamSecurityPolicyStatementPermission1.addAction("swf:DescribeActivityType");
		iamSecurityPolicyStatementPermission1.addAction("swf:DescribeDomain");
		iamSecurityPolicyStatementPermission1.addAction("swf:DescribeWorkflowExecution");
		iamSecurityPolicyStatementPermission1.addAction("swf:DescribeWorkflowType");
		iamSecurityPolicyStatementPermission1.addAction("swf:GetWorkflowExecutionHistory");
		iamSecurityPolicyStatementPermission1.addAction("swf:ListActivityTypes");
		iamSecurityPolicyStatementPermission1.addAction("swf:ListClosedWorkflowExecutions");
		iamSecurityPolicyStatementPermission1.addAction("swf:ListOpenWorkflowExecutions");
		iamSecurityPolicyStatementPermission1.addAction("swf:RequestCancelWorkflowExecution");
		iamSecurityPolicyStatementPermission1.addAction("swf:SignalWorkflowExecution");
		iamSecurityPolicyStatementPermission1.addAction("swf:StartWorkflowExecution");
		iamSecurityPolicyStatementPermission1.addAction("swf:TerminateWorkflowExecution");

		iamSecurityPolicyStatementPermission1.setResource("arn:aws:swf:*:" + accountNumber + ":/domain/" + domainName);

		iamSecurityPolicyStatement.add(iamSecurityPolicyStatementPermission1);

		IAMSecurityPolicyStatementPermission iamSecurityPolicyStatementPermission2 = new IAMSecurityPolicyStatementPermission();
		iamSecurityPolicyStatementPermission2.setEffect("Allow");

		iamSecurityPolicyStatementPermission2.addAction("dynamodb:GetItem");
		iamSecurityPolicyStatementPermission2.addAction("dynamodb:BatchGetItem");
		iamSecurityPolicyStatementPermission2.addAction("dynamodb:Query");
		iamSecurityPolicyStatementPermission2.addAction("dynamodb:Scan");

		iamSecurityPolicyStatementPermission2
				.addResource("arn:aws:dynamodb:*:" + accountNumber + ":table/SchedulerConfig");
		iamSecurityPolicyStatementPermission2
				.addResource("arn:aws:dynamodb:*:" + accountNumber + ":table/SchedulerConfig/index/*");
		iamSecurityPolicyStatementPermission2
				.addResource("arn:aws:dynamodb:*:" + accountNumber + ":table/SchedulerUDF");
		iamSecurityPolicyStatementPermission2
				.addResource("arn:aws:dynamodb:*:" + accountNumber + ":table/SchedulerUDF/index/*");
		iamSecurityPolicyStatementPermission2
				.addResource("arn:aws:dynamodb:*:" + accountNumber + ":table/SchedulerUDJ");
		iamSecurityPolicyStatementPermission2
				.addResource("arn:aws:dynamodb:*:" + accountNumber + ":table/SchedulerUDJ/index/*");
		iamSecurityPolicyStatementPermission2
				.addResource("arn:aws:dynamodb:*:" + accountNumber + ":table/SchedulerWorkflowState");
		iamSecurityPolicyStatementPermission2
				.addResource("arn:aws:dynamodb:*:" + accountNumber + ":table/SchedulerWorkflowState/index/*");

		iamSecurityPolicyStatement.add(iamSecurityPolicyStatementPermission2);

		IAMSecurityPolicyStatementPermission iamSecurityPolicyStatementPermission3 = new IAMSecurityPolicyStatementPermission();
		iamSecurityPolicyStatementPermission3.setEffect("Allow");

		iamSecurityPolicyStatementPermission3.addAction("dynamodb:GetItem");
		iamSecurityPolicyStatementPermission3.addAction("dynamodb:BatchGetItem");
		iamSecurityPolicyStatementPermission3.addAction("dynamodb:Query");
		iamSecurityPolicyStatementPermission3.addAction("dynamodb:Scan");
		iamSecurityPolicyStatementPermission3.addAction("dynamodb:PutItem");
		iamSecurityPolicyStatementPermission3.addAction("dynamodb:UpdateItem");
		iamSecurityPolicyStatementPermission3.addAction("dynamodb:DeleteItem");
		iamSecurityPolicyStatementPermission3.addAction("dynamodb:BatchWriteItem");

		iamSecurityPolicyStatementPermission3
				.addResource("arn:aws:dynamodb:*:" + accountNumber + ":table/SchedulerUDE");
		iamSecurityPolicyStatementPermission3
				.addResource("arn:aws:dynamodb:*:" + accountNumber + ":table/SchedulerUDE/index/*");

		iamSecurityPolicyStatement.add(iamSecurityPolicyStatementPermission3);

		iamSecurityPolicy.add(iamSecurityPolicyStatement);

		System.out.println(iamSecurityPolicy.toSecurityPolicyDocument());

		createSecurityPolicy(policyName, policyName, iamSecurityPolicy.toSecurityPolicyDocument());

	}

	public static void deleteSecurityPolicyUser(String arg) {
		deleteSecurityPolicy(arg);

	}

	public static void createSecurityPolicyWorkflow(String arg) {

		String[] args = arg.split(",");

		String policyName = args[0];
		String accountNumber = args[1];
		String domainName = args[2];

		IAMSecurityPolicy iamSecurityPolicy = new IAMSecurityPolicy();

		IAMSecurityPolicyStatement iamSecurityPolicyStatement = new IAMSecurityPolicyStatement();

		IAMSecurityPolicyStatementPermission iamSecurityPolicyStatementPermission1 = new IAMSecurityPolicyStatementPermission();

		iamSecurityPolicyStatementPermission1.setEffect("Allow");
		iamSecurityPolicyStatementPermission1.setAction("swf:*");
		iamSecurityPolicyStatementPermission1.setResource("arn:aws:swf:*:" + accountNumber + ":/domain/" + domainName);

		IAMSecurityPolicyStatementPermission iamSecurityPolicyStatementPermission2 = new IAMSecurityPolicyStatementPermission();
		iamSecurityPolicyStatementPermission2.setEffect("Allow");

		iamSecurityPolicyStatementPermission2.addAction("dynamodb:GetItem");
		iamSecurityPolicyStatementPermission2.addAction("dynamodb:BatchGetItem");
		iamSecurityPolicyStatementPermission2.addAction("dynamodb:Query");
		iamSecurityPolicyStatementPermission2.addAction("dynamodb:Scan");

		iamSecurityPolicyStatementPermission2
				.addResource("arn:aws:dynamodb:*:" + accountNumber + ":table/SchedulerConfig");
		iamSecurityPolicyStatementPermission2
				.addResource("arn:aws:dynamodb:*:" + accountNumber + ":table/SchedulerConfig/index/*");

		IAMSecurityPolicyStatementPermission iamSecurityPolicyStatementPermission3 = new IAMSecurityPolicyStatementPermission();
		iamSecurityPolicyStatementPermission3.setEffect("Allow");

		iamSecurityPolicyStatementPermission3.addAction("dynamodb:GetItem");
		iamSecurityPolicyStatementPermission3.addAction("dynamodb:BatchGetItem");
		iamSecurityPolicyStatementPermission3.addAction("dynamodb:Query");
		iamSecurityPolicyStatementPermission3.addAction("dynamodb:Scan");
		iamSecurityPolicyStatementPermission3.addAction("dynamodb:PutItem");
		iamSecurityPolicyStatementPermission3.addAction("dynamodb:UpdateItem");
		iamSecurityPolicyStatementPermission3.addAction("dynamodb:DeleteItem");
		iamSecurityPolicyStatementPermission3.addAction("dynamodb:BatchWriteItem");

		iamSecurityPolicyStatementPermission3
				.addResource("arn:aws:dynamodb:*:" + accountNumber + ":table/SchedulerUDE");
		iamSecurityPolicyStatementPermission3
				.addResource("arn:aws:dynamodb:*:" + accountNumber + ":table/SchedulerUDE/index/*");
		iamSecurityPolicyStatementPermission3
				.addResource("arn:aws:dynamodb:*:" + accountNumber + ":table/SchedulerWorkflowState");
		iamSecurityPolicyStatementPermission3
				.addResource("arn:aws:dynamodb:*:" + accountNumber + ":table/SchedulerWorkflowState/index/*");

		IAMSecurityPolicyStatementPermission iamSecurityPolicyStatementPermission4 = new IAMSecurityPolicyStatementPermission();
		iamSecurityPolicyStatementPermission4.setEffect("Allow");

		iamSecurityPolicyStatementPermission4.addAction("lambda:InvokeFunction");

		iamSecurityPolicyStatementPermission4.addResource("arn:aws:lambda:*:" + accountNumber + ":*:*");

		iamSecurityPolicyStatement.add(iamSecurityPolicyStatementPermission1);

		iamSecurityPolicyStatement.add(iamSecurityPolicyStatementPermission2);

		iamSecurityPolicyStatement.add(iamSecurityPolicyStatementPermission3);
		iamSecurityPolicyStatement.add(iamSecurityPolicyStatementPermission4);

		iamSecurityPolicy.add(iamSecurityPolicyStatement);

		System.out.println(iamSecurityPolicy.toSecurityPolicyDocument());

		createSecurityPolicy(policyName, policyName, iamSecurityPolicy.toSecurityPolicyDocument());

	}

	public static void deleteSecurityPolicyWorkflow(String arg) {

		deleteSecurityPolicy(arg);

	}

}