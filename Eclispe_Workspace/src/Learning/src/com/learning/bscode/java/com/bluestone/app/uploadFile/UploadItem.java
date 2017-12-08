package com.bluestone.app.uploadFile;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class UploadItem {

    private String name;
    private CommonsMultipartFile fileData;
    private boolean refreshList = true;

    public String getName() {
        return name;
    }

    public boolean isRefreshList() {
        return refreshList;
    }

    public void setRefreshList(boolean refreshList) {
        this.refreshList = refreshList;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CommonsMultipartFile getFileData() {
        return fileData;
    }

    public void setFileData(CommonsMultipartFile fileData) {
        this.fileData = fileData;
    }
}