package com.fattech.twitterclone.models;

public abstract class BaseModel {
    private Long id;

    private Long createdAt;

    private Long lastModified;

    public BaseModel() {
    }

    public BaseModel(Long id, Long createdAt, Long lastModified) {
        this.id = id;
        this.createdAt = createdAt;
        this.lastModified = lastModified;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getLastModified() {
        return lastModified;
    }

    public void setLastModified(Long lastModified) {
        this.lastModified = lastModified;
    }
}
