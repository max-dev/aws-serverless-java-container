/*
 * Copyright 2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance
 * with the License. A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package com.amazonaws.serverless.proxy.internal.servlet;

import com.amazonaws.serverless.exceptions.InvalidRequestEventException;
import com.amazonaws.serverless.proxy.RequestReader;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.ContainerConfig;
import com.amazonaws.services.lambda.runtime.Context;

import javax.ws.rs.core.SecurityContext;

/**
 * Simple implementation of the <code>RequestReader</code> interface that receives an <code>AwsProxyRequest</code>
 * object and uses it to initialize a <code>AwsProxyHttpServletRequest</code> object.
 */
public class AwsProxyHttpServletRequestReader extends RequestReader<AwsProxyRequest, AwsProxyHttpServletRequest> {

    //-------------------------------------------------------------
    // Methods - Implementation
    //-------------------------------------------------------------

    @Override
    public AwsProxyHttpServletRequest readRequest(AwsProxyRequest request, SecurityContext securityContext, Context lambdaContext, ContainerConfig config)
            throws InvalidRequestEventException {
        request.setPath(stripBasePath(request.getPath(), config));
        AwsProxyHttpServletRequest servletRequest = new AwsProxyHttpServletRequest(request, lambdaContext, securityContext, config);
        servletRequest.setAttribute(API_GATEWAY_CONTEXT_PROPERTY, request.getRequestContext());
        servletRequest.setAttribute(API_GATEWAY_STAGE_VARS_PROPERTY, request.getStageVariables());
        servletRequest.setAttribute(LAMBDA_CONTEXT_PROPERTY, lambdaContext);

        return servletRequest;
    }

    //-------------------------------------------------------------
    // Methods - Protected
    //-------------------------------------------------------------

    @Override
    protected Class<? extends AwsProxyRequest> getRequestClass() {
        return AwsProxyRequest.class;
    }
}
