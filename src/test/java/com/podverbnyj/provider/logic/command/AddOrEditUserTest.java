package com.podverbnyj.provider.logic.command;

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

import static com.podverbnyj.provider.dao.db.entity.constant.SQLConstant.ServiceConstants.CREATE_SERVICE;
import static com.podverbnyj.provider.dao.db.entity.constant.SQLConstant.ServiceConstants.FIND_ALL_SERVICES;
import static com.podverbnyj.provider.dao.db.entity.constant.SQLConstant.TariffConstants.FIND_ALL_TARIFFS;
import static com.podverbnyj.provider.logic.command.AdminRequestCommand.REFERER;
import static com.podverbnyj.provider.logic.command.AdminRequestCommand.SERVICE_NAME_RU;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class AddOrEditUserTest {
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
    public void addOrEditServiceTest() throws Exception {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        PreparedStatement ps = mock(PreparedStatement.class);

        User user = new User.UserBuilder("user5", "5A39BEAD318F306939ACB1D016647BE2E38C6501C58367FDB3E9F52542AA2442").
                setRole(Role.ADMIN).
                setLanguage(Language.RU).
                setStatus(Status.ACTIVE).
                build();

        when(req.getParameter("adminRequest")).thenReturn("Add or edit service");
        HttpSession sessionMock = mock(HttpSession.class);
        when(req.getSession()).thenReturn(sessionMock);
        when(req.getSession().getAttribute("currentUser")).thenReturn(user);

        when(req.getParameter(SERVICE_NAME_RU)).thenReturn("Сервис 3");
        when(req.getParameter("serviceNameEn")).thenReturn("Service 3");


        when(con.prepareStatement(CREATE_SERVICE))
                .thenReturn(ps);






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

        when(con.prepareStatement(FIND_ALL_TARIFFS))
                .thenReturn(ps);

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




        when(req.getSession().getAttribute("language")).thenReturn("ru");
        when(req.getSession().getAttribute("servicesIsSorted")).thenReturn(false);
        when(req.getSession().getAttribute("tariffsIsSortedByName")).thenReturn(false);
        when(req.getSession().getAttribute("sortedByPrice")).thenReturn(false);


        assertEquals("admin.jsp#success", new AdminRequestCommand().execute(req, resp));

    }


}
