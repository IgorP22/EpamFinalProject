package com.podverbnyj.provider.DAO;


import com.podverbnyj.provider.DAO.db.DBException;

import java.sql.SQLException;
import java.util.ArrayList;
interface AbstractDAO<T> {

    abstract ArrayList<T> findAll() throws DBException;
    abstract boolean create(T entity) throws DBException;
    abstract T getById(int id) throws DBException;
    abstract boolean update(T entity) throws DBException;
    abstract boolean delete(T entity) throws DBException;






}
