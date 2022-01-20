package com.paymybuddy.controllers;

import javax.annotation.security.RolesAllowed;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ApplicationController {

  @GetMapping("/home")
  @RolesAllowed("USER")
  public String getHome(Authentication authentication, Model model) {
    if (authentication != null && authentication.isAuthenticated()) {
      return "home";
    } else {
      return "/login";
    }
  }
}
