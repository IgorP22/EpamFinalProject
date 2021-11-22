package com.podverbnyj.provider.DAO.db;

import com.podverbnyj.provider.DAO.db.entity.UserPayment;
import org.apache.logging.log4j.*;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
        PreparedStatement ps = null;
        ResultSet rs = null;
        UserPayment userPayment;
        try {
            ps = con.prepareStatement(FIND_ALL_PAYMENTS_BY_USER_ID);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
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

        userPayment.setUserId(rs.getInt(2));
        System.out.println();

//        System.out.println(rs.getDate(2));

        userPayment.setDate(rs.getTimestamp(3));

        userPayment.setSum(rs.getDouble(4));

        log.trace("User payment created ==> " + userPayment);
        return userPayment;
    }

    private void setUserPaymentsStatement(UserPayment userPayment, PreparedStatement ps) throws SQLException {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(date);

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
