package com.podverbnyj.provider.dao.db;


import com.podverbnyj.provider.dao.db.entity.PasswordRecovery;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.podverbnyj.provider.dao.db.entity.constant.SQLConstant.PasswordRecoveryConstants.GET_ENTITY_BY_CODE;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PasswordRecoveryDBManagerTestNew {

    private Connection con;


    @Before
    public void setUpGetByIdTestNew() throws SQLException {
        con = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(con.prepareStatement(GET_ENTITY_BY_CODE))
                .thenReturn(ps);

        when(ps.executeQuery())
                .thenReturn(rs);

        when(rs.next())
                .thenReturn(true)
                .thenReturn(false);

        when(rs.getInt("user_id"))
                .thenReturn(2);

        when(rs.getString("code"))
                .thenReturn("superCode");
    }

    @Test
    public void getPasswordRecoveryTestNew() throws Exception {
        System.out.println("PasswordRecoveryDBManager.getPasswordRecoveryTest");
        PasswordRecoveryDBManager passwordRecoveryDBManager = mock(PasswordRecoveryDBManager.class);
        PasswordRecovery prTest = new PasswordRecovery();
        prTest.setUserId(2);
        prTest.setCode("superCode");

        when(passwordRecoveryDBManager.getPasswordRecovery(con, "superCode")).thenCallRealMethod();

        PasswordRecovery pr = passwordRecoveryDBManager.getPasswordRecovery(con, "superCode");

        assertEquals(prTest.getUserId(), pr.getUserId());
    }


}

