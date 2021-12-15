package com.podverbnyj.provider.logic.command;

import com.podverbnyj.provider.dao.*;
import com.podverbnyj.provider.dao.db.DBException;
import com.podverbnyj.provider.dao.db.entity.Service;
import com.podverbnyj.provider.dao.db.entity.User;
import com.podverbnyj.provider.dao.db.entity.UserPayment;
import com.podverbnyj.provider.dao.db.entity.UserTariff;
import com.podverbnyj.provider.dao.db.entity.constant.Status;
import org.apache.logging.log4j.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import static com.podverbnyj.provider.utils.GetUser.getUser;

/**
 * UserRequestCommand class handles all requests from user page of web application,
 * implements Command interface.
 */
public class UserRequestCommand implements Command {

    private static final Logger log = LogManager.getLogger(UserRequestCommand.class);
    private static final UserDAO userDAO = UserDAO.getInstance();
    private static final UserPaymentDAO userPaymentDAO = UserPaymentDAO.getInstance();
    private static final UserTariffDAO userTariffDAO = UserTariffDAO.getInstance();
    private static final ServiceDAO serviceDAO = ServiceDAO.getInstance();
    public static final String USER_JSP_SUCCESS = "user.jsp#success";
    public static final String USER_FLAG = "userFlag";
    public static final String CURRENT_USER = "currentUser";
    public static final String ONLY_TOP_UP = "OnlyTopUp";

    /**
     * Main method for handling user requests, get parameters of 'userRequest' and
     * chose methods to handle it.
     *
     * @param req  request from client
     * @param resp response to client
     * @return address of web page for redirecting the user to the appropriate page
     * @throws DBException high level message for error page.
     */
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws DBException {
        String userRequest = req.getParameter("userRequest");

        // possible parameters of 'userRequest'
        String getServices = "Choice of services";
        String editProfile = "Edit profile";
        String editBalance = "Edit balance";
        String paymentHistory = "Payment history";
        String updateServices = "Update services";

        // checking access rights to administrative pages
        if (req.getSession().getAttribute(CURRENT_USER) == null) {
            log.info("Attempt to access user page without permission");
            // redirecting to start page if no 'currentUser' in session
            return "index.jsp";
        }
        req.getSession().setAttribute(USER_FLAG, null);
        User currentUser = ((User) (req.getSession().getAttribute(CURRENT_USER)));
        currentUser = userDAO.getById(currentUser.getId());
        req.getSession().setAttribute(CURRENT_USER, currentUser);
        int userID = currentUser.getId();

        // if 'userRequest' parameters equal 'Edit balance'
        if (editBalance.equals(userRequest)) {
            User user = userDAO.getById(userID);
            double sum = Double.parseDouble(req.getParameter("sum"));
            // update balance
            user.setBalance((user.getBalance() + sum));
            // unblock user balance
            user.setStatus(Status.ACTIVE);
            userDAO.update(user);
            // set record in history
            UserPayment userPayment = new UserPayment(userID, sum);
            userPaymentDAO.create(userPayment);
            // update  currentUser
            req.getSession().setAttribute(CURRENT_USER, user);
            return USER_JSP_SUCCESS;
        }

        // if 'userRequest' parameters equal 'Payment history',
        // calling a method 'getPaginatedPages'
        if (paymentHistory.equals(userRequest)) {
            return getPaginatedPages(req, userID);
        }

        // if 'userRequest' parameters equal 'Edit profile',
        // calling a method 'addOrEditUser'
        if (editProfile.equals(userRequest)) {
            return addOrEditUser(req);
        }

        // if 'userRequest' parameters equal 'Choice of services'
        if (getServices.equals(userRequest)) {
            String userFlag = "Choice";
            //receive list of users tariff
            List<UserTariff> userTariffList;
            userTariffList = userTariffDAO.findAll(userID);
            // set userTariffList to session attribute
            req.getSession().setAttribute("userTariffList", userTariffList);
            // set flag to show results on user.jsp page
            req.getSession().setAttribute(USER_FLAG, userFlag);
            return "user.jsp";
        }

        // if 'userRequest' parameters equal 'Update services'
        if (updateServices.equals(userRequest)) {
            // generate new userTariffList
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
            // record new list to DB
            userTariffDAO.update(userTariffList, userID);
            // update total cost
            double totalCost = userTariffDAO.getTotalCost(userID);
            req.getSession().setAttribute("totalCost", totalCost);
            return USER_JSP_SUCCESS;
        }

        return req.getHeader("referer");
    }

    /**
     * Realise paginated history of payments list for specified user
     *
     * @param req    receive or set flags for pagination
     * @param userID user id to receive payment history list
     * @return history of payment list, divided into pages
     * @throws DBException high level message for error page.
     */
    private String getPaginatedPages(HttpServletRequest req, int userID) throws DBException {
        // chose payments flag set
        if (req.getSession().getAttribute(ONLY_TOP_UP) == null) {
            req.getSession().setAttribute(ONLY_TOP_UP, "Off");
        }

        if ("Off".equals(req.getParameter("sorter"))) {
            req.getSession().setAttribute(ONLY_TOP_UP, "On");
        }

        if ("On".equals(req.getParameter("sorter"))) {
            req.getSession().setAttribute(ONLY_TOP_UP, "Off");
        }

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


        // switch content flag (all list or only top_up list)
        int size;
        List<UserPayment> userPaymentsList;

        if ("On".equals(req.getSession().getAttribute(ONLY_TOP_UP))) {
            size = userPaymentDAO.getUsersPaymentsTopUpSize(userID);
            userPaymentsList = userPaymentDAO.findTopUpGroup(userID, pageSize, pageSize * (page - 1));
        } else {
            size = userPaymentDAO.getUsersPaymentsSize(userID);
            userPaymentsList = userPaymentDAO.findGroup(userID, pageSize, pageSize * (page - 1));
        }

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

    /**
     * Add new user or edit existing user
     *
     * @param req receive or set some parameters or attributes on request
     * @return redirecting to page, depends on result
     * @throws DBException high level message for error page.
     */
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
}
