package com.bluestone.app.admin.service.product.config;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.bluestone.app.design.dao.DesignDao;

@Service
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
public class ProductListingService {

    private static final Logger log = LoggerFactory.getLogger(ProductListingService.class);

    @Autowired
    private DesignDao designdao;

    @Autowired
    private ProductUploadContext productUploadContext;

    @PostConstruct
    public void init() {
        Assert.notNull(productUploadContext, "ProductUploadContext should not be null");
    }

    public Map<String, Object> getProductAndImageList() {
        log.info("ProductListingService.getProductAndImageList()");

        Map<String, Object> lists = new HashMap<String, Object>();
        List<String> designCodes = new ArrayList<String>();
        String eachDirectoryName = null;

        File rootFolder = new File(productUploadContext.getProductUploadPath());
        log.info("ProductListingService.getProductAndImageList(): ProductUploadPath={}", rootFolder.getAbsolutePath());

        File[] listOfFiles = rootFolder.listFiles();

        if (listOfFiles != null) {
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isDirectory()) {
                    eachDirectoryName = listOfFiles[i].getName();
                    designCodes.add(eachDirectoryName);
                }
            }
            List<String> listOfDesignCodes = null;
            List<String> customizationList = new ArrayList<String>();
            if (designCodes.size() > 0) {
                listOfDesignCodes = designdao.getDesignCodeListByCode(designCodes);
            }
            if (listOfDesignCodes != null) {
                designCodes.removeAll(listOfDesignCodes);

                for (String designCode : listOfDesignCodes) {
                    //String path = productUploadPath + File.separator + designCode + File.separator + "Render";
                    String path = productUploadContext.getImagesDirPath(designCode);
                    File file = new File(path);
                    if (!file.exists()) {
                        customizationList.add(designCode);
                    }
                }
            }
            if (customizationList.size() > 0 && listOfDesignCodes != null) {
                listOfDesignCodes.removeAll(customizationList);
            }
            lists.put("alreadyUploaded", listOfDesignCodes);
            lists.put("forUpload", designCodes);
            lists.put("forCustomization", customizationList);
        }
        log.info("ProductListingService.getProductAndImageList() {}", lists);
        return lists;
    }
}
