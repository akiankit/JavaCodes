package com.bluestone.app.core.util;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;

public class FileUtil {
    
    private static final Logger log = LoggerFactory.getLogger(FileUtil.class);
    
    public static String generateUniqueFileName(String FILE_NAME_PREFIX, String fileExtension) {
        String unique = UUID.randomUUID().toString();
        String fileName = FILE_NAME_PREFIX + unique;
        return System.getProperty("java.io.tmpdir") + File.separator + fileName.replaceAll("[^a-zA-Z0-9]+", "") + fileExtension;
    }
    
    public static File getFile(final String fileName) {
        File file = new File(fileName);
        final String fileAbsolutePath = file.getAbsolutePath();
        log.debug("FileUtil.getFile() : {}", fileAbsolutePath);
        if (file.exists()) {
            log.debug("{} exists , hence deleting it and creating a new one.", fileAbsolutePath);
            file.delete();
            try {
                boolean isNewFile = file.createNewFile();
                if (isNewFile) {
                    log.debug("A new file with the name {} was created successfully", fileAbsolutePath);
                } else {
                    log.error("Failed to create a new file-{}. Check if the file {} already exists", fileName, fileAbsolutePath);
                }
            } catch (IOException e) {
                log.error("file download - {} file creation failed.", fileAbsolutePath, e);
                throw new RuntimeException(e);
            }
        } else {
            log.debug("No previous File - {} exists. So no deletion required", fileAbsolutePath);
        }
        return file;
    }
    
    public static void closeStream(Closeable closable) {
        if (closable != null) {
            try {
                closable.close();
            } catch (Exception e) {
                log.warn("Error while closing file stream {}", closable, e);
            }
        }
    }
    
   /* public static void copyFileDataToHttpResponse(File file, HttpServletResponse response) throws IOException {
        log.info("AdminUtilityController.copyFileDataToHttpResponse()");
        FileInputStream fileInputStream = null;
        OutputStream outputStream = null;
        try {
        	if(file!= null){
	            response.setContentType("text/csv");
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
            log.error("AdminUtilityController.copyFileDataToHttpResponse(): File {} does not exits.",
                      (file != null ? file.getAbsolutePath() : "null") , fileNotFoundException);
        }
    }*/
}
