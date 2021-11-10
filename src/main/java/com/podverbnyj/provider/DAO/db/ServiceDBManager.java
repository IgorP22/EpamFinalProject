package com.podverbnyj.provider.DAO.db;

import com.podverbnyj.provider.DAO.db.entity.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static com.podverbnyj.provider.DAO.db.entity.constant.SQLConstant.ServiceConstants.*;


public class ServiceDBManager {

    private static final Logger log = LogManager.getLogger(ServiceDBManager.class);


    static ServiceDBManager instance;

    public static synchronized ServiceDBManager getInstance() {
        if (instance == null) {
            instance = new ServiceDBManager();
        }
        return instance;
    }

    private ServiceDBManager() {
        // no op
    }

    public ArrayList<Service> findAll(Connection con) throws SQLException {
        ArrayList<Service> services = new ArrayList<>();
        ResultSet rs = null;
        Service service;
        try {
            rs = con.createStatement().executeQuery(FIND_ALL_SERVICES);
            while (rs.next()) {
                service = getService(rs);
                services.add(service);
            }
            return services;
        } finally {
            close(rs);
        }
    }

    public boolean create(Connection con, Service service) throws SQLException {
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(CREATE_SERVICE);
            setServiceStatement(service, ps);
            ps.executeUpdate();
            return true;
        } finally {
            close(ps);
        }
    }



    public Service getById(Connection con, int id) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Service service = null;
        try {
            ps = con.prepareStatement(GET_SERVICE_BY_ID);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                service = getService(rs);
            }
            return service;
        } finally {
            close(rs);
            close(ps);
        }
    }

    public boolean update(Connection con, Service service) throws SQLException {
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(UPDATE_SERVICE);
            setServiceStatement(service, ps);
            ps.setInt(3, service.getId());
            ps.executeUpdate();
            return true;
        } finally {
            close(ps);
        }
    }

    public boolean delete(Connection con, Service service) throws SQLException {
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(DELETE_SERVICE_BY_ID);
            ps.setInt(1, service.getId());
            ps.executeUpdate();
            return true;
        } finally {
            close(ps);
        }
    }

    public void close(AutoCloseable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (Exception ex) {
                log.error("Error closing resource " + resource, ex);
            }
        }
    }

    private void setServiceStatement(Service service, PreparedStatement ps) throws SQLException {
        int index = 1;
        ps.setString(index++, service.getTitleRu());
        ps.setString(index++, service.getTitleEn());

    }

    private Service getService(ResultSet rs) throws SQLException {
        Service service = null;

        service.setId(rs.getInt("service_id"));
        service.setTitleRu(rs.getString("service_id"));
        service.setTitleEn(rs.getString("service_id"));

        log.trace("Service created ==> " + service);
        return service;
    }
}