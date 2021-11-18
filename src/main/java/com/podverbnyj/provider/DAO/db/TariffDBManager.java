package com.podverbnyj.provider.DAO.db;

import com.podverbnyj.provider.DAO.db.entity.Tariff;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.podverbnyj.provider.DAO.db.entity.constant.SQLConstant.TariffConstants.*;


public class TariffDBManager {

    private static final Logger log = LogManager.getLogger(TariffDBManager.class);


    static TariffDBManager instance;

    public static synchronized TariffDBManager getInstance() {
        if (instance == null) {
            instance = new TariffDBManager();
        }
        return instance;
    }

    private TariffDBManager() {
        // no op
    }

    public List<Tariff> findAll(Connection con) throws SQLException {
        List<Tariff> tariffs = new ArrayList<>();
        ResultSet rs = null;
        Tariff tariff;
        try {
            rs = con.createStatement().executeQuery(FIND_ALL_TARIFFS);
            while (rs.next()) {
                tariff = getTariff(rs);
                tariffs.add(tariff);
            }
            return tariffs;
        } finally {
            close(rs);
        }
    }

    public boolean create(Connection con, Tariff tariff) throws SQLException {
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(CREATE_TARIFF);
            setTariffStatement(tariff, ps);
            ps.executeUpdate();
            return true;
        } finally {
            close(ps);
        }
    }



    public Tariff getById(Connection con, int id) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Tariff tariff = null;
        try {
            ps = con.prepareStatement(GET_TARIFF_BY_ID);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                tariff = getTariff(rs);
            }
            return tariff;
        } finally {
            close(rs);
            close(ps);
        }
    }

    public boolean update(Connection con, Tariff tariff) throws SQLException {
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(UPDATE_TARIFF);
            setTariffStatement(tariff, ps);
            ps.setInt(7, tariff.getId());
            ps.executeUpdate();
            return true;
        } finally {
            close(ps);
        }
    }

    public boolean delete(Connection con, Tariff tariff) throws SQLException {
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(DELETE_TARIFF_BY_ID);
            ps.setInt(1, tariff.getId());
            ps.executeUpdate();
            return true;
        } finally {
            close(ps);
        }
    }

    public void close(AutoCloseable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (Exception ex) {
                log.error("Error closing resource " + resource, ex);
            }
        }
    }

    private void setTariffStatement(Tariff tariff, PreparedStatement ps) throws SQLException {
        int index = 1;
        ps.setString(index++, tariff.getNameRu());
        ps.setString(index++, tariff.getNameEn());
        ps.setDouble(index++, tariff.getPrice());
        ps.setInt(index++, tariff.getServiceId());
        ps.setString(index++, tariff.getDescriptionRu());
        ps.setString(index++, tariff.getDescriptionEn());
    }

    private Tariff getTariff(ResultSet rs) throws SQLException {
        Tariff tariff = new Tariff();

        tariff.setId(rs.getInt("tariff_id"));
        tariff.setNameRu(rs.getString("name_ru"));
        tariff.setNameEn(rs.getString("name_en"));
        tariff.setPrice(rs.getDouble("price"));
        tariff.setServiceId(rs.getInt("service_id"));
        tariff.setDescriptionRu(rs.getString("description_ru"));
        tariff.setDescriptionEn(rs.getString("description_en"));

        log.trace("Tariff created ==> " + tariff);
        return tariff;
    }
}
