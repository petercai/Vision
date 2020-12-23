package cai.peter.vision.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/*
Spring security:
  https://www.marcobehler.com/guides/spring-security
 */

@Configuration
@EnableWebSecurity(debug = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    /*
     * basic authentication
     */
    PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    auth.inMemoryAuthentication()
        .withUser("peter")
        .password(encoder.encode("peter"))
        .roles("USER")
        .and()
        .withUser("admin")
        .password(encoder.encode("admin"))
        .roles("USER", "ADMIN");
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception { // 2
        // basic loging
        http.authorizeRequests()
          .antMatchers("/", "/actuator/**", "/rest/**")
          .permitAll()
          .anyRequest().authenticated().and().httpBasic();

//    http.authorizeRequests()
//        .antMatchers("/", "/home")
//        .permitAll() // (3)
//        .anyRequest()
//        .authenticated() // (4)
//        .and()
//        .formLogin() // (5)
//        .loginPage("/login") // (5)
//        .permitAll()
//        .and()
//        .logout() // (6)
//        .permitAll()
//        .and()
//        .httpBasic(); // (7)

    /*
    1. A normal Spring @Configuration with the @EnableWebSecurity annotation, extending from WebSecurityConfigurerAdapter.
    2. By overriding the adapter’s configure(HttpSecurity) method, you get a nice little DSL with which you can configure your FilterChain.
    3. All requests going to / and /home are allowed (permitted) - the user does not have to authenticate.
        You are using an antMatcher, which means you could have also used wildcards (*, \*\*, ?) in the string.
    4. Any other request needs the user to be authenticated first, i.e. the user needs to login.
    5. You are allowing form login (username/password in a form), with a custom loginPage (/login, i.e. not Spring Security’s auto-generated one).
        Anyone should be able to access the login page, without having to log in first (permitAll; otherwise we would have a Catch-22!).
    6. The same goes for the logout page
    7. On top of that, you are also allowing Basic Auth, i.e. sending in an HTTP Basic Auth Header to authenticate.
     */
  }
}
