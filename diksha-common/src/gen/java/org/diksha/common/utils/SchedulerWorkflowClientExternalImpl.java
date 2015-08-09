/**
 * This code was generated from {@link org.diksha.common.utils.SchedulerWorkflow}.
 *
 * Any changes made directly to this file will be lost when 
 * the code is regenerated.
 */
package org.diksha.common.utils;

import com.amazonaws.services.simpleworkflow.flow.DataConverter;
import com.amazonaws.services.simpleworkflow.flow.StartWorkflowOptions;
import com.amazonaws.services.simpleworkflow.flow.WorkflowClientExternalBase;
import com.amazonaws.services.simpleworkflow.flow.generic.GenericWorkflowClientExternal;
import com.amazonaws.services.simpleworkflow.model.WorkflowExecution;
import com.amazonaws.services.simpleworkflow.model.WorkflowType;

class SchedulerWorkflowClientExternalImpl extends WorkflowClientExternalBase implements SchedulerWorkflowClientExternal {

    public SchedulerWorkflowClientExternalImpl(WorkflowExecution workflowExecution, WorkflowType workflowType, 
            StartWorkflowOptions options, DataConverter dataConverter, GenericWorkflowClientExternal genericClient) {
        super(workflowExecution, workflowType, options, dataConverter, genericClient);
    }

    @Override
    public void invoke(org.diksha.common.utils.SchedulerWorkflowState SchedulerWorkflowState, java.lang.String functionType, java.lang.String functionName, java.lang.String functionContext) { 
        invoke(SchedulerWorkflowState, functionType, functionName, functionContext, null);
    }

    @Override
    public void invoke(org.diksha.common.utils.SchedulerWorkflowState SchedulerWorkflowState, java.lang.String functionType, java.lang.String functionName, java.lang.String functionContext, StartWorkflowOptions startOptionsOverride) {
    
        Object[] _arguments_ = new Object[4]; 
        _arguments_[0] = SchedulerWorkflowState;
        _arguments_[1] = functionType;
        _arguments_[2] = functionName;
        _arguments_[3] = functionContext;
        dynamicWorkflowClient.startWorkflowExecution(_arguments_, startOptionsOverride);
    }

    @Override
    public void cancelWorkflowExecution(java.lang.String reason) {
        Object[] _arguments_ = new Object[1];
        _arguments_[0] = reason;
        dynamicWorkflowClient.signalWorkflowExecution("cancelWorkflowExecution", _arguments_);
    }

    @Override
    public org.diksha.common.utils.SchedulerWorkflowState getState()  {
        org.diksha.common.utils.SchedulerWorkflowState _state_ = null;
        try {
            _state_ = dynamicWorkflowClient.getWorkflowExecutionState(org.diksha.common.utils.SchedulerWorkflowState.class);
        } catch (Throwable _failure_) {
            if (_failure_ instanceof RuntimeException) {
                throw (RuntimeException) _failure_;
            } else if (_failure_ instanceof Error) {
                throw (Error) _failure_;
            } else {
                throw new RuntimeException("Unknown exception.", _failure_);
            }
        }

        return _state_;
    }
}