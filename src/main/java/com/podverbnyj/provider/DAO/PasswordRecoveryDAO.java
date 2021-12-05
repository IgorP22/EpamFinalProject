package com.podverbnyj.provider.DAO;

import com.podverbnyj.provider.DAO.db.DBException;
import com.podverbnyj.provider.DAO.db.DBUtils;
import com.podverbnyj.provider.DAO.db.PasswordRecoveryDBManager;
import com.podverbnyj.provider.DAO.db.entity.PasswordRecovery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;


public class PasswordRecoveryDAO {

    private static final Logger log = LogManager.getLogger(PasswordRecoveryDAO.class);
    private static final DBUtils dbUtils = DBUtils.getInstance();
    private static final PasswordRecoveryDBManager passwordRecoveryDBManager = PasswordRecoveryDBManager.getInstance();

    static PasswordRecoveryDAO instance;

    public static synchronized PasswordRecoveryDAO getInstance() {
        if (instance == null) {
            instance = new PasswordRecoveryDAO();
        }
        return instance;
    }

    private PasswordRecoveryDAO() {
        // no op
    }

    public boolean create(PasswordRecovery passwordRecovery) throws DBException {
        Connection con = dbUtils.getConnection();
        try {
            return passwordRecoveryDBManager.create(con, passwordRecovery);
        } catch (SQLException ex) {
            log.error("Can't create password recovery code ==> {}", passwordRecovery, ex);
            throw new DBException("Can't create password recovery code ==> " + passwordRecovery);
        } finally {
            passwordRecoveryDBManager.close(con);
        }
    }


    public PasswordRecovery getPasswordRecovery(int userId) throws DBException {
        Connection con = dbUtils.getConnection();
        try {
            return passwordRecoveryDBManager.getPasswordRecovery(con, userId);
        } catch (SQLException ex) {
            log.error("Can't get code by id ==> {}", userId, ex);
            throw new DBException("Can't get code by id ==> " + userId);
        } finally {
            passwordRecoveryDBManager.close(con);
        }
    }

    public PasswordRecovery getPasswordRecovery(String code) throws DBException {
        Connection con = dbUtils.getConnection();
        try {
            return passwordRecoveryDBManager.getPasswordRecovery(con, code);
        } catch (SQLException ex) {
            log.error("Can't get id by code ==> {}", code, ex);
            throw new DBException("Can't get id by code ==> " + code);
        } finally {
            passwordRecoveryDBManager.close(con);
        }
    }


    public boolean deleteByCode(String code) throws DBException {
        Connection con = dbUtils.getConnection();
        try {
            return passwordRecoveryDBManager.deleteByCode(con, code);
        } catch (SQLException ex) {
            log.error("Can't delete by code ==> {}", code, ex);
            throw new DBException("Can't delete by code ==> " + code);
        } finally {
            passwordRecoveryDBManager.close(con);
        }
    }
}
