package com.bluestone.app.core;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.webflow.context.servlet.FlowUrlHandler;
import org.springframework.webflow.core.FlowException;
import org.springframework.webflow.execution.repository.FlowExecutionRestorationFailureException;
import org.springframework.webflow.execution.repository.snapshot.SnapshotNotFoundException;
import org.springframework.webflow.mvc.servlet.FlowHandlerAdapter;

public class CustomFlowHandlerAdapter extends FlowHandlerAdapter {
    
    private static final Logger log = LoggerFactory.getLogger(CustomFlowHandlerAdapter.class);
    
    @Override
    protected void defaultHandleException(String flowId, FlowException e, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        if (((e instanceof SnapshotNotFoundException) || (e instanceof FlowExecutionRestorationFailureException)) && flowId != null) {
            if (!response.isCommitted()) {
                log.warn("Not able to find flow with id {}", flowId);
                // by default, attempting to restart the flow. If possible then later on try to redirect to last succesfull flow id
                FlowUrlHandler flowUrlHandler = getFlowUrlHandler();
                String flowUrl = flowUrlHandler.createFlowDefinitionUrl(flowId, null, request);
                sendRedirect(flowUrl, request, response);
            }
        } else {
            super.defaultHandleException(flowId, e, request, response);
        }
    }
}
