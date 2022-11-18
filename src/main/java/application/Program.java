package application;

import model.dao.DaoFactory;
import model.entities.Department;
import model.entities.Seller;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class Program {
    public static void main(String[] args) {
        // INSERT DEPARTMENT
        //DaoFactory.createDepartmentDao().insert(new Department(null,"test"));

        // FIND DEPARTMENT
        //DaoFactory.createDepartmentDao().findById(2);

        // FIND ALL DEPARTMENTS
        //DaoFactory.createDepartmentDao().findAll();

        // INSERT SELLER
        //DaoFactory.createSellerDao().insert(new Seller(null, "Max", "max@gmail.com", new Date(), 2900.0, new Department(1, "Computers")));

        // FIND SELLER
        //DaoFactory.createSellerDao().findById(5);

        // FIND ALL SELLERS
        //DaoFactory.createSellerDao().findAll();

        // UPTADE SELLER
        //DaoFactory.createSellerDao().update(new Seller(4, "Alex Pink Blue", "bob@gmail.com", new Date(), 2900.0, new Department(2, "Eletronics")));

        // DELETE SELLER
        //DaoFactory.createSellerDao().deleteById(28);



    }
}
