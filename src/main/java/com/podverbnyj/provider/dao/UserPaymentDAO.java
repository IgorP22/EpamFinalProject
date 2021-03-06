package com.podverbnyj.provider.dao;

import com.podverbnyj.provider.dao.db.DBException;
import com.podverbnyj.provider.dao.db.DBUtils;
import com.podverbnyj.provider.dao.db.UserPaymentDBManager;
import com.podverbnyj.provider.dao.db.entity.UserPayment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * DAO level for database table 'user_payments'.
 *
 * All method get connection from datasource, and call the same named method
 * from UserPaymentDBManager on it, send or receive data (more comments
 * in UserPaymentDBManager), and close connection at the end.
 *
 * If UserPaymentDBManager throw exception, it's caught here and replaced by
 * our own DBException with high level message for error page.
 */
public class UserPaymentDAO {

    private static final Logger log = LogManager.getLogger(UserPaymentDAO.class);
    private static final DBUtils dbUtils = DBUtils.getInstance();
    private static final UserPaymentDBManager userPaymentDBManager = UserPaymentDBManager.getInstance();

    static UserPaymentDAO instance;

    public static synchronized UserPaymentDAO getInstance() {
        if (instance == null) {
            instance = new UserPaymentDAO();
        }
        return instance;
    }

    private UserPaymentDAO() {
        // no op
    }

    public List<UserPayment> findAll(int userId) throws DBException {
        Connection con = dbUtils.getConnection();
        try {
            return userPaymentDBManager.findAllByUserId(con, userId);
        } catch (SQLException ex) {
            log.error("Can't receive payment history from DB for user {}", userId, ex);
            throw new DBException("Can't receive payment history from DB for user " + userId);
        } finally {
            userPaymentDBManager.close(con);
        }
    }

    public List<UserPayment> findGroup(int userId, int limit, int offset) throws DBException {
        Connection con = dbUtils.getConnection();
        try {
            return userPaymentDBManager.findGroupByUserId(con, userId, limit, offset);
        } catch (SQLException ex) {
            log.error("Can't receive payment history group from DB for user {}", userId, ex);
            throw new DBException("Can't receive payment history group from DB for user " + userId);
        } finally {
            userPaymentDBManager.close(con);
        }
    }

    public List<UserPayment> findTopUpGroup(int userId, int limit, int offset) throws DBException {
        Connection con = dbUtils.getConnection();
        try {
            return userPaymentDBManager.findTopUpGroupByUserId(con, userId, limit, offset);
        } catch (SQLException ex) {
            log.error("Can't receive payment history group (positive only) from DB for user {}", userId, ex);
            throw new DBException("Can't receive payment history group (positive only) from DB for user " + userId);
        } finally {
            userPaymentDBManager.close(con);
        }
    }

    public boolean create(UserPayment userPayment) throws DBException {
        Connection con = dbUtils.getConnection();
        try {
            return userPaymentDBManager.create(con, userPayment);
        } catch (SQLException ex) {
            log.error("Can't add payment ==> {}", userPayment, ex);
            throw new DBException("Can't add payment ==> " + userPayment);
        } finally {
            userPaymentDBManager.close(con);
        }
    }

    public int getUsersPaymentsSize(int userId) throws DBException {
        Connection con = dbUtils.getConnection();
        try {
            return userPaymentDBManager.getUsersPaymentsSize(con, userId);
        } catch (SQLException ex) {
            log.error("Can't receive payment size from DB for user {}", userId, ex);
            throw new DBException("Can't receive payment size from DB for user " + userId);
        } finally {
            userPaymentDBManager.close(con);
        }
    }

    public int getUsersPaymentsTopUpSize(int userId) throws DBException {
        Connection con = dbUtils.getConnection();
        try {
            return userPaymentDBManager.getUsersPaymentsTopUpSize(con, userId);
        } catch (SQLException ex) {
            log.error("Can't receive payment size from DB for user {}", userId, ex);
            throw new DBException("Can't receive payment size from DB for user " + userId);
        } finally {
            userPaymentDBManager.close(con);
        }
    }


}
