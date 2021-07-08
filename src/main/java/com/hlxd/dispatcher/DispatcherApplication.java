package com.hlxd.dispatcher;

import com.hlxd.dispatcher.service.SocketServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class DispatcherApplication {

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(DispatcherApplication.class, args);
		applicationContext.getBean(SocketServer.class).start();
	}

}
