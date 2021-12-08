package com.podverbnyj.provider.dao.db;


import com.podverbnyj.provider.dao.db.entity.Service;
import org.junit.Before;
import org.junit.Test;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static com.podverbnyj.provider.dao.db.entity.constant.SQLConstant.ServiceConstants.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class ServiceDBManagerTest {

    private Connection con;

    @Before
    public void setUpFindAllTest() throws SQLException {
        con = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);


        when(con.prepareStatement(FIND_ALL_SERVICES))
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
        when(rs.getString("title_ru"))
                .thenReturn("Сервис 1")
                .thenReturn("Сервис 2");
        when(rs.getString("title_en"))
                .thenReturn("Service 1")
                .thenReturn("Service 2");

        when(con.prepareStatement(FIND_ALL_SERVICES))
                .thenReturn(ps);


    }


    @Test
    public void findAllTest() throws Exception {
        System.out.println("ServiceDBManager.findAllTest");

        ServiceDBManager serviceDBManager = mock(ServiceDBManager.class);

        when(serviceDBManager.findAll(con)).thenCallRealMethod();

        List<Service> services = serviceDBManager.findAll(con);

        assertEquals(2, services.size());

    }


    @Before
    public void setUpGetByIdTest() throws SQLException {
//        con = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);


        when(con.prepareStatement(GET_SERVICE_BY_ID))
                .thenReturn(ps);


        when(ps.executeQuery())
                .thenReturn(rs);

        when(rs.next())
                .thenReturn(true)
                .thenReturn(false);

        when(rs.getInt(1))
                .thenReturn(15);

        when(rs.getString("title_ru"))
                .thenReturn("Сервис 2");

        when(rs.getString("title_en"))
                .thenReturn("Service 2");

    }

    @Test
    public void getByIDTest() throws Exception {
        System.out.println("ServiceDBManager.getByLoginTest");
        ServiceDBManager serviceDBManager = mock(ServiceDBManager.class);

        when(serviceDBManager.getById(con, 15)).thenCallRealMethod();

        Service service = serviceDBManager.getById(con, 15);

        Service testService = new Service();
        testService.setTitleEn("Service 2");
        testService.setTitleRu("Сервис 2");
        testService.setId(15);

        assertEquals(testService, service);
    }


    @Before
    public void setUpCreateTest() throws SQLException {
        PreparedStatement ps = mock(PreparedStatement.class);
        when(con.prepareStatement(CREATE_SERVICE))
                .thenReturn(ps);
    }

    @Test
    public void createTest() throws Exception {
        System.out.println("ServiceDBManager.createTest");
        Service service = new Service();
        service.setTitleEn("Service 2");
        service.setTitleRu("Сервис 2");
        service.setId(15);

        ServiceDBManager serviceDBManager = mock(ServiceDBManager.class);
        when(serviceDBManager.create(con, service)).thenCallRealMethod();
        boolean result = serviceDBManager.create(con, service);
        assertTrue(result);
    }

    @Before
    public void setUpUpdateTest() throws SQLException {
        PreparedStatement ps = mock(PreparedStatement.class);
        when(con.prepareStatement(UPDATE_SERVICE))
                .thenReturn(ps);
    }

    @Test
    public void updateTest() throws Exception {
        System.out.println("ServiceDBManager.updateTest");
        Service service = new Service();
        service.setTitleEn("Service 2");
        service.setTitleRu("Сервис 2");
        service.setId(15);

        ServiceDBManager serviceDBManager = mock(ServiceDBManager.class);
        when(serviceDBManager.update(con, service)).thenCallRealMethod();


        boolean result = serviceDBManager.update(con, service);
        assertTrue(result);
    }
}

