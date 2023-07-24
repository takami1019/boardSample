package com.boardSample;

import java.util.List;
import java.util.Optional;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import javax.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Controller

public class AdminController {

	@Autowired
	MessageRepository repository;
	
	@Autowired
	UserRepository repository2;
	
	@PersistenceContext
	EntityManager entityManager;
	
	@GetMapping("/admin")
	public ModelAndView secret(ModelAndView mav,
							@AuthenticationPrincipal UserDetails userDetails,
							@ModelAttribute("formModel") Message message) {
        mav.setViewName("admin");
        mav.addObject("title","サンプル掲示板 管理者ページ");
        mav.addObject("formModel",message);
        String username = userDetails.getUsername();
        mav.addObject("user",username +"でログインしています。");
        mav.addObject("flag",username);
        List<Message> list =(List<Message>)repository.findAll();
        mav.addObject("data",list);
        return mav;
    }
	
  @PostMapping("/admin")
  @Transactional
  public ModelAndView msgform(ModelAndView mav,
		  					@AuthenticationPrincipal UserDetails userDetails,
		  					@ModelAttribute("formModel") @Validated Message message,
		  					BindingResult result) {
	  String username = userDetails.getUsername();
	  ModelAndView res=null;
	  if(!result.hasErrors()) {
		  mav.setViewName("admin");
		  message.setDatetime(Calendar.getInstance().getTime());
		  message.setUserName(username);
		  repository.saveAndFlush(message);
		  mav.addObject("msg","新しいメッセージを受け付けました");
		  mav.addObject("title","Message");
		  res= new ModelAndView("redirect:/secret");
	  }else {
		  mav.setViewName("secret");
		  mav.addObject("msg","投稿エラー");
		  mav.addObject("title","サンプル掲示板");
		  List<Message> list =(List<Message>)repository.findAll();
		  mav.addObject("data",list);
		  res =mav;
  		}
	  return res;
  }
  

  @GetMapping("/user")
	public ModelAndView user(ModelAndView mav,
							@AuthenticationPrincipal UserDetails userDetails,
							@ModelAttribute("formModel") User user) {
      mav.setViewName("user");
      mav.addObject("msg","楽しい掲示板 管理者ページ");
      mav.addObject("formModel",user);
      String username = userDetails.getUsername();
      mav.addObject("user",username +"でログインしています。");
      mav.addObject("flag",username);
      List<User> list =(List<User>)repository2.findAll();
      mav.addObject("userdata",list);
      return mav;
  }
  
  @GetMapping("/userDelete")
	public ModelAndView userDelete(@RequestParam("userId") String userId,
							@ModelAttribute User user,
							ModelAndView mav) {
		mav.setViewName("userDelete");
		mav.addObject("title","サンプル掲示板 ユーザ管理");
		mav.addObject("msg","下記ユーザを削除します。");
		Optional<User> data=repository2.findById(userId);
		mav.addObject("formModel",data.get());
		return mav;
}

@PostMapping("/userDelete")
@Transactional
public ModelAndView remove(@RequestParam String userId,
		  					ModelAndView mav) {
	  repository2.deleteById(userId);
	  return new ModelAndView("redirect:/user");
}

  
 
}
