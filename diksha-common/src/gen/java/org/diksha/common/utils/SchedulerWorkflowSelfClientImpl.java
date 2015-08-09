/**
 * This code was generated from {@link org.diksha.common.utils.SchedulerWorkflow}.
 *
 * Any changes made directly to this file will be lost when 
 * the code is regenerated.
 */
package org.diksha.common.utils;

import com.amazonaws.services.simpleworkflow.flow.core.AndPromise;
import com.amazonaws.services.simpleworkflow.flow.core.Promise;
import com.amazonaws.services.simpleworkflow.flow.core.Task;
import com.amazonaws.services.simpleworkflow.flow.DataConverter;
import com.amazonaws.services.simpleworkflow.flow.StartWorkflowOptions;
import com.amazonaws.services.simpleworkflow.flow.WorkflowSelfClientBase;
import com.amazonaws.services.simpleworkflow.flow.generic.ContinueAsNewWorkflowExecutionParameters;
import com.amazonaws.services.simpleworkflow.flow.generic.GenericWorkflowClient;

public class SchedulerWorkflowSelfClientImpl extends WorkflowSelfClientBase implements SchedulerWorkflowSelfClient {

    public SchedulerWorkflowSelfClientImpl() {
        this(null, new com.amazonaws.services.simpleworkflow.flow.JsonDataConverter(), null);
    }

    public SchedulerWorkflowSelfClientImpl(GenericWorkflowClient genericClient) {
        this(genericClient, new com.amazonaws.services.simpleworkflow.flow.JsonDataConverter(), null);
    }

    public SchedulerWorkflowSelfClientImpl(GenericWorkflowClient genericClient, 
            DataConverter dataConverter, StartWorkflowOptions schedulingOptions) {
            
        super(genericClient, dataConverter, schedulingOptions);
    }

    @Override
    public final void invoke(org.diksha.common.utils.SchedulerWorkflowState SchedulerWorkflowState, java.lang.String functionType, java.lang.String functionName, java.lang.String functionContext) { 
        invokeImpl(Promise.asPromise(SchedulerWorkflowState), Promise.asPromise(functionType), Promise.asPromise(functionName), Promise.asPromise(functionContext), (StartWorkflowOptions)null);
    }

    @Override
    public final void invoke(org.diksha.common.utils.SchedulerWorkflowState SchedulerWorkflowState, java.lang.String functionType, java.lang.String functionName, java.lang.String functionContext, Promise<?>... waitFor) { 
        invokeImpl(Promise.asPromise(SchedulerWorkflowState), Promise.asPromise(functionType), Promise.asPromise(functionName), Promise.asPromise(functionContext), (StartWorkflowOptions)null, waitFor);
    }
    
    @Override
    public final void invoke(org.diksha.common.utils.SchedulerWorkflowState SchedulerWorkflowState, java.lang.String functionType, java.lang.String functionName, java.lang.String functionContext, StartWorkflowOptions optionsOverride, Promise<?>... waitFor) {
        invokeImpl(Promise.asPromise(SchedulerWorkflowState), Promise.asPromise(functionType), Promise.asPromise(functionName), Promise.asPromise(functionContext), optionsOverride, waitFor);
    }
    
    @Override
    public final void invoke(Promise<org.diksha.common.utils.SchedulerWorkflowState> SchedulerWorkflowState, Promise<java.lang.String> functionType, Promise<java.lang.String> functionName, Promise<java.lang.String> functionContext) {
        invokeImpl(SchedulerWorkflowState, functionType, functionName, functionContext, (StartWorkflowOptions)null);
    }

    @Override
    public final void invoke(Promise<org.diksha.common.utils.SchedulerWorkflowState> SchedulerWorkflowState, Promise<java.lang.String> functionType, Promise<java.lang.String> functionName, Promise<java.lang.String> functionContext, Promise<?>... waitFor) {
        invokeImpl(SchedulerWorkflowState, functionType, functionName, functionContext, (StartWorkflowOptions)null, waitFor);
    }

    @Override
    public final void invoke(Promise<org.diksha.common.utils.SchedulerWorkflowState> SchedulerWorkflowState, Promise<java.lang.String> functionType, Promise<java.lang.String> functionName, Promise<java.lang.String> functionContext, StartWorkflowOptions optionsOverride, Promise<?>... waitFor) {
        invokeImpl(SchedulerWorkflowState, functionType, functionName, functionContext, optionsOverride, waitFor);
    }
    
    protected void invokeImpl(final Promise<org.diksha.common.utils.SchedulerWorkflowState> SchedulerWorkflowState, final Promise<java.lang.String> functionType, final Promise<java.lang.String> functionName, final Promise<java.lang.String> functionContext, final StartWorkflowOptions schedulingOptionsOverride, Promise<?>... waitFor) {
        new Task(new Promise[] { SchedulerWorkflowState, functionType, functionName, functionContext, new AndPromise(waitFor) }) {
    		@Override
			protected void doExecute() throws Throwable {
                ContinueAsNewWorkflowExecutionParameters _parameters_ = new ContinueAsNewWorkflowExecutionParameters();
                Object[] _input_ = new Object[4];
                _input_[0] = SchedulerWorkflowState.get();
                _input_[1] = functionType.get();
                _input_[2] = functionName.get();
                _input_[3] = functionContext.get();
                String _stringInput_ = dataConverter.toData(_input_);
				_parameters_.setInput(_stringInput_);
				_parameters_ = _parameters_.createContinueAsNewParametersFromOptions(schedulingOptions, schedulingOptionsOverride);
                
                if (genericClient == null) {
                    genericClient = decisionContextProvider.getDecisionContext().getWorkflowClient();
                }
                genericClient.continueAsNewOnCompletion(_parameters_);
			}
		};
    }
}