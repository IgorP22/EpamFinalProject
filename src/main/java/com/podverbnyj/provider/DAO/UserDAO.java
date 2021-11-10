package com.podverbnyj.provider.DAO;

import com.podverbnyj.provider.DAO.db.DBException;
import com.podverbnyj.provider.DAO.db.DBUtils;
import com.podverbnyj.provider.DAO.db.UserDBManager;
import com.podverbnyj.provider.DAO.db.entity.User;
import org.apache.logging.log4j.*;


import java.sql.Connection;


import java.sql.SQLException;
import java.util.ArrayList;





public class UserDAO implements AbstractDAO<User>{

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
    public ArrayList<User> findAll() throws DBException {
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
            log.error("Can't create user ==> " + user, ex);
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
            log.error("Can't get user by ID ==> " + id, ex);
            throw new DBException("Can't create user by ID ==> " + id);
        } finally {
            userDBManager.close(con);
        }
    }



    public User getByName(String name) throws DBException {
        Connection con = dbUtils.getConnection();
        try {
            return userDBManager.getByName(con, name);
        } catch (SQLException ex) {
            log.error("Can't get user by name ==> " + name, ex);
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
        }catch (SQLException ex) {
            log.error("Can't update user ==> " + user.getLogin(), ex);
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
        }catch (SQLException ex) {
            log.error("Can't delete user ==> " + user.getLogin(), ex);
            throw new DBException("Can't delete user ==> " + user.getLogin());
        } finally {
            userDBManager.close(con);
        }
    }
}
