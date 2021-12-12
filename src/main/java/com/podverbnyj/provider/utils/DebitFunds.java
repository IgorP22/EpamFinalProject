package com.podverbnyj.provider.utils;

import com.podverbnyj.provider.dao.UserDAO;
import com.podverbnyj.provider.dao.UserTariffDAO;
import com.podverbnyj.provider.dao.db.DBException;
import com.podverbnyj.provider.dao.db.entity.User;
import com.podverbnyj.provider.dao.db.entity.UserPayment;
import com.podverbnyj.provider.dao.db.entity.constant.Language;
import com.podverbnyj.provider.dao.db.entity.constant.Status;
import org.apache.logging.log4j.*;

import java.util.ArrayList;
import java.util.List;

import static com.podverbnyj.provider.utils.EmailSender.emailSender;

/**
 * Class DebitFunds create list of users whose balance have to be automatically
 * debited, generate list of payment history, and send email about blocking user.
 */
public class DebitFunds {
    private static final Logger log = LogManager.getLogger(DebitFunds.class);
    private static final UserDAO userDAO = UserDAO.getInstance();
    private static final UserTariffDAO userTariffDAO = UserTariffDAO.getInstance();

    private DebitFunds() {
    }

    /**
     * Debiting funds method
     *
     * @throws DBException high level message for log.
     */
    public static void debitFunds() throws DBException {
        log.info("Debiting funds from users accounts started");

        List<User> listOfUsers;
        List<UserPayment> userPaymentList = new ArrayList<>();

        // get list of all users
        listOfUsers = userDAO.findAll();

        // remove admin from list
        listOfUsers.removeIf(user -> ("admin").equals(user.getRole().value()));
        // remove blocked users from list
        listOfUsers.removeIf(user -> ("blocked").equals(user.getStatus().value()));


        for (User user : listOfUsers) {
            // get sum to debit
            double sumToDebit = userTariffDAO.getTotalCost(user.getId()) / 30;

            // block users with insufficient funds
            if (user.getBalance() < sumToDebit) {
                sendEmail(user);
                log.info("User {} blocked.", user.getLogin());
                user.setStatus(Status.BLOCKED);
            } else {
                // creating list of all payments, which was > 0
                if (sumToDebit > 0.009) {
                    user.setBalance(user.getBalance() - sumToDebit);
                    UserPayment userPayment = new UserPayment(user.getId(), -sumToDebit);
                    userPaymentList.add(userPayment);
                }
            }
        }
        // update data in DB
        userDAO.debitAllUsers(listOfUsers, userPaymentList);
    }

    /**
     * Send email to user about blocking his account. Reason: insufficient funds.
     *
     * @param user user for receiving  language, email and notification setting from DB
     */
    private static void sendEmail(User user) {
        if (user.isNotification() && (user.getEmail() != null)) {
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
            try {
                emailSender(user.getEmail(), subject, body, null);
            } catch (DBException e) {
                log.error("Email sending to user {} fall down ....", user.getLogin());
            }
            log.info("Email about blocking account sent to user{}. Reason: insufficient funds.", user.getLogin());
        }
    }
}
