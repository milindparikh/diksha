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

import java.util.Date;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "SchedulerWorkflowState")
public class SchedulerWorkflowState {

	public String loopState;
	public int loopCount;
	public Date lastExecutionTimeDate;
	public Date lastProposedTimeDate;
	public Date workflowStartedDate;
	public Date workflowLastUpdatedDate;
	
	public String jobName;
	public String cronExpression;
	public int repeatTimes;
	public Date startTimeDate;
	public Date endTimeDate;
	public String timeZone;
	@DynamoDBHashKey
	public String clientId;
	

		
		public SchedulerWorkflowState(String jobName, 
										String cronExpression, 
										int repeatTimes, 
										Date startTimeDate,
										Date endTimeDate,
										String timeZone, 
										String clientId,
										Date workflowStartedDate) {
		
		loopState = new String();
		loopCount = 0;
		this.lastExecutionTimeDate = new Date(0L);
		this.lastProposedTimeDate = new Date(0L);
		this.workflowLastUpdatedDate = new Date(0L);
		
		
		this.timeZone = timeZone;
		this.jobName = jobName;
		
		this.startTimeDate = startTimeDate;
		this.endTimeDate = endTimeDate;
		this.repeatTimes = repeatTimes;
		this.cronExpression = cronExpression;
		this.clientId = clientId;
		this.workflowStartedDate = workflowStartedDate;

	}
	
	public Date getWorkflowStartedDate () {
		return workflowStartedDate;
	}

	public void setWorkflowStartedDate (Date workflowStartedDate) {
		this.workflowStartedDate = workflowStartedDate;
	}
	
	public Date getWorkflowLastUpdatedDate () {
		return workflowLastUpdatedDate;
	}

	public void  setWorkflowLastUpdatedDate(Date workflowLastUpdatedDate) {
		this.workflowLastUpdatedDate = workflowLastUpdatedDate;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public SchedulerWorkflowState() {
		loopState = new String();
		loopCount = 0;
		timeZone = new String ("America/Los_Angeles");
	}
	
	public SchedulerWorkflowState (SchedulerWorkflowState schedulerWorkflowState) {
		this.loopState = schedulerWorkflowState.loopState;
		this.loopCount = schedulerWorkflowState.loopCount;
		this.startTimeDate = schedulerWorkflowState.startTimeDate;
		this.endTimeDate = schedulerWorkflowState.endTimeDate;
		this.lastExecutionTimeDate = schedulerWorkflowState.lastExecutionTimeDate;
		this.repeatTimes = schedulerWorkflowState.repeatTimes;
		this.cronExpression = schedulerWorkflowState.cronExpression;
		this.timeZone = schedulerWorkflowState.timeZone;
		this.lastProposedTimeDate = schedulerWorkflowState.lastProposedTimeDate;
		this.clientId = schedulerWorkflowState.clientId;
		
		this.workflowStartedDate = schedulerWorkflowState.workflowStartedDate;
		this.workflowLastUpdatedDate = schedulerWorkflowState.workflowLastUpdatedDate;
		this.jobName = schedulerWorkflowState.jobName;
		
	}
	
	
	
	
	
	public String getLoopState() {
		return loopState;
	}
	public void setLoopState(String loopState) {
		this.loopState = loopState;
	
	}	

	public int getLoopCount() {
		return loopCount;
	}
	public void setLoopCount(int loopCount) {
		this.loopCount = loopCount;
	}
	
    public Date getLastExecutionTimeDate() {
	return lastExecutionTimeDate;
    }
    public void setLastExecutionTimeDate(Date lastExecutionTimeDate) {
	this.lastExecutionTimeDate = lastExecutionTimeDate;
    }

    public Date getLastProposedTimeDate() {
    	return lastProposedTimeDate;
    }
    public void setLastProposedTimeDate(Date lastProposedTimeDate) {
    	this.lastProposedTimeDate = lastProposedTimeDate;
    }

    
	
	
	
    public Date getStartTimeDate() {
	return startTimeDate;
    }
    public void setStartTimeDate(Date startTimeDate) {
	this.startTimeDate = startTimeDate;
    }


    public Date getEndTimeDate() {
	return endTimeDate;
    }
    public void setEndTimeDate(Date endTimeDate) {
	this.endTimeDate = endTimeDate;
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

    
    public String getTimeZone() {
	return timeZone;
    }
    public void setTimeZone(String timeZone) {
	this.timeZone = timeZone;
    }

	

	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	
	
	
	
	
	
}
