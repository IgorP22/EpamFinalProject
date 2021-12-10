package com.podverbnyj.provider.dao.db;


import com.podverbnyj.provider.dao.db.entity.UserTariff;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


import static com.podverbnyj.provider.dao.db.entity.constant.SQLConstant.UserTariffConstants.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserTariffDBManagerTest {

    private Connection con;

    @Before
    public void setUpFindAllByUserIdTest() throws SQLException {
        con = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);


        when(con.prepareStatement(GET_ALL_TARIFFS_BY_USER_ID))
                .thenReturn(ps);

        when(ps.executeQuery())
                .thenReturn(rs);

        when(rs.next())
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(false);

        when(rs.getInt(1))
                .thenReturn(7)
                .thenReturn(15);

        when(rs.getInt(2))
                .thenReturn(7)
                .thenReturn(30);
    }


    @Test
    public void findAllByUserIdTest() throws Exception {
        System.out.println("UserTariffDBManager.findAllByUserIdTest");
        UserTariffDBManager userTariffDBManager = mock(UserTariffDBManager.class);
        when(userTariffDBManager.findAllByUserId(con, 7)).thenCallRealMethod();
        List<UserTariff> userTariffs = userTariffDBManager.findAllByUserId(con, 7);
        assertEquals(2, userTariffs.size());

    }

    @Before
    public void setUpGetTotalCostTest() throws SQLException {

        PreparedStatement ps = mock(PreparedStatement.class);
        when(con.prepareStatement(GET_TOTAL_COST_BY_USER_ID))
                .thenReturn(ps);

        ResultSet rs = mock(ResultSet.class);

        when(ps.executeQuery())
                .thenReturn(rs);

        when(rs.next())
                .thenReturn(true);

        when(rs.getDouble(1))
                .thenReturn(500.0);
    }

    @Test
    public void getTotalCostTest() throws Exception {
        System.out.println("UserTariffDBManager.getTotalCostTest");

        UserTariffDBManager userTariffDBManager = mock(UserTariffDBManager.class);
        when(userTariffDBManager.getTotalCost(con, 2)).thenCallRealMethod();
        double result = userTariffDBManager.getTotalCost(con, 2);
        assertEquals(500, result, 0.0);
    }

}

