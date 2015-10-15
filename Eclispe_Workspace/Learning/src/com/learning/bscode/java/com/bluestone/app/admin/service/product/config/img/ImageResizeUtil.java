package com.bluestone.app.admin.service.product.config.img;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bluestone.app.admin.service.product.config.ProductUploadException;
import com.bluestone.app.core.util.ImageProperties;
import com.bluestone.app.design.model.Customization;


class ImageResizeUtil {

    private static final Logger log = LoggerFactory.getLogger(ImageResizeUtil.class);

    final static private Integer[] IMAGE_SIZE_DIMENSION = ImageProperties.imageSizeDimension;
    final static private String[] IMAGE_SIZE = ImageProperties.imageSizes;


    private static BufferedImage resizeImageWithHint(BufferedImage originalImage, int width) {
        return Scalr.resize(originalImage, Scalr.Method.QUALITY, Scalr.Mode.FIT_EXACT, width, null);
    }

    /**
     * This will generate only the resized images for the Parent customization. For all others customizations that share these images,
     * we will copy the files internally in S3 to save time.
     *
     * @param designCode
     * @param parentCustomization
     * @return
     * @throws ProductUploadException
     */
    static String resizeAndRenameImages(String designCode, final Customization parentCustomization, String srcImageDirPath) throws ProductUploadException {
        log.info("ImageResizeUtil.resizeAndRenameImages(): DesignCode=[{}] Parent CustomizationId=[{}] Sku=[{}] \nSource image dir=[{}]",
                 designCode, parentCustomization.getId(), parentCustomization.getSkuCode(), srcImageDirPath);
        final String outputDirPath = srcImageDirPath + File.separator + "temp";
        File file = new File(srcImageDirPath);

        if (file.exists()) {
            log.info("ImageResizeUtil: Verified that Path=[{}] exists.", file.getAbsolutePath());
        } else {
            log.error("Error : Design=[{}] CustomizationId=[{}] : Could not Render folder in [{}] ", designCode, parentCustomization.getId(), srcImageDirPath);
            throw new ProductUploadException("Could not Render folder in " + srcImageDirPath);
        }

        final File tempDir = new File(outputDirPath);
        try {
            if (tempDir.exists()) {
                FileUtils.deleteDirectory(tempDir);
            }
        } catch (IOException e) {
            log.error("Error: ImageResizeUtil.resizeAndRenameImages(): Failed to delete the existing Temp Directory - {}", tempDir);
        }

        final boolean makeTempDir = tempDir.mkdirs();
        if (makeTempDir) {
            log.info("ImageResizeUtil: Successful Creation of temp directory=[{}]", tempDir.getAbsolutePath());
        } else {
            if (tempDir.exists()) {
                log.error("Error: ImageResizeUtil.resizeAndRenameImages(): Temp Directory {} already exists.", tempDir.getAbsolutePath());
            }
            log.error("Could not create the temp directory under {} . If the temp directory already exists, then remove it and try again ", srcImageDirPath);
            throw new ProductUploadException("Could not create the temp directory under [" + srcImageDirPath +
                                             "] If the temp directory already exists, then remove it and try again.");
        }


        File[] listOfFiles = file.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            String eachFileName;
            File eachFile = listOfFiles[i];
            log.info("ImageResizeUtil:Processing file:[{}]", eachFile.getName());
            if (eachFile.isFile()) {
                eachFileName = eachFile.getName();
                if (!eachFileName.equalsIgnoreCase("thumbs.db")) {
                    BufferedImage originalImage = null;
                    BufferedImage originalImage512 = null;
                    try {
                        originalImage = ImageIO.read(new File(srcImageDirPath + File.separator + eachFileName));
                        String[] parts = eachFileName.split("_");
                        final String lastPart = parts[parts.length - 1].toLowerCase();
                        final String lastPartMinusExtension = lastPart.replace(ImageProperties.imageExtension, " ").trim();

                        int type = getImageType(originalImage);
                        //originalImage512 = resizeImageWithHint(originalImage, type, 512, 512);
                        originalImage512 = resizeImageWithHint(originalImage, 512);
                        File outFile = null;

                        for (int j = 0; j < IMAGE_SIZE_DIMENSION.length; j++) {
                            BufferedImage imageToBeUsed = null;
                            if (IMAGE_SIZE_DIMENSION[j] == 0) {
                                imageToBeUsed = originalImage;
                                outFile = null;
                            } else {
                                if (outFile != null) {
                                    originalImage512 = ImageIO.read(outFile);
                                    if (IMAGE_SIZE_DIMENSION[j] < 200) {
                                        //originalImage512 = resizeImageWithHint(originalImage512, type, 100, 100);
                                        originalImage512 = resizeImageWithHint(originalImage512, 100);
                                    }
                                }
                                //imageToBeUsed = resizeImageWithHint(originalImage512, type, IMAGE_SIZE_DIMENSION[j], IMAGE_SIZE_DIMENSION[j]);
                                imageToBeUsed = resizeImageWithHint(originalImage512, IMAGE_SIZE_DIMENSION[j]);
                            }

                            outFile = new File(outputDirPath + File.separator
                                               + parentCustomization.getSkuCode()
                                               + "-"
                                               + lastPartMinusExtension
                                               + "-"
                                               + IMAGE_SIZE[j]
                                               + ImageProperties.imageExtension);
                            log.debug("ImageResizeUtil:Producing images for CustomizationId=[{}] Sku=[{}] ImageFile=[{}]",
                                      parentCustomization.getId(), parentCustomization.getSkuCode(), outFile.getAbsolutePath());
                            ImageIO.write(imageToBeUsed, ImageProperties.resizeImageExtension, outFile);
                        }
                    } catch (IOException e) {
                        log.error("Error during image resizing for Design Code=[{}] ", designCode, e);
                        throw new ProductUploadException("Error during image resizing for Design Code " + designCode + " " + e.toString(), e);
                    } finally {
                        if (originalImage != null) {
                            originalImage.flush();
                        }
                        if (originalImage512 != null) {
                            originalImage512.flush();
                        }
                    }
                }
            } else {
                log.error("Error: ImageResizeUtil.resizeAndRenameImages(): Directory=[{}] has a file=[{}] which not a regular file", srcImageDirPath, eachFile.getName());
            }
        }
        return outputDirPath;
    }

    private static int getImageType(BufferedImage originalImage) {
        log.debug("ImageResizeUtil.getImageType():{}");
        if (originalImage.getType() == 0) {
            return BufferedImage.TYPE_INT_ARGB;
        } else {
            return originalImage.getType();
        }
    }
}

