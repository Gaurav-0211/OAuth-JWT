package com.oauth.exception;

public class ResourceNotFoundException extends RuntimeException{
    private String resourceName;
    private String resourceField;
    private Integer fieldValue;
    public ResourceNotFoundException(String resourceName, String resourceField, Integer fieldValue){
        super(String.format("%s not found with %s : %s", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.resourceField = resourceField;
        this.fieldValue = fieldValue;
    }
}
