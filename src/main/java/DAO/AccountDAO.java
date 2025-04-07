package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;


public class AccountDAO {
    //Inserts user account into the account table
    public Account insertAccount(Account account) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "INSERT INTO account (username, password) VALUES (?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            preparedStatement.executeUpdate();
            ResultSet psKeyResultSet = preparedStatement.getGeneratedKeys();

            if (psKeyResultSet.next()) {
                int generated_account_id = psKeyResultSet.getInt(1);
                return new Account(generated_account_id, account.getUsername(), account.getPassword());
            }
            
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        //Returns null if unable to insert account
        return null;
    }

    //Gets user by username in the account table
    public Account getAccountByUsername(String username) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT account_id, username, password FROM account WHERE username = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, username);
            
            ResultSet psResult = preparedStatement.executeQuery();

            while(psResult.next()) {
                Account account = new Account (
                    psResult.getInt("account_id"),
                    psResult.getString("username"),
                    psResult.getString("password")
                );

                return account;
            }

            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        //Returns null if username does not exist
        return null;
    }

    //Gets user by id in the account table
    public Account getAccountById(int id) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT account_id, username, password FROM account WHERE account_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);
            
            ResultSet psResult = preparedStatement.executeQuery();

            while(psResult.next()) {
                Account account = new Account (
                    psResult.getInt("account_id"),
                    psResult.getString("username"),
                    psResult.getString("password")
                );

                return account;
            }

            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        //Returns null if id does not exist
        return null;
    }
}
