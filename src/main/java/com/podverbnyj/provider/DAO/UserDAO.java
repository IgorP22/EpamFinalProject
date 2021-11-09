package com.podverbnyj.provider.DAO;

import com.podverbnyj.provider.DAO.db.DBUtils;
import com.podverbnyj.provider.DAO.db.entity.User;
import com.podverbnyj.provider.DAO.db.entity.constant.Language;
import com.podverbnyj.provider.DAO.db.entity.constant.Role;
import com.podverbnyj.provider.DAO.db.entity.constant.Status;
import org.apache.logging.log4j.*;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static com.podverbnyj.provider.DAO.db.SQLConstant.UserConstants.FIND_ALL_USERS;
import static com.podverbnyj.provider.DAO.db.SQLConstant.UserConstants.GET_USER_BY_LOGIN;


public class UserDAO implements AbstractDAO<User>{

    private static final Logger log = LogManager.getLogger(UserDAO.class);


    @Override
    public ArrayList<User> findAll() throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        Connection con = null;
        ResultSet rs = null;
        try {
            con = DBUtils.getInstance().getConnection();
            rs = con.createStatement().executeQuery(FIND_ALL_USERS);
            while (rs.next()) {
                User user = new User.UserBuilder(rs.getString(2),rs.getString(3))
                        .setId(rs.getInt(1))
                        .setEmail(rs.getString(4))
                        .setName(rs.getString(5))
                        .setSurname(rs.getString(6))
                        .setPhone(rs.getString(7))
                        .setBalance(rs.getDouble(8))
                        .setLanguage(Language.valueOf(rs.getString(9)))
                        .build();


                users.add(user);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        if (rs != null) {
            rs.close();
        }
        con.close();
        return users;
    }

    @Override
    public boolean create(User entity) {
        return false;
    }

    @Override
    public User getByName(String name) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = null;
        con = DBUtils.getInstance().getConnection();
        try {
            ps = con.prepareStatement(GET_USER_BY_LOGIN);
            ps.setString(1,name);
            rs = ps.executeQuery();
            if (rs.next()) {
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
                log.trace("User created ==> "+user);
            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public boolean update(User entity) {
        return false;
    }

    @Override
    public boolean delete(User entity) {
        return false;
    }
}
