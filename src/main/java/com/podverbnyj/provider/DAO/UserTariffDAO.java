package com.podverbnyj.provider.DAO;

import com.podverbnyj.provider.DAO.db.DBException;
import com.podverbnyj.provider.DAO.db.DBUtils;
import com.podverbnyj.provider.DAO.db.UserTariffDBManager;
import com.podverbnyj.provider.DAO.db.entity.UserTariff;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


public class UserTariffDAO {

    private static final Logger log = LogManager.getLogger(UserTariffDAO.class);
    private static final DBUtils dbUtils = DBUtils.getInstance();
    private static final UserTariffDBManager USER_TARIFF_DB_MANAGER = UserTariffDBManager.getInstance();

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
            return USER_TARIFF_DB_MANAGER.findAllByUserId(con, userId);
        } catch (SQLException ex) {
            log.error("Can't receive list of user's tariffs from DB", ex);
            throw new DBException("Can't receive list of user's tariffs from DB");
        } finally {
            USER_TARIFF_DB_MANAGER.close(con);
        }
    }


    public boolean update(List<UserTariff> userTariffs, int userId) throws DBException {
        Connection con = dbUtils.getConnection();
        try {
            return USER_TARIFF_DB_MANAGER.update(con, userTariffs, userId);
        } catch (SQLException ex) {
            log.error("Can't create list of tariffs ==> " + userTariffs, ex);
            throw new DBException("Can't create list of tariffs");
        } finally {
            USER_TARIFF_DB_MANAGER.close(con);
        }
    }



}
