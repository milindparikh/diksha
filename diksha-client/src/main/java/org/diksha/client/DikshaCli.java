
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

package org.diksha.client;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.diksha.common.dyutils.DyDBUtils;
import org.diksha.common.dyutils.SchedulerDynamoTable;
import org.diksha.common.utils.IAMUtils;
import org.diksha.common.utils.SWFUtils;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;

/* 
 * This is the command line interface to samay; a scheduler for scheduling functions.
 * 
 *  -ccfg "cf1|https://swf.us-east-1.amazonaws.com|helloWorldWalkthrough|70000"
 *  -lcfg "cf1" -ef "L|arn:aws:lambda:us-east-1:123456789012:function:echocool|somecontext|0 0-59 * * * *|3|24.07.2015T14:34:00-0700|24.07.2015T14:36:00-0700"
 *  
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 */

public class DikshaCli {
	public static void main(String[] args) {

		CommandLine commandLine;

		Options options = new Options();
		CommandLineParser parser = new GnuParser();



		options.addOption(Option.builder("adminitsps").longOpt("admin-init-security-policies")
				.desc("init security policies <acct-nbr>").hasArg().build());

		

		options.addOption(Option.builder("adminit").longOpt("admin-init")
				.desc("shortcut to creating necessary domain, dynamodb tables, config and policies").build());
		options.addOption(Option.builder("admclean").longOpt("admin-clean")
				.desc("HARD delete of domain, dynamodb tables... policies need to be manually deleted").build());

		options.addOption(Option.builder("admcd").longOpt("admin-create-domain").desc("create domain").hasArgs()
				.valueSeparator('|').build());

		options.addOption(Option.builder("admdd").longOpt("admin-deprecate-domain").desc("deprecate domain").hasArgs()
				.valueSeparator('|').build());

		options.addOption(Option.builder("admcdt").longOpt("admin-create-dynano-tables")
				.desc("helper to create dynamotables").hasArgs().valueSeparator('|').build());

		options.addOption(Option.builder("admddt").longOpt("admin-delete-dynano-tables")
				.desc("helper to delete dynamotables").hasArgs().valueSeparator('|').build());

		options.addOption(Option.builder("admcspa").longOpt("admin-create-security-policy-admin")
				.desc("create security policy admin").hasArg().build());

		options.addOption(Option.builder("admdspa").longOpt("admin-delete-security-policy-admin")
				.desc("delete security policy admin").hasArg().build());

		options.addOption(Option.builder("admcspd").longOpt("admin-create-security-policy-designer")
				.desc("create security policy designer").hasArg().build());

		options.addOption(Option.builder("admdspd").longOpt("admin-delete-security-policy-designer")
				.desc("delete security policy designer").hasArg().build());

		options.addOption(Option.builder("admcspu").longOpt("admin-create-security-policy-user")
				.desc("create security policy user").hasArg().build());

		options.addOption(Option.builder("admdspu").longOpt("admin-delete-security-policy-user")
				.desc("delete security policy user").hasArg().build());

		options.addOption(Option.builder("admcspw").longOpt("admin-create-security-policy-workflow")
				.desc("create security policy workflow").hasArg().build());

		options.addOption(Option.builder("admdspw").longOpt("admin-delete-security-policy-workflow")
				.desc("delete security policy workflow").hasArg().build());

		options.addOption(Option.builder("admcsga").longOpt("admin-create-security-group-admin")
				.desc("create security group admin").hasArg().build());

		options.addOption(Option.builder("admdsga").longOpt("admin-delete-security-group-admin")
				.desc("delete security group admin").hasArg().build());

		options.addOption(Option.builder("admcsgd").longOpt("admin-create-security-group-designer")
				.desc("create security group designer").hasArg().build());

		options.addOption(Option.builder("admdsgd").longOpt("admin-delete-security-group-designer")
				.desc("delete security group designer").hasArg().build());

		options.addOption(Option.builder("admcsgu").longOpt("admin-create-security-group-user")
				.desc("create security group user").hasArg().build());

		options.addOption(Option.builder("admdsgu").longOpt("admin-delete-security-group-user")
				.desc("delete security group user").hasArg().build());

		options.addOption(Option.builder("admcsgw").longOpt("admin-create-security-group-workflow")
				.desc("create security group workflow").hasArg().build());

		options.addOption(Option.builder("admdsgw").longOpt("admin-delete-security-group-workflow")
				.desc("delete security group workflow").hasArg().build());

		options.addOption(Option.builder("cf").longOpt("create-function")
				.desc("helper to create a refernce to a function in scheduler  -cf cool|L|arn:aws:lambda:us-east-1:709574751419:function:echocool")
				.hasArgs().valueSeparator('|').build());

		options.addOption(Option.builder("df").longOpt("delete-function")
				.desc("helper to delete the refernce to a function created by -cf option in scheduler").hasArg()
				.build());

		options.addOption(Option.builder("lfs").longOpt("list-functions")
				.desc("helper to list all the refeences to  functions created by -cf option in scheduler").build());

		options.addOption(Option.builder("ef").longOpt("execute-function")
				.desc("option to execute a raw function in scheduler  -lcfg cf1 -ef L|arn:aws:lambda:us-east-1:123456789012:function:echocool|somecontext|0 0-59 * * * *|3|24.07.2015T14:34:00-0700|24.07.2015T14:36:00-0700")
				.hasArgs().valueSeparator('|').build());

		// configid|endPoint|domain|socketTimeout|taskList
		// config1|https://swf.us-east-1.amazonaws.com|helloWorldWalkthrough|70000|HelloWorldList
		options.addOption(Option.builder("ccfg").longOpt("create-config")
				.desc("create a configurration in scheduler...config1|https://swf.us-east-1.amazonaws.com|helloWorldWalkthrough|70000|HelloWorldList")
				.hasArgs().valueSeparator('|').build());

		options.addOption(Option.builder("lcfg").longOpt("load-config").desc("load a configurration in scheduler.")
				.hasArgs().valueSeparator('|').build());

		options.addOption(Option.builder("cj").longOpt("create-job")
				.desc("create a refernce to a job in scheduler --- -cj runcooljobeverymin|cool|contextmin|0 0-59 * * * *|2")
				.hasArgs().valueSeparator('|').build());

		options.addOption(Option.builder("dj").longOpt("delete-job")
				.desc("delete a refernce to a job created by -cj option in scheduler").hasArg().build());

		options.addOption(Option.builder("ljs").longOpt("list-jobs")
				.desc("list all the jobs loaded in the schedule for the job to run in scheduler").build());

		options.addOption(Option.builder("ej").longOpt("execute-job")
				.desc("execute the schedule for the job to run in scheduler").hasArg().build());

		options.addOption(
				Option.builder("laes").longOpt("list-active-jobs").desc("list active  jobs  in scheduler").build());

		options.addOption(Option.builder("eid").longOpt("execution-id").desc("execution id if known").hasArg().build());

		options.addOption(Option.builder("rcon").longOpt("reconcile")
				.desc("reconcile active executions in Scheduler to what is reported in AWS scheduler").build());

		options.addOption(Option.builder("rrep").longOpt("reconcile-report")
				.desc("reconcile report on open executions in Scheduler to what is reported in AWS scheduler").build());

		options.addOption(Option.builder("rfix").longOpt("reconcile-fix")
				.desc("reconcile fix executions in Scheduler to what is reported in AWS scheduler").build());

		options.addOption(Option.builder("lse").longOpt("list-status-execution")
				.desc("list status for specifed exection to run in scheduler").hasArg().build());

		options.addOption(Option.builder("cane").longOpt("cancel-execution")
				.desc("option to cancel an existing execution in scheduler args: clientId|reason").hasArgs()
				.valueSeparator('|').build());

		options.addOption(Option.builder("h").longOpt("help").desc("help").build());

		String header = "The commandline client for Diksha - AWS Lambda Scheduler\n\n";
		String footer = "\nPlease report issues at https://github.com/milindparikh/diksha/issues \n";

		try {
			commandLine = parser.parse(options, args);

			if (commandLine.hasOption("adminit")) {
				init();
			}

			if (commandLine.hasOption("adminitsps")) {
			    init_security_policies(commandLine.getOptionValue("adminitsps"));
			    
			}

			if (commandLine.hasOption("admcdt")) {

				String[] tables = commandLine.getOptionValues("admcdt");
				DyDBUtils.createDynamoTables(tables);
			}

			if (commandLine.hasOption("admddt")) {

				String[] tables = commandLine.getOptionValues("admddt");
				DyDBUtils.deleteDynamoTables(tables);
			}

			if (commandLine.hasOption("admcd")) {

				String[] domainArgs = commandLine.getOptionValues("admcd");
				SWFUtils.createDomain(domainArgs);
			}
			if (commandLine.hasOption("admdd")) {

				String domainName = commandLine.getOptionValue("admdd");
				SWFUtils.deprecateDomain(domainName);
			}

			if (commandLine.hasOption("cf")) {

				String[] functionArgs = commandLine.getOptionValues("cf");
				DyDBUtils.createFunction(functionArgs);
			}

			if (commandLine.hasOption("lfs")) {

				DyDBUtils.listFunctions();
			}

			if (commandLine.hasOption("df")) {

				String functionAlias = commandLine.getOptionValue("df");
				DyDBUtils.deleteFunction(functionAlias);
			}

			if (commandLine.hasOption("ccfg")) {
				DyDBUtils.createSchedulerConfig(commandLine.getOptionValues("ccfg"));

			}

			if (commandLine.hasOption("rcon")) {

				DyDBUtils.reconcile(commandLine.getOptionValue("lcfg"));
			}

			if (commandLine.hasOption("rrep")) {

				DyDBUtils.reconcileReport(commandLine.getOptionValue("lcfg"));
			}

			if (commandLine.hasOption("rfix")) {

				DyDBUtils.reconcileFix(commandLine.getOptionValue("lcfg"));
			}

			if (commandLine.hasOption("lse")) {

				DyDBUtils.listStatusExecution(commandLine.getOptionValue("lcfg"), commandLine.getOptionValue("lse"));
			}
			if (commandLine.hasOption("cane")) {

				DyDBUtils.cancelExecution(commandLine.getOptionValue("lcfg"), commandLine.getOptionValues("cane"));

			}

			if (commandLine.hasOption("ef")) {
				DyDBUtils.executeFunction(commandLine.getOptionValue("lcfg"), commandLine.getOptionValues("ef"));

			}

			if (commandLine.hasOption("cj")) {

				String[] jobArgs = commandLine.getOptionValues("cj");
				DyDBUtils.createJob(jobArgs);
			}

			if (commandLine.hasOption("dj")) {

				String jobName = commandLine.getOptionValue("dj");
				DyDBUtils.deleteJob(jobName);
			}

			if (commandLine.hasOption("ljs")) {
				DyDBUtils.listJobs();
			}

			if (commandLine.hasOption("ej")) {

				String jobName = commandLine.getOptionValue("ej");
				DyDBUtils.executeJob(commandLine.getOptionValue("lcfg"), jobName);
			}

			if (commandLine.hasOption("laes")) {

				DyDBUtils.listActiveJobs(commandLine.getOptionValue("eid"));
			}

			if (commandLine.hasOption("admcspa")) {

				IAMUtils.createSecurityPolicyAdmin(commandLine.getOptionValue("admcspa"));
			}

			if (commandLine.hasOption("admdspa")) {

				IAMUtils.deleteSecurityPolicyAdmin(commandLine.getOptionValue("admdspa"));
			}

			if (commandLine.hasOption("admcsga")) {

				String[] args1 = commandLine.getOptionValue("admcsga").split(",");
				String groupName = args1[0];
				String policyName = args1[1];

				IAMUtils.createSecurityGroup(groupName, policyName);

			}

			if (commandLine.hasOption("admdsga")) {

				IAMUtils.deleteSecurityGroup(commandLine.getOptionValue("admdsga"));
			}

			if (commandLine.hasOption("admcspd")) {

				IAMUtils.createSecurityPolicyDesigner(commandLine.getOptionValue("admcspd"));
			}

			if (commandLine.hasOption("admdspd")) {

				IAMUtils.deleteSecurityPolicyDesigner(commandLine.getOptionValue("admdspd"));
			}

			if (commandLine.hasOption("admcsgd")) {

				String[] args1 = commandLine.getOptionValue("admcsgd").split(",");
				String groupName = args1[0];
				String policyName = args1[1];

				IAMUtils.createSecurityGroup(groupName, policyName);

			}

			if (commandLine.hasOption("admdsgd")) {

				IAMUtils.deleteSecurityGroup(commandLine.getOptionValue("admdsgd"));
			}

			if (commandLine.hasOption("admcspu")) {

				IAMUtils.createSecurityPolicyUser(commandLine.getOptionValue("admcspu"));
			}

			if (commandLine.hasOption("admdspu")) {

				IAMUtils.deleteSecurityPolicyUser(commandLine.getOptionValue("admdspu"));
			}

			if (commandLine.hasOption("admcsgu")) {

				String[] args1 = commandLine.getOptionValue("admcsgu").split(",");
				String groupName = args1[0];
				String policyName = args1[1];

				IAMUtils.createSecurityGroup(groupName, policyName);

			}

			if (commandLine.hasOption("admdsgu")) {

				IAMUtils.deleteSecurityGroup(commandLine.getOptionValue("admdsgu"));
			}

			if (commandLine.hasOption("admcspw")) {

				IAMUtils.createSecurityPolicyWorkflow(commandLine.getOptionValue("admcspw"));
			}

			if (commandLine.hasOption("admdspw")) {

				IAMUtils.deleteSecurityPolicyWorkflow(commandLine.getOptionValue("admdspw"));
			}

			if (commandLine.hasOption("admcsgw")) {

				String[] args1 = commandLine.getOptionValue("admcsgw").split(",");
				String groupName = args1[0];
				String policyName = args1[1];

				IAMUtils.createSecurityGroup(groupName, policyName);

			}

			if (commandLine.hasOption("admdsgw")) {

				IAMUtils.deleteSecurityGroup(commandLine.getOptionValue("admdsgw"));
			}

			if (commandLine.hasOption("h")) {
				HelpFormatter formatter = new HelpFormatter();
				formatter.setWidth(100);
				formatter.printHelp("Diksha - AWS Lambda Scheduler", header, options, footer, true);
			}
			
			

		} catch (ParseException exception) {

			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("Diksha - AWS Lambda Scheduler", header, options, footer, true);
		}
	}

    private static void init_security_policies (String awsAcct) {
	IAMUtils.createSecurityPolicyAdmin("dikshaAdminPolicy"+","+awsAcct + "," + "dikshaDomain");
	
	IAMUtils.createSecurityPolicyDesigner("dikshaDesignerPolicy"+","+awsAcct + "," + "dikshaDomain");
	IAMUtils.createSecurityPolicyUser("dikshaUserPolicy"+","+awsAcct + "," + "dikshaDomain");
	IAMUtils.createSecurityPolicyWorkflow("dikshaWorkflowPolicy"+","+awsAcct + "," + "dikshaDomain");
	
    }


    private static void init() {
	String[] createDomainArgs = { "dikshaDomain", "domain generated by init", "1" };
	SWFUtils.createDomain(createDomainArgs);
	
		DynamoDB dynamoDB = DyDBUtils.getDynamoDB();

		String createDynamoTableUDFArgs = "SchedulerUDF,1,1,functionAlias,S,,";
		String createDynamoTableUDJArgs = "SchedulerUDJ,1,1,jobName,S,,";
		String createDynamoTableUDEArgs = "SchedulerUDE,1,1,executionId,S,,";
		String createDynamoTableConfigArgs = "SchedulerConfig,1,1,configId,S,,";
		String createDynamoTableWorkflowStateArgs = "SchedulerWorkflowState,1,1,clientId,S,loopState,G:S:1:1";

		SchedulerDynamoTable schedulerDynamoTable = new SchedulerDynamoTable(createDynamoTableUDFArgs);
		schedulerDynamoTable.createDynamoTable(dynamoDB);

		schedulerDynamoTable = new SchedulerDynamoTable(createDynamoTableUDJArgs);
		schedulerDynamoTable.createDynamoTable(dynamoDB);

		schedulerDynamoTable = new SchedulerDynamoTable(createDynamoTableUDEArgs);
		schedulerDynamoTable.createDynamoTable(dynamoDB);

		schedulerDynamoTable = new SchedulerDynamoTable(createDynamoTableConfigArgs);
		schedulerDynamoTable.createDynamoTable(dynamoDB);

		schedulerDynamoTable = new SchedulerDynamoTable(createDynamoTableWorkflowStateArgs);
		schedulerDynamoTable.createDynamoTable(dynamoDB);

		String[] createSchedulerConfigArgs = { "cf1", "https://swf.us-east-1.amazonaws.com", "dikshaDomain", "70000",
				"dikshaList" };
		DyDBUtils.createSchedulerConfig(createSchedulerConfigArgs);


	}
 
}
