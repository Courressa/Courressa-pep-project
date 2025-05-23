package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    //Constructor
    public SocialMediaController() {
        accountService = new AccountService();
        messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);

        app.post("/register", this::postAccountHandler);
        app.post("/login", this::postLoggedInAccountHandler);
        app.post("/messages", this::postCreateMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::getDeleteMessageByIdHandler);
        app.patch("/messages/{message_id}", this::getChangeMessageByIdHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByPosterIdHandler);
        
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    //Handler to post a new account
    private void postAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        Account account = om.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.registerAccount(account);

        if (addedAccount != null) {
            ctx.json(om.writeValueAsString(addedAccount));
        } else {
            ctx.status(400);
        }
    }

    //Handler to verify user login and return account
    private void postLoggedInAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        Account account = om.readValue(ctx.body(), Account.class);
        Account userAccount = accountService.verifyUser(account);
        
        if (userAccount != null) {
            ctx.json(om.writeValueAsString(userAccount)).status(200);
        } else {
            ctx.status(401);
        }
    }

    //Handler to create a new message
    private void postCreateMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        Message message = om.readValue(ctx.body(), Message.class);
        Message addedMessage = messageService.createMessage(message);
        
        if (addedMessage != null) {
            ctx.json(om.writeValueAsString(addedMessage)).status(200);
        } else {
            ctx.status(400);
        }
    }

    //Handler to get all messages
    private void getAllMessagesHandler(Context ctx) {
        List<Message> allMessages = messageService.getAllMessages();
        ctx.json(allMessages).status(200);
    }

    //Handler to get message by ID
    private void getMessageByIdHandler(Context ctx) {
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message retrievedMessage = messageService.getMessageByID(message_id);

        if (retrievedMessage != null) {
            ctx.json(retrievedMessage).status(200);
        } else {
            ctx.json("").status(200);
        }
    }

    //Handler to delete message by ID
    private void getDeleteMessageByIdHandler(Context ctx) {
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message deletedMessage = messageService.getMessageByID(message_id);
        
        if (deletedMessage != null) {
            ctx.json(deletedMessage).status(200);
        } else {
            ctx.json("").status(200);
        }
    }

    //Handler to change message text by ID
    private void getChangeMessageByIdHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        Message message = om.readValue(ctx.body(), Message.class);
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message changeMessageText = messageService.changeMessageByID(message_id, message);
        
        if (changeMessageText != null) {
            ctx.json(om.writeValueAsString(changeMessageText)).status(200);
        } else {
            ctx.status(400);
        }
    }

    //Handler to get all messages by poster ID
    private void getAllMessagesByPosterIdHandler(Context ctx) {
        int posterId = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> allMessages = messageService.getAllMessagesByPosterId(posterId);
        ctx.json(allMessages).status(200);
    }
}