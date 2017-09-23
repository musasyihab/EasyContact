package com.musasyihab.easycontact.network.request;

/**
 * Created by musasyihab on 9/22/17.
 */

public class ImageUploadRequest {
    private String upload_preset;
    private String file;

    public String getUpload_preset() {
        return upload_preset;
    }

    public void setUpload_preset(String upload_preset) {
        this.upload_preset = upload_preset;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
