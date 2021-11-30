package com.podverbnyj.provider.DAO.db;

import com.podverbnyj.provider.DAO.db.entity.PasswordRecovery;
import com.podverbnyj.provider.DAO.db.entity.Tariff;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.podverbnyj.provider.DAO.db.entity.constant.SQLConstant.PasswordRecoveryConstants.*;


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

    public void close(AutoCloseable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (Exception ex) {
                log.error("Error closing resource " + resource, ex);
            }
        }
    }
}
