/**
 * This code was generated from {@link org.diksha.common.utils.SchedulerWorkflow}.
 *
 * Any changes made directly to this file will be lost when 
 * the code is regenerated.
 */
package org.diksha.common.utils;

import com.amazonaws.services.simpleworkflow.flow.DataConverter;
import com.amazonaws.services.simpleworkflow.flow.StartWorkflowOptions;
import com.amazonaws.services.simpleworkflow.flow.WorkflowClientBase;
import com.amazonaws.services.simpleworkflow.flow.core.Promise;
import com.amazonaws.services.simpleworkflow.flow.generic.GenericWorkflowClient;
import com.amazonaws.services.simpleworkflow.model.WorkflowExecution;
import com.amazonaws.services.simpleworkflow.model.WorkflowType;

class SchedulerWorkflowClientImpl extends WorkflowClientBase implements SchedulerWorkflowClient {

    public SchedulerWorkflowClientImpl(WorkflowExecution workflowExecution, WorkflowType workflowType,  
            StartWorkflowOptions options, DataConverter dataConverter, GenericWorkflowClient genericClient) {
        super(workflowExecution, workflowType, options, dataConverter, genericClient);
    }
    
    @Override
    public final Promise<Void> invoke(org.diksha.common.utils.SchedulerWorkflowState SchedulerWorkflowState, java.lang.String functionType, java.lang.String functionName, java.lang.String functionContext) { 
        return invoke(Promise.asPromise(SchedulerWorkflowState), Promise.asPromise(functionType), Promise.asPromise(functionName), Promise.asPromise(functionContext), (StartWorkflowOptions)null);
    }
    
    @Override
    public final Promise<Void> invoke(org.diksha.common.utils.SchedulerWorkflowState SchedulerWorkflowState, java.lang.String functionType, java.lang.String functionName, java.lang.String functionContext, Promise<?>... waitFor) {
        return invoke(Promise.asPromise(SchedulerWorkflowState), Promise.asPromise(functionType), Promise.asPromise(functionName), Promise.asPromise(functionContext), (StartWorkflowOptions)null, waitFor);
    }
    
    
    @Override
    
    public final Promise<Void> invoke(org.diksha.common.utils.SchedulerWorkflowState SchedulerWorkflowState, java.lang.String functionType, java.lang.String functionName, java.lang.String functionContext, StartWorkflowOptions optionsOverride, Promise<?>... waitFor) {
        return invoke(Promise.asPromise(SchedulerWorkflowState), Promise.asPromise(functionType), Promise.asPromise(functionName), Promise.asPromise(functionContext), optionsOverride, waitFor);
    }

    @Override
    public final Promise<Void> invoke(Promise<org.diksha.common.utils.SchedulerWorkflowState> SchedulerWorkflowState, Promise<java.lang.String> functionType, Promise<java.lang.String> functionName, Promise<java.lang.String> functionContext) {
        return invoke(SchedulerWorkflowState, functionType, functionName, functionContext, (StartWorkflowOptions)null);
    }

    @Override
    public final Promise<Void> invoke(Promise<org.diksha.common.utils.SchedulerWorkflowState> SchedulerWorkflowState, Promise<java.lang.String> functionType, Promise<java.lang.String> functionName, Promise<java.lang.String> functionContext, Promise<?>... waitFor) {
        return invoke(SchedulerWorkflowState, functionType, functionName, functionContext, (StartWorkflowOptions)null, waitFor);
    }

    @Override
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public final Promise<Void> invoke(Promise<org.diksha.common.utils.SchedulerWorkflowState> SchedulerWorkflowState, Promise<java.lang.String> functionType, Promise<java.lang.String> functionName, Promise<java.lang.String> functionContext, StartWorkflowOptions optionsOverride, Promise<?>... waitFor) {
        Promise[] _input_ = new Promise[4];
        _input_[0] = SchedulerWorkflowState;
        _input_[1] = functionType;
        _input_[2] = functionName;
        _input_[3] = functionContext;
        return (Promise) startWorkflowExecution(_input_, optionsOverride, Void.class, waitFor);
    }
    	

    @Override
    public void cancelWorkflowExecution(java.lang.String reason) { 
        Object[] _input_ = new Object[1];
        _input_[0] = reason;
        signalWorkflowExecution("cancelWorkflowExecution", _input_);
    }
}