package com.podverbnyj.provider.DAO;

import com.podverbnyj.provider.DAO.db.DBException;
import com.podverbnyj.provider.DAO.db.DBUtils;
import com.podverbnyj.provider.DAO.db.UserDBManager;
import com.podverbnyj.provider.DAO.db.entity.User;
import com.podverbnyj.provider.DAO.db.entity.UserPayment;
import org.apache.logging.log4j.*;


import java.sql.Connection;


import java.sql.SQLException;
import java.util.List;


public class UserDAO implements AbstractDAO<User> {

    private static final Logger log = LogManager.getLogger(UserDAO.class);
    private static final DBUtils dbUtils = DBUtils.getInstance();
    private static final UserDBManager userDBManager = UserDBManager.getInstance();

    static UserDAO instance;

    public static synchronized UserDAO getInstance() {
        if (instance == null) {
            instance = new UserDAO();
        }
        return instance;
    }

    private UserDAO() {
        // no op
    }


    @Override
    public List<User> findAll() throws DBException {
        Connection con = dbUtils.getConnection();
        try {
            return userDBManager.findAll(con);
        } catch (SQLException ex) {
            log.error("Can't receive list of users from DB", ex);
            throw new DBException("Can't receive list of users from DB");
        } finally {
            userDBManager.close(con);
        }
    }

    @Override
    public boolean create(User user) throws DBException {
        Connection con = dbUtils.getConnection();
        try {
            return userDBManager.create(con, user);
        } catch (SQLException ex) {
            log.error("Can't create user ==> {}", user, ex);
            throw new DBException("Can't create user ==> " + user);
        } finally {
            userDBManager.close(con);
        }
    }

    @Override
    public User getById(int id) throws DBException {
        Connection con = dbUtils.getConnection();
        try {
            return userDBManager.getById(con, id);
        } catch (SQLException ex) {
            log.error("Can't get user by ID ==> {}", id, ex);
            throw new DBException("Can't create user by ID ==> " + id);
        } finally {
            userDBManager.close(con);
        }
    }


    public User getByLogin(String name) throws DBException {
        Connection con = dbUtils.getConnection();
        try {
            return userDBManager.getByLogin(con, name);
        } catch (SQLException ex) {
            log.error("Can't get user by name ==> {}", name, ex);
            throw new DBException("Can't create user by name ==> " + name);
        } finally {
            userDBManager.close(con);
        }
    }


    @Override
    public boolean update(User user) throws DBException {
        Connection con = dbUtils.getConnection();
        try {
            return userDBManager.update(con, user);
        } catch (SQLException ex) {
            log.error("Can't update user ==> {}", user.getLogin(), ex);
            throw new DBException("Can't update user ==> " + user.getLogin());
        } finally {
            userDBManager.close(con);
        }
    }

    @Override
    public boolean delete(User user) throws DBException {
        Connection con = dbUtils.getConnection();
        try {
            return userDBManager.delete(con, user);
        } catch (SQLException ex) {
            log.error("Can't delete user ==> {}", user.getLogin(), ex);
            throw new DBException("Can't delete user ==> " + user.getLogin());
        } finally {
            userDBManager.close(con);
        }
    }

    public int countAdmins() throws DBException {
        Connection con = dbUtils.getConnection();
        try {
            return userDBManager.countAdmins(con);
        } catch (SQLException ex) {
            log.error("Can't count admins amount ==> ", ex);
            throw new DBException("Can't count admins amount");
        } finally {
            userDBManager.close(con);
        }
    }

    public void debitAllUsers(List<User> listOfUsers, List<UserPayment> userPaymentList) throws DBException {
        Connection con = dbUtils.getConnection();
        try {
            userDBManager.debitAllUsers(con, listOfUsers, userPaymentList);
            con.commit();
            log.info("Users funds debited successfully \n{}\n{}", listOfUsers, userPaymentList);

        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException e) {
                log.error("Rollback error, funds are not debited \n{}\n{}", listOfUsers, userPaymentList, ex);
                throw new DBException("Rollback error, funds are not debited");
            }
            log.error("Funds are not debited \n{}\n{}", listOfUsers, userPaymentList, ex);
            throw new DBException("Funds are not debited");
        } finally {
            userDBManager.close(con);
        }
    }
}
