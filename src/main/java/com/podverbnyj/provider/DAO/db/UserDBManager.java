package com.podverbnyj.provider.DAO.db;

import com.podverbnyj.provider.DAO.db.entity.User;
import com.podverbnyj.provider.DAO.db.entity.UserPayment;
import com.podverbnyj.provider.DAO.db.entity.constant.Language;
import com.podverbnyj.provider.DAO.db.entity.constant.Role;
import com.podverbnyj.provider.DAO.db.entity.constant.Status;
import org.apache.logging.log4j.*;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.podverbnyj.provider.DAO.db.entity.constant.SQLConstant.UserConstants.*;

@SuppressWarnings("UnusedAssignment")
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

    public List<User> findAll(Connection con) throws SQLException {
        List<User> users = new ArrayList<>();
        ResultSet rs = null;
        User user;
        try {
            rs = con.createStatement().executeQuery(FIND_ALL_USERS);
            while (rs.next()) {
                user = getUser(rs);
                users.add(user);
            }
            return users;
        } finally {
            close(rs);
        }
    }

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

    public User getByName(Connection con, String name) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = null;
        try {
            ps = con.prepareStatement(GET_USER_BY_LOGIN);
            ps.setString(1, name);
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

    public int countAdmins(Connection con) throws SQLException {
        ResultSet rs = null;
        int counter = 1;
        try {
            rs = con.createStatement().executeQuery(COUNT_ADMINS);
            if (rs.next()) {
                counter = rs.getInt(1);
            }
            return counter;
        } finally {
            close(rs);
        }
    }

    public void debitAllUsers(Connection con, List<User> listOfUsers, List<UserPayment> userPaymentList) throws SQLException {
        con.setAutoCommit(false);
        for (User user : listOfUsers) {
            update(con,user);
        }
        for (UserPayment userPayment: userPaymentList){
            UserPaymentDBManager.getInstance().create(con,userPayment);
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
        log.trace("User created ==> " + user);
        return user;
    }
}
