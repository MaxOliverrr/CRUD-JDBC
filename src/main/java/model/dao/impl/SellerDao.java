package model.dao.impl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import model.dao.GenericDao;
import model.entities.Department;
import model.entities.Seller;
import util.DbException;

import java.sql.*;
import java.sql.Date;
import java.util.*;

@NoArgsConstructor
@AllArgsConstructor

public class SellerDao implements GenericDao {
    private Connection connection;
    @Override
    public void insert(Object obj) {
        Seller seller = (Seller) obj;
        String sql = "INSERT INTO seller (Name, Email, BirthDate, BaseSalary, DepartmentId) VALUES (?, ?, ?, ?, ?)";

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setString(1, seller.getName());
            preparedStatement.setString(2, seller.getEmail());
            preparedStatement.setDate(3,  new Date(seller.getBirthDate().getTime()));
            preparedStatement.setString(4, seller.getBaseSalary().toString());
            preparedStatement.setString(5, seller.getDepartment().getId().toString());

            int rowsAffected = preparedStatement.executeUpdate();

            if(rowsAffected > 0){
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if(resultSet.next()){
                    int id = resultSet.getInt(1);
                    seller.setId(id);
                    System.out.println("O seller " + seller.getName() + " foi adicionado com o id " + id);
                }
                resultSet.close();
            } else {
                throw new DbException("Nenhuma coluna foi afetada");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public void update(Object obj) {
        Seller seller = (Seller) obj;
        String sql = "UPDATE seller SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ?  WHERE id = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, seller.getName());
            preparedStatement.setString(2, seller.getEmail());
            preparedStatement.setDate(3,  new Date(seller.getBirthDate().getTime()));
            preparedStatement.setString(4, seller.getBaseSalary().toString());
            preparedStatement.setString(5, seller.getDepartment().getId().toString());
            preparedStatement.setInt(6, seller.getId());

            int rowsAffected = preparedStatement.executeUpdate();

            if(rowsAffected > 0){
                System.out.println("O seller " + seller.getId() + " foi atualizado");
                return;
            }
            System.out.println("Nenhum seller foi atualizado");

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM seller WHERE seller.id = ?";
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            if(rowsAffected > 0){
                System.out.println("Seller " +id+ " deletado");
                return;
            }
            System.out.println("Nenhum seller com o id " +id+ " encontrado");
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    private Optional<Seller> getSellerById(Integer id)  {
        String sql = "SELECT seller.*, department.name as departmentName FROM seller JOIN department ON seller.DepartmentId = department.Id WHERE seller.id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql);){
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return Optional.of(new Seller(rs
                        .getInt("id"), rs
                        .getString("Name"), rs
                        .getString("Email"), rs
                        .getDate("BirthDate"), rs
                        .getDouble("BaseSalary"), new Department(rs
                        .getInt("DepartmentId"), rs
                        .getString("departmentName"))));
            }
            rs.close();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public void findById(Integer id) {
        Optional<Seller> seller = getSellerById(id);
        if(seller.isEmpty()){
            System.out.println("Nenhum seller encontrado");
            return;
        }
        System.out.println(seller.get());
    }

    @Override
    public void findAll() {
        String sql = "SELECT seller.*, department.name AS departmentName FROM seller JOIN department WHERE seller.DepartmentId = department.Id ORDER BY Name";
        List<Seller> sellerList = new ArrayList<>();
        Map<Integer, Department> departments = new HashMap<>();

        try(Statement stmt = connection.createStatement(); ResultSet resultSet = stmt.executeQuery(sql)){
            while(resultSet.next()){
                Department department = null;

                int id = resultSet.getInt("id");
                String name = resultSet.getString("Name");
                String email = resultSet.getString("Email");
                Date birthDate = resultSet.getDate("BirthDate");
                Double baseSalary = Double.parseDouble(resultSet.getString("BaseSalary"));
                if (departments.containsKey(resultSet.getInt("DepartmentId"))) {
                    department = departments.get(resultSet.getInt("DepartmentId"));
                } else {
                    department = new Department(resultSet.getInt("DepartmentId"), resultSet.getString("departmentName"));
                    departments.put(resultSet.getInt("DepartmentId"), department);
                }
                Seller seller = new Seller(id, name, email, birthDate,baseSalary, department);
                sellerList.add(seller);
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        sellerList.forEach(System.out::println);
    }

}
