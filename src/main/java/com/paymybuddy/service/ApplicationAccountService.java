package com.paymybuddy.service;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import com.paymybuddy.UtilService;
import com.paymybuddy.exceptions.ApplicationAccountException;
import com.paymybuddy.exceptions.UserNotFoundException;
import com.paymybuddy.model.ApplicationAccount;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.ApplicationAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicationAccountService {

  @Autowired
  private ApplicationAccountRepository applicationAccountRepository;

  @Autowired
  private UtilService utilService;


  /**
   * retrieve application account of a user by its id.
   * 
   * @param id id of application account
   * @return application account if it exists
   */
  public Optional<ApplicationAccount> findById(Long id) {
    return applicationAccountRepository.findById(id);
  }

  /**
   * retrieve user's application account by his email
   * 
   * @param email email of owner of applicationAccount
   * @return application account if it exists
   */
  public Optional<ApplicationAccount> findByEmail(String email) {
    return applicationAccountRepository.findByEmail(email);
  }

  /**
   * save a ApplicationAccount.
   * 
   * @param appAccount
   * @return the application account saved.
   */
  public ApplicationAccount save(ApplicationAccount appAccount) {
    return applicationAccountRepository.save(appAccount);
  }

  /**
   * create a new application account of a user registred in db.
   * 
   * @param user the new user
   * @return application account initialized.
   * @throws NoSuchAlgorithmException
   */
  public ApplicationAccount createAccountforUser(User user) throws NoSuchAlgorithmException {

    if (user != null) {
      if (user.getApplicationAccount() == null) {
        ApplicationAccount appAccountOfUser = new ApplicationAccount();
        appAccountOfUser.setAccountNumber(utilService.getRandomApplicationAccountNumber());
        appAccountOfUser.setBalance(0d);
        user.setApplicationAccount(appAccountOfUser);
        return appAccountOfUser;
      } else {
        throw new ApplicationAccountException("this user " + user.getFullName() + "has already an application account.");

      }
    } else {
      throw new UserNotFoundException("For creation of application account,the user doesn't exist");
    }
  }

  /**
   * Withdraw a amount on applicationAccount (commission included) and save it in bdd.
   * 
   * @param applicationAccount the application account of sender of amount
   * @param amount the amount of transaction ( commission included)
   * @throws ApplicationAccountException if amount is greater than balance of account.
   */
  public void withdraw(ApplicationAccount senderApplicationAccount, double amount) {
    if (senderApplicationAccount.getBalance() >= amount) {
      senderApplicationAccount.setBalance(senderApplicationAccount.getBalance() - amount);
      this.save(senderApplicationAccount);
    } else {
      throw new ApplicationAccountException("You can't send this amount (commision included)" + amount + " to your friend because your balance is not sufficient");
    }
  }

  /**
   * credit application account with the amount in parameter and save it in bdd.
   * 
   * @param receveiverApplicationAccount application account of receiver of amount
   * @param amount amount to credit
   */
  public void credit(ApplicationAccount receiverApplicationAccount, double amount) {
    receiverApplicationAccount.setBalance(receiverApplicationAccount.getBalance() + amount);
    this.save(receiverApplicationAccount);
  }
}
