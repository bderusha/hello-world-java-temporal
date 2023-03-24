package com.pantanal.helloworld;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.temporal.client.WorkflowClient;

@SpringBootApplication
public class Application {
	private WorkflowClient workflowClient;

	public Application(WorkflowClient workflowClient)
	{
		this.workflowClient = workflowClient;
	}
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}

