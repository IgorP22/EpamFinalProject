package com.podverbnyj.provider.web;


import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.podverbnyj.provider.DAO.db.DBException;
import com.podverbnyj.provider.logic.command.Command;
import com.podverbnyj.provider.logic.command.CommandContainer;
import org.apache.logging.log4j.*;

@WebServlet("/controller")
public class Controller extends HttpServlet {

    private static final Logger log = LogManager.getLogger(Controller.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String commandName = req.getParameter("command");
        log.trace("command ==> " + commandName);
        Command command = CommandContainer.getCommand(commandName);
        String address = "error.jsp";
        try {
            address = command.execute(req, resp);
        } catch (DBException | SQLException ex) {
            log.error("Error receiving address ",ex);
            req.setAttribute("ex", ex);
        }
        req.getRequestDispatcher(address).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String commandName = req.getParameter("command");
        log.trace("commandName ==> " + commandName);

        Command command = CommandContainer.getCommand(commandName);
        log.trace("command ==> " + command);
        String address = "error.jsp";
        try {
            address = command.execute(req, resp);
        } catch (DBException | SQLException ex) {
            log.error("Error receiving address ",ex);
            req.getSession().setAttribute("ex", ex);
        }
        resp.sendRedirect(address);
    }

}
