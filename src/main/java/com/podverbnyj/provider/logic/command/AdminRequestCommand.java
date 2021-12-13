package com.podverbnyj.provider.logic.command;

import com.podverbnyj.provider.dao.ServiceDAO;
import com.podverbnyj.provider.dao.TariffDAO;
import com.podverbnyj.provider.dao.UserDAO;
import com.podverbnyj.provider.dao.UserTariffDAO;
import com.podverbnyj.provider.dao.db.DBException;
import com.podverbnyj.provider.dao.db.UserDBManager;
import com.podverbnyj.provider.dao.db.entity.Service;
import com.podverbnyj.provider.dao.db.entity.Tariff;
import com.podverbnyj.provider.dao.db.entity.User;
import com.podverbnyj.provider.dao.db.entity.UserTariff;
import com.podverbnyj.provider.dao.db.entity.constant.Language;
import com.podverbnyj.provider.dao.db.entity.constant.Role;
import com.podverbnyj.provider.dao.db.entity.constant.Status;
import com.podverbnyj.provider.utils.Sorter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.podverbnyj.provider.utils.EmailSender.emailSender;
import static com.podverbnyj.provider.utils.GetUser.getUser;

/**
 * AdminRequestCommand class handles all requests from admin pages of web application,
 * implements Command interface.
 */
public class AdminRequestCommand implements Command {

    private static final Logger log = LogManager.getLogger(AdminRequestCommand.class);
    private static final UserDAO userDAO = UserDAO.getInstance();
    private static final ServiceDAO serviceDAO = ServiceDAO.getInstance();
    private static final TariffDAO tariffDAO = TariffDAO.getInstance();
    private static final UserTariffDAO userTariffDAO = UserTariffDAO.getInstance();
    public static final String USER_TO_EDIT_ID = "userToEditId";
    public static final String ADMIN_USERS_JSP_SUCCESS = "admin_users.jsp#success";
    public static final String REFERER = "referer";
    public static final String SERVICE_TO_EDIT = "serviceToEdit";
    public static final String TARIFF_TO_EDIT = "tariffToEdit";
    public static final String USER_TO_EDIT = "userToEdit";
    public static final String CONFIRMATION = "confirmation";
    public static final String USER_ID_TO_DELETE = "userIdToDelete";
    public static final String USER_LOGIN = "userLogin";
    public static final String SERVICE_ID = "serviceId";
    public static final String ADMIN_JSP_SUCCESS = "admin.jsp#success";
    public static final String SERVICE_NAME_RU = "serviceNameRu";
    public static final String TARIFF_ID = "tariffId";
    public static final String TARIFF_NAME_RU = "tariffNameRu";

    /**
     * Main method for handling admin requests, get parameters of 'adminRequest' and
     * chose methods to handle it.
     *
     * @param req  request from client
     * @param resp response to client
     * @return address of web page for redirecting the user to the appropriate page
     * @throws DBException high level message for error page.
     */
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws DBException {
        resp.setCharacterEncoding("UTF-8");

        // checking access rights to administrative pages
        if (req.getSession().getAttribute("currentUser") == null) {
            log.info("Attempt to access admin page without permission");
            // redirecting to start page if no 'currentUser' in session
            return req.getHeader("index.jsp");
        }

        String adminRequest = req.getParameter("adminRequest");

        // possible parameters of 'adminRequest' for working with services and tariffs
        String getListOfServicesAndTariff = "List of services and tariffs";
        String editService = "Add or edit service";
        String deleteService = "Delete service";
        String editTariff = "Add or edit tariff";
        String deleteTariff = "Delete tariff";
        String removeDataFromSession = "Remove data";

        /*
         All next 'if' call different methods depends on 'adminRequest' parameters
         */

        // if 'adminRequest' parameters contains 'user', calling a method 'userRequests'
        // for admins work with users userRequests
        if (adminRequest.contains("user")) {
            String address = userRequests(adminRequest, req);
            if (address != null) return address;
        }

        // if 'adminRequest' parameters equal 'List of services and tariffs',
        // calling a method 'getPriceList'
        if (getListOfServicesAndTariff.equals(adminRequest)) {
            getPriceList(req);
            return req.getHeader(REFERER);
        }

        // if 'adminRequest' parameters equal 'Remove data',
        // data from session will be removed
        if (removeDataFromSession.equals(adminRequest)) {
            req.getSession().setAttribute(SERVICE_TO_EDIT, null);
            req.getSession().setAttribute(TARIFF_TO_EDIT, null);
            req.getSession().setAttribute(USER_TO_EDIT, null);
            return req.getHeader(REFERER);
        }

        // if 'adminRequest' parameters equal 'Add or edit service',
        // calling a method 'addOrEditService'
        if (editService.equals(adminRequest)) {
            String address = addOrEditService(req);
            if (address != null) return address;
        }

        // if 'adminRequest' parameters equal 'Delete service',
        // calling a method 'deleteService'
        if (deleteService.equals(adminRequest)) {
            return deleteService(req);
        }

        // if 'adminRequest' parameters equal 'Add or edit tariff',
        // calling a method 'addOrEditTariff'
        if (editTariff.equals(adminRequest)) {
            String address = addOrEditTariff(req);
            if (address != null) return address;
        }

        // if 'adminRequest' parameters equal 'Delete tariff',
        // calling a method 'deleteTariff'
        if (deleteTariff.equals(adminRequest)) {
            return deleteTariff(req);
        }

        // return to referer page
        return req.getHeader(REFERER);
    }

    /**
     * Method for handling admin requests parameters for admins work with users:
     * get list of users, block, unblock, add, edit or delete users
     *
     * @param adminRequest parameter of adminRequest
     * @param req          request from client
     * @return address of web page for redirecting the user to the appropriate page
     * @throws DBException high level message for error page.
     */
    private static String userRequests(String adminRequest, HttpServletRequest req) throws DBException {

        // possible parameters of 'adminRequest' for working with users
        String getUsersList = "List of users";
        String addOrEditUser = "Add or edit user";
        String blockUser = "Block user";
        String unblockUser = "Unblock user";
        String deleteUser = "Delete user";
        String sendEmailToAllUsers = "Email to all user";

        if (sendEmailToAllUsers.equals(adminRequest)) {
            List<User> listOfNotificatedUsers = userDAO.findAllNotificatedUsers();
            sendEmailAboutAction(listOfNotificatedUsers);
            return ADMIN_USERS_JSP_SUCCESS;
        }

        // blocking user
        if (blockUser.equals(adminRequest)) {
            int userID = Integer.parseInt(req.getParameter(USER_TO_EDIT_ID));
            User user = userDAO.getById(userID);
            user.setStatus(Status.BLOCKED);
            userDAO.update(user);
            sendEmailAboutBlocking(user);
            getUsersList(req);
            return ADMIN_USERS_JSP_SUCCESS;
        }

        // unblocking user
        if (unblockUser.equals(adminRequest)) {
            int userID = Integer.parseInt(req.getParameter(USER_TO_EDIT_ID));
            User user = userDAO.getById(userID);
            user.setStatus(Status.ACTIVE);
            userDAO.update(user);
            sendEmailAboutUnblocking(user);
            getUsersList(req);
            return ADMIN_USERS_JSP_SUCCESS;
        }

        // delete user
        if (deleteUser.equals(adminRequest)) {
            return deleteUser(req);
        }

        // add or edit user
        if (addOrEditUser.equals(adminRequest)) {
            String address = addOrEditUser(req);
            if (address != null) return address;
        }

        // get users list
        if (getUsersList.equals(adminRequest)) {
            getUsersList(req);
            return req.getHeader(REFERER);
        }

        // return null if case of some other requests
        return null;
    }

    /**
     * Send email to user about blocking his account
     *
     * @param listOfNotificatedUsers user list for receiving  language, email and notification setting from DB
     * @throws DBException high level message for error page.
     */
    private static void sendEmailAboutAction(List<User> listOfNotificatedUsers) throws DBException {
        for (User user: listOfNotificatedUsers) {
            String subject;
            String body;
            List<UserTariff> userTariffList;
            userTariffList = userTariffDAO.findAll(user.getId());

            StringBuilder tariffList = new StringBuilder();

            if (user.getLanguage() == Language.RU) {
                for (UserTariff userTariff:userTariffList) {
                    userTariff.getTariffId();
                    tariffList.append(tariffDAO.getById(userTariff.getTariffId()).getNameRu())
                            .append(":  Старая цена -  ")
                            .append(tariffDAO.getById(userTariff.getTariffId()).getPrice())
                            .append("  Новая цена: ")
                            .append((tariffDAO.getById(userTariff.getTariffId()).getPrice())/2)
                            .append(System.lineSeparator());
                }
                subject = "Aкция!!! -50% на ваши тарифы co следующего месяца";
                body = "Уважаемый пользователь, " + user.getLogin() + "." + System.lineSeparator() +
                        "Стоимость снижена " + System.lineSeparator() +
                        tariffList;
            } else {
                for (UserTariff userTariff:userTariffList) {
                    userTariff.getTariffId();
                    tariffList.append(tariffDAO.getById(userTariff.getTariffId()).getNameEn())
                            .append(":  Old price -  ")
                            .append(tariffDAO.getById(userTariff.getTariffId()).getPrice())
                            .append("  New price: ")
                            .append((tariffDAO.getById(userTariff.getTariffId()).getPrice())/2)
                            .append(System.lineSeparator());
                }
                subject = "Promotion!!! -50% on your tariffs of the next month";
                body = "Dear user, " + user.getLogin() + "." + System.lineSeparator() +
                        "Cost reduced " + System.lineSeparator() +
                        tariffList;
            }
            emailSender(user.getEmail(), subject, body, null);
            log.info("Email about blocking account sent to user{}. Reason: blocked by admin.", user.getLogin());
        }
    }



    /**
     * Send email to user about blocking his account
     *
     * @param user user for receiving  language, email and notification setting from DB
     * @throws DBException high level message for error page.
     */
    private static void sendEmailAboutBlocking(User user) throws DBException {
        if (user.isNotification() && (user.getEmail() != null)) {
            String subject;
            String body;
            if (user.getLanguage() == Language.RU) {
                subject = "Ваш аккаунт заблокирован администратором.";
                body = "Уважаемый пользователь, " + user.getLogin() + "." + System.lineSeparator() +
                        "Ваш аккаунт был заблокирован администратором." + System.lineSeparator() +
                        "Пожалуйста свяжитесь с  нами любым удобным для Вас способом..";
            } else {
                subject = "Your account has been blocked by the administrator.";
                body = "Dear user, " + user.getLogin() + "." + System.lineSeparator() +
                        "Your account has been blocked by the administrator." + System.lineSeparator() +
                        "Please contact us in any way convenient for you.";
            }
            emailSender(user.getEmail(), subject, body, null);
            log.info("Email about blocking account sent to user{}. Reason: blocked by admin.", user.getLogin());
        }
    }

    /**
     * Send email to user about unblocking his account
     *
     * @param user user for receiving  language, email and notification setting from DB
     * @throws DBException high level message for error page.
     */
    private static void sendEmailAboutUnblocking(User user) throws DBException {
        if (user.isNotification() && (user.getEmail() != null)) {
            String subject;
            String body;
            if (user.getLanguage() == Language.RU) {
                subject = "Ваш аккаунт был разблокирован.";
                body = "Уважаемый пользователь, " + user.getLogin() + "." + System.lineSeparator() +
                        "Ваш аккаунт был разблокирован.";
            } else {
                subject = "Your account has been unblocked.";
                body = "Dear user, " + user.getLogin() + "." + System.lineSeparator() +
                        "Your account has been unblocked.";
            }
            emailSender(user.getEmail(), subject, body, null);
            log.info("Email about unblocking account sent to user{}.", user.getLogin());
        }
    }

    /**
     * Get list of users and set it as attribute into session
     *
     * @param req to set session attribute
     * @throws DBException high level message for error page.
     */
    private static void getUsersList(HttpServletRequest req) throws DBException {
        List<User> listOfUsers = userDAO.findAll();
        boolean sortedByLogin = (boolean) req.getSession().getAttribute("sortedByPrice");
        if (sortedByLogin) {
            Sorter.sortUsersByLogin(listOfUsers);
        } else {
            Sorter.sortUsersByLoginReverseOrder(listOfUsers);
        }
        req.getSession().setAttribute("ListOfUsers", listOfUsers);
    }

    /**
     * Check chance to delete user and delete record from DB if allowed
     *
     * @param req receive or set confirmation flag and user id to delete
     * @return redirecting to page, depends on result
     * @throws DBException high level message for error page.
     */
    private static String deleteUser(HttpServletRequest req) throws DBException {
        String confirmation = req.getParameter(CONFIRMATION);

        // if not confirmed yet, ask for confirmation to delete
        if (confirmation == null) {
            req.getSession().setAttribute(USER_ID_TO_DELETE, req.getParameter(USER_TO_EDIT_ID));
            return "admin_users.jsp#deleteSUserConfirmation";
        }

        // if last 'admin' in DB prohibit to delete that user
        int idToDelete = Integer.parseInt((String) req.getSession().getAttribute(USER_ID_TO_DELETE));
        User user = userDAO.getById(idToDelete);
        if (user.getRole() == Role.ADMIN && userDAO.countAdmins() == 1) {
            req.setAttribute(CONFIRMATION, null);
            return "admin_users.jsp#lastAdminDeleteError";
        }

        // if confirmed delete user from DB
        if (req.getParameter(CONFIRMATION).equals("true")) {
            idToDelete = Integer.parseInt((String) req.getSession().getAttribute(USER_ID_TO_DELETE));
            userDAO.delete(userDAO.getById(idToDelete));
            getUsersList(req);
            req.setAttribute(CONFIRMATION, null);
            return ADMIN_USERS_JSP_SUCCESS;
        }
        return req.getHeader(REFERER);
    }

    /**
     * Add new user or edit existing user
     *
     * @param req receive or set some parameters or attributes on request
     * @return redirecting to page, depends on result
     * @throws DBException high level message for error page.
     */
    private static String addOrEditUser(HttpServletRequest req) throws DBException {

        // if we have only user's login, so its new user, creating record in DB
        if (req.getParameter(USER_TO_EDIT_ID) == null && req.getParameter(USER_LOGIN) != null) {
            User user;
            user = getUser(req);
            userDAO.create(user);
            log.info("User added: {}", user.getLogin());
            getUsersList(req);
            req.getSession().setAttribute(USER_TO_EDIT, null);
            return ADMIN_USERS_JSP_SUCCESS;
        }

        // if we have only user id, receive data from DB for that id and set it as userToEdit attribute in session
        if (req.getParameter(USER_TO_EDIT_ID) != null && req.getParameter(USER_LOGIN) == null) {
            int idToEdit = Integer.parseInt(req.getParameter(USER_TO_EDIT_ID));
            User user = userDAO.getById(idToEdit);
            req.setAttribute(USER_TO_EDIT_ID, idToEdit);
            req.getSession().setAttribute(USER_TO_EDIT, user);
            return "admin_users.jsp#addOrEditUser";
        }

        // if id & login present, update user's record in DB
        if (req.getParameter(USER_TO_EDIT_ID) != null && req.getParameter(USER_LOGIN) != null) {
            int idToEdit = Integer.parseInt(req.getParameter(USER_TO_EDIT_ID));
            User user;
            user = getUser(req);
            user.setId(idToEdit);
            String password = userDAO.getById(idToEdit).getPassword();
            if (password.equals(req.getParameter("userPassword"))) {
                user.setPassword(password);
            }
            user.setBalance(userDAO.getById(idToEdit).getBalance());
            userDAO.update(user);
            getUsersList(req);
            req.getSession().setAttribute(USER_TO_EDIT, null);
            return ADMIN_USERS_JSP_SUCCESS;
        }
        return null;
    }

    /**
     * Delete service after confirmation
     *
     * @param req receive or set confirmation flag and service id to delete
     * @return redirecting to page, depends on result
     * @throws DBException high level message for error page.
     */
    private String deleteService(HttpServletRequest req) throws DBException {
        String confirmation = req.getParameter(CONFIRMATION);

        // if not confirmed yet, ask for confirmation to delete
        if (confirmation == null) {
            req.getSession().setAttribute("serviceIdToDelete", req.getParameter(SERVICE_ID));
            return "admin.jsp#deleteServiceConfirmation";
        }

        // delete after confirmation
        if (req.getParameter(CONFIRMATION).equals("true")) {
            int idToDelete = Integer.parseInt((String) req.getSession().getAttribute("serviceIdToDelete"));
            serviceDAO.delete(serviceDAO.getById(idToDelete));
            log.info("Service {} deleted", idToDelete);
            getPriceList(req);
            req.setAttribute(CONFIRMATION, null);

            return ADMIN_JSP_SUCCESS;
        }
        return req.getHeader(REFERER);
    }

    /**
     * Add new service or edit existing service
     *
     * @param req receive or set some parameters or attributes on request
     * @return redirecting to page, depends on result
     * @throws DBException high level message for error page.
     */
    private String addOrEditService(HttpServletRequest req) throws DBException {

        // if we have only service name, so its new service, creating record in DB
        if (req.getParameter(SERVICE_ID) == null && req.getParameter(SERVICE_NAME_RU) != null) {
            Service service = new Service();
            service.setTitleRu(req.getParameter(SERVICE_NAME_RU));
            service.setTitleEn(req.getParameter("serviceNameEn"));
            serviceDAO.create(service);
            log.info("Service {} added", service);
            getPriceList(req);
            req.getSession().setAttribute(SERVICE_TO_EDIT, null);
            return ADMIN_JSP_SUCCESS;
        }

        // if we have only service id, receive data from DB for that id and set it as serviceToEdit attribute in session
        if (req.getParameter(SERVICE_ID) != null && req.getParameter(SERVICE_NAME_RU) == null) {
            int idToEdit = Integer.parseInt(req.getParameter(SERVICE_ID));
            Service service = serviceDAO.getById(idToEdit);
            req.setAttribute(SERVICE_ID, idToEdit);
            req.getSession().setAttribute(SERVICE_TO_EDIT, service);
            return "admin.jsp#addOrEditService";
        }

        // if id & name present, update user's record in DB
        if (req.getParameter(SERVICE_ID) != null && req.getParameter(SERVICE_NAME_RU) != null) {
            int idToEdit = Integer.parseInt(req.getParameter(SERVICE_ID));
            Service service = new Service();
            service.setId(idToEdit);
            service.setTitleRu(req.getParameter(SERVICE_NAME_RU));
            service.setTitleEn(req.getParameter("serviceNameEn"));
            serviceDAO.update(service);
            log.info("Service {} updated", service);
            getPriceList(req);
            req.getSession().setAttribute(SERVICE_TO_EDIT, null);
            return ADMIN_JSP_SUCCESS;
        }
        return null;
    }

    /**
     * Delete tariff after confirmation
     *
     * @param req receive or set confirmation flag and tariff id to delete
     * @return redirecting to page, depends on result
     * @throws DBException high level message for error page.
     */
    private String deleteTariff(HttpServletRequest req) throws DBException {
        String confirmation = req.getParameter(CONFIRMATION);

        // if not confirmed yet, ask for confirmation to delete
        if (confirmation == null) {
            req.getSession().setAttribute("tariffIdToDelete", req.getParameter(TARIFF_ID));
            return "admin.jsp#deleteTariffConfirmation";
        }

        // delete after confirmation
        if (req.getParameter(CONFIRMATION).equals("true")) {
            int idToDelete = Integer.parseInt((String) req.getSession().getAttribute("tariffIdToDelete"));
            tariffDAO.delete(tariffDAO.getById(idToDelete));
            log.info("Tariff with id {} deleted", idToDelete);
            getPriceList(req);
            req.setAttribute(CONFIRMATION, null);
            return ADMIN_JSP_SUCCESS;
        }
        return req.getHeader(REFERER);
    }

    /**
     * Add new tariff or edit existing tariff
     *
     * @param req receive or set some parameters or attributes on request
     * @return redirecting to page, depends on result
     * @throws DBException high level message for error page.
     */
    private String addOrEditTariff(HttpServletRequest req) throws DBException {
        // if we have only tariff name, so its new tariff, creating record in DB
        if (req.getParameter(TARIFF_ID) == null && req.getParameter(TARIFF_NAME_RU) != null) {
            Tariff tariff = getTariff(req);
            tariffDAO.create(tariff);
            log.info("Tariff added: {}", tariff);
            getPriceList(req);
            req.getSession().setAttribute(TARIFF_TO_EDIT, null);
            return ADMIN_JSP_SUCCESS;
        }

        // if we have only tariff id, receive data from DB for that id and set it as tariffToEdit attribute in session
        if (req.getParameter(TARIFF_ID) != null && req.getParameter(TARIFF_NAME_RU) == null) {
            int idToEdit = Integer.parseInt((req.getParameter(TARIFF_ID)));
            Tariff tariff = tariffDAO.getById(idToEdit);
            Service service = serviceDAO.getById(tariff.getServiceId());
            req.setAttribute(TARIFF_ID, idToEdit);
            req.getSession().setAttribute("serviceListForTariff", service);
            req.getSession().setAttribute(TARIFF_TO_EDIT, tariff);
            return "admin.jsp#addOrEditTariff";
        }

        // if id & name present, update tariff record in DB
        if (req.getParameter(TARIFF_ID) != null && req.getParameter(TARIFF_NAME_RU) != null) {
            int idToEdit = Integer.parseInt(req.getParameter(TARIFF_ID));
            Tariff tariff = getTariff(req);
            tariff.setId(idToEdit);
            tariffDAO.update(tariff);
            log.info("Tariff updated: {}", tariff);
            getPriceList(req);
            req.getSession().setAttribute(TARIFF_TO_EDIT, null);
            return ADMIN_JSP_SUCCESS;
        }
        return null;
    }

    /**
     * Creating tariff entity from req
     *
     * @param req receive or set some parameters or attributes on request
     * @return redirecting to page, depends on result
     */
    private Tariff getTariff(HttpServletRequest req) {
        Tariff tariff = new Tariff();

        tariff.setNameRu(req.getParameter(TARIFF_NAME_RU));
        tariff.setNameEn(req.getParameter("tariffNameEn"));
        tariff.setPrice(Double.parseDouble(req.getParameter("tariffPrice")));
        if (req.getParameter(SERVICE_ID) == null) {
            tariff.setServiceId(Integer.parseInt(req.getParameter("serviceIdForTariff")));
        } else {
            tariff.setServiceId(Integer.parseInt(req.getParameter(SERVICE_ID)));
        }

        tariff.setDescriptionRu(req.getParameter("tariffDescriptionRu"));
        tariff.setDescriptionEn(req.getParameter("tariffDescriptionEn"));
        return tariff;
    }

    /**
     * Get lists of tariffs and services, sort them according to flags
     * and set them as new session attribute. Used to update data on webpage.
     *
     * @param req receive or set some parameters or attributes on request
     * @throws DBException high level message for error page.
     */
    private void getPriceList(HttpServletRequest req) throws DBException {

        boolean servicesIsSorted = (boolean) req.getSession().getAttribute("servicesIsSorted");
        boolean tariffsIsSortedByName = (boolean) req.getSession().getAttribute("tariffsIsSortedByName");
        boolean sortedByPrice = (boolean) req.getSession().getAttribute("sortedByPrice");

        List<Service> listOfServices = serviceDAO.findAll();
        List<Tariff> listOfTariffs = tariffDAO.findAll();
        String language = req.getSession().getAttribute("language").toString();


        if (servicesIsSorted) {
            Sorter.sortServicesByName(listOfServices, language);
        }
        if (tariffsIsSortedByName) {
            Sorter.sortTariffsByName(listOfTariffs, language);
        }
        if (sortedByPrice) {
            Sorter.sortTariffsByPrice(listOfTariffs);
        }

        req.getSession().setAttribute("ListOfServices", listOfServices);
        req.getSession().setAttribute("ListOfTariffs", listOfTariffs);
    }
}
