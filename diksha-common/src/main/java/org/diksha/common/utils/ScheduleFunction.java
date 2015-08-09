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

import java.text.SimpleDateFormat;
import java.text.ParseException;

import java.util.Date;

import org.diksha.common.dyutils.DyDBUtils;
import org.diksha.common.dyutils.SchedulerUDF;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;


public class ScheduleFunction {
	
	 public String functionType;
	 public String functionName;
	 public String functionContext;
	 public String cronExpression = new String("0 * * * * *");

	 public int repeatTimes = 1;
	 public String startTime;
	 public String endTime;	 
     public Date startTimeDate = null;
     public Date endTimeDate = null ;

	 public String timeZone;
	
		    
	 public ScheduleFunction(String [] args) {
		int cnt = 0;
		SimpleDateFormat dateFormat = new SimpleDateFormat ("dd.MM.yyyy'T'HH:mm:ssZ");
		
		
		
		DynamoDBMapper mapper = DyDBUtils.getDynamoDBMapper();
		
		SchedulerUDF udfFunction = mapper.load(SchedulerUDF.class, args[cnt++]);
		
		this.functionType = udfFunction.getFunctionType();
		this.functionName = udfFunction.getFunctionName();
		
		
		
		/*
		this.functionType = new String (args[cnt++]);          // 0
		this.functionName = new String (args[cnt++]);          // 1
*/				
		this.functionContext = new String (args[cnt++]);       // 2
		
		
		if (args.length >= 3) {	   
			if (args[cnt] != "") {
				this.cronExpression = new String (args[cnt]);       // 3
			}
			else {
				this.cronExpression = new String("0 * * * * *");
			}
		}
		cnt++;
		
		
		if (args.length >= 4) {
		    if (args[cnt] != "") {
			this.repeatTimes = Integer.parseInt(args[cnt]);
		    }
		    else {
			this.repeatTimes = 1;
		    }
		}
		cnt++;                                                 //4
		
		if (args.length >= 5) {
		    this.startTime = new String(args[cnt++]);         // 5
		    
		     try {
				 if (! startTime.isEmpty()) {
		    			startTimeDate =  dateFormat.parse(startTime);
					} 	 
			 }
			 catch(ParseException pe) {
				 System.out.println(pe.toString());
			 }
		     
		}
		else {
		    this.startTime = new String("");
		}
			
		if (args.length >= 6) {
		    this.endTime = new String(args[cnt++]);         // 6
		    
		    

		     try {
				 if (! endTime.isEmpty()) {
						endTimeDate = dateFormat.parse(endTime);
					}			  
				 
			 }
			 catch(ParseException pe) {
				 System.out.println(pe.toString());
			 }
		    
		    
		    
		}
		else {
		    this.endTime = new String("");
		}

			
		if (args.length >= 7) {
		    this.timeZone = new String(args[cnt++]);         // 7
		}
		else {
		    this.timeZone = new String("America/Los_Angeles");
		}
    }
}
