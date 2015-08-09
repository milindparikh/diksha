/**
 * This code was generated from {@link org.diksha.common.utils.SchedulerActivities}.
 *
 * Any changes made directly to this file will be lost when 
 * the code is regenerated.
 */
package org.diksha.common.utils;

import com.amazonaws.services.simpleworkflow.flow.core.Promise;
import com.amazonaws.services.simpleworkflow.flow.ActivitiesClient;
import com.amazonaws.services.simpleworkflow.flow.ActivitySchedulingOptions;

public interface SchedulerActivitiesClient extends ActivitiesClient
{
    Promise<Void> runPeriodicActivity(java.lang.String functionName, java.lang.String functionContext);
    Promise<Void> runPeriodicActivity(java.lang.String functionName, java.lang.String functionContext, Promise<?>... waitFor);
    Promise<Void> runPeriodicActivity(java.lang.String functionName, java.lang.String functionContext, ActivitySchedulingOptions optionsOverride, Promise<?>... waitFor);
    Promise<Void> runPeriodicActivity(Promise<java.lang.String> functionName, Promise<java.lang.String> functionContext);
    Promise<Void> runPeriodicActivity(Promise<java.lang.String> functionName, Promise<java.lang.String> functionContext, Promise<?>... waitFor);
    Promise<Void> runPeriodicActivity(Promise<java.lang.String> functionName, Promise<java.lang.String> functionContext, ActivitySchedulingOptions optionsOverride, Promise<?>... waitFor);
    Promise<Void> terminateScheduler();
    Promise<Void> terminateScheduler(Promise<?>... waitFor);
    Promise<Void> terminateScheduler(ActivitySchedulingOptions optionsOverride, Promise<?>... waitFor);
}