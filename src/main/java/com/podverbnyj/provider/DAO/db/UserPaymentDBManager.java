package com.podverbnyj.provider.DAO.db;

import com.podverbnyj.provider.DAO.db.entity.UserPayment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.podverbnyj.provider.DAO.db.entity.constant.SQLConstant.UserPaymentsConstants.*;



public class UserPaymentDBManager {

    private static final Logger log = LogManager.getLogger(UserPaymentDBManager.class);


    static UserPaymentDBManager instance;

    public static synchronized UserPaymentDBManager getInstance() {
        if (instance == null) {
            instance = new UserPaymentDBManager();
        }
        return instance;
    }

    private UserPaymentDBManager() {
        // no op
    }


    public List<UserPayment> findAllByUserId(Connection con, int userId) throws SQLException {
        List<UserPayment> userPayments = new ArrayList<>();
        ResultSet rs = null;
        UserPayment userPayment;
        try {
            rs = con.createStatement().executeQuery(FIND_ALL_PAYMENTS_BY_USER_ID);
            while (rs.next()) {
                userPayment = getUserPayments(rs);
                userPayments.add(userPayment);
            }
            return userPayments;
        } finally {
            close(rs);
        }
    }

    public boolean create(Connection con, UserPayment userPayment) throws SQLException {
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(CREATE_PAYMENT);
            setUserPaymentsStatement(userPayment, ps);
            ps.executeUpdate();
            return true;
        } finally {
            close(ps);
        }
    }

    private UserPayment getUserPayments(ResultSet rs) throws SQLException {
        UserPayment userPayment = new UserPayment();

        userPayment.setUserId(rs.getInt(1));
        userPayment.setDate(rs.getDate(2));
        userPayment.setSum(rs.getDouble(3));

        log.trace("User payment created ==> " + userPayment);
        return userPayment;
    }

    private void setUserPaymentsStatement(UserPayment userPayment, PreparedStatement ps) throws SQLException {
        java.util.Date dt = userPayment.getDate();
        java.text.SimpleDateFormat sdf =
                new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(dt);

        int index = 1;
        ps.setInt(index++, userPayment.getUserId());
        ps.setString(index++, currentTime);
        ps.setDouble(index++, userPayment.getSum());
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


}
