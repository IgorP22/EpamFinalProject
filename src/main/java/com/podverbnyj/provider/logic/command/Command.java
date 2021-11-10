package com.podverbnyj.provider.logic.command;

import com.podverbnyj.provider.DAO.db.DBException;

import javax.servlet.http.*;
import java.sql.SQLException;

public interface Command {

	String execute(HttpServletRequest req,
			HttpServletResponse resp) throws DBException, SQLException;
	
	

}
