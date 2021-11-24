package com.podverbnyj.provider.logic.command;

import com.podverbnyj.provider.DAO.ServiceDAO;
import com.podverbnyj.provider.DAO.TariffDAO;
import com.podverbnyj.provider.DAO.UserDAO;
import com.podverbnyj.provider.DAO.db.DBException;
import com.podverbnyj.provider.DAO.db.entity.Service;
import com.podverbnyj.provider.DAO.db.entity.Tariff;
import com.podverbnyj.provider.DAO.db.entity.User;
import com.podverbnyj.provider.DAO.db.entity.constant.Role;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.podverbnyj.provider.utils.HashPassword.securePassword;

public class DownloadCommand implements Command {

    private static final Logger log = LogManager.getLogger(DownloadCommand.class);
    private static final UserDAO userDAO = UserDAO.getInstance();
    private static final ServiceDAO serviceDAO = ServiceDAO.getInstance();
    private static final TariffDAO tariffDAO = TariffDAO.getInstance();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws DBException {

        String file = req.getParameter("file");
        System.out.println(file);



        List<Tariff> tariffList = (List<Tariff>) req.getSession().getAttribute("ListOfServices");
        List<Service> serviceList = (List<Service>) req.getSession().getAttribute("ListOfTariffs");
        System.out.println(tariffList);
        System.out.println(serviceList);


        return req.getHeader("referer");
    }
}
