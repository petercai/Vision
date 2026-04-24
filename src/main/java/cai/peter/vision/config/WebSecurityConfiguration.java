package cai.peter.vision.config;

import cai.peter.vision.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/*
Spring security:
  https://www.marcobehler.com/guides/spring-security
 */

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class WebSecurityConfiguration {

  private final UserService userService;


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

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
        .csrf(AbstractHttpConfigurer::disable)
        .formLogin(Customizer.withDefaults())
        .logout(logout -> logout.invalidateHttpSession(true).deleteCookies("JSESSIONID"))
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers("/", "/actuator/**", "/rest/user/login")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .authenticationProvider(daoAuthenticationProvider());
    return http.build();
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
