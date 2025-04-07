package Service;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Account;
import Model.Message;

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
}
