package com.podverbnyj.provider.dao.db;


import com.podverbnyj.provider.dao.db.entity.PasswordRecovery;
import com.podverbnyj.provider.dao.db.entity.Service;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.podverbnyj.provider.dao.db.entity.constant.SQLConstant.PasswordRecoveryConstants.*;
import static com.podverbnyj.provider.dao.db.entity.constant.SQLConstant.ServiceConstants.CREATE_SERVICE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PasswordRecoveryDBManagerTest {

    private Connection con;


    @Before
    public void setUpGetByIdTest() throws SQLException {
        con = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);


        when(con.prepareStatement(GET_ENTITY_BY_USER_ID))
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
    public void getPasswordRecoveryTest() throws Exception {
        System.out.println("PasswordRecoveryDBManager.getPasswordRecoveryTest");
        PasswordRecoveryDBManager passwordRecoveryDBManager = mock(PasswordRecoveryDBManager.class);
        PasswordRecovery prTest = new PasswordRecovery();
        prTest.setUserId(2);
        prTest.setCode("superCode");

        when(passwordRecoveryDBManager.getPasswordRecovery(con, 2)).thenCallRealMethod();


        PasswordRecovery pr = passwordRecoveryDBManager.getPasswordRecovery(con, 2);
        System.out.println(pr);
        assertEquals(prTest.getCode(), pr.getCode());
    }


}

