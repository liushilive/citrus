/*
 * Copyright 2006-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.consol.citrus.validation.builder;

import com.consol.citrus.context.TestContext;
import com.consol.citrus.message.DefaultMessage;
import com.consol.citrus.message.Message;
import com.consol.citrus.message.MessageHeaders;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.testng.AbstractTestNGUnitTest;
import com.consol.citrus.validation.interceptor.AbstractMessageConstructionInterceptor;
import com.consol.citrus.variable.dictionary.json.JsonMappingDataDictionary;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Christoph Deppisch
 */
public class StaticMessageContentBuilderTest extends AbstractTestNGUnitTest {

    private StaticMessageContentBuilder messageBuilder;

    @Test
    public void testBuildMessageContent() {
        final Message testMessage = new DefaultMessage("TestMessage")
                .setHeader("header1", "value1");

        messageBuilder = new StaticMessageContentBuilder(testMessage);

        final Message message = messageBuilder.buildMessageContent(context, MessageType.PLAINTEXT.name());
        Assert.assertEquals(message.getPayload(), testMessage.getPayload());
        Assert.assertNotEquals(message.getHeader(MessageHeaders.ID), testMessage.getHeader(MessageHeaders.ID));
        Assert.assertEquals(message.getHeaders().size(), testMessage.getHeaders().size() + 1);
        Assert.assertEquals(message.getHeader("header1"), testMessage.getHeader("header1"));
        Assert.assertEquals(message.getHeader(MessageHeaders.MESSAGE_TYPE), MessageType.PLAINTEXT.name());
    }

    @Test
    public void testBuildMessageContentWithAdditionalHeader() {
        final Message testMessage = new DefaultMessage("TestMessage")
                .setHeader("header1", "value1");

        messageBuilder = new StaticMessageContentBuilder(testMessage);
        messageBuilder.getMessageHeaders().put("additional", "new");

        final Message message = messageBuilder.buildMessageContent(context, MessageType.PLAINTEXT.name());
        Assert.assertEquals(message.getPayload(), testMessage.getPayload());
        Assert.assertNotEquals(message.getHeader(MessageHeaders.ID), testMessage.getHeader(MessageHeaders.ID));
        Assert.assertNotNull(message.getHeader("additional"));
        Assert.assertEquals(message.getHeader("additional"), "new");
    }

    @Test
    public void testBuildMessageContentWithAdditionalHeaderData() {
        final Message testMessage = new DefaultMessage("TestMessage")
                .setHeader("header1", "value1");

        messageBuilder = new StaticMessageContentBuilder(testMessage);
        messageBuilder.getHeaderData().add("TestMessageData");

        final Message message = messageBuilder.buildMessageContent(context, MessageType.PLAINTEXT.name());
        Assert.assertEquals(message.getPayload(), testMessage.getPayload());
        Assert.assertNotEquals(message.getHeader(MessageHeaders.ID), testMessage.getHeader(MessageHeaders.ID));
        Assert.assertEquals(message.getHeaderData().size(), 1L);
        Assert.assertEquals(message.getHeaderData().get(0), "TestMessageData");
    }

    @Test
    public void testBuildMessageContentWithMultipleHeaderData() {
        final Message testMessage = new DefaultMessage("TestMessage")
                .setHeader("header1", "value1");

        messageBuilder = new StaticMessageContentBuilder(testMessage);
        messageBuilder.getHeaderData().add("TestMessageData1");
        messageBuilder.getHeaderData().add("TestMessageData2");

        final Message message = messageBuilder.buildMessageContent(context, MessageType.PLAINTEXT.name());
        Assert.assertEquals(message.getPayload(), testMessage.getPayload());
        Assert.assertEquals(message.getHeader("header1"), testMessage.getHeader("header1"));
        Assert.assertNotEquals(message.getHeader(MessageHeaders.ID), testMessage.getHeader(MessageHeaders.ID));
        Assert.assertEquals(message.getHeaderData().size(), 2L);
        Assert.assertEquals(message.getHeaderData().get(0), "TestMessageData1");
        Assert.assertEquals(message.getHeaderData().get(1), "TestMessageData2");
    }

    @Test
    public void testBuildMessageContentWithAdditionalHeaderResource() {
        final Message testMessage = new DefaultMessage("TestMessage")
                .setHeader("header1", "value1");

        messageBuilder = new StaticMessageContentBuilder(testMessage);
        messageBuilder.getHeaderResources().add("classpath:com/consol/citrus/validation/builder/payload-data-resource.txt");

        final Message message = messageBuilder.buildMessageContent(context, MessageType.PLAINTEXT.name());
        Assert.assertEquals(message.getPayload(), testMessage.getPayload());
        Assert.assertEquals(message.getHeader("header1"), testMessage.getHeader("header1"));
        Assert.assertNotEquals(message.getHeader(MessageHeaders.ID), testMessage.getHeader(MessageHeaders.ID));
        Assert.assertEquals(message.getHeaderData().size(), 1L);
        Assert.assertEquals(message.getHeaderData().get(0), "TestMessageData");
    }

    @Test
    public void testBuildMessageContentWithMessageInterceptor() {
        final Message testMessage = new DefaultMessage("TestMessage")
                .setHeader("header1", "value1");

        messageBuilder = new StaticMessageContentBuilder(testMessage);
        messageBuilder.getMessageInterceptors().add(new AbstractMessageConstructionInterceptor() {
            @Override
            public boolean supportsMessageType(final String messageType) {
                return true;
            }
        });

        final Message message = messageBuilder.buildMessageContent(context, MessageType.PLAINTEXT.name());
        Assert.assertEquals(message.getPayload(), testMessage.getPayload());
        Assert.assertNotEquals(message.getHeader(MessageHeaders.ID), testMessage.getHeader(MessageHeaders.ID));
    }

    @Test
    public void testBuildMessageContentWithDataDictionary() {
        final Message testMessage = new DefaultMessage("TestMessage")
                .setHeader("header1", "value1");

        messageBuilder = new StaticMessageContentBuilder(testMessage);
        messageBuilder.setDataDictionary(new JsonMappingDataDictionary());

        final Message message = messageBuilder.buildMessageContent(context, MessageType.PLAINTEXT.name());
        Assert.assertEquals(message.getPayload(), testMessage.getPayload());
        Assert.assertEquals(message.getHeader("header1"), testMessage.getHeader("header1"));
        Assert.assertNotEquals(message.getHeader(MessageHeaders.ID), testMessage.getHeader(MessageHeaders.ID));
    }

    @Test
    public void testBuildMessageContentWithVariableSupport() {
        context.setVariable("payload", "TestMessage");
        context.setVariable("header", "value1");

        final Message testMessage = new DefaultMessage("${payload}")
                .setHeader("header1", "${header}");

        messageBuilder = new StaticMessageContentBuilder(testMessage);

        final Message message = messageBuilder.buildMessageContent(context, MessageType.PLAINTEXT.name());
        Assert.assertEquals(message.getPayload(), "TestMessage");
        Assert.assertNotEquals(message.getHeader(MessageHeaders.ID), testMessage.getHeader(MessageHeaders.ID));
        Assert.assertEquals(message.getHeader("header1"), "value1");
    }

    @Test
    public void testBuildMessageContentWithObjectPayload() {
        final Message testMessage = new DefaultMessage(1000)
                .setHeader("header1", 1000);

        messageBuilder = new StaticMessageContentBuilder(testMessage);

        final Message message = messageBuilder.buildMessageContent(context, MessageType.PLAINTEXT.name());
        Assert.assertEquals(message.getPayload(), testMessage.getPayload());
        Assert.assertNotEquals(message.getHeader(MessageHeaders.ID), testMessage.getHeader(MessageHeaders.ID));
        Assert.assertEquals(message.getHeader("header1"), testMessage.getHeader("header1"));
    }

    @Test
    public void testMessageTypeIsInHeaders() {

        //GIVEN
        messageBuilder = new StaticMessageContentBuilder(new DefaultMessage());
        final String messageType = "application/json";
        final TestContext testContext = mock(TestContext.class);
        when(testContext.resolveDynamicValuesInMap(any())).thenReturn(new HashMap<>());

        //WHEN
        final Map<String, Object> headers = messageBuilder.buildMessageHeaders(testContext, messageType);

        //THEN
        Assert.assertEquals(headers.get(MessageHeaders.MESSAGE_TYPE), messageType);
    }
}
