package com.test.scheduler;

import org.apache.camel.builder.RouteBuilder;

public class TestRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("direct:test")
			.log("Test print ")
			.end();
		
	}
	
	

}
