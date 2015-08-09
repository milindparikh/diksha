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

@DynamoDBTable(tableName = "SchedulerConfig")
public class SchedulerConfig {
	
    @DynamoDBHashKey
	private String configId;
	private String endPoint;
	private String domain;
	private String socketTimeout;
	private String taskList;
	
	
	public SchedulerConfig() {
	
	}
	
	public SchedulerConfig(String [] args) {
		configId = args[0];
		endPoint = args[1];
		domain = args[2];
		socketTimeout = args[3];
		taskList = args[4];
			
	}
	
	public String getTaskList() {
		return taskList;
	}
	
	public void setTaskList(String taskList) {
		this.taskList = taskList;
	}
	
	public String getSocketTimeout() {
		return socketTimeout;
	}
	
	public void setSocketTimeout(String socketTimeout) {
		this.socketTimeout = socketTimeout;
	}
	
	public String getConfigId() {
		return configId;
	}
	public void setConfigId(String configId) {
		this.configId = configId;
	}
	
	public String getEndPoint() {
		return endPoint;
	}
	
	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}
	
	public String getDomain() {
			return domain;
	}
	
	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	
	
}
