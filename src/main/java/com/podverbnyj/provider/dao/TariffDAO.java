package com.podverbnyj.provider.dao;

import com.podverbnyj.provider.dao.db.DBException;
import com.podverbnyj.provider.dao.db.DBUtils;
import com.podverbnyj.provider.dao.db.TariffDBManager;
import com.podverbnyj.provider.dao.db.entity.Tariff;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * DAO level for database table 'tariff', implements AbstractDAO interface,
 * parametrized by Tariff.
 *
 * All method get connection from datasource, and call the same named method
 * from TariffDBManager on it, send or receive data (more comments in TariffDBManager),
 * and close connection at the end.
 *
 * If TariffDBManager throw exception, it's caught here and replaced by our own DBException
 * with high level message for error page.
 */
public class TariffDAO implements AbstractDAO<Tariff> {

    private static final Logger log = LogManager.getLogger(TariffDAO.class);
    private static final DBUtils dbUtils = DBUtils.getInstance();
    private static final TariffDBManager tariffDBManager = TariffDBManager.getInstance();

    static TariffDAO instance;

    public static synchronized TariffDAO getInstance() {
        if (instance == null) {
            instance = new TariffDAO();
        }
        return instance;
    }

    private TariffDAO() {
        // no op
    }


    @Override
    public List<Tariff> findAll() throws DBException {
        Connection con = dbUtils.getConnection();
        try {
            return tariffDBManager.findAll(con);
        } catch (SQLException ex) {
            log.error("Can't receive list of tariffs from DB", ex);
            throw new DBException("Can't receive list of tariffs from DB");
        } finally {
            tariffDBManager.close(con);
        }
    }

    @Override
    public boolean create(Tariff tariff) throws DBException {
        Connection con = dbUtils.getConnection();
        try {
            return tariffDBManager.create(con, tariff);
        } catch (SQLException ex) {
            log.error("Can't create tariff ==> {}", tariff, ex);
            throw new DBException("Can't create tariff ==> " + tariff);
        } finally {
            tariffDBManager.close(con);
        }
    }


    @Override
    public Tariff getById(int id) throws DBException {
        Connection con = dbUtils.getConnection();
        try {
            return tariffDBManager.getById(con, id);
        } catch (SQLException ex) {
            log.error("Can't get tariff by ID ==> {}", id, ex);
            throw new DBException("Can't create tariff by ID ==> " + id);
        } finally {
            tariffDBManager.close(con);
        }
    }


    @Override
    public boolean update(Tariff tariff) throws DBException {
        Connection con = dbUtils.getConnection();
        try {
            return tariffDBManager.update(con, tariff);
        } catch (SQLException ex) {
            log.error("Can't update tariff ==> {}", tariff.getId(), ex);
            throw new DBException("Can't update tariff ==> " + tariff.getId());
        } finally {
            tariffDBManager.close(con);
        }
    }

    @Override
    public boolean delete(Tariff tariff) throws DBException {
        Connection con = dbUtils.getConnection();
        try {
            return tariffDBManager.delete(con, tariff);
        } catch (SQLException ex) {
            log.error("Can't delete tariff ==> {}", tariff.getId(), ex);
            throw new DBException("Can't delete tariff ==> " + tariff.getId());
        } finally {
            tariffDBManager.close(con);
        }
    }
}
