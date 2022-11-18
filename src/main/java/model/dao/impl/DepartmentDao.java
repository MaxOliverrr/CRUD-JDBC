package model.dao.impl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import model.dao.GenericDao;
import model.entities.Department;
import util.DbException;

import java.sql.*;
import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDao implements GenericDao {
    Connection connection;
    @Override
    public void insert(Object obj) {
        Department department = (Department) obj;
        String sql = "INSERT INTO department (Name) VALUES (?)";

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setString(1, department.getName());

            int rowsAffected = preparedStatement.executeUpdate();

            if(rowsAffected > 0){
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if(resultSet.next()){
                    int id = resultSet.getInt(1);
                    department.setId(id);
                    System.out.println("O departamento " + department.getName() + " foi adicionado com o id " + id);
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
        Department department = (Department) obj;
        String sql = "UPDATE department SET Name = ? WHERE id = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, department.getName());
            preparedStatement.setInt(2, department.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            if(rowsAffected > 0){
                System.out.println("O departamento " + department.getId() + " foi atualizado");
                return;
            }
            System.out.println("Nenhum departamento foi atualizado");

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM department WHERE seller.id = ?";
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

    private Optional<Department> getDeparmentById(Integer id)  {
        String sql = "SELECT * FROM department WHERE department.id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql);){
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return Optional.of(new Department(rs
                        .getInt("id"), rs
                        .getString("Name")));
            }
            rs.close();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public void findById(Integer id) {
        Optional<Department> department = getDeparmentById(id);
        if(department.isEmpty()){
            System.out.println("Nenhum deparment foi encontrado");
            return;
        }
        System.out.println(department.get());
    }

    @Override
    public void findAll() {
        String sql = "SELECT * FROM department";
        List<Department> departmentList = new ArrayList<>();

        try(Statement stmt = connection.createStatement(); ResultSet resultSet = stmt.executeQuery(sql)){
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("Name");
                Department department = new Department(id, name);
                departmentList.add(department);
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        departmentList.forEach(System.out::println);
    }
}
