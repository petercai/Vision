package com.peppermint.vision.rest.api;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.peppermint.vision.rest.dto.User;
import com.peppermint.vision.rest.dto.UserModel;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

@SessionAttributes("currentUser")
@RestController
@RequestMapping("/rest/user")
@Slf4j
public class UserController {




  /**
   * Spring Boot which uses Spring Security internally provides a SecurityContextHolder class which
   * allows the lookup of the currently authenticated user via:
	 *
	 * <p>Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	 *
	 * <p>The authentication instance now provides the following methods:
   *
   * <p>Get the username of the logged in user: getPrincipal()
	 * <p>Get the password of the authenticated user: getCredentials()
	 * <p>Get the assigned roles of the authenticated user: getAuthorities()
	 * <p>Get further details of the authenticated user: getDetails()
   *
   * <p>Since Spring Security 3.2 you can get currently logged in user (your implementation of
   * UserDetails) by adding a parameter inside your controller method: void
   * method(@AuthenticationPrincipal User user)
   *
   * @param user
   * @return
   */
  @GetMapping("/profile")
  public ResponseEntity<UserModel> get(@AuthenticationPrincipal User user) {
		UserModel userModel = new UserModel();
		userModel.setId(user.getId());
		userModel.setName(user.getName());
		userModel.setEmail(user.getEmail());
		userModel.setEnabled(!user.isDisabled());
		userModel.setApiKey(user.getApiKey());
//		for (UserRole role : userRoleDAO.findAll(user)) {
//			if (role.getRole() == Role.ADMIN) {
//				userModel.setAdmin(true);
//			}
//		}
    return new ResponseEntity<>(userModel, HttpStatus.OK);
	}



	@GetMapping(value = "/logout")
	public String logout_user(HttpSession session)
	{
		session.removeAttribute("username");
		session.invalidate();
		return "redirect:/login";
	}

	@PostMapping("/login")
	public String login_user(@RequestParam("username") String username,@RequestParam("password") String password,
		HttpSession session, ModelMap modelMap)
	{

		User auser= null;

		if(auser!=null)
		{
//			String uname=auser.getUser_email();
//			String upass=auser.getUser_pass();

//			if(username.equalsIgnoreCase(uname) && password.equalsIgnoreCase(upass))
//			{
//				session.setAttribute("username",username);
//				return "dummy";
//			}
//			else
			{
				modelMap.put("error", "Invalid Account");
				return "login";
			}
		}
		else
		{
			modelMap.put("error", "Invalid Account");
			return "login";
		}

	}
}
