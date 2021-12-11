package com.podverbnyj.provider.dao.db;

import com.podverbnyj.provider.dao.db.entity.UserPayment;
import org.apache.logging.log4j.*;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.podverbnyj.provider.dao.db.entity.constant.SQLConstant.UserPaymentsConstants.*;

/**
 * Database table 'user_payments' DBManager
 */
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

    /**
     * Create list of all payments, made from user's account with @param userId from DB
     *
     * @param con connection received from DAO level
     * @param userId id of user for 'user_payments' table
     * @return List of all payments for specified user from DB
     * @throws SQLException in case of errors in data exchange with the database
     */
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
            close(ps);
        }
    }

    /**
     * Create list of payments, made from user's account with @param userId from DB,
     * but take only part, which set in @param limit and @param offset
     * Created to realize pagination.
     *
     * @param con connection received from DAO level
     * @param userId id of user for 'user_payments' table
     * @param limit number of records from DB
     * @param offset number of rows to skip
     * @return List of payments for specified user, with specified size from DB
     * @throws SQLException in case of errors in data exchange with the database
     */
    public List<UserPayment> findGroupByUserId(Connection con, int userId, int limit, int offset) throws SQLException {
        List<UserPayment> userPayments = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        UserPayment userPayment;
        try {
            ps = con.prepareStatement(FIND_GROUP_PAYMENTS_BY_USER_ID);
            ps.setInt(1, userId);
            ps.setInt(2, limit);
            ps.setInt(3, offset);
            rs = ps.executeQuery();
            while (rs.next()) {
                userPayment = getUserPayments(rs);
                userPayments.add(userPayment);
            }
            return userPayments;
        } finally {
            close(rs);
            close(ps);
        }
    }

    /**
     * Get number of records for user's account with @param userId from DB
     * @param con connection received from DAO level
     * @param userId id of user for 'user_payments' table
     * @return number of records for specified user's account
     * @throws SQLException in case of errors in data exchange with the database
     */
    public int getUsersPaymentsSize(Connection con, int userId) throws SQLException {
        int result = 0;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(GET_COUNT_USERS_PAYMENT_SIZE);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1);
            }
            return result;
        } finally {
            close(rs);
            close(ps);
        }
    }

    /**
     * Create new user_payments entity in DB
     * @param con connection received from DAO level
     * @param userPayment record with payment's data
     * @return 'true' if success
     * @throws SQLException in case of errors in data exchange with the database
     */
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

    /**
     * Write all data from ResultSet to the userPayment entity
     * @param rs result set
     * @return user - entity with data from @param
     * @throws SQLException in case of errors to receive data
     */
    private UserPayment getUserPayments(ResultSet rs) throws SQLException {
        UserPayment userPayment = new UserPayment();

        userPayment.setUserId(rs.getInt(2));
        userPayment.setDate(rs.getTimestamp(3));
        userPayment.setSum(rs.getDouble(4));

        log.trace("User payment created ==> {}", userPayment);
        return userPayment;
    }

    /**
     * Set fields of userPayment entity to the prepared statement parameters for SQL request
     * @param userPayment data to set into @param ps here
     * @param ps prepared statement
     * @throws SQLException in case of errors to set parameters
     */
    private void setUserPaymentsStatement(UserPayment userPayment, PreparedStatement ps) throws SQLException {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(date);

        int index = 1;
        ps.setInt(index++, userPayment.getUserId());
        ps.setString(index++, currentTime);
        ps.setDouble(index, userPayment.getSum());
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


}
