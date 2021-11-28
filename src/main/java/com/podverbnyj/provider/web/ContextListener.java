package com.podverbnyj.provider.web;


import com.podverbnyj.provider.DAO.db.DBException;
import org.apache.logging.log4j.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Calendar;
import java.util.Properties;

import static com.podverbnyj.provider.utils.DebitFunds.debitFunds;

@WebListener
public class ContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();
        String path = ctx.getRealPath("/WEB-INF/log4j2.log");
        System.setProperty("logFile", path);

        final Logger log = LogManager.getLogger(ContextListener.class);
        log.debug("path = " + path);

        debitFundsThread(log);


        // I18n initialization

        // obtain file name with locales descriptions
        String localesFileName = ctx.getInitParameter("locales");
        System.out.println(localesFileName);

        // obtain real path on server
        String localesFileRealPath = ctx.getRealPath(localesFileName);
        System.out.println(localesFileRealPath);
        // local descriptions
        Properties locales = new Properties();
        try {
            locales.load(new FileInputStream(localesFileRealPath));
        } catch (IOException ex) {
            log.error("Error to start I18n " + ex);
        }

        // save descriptions to servlet context
        ctx.setAttribute("locales", locales);

        log.debug("locales ==> " + locales);

    }

    private void debitFundsThread(Logger log) {
        Thread t1 = new Thread(() -> {
            while (true) {
                Calendar c = Calendar.getInstance();
                c.add(Calendar.DAY_OF_MONTH, 1);
                c.set(Calendar.HOUR_OF_DAY, 0);
                c.set(Calendar.MINUTE, 0);
                c.set(Calendar.SECOND, 0);
                c.set(Calendar.MILLISECOND, 0);
                long timeBeforeTaskStart = (c.getTimeInMillis() - System.currentTimeMillis()) + 500000;
                Duration duration = Duration.ofMillis(timeBeforeTaskStart);
                long hours = duration.toHours();
                duration = duration.minusHours(hours);
                long minutes = duration.toMinutes();
                duration = duration.minusMinutes(minutes);
                long seconds = duration.getSeconds();
                duration = duration.minusSeconds(seconds);
                long milliSec = duration.toMillis();
                String logMessage = "Time to start debiting funds:" + hours + "h " + minutes + "min "
                        + seconds + "sec " + milliSec + "ms";
                System.out.println(timeBeforeTaskStart);
                log.info(logMessage);

                try {
                    Thread.sleep(timeBeforeTaskStart);
                } catch (InterruptedException ex) {
                    log.error("Thread fall down, funds will not be debited " + ex);
                }
                try {
                    debitFunds();
                } catch (DBException ex) {
                    log.error("Error to debit funds" + ex);
                }
            }
        });
        t1.start();
    }
}
