package com.test.scheduler;

import org.apache.camel.builder.RouteBuilder;

/**
 * 
 *
 */
public class SchedulerRoutes extends RouteBuilder {
	
	
	@Override
	public void configure() throws Exception {
	
		//Below lines of code is for quartz scheduler route. Here the job is scheduled in the route
		from("quartz2://testGroup/testTimer?cron="+"0+0/2+*+1/1+*+?+*"+"&job.name=sample&job.value=paramValue&deleteJob=true")
		.routeId("scheduleRoute")
		.process(new ScheduleJobProcessor())		
		.end();		
		
	}
}
