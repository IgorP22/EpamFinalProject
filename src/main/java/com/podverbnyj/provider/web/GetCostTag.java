package com.podverbnyj.provider.web;

import com.podverbnyj.provider.dao.UserTariffDAO;
import com.podverbnyj.provider.dao.db.DBException;

import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

/**
 * My own java tag to receive cost of all connected tariffs for specified user
 */
public class GetCostTag extends TagSupport {
    private static final UserTariffDAO userTariffDAO = UserTariffDAO.getInstance();
    private int userID;

    public void setUserID(int userID) {
        this.userID = userID;
    }

    @Override
    public int doStartTag(){
        double totalCost = 0;
        try {
            totalCost = userTariffDAO.getTotalCost(userID);
        } catch (DBException e) {
            e.printStackTrace();
        }

        try {
            pageContext.getOut().println(totalCost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return SKIP_BODY;
    }
}
