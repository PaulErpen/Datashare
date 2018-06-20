package com.github.paulerpen.datashare.Storageservice;

import org.springframework.boot.context.properties.ConfigurationProperties;
/**
 * Object being handled by bean of the same name,
 * in order to be a wrapper to the root location of a storageservice
 * @author paul
 *
 */
@ConfigurationProperties("storage")
public class StorageProperties {

    /**
     * Folder location for storing files
     */
    private String location = "";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
