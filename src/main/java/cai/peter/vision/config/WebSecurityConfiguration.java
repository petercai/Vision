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
    /*
     * basic auth
     */
//    PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
//    auth.inMemoryAuthentication()
//        .withUser("peter")
//        .password(encoder.encode("peter"))
//        .roles("USER")
//        .and()
//        .withUser("admin")
//        .password(encoder.encode("admin"))
//        .roles("USER", "ADMIN");
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception { // 2
    http.sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
        .sessionFixation().migrateSession()
        .and()
        .authorizeRequests()
        .antMatchers("/", "/actuator/**", "/rest/user/login")
        .permitAll()
        .anyRequest()
        .authenticated();
//        .and()
//        .httpBasic();
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

  /**
   * 1. A normal Spring @Configuration with the @EnableWebSecurity annotation, extending from
   * WebSecurityConfigurerAdapter.
   *
   * <p>2. By overriding the adapter’s configure(HttpSecurity) method, you get a nice little DSL
   * with which you can configure your FilterChain.
   *
   * <p>3. All requests going to / and /home are allowed (permitted) - the user does not have to
   * authenticate. You are using an antMatcher, which means you could have also used wildcards (*,
   * \*\*, ?) in the string.
   *
   * <p>4. Any other request needs the user to be authenticated first, i.e. the user needs to login.
   *
   * <p>5. You are allowing form login (username/password in a form), with a custom loginPage
   * (/login, i.e. not Spring Security’s auto-generated one). Anyone should be able to access the
   * login page, without having to log in first (permitAll; otherwise we would have a Catch-22!).
   *
   * <p>6. The same goes for the logout page
   *
   * <p>7. On top of that, you are also allowing Basic Auth, i.e. sending in an HTTP Basic Auth
   * Header to authenticate.
   */
/*  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .antMatchers("/", "/home")
        .permitAll() // (3)
        .anyRequest()
        .authenticated() // (4)
        .and()
        .formLogin() // (5)
        .loginPage("/login") // (5)
        .permitAll()
        .and()
        .logout() // (6)
        .permitAll()
        .and()
        .httpBasic(); // (7)
  }*/
}
