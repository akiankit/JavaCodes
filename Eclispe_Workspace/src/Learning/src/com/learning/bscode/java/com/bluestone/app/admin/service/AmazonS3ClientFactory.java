package com.bluestone.app.admin.service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Rahul Agrawal
 *         Date: 9/19/12
 */
@Service
public class AmazonS3ClientFactory {

    private static final Logger log = LoggerFactory.getLogger(AmazonS3ClientFactory.class);

    @Value("${s3-access-id}")
    private String accessId;

    @Value("${s3-access-key}")
    private String secretKey;

/*
     ***** Does not work this way #########  don't know why - need to find out

    @Value("${applicationProperties.s3-access-id}")
    private String accessId;

    @Value("${applicationProperties.s3-access-key}")
    private String secretKey;

    @Value("${applicationProperties.s3-bucket-name}")
    private String bucketName;
*/


    private AmazonS3Client amazonS3Client;

    @PostConstruct
    public void init() {
        log.info("AmazonS3ClientFactory.init()");
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessId, secretKey);
        amazonS3Client = new AmazonS3Client(credentials);
    }

    public AmazonS3Client getClient() {
        return amazonS3Client;
    }

    @PreDestroy
    public void destroy() {
        log.info("AmazonS3ClientFactory.destroy()");
        amazonS3Client.shutdown();
    }

}
