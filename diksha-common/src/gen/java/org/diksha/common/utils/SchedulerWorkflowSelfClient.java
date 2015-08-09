/**
 * This code was generated from {@link org.diksha.common.utils.SchedulerWorkflow}.
 *
 * Any changes made directly to this file will be lost when 
 * the code is regenerated.
 */
package org.diksha.common.utils;

import com.amazonaws.services.simpleworkflow.flow.core.Promise;
import com.amazonaws.services.simpleworkflow.flow.StartWorkflowOptions;
import com.amazonaws.services.simpleworkflow.flow.WorkflowSelfClient;

public interface SchedulerWorkflowSelfClient extends WorkflowSelfClient
{
    void invoke(org.diksha.common.utils.SchedulerWorkflowState SchedulerWorkflowState, java.lang.String functionType, java.lang.String functionName, java.lang.String functionContext);
    void invoke(org.diksha.common.utils.SchedulerWorkflowState SchedulerWorkflowState, java.lang.String functionType, java.lang.String functionName, java.lang.String functionContext, Promise<?>... waitFor);
    void invoke(org.diksha.common.utils.SchedulerWorkflowState SchedulerWorkflowState, java.lang.String functionType, java.lang.String functionName, java.lang.String functionContext, StartWorkflowOptions optionsOverride, Promise<?>... waitFor);
    void invoke(Promise<org.diksha.common.utils.SchedulerWorkflowState> SchedulerWorkflowState, Promise<java.lang.String> functionType, Promise<java.lang.String> functionName, Promise<java.lang.String> functionContext);
    void invoke(Promise<org.diksha.common.utils.SchedulerWorkflowState> SchedulerWorkflowState, Promise<java.lang.String> functionType, Promise<java.lang.String> functionName, Promise<java.lang.String> functionContext, Promise<?>... waitFor);
    void invoke(Promise<org.diksha.common.utils.SchedulerWorkflowState> SchedulerWorkflowState, Promise<java.lang.String> functionType, Promise<java.lang.String> functionName, Promise<java.lang.String> functionContext, StartWorkflowOptions optionsOverride, Promise<?>... waitFor);
}