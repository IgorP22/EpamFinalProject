package com.podverbnyj.provider.logic.command;

import com.podverbnyj.provider.dao.*;
import com.podverbnyj.provider.dao.db.DBException;
import com.podverbnyj.provider.dao.db.entity.Service;
import com.podverbnyj.provider.dao.db.entity.User;
import com.podverbnyj.provider.dao.db.entity.UserPayment;
import com.podverbnyj.provider.dao.db.entity.UserTariff;
import com.podverbnyj.provider.dao.db.entity.constant.Language;
import com.podverbnyj.provider.dao.db.entity.constant.Role;
import com.podverbnyj.provider.dao.db.entity.constant.Status;
import org.apache.logging.log4j.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    public static final String USER_JSP_SUCCESS = "user.jsp#success";
    public static final String USER_FLAG = "userFlag";
    public static final String CURRENT_USER = "currentUser";


    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws DBException, SQLException {
        String userRequest = req.getParameter("userRequest");

        String getServices = "Choice of services";
        String editProfile = "Edit profile";
        String editBalance = "Edit balance";
        String paymentHistory = "Payment history";
        String updateServices = "Update services";

        if (req.getSession().getAttribute(CURRENT_USER) == null) {
            log.info("Attempt to access page without permission");
            return "index.jsp";
        }
        req.getSession().setAttribute(USER_FLAG, null);
        User currentUser = ((User) (req.getSession().getAttribute(CURRENT_USER)));
        int userID = currentUser.getId();

        if (editBalance.equals(userRequest)) {
            User user = userDAO.getById(userID);
            double sum = Double.parseDouble(req.getParameter("sum"));
            user.setBalance((user.getBalance() + sum));
            user.setStatus(Status.ACTIVE);
            userDAO.update(user);
            UserPayment userPayment = new UserPayment(userID, sum);
            userPaymentDAO.create(userPayment);
            req.getSession().setAttribute(CURRENT_USER, user);
            return USER_JSP_SUCCESS;
        }

        if (paymentHistory.equals(userRequest)) {
            return getPaginatedPages(req, userID);
        }

        if (editProfile.equals(userRequest)) {
            return addOrEditUser(req);
        }

        if (getServices.equals(userRequest)) {
            String userFlag = "Choice";
            List<UserTariff> userTariffList;
            userTariffList = userTariffDAO.findAll(userID);
            req.getSession().setAttribute("userTariffList", userTariffList);
            req.getSession().setAttribute(USER_FLAG, userFlag);
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
            return USER_JSP_SUCCESS;
        }

        return req.getHeader("referer");
    }

    private String getPaginatedPages(HttpServletRequest req, int userID) throws DBException {
        final String PAGE_SIZE = "pageSize";
        int page;
        if (req.getParameter("page") == null || req.getParameter("page").equals("")) {
            page = 1;
        } else {
            String paramPage = req.getParameter("page");
            page = Integer.parseInt(paramPage);
        }

        int pageSize;
        if (req.getParameter(PAGE_SIZE) == null || req.getParameter(PAGE_SIZE).equals("")) {
            pageSize = 10;
        } else {
            String paramPageSize = req.getParameter(PAGE_SIZE);
            pageSize = Integer.parseInt(paramPageSize);
        }

        int size = userPaymentDAO.getUsersPaymentsSize(userID);


        List<UserPayment> userPaymentsList = userPaymentDAO.findGroup(userID, pageSize, pageSize * (page - 1));

        int minPagePossible = Math.max(page, 1);

        int pageCount = (int) Math.ceil((double) size / pageSize);
        int maxPagePossible = Math.min(page, pageCount);

        req.getSession().setAttribute("userPaymentsList", userPaymentsList);
        req.getSession().setAttribute("pageCount", pageCount);
        req.getSession().setAttribute("page", page);
        req.getSession().setAttribute(PAGE_SIZE, pageSize);
        req.getSession().setAttribute("minPossiblePage", minPagePossible);
        req.getSession().setAttribute("maxPossiblePage", maxPagePossible);

        String userFlag = "History";
        req.getSession().setAttribute(USER_FLAG, userFlag);
        return "user.jsp";
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
        user.setBalance(((User) req.getSession().getAttribute(CURRENT_USER)).getBalance());

        userDAO.update(user);
        req.getSession().setAttribute(CURRENT_USER, user);
        req.getSession().setAttribute("userToEdit", null);
        return USER_JSP_SUCCESS;
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
