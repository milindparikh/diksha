/**
 * This code was generated from {@link org.diksha.common.utils.SchedulerWorkflow}.
 *
 * Any changes made directly to this file will be lost when 
 * the code is regenerated.
 */
package org.diksha.common.utils;

import com.amazonaws.services.simpleworkflow.flow.core.Promise;
import com.amazonaws.services.simpleworkflow.flow.StartWorkflowOptions;
import com.amazonaws.services.simpleworkflow.flow.WorkflowClient;

public interface SchedulerWorkflowClient extends WorkflowClient
{
    Promise<Void> invoke(org.diksha.common.utils.SchedulerWorkflowState SchedulerWorkflowState, java.lang.String functionType, java.lang.String functionName, java.lang.String functionContext);
    Promise<Void> invoke(org.diksha.common.utils.SchedulerWorkflowState SchedulerWorkflowState, java.lang.String functionType, java.lang.String functionName, java.lang.String functionContext, Promise<?>... waitFor);
    Promise<Void> invoke(org.diksha.common.utils.SchedulerWorkflowState SchedulerWorkflowState, java.lang.String functionType, java.lang.String functionName, java.lang.String functionContext, StartWorkflowOptions optionsOverride, Promise<?>... waitFor);
    Promise<Void> invoke(Promise<org.diksha.common.utils.SchedulerWorkflowState> SchedulerWorkflowState, Promise<java.lang.String> functionType, Promise<java.lang.String> functionName, Promise<java.lang.String> functionContext);
    Promise<Void> invoke(Promise<org.diksha.common.utils.SchedulerWorkflowState> SchedulerWorkflowState, Promise<java.lang.String> functionType, Promise<java.lang.String> functionName, Promise<java.lang.String> functionContext, Promise<?>... waitFor);
    Promise<Void> invoke(Promise<org.diksha.common.utils.SchedulerWorkflowState> SchedulerWorkflowState, Promise<java.lang.String> functionType, Promise<java.lang.String> functionName, Promise<java.lang.String> functionContext, StartWorkflowOptions optionsOverride, Promise<?>... waitFor);
    void cancelWorkflowExecution(java.lang.String reason);
}