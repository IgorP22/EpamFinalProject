package com.podverbnyj.provider.DAO;


import java.sql.SQLException;
import java.util.ArrayList;
interface AbstractDAO<T> {

    abstract ArrayList<T> findAll() throws SQLException;
    abstract boolean create(T entity);
    abstract T getByName(String name);
    abstract boolean update(T entity);
    abstract boolean delete(T entity);






}
