package com.fattech.twitterclone.repos;

import com.fattech.twitterclone.models.BaseModel;

import java.util.List;

public interface EntityDAO<T extends BaseModel> {
    Long save(T entity);

    Long update(T entity, Long id);

    Long delete(Long id);

    List<T> getAll();

    T getById(Long id);
}
