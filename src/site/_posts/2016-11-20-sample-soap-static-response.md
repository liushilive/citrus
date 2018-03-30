---
layout: sample
title: Static SOAP response sample
sample: sample-soap-static-response
description: Shows how to setup a static response generating SOAP web service server component
categories: [samples]
permalink: /samples/soap-static-response/
---

Usually incoming requests on server components are handled by a Citrus test case where the tester defines the received request message. Also the test
case provides a proper response message to the calling client. There may be times we do not want to handle request/response in the test case. Instead we
need to define a static response message that should always be sent from a server component in Citrus.

This sample shows how to setup a Citrus SOAP web service server component that automatically provides response messages to incoming requests.

Objectives
---------

We want to have a server component that provides a static response message to calling clients. Depending on the requested resource path the server
should provide different response messages. we can do this in Citrus with a little bit of Spring bean configuration:

{% highlight java %}
@Bean
public WebServiceServer todoListServer() throws Exception {
    return CitrusEndpoints.soap()
            .server()
            .port(8080)
            .endpointAdapter(dispatchingEndpointAdapter())
            .timeout(10000)
            .autoStart(true)
            .build();
}

@Bean
public RequestDispatchingEndpointAdapter dispatchingEndpointAdapter() {
    RequestDispatchingEndpointAdapter dispatchingEndpointAdapter = new RequestDispatchingEndpointAdapter();
    dispatchingEndpointAdapter.setMappingKeyExtractor(mappingKeyExtractor());
    dispatchingEndpointAdapter.setMappingStrategy(mappingStrategy());
    return dispatchingEndpointAdapter;
}
{% endhighlight %}

The *todoListServer* is a normal server component in Citrus. The endpoint-adapter is different though and defines the way how to automatically respond to calling clients.
The server uses a **dispatchingEndpointAdapter**. This endpoint adapter implementation uses a mapping key extractor and a mapping strategy in order to map incoming requests to response generating
adapters.

{% highlight java %}
@Bean
public HeaderMappingKeyExtractor mappingKeyExtractor() {
    return new SoapActionMappingKeyExtractor();
}

@Bean
public SimpleMappingStrategy mappingStrategy() {
    SimpleMappingStrategy mappingStrategy = new SimpleMappingStrategy();

    Map<String, EndpointAdapter> mappings = new HashMap<>();

    mappings.put("getTodo", todoResponseAdapter());
    mappings.put("getTodoList", todoListResponseAdapter());

    mappingStrategy.setAdapterMappings(mappings);
    return mappingStrategy;
}
{% endhighlight %}

The mapping key extractor implementation evaluates the SOAP action header. Depending on that SOAP action value the
mapping strategy maps incoming requests to different response generating adapter implementations. Here in this example we define **getTodo** and **getTodoList** actions with response
adapters.

{% highlight java %}
@Bean
public EndpointAdapter todoResponseAdapter() {
    StaticResponseEndpointAdapter endpointAdapter = new StaticResponseEndpointAdapter();
    endpointAdapter.setMessagePayload("<getTodoResponse xmlns=\"http://citrusframework.org/samples/todolist\">" +
                "<todoEntry xmlns=\"http://citrusframework.org/samples/todolist\">" +
                    "<id>${todoId}</id>" +
                    "<title>${todoName}</title>" +
                    "<description>${todoDescription}</description>" +
                    "<done>false</done>" +
                "</todoEntry>" +
            "</getTodoResponse>");
    return endpointAdapter;
}

@Bean
public EndpointAdapter todoListResponseAdapter() {
    StaticResponseEndpointAdapter endpointAdapter = new StaticResponseEndpointAdapter();
    endpointAdapter.setMessagePayload("<getTodoListResponse xmlns=\"http://citrusframework.org/samples/todolist\">" +
                "<list>" +
                    "<todoEntry>" +
                        "<id>${todoId}</id>" +
                        "<title>${todoName}</title>" +
                        "<description>${todoDescription}</description>" +
                        "<done>false</done>" +
                    "</todoEntry>" +
                "</list>" +
            "</getTodoListResponse>");
    return endpointAdapter;
}
{% endhighlight %}

The response adapters provide static response messages. In summary we have a small SOAP web service server component that automatically responds to incoming request messages
with static message payloads. As usual the SOAP envelope handling is automatically done within the server component.

Run
---------

The sample application uses Maven as build tool. So you can compile, package and test the
sample with Maven.
 
     mvn clean install
    
This executes the complete Maven build lifecycle. During the build you will see Citrus performing some integration tests.

Execute all Citrus tests by calling

     mvn integration-test

You can also pick a single test by calling

     mvn integration-test -Ptest=TodoListIT

You should see Citrus performing several tests with lots of debugging output. 
And of course green tests at the very end of the build.

Of course you can also start the Citrus tests from your favorite IDE.
Just start the Citrus test using the TestNG IDE integration in IntelliJ, Eclipse or Netbeans.