package Service;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Account;
import Model.Message;

import java.util.List;

public class MessageService {
    AccountDAO accountDAO;
    MessageDAO messageDAO;

    //Constructor
    public MessageService() {
        accountDAO = new AccountDAO();
        messageDAO = new MessageDAO();
    }

    //Methods
    //Create a new message
    public Message createMessage(Message message) {
        Account accountCheck = accountDAO.getAccountById(message.posted_by);
        Message addedMessage = null;

        //Checks if message_text is not blank, is not over 255 characters, and posted_by refers to a real, existing user.
        if ((message.message_text.length() > 0) && (message.message_text.length() <= 255) && (accountCheck != null)) {
            addedMessage = messageDAO.addMessage(message);
        }

        return addedMessage;
    }

    //Retrieve all messages
    public List<Message> getAllMessages() {
        return messageDAO.retrieveAllMessages();
    }

    //Retrieve message by ID
    public Message getMessageByID(int id) {
        return messageDAO.retrieveMessageById(id);
    }

    //Delete message by ID
    public Message deleteMessageByID(int id) {
        return messageDAO.deleteMessageById(id);
    }

    //Change message by ID
    public Message changeMessageByID(int id, Message message) {
        Message messageCheck = messageDAO.retrieveMessageById(id);

        //Checks if the message id already exists and the new message_text is not blank and is not over 255 characters.
        if ((messageCheck != null) && (message.message_text.length() > 0) && (message.message_text.length() <= 255)) {
            boolean changedMessageCheck = messageDAO.changeMessageTextById(id, message);

            //Checks if messasge was updated and retrieves the updated message information
            if (changedMessageCheck) {
                return messageDAO.retrieveMessageById(id);
            }
            
        }

        return null;
    }
}
