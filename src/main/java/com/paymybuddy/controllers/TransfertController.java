package com.paymybuddy.controllers;

import com.paymybuddy.dto.ApplicationTransactionDto;
import com.paymybuddy.dto.ConnectedUserDto;
import com.paymybuddy.exceptions.UserNotFoundException;
import com.paymybuddy.model.ApplicationTransaction;
import com.paymybuddy.model.User;
import com.paymybuddy.service.ApplicationTransactionService;
import com.paymybuddy.service.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/transfert")
@Log4j2
public class TransfertController {
  @Autowired
  private UserService userService;

  @Autowired
  private ApplicationTransactionService appTransactionService;

  @Autowired
  private ModelMapper modelMapper;

  /**
   * return view of transfert.
   *
   * @param authentication authentication of connected user.
   * @param model model to send informations for the view
   * @return view of transfert.
   */
  @GetMapping("")
  public String getTransfert(Authentication authentication, Model model) {
    Optional<User> user = userService.findByEmail(authentication.getName());

    if (user.isPresent()) {

      User existedUser = user.get();

      if (existedUser.getBankAccount() != null) {

        // retrieve all connected user for user and create connectedUserDto
        List<User> connectedUsers = userService.findConnectedUserByEmail(existedUser.getEmail());
        List<ConnectedUserDto> connectedUsersDto = new ArrayList<>();
        for (User connectedUser : connectedUsers) {
          connectedUsersDto.add(modelMapper.map(connectedUser, ConnectedUserDto.class));
        }
        model.addAttribute("connectedUsers", connectedUsersDto);

        // creation of TransactionDto to fill form for sendMoneyTo
        if (!model.containsAttribute("transaction")) {
          ApplicationTransactionDto transactionDto = new ApplicationTransactionDto();
          model.addAttribute("transaction", transactionDto);
        }

        // update list of all transactions for the user and display them in table
        List<ApplicationTransaction> userAppTransactions = appTransactionService.findBySender(existedUser);
        List<ApplicationTransactionDto> userTransactionsDto = new ArrayList<>();

        for (ApplicationTransaction userTransaction : userAppTransactions) {
          ApplicationTransactionDto appTransactionDto = modelMapper.map(userTransaction, ApplicationTransactionDto.class);
          appTransactionDto.setSenderEmail(userTransaction.getSender().getEmail());
          appTransactionDto.setReceiverEmail(userTransaction.getReceiver().getEmail());
          userTransactionsDto.add(appTransactionDto);
        }
        model.addAttribute("userTransactions", userTransactionsDto);

        model.addAttribute("userEmail", user.get().getEmail());

        return "transfert";

      } else {
        return "redirect:/profile";
      }

    } else {
      throw new UserNotFoundException("this user is not authenticated!");
    }
  }

  @PostMapping("/connection")
  public String saveConnectionUser(@Valid String email, RedirectAttributes redirectAttrs, Authentication auth) {

    Optional<User> connectedUser = userService.findByEmail(email);
    Optional<User> authenticatedUser = userService.findByEmail(auth.getName());

    if (authenticatedUser.isPresent()) {

      User user = authenticatedUser.get();

      if (connectedUser.isPresent()) {

        User connectionUser = connectedUser.get();
        List<User> connectedUsers = userService.findConnectedUserByEmail(user.getEmail());

        if (connectedUsers.contains(connectionUser)) {

          redirectAttrs.addFlashAttribute("warning", "the user with this email " + email + " already connected with you!");

        } else {

          user.addConnectionUser(connectionUser);
          userService.save(user);
          redirectAttrs.addFlashAttribute("success", "the user with this email " + email + " was registred!");
        }

      } else {
        log.error("Not be able to add connectionUser with email: {} cause of not found in database.", email);
        redirectAttrs.addFlashAttribute("error", "the user with this email " + email + " is not registred in application!");

      }
      return "redirect:/transfert";

    } else {
      throw new UserNotFoundException("the user is not authenticated.");
    }

  }
/**
 * 
 * @param transactionDto
 * @param bindingResult
 * @param authentication
 * @param model
 * @param redirectAttributes
 * @return
 */
  @PostMapping("/sendmoneyto")
  public String sendMoneyTo(@Valid @ModelAttribute(value = "transaction") ApplicationTransactionDto transactionDto, BindingResult bindingResult, Authentication authentication, Model model,
      RedirectAttributes redirectAttributes) {
    
    Optional<User> user = userService.findByEmail(authentication.getName());

    // in case of validation errors for transactionDto
    if (bindingResult.hasErrors()) {

      redirectAttributes.addFlashAttribute("error", "a problem has occured in transaction, please check red fields!");

      // Add bindingResult and ModelAttribute transactionDto to redirectAttribute for redirection
      // see in @GetMapping condition on creation of new transactionDto
      redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.transaction", bindingResult);
      redirectAttributes.addFlashAttribute("transaction", transactionDto);
      return "redirect:/transfert";
    }

    if (user.isPresent() && user.get().getEmail().equalsIgnoreCase(transactionDto.getSenderEmail())) {
      User sender = user.get();

      ApplicationTransaction transaction = modelMapper.map(transactionDto, ApplicationTransaction.class);
      Optional<User> receiverUser = userService.findByEmail(transactionDto.getReceiverEmail());

      if (receiverUser.isPresent()) {
        User receiver = receiverUser.get();

        // proceed transaction beetween sender and receiver
        ApplicationTransaction succeededTransaction = appTransactionService.proceedBetweenUsers(transaction, sender, receiver);
        redirectAttributes.addFlashAttribute("success", "Transaction of " + succeededTransaction.getAmount() + "€ " + "to " + receiver.getFullName() + " was successfull!");

      } else {
        throw new UserNotFoundException("this receiver is not authenticated !");
      }

    } else {
      throw new UserNotFoundException("this sender is not authenticated or user account not corresponds with sender.email");
    }

    return ("redirect:/transfert");
  }
}
