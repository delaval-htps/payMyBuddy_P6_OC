package com.paymybuddy.controllers;

import java.util.Optional;
import javax.validation.Valid;
import com.paymybuddy.dto.UserDto;
import com.paymybuddy.model.User;
import com.paymybuddy.security.oauth2.user.CustomOAuth2User;
import com.paymybuddy.service.OAuth2ProviderService;
import com.paymybuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import lombok.extern.log4j.Log4j2;

@Controller
@Log4j2
public class RegistrationController {

  @Autowired
  private UserService userService;

  @Autowired
  private OAuth2ProviderService oAuth2ProviderService;

  @Autowired
  PasswordEncoder passwordEncoder;

  @GetMapping("/registration")
  public String registerNewUser(Model model,
      org.springframework.security.core.Authentication authentication) {

    UserDto userDto = new UserDto();

    if (authentication.getPrincipal() instanceof CustomOAuth2User) {
      CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
      userDto.setEmail(oAuth2User.getEmail());
      userDto.setLastName(oAuth2User.getLastName());
      userDto.setFirstName(oAuth2User.getFirstName());
    }
    
    model.addAttribute("user", userDto);
    return "registration";
  }

  /**
   * save a user just registrated with form registration.
   * 
   * @param model model to pass pojo with information about user
   * @param userDto informations of user form the form registration
   * @param bindingResult list of errors if problem of validation
   * @return page registration if there is a validation's error or home with sucessfull
   *         authentication.
   */
  @PostMapping("/registration")
  public String saveNewUser(Model model, @Valid @ModelAttribute(value = "user") UserDto userDto,
      BindingResult bindingResult, Authentication authentication) {

    if (bindingResult.hasErrors()) {
      return "registration";
    }

    Optional<User> existedUser = userService.findByEmail(userDto.getEmail());

    if (!existedUser.isPresent()) {
      User newUser = new User();
      newUser.setLastName(userDto.getLastName());
      newUser.setFirstName(userDto.getFirstName());
      newUser.setAddress(userDto.getAddress());
      newUser.setPhone(userDto.getPhone());
      newUser.setZip(userDto.getZip());
      newUser.setCity(userDto.getCity());
      newUser.setEmail(userDto.getEmail());
      newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
      newUser.setEnabled((byte) 1);
      User saveUser = userService.save(newUser);
      model.addAttribute("user", newUser);

      // case of new user but logged with OAuth2login()
      if (authentication.getPrincipal() instanceof CustomOAuth2User) {
        oAuth2ProviderService
            .saveOAuth2ProviderForUser((CustomOAuth2User) authentication.getPrincipal(), saveUser);
      }
      
    } else {
      bindingResult.addError(new FieldError("user", "duplicatedUser",
          "Please chose another names and email because they already use by another user!"));

      log.error("user {} {} already existed in database.", userDto.getLastName(),
          userDto.getFirstName());
      return "registration";
    }
    
    return "redirect:/home";
  }
}
