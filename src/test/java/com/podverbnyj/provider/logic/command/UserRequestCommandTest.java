package com.podverbnyj.provider.logic.command;

import com.podverbnyj.provider.dao.ServiceDAO;
import com.podverbnyj.provider.dao.TariffDAO;
import com.podverbnyj.provider.dao.UserDAO;
import com.podverbnyj.provider.dao.db.DBUtils;
import com.podverbnyj.provider.dao.db.ServiceDBManager;
import com.podverbnyj.provider.dao.db.UserDBManager;
import com.podverbnyj.provider.dao.db.entity.Service;
import com.podverbnyj.provider.dao.db.entity.User;
import com.podverbnyj.provider.dao.db.entity.constant.Language;
import com.podverbnyj.provider.dao.db.entity.constant.Role;
import com.podverbnyj.provider.dao.db.entity.constant.Status;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockedStatic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static com.podverbnyj.provider.dao.db.entity.constant.SQLConstant.ServiceConstants.CREATE_SERVICE;
import static com.podverbnyj.provider.dao.db.entity.constant.SQLConstant.ServiceConstants.FIND_ALL_SERVICES;
import static com.podverbnyj.provider.dao.db.entity.constant.SQLConstant.TariffConstants.FIND_ALL_TARIFFS;
import static com.podverbnyj.provider.dao.db.entity.constant.SQLConstant.UserConstants.GET_USER_BY_ID;
import static com.podverbnyj.provider.dao.db.entity.constant.SQLConstant.UserConstants.UPDATE_USER;
import static com.podverbnyj.provider.logic.command.AdminRequestCommand.REFERER;
import static com.podverbnyj.provider.logic.command.AdminRequestCommand.SERVICE_NAME_RU;
import static com.podverbnyj.provider.utils.HashPassword.securePassword;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class UserRequestCommandTest {

    private DBUtils dbUtils;
    private static MockedStatic<DBUtils> dbUtilsMock;
    private Connection con;

    @BeforeClass
    public static void setUpGlobal() {
        dbUtilsMock = mockStatic(DBUtils.class);
    }

    @AfterClass
    public static void tearDownAll() {
        dbUtilsMock.close();
    }

    @Before
    public void setUp() {
        con = mock(Connection.class);

        dbUtils = mock(DBUtils.class);
        when(dbUtils.getConnection()).thenReturn(con);

        dbUtilsMock.when(DBUtils::getInstance)
                .thenReturn(dbUtils);
    }


    @Test
    public void editUserTest() throws Exception {

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        User testUser = new User.UserBuilder("user5", "5A39BEAD318F306939ACB1D016647BE2E38C6501C58367FDB3E9F52542AA2442").
                setRole(Role.USER).
                setLanguage(Language.RU).
                setStatus(Status.ACTIVE).
                setId(10).
                build();

        HttpSession sessionMock = mock(HttpSession.class);
        when(req.getSession()).thenReturn(sessionMock);

        when(req.getParameter("userRequest")).thenReturn("Edit profile");
        when(req.getSession().getAttribute("currentUser")).thenReturn(testUser);
        when(req.getParameter("userToEditId")).thenReturn("10");

        when(req.getParameter("userLogin")).thenReturn("user5");
        when(req.getParameter("userPassword")).thenReturn("5A39BEAD318F306939ACB1D016647BE2E38C6501C58367FDB3E9F52542AA2442");
        when(req.getParameter("userEmail")).thenReturn("test");
        when(req.getParameter("userName")).thenReturn("test");
        when(req.getParameter("userSurname")).thenReturn("test");
        when(req.getParameter("userPhone")).thenReturn("test");
        when(req.getParameter("userLanguage")).thenReturn("RU");
        when(req.getParameter("userRole")).thenReturn("USER");
        when(req.getParameter("userNotification")).thenReturn("0");
        when(req.getParameter("userStatus")).thenReturn("ACTIVE");



        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);
        when(con.prepareStatement(any()))
                .thenReturn(ps);
        when(ps.executeQuery())
                .thenReturn(rs);




        when(ps.executeUpdate()).thenReturn(1);


        when(ps.executeQuery())
                .thenReturn(rs);




        when(rs.next())
                .thenReturn(true)
                .thenReturn(false);

        when(rs.getString("login"))
                .thenReturn("obama");


        when(rs.getString("password"))
                .thenReturn("obamapass");


        when(rs.getInt("user_id"))
                .thenReturn(7);


        when(rs.getString("email"))
                .thenReturn("testfinalproject2@gmail.com");


        when(rs.getString("name"))
                .thenReturn("Andrey");


        when(rs.getString("surname"))
                .thenReturn("Andreev");


        when(rs.getString("phone"))
                .thenReturn("+38(050)333-33-33");


        when(rs.getDouble("balance"))
                .thenReturn(330.5);

        when(rs.getString("language"))
                .thenReturn("RU");

        when(rs.getString("role"))
                .thenReturn("ADMIN");

        when(rs.getInt("notification"))
                .thenReturn(-1);

        when(rs.getString("status"))
                .thenReturn("BLOCKED");



        assertEquals("user.jsp#success", new UserRequestCommand().execute(req, resp));


    }


    @Test
    public void editBalanceTest() throws Exception {

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        User testUser = new User.UserBuilder("user5", "5A39BEAD318F306939ACB1D016647BE2E38C6501C58367FDB3E9F52542AA2442").
                setRole(Role.USER).
                setLanguage(Language.RU).
                setStatus(Status.ACTIVE).
                setId(10).
                build();

        HttpSession sessionMock = mock(HttpSession.class);
        when(req.getSession()).thenReturn(sessionMock);

        when(req.getParameter("userRequest")).thenReturn("Edit balance");
        when(req.getSession().getAttribute("currentUser")).thenReturn(testUser);
        when(req.getParameter("userToEditId")).thenReturn("10");

        when(req.getParameter("userLogin")).thenReturn("user5");
        when(req.getParameter("userPassword")).thenReturn("5A39BEAD318F306939ACB1D016647BE2E38C6501C58367FDB3E9F52542AA2442");
        when(req.getParameter("userEmail")).thenReturn("test");
        when(req.getParameter("userName")).thenReturn("test");
        when(req.getParameter("userSurname")).thenReturn("test");
        when(req.getParameter("userPhone")).thenReturn("test");
        when(req.getParameter("userLanguage")).thenReturn("RU");
        when(req.getParameter("userRole")).thenReturn("USER");
        when(req.getParameter("userNotification")).thenReturn("0");
        when(req.getParameter("userStatus")).thenReturn("ACTIVE");



        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);
        when(con.prepareStatement(any()))
                .thenReturn(ps);
        when(ps.executeQuery())
                .thenReturn(rs);





        when(rs.next())
                .thenReturn(true)
                .thenReturn(false);

        when(rs.getString("login"))
                .thenReturn("obama");


        when(rs.getString("password"))
                .thenReturn("obamapass");


        when(rs.getInt("user_id"))
                .thenReturn(7);


        when(rs.getString("email"))
                .thenReturn("testfinalproject2@gmail.com");


        when(rs.getString("name"))
                .thenReturn("Andrey");


        when(rs.getString("surname"))
                .thenReturn("Andreev");


        when(rs.getString("phone"))
                .thenReturn("+38(050)333-33-33");


        when(rs.getDouble("balance"))
                .thenReturn(330.5);

        when(rs.getString("language"))
                .thenReturn("RU");

        when(rs.getString("role"))
                .thenReturn("ADMIN");

        when(rs.getInt("notification"))
                .thenReturn(-1);

        when(rs.getString("status"))
                .thenReturn("BLOCKED");


        when(req.getParameter("sum")).thenReturn("500.5");




        assertEquals("user.jsp#success", new UserRequestCommand().execute(req, resp));


    }

    @Test
    public void paymentHistoryTest() throws Exception {

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        User testUser = new User.UserBuilder("user5", "5A39BEAD318F306939ACB1D016647BE2E38C6501C58367FDB3E9F52542AA2442").
                setRole(Role.USER).
                setLanguage(Language.RU).
                setStatus(Status.ACTIVE).
                setId(10).
                build();

        HttpSession sessionMock = mock(HttpSession.class);
        when(req.getSession()).thenReturn(sessionMock);

        when(req.getParameter("userRequest")).thenReturn("Payment history");
        when(req.getSession().getAttribute("currentUser")).thenReturn(testUser);
        when(req.getParameter("userToEditId")).thenReturn("10");

        when(req.getParameter("userLogin")).thenReturn("user5");
        when(req.getParameter("userPassword")).thenReturn("5A39BEAD318F306939ACB1D016647BE2E38C6501C58367FDB3E9F52542AA2442");
        when(req.getParameter("userEmail")).thenReturn("test");
        when(req.getParameter("userName")).thenReturn("test");
        when(req.getParameter("userSurname")).thenReturn("test");
        when(req.getParameter("userPhone")).thenReturn("test");
        when(req.getParameter("userLanguage")).thenReturn("RU");
        when(req.getParameter("userRole")).thenReturn("USER");
        when(req.getParameter("userNotification")).thenReturn("0");
        when(req.getParameter("userStatus")).thenReturn("ACTIVE");



        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);
        when(con.prepareStatement(any()))
                .thenReturn(ps);
        when(ps.executeQuery())
                .thenReturn(rs);





        when(rs.next())
                .thenReturn(true)
                .thenReturn(false);

        when(rs.getString("login"))
                .thenReturn("obama");


        when(rs.getString("password"))
                .thenReturn("obamapass");


        when(rs.getInt("user_id"))
                .thenReturn(7);


        when(rs.getString("email"))
                .thenReturn("testfinalproject2@gmail.com");


        when(rs.getString("name"))
                .thenReturn("Andrey");


        when(rs.getString("surname"))
                .thenReturn("Andreev");


        when(rs.getString("phone"))
                .thenReturn("+38(050)333-33-33");


        when(rs.getDouble("balance"))
                .thenReturn(330.5);

        when(rs.getString("language"))
                .thenReturn("RU");

        when(rs.getString("role"))
                .thenReturn("ADMIN");

        when(rs.getInt("notification"))
                .thenReturn(-1);

        when(rs.getString("status"))
                .thenReturn("BLOCKED");


        when(req.getParameter("sum")).thenReturn("500.5");






        assertEquals("user.jsp", new UserRequestCommand().execute(req, resp));


    }

}
