package com.podverbnyj.provider.logic.command;

import com.podverbnyj.provider.dao.db.DBException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogoutCommand implements Command {

    private static final Logger log = LogManager.getLogger(LogoutCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws DBException {

        log.info("Session invalidated");
        req.getSession().invalidate();
        return "index.jsp";
    }
}
