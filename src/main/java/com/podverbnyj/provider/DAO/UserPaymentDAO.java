package com.podverbnyj.provider.DAO;

import com.podverbnyj.provider.DAO.db.DBException;
import com.podverbnyj.provider.DAO.db.DBUtils;
import com.podverbnyj.provider.DAO.db.UserPaymentDBManager;
import com.podverbnyj.provider.DAO.db.entity.UserPayment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


public class UserPaymentDAO {

    private static final Logger log = LogManager.getLogger(UserPaymentDAO.class);
    private static final DBUtils dbUtils = DBUtils.getInstance();
    private static final UserPaymentDBManager USER_PAYMENT_DB_MANAGER = UserPaymentDBManager.getInstance();

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
            return USER_PAYMENT_DB_MANAGER.findAllByUserId(con, userId);
        } catch (SQLException ex) {
            log.error("Can't receive payment history from DB", ex);
            throw new DBException("Can't receive payment history from DB");
        } finally {
            USER_PAYMENT_DB_MANAGER.close(con);
        }
    }

    public boolean create(UserPayment userPayment) throws DBException {
        Connection con = dbUtils.getConnection();
        try {
            return USER_PAYMENT_DB_MANAGER.create(con, userPayment);
        } catch (SQLException ex) {
            log.error("Can't add payment ==> " + userPayment, ex);
            throw new DBException("Can't add payment ==> " + userPayment);
        } finally {
            USER_PAYMENT_DB_MANAGER.close(con);
        }
    }

}
