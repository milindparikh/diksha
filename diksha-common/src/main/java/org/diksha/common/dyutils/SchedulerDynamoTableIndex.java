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

public class SchedulerDynamoTableIndex {

	

    public  String localOrGlobal;
    public String indexName;
    public  long readCapacityUnits = 1L;
    public long writeCapacityUnits = 1L;
    public String hashKey;
    public String rangeKey;
    public String projectionType;  // INCLUDE, KEYS_ONLY, ALL
    public  ArrayList<String> projectionTypeNonKeyAttributes =  new ArrayList<String>();

    public SchedulerDynamoTableIndex(
	 			    String localOrGlobal, 
				    String indexName,
				    long readCapacityUnits,
				    long writeCapacityUnits,
				    String hashKey,
				    String rangeKey,
				    String projectionType,
				    ArrayList<String> projectionTypeNonKeyAttributes ) {
	
	this.localOrGlobal  = localOrGlobal;
 	this.indexName = indexName;
	this.readCapacityUnits = readCapacityUnits;
	
	this.writeCapacityUnits = writeCapacityUnits;
	this.hashKey = hashKey;
	this.rangeKey = rangeKey;
	
	this.projectionType = projectionType;
	this.projectionTypeNonKeyAttributes = projectionTypeNonKeyAttributes;
	
    }

    public boolean isGlobal() {
	if (localOrGlobal.equals("G") ) {
	    return true;
	}
	else {
	    return false;
	}
    }
    
    public GlobalSecondaryIndex getGlobalSecondaryIndex() {

	Projection	    projection ;
	
	if (projectionType.equals("INCLUDE") && (projectionTypeNonKeyAttributes != null)) {
            projection = new Projection()
                .withProjectionType(projectionType)
		.withNonKeyAttributes(projectionTypeNonKeyAttributes.toArray(new String[projectionTypeNonKeyAttributes.size()] ));;
	}
	else {
	    System.out.println(projectionType);
	    projection = new Projection()
                .withProjectionType(projectionType);
	}

	
	GlobalSecondaryIndex index = new GlobalSecondaryIndex()
            .withIndexName(indexName)
            .withProvisionedThroughput(new ProvisionedThroughput()
				       .withReadCapacityUnits(readCapacityUnits)
				       .withWriteCapacityUnits(writeCapacityUnits))
            .withKeySchema(new KeySchemaElement() 
                .withAttributeName(hashKey) 
                .withKeyType(KeyType.HASH), 
                    new KeySchemaElement() 
                        .withAttributeName(rangeKey) 
			   .withKeyType(KeyType.RANGE))
	    .withProjection(projection);
	return index;
    }

	
	
	
	
}
