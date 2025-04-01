package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    AccountDAO accountDAO;

    //constructors
    public AccountService() {
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    //methods
    public Account registerAccount(Account account) {
        Account registeredAcc = null;
        Account accountCheck = accountDAO.getAccountByUsername(account.username);
       
        //checks if username is not blank, password is at least 4 characters long, and an Account with that username does not already exist
        if ((account.username.length() > 0) && (account.password.length() >= 4) && (accountCheck == null)) {
            registeredAcc = accountDAO.insertAccount(account);
        }

        return registeredAcc;
    }
}
