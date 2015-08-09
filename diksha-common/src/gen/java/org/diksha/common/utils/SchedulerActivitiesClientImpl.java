/**
 * This code was generated from {@link org.diksha.common.utils.SchedulerActivities}.
 *
 * Any changes made directly to this file will be lost when 
 * the code is regenerated.
 */
package org.diksha.common.utils;

import com.amazonaws.services.simpleworkflow.flow.ActivitiesClientBase;
import com.amazonaws.services.simpleworkflow.flow.ActivitySchedulingOptions;
import com.amazonaws.services.simpleworkflow.flow.DataConverter;
import com.amazonaws.services.simpleworkflow.flow.core.Promise;
import com.amazonaws.services.simpleworkflow.flow.generic.GenericActivityClient;
import com.amazonaws.services.simpleworkflow.model.ActivityType;

public class SchedulerActivitiesClientImpl extends ActivitiesClientBase implements SchedulerActivitiesClient {

	public SchedulerActivitiesClientImpl() {
        this(null, new com.amazonaws.services.simpleworkflow.flow.JsonDataConverter(), null);
    }

    public SchedulerActivitiesClientImpl(GenericActivityClient genericClient) {
        this(genericClient, new com.amazonaws.services.simpleworkflow.flow.JsonDataConverter(), null);
    }
    
    public SchedulerActivitiesClientImpl(GenericActivityClient genericClient, 
            DataConverter dataConverter, ActivitySchedulingOptions schedulingOptions) {
            
        super(genericClient, dataConverter, schedulingOptions);
    }
    
    @Override
    public final Promise<Void> runPeriodicActivity(java.lang.String functionName, java.lang.String functionContext) {
        return runPeriodicActivityImpl(Promise.asPromise(functionName), Promise.asPromise(functionContext), (ActivitySchedulingOptions)null);
    }

    @Override
    public final Promise<Void> runPeriodicActivity(java.lang.String functionName, java.lang.String functionContext, Promise<?>... waitFor) {
        return runPeriodicActivityImpl(Promise.asPromise(functionName), Promise.asPromise(functionContext), (ActivitySchedulingOptions)null, waitFor);
    }

    @Override
    public final Promise<Void> runPeriodicActivity(java.lang.String functionName, java.lang.String functionContext, ActivitySchedulingOptions optionsOverride, Promise<?>... waitFor) {
        return runPeriodicActivityImpl(Promise.asPromise(functionName), Promise.asPromise(functionContext), optionsOverride, waitFor);
    }

    @Override
    public final Promise<Void> runPeriodicActivity(Promise<java.lang.String> functionName, Promise<java.lang.String> functionContext) {
        return runPeriodicActivityImpl(functionName, functionContext, (ActivitySchedulingOptions)null);
    }

    @Override
    public final Promise<Void> runPeriodicActivity(Promise<java.lang.String> functionName, Promise<java.lang.String> functionContext, Promise<?>... waitFor) {
        return runPeriodicActivityImpl(functionName, functionContext, (ActivitySchedulingOptions)null, waitFor);
    }

    @Override
    public final Promise<Void> runPeriodicActivity(Promise<java.lang.String> functionName, Promise<java.lang.String> functionContext, ActivitySchedulingOptions optionsOverride, Promise<?>... waitFor) {
        return runPeriodicActivityImpl(functionName, functionContext, optionsOverride, waitFor);
    }
    
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected Promise<Void> runPeriodicActivityImpl(final Promise<java.lang.String> functionName, final Promise<java.lang.String> functionContext, final ActivitySchedulingOptions optionsOverride, Promise<?>... waitFor) {

        ActivityType activityType = new ActivityType();
		activityType.setName("SchedulerActivities.runPeriodicActivity");
		activityType.setVersion("7.0");

        Promise[] _input_ = new Promise[2];
        _input_[0] = functionName;
        _input_[1] = functionContext;

        return (Promise)scheduleActivity(activityType, _input_, optionsOverride, Void.class, waitFor);
    }

    @Override
    public final Promise<Void> terminateScheduler() {
        return terminateSchedulerImpl((ActivitySchedulingOptions)null);
    }

    @Override
    public final Promise<Void> terminateScheduler(Promise<?>... waitFor) {
        return terminateSchedulerImpl((ActivitySchedulingOptions)null, waitFor);
    }

    @Override
    public final Promise<Void> terminateScheduler(ActivitySchedulingOptions optionsOverride, Promise<?>... waitFor) {
        return terminateSchedulerImpl(optionsOverride, waitFor);
    }
    
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected Promise<Void> terminateSchedulerImpl(final ActivitySchedulingOptions optionsOverride, Promise<?>... waitFor) {

        ActivityType activityType = new ActivityType();
		activityType.setName("SchedulerActivities.terminateScheduler");
		activityType.setVersion("7.0");

        Promise[] _input_ = new Promise[0];

        return (Promise)scheduleActivity(activityType, _input_, optionsOverride, Void.class, waitFor);
    }

}