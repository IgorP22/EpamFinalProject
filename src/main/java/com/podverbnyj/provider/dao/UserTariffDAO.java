package com.podverbnyj.provider.dao;

import com.podverbnyj.provider.dao.db.DBException;
import com.podverbnyj.provider.dao.db.DBUtils;
import com.podverbnyj.provider.dao.db.UserTariffDBManager;
import com.podverbnyj.provider.dao.db.entity.UserTariff;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * DAO level for database table 'user_tariffs'.
 *
 * All method get connection from datasource, and call the same named method
 * from UserTariffDBManager on it, send or receive data (more comments in UserTariffDBManager),
 * and close connection at the end.
 *
 * If UserTariffDBManager throw exception, it's caught here and replaced by our own DBException
 * with high level message for error page.
 */
public class UserTariffDAO {

    private static final Logger log = LogManager.getLogger(UserTariffDAO.class);
    private static final DBUtils dbUtils = DBUtils.getInstance();
    private static final UserTariffDBManager userTariffDBManager = UserTariffDBManager.getInstance();

    static UserTariffDAO instance;

    public static synchronized UserTariffDAO getInstance() {
        if (instance == null) {
            instance = new UserTariffDAO();
        }
        return instance;
    }

    private UserTariffDAO() {
        // no op
    }

    public List<UserTariff> findAll(int userId) throws DBException {
        Connection con = dbUtils.getConnection();
        try {
            return userTariffDBManager.findAllByUserId(con, userId);
        } catch (SQLException ex) {
            log.error("Can't receive list of user's tariffs from DB", ex);
            throw new DBException("Can't receive list of user's tariffs from DB");
        } finally {
            userTariffDBManager.close(con);
        }
    }

    public double getTotalCost(int userId) throws DBException {
        Connection con = dbUtils.getConnection();
        try {
            return userTariffDBManager.getTotalCost(con, userId);
        } catch (SQLException ex) {
            log.error("Can't receive total cost of all tariffs", ex);
            throw new DBException("Can't receive total cost of all tariffs from DB");
        } finally {
            userTariffDBManager.close(con);
        }
    }


    public boolean update(List<UserTariff> userTariffs, int userId) throws DBException {
        Connection con = dbUtils.getConnection();
        try {
            return userTariffDBManager.update(con, userTariffs, userId);
        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException e) {
                log.error("Rollback error {}", userTariffs, ex);
                throw new DBException("Transaction failed & rollback failed");
            }
            log.error("Can't create list of tariffs ==> {}", userTariffs, ex);
            throw new DBException("Can't create list of tariffs");
        } finally {
            userTariffDBManager.close(con);
        }
    }



}
