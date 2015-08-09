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

import java.util.ArrayList;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.GlobalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.LocalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProjectionType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

public class SchedulerDynamoTable  {
    public String tableName;
    public long readCapacityUnits = 1L;
    public long writeCapacityUnits = 1L;
    public String hashKeyName;
    public String hashKeyType;
    public String rangeKeyName;
    public String rangeKeyType;
    public ArrayList <String> additionalAttributes = new ArrayList <String>();
    public String rangeKeyTypeGlobalOrLocal;
    public long indexReadCapacityUnits;
    public long indexWriteCapacityUnits;
    public ArrayList <SchedulerDynamoTableIndex> indices = new ArrayList <SchedulerDynamoTableIndex>() ;
    
                                          
    // "tableName,1,1,hashKeyName,hashKeyType,rangeKeyName,{G|L}:rangeKeyType:readcap:writecap"
    public SchedulerDynamoTable(String arg) {
	String [] args = arg.split(",");


	int cnt = 0;
	tableName = args[cnt++];
	
	if (!args[cnt].isEmpty()) {
	    readCapacityUnits = (long) Integer.parseInt(args[cnt]);
	}
	cnt++;

	if (!args[cnt].isEmpty()) {
	    writeCapacityUnits = (long) Integer.parseInt(args[cnt]);
	}
	cnt++;

	hashKeyName = args[cnt++];
	hashKeyType = args[cnt++];
	
	if (args.length >= 6) {
	    rangeKeyName = args[cnt++];
	}

	if (args.length >= 7) {
	    String tmpString = args[cnt++];
	    String [] tmpArgs = tmpString.split(":");
	    
	    rangeKeyTypeGlobalOrLocal = tmpArgs[0];
	    
	    rangeKeyType = tmpArgs[1] ;
	    
	    if (rangeKeyTypeGlobalOrLocal.equals("G")) {
		indexReadCapacityUnits = (long) Integer.parseInt(tmpArgs[2]);
		indexWriteCapacityUnits = (long) Integer.parseInt(tmpArgs[3]);
	    }

	}





	if (tableName.equals("SchedulerWorkflowState")) {
	    

	    // additionalAttributes.add("loopCount:S");
	    //	    additionalAttributes.add("lastExecutionTimeDate:S"); 
	    //	    additionalAttributes.add("lastProposedTimeDate:S");
	    ArrayList<String> nonKeyAttributes = new ArrayList<String>();
	    nonKeyAttributes.add("loopCount");
	    
	    indices.add(new SchedulerDynamoTableIndex("G", "loopStateIndex", 1L, 1L, "loopState", "clientId", "ALL", nonKeyAttributes));
	}
	    
	
    }



    public String toString() {
	return new String(
			  "tableName: " + tableName + "\n" + 
			  "readCapacityUnits: " + readCapacityUnits + "\n" + 
			  "writeCapacityUnits: " + writeCapacityUnits + "\n" + 
			  "hashKeyName: " + hashKeyName + "\n" + 
			  "hashKeyType: " + hashKeyType + "\n" + 
			  "rangeKeyName: " + rangeKeyName + "\n" + 
			  "rangeKeyType: " + rangeKeyType + "\n" + 
			  "rangeKeyTypeGlobalOrLocal: " + rangeKeyTypeGlobalOrLocal + 
			  "indexReadCapacityUnits: " + indexReadCapacityUnits  + 
			  "indexWriteCapacityUnits: " + indexWriteCapacityUnits 
			  );
    }


    public void createDynamoTable(DynamoDB dynamoDB) {

        try {
	    System.out.println(toString());
	    
            ArrayList<KeySchemaElement> keySchema = new ArrayList<KeySchemaElement>();
            keySchema.add(new KeySchemaElement()
                .withAttributeName(hashKeyName)
                .withKeyType(KeyType.HASH));
            
            ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
            attributeDefinitions.add(new AttributeDefinition()
				     .withAttributeName(hashKeyName)
				     .withAttributeType(hashKeyType));
	    if (rangeKeyType != null) {
	    	if (!rangeKeyType.isEmpty()) {
		    attributeDefinitions.add(new AttributeDefinition()
					     .withAttributeName(rangeKeyName)
					     .withAttributeType(rangeKeyType));
		}
	    }
	    
	    if (additionalAttributes.size() > 0 ) {
		for (int cnt = 0; cnt < additionalAttributes.size(); cnt++) {
		    String [] args = (additionalAttributes.get(cnt).toString()).split(":");
		    if (args.length == 2) {
			attributeDefinitions.add(new AttributeDefinition()
					     .withAttributeName(args[0])
					     .withAttributeType(args[1]));
			
		    }
		    
		}
	    }
		    
	    

            CreateTableRequest request = new CreateTableRequest()
                    .withTableName(tableName)
                    .withKeySchema(keySchema)
                    .withProvisionedThroughput( new ProvisionedThroughput()
                        .withReadCapacityUnits(readCapacityUnits)
                        .withWriteCapacityUnits(writeCapacityUnits));

	    ArrayList<LocalSecondaryIndex> localSecondaryIndexes = new ArrayList<LocalSecondaryIndex>();
	    ArrayList<GlobalSecondaryIndex> globalSecondaryIndexes = new ArrayList<GlobalSecondaryIndex>();


	    if (rangeKeyType != null) {
	    	if (!rangeKeyType.isEmpty()) {
 	    		if (rangeKeyTypeGlobalOrLocal.equals("L")) {
			    localSecondaryIndexes.add(new LocalSecondaryIndex()
						      .withIndexName( rangeKeyName   + "-Index")
						      .withKeySchema(
								     new KeySchemaElement().withAttributeName(hashKeyName).withKeyType(KeyType.HASH), 
								     new KeySchemaElement() .withAttributeName(rangeKeyName) .withKeyType(KeyType.RANGE))
						      .withProjection(new Projection() .withProjectionType(ProjectionType.KEYS_ONLY)));
	    		}
	    		else {
			    globalSecondaryIndexes.add(new GlobalSecondaryIndex()
						       .withIndexName( rangeKeyName   + "-Index")
						       .withKeySchema(
								      new KeySchemaElement().withAttributeName(hashKeyName).withKeyType(KeyType.HASH), 
								      new KeySchemaElement() .withAttributeName(rangeKeyName).withKeyType(KeyType.RANGE))
						       .withProvisionedThroughput( new ProvisionedThroughput()
										   .withReadCapacityUnits(indexReadCapacityUnits)
										       .withWriteCapacityUnits(indexWriteCapacityUnits))
						       .withProjection(new Projection() .withProjectionType(ProjectionType.KEYS_ONLY)));
	    		}
	    	}
	    }




	    if (indices.size() > 0) {

		for (int cnt = 0; cnt < indices.size(); cnt++) {
		    SchedulerDynamoTableIndex schedulerDynamoTableIndex = indices.get(cnt);
		    
		    if (schedulerDynamoTableIndex.isGlobal() ) {
			//			System.out.println("IS GLOBAL");
			globalSecondaryIndexes.add(schedulerDynamoTableIndex.getGlobalSecondaryIndex());
			//			System.out.println("size = " + globalSecondaryIndexes.size());
		    }
		    
		}
		
	    }






	    if (localSecondaryIndexes.size() > 0) 
		request.setLocalSecondaryIndexes(localSecondaryIndexes);
	    if (globalSecondaryIndexes.size() > 0) 
		request.setGlobalSecondaryIndexes(globalSecondaryIndexes);
		
	    request.setAttributeDefinitions(attributeDefinitions);
	    System.out.println("Issuing CreateTable request for " + tableName);
	    Table table = dynamoDB.createTable(request);
	    System.out.println("Waiting for " + tableName
	    		+ " to be created...this may take a while...");
	    table.waitForActive();

        } catch (Exception e) {
            System.err.println("CreateTable request failed for " + tableName);
            System.err.println(e.getMessage());
        }

    }



    public  void deleteDynamoTable(DynamoDB dynamoDB) {
        Table table = dynamoDB.getTable(tableName);
        try {
            System.out.println("Issuing DeleteTable request for " + tableName);
            table.delete();
            System.out.println("Waiting for " + tableName
			       + " to be deleted...this may take a while...");
            table.waitForDelete();
	    
        } catch (Exception e) {
            System.err.println("DeleteTable request failed for " + tableName);
            System.err.println(e.getMessage());
        }
    }	
}

