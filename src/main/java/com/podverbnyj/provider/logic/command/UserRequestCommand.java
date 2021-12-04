package com.podverbnyj.provider.logic.command;

import com.podverbnyj.provider.DAO.*;
import com.podverbnyj.provider.DAO.db.DBException;
import com.podverbnyj.provider.DAO.db.entity.Service;
import com.podverbnyj.provider.DAO.db.entity.User;
import com.podverbnyj.provider.DAO.db.entity.UserPayment;
import com.podverbnyj.provider.DAO.db.entity.UserTariff;
import com.podverbnyj.provider.DAO.db.entity.constant.Language;
import com.podverbnyj.provider.DAO.db.entity.constant.Role;
import com.podverbnyj.provider.DAO.db.entity.constant.Status;
import org.apache.logging.log4j.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.podverbnyj.provider.utils.HashPassword.securePassword;

public class UserRequestCommand implements Command {

    private static final Logger log = LogManager.getLogger(UserRequestCommand.class);
    private static final UserDAO userDAO = UserDAO.getInstance();
    private static final UserPaymentDAO userPaymentDAO = UserPaymentDAO.getInstance();
    private static final UserTariffDAO userTariffDAO = UserTariffDAO.getInstance();
    private static final ServiceDAO serviceDAO = ServiceDAO.getInstance();
    private static final int shift = 0;

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws DBException, SQLException {
        String userRequest = req.getParameter("userRequest");
        System.out.println(userRequest);

        String getServices = "Choice of services";
        String editProfile = "Edit profile";
        String editBalance = "Edit balance";
        String paymentHistory = "Payment history";
        String updateServices = "Update services";

        if (req.getSession().getAttribute("currentUser") == null) {
            return "index.jsp";
        }
        req.getSession().setAttribute("userFlag", null);
        int userID = ((User) (req.getSession().getAttribute("currentUser"))).getId();

        if (editBalance.equals(userRequest)) {
            User user = userDAO.getById(userID);
            System.out.println(req.getParameter("sum"));
            double sum = Double.parseDouble(req.getParameter("sum"));
            user.setBalance((user.getBalance() + sum));
            user.setStatus(Status.ACTIVE);
            userDAO.update(user);
            UserPayment userPayment = new UserPayment(userID, sum);
            userPaymentDAO.create(userPayment);


            req.getSession().setAttribute("currentUser", user);
            return "user.jsp#success";
        }

        if (paymentHistory.equals(userRequest)) {
//            List<UserPayment> userPaymentsList;
//            userPaymentsList = userPaymentDAO.findAll(userID);
//            System.out.println(userPaymentsList);
//            req.getSession().setAttribute("userPaymentsList", userPaymentsList);

            int page;
            if (req.getParameter("page") == null || req.getParameter("page").equals("")) {
                page = 1;
            } else {
                String paramPage = req.getParameter("page");
                page = Integer.parseInt(paramPage);
            }

            int pageSize;
            if (req.getParameter("pageSize") == null || req.getParameter("pageSize").equals("")) {
                pageSize = 10;
            } else {
                String paramPageSize = req.getParameter("pageSize");
                pageSize = Integer.parseInt(paramPageSize);
            }

            System.out.println(page);
            System.out.println(pageSize);


            int size = userPaymentDAO.getUsersPaymentsSize(userID);
            System.out.println(size);


            List<UserPayment> userPaymentsList = userPaymentDAO.findGroup(userID, pageSize, pageSize * (page - 1));
            System.out.println(userPaymentsList);

            int minPagePossible = Math.max(page - shift, 1);

            int pageCount = (int) Math.ceil((double) size / pageSize);
            System.out.println(pageCount);
            int maxPagePossible = Math.min(page + shift, pageCount);

            req.getSession().setAttribute("userPaymentsList", userPaymentsList);
            req.getSession().setAttribute("pageCount", pageCount);
            req.getSession().setAttribute("page", page);
            req.getSession().setAttribute("pageSize", pageSize);
            req.getSession().setAttribute("minPossiblePage", minPagePossible);
            req.getSession().setAttribute("maxPossiblePage", maxPagePossible);

            String userFlag = "History";
            req.getSession().setAttribute("userFlag", userFlag);
            return "user.jsp";
//            try {
//                req.getRequestDispatcher("WEB-INF/jsp/user.jsp").forward(req, resp);
//            } catch (ServletException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

        }

        if (editProfile.equals(userRequest)) {
            return addOrEditUser(req);
        }

        if (getServices.equals(userRequest)) {
            String userFlag = "Choice";
            List<UserTariff> userTariffList;
            userTariffList = userTariffDAO.findAll(userID);
            System.out.println(userTariffList);
            req.getSession().setAttribute("userTariffList", userTariffList);
            req.getSession().setAttribute("userFlag", userFlag);
        }

        if (updateServices.equals(userRequest)) {
            List<UserTariff> userTariffList = new ArrayList<>();
            List<Service> serviceList;
            serviceList = serviceDAO.findAll();
            for (Service service : serviceList) {
                String serviceId = String.valueOf(service.getId());
                if (req.getParameter(serviceId) != null) {
                    int userTariffId = Integer.parseInt(req.getParameter(serviceId));
                    UserTariff userTariff = new UserTariff(userID, userTariffId);
                    userTariffList.add(userTariff);
                }
            }
            userTariffDAO.update(userTariffList, userID);
            double totalCost = userTariffDAO.getTotalCost(userID);
            req.getSession().setAttribute("totalCost", totalCost);
            System.out.println(userTariffList);
            return "user.jsp#success";
        }

        return req.getHeader("referer");
    }

    private String addOrEditUser(HttpServletRequest req) throws DBException {

        int idToEdit = Integer.parseInt(req.getParameter("userToEditId"));
        User user;
        user = getUser(req);
        user.setId(idToEdit);
        String password = userDAO.getById(idToEdit).getPassword();
        if (password.equals(req.getParameter("userPassword"))) {
            user.setPassword(password);
        }
        user.setBalance(((User) req.getSession().getAttribute("currentUser")).getBalance());

        userDAO.update(user);
        req.getSession().setAttribute("currentUser", user);
        req.getSession().setAttribute("userToEdit", null);
        return "user.jsp#success";
    }

    private User getUser(HttpServletRequest req) throws DBException {
        User user;
        user = new User.UserBuilder(
                req.getParameter("userLogin"),
                securePassword(req.getParameter("userPassword")))
                .setEmail(req.getParameter("userEmail"))
                .setName(req.getParameter("userName"))
                .setSurname(req.getParameter("userSurname"))
                .setPhone(req.getParameter("userPhone"))
                .setLanguage(Language.valueOf(req.getParameter("userLanguage")))
                .setRole(Role.valueOf(req.getParameter("userRole")))
                .setNotification(Boolean.parseBoolean(req.getParameter("userNotification")))
                .setStatus(Status.valueOf(req.getParameter("userStatus")))
                .build();
        return user;
    }
}
