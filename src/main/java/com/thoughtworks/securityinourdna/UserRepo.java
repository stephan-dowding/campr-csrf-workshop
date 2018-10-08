package com.thoughtworks.securityinourdna;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepo {

    private final Connection connection;

    public UserRepo(Connection connection) {
        this.connection = connection;
    }

    public void addName(String username, String password) throws Exception {
        final String query = "insert into users values (?, ?)";
        final PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, username);
        stmt.setString(2, password);
        stmt.execute();
    }

    public List<String> allUsers() throws SQLException {
        List<String> vendors = new ArrayList<>();
        final String query = "select username from users";
        final ResultSet resultSet = connection.prepareStatement(query).executeQuery();
        while(resultSet.next()){
            vendors.add(resultSet.getString("username"));
        }
        return vendors;
    }

    public String login(String username, String password) {
        try {
            final String query = "select * from users where username = ? and password = ?";

            final PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);

            final ResultSet resultSet;
            resultSet = stmt.executeQuery();
            resultSet.next();
            return resultSet.getString("username");
        } catch (SQLException e) {
            throw new InvalidCredentials();
        }
    }

    public void delete(String name) throws SQLException {
        final String query = "delete from users where username = ?";
        final PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, name);
        stmt.execute();
    }
}
