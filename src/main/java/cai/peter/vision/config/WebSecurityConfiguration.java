package cai.peter.vision.config;

import cai.peter.vision.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/*
Spring security:
  https://www.marcobehler.com/guides/spring-security
 */

@Configuration
@EnableWebSecurity(debug = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Autowired
  private UserService userService;


  @Bean
  public BCryptPasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder();
  }

  @Bean
  public DaoAuthenticationProvider daoAuthenticationProvider(){
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userService);
    provider.setPasswordEncoder(passwordEncoder());
    return provider;
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(daoAuthenticationProvider());

  }

  @Override
  protected void configure(HttpSecurity http) throws Exception { // 2
    http.sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
//        .sessionFixation()
//        .migrateSession()
        .and().csrf().disable() //Cross Site Request Forgery
        .formLogin()// https://www.baeldung.com/spring-security-login
        //        .loginPage("/login.html")
//        .loginProcessingUrl("/login")
        //      .defaultSuccessUrl("/home")
        .and()
        .logout()  //https://www.baeldung.com/spring-security-logout
//        .logoutUrl("/logout")
//        .logoutSuccessUrl("/")
        .invalidateHttpSession(true)
        .deleteCookies("JSESSIONID")
        .and()
        .authorizeRequests()
        .antMatchers("/", "/actuator/**", "/rest/user/login")
        .permitAll()
        .anyRequest()
        .authenticated();
}

  /**
   * 2. stateless (no session)
   * @param http
   * @throws Exception
   */
  /*protected void configure(HttpSecurity http) throws Exception {
    http
      // deactivate session creation
      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      .and().csrf().disable()

      // store SecurityContext in Cookie / delete Cookie on logout
      .securityContext().securityContextRepository(cookieSecurityContextRepository)
      .and().logout().permitAll().deleteCookies(SignedUserInfoCookie.NAME)

      // deactivate RequestCache and append originally requested URL as query parameter to login form request
      .and().requestCache().disable()
      .exceptionHandling().authenticationEntryPoint(loginWithTargetUrlAuthenticationEntryPoint)

      // configure form-based login
      .and().formLogin()
      .loginPage(LOGIN_FORM_URL)
      // after successful login forward user to originally requested URL
      .successHandler(redirectToOriginalUrlAuthenticationSuccessHandler)

      .and().authorizeRequests()
      .antMatchers(LOGIN_FORM_URL).permitAll()
      .antMatchers("/**").authenticated();

  }*/


}
