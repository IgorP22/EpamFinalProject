package com.podverbnyj.provider.dao.db;


import com.podverbnyj.provider.dao.db.entity.Service;
import com.podverbnyj.provider.dao.db.entity.Tariff;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static com.podverbnyj.provider.dao.db.entity.constant.SQLConstant.ServiceConstants.*;
import static com.podverbnyj.provider.dao.db.entity.constant.SQLConstant.TariffConstants.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TariffDBManagerTest {

    private Connection con;

    @Before
    public void setUpFindAllTest() throws SQLException {
        con = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);


        when(con.prepareStatement(FIND_ALL_TARIFFS))
                .thenReturn(ps);

        when(ps.executeQuery())
                .thenReturn(rs);

        when(rs.next())
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(false);

        when(rs.getInt("tariff_id"))
                .thenReturn(7)
                .thenReturn(15);

        when(rs.getString("name_ru"))
                .thenReturn("Тариф 1")
                .thenReturn("Тариф 2");

        when(rs.getString("name_en"))
                .thenReturn("Tariff 1")
                .thenReturn("Tariff 2");

        when(rs.getDouble("price"))
                .thenReturn(100.5)
                .thenReturn(200.5);

        when(rs.getInt("service_id"))
                .thenReturn(1)
                .thenReturn(2);

        when(rs.getString("description_ru"))
                .thenReturn("Описание 1")
                .thenReturn("Описание 2");

        when(rs.getString("description_en"))
                .thenReturn("Description 1")
                .thenReturn("Description 2");

    }


    @Test
    public void findAllTest() throws Exception {
        System.out.println("TariffDBManager.findAllTest");

        TariffDBManager tariffDBManager = mock(TariffDBManager.class);

        when(tariffDBManager.findAll(con)).thenCallRealMethod();

        List<Tariff> tariffs = tariffDBManager.findAll(con);

        assertEquals(2, tariffs.size());

    }


    @Before
    public void setUpGetByIdTest() throws SQLException {
//        con = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);


        when(con.prepareStatement(GET_TARIFF_BY_ID))
                .thenReturn(ps);


        when(ps.executeQuery())
                .thenReturn(rs);

        when(rs.next())
                .thenReturn(true)
                .thenReturn(false);

        when(rs.getInt("tariff_id"))
                .thenReturn(15);

        when(rs.getString("name_ru"))
                .thenReturn("Тариф 2");

        when(rs.getString("name_en"))
                .thenReturn("Tariff 2");

        when(rs.getDouble("price"))
                .thenReturn(200.5);

        when(rs.getInt("service_id"))
                .thenReturn(2);

        when(rs.getString("description_ru"))
                .thenReturn("Описание 2");

        when(rs.getString("description_en"))
                .thenReturn("Description 2");

    }

    @Test
    public void getByIDTest() throws Exception {
        System.out.println("TariffDBManager.getByLoginTest");
        TariffDBManager tariffDBManager = mock(TariffDBManager.class);

        when(tariffDBManager.getById(con, 15)).thenCallRealMethod();

        Tariff tariff = tariffDBManager.getById(con, 15);

        Tariff testTariff = new Tariff();
        testTariff.setId(15);
        testTariff.setNameRu("Тариф 2");
        testTariff.setNameEn("Tariff 2");
        testTariff.setPrice(200.5);
        testTariff.setServiceId(2);
        testTariff.setDescriptionEn("Description 2");
        testTariff.setDescriptionRu("Описание 2");


        assertEquals(testTariff, tariff);
    }


    @Before
    public void setUpCreateTest() throws SQLException {
        PreparedStatement ps = mock(PreparedStatement.class);
        when(con.prepareStatement(CREATE_TARIFF))
                .thenReturn(ps);
    }

    @Test
    public void createTest() throws Exception {
        System.out.println("TariffDBManager.createTest");
        Tariff testTariff = new Tariff();
        testTariff.setId(15);
        testTariff.setNameRu("Тариф 2");
        testTariff.setNameEn("Tariff 2");
        testTariff.setPrice(200.5);
        testTariff.setServiceId(2);
        testTariff.setDescriptionEn("Description 2");
        testTariff.setDescriptionRu("Описание 2");


        TariffDBManager tariffDBManager = mock(TariffDBManager.class);
        when(tariffDBManager.create(con, testTariff)).thenCallRealMethod();
        boolean result = tariffDBManager.create(con, testTariff);
        assertTrue(result);
    }

    @Before
    public void setUpUpdateTest() throws SQLException {
        PreparedStatement ps = mock(PreparedStatement.class);
        when(con.prepareStatement(UPDATE_TARIFF))
                .thenReturn(ps);
    }

    @Test
    public void updateTest() throws Exception {
        System.out.println("TariffDBManager.updateTest");
        Tariff testTariff = new Tariff();
        testTariff.setId(15);
        testTariff.setNameRu("Тариф 2");
        testTariff.setNameEn("Tariff 2");
        testTariff.setPrice(200.5);
        testTariff.setServiceId(2);
        testTariff.setDescriptionEn("Description 2");
        testTariff.setDescriptionRu("Описание 2");

        TariffDBManager tariffDBManager = mock(TariffDBManager.class);
        when(tariffDBManager.update(con, testTariff)).thenCallRealMethod();


        boolean result = tariffDBManager.update(con, testTariff);
        assertTrue(result);
    }


}

