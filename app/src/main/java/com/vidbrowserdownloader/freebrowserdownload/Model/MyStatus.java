package com.vidbrowserdownloader.freebrowserdownload.Model;

import android.net.Uri;

public class MyStatus {
    String name;
    String path;


    Uri uri;


    public MyStatus(String path, String name, Uri uri) {


        this.path = path;
        this.name = name;
        this.uri = uri;
    }




    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

}
