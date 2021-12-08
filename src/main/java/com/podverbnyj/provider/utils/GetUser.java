package com.podverbnyj.provider.utils;

import com.podverbnyj.provider.dao.db.DBException;
import com.podverbnyj.provider.dao.db.entity.User;
import com.podverbnyj.provider.dao.db.entity.constant.Language;
import com.podverbnyj.provider.dao.db.entity.constant.Role;
import com.podverbnyj.provider.dao.db.entity.constant.Status;

import javax.servlet.http.HttpServletRequest;

import static com.podverbnyj.provider.utils.HashPassword.securePassword;

public class GetUser {
    private GetUser() {
    }

    public static User getUser(HttpServletRequest req) throws DBException {
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
