package com.bluestone.app.admin.service.product.config;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bluestone.app.core.util.DateTimeUtil;

/**
 * @author Rahul Agrawal
 *         Date: 3/19/13
 */
class Cleaner {

    private static final Logger log = LoggerFactory.getLogger(Cleaner.class);

    static void backupAndDelete(String designCode, String productUploadPath) {
        log.info("No errors during product upload. Going to delete the files used for product upload [{}{}{}]",
                 productUploadPath, File.separator, designCode);
        final File src = new File(productUploadPath + File.separator + designCode);
        try {
            final String now = DateTimeUtil.formatDate(DateTimeUtil.getDate(), "yyyyMMddHHmmss");
            File destinationDir = new File("/tmp", (designCode + "_" + now));
            FileUtils.copyDirectoryToDirectory(src, destinationDir);
            delete(src);
            log.info("Copied to /tmp and Deleted all the files under {} successfully.", src.getAbsolutePath());

        } catch (Exception e) {
            log.error("Error while deleting src file=[{}]", src.getAbsolutePath(), e);
        }
    }

    static void delete(File file) throws IOException {
        log.debug("Deleting file {}", file.getAbsolutePath());
        FileUtils.forceDelete(file);
    }
}
