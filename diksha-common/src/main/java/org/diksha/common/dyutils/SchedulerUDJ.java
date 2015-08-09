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

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

//jobName|functionAlias|functionContext|cronExpression|repeatTimes|startDate|endDate|timeZone
@DynamoDBTable(tableName = "SchedulerUDJ")

public class SchedulerUDJ {

    private String jobName;
    private String functionAlias;
    private String functionContext;
    private String cronExpression;
    private int repeatTimes;
    private Date startTimeDate = null;
    private Date endTimeDate = null;
    private String timeZone;
    SimpleDateFormat dateFormat = new SimpleDateFormat ("dd.MM.yyyy'T'HH:mm:ssZ");
    
    
    public SchedulerUDJ() {
    }

    public SchedulerUDJ(String [] jobArgs) {
	int cnt = 0;
	this.jobName = jobArgs[cnt++];
	this.functionAlias = jobArgs[cnt++];
	this.functionContext = jobArgs[cnt++];
	this.cronExpression = jobArgs[cnt++];
	
	if (jobArgs.length >= 5) {
	    if(!jobArgs[cnt].isEmpty()) {
		this.repeatTimes = Integer.parseInt(jobArgs[cnt]);
	    }
	    else {
		this.repeatTimes = 1;
	    }
	}
	else {
	    this.repeatTimes = 1;
	}
	cnt++;

	if (jobArgs.length >= 6) {
	    if(!jobArgs[cnt].isEmpty()) {
		String startTime = jobArgs[cnt];
		try {
			 if (! startTime.isEmpty()) {
	    			startTimeDate =  dateFormat.parse(startTime);
				} 	 
		 }
		 catch(ParseException pe) {
			 System.out.println(pe.toString());
		 }	
	    }
	}
	cnt++;

	if (jobArgs.length >= 7) {
	    if(!jobArgs[cnt].isEmpty()) {
		String endTime = jobArgs[cnt];
		try {
			 if (! endTime.isEmpty()) {
					endTimeDate = dateFormat.parse(endTime);
				}			  
			 
		 }
		 catch(ParseException pe) {
			 System.out.println(pe.toString());
		 }
		
		
	    }
	}
	cnt++;

	if (jobArgs.length >= 8) {
	    if(!jobArgs[cnt].isEmpty()) {
	    	this.timeZone = jobArgs[cnt];
	    }	
	}

    }
    public SchedulerUDJ(String jobName, String functionAlias, String functionContext, String cronExpression, int repeatTimes, String startTime, String endTime, String timeZone) {
	this.jobName = jobName;
	this.functionAlias = functionAlias;
	this.functionContext = functionContext;
	this.cronExpression = cronExpression;
	this.repeatTimes = repeatTimes;
	this.timeZone = timeZone;
	
	try {
		 if (! startTime.isEmpty()) {
   			startTimeDate =  dateFormat.parse(startTime);
			} 	 
	 }
	 catch(ParseException pe) {
		 System.out.println(pe.toString());
	 }	
	try {
		 if (! endTime.isEmpty()) {
				endTimeDate = dateFormat.parse(endTime);
			}			  
		 
	 }
	 catch(ParseException pe) {
		 System.out.println(pe.toString());
	 }
	
	}

    @DynamoDBHashKey
    public String getJobName() {
	return jobName;
    }
    public void setJobName(String jobName) {
	this.jobName = jobName;
    }


    public String getFunctionAlias() {
	return functionAlias;
    }
    public void setFunctionAlias(String functionAlias) {
	this.functionAlias = functionAlias;
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
    public void setStartDate(Date startTimeDate) {
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

	
}
