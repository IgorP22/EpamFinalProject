package com.podverbnyj.provider.logic.command;

import com.podverbnyj.provider.dao.UserDAO;
import com.podverbnyj.provider.dao.db.DBUtils;
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

import static com.podverbnyj.provider.dao.db.entity.constant.SQLConstant.UserConstants.GET_USER_BY_LOGIN;
import static com.podverbnyj.provider.dao.db.entity.constant.SQLConstant.UserTariffConstants.GET_TOTAL_COST_BY_USER_ID;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class LoginCommandTest {
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
    public void loginCommandTest() throws Exception {
        User user = new User.UserBuilder("user5", "5A39BEAD318F306939ACB1D016647BE2E38C6501C58367FDB3E9F52542AA2442").
                setRole(Role.USER).
                setLanguage(Language.RU).
                setStatus(Status.ACTIVE).
                build();



        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);

        when(req.getParameter("g-recaptcha-response")).thenReturn("test");

        when(req.getParameter("login")).thenReturn("user5");
        when(req.getParameter("password")).thenReturn("user5");

        UserDAO userDAO = mock(UserDAO.class);

        when(userDAO.getByLogin("user5")).thenReturn(user);

        PreparedStatement ps = mock(PreparedStatement.class);

        ResultSet rs = mock(ResultSet.class);

        when(con.prepareStatement(GET_USER_BY_LOGIN))
                .thenReturn(ps);

        when(ps.executeQuery())
                .thenReturn(rs);

        when(rs.next())
                .thenReturn(true)
                .thenReturn(false);

        when(rs.getString("login"))
                .thenReturn("user5");


        when(rs.getString("password"))
                .thenReturn("5A39BEAD318F306939ACB1D016647BE2E38C6501C58367FDB3E9F52542AA2442");

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
                .thenReturn("USER");

        when(rs.getInt("notification"))
                .thenReturn(-1);

        when(rs.getString("status"))
                .thenReturn("BLOCKED");

        HttpSession sessionMock = mock(HttpSession.class);
        when(req.getSession()).thenReturn(sessionMock);

        when(con.prepareStatement(GET_TOTAL_COST_BY_USER_ID))
                .thenReturn(ps);

        assertEquals("index.jsp#wrongCaptcha", new LoginCommand().execute(req, resp));

    }
}
