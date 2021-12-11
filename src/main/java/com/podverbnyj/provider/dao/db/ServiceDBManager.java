package com.podverbnyj.provider.dao.db;

import com.podverbnyj.provider.dao.db.entity.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.podverbnyj.provider.dao.db.entity.constant.SQLConstant.ServiceConstants.*;

/**
 * Database table 'service' DBManager
 */
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

    /**
     * Create list of all services from DB
     * @param con  connection received from DAO level
     * @return List of all services from DB
     * @throws SQLException in case of errors in data exchange with the database
     */
    public List<Service> findAll(Connection con) throws SQLException {
        List<Service> services = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Service service;
        try {
            ps = con.prepareStatement(FIND_ALL_SERVICES);
            rs = ps.executeQuery();
            while (rs.next()) {
                service = getService(rs);
                services.add(service);
            }
            return services;
        } finally {
            close(rs);
            close(ps);
        }
    }

    /**
     * Create new service entity in DB
     * @param con connection received from DAO level
     * @param service new database entity data
     * @return 'true' if entity created
     * @throws SQLException in case of errors in data exchange with the database
     */
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

    /**
     * Receive service entity from database by service id
     * @param con connection received from DAO level
     * @param id service id
     * @return returns the service record with the specified id, may be 'null' if no such service
     * @throws SQLException in case of errors in data exchange with the database
     */
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

    /**
     * Update service entity in database, service to edit is selected by the service
     * id from the param
     * @param con connection received from DAO level
     * @param service service to update
     * @return returns 'true' if update was successful
     * @throws SQLException in case of errors in data exchange with the database
     */
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

    /**
     * Delete service entity from database, service to delete is selected by the service
     * id from the param, other service fields are not used
     * @param con connection received from DAO level
     * @param service service to delete
     * @return returns 'true' if delete was successful
     * @throws SQLException in case of errors in data exchange with the database
     */
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

    /**
     * Close resources after using
     * @param resource any autocloseable resource to close
     */
    public void close(AutoCloseable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (Exception ex) {
                log.error("Error closing resource {}", resource, ex);
            }
        }
    }


    /**
     * Set fields of service entity to the prepared statement parameters for SQL request
     * @param service data to set into @param ps here
     * @param ps prepared statement
     * @throws SQLException in case of errors to set parameters
     */
    private void setServiceStatement(Service service, PreparedStatement ps) throws SQLException {
        int index = 1;
        ps.setString(index++, service.getTitleRu());
        ps.setString(index, service.getTitleEn());

    }

    /**
     * Write all data from ResultSet to the service entity
     * @param rs result set
     * @return Service - entity with data from @param
     * @throws SQLException in case of errors to receive data
     */
    private Service getService(ResultSet rs) throws SQLException {
        Service service = new Service();

        service.setId(rs.getInt(1));
        service.setTitleRu(rs.getString("title_ru"));
        service.setTitleEn(rs.getString("title_en"));

        log.trace("Service created ==> {}", service);
        return service;
    }
}
