package com.bluestone.app.admin.service.product.config.customization.derived;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.google.common.base.Throwables;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.admin.service.AmazonS3ClientFactory;
import com.bluestone.app.admin.service.UniwareCreateUpdateCustomizationService;
import com.bluestone.app.admin.service.product.config.ProductUploadException;
import com.bluestone.app.design.model.Customization;
import com.bluestone.app.design.model.Image;

/**
 * @author Rahul Agrawal
 *         Date: 3/19/13
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = true)
public class DerivedCustomizationIntegrationProcessor {

    private static final Logger log = LoggerFactory.getLogger(DerivedCustomizationIntegrationProcessor.class);

    @Autowired
    private AmazonS3ClientFactory s3ClientFactory;

    @Value("${s3-bucket-name}")
    private String bucketName;

    @Autowired
    private UniwareCreateUpdateCustomizationService uniwareCreateUpdateCustomizationService;

    public void pushToS3AndUniware(final Customization parentCustomization, Set<Customization> derivedCustomization) throws ProductUploadException {
        log.info("DerivedCustomizationIntegrationProcessor.pushToS3AndUniware(): ParentCustomizationId=[{}] , S3 Bucket=[{}]", parentCustomization.getId(), bucketName);
        for (Customization eachCustomization : derivedCustomization) {
            final int parentImageVersionNo = parentCustomization.getImageVersion();
            eachCustomization.setImageVersion(parentImageVersionNo);

            copyParentImagesToS3(parentCustomization, eachCustomization);
            log.info("DerivedCustomizationIntegrationProcessor:CustomizationId=[{}] Setting Image version to [{}] . Please verify the same in the database.",
                     parentCustomization.getId(), parentImageVersionNo);

            uniwareCreateUpdateCustomizationService.execute(eachCustomization);
            log.info("***** Success for DesignId=[{}] Derived CustomizationId=[{}] Sku=[{}] **** \n", eachCustomization.getId(), eachCustomization.getSkuCode());
        }
    }


    private void copyParentImagesToS3(final Customization parentCustomization, final Customization newCustomization) throws ProductUploadException {
        final long newCustomizationId = newCustomization.getId();
        final String newCustomizationSkuCode = newCustomization.getSkuCode();
        log.info("DerivedCustomizationIntegrationProcessor.copyParentImagesToS3():Parent->CustId=[{}] Sku=[{}] Child->CustId=[{}] Sku=[{}]",
                 parentCustomization.getId(), parentCustomization.getSkuCode(), newCustomizationId, newCustomizationSkuCode);
        List<Image> imageUrls = parentCustomization.getImageUrls();
        List<Image> newImageUrls = newCustomization.getImageUrls();
        int i = 0;
        for (Image parentCustomizationImage : imageUrls) {
            final Map<String, String> oldCustomizationSizeVsImageUrlMap = parentCustomizationImage.getSizeVsUrl();
            final Image newCustomizationImage = newImageUrls.get(i);
            i++;
            for (String eachSize : oldCustomizationSizeVsImageUrlMap.keySet()) {
                String newUrl = newCustomizationImage.getImageOfSize(eachSize);
                String urlTobeCopied = oldCustomizationSizeVsImageUrlMap.get(eachSize);

                String oldFileName = urlTobeCopied.substring(1);
                final String[] oldKeyNameAndVersion = getKeyAndVersion(oldFileName);

                String newFileName = newUrl.substring(1);
                final String[] newKeyNameAndVersion = getKeyAndVersion(newFileName);
                AmazonS3Client amazonS3Client = null;
                log.debug("Copying ImagesToS3():[src-file={}] [target-file={}]", oldFileName, newFileName);
                try {
                    amazonS3Client = s3ClientFactory.getClient();
                    CopyObjectRequest copyObjectRequest = new CopyObjectRequest(bucketName, oldKeyNameAndVersion[0],
                                                                                bucketName, newKeyNameAndVersion[0]);
                    copyObjectRequest.setCannedAccessControlList(CannedAccessControlList.PublicRead);
                    amazonS3Client.copyObject(copyObjectRequest);
                } catch (Exception e) {
                    log.error("Error: DerivedCustomizationIntegrationProcessor.uploadToS3(): Derived CustomizationId=[{}] Sku=[{}] Reason={}",
                              newCustomizationId, newCustomizationSkuCode, e.toString(), Throwables.getRootCause(e));
                    throw new ProductUploadException(
                            "Error in copying images to S3 for Derived CustomizationId=" + newCustomizationId + " Sku=" + newCustomizationSkuCode);
                }
            }
        }
    }

    private String[] getKeyAndVersion(String fileName) {
        //return fileName.split("\\?");
        return StringUtils.split(fileName, "\\?");
    }

}
