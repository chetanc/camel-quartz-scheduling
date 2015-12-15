package com.test.scheduler;

import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScheduleJobProcessor implements Processor {
	
	private static Logger LOGGER = LoggerFactory.getLogger(ScheduleJobProcessor.class);

	public void process(Exchange exchange) throws Exception {
		
		LOGGER.info("getting Headers ");
		Map<String, Object> headers = exchange.getIn().getHeaders();
		
		LOGGER.info("Printing headers ");
		LOGGER.info("Headers : " + headers);
	
		
	}
	
	

}
