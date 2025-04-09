package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class MessageDAO {
    //Create new messages in the message table
    public Message addMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            preparedStatement.executeUpdate();
            ResultSet psKeyResultSet = preparedStatement.getGeneratedKeys();

            if (psKeyResultSet.next()) {
                int generated_message_id = psKeyResultSet.getInt(1);
                return new Message(generated_message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }

            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        //Returns null if unable to create a new message
        return null;
    }

    //Get all messages in the message table
    public List<Message> retrieveAllMessages() {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> allMessages = new ArrayList<>();

        try {
            String sql = "SELECT * FROM message;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet psResult = preparedStatement.executeQuery();

            while (psResult.next()) {
                Message message = new Message(
                    psResult.getInt("message_id"),
                    psResult.getInt("posted_by"),
                    psResult.getString("message_text"),
                    psResult.getLong("time_posted_epoch")
                );

                allMessages.add(message);
            }
            
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return allMessages;
    }

    //Get message by ID
    public Message retrieveMessageById(int id) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * FROM message WHERE message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);
            
            ResultSet psResult = preparedStatement.executeQuery();

            while (psResult.next()) {
                Message message = new Message(
                    psResult.getInt("message_id"),
                    psResult.getInt("posted_by"),
                    psResult.getString("message_text"),
                    psResult.getLong("time_posted_epoch")
                );
                
                return message;
            }
            
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return null;
    }

    //Delete message by ID
    public Message deleteMessageById(int id) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "DELETE FROM message WHERE message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, id);
            
            preparedStatement.executeUpdate();
            ResultSet psKeyResultSet = preparedStatement.getGeneratedKeys();

            if (psKeyResultSet.next()) {
                int generated_message_id = psKeyResultSet.getInt(1);
                int generated_posted_by = psKeyResultSet.getInt("posted_by");
                String generated_message_text = psKeyResultSet.getString("message_text");
                Long generated_time_posted = psKeyResultSet.getLong("time_posted_epoch");
                
                return new Message(generated_message_id, generated_posted_by, generated_message_text, generated_time_posted);
            }
            
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return null;
    }

    //Patch message by ID
    public boolean changeMessageTextById(int id, Message message) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, message.getMessage_text());
            preparedStatement.setInt(2, id);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                return true;
            }
            
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return false;
    }
}
