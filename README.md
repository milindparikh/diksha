## Diksha:  AWS Lambda Scheduler

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
    * Create policy for different diksha roles  -
       * Admin - Creates intial configuration (DynamoDB tables, policies, and SWF Domain)
       * Designer - Creates function alias and jobs
       * User - Executes jobs, list status of job, view history of job and cancels job
     * Create function alias, create, execute and cancel jobs
     * List status and history of job execution
* Diksha Engine - You can use diksha engine anywhere and it will trigger AWS Lambda function as per scheduled jobs. You just need to specify configuration (default configuration is "cf1") and it will pick jobs as per their schedule.

####  Getting Started
### Examples of Usage




#### TODO
- [ ] Rest based API
- [ ] Ability to configure region
- [ ] User docs explaining all options
- [ ] Running Diksha as service
- [ ] Create AMI for Diksha Engine



