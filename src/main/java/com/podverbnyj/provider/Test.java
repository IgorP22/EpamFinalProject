package com.podverbnyj.provider;


import com.podverbnyj.provider.DAO.db.DBUtils;

import org.apache.logging.log4j.*;


import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/test")
public class Test extends HelloServlet{
    private static final Logger log = LogManager.getLogger(Test.class);


    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("Test#doGet");

        Connection con = DBUtils.getInstance().getConnection();

        resp.getWriter().println(con);
        log.debug("con = " + con);
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
