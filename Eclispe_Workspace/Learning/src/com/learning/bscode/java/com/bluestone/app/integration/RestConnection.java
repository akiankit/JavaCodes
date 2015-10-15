package com.bluestone.app.integration;

import javax.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class RestConnection {

    private static final Logger log = LoggerFactory.getLogger(RestConnection.class);

    @Autowired
    ApplicationContext applicationContext;


    @PostConstruct
    public void init() {
        if (applicationContext instanceof ConfigurableApplicationContext) {
            log.info("RestConnection.init(): Registering the shutdown hook********");
            ((ConfigurableApplicationContext) applicationContext).registerShutdownHook();
        }
    }

    // Create an HttpClient with the ThreadSafeClientConnManager.
    // This connection manager must be used if more than one thread will be using the HttpClient.

    @Autowired
    private HttpClient httpclient;

    public String executeGetRequest(String serviceUrl, String path, Map<String, Object> queryParams) throws Exception {
        log.debug("RestConnection.executeGetRequest(): GET ServiceUrl={}/{}", serviceUrl, path);
        HttpGet httpGet = null;
        try {
            URIBuilder builder = getURIBuilder(serviceUrl, path);
            if (queryParams != null && queryParams.size() > 0) {
                for (String eachKey : queryParams.keySet()) {
                    Object eachValue = queryParams.get(eachKey);
                    log.debug("\tQuery Param :[{}]=[{}]", eachKey, (eachValue == null ? "null" : eachValue));
                    if (eachValue != null) {
                        builder.setParameter(eachKey, eachValue.toString());
                    }
                }
            }
            httpGet = new HttpGet(builder.build());

            log.trace("Making GET Request for url: {}", httpGet.getURI().toString());
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseBody = httpclient.execute(httpGet, responseHandler, new BasicHttpContext());
            log.info("Response=[{}] for GET {}", responseBody, httpGet.getURI().toString());
            return responseBody;
        } catch (Exception exception) {
            log.error("Error executing GET http://" + serviceUrl + "/" + path + " : Reason = " + exception.toString(), exception);
            httpGet.abort();
            throw exception;
        }
    }


    public String executePostRequest(String serviceUrl, String path, Map<String, Object> formParams) throws Exception {
        log.debug("RestConnection.executePostRequest(): Post ServiceUrl={}/{}", serviceUrl, path);
        HttpPost httpPost = null;
        try {
            URIBuilder builder = getURIBuilder(serviceUrl, path);
            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            if (formParams != null && formParams.size() > 0) {
                for (String eachKey : formParams.keySet()) {
                    Object value = formParams.get(eachKey);
                    log.trace("key/value={}:{}", eachKey, (value == null ? "null" : value));
                    if (value != null) {
                        formparams.add(new BasicNameValuePair(eachKey, value.toString()));
                    }
                }
            }

            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
            httpPost = new HttpPost(builder.build());
            httpPost.setEntity(entity);

            log.trace("Making Post Request for url:{}", httpPost.getURI().toString());
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseBody = httpclient.execute(httpPost, responseHandler, new BasicHttpContext());
            log.debug("Response for POST {} = {}", httpPost.getURI().toString(), responseBody);
            return responseBody;
        } catch (Exception exception) {
            log.error("Error executing POST http://" + serviceUrl + "/" + path + ": Reason= " + exception.toString(), exception);
            httpPost.abort();
            throw exception;
        }
    }

    private URIBuilder getURIBuilder(String serviceUrl, String path) {
        URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setScheme("http");
        uriBuilder.setHost(serviceUrl);
        uriBuilder.setPath("/" + path);
        return uriBuilder;
    }
}
