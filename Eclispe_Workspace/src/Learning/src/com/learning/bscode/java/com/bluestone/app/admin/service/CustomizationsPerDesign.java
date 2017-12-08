package com.bluestone.app.admin.service;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.CopyObjectResult;
import com.google.common.base.Throwables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.design.model.Customization;
import com.bluestone.app.design.model.Image;

@Service
@Transactional(readOnly = true)
public class CustomizationsPerDesign {

    private static final Logger log = LoggerFactory.getLogger(CustomizationsPerDesign.class);

    @Autowired
    private AmazonS3ClientFactory s3ClientFactory;

    @Value("${s3-bucket-name}")
    private String bucketName;
    //private String bucketName;

    @PostConstruct
    public void init() {
        log.debug("CustomizationsPerDesign.init()");
        log.info("CustomizationsPerDesign : s3-bucket-name={}", bucketName);
    }

    public void uploadToS3(Customization baseCustomization, Customization customization) {
        log.info("CustomizationsPerDesign.uploadToS3(): BucketName=[{}] BaseCustomizationSku=[{}], Customization Sku=[{}]",
                 bucketName, baseCustomization.getSkuCode(), customization.getSkuCode());
        List<Image> imageUrls = baseCustomization.getImageUrls();
        List<Image> newImageUrls = customization.getImageUrls();
        int i = 0;
        for (Image oldCustomizationImage : imageUrls) {
            Map<String, String> oldCustomizationSizeVsImageUrlMap = oldCustomizationImage.getSizeVsUrl();
            Image newCustomizationImage = newImageUrls.get(i);
            i++;
            for (String eachSize : oldCustomizationSizeVsImageUrlMap.keySet()) {
                String newUrl = newCustomizationImage.getImageOfSize(eachSize);
                String urlTobeCopied = oldCustomizationSizeVsImageUrlMap.get(eachSize);

                String oldFileName = urlTobeCopied.substring(1);
                String[] oldKeyNameAndVersion = oldFileName.split("\\?");

                String newFileName = newUrl.substring(1);
                String[] newKeyNameAndVersion = newFileName.split("\\?");

                log.info("old key=[{}]   new key=[{}] ", oldKeyNameAndVersion[0], newKeyNameAndVersion[0]);
                try {
                    CopyObjectRequest copyObjectRequest = new CopyObjectRequest(bucketName, oldKeyNameAndVersion[0], bucketName, newKeyNameAndVersion[0]);
                    copyObjectRequest.setCannedAccessControlList(CannedAccessControlList.PublicRead);
                    CopyObjectResult copyObject = s3ClientFactory.getClient().copyObject(copyObjectRequest);
                } catch (AmazonServiceException e) {
                    log.error("Error: CustomizationsPerDesign.uploadToS3(): {} {} ", e.toString(), Throwables.getRootCause(e));
                    throw e;
                }
            }
        }
    }
}
