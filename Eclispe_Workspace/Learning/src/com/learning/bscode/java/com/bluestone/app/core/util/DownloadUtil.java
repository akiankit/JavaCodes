package com.bluestone.app.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;

public class DownloadUtil {
    
    private static final Logger log = LoggerFactory.getLogger(DownloadUtil.class);
    
    public static void copyFileDataToHttpResponse(File file, HttpServletResponse response, String contentType) throws IOException {
        log.info("DownloadUtil.copyFileDataToHttpResponse()");
        FileInputStream fileInputStream = null;
        OutputStream outputStream = null;
        try {
            if(file!= null){
                response.setContentType(contentType);
                response.setContentLength(new Long(file.length()).intValue());
                response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
                fileInputStream = new FileInputStream(file);
                outputStream = response.getOutputStream();
    
                //Simple utility methods for file and stream copying.
                // All copy methods use a block size of 4096 bytes, and ***close all**** affected streams when done.
                FileCopyUtils.copy(fileInputStream, outputStream);
                boolean fileWasDeleted = file.delete();
                if(fileWasDeleted) {
                    log.debug("Successfully deleted the file {}", file.getAbsolutePath());
                } else {
                    log.warn("Failed to delete the file {}", file.getAbsolutePath());
                }
            }
        } catch (FileNotFoundException fileNotFoundException) {
            log.error("DownloadUtil.copyFileDataToHttpResponse(): File {} does not exits.",
                      (file != null ? file.getAbsolutePath() : "null") , fileNotFoundException);
        }
    }

    private static File writeContentToFile(String text, String fileNamePrefix, String fileExtension) throws Exception {
        final String generatedFileName = FileUtil.generateUniqueFileName(fileNamePrefix, fileExtension);
        FileWriter fileWriter = null;
        File file = null;
        try {
            file = FileUtil.getFile(generatedFileName);
            fileWriter = new FileWriter(file);
            fileWriter.write(text);
            fileWriter.flush();
            return file;
        } catch (Exception exception) {
            log.error("Error while writing contents to file {}", (file != null ? file.getAbsolutePath() : ""), exception);
            throw exception;
        } finally {
            FileUtil.closeStream(fileWriter);
        }
    }

    public static void copyDataToHttpResponse(String fileNamePrefix, HttpServletResponse response, String contentType, String text, String fileExtension) throws Exception {
        File productDataToFile = writeContentToFile(text, fileNamePrefix, fileExtension);
        copyFileDataToHttpResponse(productDataToFile, response, contentType);
    }

}
