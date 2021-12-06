package com.podverbnyj.provider.dao;

import com.podverbnyj.provider.dao.db.DBException;
import com.podverbnyj.provider.dao.db.DBUtils;
import com.podverbnyj.provider.dao.db.ServiceDBManager;
import com.podverbnyj.provider.dao.db.entity.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


public class ServiceDAO implements AbstractDAO<Service>{

    private static final Logger log = LogManager.getLogger(ServiceDAO.class);
    private static final DBUtils dbUtils = DBUtils.getInstance();
    private static final ServiceDBManager serviceDBManager = ServiceDBManager.getInstance();

    static ServiceDAO instance;

    public static synchronized ServiceDAO getInstance() {
        if (instance == null) {
            instance = new ServiceDAO();
        }
        return instance;
    }

    private ServiceDAO() {
        // no op
    }


    @Override
    public List<Service> findAll() throws DBException {
        Connection con = dbUtils.getConnection();
        try {
            return serviceDBManager.findAll(con);
        } catch (SQLException ex) {
            log.error("Can't receive list of services from DB", ex);
            throw new DBException("Can't receive list of services from DB");
        } finally {
            serviceDBManager.close(con);
        }
    }

    @Override
    public boolean create(Service service) throws DBException {
        Connection con = dbUtils.getConnection();
        try {
            return serviceDBManager.create(con, service);
        } catch (SQLException ex) {
            log.error("Can't create service ==> {}", service, ex);
            throw new DBException("Can't create service ==> " + service);
        } finally {
            serviceDBManager.close(con);
        }
    }


    @Override
    public Service getById(int id) throws DBException {
        Connection con = dbUtils.getConnection();
        try {
            return serviceDBManager.getById(con, id);
        } catch (SQLException ex) {
            log.error("Can't get service by ID ==> {}", id, ex);
            throw new DBException("Can't create service by ID ==> " + id);
        } finally {
            serviceDBManager.close(con);
        }
    }


    @Override
    public boolean update(Service service) throws DBException {
        Connection con = dbUtils.getConnection();
        try {
            return serviceDBManager.update(con, service);
        }catch (SQLException ex) {
            log.error("Can't update service ==> {}", service.getId(), ex);
            throw new DBException("Can't update service ==> " + service.getId());
        } finally {
            serviceDBManager.close(con);
        }
    }

    @Override
    public boolean delete(Service service) throws DBException {
        Connection con = dbUtils.getConnection();
        try {
            return serviceDBManager.delete(con, service);
        }catch (SQLException ex) {
            log.error("Can't delete service ==> {}", service.getId(), ex);
            throw new DBException("Can't delete service ==> " + service.getId());
        } finally {
            serviceDBManager.close(con);
        }
    }
}
