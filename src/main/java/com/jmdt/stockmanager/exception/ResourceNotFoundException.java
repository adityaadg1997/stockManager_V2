package com.jmdt.stockmanager.exception;

public class ResourceNotFoundException extends RuntimeException {

    String resource;
    String resourceName;
    String resourceValue;

    public ResourceNotFoundException() {
    }

    public ResourceNotFoundException(String resource, String resourceName, String resourceValue) {
        super(resource + " with " + resourceName +" "+ resourceValue + " Not Found! ");
        this.resource = resource;
        this.resourceName = resourceName;
        this.resourceValue = resourceValue;
    }
}
