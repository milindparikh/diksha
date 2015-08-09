/**
 * This code was generated from {@link org.diksha.common.utils.SchedulerWorkflow}.
 *
 * Any changes made directly to this file will be lost when 
 * the code is regenerated.
 */
package org.diksha.common.utils;

import com.amazonaws.services.simpleworkflow.flow.StartWorkflowOptions;
import com.amazonaws.services.simpleworkflow.flow.WorkflowClientExternal;

public interface SchedulerWorkflowClientExternal extends WorkflowClientExternal
{
    void invoke(org.diksha.common.utils.SchedulerWorkflowState SchedulerWorkflowState, java.lang.String functionType, java.lang.String functionName, java.lang.String functionContext);
    void invoke(org.diksha.common.utils.SchedulerWorkflowState SchedulerWorkflowState, java.lang.String functionType, java.lang.String functionName, java.lang.String functionContext, StartWorkflowOptions optionsOverride);
    void cancelWorkflowExecution(java.lang.String reason);
    org.diksha.common.utils.SchedulerWorkflowState getState() ;
}