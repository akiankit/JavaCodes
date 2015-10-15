package com.bluestone.app.admin.service.product.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bluestone.app.core.util.ImageEnum;
import com.bluestone.app.core.util.ImageProperties;
import com.bluestone.app.design.model.Design;
import com.bluestone.app.design.model.DesignImageView;

/**
 * @author Rahul Agrawal
 *         Date: 9/20/12
 */
@Service
public class DesignViewHelper {

    @Autowired
    private ProductUploadContext productUploadContext;

    private static final Logger log = LoggerFactory.getLogger(DesignViewHelper.class);

    public void updateDesignView(Design design) throws ProductUploadException {
        log.debug("DesignViewHelper.updateDesignView() for Design=[{}]", design.getDesignCode());
        getCreateDesignView(design.getDesignCode(),
                            design.getDesignCategory().getCategoryType(),
                            true,
                            design.getDesignImageView());

    }

    public void createDesignView(Design design) throws ProductUploadException {
        log.debug("DesignViewHelper.createDesignView()");
        getCreateDesignView(design.getDesignCode(),
                            design.getDesignCategory().getCategoryType(),
                            false,
                            design.getDesignImageView());

    }

    void getCreateDesignView(String designCode, String category, boolean isUpdate, DesignImageView designImageView) throws ProductUploadException {
        log.info("DesignViewHelper.getCreateDesignView(): Design Code=[{}] Category=[{}] isUpdate=[{}]\n",
                 designCode, category, Boolean.toString(isUpdate));

        List<ImageEnum> listOfImageViews = getImageTypeList(designCode);

        //@todo : Rahul Agrawal : //shd we do any validation for missing images ???

        if (isUpdate) {
            designImageView.clearAllViews();
        }

        // DesignImageView designImageView = new DesignImageView();
        for (ImageEnum imageEnum : listOfImageViews) {
            switch (imageEnum) {
                case BAND:
                    designImageView.setHasBand(true);
                    break;
                case BACK:
                    designImageView.setHasBack(true);
                    break;
                case CLOSELAYDOWN:
                    designImageView.setHasCloseLaydown(true);
                    break;
                case CLOSEUP:
                    designImageView.setHasCloseUp(true);
                    break;
                case FRONT:
                    designImageView.setHasFront(true);
                    break;
                case FRONTONE:
                    designImageView.setHasFrontOne(true);
                    break;
                case HAND:
                    designImageView.setHasHand(true);
                    break;
                case PERSPECTIVE:
                    designImageView.setHasPerspective(true);
                    break;
                case PERSPECTIVE2NOS:
                    designImageView.setHasPerspective2Nos(true);
                    break;
                case SCALE:
                    designImageView.setHasScale(true);
                    break;
                case SIDE:
                    designImageView.setHasSide(true);
                    break;
                case TOP:
                    designImageView.setHasTop(true);
                    break;
                case NECK:
                    designImageView.setHasNeck(true);
                    break;
                case EARRING:
                    designImageView.setHasEarring(true);
                    break;
                case BANGLEHAND:
                    designImageView.setHasBangleHand(true);
                    break;
                default:
                    break;
            }
        }
    }

    private List<ImageEnum> getImageTypeList(String designCode) throws ProductUploadException {
        //  ##### ~/codeXXXX/Render/
        String imagesDirPath = this.productUploadContext.getImagesDirPath(designCode);
        log.info("DesignViewHelper.getImageTypeList() from directory=[{}]", imagesDirPath);
        String aFile;
        File folder = new File(imagesDirPath);
        File[] listOfFiles = folder.listFiles();
        List<ImageEnum> listOfImageViews = new ArrayList<ImageEnum>();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                aFile = listOfFiles[i].getName();
                if (aFile.equalsIgnoreCase("thumbs.db")) {
                    log.info("Skipping the file as it is a thumbs.db");
                } else {
                    String[] parts = aFile.split("_");
                    final String lastPart = parts[parts.length - 1].toLowerCase();
                    final String shortName = lastPart.replace(ImageProperties.imageExtension, " ").trim();
                    final ImageEnum fullName = ImageProperties.imageViewNames.get(shortName);
                    log.info("Image Short Name=[{}] , Full Name=[{}]", shortName, fullName);
                    if (fullName != null) {
                        listOfImageViews.add(fullName);
                    } else {
                        throw new ProductUploadException("Invalid image type = " + shortName + " in " + aFile);
                    }
                }
            }
        }
        return listOfImageViews;
    }

    /* public LinkedList<String> checkForMissingImages(String category,String code,String dirname) {
        List<ImageEnum> listOfImageViews = ImageResizeUtil2.getImageTypeList(code, productUploadPath,dirname);
        final LinkedList<String> errors = new LinkedList<String>();
        List<ImageEnum> imageReqList = ImageProperties.reqImageMap.get(category.toLowerCase());
        imageReqList.removeAll(listOfImageViews);
        if (imageReqList.size() > 0) {
            String mError = "Missing images:";
            for (ImageEnum imageEnum : imageReqList) {
                mError = mError.concat(imageEnum.getFullName());
                mError = mError.concat(" ");
            }
            errors.add(mError);
        }
        return errors;
    }*/

}
