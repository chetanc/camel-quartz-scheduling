package com.test.scheduler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.apache.camel.CamelContext;
import org.apache.camel.component.quartz2.QuartzComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.JavaUuidGenerator;
import org.apache.camel.main.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CamelQuartzTrial {

	private static final Logger logger = LoggerFactory
			.getLogger(CamelQuartzTrial.class);

	/** camel context */
	private CamelContext camelContext;

	private ServerSocket serverSocket;

	private Main main;

	/**
	 * Method to be called Singleton or HASignleton
	 */
	public void initialize() throws Exception {
		try {
			logger.info("Started initialize of CamelQuartzTrial ");
			buildCamelContext();
			String socketPort = "9001";
			int port = Integer.parseInt(socketPort);
			serverSocket = new ServerSocket(port);
		} catch (Exception e) {
			logger.error("Error in starting CamelQuartzTrial ", e);
			throw e;
		}
	}

	public class SchedulerCommandSocket extends Thread {

		public void run() {
			while (true) {
				try {
					logger.info("Waiting for stop command on port "
							+ serverSocket.getLocalPort() + "...");
					Socket server = serverSocket.accept();
					DataInputStream in = new DataInputStream(
							server.getInputStream());
					String command = in.readUTF();
					logger.info("Received command " + command);
					if ("stop_scheduler".equals(command)) {
						shutdown();
						DataOutputStream out = new DataOutputStream(
								server.getOutputStream());
						out.writeUTF("Stopping scheduler ");
						server.close();
						serverSocket.close();
						break;
					}
				} catch (SocketTimeoutException s) {
					logger.error(
							"Socket timed out while waiting for stop command ",
							s);
				} catch (IOException e) {
					logger.error("IOException while socket bind ");
					break;
				}
			}
		}
	}

	/**
	 * Method to start scheduler
	 * 
	 * 
	 */
	public void startScheduler() throws Exception {
		initialize();
		new SchedulerCommandSocket().start();
		logger.info("Starting camel context");
		// this is blocking call
		main.run();
		logger.info("Shutdown camel context ");
	}

	/**
	 * method will be called at container shutdown
	 */
	public void shutdown() {
		try {
			logger.info("Shutting down CamelQuartzScheduler ");
			main.shutdown();
		} catch (Exception exception) {
			logger.warn("Exception while stopping CamelQuartzScheduler ",
					exception);
		}
	}

	/**
	 * This method builds the camelContext
	 * 
	 * @param context
	 */
	public void buildCamelContext() throws Exception {
		logger.info("Building Camel Context ");

		main = new Main();
		main.enableHangupSupport();
		// bind MyBean into the registery
		camelContext = new DefaultCamelContext();
		// camelContext = new DefaultCamelContext(context);

		QuartzComponent quartzComponent = new QuartzComponent();

		// TODO

		camelContext.addComponent("quartz2", quartzComponent);
		camelContext.setUuidGenerator(new JavaUuidGenerator());
		logger.info("Adding routes for scheduler APIs ");
		camelContext.addRoutes(new SchedulerRoutes());
		main.getCamelContexts().add(camelContext);
	}

	/*
	 * public ProducerTemplate getProducerTemplate() { return
	 * camelContext.createProducerTemplate(); }
	 */

	public static void main(String[] args) throws Exception {
		try {
			CamelQuartzTrial scheduler = new CamelQuartzTrial();
			scheduler.startScheduler();

		} catch (Exception e) {
			logger.error("Exception while starting scheduler ", e);
			throw e;
		}

	}

}
