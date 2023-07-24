package com.boardSample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class SecurityController {
	
	@RequestMapping("/")
	public ModelAndView index(ModelAndView mav) {
		mav.setViewName("index");
		mav.addObject("title","サンプル掲示板");
		mav.addObject("msg","トップページ");
		return mav;
	}
	
	@GetMapping("/register")
	public ModelAndView register(ModelAndView mav) {
		mav.setViewName("register");
		mav.addObject("title","サンプル掲示板");
		mav.addObject("msg","登録ページ");
		return mav;
	}
	
	@PostMapping("/register")
	public ModelAndView commit(@RequestParam("username")String username,
								@RequestParam("password")String password,
								ModelAndView mav) {
		try {
		JdbcUserDetailsManager users =new JdbcUserDetailsManager(this.dataSource);
		users.createUser(makeUser(username,password,"USER"));
		 return new ModelAndView("redirect:/");
		}catch(Exception e) {
			mav.setViewName("register");
			mav.addObject("title","サンプル掲示板");
			mav.addObject("msg","そのユーザIDは既に存在します。別のユーザIDを登録してください");
			return mav;
		}
	}
	

	
	
	@RequestMapping("/login")
	public ModelAndView login(ModelAndView mav,
							@RequestParam(value="error",required=false)String error){
		mav.setViewName("login");
		System.out.println(error);
		if(error !=null) {
			mav.addObject("msg","ログインできませんでした。");
		}else {
			mav.addObject("msg","ユーザ名とパスワードを入力");
		}
		return mav;
	}
	
	
	@Autowired
	private DataSource dataSource;
	
	private UserDetails makeUser(String user,String pass,String role) {
		return User.withUsername(user)
				.password(
						PasswordEncoderFactories
						.createDelegatingPasswordEncoder()
						.encode(pass))
						.roles(role)
						.build();
	}
}
