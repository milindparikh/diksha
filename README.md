## Diksha:  AWS Lambda Scheduler
#### Currently in Alpha stage. Do not use in production environments.
[![Build Status](https://travis-ci.org/milindparikh/diksha.png)](https://travis-ci.org/milindparikh/diksha)

Diksha is is scalable scheduler that can be used to scheduler [AWS lambda](https://aws.amazon.com/lambda/) functions.

#### Key features:
 * Use [cron expressions](https://en.wikipedia.org/wiki/Cron#CRON_expression) to schedule functions.
 * Can also schedule function to run number of times, start or end at particular time.
 * Uses [AWS IAM](https://aws.amazon.com/iam/) Policy.
 * Uses [AWS SWF](https://aws.amazon.com/swf/) as reccommended by [AWS Team](https://aws.amazon.com/about-aws/whats-new/2015/08/trigger-aws-lambda-functions-using-amazon-simple-workflow/) to trigger function
 * Scalable and elastic.
 * Uses [AWS DynamoDB](https://aws.amazon.com/dynamodb/) to store history
 * It is command line driven
 * It just requires java 1.7

#### Components:
 * Diksha Client - It is used to:
    * Configure settings in AWS SWF and DynamoDB
    * Create IAM policy for different diksha roles  -
       * Admin - Creates intial configuration (DynamoDB tables, policies, and SWF Domain)
       * Designer - Creates function alias and jobs
       * User - Executes jobs, list status of job, view history of job and cancels job
     * Create function alias, create, execute and cancel jobs
     * List status and history of job execution
     * Help for all available option are available in -h option
 * Diksha Engine - You can use diksha engine anywhere and it will trigger AWS Lambda function as per scheduled jobs. You just need to specify configuration (default configuration is "cf1") and it will pick jobs as per their schedule.

####  Getting Started
Step 1: Make sure AWS Account that you are going to use has following policy (Update account number with your own account number)
```
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": "iam:*",
      "Effect": "Allow",
      "Resource": "arn:aws:iam::YOURACCOUNTNUMBER"
    },
    {
      "Action": [
        "lambda:InvokeFunction"
      ],
      "Effect": "Allow",
      "Resource": "arn:aws:lambda:us-east-1:YOURACCOUNTNUMBER:function:*"
    },
    {
      "Action": "swf:*",
      "Effect": "Allow",
      "Resource": "*"
    },
    {
      "Action": "logs:*",
      "Effect": "Allow",
      "Resource": "*"
    }
  ]
}
```
Step 2: Set AWS credentials as environment variable.
```
export AWS_ACCESS_KEY_ID=YOURKEYID
export AWS_SECRET_ACCESS_KEY=YOURKEY
```
Step 3: Initialize client. Either build your own diksha-client jar or you can download [latest release](https://github.com/milindparikh/diksha/releases/download/diksha-0.0.1/diksha-client-0.0.1.jar). Here we are using default configuration, but diksha client also lets you configure as per your need, check all available options using -h option. 
```
java -jar scheduler-client-0.0.1.jar -adminit
```
Step 4: Run diksha engine. Either build your own diksha-engine jar or you can download [latest release](https://github.com/milindparikh/diksha/releases/download/diksha-0.0.1/diksha-engine-0.0.1.jar). You can run this either on local machine or any EC2 instance. By default it will use configuration "cf1". If you had user defined configuration you can use by specifying -lcfg your configuration
```
java -jar diksha-engine-0.0.1.jar 
```

You are all set to go.

### Example

#### Create function alias
Create function alias for existing Lambda ARN. cf1 is default configuration, can use your own configuration if defined initially.
```
 java -jar diksha-client-0.0.1.jar -lcfg cf1 -cf "functionAlias|L|arn:aws:lambda:us-east-1:YOURACCOUNT:function:functionName"
```

#### Create Job that runs every minute for 10 times
This will just define job, it wil be executed by -ej option.
```
java -jar diksha-client-0.0.1.jar -lcfg cf1 -cj "jobName|functionName|context|0 0-59 * * * *|10"
```

#### Create Job that runs every minute for 100 times and starts at 15-Aug-2015 and ends at 16-Aug-2015
Between number of times and end time, one that is reached earlier takes precedence. Time format is  dd.MM.yyyy'T'HH:mm:ssZ. This will just define job, it wil be executed by -ej option.

```
java -jar diksha-client-0.0.1.jar -lcfg cf1 -cj "jobName|functionName|context|0 0-59 * * * *|100|15.08.2015T12:00:00-000|16.08.2015T12:00:00-000"
```
#### Execute job
This will return execution id for job. We will use execution id for listing status, history or canceling job.
```
java -jar diksha-client-0.0.1.jar -lcfg cf1 -ej jobName
```
#### List job status and history
```
java -jar diksha-client-0.0.1.jar -lcfg -cf1 -lse executionId
```
#### Cancel job
```
java -jar diksha-client-0.0.1.jar -lcfg -cf1 -cane "executionId|reason"
```
#### TODO
- [ ] User docs explaining all options
- [ ] Running Diksha as service
- [ ] Create AMI for Diksha Engine
- [ ] Rest based API
- [ ] Ability to configure region


