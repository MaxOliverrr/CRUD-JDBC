package model.dao;

import java.util.List;

public interface GenericDao<T> {
    void insert(T t);
    void update(T t);
    void deleteById(Integer id);
    void findById(Integer id);
    void findAll();
}
