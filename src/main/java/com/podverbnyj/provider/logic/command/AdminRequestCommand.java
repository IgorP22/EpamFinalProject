package com.podverbnyj.provider.logic.command;

import com.podverbnyj.provider.DAO.ServiceDAO;
import com.podverbnyj.provider.DAO.TariffDAO;
import com.podverbnyj.provider.DAO.UserDAO;
import com.podverbnyj.provider.DAO.db.DBException;
import com.podverbnyj.provider.DAO.db.entity.Service;
import com.podverbnyj.provider.DAO.db.entity.Tariff;
import com.podverbnyj.provider.DAO.db.entity.User;
import com.podverbnyj.provider.DAO.db.entity.constant.Language;
import com.podverbnyj.provider.DAO.db.entity.constant.Role;
import com.podverbnyj.provider.DAO.db.entity.constant.Status;
import com.podverbnyj.provider.utils.Sorter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.podverbnyj.provider.utils.HashPassword.securePassword;

public class AdminRequestCommand implements Command {

    private static final Logger log = LogManager.getLogger(AdminRequestCommand.class);
    private static final UserDAO userDAO = UserDAO.getInstance();
    private static final ServiceDAO serviceDAO = ServiceDAO.getInstance();
    private static final TariffDAO tariffDAO = TariffDAO.getInstance();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws DBException {
        resp.setCharacterEncoding("UTF-8");
        if (req.getSession().getAttribute("currentUser") == null) {
            return req.getHeader("index.jsp");
        }

        String adminRequest = req.getParameter("adminRequest");
        System.out.println(adminRequest);

        String getListOfServicesAndTariff = "List of services and tariffs";
        String getUsersList = "List of users";

        String addOrEditUser = "Add or edit user";
        String blockUser = "Block user";
        String unblockUser = "Unblock user";
        String deleteUser = "Delete user";

        String editService = "Add or edit service";
        String deleteService = "Delete service";
        String editTariff = "Add or edit tariff";
        String deleteTariff = "Delete tariff";
        String removeDataFromSession = "Remove data";

        if (blockUser.equals(adminRequest)) {
            int userID = Integer.parseInt(req.getParameter("userToEditId"));
            User user = userDAO.getById(userID);
            user.setStatus(Status.BLOCKED);
            userDAO.update(user);
            //todo email to user
            getUsersList(req);
            return "admin_users.jsp#success";
        }

        if (unblockUser.equals(adminRequest)) {
            int userID = Integer.parseInt(req.getParameter("userToEditId"));
            User user = userDAO.getById(userID);
            user.setStatus(Status.ACTIVE);
            userDAO.update(user);
            //todo email to user
            getUsersList(req);
            return "admin_users.jsp#success";
        }

        if (deleteUser.equals(adminRequest)) {
            return deleteUser(req);
        }

        if (addOrEditUser.equals(adminRequest)) {
            String address = addOrEditUser(req);
            if (address != null) return address;
        }


        if (getListOfServicesAndTariff.equals(adminRequest)) {
            getPriceList(req);
            return req.getHeader("referer");
        }

        if (getUsersList.equals(adminRequest)) {
            getUsersList(req);
            return req.getHeader("referer");
        }

        if (removeDataFromSession.equals(adminRequest)) {
            req.getSession().setAttribute("serviceToEdit", null);
            req.getSession().setAttribute("tariffToEdit", null);
            req.getSession().setAttribute("userToEdit", null);
            return req.getHeader("referer");
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

        return req.getHeader("referer");
    }

    private void getUsersList(HttpServletRequest req) throws DBException {
        List<User> listOfUsers = userDAO.findAll();
        boolean sortedByLogin = (boolean) req.getSession().getAttribute("sortedByPrice");
        if (sortedByLogin) {
            Sorter.sortUsersByLogin(listOfUsers);
        } else {
            Sorter.sortUsersByLoginReverseOrder(listOfUsers);
        }
        req.getSession().setAttribute("ListOfUsers", listOfUsers);
    }

    private String deleteUser(HttpServletRequest req) throws DBException {
        String confirmation = req.getParameter("confirmation");
        if (confirmation == null) {
            req.getSession().setAttribute("userIdToDelete", req.getParameter("userToEditId"));
            return "admin_users.jsp#deleteSUserConfirmation";
        }

        int idToDelete = Integer.parseInt((String) req.getSession().getAttribute("userIdToDelete"));
        User user = userDAO.getById(idToDelete);
        if (user.getRole() == Role.ADMIN && userDAO.countAdmins() == 1) {
            req.setAttribute("confirmation", null);
            return "admin_users.jsp#lastAdminDeleteError";
        }

        if (req.getParameter("confirmation").equals("true")) {
            idToDelete = Integer.parseInt((String) req.getSession().getAttribute("userIdToDelete"));
            userDAO.delete(userDAO.getById(idToDelete));
            getUsersList(req);
            req.setAttribute("confirmation", null);

            return "admin_users.jsp#success";
        }
        return req.getHeader("referer");
    }

    private String addOrEditUser(HttpServletRequest req) throws DBException {

        if (req.getParameter("userToEditId") == null && req.getParameter("userLogin") != null) {
            User user;
            user = getUser(req);
//            if (userDAO.getByLogin(user.getLogin()) != null) {
//                String busyMessage = "This username is already taken";
//                req.getSession().setAttribute("busyMessage",busyMessage);
//                return req.getHeader("referer");
//            }


            userDAO.create(user);
            System.out.println("User added");
            getUsersList(req);
            req.getSession().setAttribute("userToEdit", null);
            return "admin_users.jsp#success";
        }

        if (req.getParameter("userToEditId") != null && req.getParameter("userLogin") == null) {
            int idToEdit = Integer.parseInt(req.getParameter("userToEditId"));
            User user = userDAO.getById(idToEdit);
            req.setAttribute("userToEditId", idToEdit);
            req.getSession().setAttribute("userToEdit", user);
            System.out.println(idToEdit);
            return "admin_users.jsp#addOrEditUser";
        }

        if (req.getParameter("userToEditId") != null && req.getParameter("userLogin") != null) {
            int idToEdit = Integer.parseInt(req.getParameter("userToEditId"));
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
            req.getSession().setAttribute("userToEdit", null);
            return "admin_users.jsp#success";
        }
        return null;
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

    private String deleteService(HttpServletRequest req) throws DBException {
        String confirmation = req.getParameter("confirmation");
        if (confirmation == null) {
            req.getSession().setAttribute("serviceIdToDelete", req.getParameter("serviceId"));
            return "admin.jsp#deleteServiceConfirmation";
        }
        if (req.getParameter("confirmation").equals("true")) {
            int idToDelete = Integer.parseInt((String) req.getSession().getAttribute("serviceIdToDelete"));
            serviceDAO.delete(serviceDAO.getById(idToDelete));
            System.out.println("Service " + idToDelete + " deleted");
            getPriceList(req);
            req.setAttribute("confirmation", null);

            return "admin.jsp#success";
        }
        return req.getHeader("referer");
    }

    private String addOrEditService(HttpServletRequest req) throws DBException {
        if (req.getParameter("serviceId") == null && req.getParameter("serviceNameRu") != null) {
            System.out.println("Service added");
            Service service = new Service();
            service.setTitleRu(req.getParameter("serviceNameRu"));
            service.setTitleEn(req.getParameter("serviceNameEn"));
            serviceDAO.create(service);
            getPriceList(req);
            req.getSession().setAttribute("serviceToEdit", null);
            return "admin.jsp#success";
        }

        if (req.getParameter("serviceId") != null && req.getParameter("serviceNameRu") == null) {
            int idToEdit = Integer.parseInt(req.getParameter("serviceId"));
            Service service = serviceDAO.getById(idToEdit);
            req.setAttribute("serviceId", idToEdit);
            req.getSession().setAttribute("serviceToEdit", service);
            return "admin.jsp#addOrEditService";
        }

        if (req.getParameter("serviceId") != null && req.getParameter("serviceNameRu") != null) {
            int idToEdit = Integer.parseInt(req.getParameter("serviceId"));
            Service service = new Service();
            service.setId(idToEdit);
            service.setTitleRu(req.getParameter("serviceNameRu"));
            service.setTitleEn(req.getParameter("serviceNameEn"));
            serviceDAO.update(service);
            getPriceList(req);
            req.getSession().setAttribute("serviceToEdit", null);
            return "admin.jsp#success";
        }
        return null;
    }

    private String deleteTariff(HttpServletRequest req) throws DBException {
        String confirmation = req.getParameter("confirmation");
        System.out.println(confirmation);
        if (confirmation == null) {
            req.getSession().setAttribute("tariffIdToDelete", req.getParameter("tariffId"));
            return "admin.jsp#deleteTariffConfirmation";
        }
        if (req.getParameter("confirmation").equals("true")) {
            int idToDelete = Integer.parseInt((String) req.getSession().getAttribute("tariffIdToDelete"));
            tariffDAO.delete(tariffDAO.getById(idToDelete));
            System.out.println("Tariff " + idToDelete + " deleted");
            getPriceList(req);
            req.setAttribute("confirmation", null);

            return "admin.jsp#success";
        }
        return req.getHeader("referer");
    }

    private String addOrEditTariff(HttpServletRequest req) throws DBException {
        if (req.getParameter("tariffId") == null && req.getParameter("tariffNameRu") != null) {
            System.out.println("Tariff added");
            Tariff tariff = getTariff(req);
            tariffDAO.create(tariff);
            getPriceList(req);
            req.getSession().setAttribute("tariffToEdit", null);
            return "admin.jsp#success";
        }

        if (req.getParameter("tariffId") != null && req.getParameter("tariffNameRu") == null) {
            int idToEdit = Integer.parseInt((req.getParameter("tariffId")));
            Tariff tariff = tariffDAO.getById(idToEdit);
            Service service = serviceDAO.getById(tariff.getServiceId());
            req.setAttribute("tariffId", idToEdit);
            req.getSession().setAttribute("serviceListForTariff", service);
            req.getSession().setAttribute("tariffToEdit", tariff);
            return "admin.jsp#addOrEditTariff";
        }

        if (req.getParameter("tariffId") != null && req.getParameter("tariffNameRu") != null) {
            int idToEdit = Integer.parseInt(req.getParameter("tariffId"));
            Tariff tariff = getTariff(req);
            tariff.setId(idToEdit);
            tariffDAO.update(tariff);
            getPriceList(req);
            req.getSession().setAttribute("tariffToEdit", null);
            return "admin.jsp#success";
        }
        return null;
    }


    private Tariff getTariff(HttpServletRequest req) {
        Tariff tariff = new Tariff();

        tariff.setNameRu(req.getParameter("tariffNameRu"));
        tariff.setNameEn(req.getParameter("tariffNameEn"));
        tariff.setPrice(Double.parseDouble(req.getParameter("tariffPrice")));
        if (req.getParameter("serviceId") == null) {
            tariff.setServiceId(Integer.parseInt(req.getParameter("serviceIdForTariff")));
        } else {
            tariff.setServiceId(Integer.parseInt(req.getParameter("serviceId")));
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
