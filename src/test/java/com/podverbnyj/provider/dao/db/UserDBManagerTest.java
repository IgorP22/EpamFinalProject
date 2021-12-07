package com.podverbnyj.provider.dao.db;


import static com.podverbnyj.provider.dao.db.entity.constant.SQLConstant.UserConstants.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import com.podverbnyj.provider.dao.db.entity.User;

import com.podverbnyj.provider.dao.db.entity.constant.Language;
import com.podverbnyj.provider.dao.db.entity.constant.Role;
import com.podverbnyj.provider.dao.db.entity.constant.Status;
import org.junit.*;

import java.sql.*;
import java.util.List;

public class UserDBManagerTest {

    private Connection con;


    @Before
    public void setUpFindAllTest() throws SQLException {
        con = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(con.prepareStatement(FIND_ALL_USERS))
                .thenReturn(ps);

        when(ps.executeQuery())
                .thenReturn(rs);

        when(rs.next())
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(false);

        when(rs.getString("login"))
                .thenReturn("obama")
                .thenReturn("tramp");

        when(rs.getString("password"))
                .thenReturn("obamapass")
                .thenReturn("tramppass");

        when(rs.getInt("user_id"))
                .thenReturn(7)
                .thenReturn(15);

        when(rs.getString("email"))
                .thenReturn("testfinalproject2@gmail.com")
                .thenReturn("testfinalproject2@gmail.com");

        when(rs.getString("name"))
                .thenReturn("Andrey")
                .thenReturn("Sergey");

        when(rs.getString("surname"))
                .thenReturn("Andreev")
                .thenReturn("Sergeev");

        when(rs.getString("phone"))
                .thenReturn("+38(050)333-33-33")
                .thenReturn("+38(050)555-44-44");

        when(rs.getDouble("balance"))
                .thenReturn(220.5)
                .thenReturn(330.5);

        when(rs.getString("language"))
                .thenReturn("RU")
                .thenReturn("RU");

        when(rs.getString("role"))
                .thenReturn("USER")
                .thenReturn("ADMIN");

        when(rs.getInt("notification"))
                .thenReturn(0)
                .thenReturn(-1);

        when(rs.getString("status"))
                .thenReturn("ACTIVE")
                .thenReturn("BLOCKED");
    }


    @Test
    public void findAllTest() throws Exception {
        System.out.println("UserDBManager.findAllTest");

        System.out.println(con);

        UserDBManager userDBManager = mock(UserDBManager.class);

        when(userDBManager.findAll(con)).thenCallRealMethod();

        List<User> users = userDBManager.findAll(con);

        assertEquals(2, users.size());

    }

    @Before
    public void setUpGetByLoginTest() throws SQLException {
//        con = mock(Connection.class);
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
    }

    @Test
    public void getByLoginTest() throws Exception {
        System.out.println("UserDBManager. getByLoginTest");
        UserDBManager userDBManager = mock(UserDBManager.class);

        when(userDBManager.getByLogin(con, "obama")).thenCallRealMethod();

        User user = userDBManager.getByLogin(con, "obama");
        User user1 = new User.UserBuilder("obama", "obamapass").build();
        assertEquals(user1, user);
    }

    @Before
    public void setUpGetByIdTest() throws SQLException {
//        con = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);


        when(con.prepareStatement(GET_USER_BY_ID))
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
    }

    @Test
    public void getByIDTest() throws Exception {
        System.out.println("UserDBManager.getByLoginTest");
        UserDBManager userDBManager = mock(UserDBManager.class);

        when(userDBManager.getById(con, 7)).thenCallRealMethod();

        User user = userDBManager.getById(con, 7);

        User testUser = new User.UserBuilder("obama", "obamapass").build();
        testUser.setId(15);
        testUser.setEmail("testfinalproject2@gmail.com");
        testUser.setName("Sergey");
        testUser.setSurname("Sergeev");
        testUser.setPhone("+38(050)222-22-22");
        testUser.setBalance(200);
        testUser.setLanguage(Language.RU);
        testUser.setRole(Role.USER);
        testUser.setNotification(true);
        testUser.setStatus(Status.ACTIVE);
        assertEquals(testUser, user);
    }

    @Before
    public void setUpCreateTest() throws SQLException {
        PreparedStatement ps = mock(PreparedStatement.class);
        when(con.prepareStatement(CREATE_USER))
                .thenReturn(ps);
    }

    @Test
    public void createTest() throws Exception {
        System.out.println("UserDBManager.createTest");
        User user = new User.UserBuilder("obama", "obamapass").build();

        UserDBManager userDBManager = mock(UserDBManager.class);
        when(userDBManager.create(con, user)).thenCallRealMethod();
        boolean result = userDBManager.create(con, user);
        assertTrue(result);
    }

    @Before
    public void setUpUpdateTest() throws SQLException {
        PreparedStatement ps = mock(PreparedStatement.class);
        when(con.prepareStatement(UPDATE_USER))
                .thenReturn(ps);
    }

    @Test
    public void updateTest() throws Exception {
        System.out.println("UserDBManager.updateTest");
        User user = new User.UserBuilder("obama", "obamapass").build();

        UserDBManager userDBManager = mock(UserDBManager.class);
        when(userDBManager.update(con, user)).thenCallRealMethod();


        boolean result = userDBManager.update(con, user);
        assertTrue(result);
    }

    @Before
    public void setUpCountAdminsTest() throws SQLException {

        PreparedStatement ps = mock(PreparedStatement.class);
        when(con.prepareStatement(COUNT_ADMINS))
                .thenReturn(ps);

        ResultSet rs = mock(ResultSet.class);

        when(ps.executeQuery())
                .thenReturn(rs);

        when(rs.next())
                .thenReturn(true);

        when(rs.getInt(1))
                .thenReturn(5);
    }

    @Test
    public void countAdminsTest() throws Exception {
        System.out.println("UserDBManager.countAdminsTest");

        UserDBManager userDBManager = mock(UserDBManager.class);
        when(userDBManager.countAdmins(con)).thenCallRealMethod();
        int result = userDBManager.countAdmins(con);
        assertEquals(5, result);
    }


}

