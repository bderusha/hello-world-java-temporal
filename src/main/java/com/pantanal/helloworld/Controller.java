package com.pantanal.helloworld;


import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.temporal.client.WorkflowOptions;

@RestController
public class Controller {
    @Value("${temporal.workflow.taskqueue}")
    private String TASK_QUEUE;

    @Value("${temporal.workflow.id}")
    private String WORKFLOW_ID;

    @Value("${temporal.server.url}")
    private String temporalServerUrl;

    private WorkflowClient client;

    public Controller()
    {
        WorkflowServiceStubs service =
                WorkflowServiceStubs.newServiceStubs(
                        WorkflowServiceStubsOptions.newBuilder()
                                .setTarget(this.temporalServerUrl).build());

        this.client = WorkflowClient.newInstance(service);
    }

    @GetMapping("/")
    String HelloWorld(){
        String msg = "Hello World";
        // Create the workflow client stub. It is used to start our workflow execution.
        HelloActivity.GreetingWorkflow workflow =
                client.newWorkflowStub(
                        HelloActivity.GreetingWorkflow.class,
                        WorkflowOptions.newBuilder()
                                .setWorkflowId(WORKFLOW_ID)
                                .setTaskQueue(TASK_QUEUE)
                                .build());

        String greeting = workflow.getGreeting("World");
        return greeting;
    }
}
