package model.dao;

import model.dao.impl.DepartmentDao;
import model.dao.impl.SellerDao;
import util.ConnectionFactory;
import util.DbException;

import java.sql.SQLException;

public class DaoFactory {
    public static SellerDao createSellerDao()  {
        try {
            return new SellerDao(ConnectionFactory.getConnection());
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    public static DepartmentDao createDepartmentDao()  {
        try {
            return new DepartmentDao(ConnectionFactory.getConnection());
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }
}
