package com.bluestone.app.admin.service.product.config.img;

import java.io.File;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.google.common.base.Throwables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bluestone.app.admin.service.AmazonS3ClientFactory;
import com.bluestone.app.admin.service.product.config.ProductUploadException;

/**
 * @author Rahul Agrawal
 *         Date: 9/19/12
 */
@Service
public class ImageUploader implements BSImageUploader {

    private static final Logger log = LoggerFactory.getLogger(ImageUploader.class);

    @Autowired
    private AmazonS3ClientFactory s3ClientFactory;

    @Value("${s3-bucket-name}")
    private String bucketName;


    @Override
    public void execute(String imageDirName) throws ProductUploadException {
        upload(imageDirName);
    }


    private void upload(String imageDirName) throws ProductUploadException {
        log.info("ImageUploader:Upload into bucket=[{}] files under [{}]", bucketName, imageDirName);
        File folder = new File(imageDirName);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            String fileName = null;
            if (listOfFiles[i].isFile()) {
                try {
                    fileName = listOfFiles[i].getName();
                    log.info("Start Uploading Image=[{}] .......", fileName);
                    PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, listOfFiles[i]);
                    putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
                    PutObjectResult putObjectResult = s3ClientFactory.getClient().putObject(putObjectRequest);
                    log.debug("Request key=" + putObjectRequest.getKey() + ", result: ETag={} , versionId={}",
                              putObjectResult.getETag(), putObjectResult.getVersionId());
                    log.debug("................Image {} upload was Successful.", fileName);
                } catch (Exception e) {
                    log.error("Image={} upload failed. ", listOfFiles[i].getAbsolutePath(), Throwables.getRootCause(e));
                    throw new ProductUploadException("Image :" + listOfFiles[i].getAbsolutePath() + " upload failed.\n");
                }
            }
        }
    }

}
