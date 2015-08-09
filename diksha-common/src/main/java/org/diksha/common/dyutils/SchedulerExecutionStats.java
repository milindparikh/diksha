package org.diksha.common.dyutils;

import java.util.Date;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "SchedulerExecutionStats")

public class SchedulerExecutionStats {
	    @DynamoDBHashKey
		private String executionId;
		private int loopCount;
		private Date lastExecutionDate;
		private Date currentExecutionDate;
		private Date nextExecutionDate;
		private String loopState;
		
		public SchedulerExecutionStats() {
		
		}
		public SchedulerExecutionStats(
				String executionId,
				int loopCount,
				Date lastExecutionDate,
				Date currentExecutionDate,
				Date nextExecutionDate,
				String loopState
				
				) {
			
			this.executionId = executionId;
			this.loopCount = loopCount;
			this.lastExecutionDate = lastExecutionDate;
			this.currentExecutionDate = currentExecutionDate;
			this.nextExecutionDate = nextExecutionDate;
			this.loopState = loopState;
		}
		
		
		public String getExecutionId() {
			return executionId;
		}
		public void setExecutionId(String executionId) {
			this.executionId = executionId;
		}
		
		public int getLoopCount() {
			return loopCount;
		}
		public void setLoopCount(int loopCount) {
			this.loopCount = loopCount;
		}
		public Date getLastExecutionDate() {
			return lastExecutionDate;
			
		}
		public void setLastExecutionDate(Date lastExecutionDate) {
			this.lastExecutionDate = lastExecutionDate;
		}

		public Date getCurrentExecutionDate() {
			return currentExecutionDate;
			
		}
		public void setCurrentExecutionDate(Date currentExecutionDate) {
			this.currentExecutionDate = currentExecutionDate;
		}

		public Date getNextExecutionDate() {
			return nextExecutionDate;
			
		}
		public void setNextExecutionDate(Date nextExecutionDate) {
			this.nextExecutionDate = nextExecutionDate;
		}
		public String getLoopState() {
				return loopState;
		}
		public void setLoopState(String loopState) {
			this.loopState = loopState;
			
		}

}