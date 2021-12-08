package com.podverbnyj.provider.logic.command;

import com.podverbnyj.provider.dao.ServiceDAO;
import com.podverbnyj.provider.dao.TariffDAO;
import com.podverbnyj.provider.dao.UserDAO;
import com.podverbnyj.provider.dao.db.DBException;
import com.podverbnyj.provider.dao.db.entity.Service;
import com.podverbnyj.provider.dao.db.entity.Tariff;
import com.podverbnyj.provider.dao.db.entity.User;
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

public class AdminRequestCommand implements Command {

    private static final Logger log = LogManager.getLogger(AdminRequestCommand.class);
    private static final UserDAO userDAO = UserDAO.getInstance();
    private static final ServiceDAO serviceDAO = ServiceDAO.getInstance();
    private static final TariffDAO tariffDAO = TariffDAO.getInstance();
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

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws DBException {
        resp.setCharacterEncoding("UTF-8");
        if (req.getSession().getAttribute("currentUser") == null) {
            return req.getHeader("index.jsp");
        }
        String adminRequest = req.getParameter("adminRequest");

        String getListOfServicesAndTariff = "List of services and tariffs";

        String editService = "Add or edit service";
        String deleteService = "Delete service";
        String editTariff = "Add or edit tariff";
        String deleteTariff = "Delete tariff";
        String removeDataFromSession = "Remove data";
        if (adminRequest.contains("user")) {

            String address = userRequests(adminRequest,req);
            if (address != null) return address;

        }
        if (getListOfServicesAndTariff.equals(adminRequest)) {
            getPriceList(req);
            return req.getHeader(REFERER);
        }
        if (removeDataFromSession.equals(adminRequest)) {
            req.getSession().setAttribute(SERVICE_TO_EDIT, null);
            req.getSession().setAttribute(TARIFF_TO_EDIT, null);
            req.getSession().setAttribute(USER_TO_EDIT, null);
            return req.getHeader(REFERER);
        }
        if (editService.equals(adminRequest)) {
            String address = addOrEditService(req);
            if (address != null) return address;
        }

        if (deleteService.equals(adminRequest)) {
            return deleteService(req);
        }

        if (editTariff.equals(adminRequest)) {
            String address = addOrEditTariff(req);
            if (address != null) return address;
        }

        if (deleteTariff.equals(adminRequest)) {
            return deleteTariff(req);
        }

        return req.getHeader(REFERER);
    }


    private static String userRequests(String adminRequest, HttpServletRequest req) throws DBException {
        String getUsersList = "List of users";
        String addOrEditUser = "Add or edit user";
        String blockUser = "Block user";
        String unblockUser = "Unblock user";
        String deleteUser = "Delete user";

        if (blockUser.equals(adminRequest)) {
            int userID = Integer.parseInt(req.getParameter(USER_TO_EDIT_ID));
            User user = userDAO.getById(userID);
            user.setStatus(Status.BLOCKED);
            userDAO.update(user);
            sendEmailAboutBlocking(user);
            getUsersList(req);
            return ADMIN_USERS_JSP_SUCCESS;
        }

        if (unblockUser.equals(adminRequest)) {
            int userID = Integer.parseInt(req.getParameter(USER_TO_EDIT_ID));
            User user = userDAO.getById(userID);
            user.setStatus(Status.ACTIVE);
            userDAO.update(user);
            sendEmailAboutUnblocking(user);
            getUsersList(req);
            return ADMIN_USERS_JSP_SUCCESS;
        }

        if (deleteUser.equals(adminRequest)) {
            return deleteUser(req);
        }

        if (addOrEditUser.equals(adminRequest)) {
            String address = addOrEditUser(req);
            if (address != null) return address;
        }

        if (getUsersList.equals(adminRequest)) {
            getUsersList(req);
            return req.getHeader(REFERER);
        }
        return null;
    }

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

    private static String deleteUser(HttpServletRequest req) throws DBException {
        String confirmation = req.getParameter(CONFIRMATION);
        if (confirmation == null) {
            req.getSession().setAttribute(USER_ID_TO_DELETE, req.getParameter(USER_TO_EDIT_ID));
            return "admin_users.jsp#deleteSUserConfirmation";
        }

        int idToDelete = Integer.parseInt((String) req.getSession().getAttribute(USER_ID_TO_DELETE));
        User user = userDAO.getById(idToDelete);
        if (user.getRole() == Role.ADMIN && userDAO.countAdmins() == 1) {
            req.setAttribute(CONFIRMATION, null);
            return "admin_users.jsp#lastAdminDeleteError";
        }

        if (req.getParameter(CONFIRMATION).equals("true")) {
            idToDelete = Integer.parseInt((String) req.getSession().getAttribute(USER_ID_TO_DELETE));
            userDAO.delete(userDAO.getById(idToDelete));
            getUsersList(req);
            req.setAttribute(CONFIRMATION, null);

            return ADMIN_USERS_JSP_SUCCESS;
        }
        return req.getHeader(REFERER);
    }

    private static String addOrEditUser(HttpServletRequest req) throws DBException {

        if (req.getParameter(USER_TO_EDIT_ID) == null && req.getParameter(USER_LOGIN) != null) {
            User user;
            user = getUser(req);
            userDAO.create(user);
            log.info("User added: {}", user.getLogin());
            getUsersList(req);
            req.getSession().setAttribute(USER_TO_EDIT, null);
            return ADMIN_USERS_JSP_SUCCESS;
        }

        if (req.getParameter(USER_TO_EDIT_ID) != null && req.getParameter(USER_LOGIN) == null) {
            int idToEdit = Integer.parseInt(req.getParameter(USER_TO_EDIT_ID));
            User user = userDAO.getById(idToEdit);
            req.setAttribute(USER_TO_EDIT_ID, idToEdit);
            req.getSession().setAttribute(USER_TO_EDIT, user);
            return "admin_users.jsp#addOrEditUser";
        }

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

    private String deleteService(HttpServletRequest req) throws DBException {
        String confirmation = req.getParameter(CONFIRMATION);
        if (confirmation == null) {
            req.getSession().setAttribute("serviceIdToDelete", req.getParameter(SERVICE_ID));
            return "admin.jsp#deleteServiceConfirmation";
        }
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

    private String addOrEditService(HttpServletRequest req) throws DBException {
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

        if (req.getParameter(SERVICE_ID) != null && req.getParameter(SERVICE_NAME_RU) == null) {
            int idToEdit = Integer.parseInt(req.getParameter(SERVICE_ID));
            Service service = serviceDAO.getById(idToEdit);
            req.setAttribute(SERVICE_ID, idToEdit);
            req.getSession().setAttribute(SERVICE_TO_EDIT, service);
            return "admin.jsp#addOrEditService";
        }

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

    private String deleteTariff(HttpServletRequest req) throws DBException {
        String confirmation = req.getParameter(CONFIRMATION);
        if (confirmation == null) {
            req.getSession().setAttribute("tariffIdToDelete", req.getParameter(TARIFF_ID));
            return "admin.jsp#deleteTariffConfirmation";
        }
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

    private String addOrEditTariff(HttpServletRequest req) throws DBException {
        if (req.getParameter(TARIFF_ID) == null && req.getParameter(TARIFF_NAME_RU) != null) {
            Tariff tariff = getTariff(req);
            tariffDAO.create(tariff);
            log.info("Tariff added: {}", tariff);
            getPriceList(req);
            req.getSession().setAttribute(TARIFF_TO_EDIT, null);
            return ADMIN_JSP_SUCCESS;
        }

        if (req.getParameter(TARIFF_ID) != null && req.getParameter(TARIFF_NAME_RU) == null) {
            int idToEdit = Integer.parseInt((req.getParameter(TARIFF_ID)));
            Tariff tariff = tariffDAO.getById(idToEdit);
            Service service = serviceDAO.getById(tariff.getServiceId());
            req.setAttribute(TARIFF_ID, idToEdit);
            req.getSession().setAttribute("serviceListForTariff", service);
            req.getSession().setAttribute(TARIFF_TO_EDIT, tariff);
            return "admin.jsp#addOrEditTariff";
        }

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
