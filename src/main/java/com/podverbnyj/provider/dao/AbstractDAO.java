package com.podverbnyj.provider.dao;


import com.podverbnyj.provider.dao.db.DBException;


import java.util.List;

interface AbstractDAO<T> {

    List<T> findAll() throws DBException;

    boolean create(T entity) throws DBException;

    T getById(int id) throws DBException;

    boolean update(T entity) throws DBException;

    boolean delete(T entity) throws DBException;


}
