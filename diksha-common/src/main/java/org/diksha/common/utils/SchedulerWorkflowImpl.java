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

import org.diksha.common.dyutils.DyDBUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.simpleworkflow.flow.DecisionContextProvider;
import com.amazonaws.services.simpleworkflow.flow.DecisionContextProviderImpl;
import com.amazonaws.services.simpleworkflow.flow.WorkflowClock;
import com.amazonaws.services.simpleworkflow.flow.WorkflowExecutionLocal;
import com.amazonaws.services.simpleworkflow.flow.annotations.Asynchronous;
import com.amazonaws.services.simpleworkflow.flow.core.Promise;
import com.amazonaws.services.simpleworkflow.flow.core.TryCatchFinally;
import com.amazonaws.services.simpleworkflow.flow.generic.GenericWorkflowClient;



public class SchedulerWorkflowImpl implements SchedulerWorkflow {

	
	public static WorkflowExecutionLocal<String> staticStatus 
    = new WorkflowExecutionLocal<String>();
	
	private SchedulerWorkflowSelfClient selfClient = new SchedulerWorkflowSelfClientImpl();
	
	private SchedulerActivitiesClient operations = new SchedulerActivitiesClientImpl();
	
	private DecisionContextProvider contextProvider
    = new DecisionContextProviderImpl();
		
	GenericWorkflowClient gwe = contextProvider.getDecisionContext().getWorkflowClient();
	
	

    boolean processingTask = false;
    TryCatchFinally periodicTask = null;
    private SchedulerWorkflowState schedulerWorkflowState;
    
    public static WorkflowExecutionLocal<String> getStaticStatus() {
        return staticStatus;
        
      }

      public static void setStaticStatus(WorkflowExecutionLocal<String> staticStatus) {
    	  SchedulerWorkflowImpl.staticStatus = staticStatus;
      }
    
	@Override
	public void invoke(  
			
   			final SchedulerWorkflowState cpy_schedulerWorkflowState, 
   			final String functionType,	
			final String functionName,
			final String functionContext
			
			
			) {
		
//		System.out.println("in invike...");
		this.schedulerWorkflowState = new SchedulerWorkflowState (cpy_schedulerWorkflowState);
//		System.out.println("IN INVOKE -- 2 statuses---- " + staticStatus.get()   + "     -->   " +  schedulerWorkflowState.loopState );
		
		staticStatus.set(schedulerWorkflowState.loopState);
//		System.out.println("2...IN INVOKE -- 2 statuses---- " + staticStatus.get()   + "     -->   " +  schedulerWorkflowState.loopState );
		
		
		if (staticStatus.get().equals("CANCEL")  ||  staticStatus.get().equals("TERMINATED")) {
			System.out.println("----GOT---- CANCEL or TERMINATED");
			return;
		}
		
		
		persistWorkflowState(this.schedulerWorkflowState);
		//		System.out.println("3...IN INVOKE -- 2 statuses---- " + staticStatus.get()   + "     -->   " +  schedulerWorkflowState.loopState );


		new TryCatchFinally() {
			@Override
			protected void doTry() throws Throwable {

				WorkflowClock clock
			       = contextProvider.getDecisionContext().getWorkflowClock();
				
				
				long terminateAfter = 0;
				long waitFor = 0;
			    

				
				if (schedulerWorkflowState.endTimeDate != null) {
					terminateAfter = (schedulerWorkflowState.endTimeDate.getTime() - clock.currentTimeMillis())/1000;
				}
				
				if (schedulerWorkflowState.startTimeDate != null) {
						waitFor = (schedulerWorkflowState.startTimeDate.getTime() - clock.currentTimeMillis())/1000;
				}
				
				if (waitFor >= 0 ) {
					if (waitFor == 0) {
							waitFor = 1;   // wait a little longer to see if the try holds
					}
				}
				else {
					waitFor = 1;   // wait a little longer to see if the try holds
					
				}
				
				Promise<Void> timer = clock.createTimer(waitFor);
				callPeriodicActivity(
							schedulerWorkflowState.loopCount,
				   			functionType,	
							functionName,
							functionContext,	
							timer);
				callSignalProcessor(timer);
					
				Promise<Void> terminateTimer ;
				
				if  ((schedulerWorkflowState.endTimeDate != null) && (terminateAfter <= 0)) {
					terminateTimer = clock.createTimer(1);
					terminateSchedule(terminateTimer, true);
				}
				else if (terminateAfter > 0 ) {
					terminateTimer = clock.createTimer(terminateAfter);
					terminateSchedule(terminateTimer, false);
				}	
				
				Promise<Void> cleantimer = clock.createTimer(90);   // 1.5mins?		        
		        interuptOld(cleantimer);
			}
			
			@Override
		    protected void doCatch(Throwable e) throws Throwable {
			    WorkflowClock clock
			       = contextProvider.getDecisionContext().getWorkflowClock();

				Date currentTime = new Date(clock.currentTimeMillis());	
				
				if (e.getMessage() == "TERMINATED") {
					System.out.println("Got -- TERMINATED -- in doCatch @" +  currentTime);
				}
				else if (e.getMessage() == "CANCEL") {
					System.out.println("Got -- CANCEL -- in doCatch @" + currentTime);
				}
				else if (e.getMessage() == "FINISH") {
					System.out.println("Got -- FINISH -- in doCatch @" + currentTime);
				}
				else if (e.getMessage() == "INTERUPTOLD") {
//					System.out.println("Got -- INTERUPTOLD -- in doCatch @" + currentTime);
					Promise<Void> tmptimer = clock.createTimer(1); 
					continueAsNew(	
							tmptimer,
				   			functionType,	
							functionName,
							functionContext
						
							);
					
				}
				else {
					throw new RuntimeException("caught this", e);
				}
		    }

			@Override
			protected void doFinally() throws Throwable {
				// TODO Auto-generated method stub
				
			}
			
		};
		
	}
	
	@Override
	public void cancelWorkflowExecution(String reason) {
		
		
		schedulerWorkflowState.loopState = "CANCEL";	  
		SchedulerWorkflowImpl.getStaticStatus().set("CANCEL");
		
		DynamoDBMapper mapper = DyDBUtils.getDynamoDBMapper();
    	mapper.save(this.schedulerWorkflowState);
    	
    	
	}
	
	@Override
    public SchedulerWorkflowState getState() {
		return schedulerWorkflowState;
	}
	
	
		
	@Asynchronous
    private void callPeriodicActivity(
    		int count,
   			String functionType,	
			String functionName,
			String functionContext,
			Promise<?>... waitFor
            ) throws Throwable {
		
	    WorkflowClock clock
	       = contextProvider.getDecisionContext().getWorkflowClock();

		
		Date currentTime = new Date(clock.currentTimeMillis());		

		
		DynamoDBMapper mapper = DyDBUtils.getDynamoDBMapper();
		if (SchedulerWorkflowImpl.getStaticStatus().get() == "TERMINATED") {
        	throw new Exception("TERMINATED");            
		}
		if (SchedulerWorkflowImpl.getStaticStatus().get() == "CANCEL") {
        	throw new Exception("CANCEL");
 		}
 
 
		
		if (count == (schedulerWorkflowState.repeatTimes + 1)) {
    		schedulerWorkflowState.loopState = "FINISH";    		 
    		SchedulerWorkflowImpl.getStaticStatus().set("FINISH");
    		mapper.save(this.schedulerWorkflowState);
        	throw new Exception("FINISH");
        }
        try {
        	DateTime currentJTime = new DateTime(clock.currentTimeMillis(), DateTimeZone.forID(schedulerWorkflowState.timeZone) );
        	Promise<Void> timer;
        	
        		
        	if  ( (! (schedulerWorkflowState.lastProposedTimeDate.getTime() == 0)) &&
        			( ( schedulerWorkflowState.lastProposedTimeDate.getTime() - currentJTime.getMillis()) >= 2000L)) {
        	    	 // unnecessarily woken up
        		
        		int periodicityInSecs =  (int) ( schedulerWorkflowState.lastProposedTimeDate.getTime() - currentJTime.getMillis())/1000;        		        		
        		timer = clock.createTimer(periodicityInSecs);
        		
        		callPeriodicActivity(count,
        				functionType,		
        				functionName,
        				functionContext,
        				timer);        	    	 		 
        	}	
        	else {
        	    	 CronExpression ce = new CronExpression(schedulerWorkflowState.cronExpression);
        	
        	    	 DateTime nextTime = ce.nextTimeAfter(currentJTime);
        	    	 if ( (Math.abs(nextTime.getMillis() - currentJTime.getMillis()) < 1000L)) {
        	    		 nextTime = ce.nextTimeAfter(nextTime);
            		
        	    	 }
        	
        	    	 int periodicityInSecs = (int) ((nextTime.getMillis() - currentJTime.getMillis()) / 1000);
        	
        	    	 schedulerWorkflowState.lastProposedTimeDate = new Date(nextTime.getMillis());
        	    	 
        	    	 timer = clock.createTimer(periodicityInSecs);
        	    	 
        	    	 schedulerWorkflowState.loopState = "PROCESSING" ;
        	    	 SchedulerWorkflowImpl.getStaticStatus().set("PROCESSING");
        	    	 
        	
        	    	 
        	    	 if (count > 0) {
        	    		// System.out.println(schedulerWorkflowState.loopState + count + "for clientId " + schedulerWorkflowState.clientId  + '@' + currentTime);
        	    		 operations.runPeriodicActivity(functionName, functionContext);
        	    	 }
        	    	 schedulerWorkflowState.loopCount = count+1;
        	    	 schedulerWorkflowState.lastExecutionTimeDate = currentTime;

        	    	mapper.save(this.schedulerWorkflowState);

        	    	 
        	    	 callPeriodicActivity(count + 1,
        	    			 			functionType,	
        	    			 			functionName,
        	    			 			functionContext,
        	    			 			timer);
        	}
        	
        }
        catch (Exception e) {
        	System.err.println("Caught Exception: " + e.getMessage());
        } 
    }
	
	
	@Asynchronous
	void continueAsNew(  						
			Promise<Void> timer,
   			String functionType,	
			String functionName,
			String functionContext
			)  throws Throwable {
		
		
		
		
		selfClient.invoke( 	
				
		   			schedulerWorkflowState, 
		   			functionType,	
					functionName,
					functionContext
					
					);
		
	}
	
	
	
	@Asynchronous
	private void interuptOld(Promise<Void> timer) throws Throwable {


		throw new Exception("INTERUPTOLD");	
	}


	
	@Asynchronous
	private void terminateSchedule(Promise<Void> timer, boolean exponentialBackoff) throws Throwable {

		schedulerWorkflowState.loopState = "TERMINATED";
		SchedulerWorkflowImpl.getStaticStatus().set("TERMINATED");
		
	
		DynamoDBMapper mapper = DyDBUtils.getDynamoDBMapper();
    	mapper.save(this.schedulerWorkflowState);
    	
    	WorkflowClock clock
	       = contextProvider.getDecisionContext().getWorkflowClock();
    	
    	
    	
    	
    		
    		Promise<Void> timerWait = clock.createTimer(1);
    		throwTerminatedExeception(timerWait);
    		
    		
    	
	}

	@Asynchronous
	private void throwTerminatedExeception(Promise<Void> timer) throws Throwable {
		throw new Exception("TERMINATED");	
	}
	@Asynchronous
	private void throwCancelledExeception(Promise<Void> timer) throws Throwable {
		throw new Exception("CANCEL");	
	}
	@Asynchronous
	private void callHeartbeat(Promise<?>... waitFor) {

		
		
	}
	
	@Asynchronous
	private void callSignalProcessor(Promise<?>... waitFor) throws Throwable {
	    WorkflowClock clock
	       = contextProvider.getDecisionContext().getWorkflowClock();

		DynamoDBMapper mapper = DyDBUtils.getDynamoDBMapper();
		    
		Promise<Void> waittimer;
		int wait = 1;		
		
			if (SchedulerWorkflowImpl.getStaticStatus().get() == "CANCEL") { 
				
				

			    waittimer = clock.createTimer(wait);
				System.out.println(".....should throw cancel exception @ " + clock.currentTimeMillis() + " for " + schedulerWorkflowState.clientId);	
			    throwCancelledExeception(waittimer);	 
		    }
			else {
				Promise<Void> timer = clock.createTimer(30);
				callSignalProcessor(timer);	
			    
			}
	}
	private void persistWorkflowState(SchedulerWorkflowState schedulerWorkflowState ) {
		DynamoDBMapper mapper = DyDBUtils.getDynamoDBMapper();
		WorkflowClock clock
	       = contextProvider.getDecisionContext().getWorkflowClock();
		Date currentTime = new Date(clock.currentTimeMillis());	
		schedulerWorkflowState.workflowLastUpdatedDate = currentTime;
		mapper.save(schedulerWorkflowState);
		
		
		
	}

}
