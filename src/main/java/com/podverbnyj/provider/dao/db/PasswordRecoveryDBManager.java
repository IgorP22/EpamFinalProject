package com.podverbnyj.provider.dao.db;

import com.podverbnyj.provider.dao.db.entity.PasswordRecovery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.podverbnyj.provider.dao.db.entity.constant.SQLConstant.PasswordRecoveryConstants.*;

/**
 * Database table 'password_recovery' DBManager
 */
public class PasswordRecoveryDBManager {

    private static final Logger log = LogManager.getLogger(PasswordRecoveryDBManager.class);


    static PasswordRecoveryDBManager instance;

    public static synchronized PasswordRecoveryDBManager getInstance() {
        if (instance == null) {
            instance = new PasswordRecoveryDBManager();
        }
        return instance;
    }

    private PasswordRecoveryDBManager() {
        // no op
    }

    /**
     * Create new entity for password recovery
     * @param con - received from DAO level
     * @param passwordRecovery - database entity
     * @return 'true' if entity created
     * @throws SQLException in case of errors in data exchange with the database
     */
    public boolean create(Connection con, PasswordRecovery passwordRecovery) throws SQLException {
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(CREATE_RECOVERY_CODE);
            ps.setInt(1, passwordRecovery.getUserId());
            ps.setString(2, passwordRecovery.getCode());
            ps.executeUpdate();
            return true;
        } finally {
            close(ps);
        }
    }

    /**
     * Receive entity from database by UserId
     * @param con - received from DAO level
     * @param userId - user Id
     * @return PasswordRecovery database entity
     * @throws SQLException in case of errors in data exchange with the database
     */
    public PasswordRecovery getPasswordRecovery(Connection con, int userId) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        PasswordRecovery passwordRecovery = new PasswordRecovery();
        try {
            ps = con.prepareStatement(GET_ENTITY_BY_USER_ID);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            if (rs.next()) {
                passwordRecovery.setUserId(rs.getInt("user_id"));
                passwordRecovery.setCode(rs.getString("code"));
            }
            return passwordRecovery;
        } finally {
            close(rs);
            close(ps);
        }
    }

    /**
     * Receive entity from database restore password code
     * @param con - received from DAO level
     * @param code - restore password code
     * @return PasswordRecovery database entity
     * @throws SQLException in case of errors in data exchange with the database
     */
    public PasswordRecovery getPasswordRecovery(Connection con, String code) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        PasswordRecovery passwordRecovery = new PasswordRecovery();
        try {
            ps = con.prepareStatement(GET_ENTITY_BY_CODE);
            ps.setString(1, code);
            rs = ps.executeQuery();
            if (rs.next()) {
                passwordRecovery.setUserId(rs.getInt("user_id"));
                passwordRecovery.setCode(rs.getString("code"));
            }
            return passwordRecovery;
        } finally {
            close(rs);
            close(ps);
        }
    }


    /**
     * Delete entity from database by restore password code
     * @param con - received from DAO level
     * @param code - restore password code
     * @return PasswordRecovery database entity
     * @throws SQLException in case of errors in data exchange with the database
     */
    public boolean deleteByCode(Connection con, String code) throws SQLException {
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(DELETE_ENTRY_BY_CODE);
            ps.setString(1, code);
            ps.executeUpdate();
            return true;
        } finally {
            close(ps);
        }
    }

    /**
     * Close resources after using
     * @param resource - type of resource to close
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
