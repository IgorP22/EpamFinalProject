package com.podverbnyj.provider.dao.db;

import com.podverbnyj.provider.dao.db.entity.UserTariff;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.podverbnyj.provider.dao.db.entity.constant.SQLConstant.UserTariffConstants.*;

/**
 * Database table 'user_tariffs' DBManager
 */
public class UserTariffDBManager {

    private static final Logger log = LogManager.getLogger(UserTariffDBManager.class);


    static UserTariffDBManager instance;

    public static synchronized UserTariffDBManager getInstance() {
        if (instance == null) {
            instance = new UserTariffDBManager();
        }
        return instance;
    }

    private UserTariffDBManager() {
        // no op
    }
    /**
     * Create list of all tariff for user with @param userId from DB
     *
     * @param con connection received from DAO level
     * @param userId id of user for 'user_tariffs' table
     * @return List of all tariffs for specified userId from DB
     * @throws SQLException in case of errors in data exchange with the database
     */
    public List<UserTariff> findAllByUserId(Connection con, int userId) throws SQLException {
        List<UserTariff> userTariffs = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        UserTariff userTariff;
        try {
            ps = con.prepareStatement(GET_ALL_TARIFFS_BY_USER_ID);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            while (rs.next()) {
                userTariff = getUserTariffs(rs);
                userTariffs.add(userTariff);
            }
            return userTariffs;
        } finally {
            close(rs);
            close(ps);
        }
    }

    /**
     * Receive total cost of all tariffs connected by user with @param userId from DB
     * @param con connection received from DAO level
     * @return total cost of tariffs chased by user
     * @throws SQLException in case of errors in data exchange with the database
     */
    public double getTotalCost(Connection con, int userId) throws SQLException {
        double totalSum = 0;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(GET_TOTAL_COST_BY_USER_ID);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            if (rs.next()) {
                totalSum = rs.getDouble(1);
            }
            return totalSum;
        } finally {
            close(rs);
            close(ps);
        }
    }


    /**
     * Write all data from ResultSet to the userTariff entity (one record)
     *
     * @param rs result set
     * @return userTariff - entity with data from @param
     * @throws SQLException in case of errors to receive data
     */
    private UserTariff getUserTariffs(ResultSet rs) throws SQLException {
        UserTariff userTariff = new UserTariff();

        userTariff.setUserId(rs.getInt(1));
        userTariff.setTariffId(rs.getInt(2));

        log.trace("User tariffs created ==> {}", userTariff);
        return userTariff;
    }

    /**
     * Update userTariffs entity in database for specified user with user's id from the param
     *
     * Transaction realized here (first all records are deleted and then new ones are inserted)
     *
     * @param con connection received from DAO level
     * @param userTariffs  list of tariffs selected by user
     * @param userId userId to update
     * @return 'true' if update was successful
     * @throws SQLException in case of errors in data exchange with the database
     */
    public boolean update(Connection con, List<UserTariff> userTariffs, int userId) throws SQLException {
        PreparedStatement ps = null;
        con.setAutoCommit(false);
        try {
            ps = con.prepareStatement(DELETE_TARIFFS_BY_USER_ID);
            ps.setInt(1, userId);
            ps.executeUpdate();
        } finally {
            close(ps);
        }

        for (UserTariff userTariff : userTariffs) {
            try {
                ps = con.prepareStatement(CREATE_USER_ID_TARIFF);
                setUserTariff(userTariff, ps);
                ps.executeUpdate();
            } finally {
                close(ps);
            }
        }
        con.commit();
        log.info("Updated {} for {}", userTariffs, userId);
        return true;
    }

    /**
     * Set fields of userTariff entity to the prepared statement parameters for SQL request
     * @param userTariff - data to set into @param ps here
     * @param ps prepared statement
     * @throws SQLException in case of errors to set parameters
     */
    private void setUserTariff(UserTariff userTariff, PreparedStatement ps) throws SQLException {

        ps.setInt(1, userTariff.getUserId());
        ps.setInt(2, userTariff.getTariffId());

    }

    /**
     * Delete all entity with specified userID from @param
     *
     * @param con connection received from DAO level
     * @param userId userId to delete
     * @return 'true' if delete was successful
     * @throws SQLException in case of errors in data exchange with the database
     */
    public boolean delete(Connection con, int userId) throws SQLException {
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(DELETE_TARIFFS_BY_USER_ID);
            ps.setInt(1, userId);
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


}
