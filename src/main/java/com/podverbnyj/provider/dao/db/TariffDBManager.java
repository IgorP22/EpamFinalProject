package com.podverbnyj.provider.dao.db;

import com.podverbnyj.provider.dao.db.entity.Tariff;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.podverbnyj.provider.dao.db.entity.constant.SQLConstant.TariffConstants.*;

/**
 * Database table 'tariff' DBManager
 */
public class TariffDBManager {

    private static final Logger log = LogManager.getLogger(TariffDBManager.class);


    static TariffDBManager instance;

    public static synchronized TariffDBManager getInstance() {
        if (instance == null) {
            instance = new TariffDBManager();
        }
        return instance;
    }

    private TariffDBManager() {
        // no op
    }

    /**
     * Create list of all tariff from DB
     * @param con connection received from DAO level
     * @return List of all tariffs from DB
     * @throws SQLException in case of errors in data exchange with the database
     */
    public List<Tariff> findAll(Connection con) throws SQLException {
        List<Tariff> tariffs = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Tariff tariff;
        try {
            ps = con.prepareStatement(FIND_ALL_TARIFFS);
            rs = ps.executeQuery();
            while (rs.next()) {
                tariff = getTariff(rs);
                tariffs.add(tariff);
            }
            return tariffs;
        } finally {
            close(rs);
            close(ps);
        }
    }

    /**
     * Create new tariff entity in DB
     * @param con connection received from DAO level
     * @param tariff new database entity data
     * @return 'true' if entity created
     * @throws SQLException in case of errors in data exchange with the database
     */
    public boolean create(Connection con, Tariff tariff) throws SQLException {
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(CREATE_TARIFF);
            setTariffStatement(tariff, ps);
            ps.executeUpdate();
            return true;
        } finally {
            close(ps);
        }
    }

    /**
     * Receive tariff entity from database by tariff id
     * @param con connection received from DAO level
     * @param id tariff id
     * @return returns the tariff record with the specified id, may be 'null' if no such tariff
     * @throws SQLException in case of errors in data exchange with the database
     */
    public Tariff getById(Connection con, int id) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Tariff tariff = null;
        try {
            ps = con.prepareStatement(GET_TARIFF_BY_ID);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                tariff = getTariff(rs);
            }
            return tariff;
        } finally {
            close(rs);
            close(ps);
        }
    }

    /**
     * Update tariff entity in database, tariff to edit is selected by the tariff
     * id from the param
     * @param con connection received from DAO level
     * @param tariff tariff to update
     * @return returns 'true' if update was successful
     * @throws SQLException in case of errors in data exchange with the database
     */
    public boolean update(Connection con, Tariff tariff) throws SQLException {
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(UPDATE_TARIFF);
            setTariffStatement(tariff, ps);
            ps.setInt(7, tariff.getId());
            ps.executeUpdate();
            return true;
        } finally {
            close(ps);
        }
    }

    /**
     * Delete tariff entity from database, tariff to delete is selected by the tariff
     * id from the param, other tariff fields are not used
     * @param con connection received from DAO level
     * @param tariff tariff to delete
     * @return returns 'true' if delete was successful
     * @throws SQLException in case of errors in data exchange with the database
     */
    public boolean delete(Connection con, Tariff tariff) throws SQLException {
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(DELETE_TARIFF_BY_ID);
            ps.setInt(1, tariff.getId());
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
     * Set fields of tariff entity to the prepared statement parameters for SQL request
     * @param tariff data to set into @param ps here
     * @param ps prepared statement
     * @throws SQLException in case of errors to set parameters
     */
    private void setTariffStatement(Tariff tariff, PreparedStatement ps) throws SQLException {
        int index = 1;
        ps.setString(index++, tariff.getNameRu());
        ps.setString(index++, tariff.getNameEn());
        ps.setDouble(index++, tariff.getPrice());
        ps.setInt(index++, tariff.getServiceId());
        ps.setString(index++, tariff.getDescriptionRu());
        ps.setString(index, tariff.getDescriptionEn());
    }

    /**
     * Write all data from ResultSet to the tariff entity
     * @param rs result set
     * @return tariff entity with data from @param
     * @throws SQLException in case of errors to receive data
     */
    private Tariff getTariff(ResultSet rs) throws SQLException {
        Tariff tariff = new Tariff();

        tariff.setId(rs.getInt("tariff_id"));
        tariff.setNameRu(rs.getString("name_ru"));
        tariff.setNameEn(rs.getString("name_en"));
        tariff.setPrice(rs.getDouble("price"));
        tariff.setServiceId(rs.getInt("service_id"));
        tariff.setDescriptionRu(rs.getString("description_ru"));
        tariff.setDescriptionEn(rs.getString("description_en"));

        log.trace("Tariff created ==> {}", tariff);
        return tariff;
    }
}
