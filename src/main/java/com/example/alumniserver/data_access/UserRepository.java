package com.example.alumniserver.data_access;

import com.example.alumniserver.models.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class UserRepository {

    private String URL = ConnectionHelper.jdbcUrl;
    private Connection conn = null;


    public ArrayList<User> getAllUsers(){
        ArrayList<User> customers = new ArrayList<>();
        try{
            // Connect to DB
            conn = DriverManager.getConnection(URL);
            System.out.println("Connection to SQLite has been established.");

            // Make SQL query
            PreparedStatement preparedStatement =
                    conn.prepareStatement("SELECT user_Id, name, picture, status, bio, fun_fact FROM user");
            // Execute Query
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                customers.add(
                        new User(
                                resultSet.getInt("user_Id"),
                                resultSet.getString("name"),
                                resultSet.getString("picture"),
                                resultSet.getString("status"),
                                resultSet.getString("bio"),
                                resultSet.getString("fun_fact")
                        ));
            }
            System.out.println("Select all customers successful");
        }
        catch (Exception exception){
            System.out.println(exception.toString());
        }
        finally {
            try {
                conn.close();
            }
            catch (Exception exception){
                System.out.println(exception.toString());
            }
        }
        return customers;
    }

    public User getUserById(int user_Id){
        User user = null;
        try{
            // Connect to DB
            conn = DriverManager.getConnection(URL);
            System.out.println("Connection to SQLite has been established.");

            // Make SQL query
            PreparedStatement preparedStatement =
                    conn.prepareStatement("SELECT user_Id, name, picture, status, bio, fun_fact FROM user WHERE user_Id = ?");
            preparedStatement.setInt(1,user_Id);
            // Execute Query
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                user = new User(
                        resultSet.getInt("user_Id"),
                        resultSet.getString("name"),
                        resultSet.getString("picture"),
                        resultSet.getString("status"),
                        resultSet.getString("bio"),
                        resultSet.getString("fun_fact")
                );
            }
            System.out.println("Select specific customer successful");
        }
        catch (Exception exception){
            System.out.println(exception.toString());
        }
        finally {
            try {
                conn.close();
            }
            catch (Exception exception){
                System.out.println(exception.toString());
            }
        }
        return user;
    }

    public Boolean addUser(User user){
        Boolean success = false;
        try{
            // Connect to DB
            conn = DriverManager.getConnection(URL);
            System.out.println("Connection to SQLite has been established.");
            // Make SQL query
            PreparedStatement preparedStatement =
                    conn.prepareStatement("INSERT INTO User(user_Id,name,picture,status,bio,fun_fact) VALUES(?,?,?,?,?,?)");
            preparedStatement.setInt(1,user.getUser_Id());
            preparedStatement.setString(2,user.getName());
            preparedStatement.setString(3,user.getPicture());
            preparedStatement.setString(4,user.getStatus());
            preparedStatement.setString(5,user.getBio());
            preparedStatement.setString(6,user.getFun_fact());
            // Execute Query
            int result = preparedStatement.executeUpdate();
            success = (result != 0);
            System.out.println("Add customer successful");
        }
        catch (Exception exception){
            System.out.println(exception.toString());
        }
        finally {
            try {
                conn.close();
            }
            catch (Exception exception){
                System.out.println(exception.toString());
            }
        }
        return success;
    }

    public boolean updateUser(User user)
    {
        boolean success = false;

        try{
            // Connect to the database
            conn = DriverManager.getConnection(URL);
            System.out.println("Connection to SQLite has been established.");

            // create query
            PreparedStatement preparedStatement =
                    conn.prepareStatement("UPDATE User SET name = ?, picture = ?, status = ?, bio = ?, fun_fact = ? WHERE CustomerId = ?");

            preparedStatement.setString(1,user.getName());
            preparedStatement.setString(2,user.getPicture());
            preparedStatement.setString(3,user.getStatus());
            preparedStatement.setString(4,user.getBio());
            preparedStatement.setString(5,user.getFun_fact());
            preparedStatement.setInt(1,user.getUser_Id());


            // Execute the Query
            ResultSet resultSet = preparedStatement.executeQuery();
            success = true;

            System.out.println("Updated specific customer successful");
        }
        catch (Exception exception){
            System.out.println(exception.toString());
        }
        finally {
            try {
                conn.close();
            }
            catch (Exception exception){
                System.out.println(exception.toString());
            }
        }

        return success;
    }
}
