package com.paymybuddy.security.configuration;

import com.paymybuddy.security.services.UserDetailsServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
public class PayMyBuddySecurityConfig extends WebSecurityConfigurerAdapter {

  private static final String HOME = "/home";

  @Autowired
  private UserDetailsServiceImpl userDetailsService;

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {

    auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http.authorizeRequests().antMatchers("/css/**").permitAll() // allowed to access to mycss.css
        .antMatchers(HOME).hasRole("USER")
        .antMatchers("/registration").permitAll()
        .antMatchers("/loginPage", "/login").permitAll()
        .anyRequest().authenticated()
        // .and()
        // .formLogin()
        // .loginPage("/loginPage")
        // .loginProcessingUrl("/login")
        // .defaultSuccessUrl(HOME)
        // .permitAll()
        .and()
        .oauth2Login()
        .loginPage("/loginPage")
        .defaultSuccessUrl(HOME)
        .failureHandler(new SimpleUrlAuthenticationFailureHandler("/login?error"))
        .permitAll()
        .and()
        .logout()
        .deleteCookies("JSESSIONID")
        .permitAll()
        .and()
        .rememberMe()
        .key("uniqueAndSecret");
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }
}
