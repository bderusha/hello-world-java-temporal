package com.pantanal.helloworld;

import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.boot.ApplicationRunner;

import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import org.springframework.context.annotation.Bean;
import com.google.protobuf.util.Durations;
import io.temporal.api.workflowservice.v1.RegisterNamespaceRequest;

@SpringBootApplication
public class Application {
    @Value("${temporal.workflow.taskqueue}")
    private String TASK_QUEUE;

	@Value("${temporal.server.url}")
	private String temporalServerUrl;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	@ConditionalOnProperty(value = "WORKER", havingValue = "true")
	ApplicationRunner applicationRunner() {
		return args -> {
			WorkflowServiceStubs service =
				WorkflowServiceStubs.newServiceStubs(
					WorkflowServiceStubsOptions.newBuilder()
						.setTarget(temporalServerUrl).build());

			WorkflowClient client = WorkflowClient.newInstance(service);
			WorkerFactory factory = WorkerFactory.newInstance(client);
			Worker worker = factory.newWorker(TASK_QUEUE);
			worker.registerWorkflowImplementationTypes(HelloActivity.GreetingWorkflowImpl.class);
			worker.registerActivitiesImplementations(new HelloActivity.GreetingActivitiesImpl());



			RegisterNamespaceRequest req = RegisterNamespaceRequest.newBuilder()
					.setNamespace("default")
					.setWorkflowExecutionRetentionPeriod(Durations.fromDays(1))
					.build();
			// Try to create the default namespace if it doesn't exist
			try {
				service.blockingStub().registerNamespace(req);
			} catch(io.grpc.StatusRuntimeException e) {
				System.out.println("default namespace already registered!");
			}


			factory.start();
		};
	}
}

