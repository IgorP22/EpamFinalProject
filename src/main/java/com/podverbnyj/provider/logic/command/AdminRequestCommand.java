package com.podverbnyj.provider.logic.command;

import com.podverbnyj.provider.DAO.ServiceDAO;
import com.podverbnyj.provider.DAO.TariffDAO;
import com.podverbnyj.provider.DAO.UserDAO;
import com.podverbnyj.provider.DAO.db.DBException;
import com.podverbnyj.provider.DAO.db.entity.Service;
import com.podverbnyj.provider.DAO.db.entity.Tariff;
import com.podverbnyj.provider.DAO.db.entity.User;
import com.podverbnyj.provider.utils.Sorter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class AdminRequestCommand implements Command {

    private static final Logger log = LogManager.getLogger(AdminRequestCommand.class);
    private static final UserDAO userDAO = UserDAO.getInstance();
    private static final ServiceDAO serviceDAO = ServiceDAO.getInstance();
    private static final TariffDAO tariffDAO = TariffDAO.getInstance();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws DBException {
        String adminRequest = req.getParameter("adminRequest");
        System.out.println(adminRequest);
        String getListOfServicesAndTariff = "List of services and tariffs";

        String getUsersList = "List of users";
        String addUser = "Add new user";
        String editService = "Add or edit service";
        String deleteService = "Delete service";
        String editTariff = "Add or edit tariff";
        String deleteTariff = "Delete tariff";
        String removeDataFromSession = "Remove data";

        String adminFlag;

        if (getListOfServicesAndTariff.equals(adminRequest)) {
            getPriceList(req);
            adminFlag = "price";
            req.getSession().setAttribute("adminFlag", adminFlag);
            System.out.println(adminFlag);
            return req.getHeader("referer");
        }

        if (getUsersList.equals(adminRequest)) {
            List<User> listOfUsers = userDAO.findAll();
            req.getSession().setAttribute("ListOfUsers", listOfUsers);
            adminFlag = "users";
            req.getSession().setAttribute("adminFlag", adminFlag);
            req.getSession().setAttribute("usersIsSorted", false);
            return req.getHeader("referer");
        }

        if (removeDataFromSession.equals(adminRequest)) {
            req.getSession().setAttribute("serviceToEdit", null);
            req.getSession().setAttribute("tariffToEdit", null);
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
            req.setAttribute("confirmation",null);

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
            int idToEdit = Integer.parseInt((String) req.getParameter("serviceId"));
            Service service = serviceDAO.getById(idToEdit);
            req.setAttribute("serviceId", idToEdit);
            req.getSession().setAttribute("serviceToEdit", service);
            return "admin.jsp#addOrEditService";
        }


        if (req.getParameter("serviceId") != null && req.getParameter("serviceNameRu") != null) {
            int idToEdit = Integer.parseInt((String) req.getParameter("serviceId"));
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
            req.setAttribute("confirmation",null);

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
            int idToEdit = Integer.parseInt((String) req.getParameter("tariffId"));
            Tariff tariff = tariffDAO.getById(idToEdit);
            Service service = serviceDAO.getById(tariff.getServiceId());
            req.setAttribute("tariffId", idToEdit);
//            req.setAttribute("serviceId", tariff.getServiceId());
//            req.setAttribute("serviceNameRu", idToEdit);
//            System.out.println(tariff.getServiceId());
//            System.out.printf("serviceI-"+req.getParameter("serviceId"));
//            System.out.printf("serviceI-"+req.getAttribute("serviceId"));
//            System.out.printf("serviceI-"+req.getSession().getAttribute("serviceId"));
            req.getSession().setAttribute("serviceListForTariff", service);
            req.getSession().setAttribute("tariffToEdit", tariff);
            return "admin.jsp#addOrEditTariff";
        }


        if (req.getParameter("tariffId") != null && req.getParameter("tariffNameRu") != null) {
            int idToEdit = Integer.parseInt((String) req.getParameter("tariffId"));
            Tariff tariff = getTariff(req);
            tariff.setId(idToEdit);
            System.out.println(tariff);

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
        tariff.setServiceId(Integer.parseInt((String) req.getParameter("serviceId")));
        tariff.setDescriptionRu(req.getParameter("tariffDescriptionRu"));
        tariff.setDescriptionEn(req.getParameter("tariffDescriptionEn"));
        return tariff;
    }

    private void getPriceList(HttpServletRequest req) throws DBException {

        boolean servicesIsSorted = (boolean) req.getSession().getAttribute("servicesIsSorted");
        boolean tariffsIsSortedByName = (boolean) req.getSession().getAttribute("tariffsIsSortedByName");
        boolean sortedByPrice = (boolean) req.getSession().getAttribute("sortedByPrice");

        System.out.println("n1 " + servicesIsSorted + " " + tariffsIsSortedByName + " " + sortedByPrice);

        List<Service> listOfServices = serviceDAO.findAll();
        List<Tariff> listOfTariffs = tariffDAO.findAll();
        if (servicesIsSorted) {
            Sorter.sortServicesByName(listOfServices, "ru");
        }
        if (tariffsIsSortedByName) {
            Sorter.sortTariffsByName(listOfTariffs, "ru");
        }
        if (sortedByPrice) {
            Sorter.sortTariffsByPrice(listOfTariffs);
        }


        req.getSession().setAttribute("ListOfServices", listOfServices);
        req.getSession().setAttribute("ListOfTariffs", listOfTariffs);


        System.out.println("n2" + servicesIsSorted + " " + tariffsIsSortedByName + " " + sortedByPrice);

    }
}
