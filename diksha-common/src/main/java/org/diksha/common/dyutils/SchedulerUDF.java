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
@DynamoDBTable(tableName = "SchedulerUDF")
public class SchedulerUDF {

    private String functionAlias;    
    
    private String functionType;
    private String functionName;

    public SchedulerUDF() {
    }
    

    public SchedulerUDF(String functionAlias, String functionType, String functionName) {
	this.functionAlias = functionAlias;
	this.functionType = functionType;
	this.functionName = functionName;
    }
    
    @DynamoDBHashKey
    public String getFunctionAlias() {
	return functionAlias;
    }
    
    public void setFunctionAlias(String functionAlias) {
	this.functionAlias = functionAlias;
    }
    

    public String getFunctionType() {
	return functionType;
    }
    
    public void setFunctionType(String functionType) {
	this.functionType = functionType;
    }


//    @DynamoDBRangeKey
    public String getFunctionName() {
	return functionName;
    }
    
    public void setFunctionName(String functionName) {
	this.functionName = functionName;
    }
    

}
