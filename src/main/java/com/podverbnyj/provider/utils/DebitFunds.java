package com.podverbnyj.provider.utils;

import com.podverbnyj.provider.DAO.UserDAO;
import com.podverbnyj.provider.DAO.UserTariffDAO;
import com.podverbnyj.provider.DAO.db.DBException;
import com.podverbnyj.provider.DAO.db.entity.User;
import com.podverbnyj.provider.DAO.db.entity.UserPayment;
import com.podverbnyj.provider.DAO.db.entity.constant.Language;
import com.podverbnyj.provider.DAO.db.entity.constant.Status;
import org.apache.logging.log4j.*;

import java.util.ArrayList;
import java.util.List;

import static com.podverbnyj.provider.utils.EmailSender.emailSender;

public class DebitFunds {
    private static final Logger log = LogManager.getLogger(DebitFunds.class);
    private static final UserDAO userDAO = UserDAO.getInstance();
    private static final UserTariffDAO userTariffDAO = UserTariffDAO.getInstance();


    public static void debitFunds() throws DBException {
        log.info("Debiting funds from users accounts started");
        List<User> listOfUsers;
        List<UserPayment> userPaymentList = new ArrayList<>();

        listOfUsers = userDAO.findAll();

        listOfUsers.removeIf(user -> ("admin").equals(user.getRole().value()));

        for (User user : listOfUsers) {
            double sumToDebit = userTariffDAO.getTotalCost(user.getId()) / 30;
            if (sumToDebit < 0.009) {
                listOfUsers.remove(user);
            }
        }

        for (User user : listOfUsers) {
            double sumToDebit = userTariffDAO.getTotalCost(user.getId()) / 30;

            if (user.getBalance() < sumToDebit) {
                if (user.getStatus() == Status.ACTIVE) {
                    if (user.getEmail() != null) {
                        String subject;
                        String body;
                        if (user.getLanguage() == Language.RU) {
                            subject = "Ваш аккаунт заблокирован.";
                            body = "Уважаемый пользователь, " + user.getLogin() + "." + System.lineSeparator() +
                                    "Ваш аккаунт заблокирован в связи с недостатком средств." + System.lineSeparator() +
                                    "Пополните пожалуйста счет для возобновления услуг." + System.lineSeparator() +
                                    "С наилучшими пожеланиями, Ваш провайдер.) ";
                        } else {
                            subject = "Your account has been blocked.";
                            body = "Dear user, " + user.getLogin() + "." + System.lineSeparator() +
                                    "Your account has been blocked due to insufficient funds." + System.lineSeparator() +
                                    "Please top up your account to resume services.." + System.lineSeparator() +
                                    "Best wishes, your provider.) ";
                        }
                        emailSender(user.getEmail(), subject, body, null);
                    }
                }
                user.setStatus(Status.BLOCKED);
            } else {
                user.setBalance(user.getBalance() - sumToDebit);
                user.setStatus(Status.ACTIVE);
                UserPayment userPayment = new UserPayment(user.getId(), -sumToDebit);
                userPaymentList.add(userPayment);
            }
        }
        userDAO.debitAllUsers(listOfUsers, userPaymentList);
    }
}
