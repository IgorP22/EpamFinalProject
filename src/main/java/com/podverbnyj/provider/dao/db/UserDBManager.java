package com.podverbnyj.provider.dao.db;

import com.podverbnyj.provider.dao.db.entity.User;
import com.podverbnyj.provider.dao.db.entity.UserPayment;
import com.podverbnyj.provider.dao.db.entity.constant.Language;
import com.podverbnyj.provider.dao.db.entity.constant.Role;
import com.podverbnyj.provider.dao.db.entity.constant.Status;
import org.apache.logging.log4j.*;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.podverbnyj.provider.dao.db.entity.constant.SQLConstant.UserConstants.*;

/**
 * Database table 'user' DBManager
 */
public class UserDBManager {


    private static final Logger log = LogManager.getLogger(UserDBManager.class);


    static UserDBManager instance;

    public static synchronized UserDBManager getInstance() {
        if (instance == null) {
            instance = new UserDBManager();
        }
        return instance;
    }

    private UserDBManager() {
        // no op
    }

    /**
     * Create list of all users from DB
     * @param con connection received from DAO level
     * @return List of all users from DB
     * @throws SQLException in case of errors in data exchange with the database
     */
    public List<User> findAll(Connection con) throws SQLException {
        List<User> users = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user;
        try {
            ps = con.prepareStatement(FIND_ALL_USERS);
            rs = ps.executeQuery();
            while (rs.next()) {
                user = getUser(rs);
                users.add(user);
            }
            return users;
        } finally {
            close(rs);
            close(ps);
        }
    }

    /**
     * Create list of all users from DB
     * @param con connection received from DAO level
     * @return List of all users from DB
     * @throws SQLException in case of errors in data exchange with the database
     */
    public List<User> findAllNotificatedUsers(Connection con) throws SQLException {
        List<User> users = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user;
        try {
            ps = con.prepareStatement(FIND_ALL_NOTIFICATED_USERS);
            rs = ps.executeQuery();
            while (rs.next()) {
                user = getUser(rs);
                users.add(user);
            }
            return users;
        } finally {
            close(rs);
            close(ps);
        }
    }

    /**
     * Create new user entity in DB
     * @param con connection received from DAO level
     * @param user ew database entity data
     * @return 'true' if entity created
     * @throws SQLException in case of errors in data exchange with the database
     */
    public boolean create(Connection con, User user) throws SQLException {
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(CREATE_USER);
            setUserStatement(user, ps);
            ps.executeUpdate();
            return true;
        } finally {
            close(ps);
        }
    }

    /**
     * Receive user's entity from database by user's login
     * @param con connection received from DAO level
     * @param login users login
     * @return returns the user record with the specified login, may be 'null' if no such user
     * @throws SQLException in case of errors in data exchange with the database
     */
    public User getByLogin(Connection con, String login) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = null;
        try {
            ps = con.prepareStatement(GET_USER_BY_LOGIN);
            ps.setString(1, login);
            rs = ps.executeQuery();
            if (rs.next()) {
                user = getUser(rs);
            }
            return user;
        } finally {
            close(rs);
            close(ps);
        }
    }

    /**
     * Receive user's entity from database by user's id
     * @param con connection received from DAO level
     * @param id users id
     * @return returns the user record with the specified id, may be 'null' if no such user
     * @throws SQLException in case of errors in data exchange with the database
     */
    public User getById(Connection con, int id) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = null;
        try {
            ps = con.prepareStatement(GET_USER_BY_ID);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                user = getUser(rs);
            }
            return user;
        } finally {
            close(rs);
            close(ps);
        }
    }

    /**
     * Update user entity in database, user to edit is selected by the user's id from the param
     * @param con connection received from DAO level
     * @param user user to update
     * @return returns 'true' if update was successful
     * @throws SQLException in case of errors in data exchange with the database
     */
    public boolean update(Connection con, User user) throws SQLException {
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(UPDATE_USER);
            setUserStatement(user, ps);
            ps.setInt(12, user.getId());
            ps.executeUpdate();
            return true;
        } finally {
            close(ps);
        }
    }

    /**
     * Delete user entity from database, user to delete is selected by the user
     * id ,taken from the param, other user fields are not used
     * @param con connection received from DAO level
     * @param user user to delete
     * @return returns 'true' if delete was successful
     * @throws SQLException in case of errors in data exchange with the database
     */
    public boolean delete(Connection con, User user) throws SQLException {
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(DELETE_USER_BY_LOGIN);
            ps.setString(1, user.getLogin());
            ps.executeUpdate();
            return true;
        } finally {
            close(ps);
        }
    }

    /**
     * Count and return number of users with role 'ADMIN'
     * @param con connection received from DAO level
     * @return number of users with role 'ADMIN'
     * @throws SQLException in case of errors in data exchange with the database
     */
    public int countAdmins(Connection con) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        int counter = 1;
        try {
            ps = con.prepareStatement(COUNT_ADMINS);
            rs = ps.executeQuery();
            if (rs.next()) {
                counter = rs.getInt(1);
            }
            return counter;
        } finally {
            close(rs);
            close(ps);
        }
    }

    /**
     *  Record to DB new user's balance after automatic debiting funds,
     *  also record data to history of payments table.
     *  Transaction realised here.
     *
     *  It's not good idea to call other DBManager here, but that's done to realizing
     *  transaction on more simple way
     *
     * @param con connection received from DAO level
     * @param listOfUsers list of users who have been debited
     * @param userPaymentList list of payments (for updating 'user_payment' table)
     * @throws SQLException in case of errors in data exchange with the database
     */
    public void debitAllUsers(Connection con, List<User> listOfUsers, List<UserPayment> userPaymentList) throws SQLException {
        con.setAutoCommit(false);
        for (User user : listOfUsers) {
            update(con, user);
        }
        for (UserPayment userPayment : userPaymentList) {
            UserPaymentDBManager.getInstance().create(con, userPayment);
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

    /**
     * Set fields of tariff entity to the prepared statement parameters for SQL request
     * @param user data to set into @param ps here
     * @param ps prepared statement
     * @throws SQLException in case of errors to set parameters
     */
    private void setUserStatement(User user, PreparedStatement ps) throws SQLException {
        int index = 1;
        ps.setString(index++, user.getLogin());
        ps.setString(index++, user.getPassword());
        ps.setString(index++, user.getEmail());
        ps.setString(index++, user.getName());
        ps.setString(index++, user.getSurname());
        ps.setString(index++, user.getPhone());
        ps.setDouble(index++, user.getBalance());
        ps.setString(index++, user.getLanguage().value());
        ps.setString(index++, user.getRole().value());
        ps.setBoolean(index++, user.isNotification());
        ps.setString(index, user.getStatus().value());
    }

    /**
     * Write all data from ResultSet to the user entity
     * @param rs result set
     * @return user - entity with data from @param
     * @throws SQLException in case of errors to receive data
     */
    private User getUser(ResultSet rs) throws SQLException {
        User user;
        user = new User.UserBuilder(
                rs.getString("login"),
                rs.getString("password"))
                .setId(rs.getInt("user_id"))
                .setEmail(rs.getString("email"))
                .setName(rs.getString("name"))
                .setSurname(rs.getString("surname"))
                .setPhone(rs.getString("phone"))
                .setBalance(rs.getDouble("balance"))
                .setLanguage(Language.valueOf(rs.getString("language")))
                .setRole(Role.valueOf(rs.getString("role")))
                .setNotification(rs.getInt("notification"))
                .setStatus(Status.valueOf(rs.getString("status")))
                .build();
        log.trace("User created ==> {}", user);
        return user;
    }
}
