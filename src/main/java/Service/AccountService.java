package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    AccountDAO accountDAO;

    //Constructors
    public AccountService() {
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    //Methods

    //Creates account
    public Account registerAccount(Account account) {
        Account registeredAcc = null;
        Account accountCheck = accountDAO.getAccountByUsername(account.username);
       
        //Checks if username is not blank, password is at least 4 characters long, and an Account with that username does not already exist
        if ((account.username.length() > 0) && (account.password.length() >= 4) && (accountCheck == null)) {
            registeredAcc = accountDAO.insertAccount(account);
        }

        return registeredAcc;
    }

    //Verify user login
    public Account verifyUser(Account account) {
        Account accountCheck = accountDAO.getAccountByUsername(account.username);
        
        //If username exists, it compares the received password to the accountCheck password
        if (accountCheck != null) {
            if (account.password.equals(accountCheck.password)) {
                return accountCheck;
            }
        }

        return null;
    }
}
