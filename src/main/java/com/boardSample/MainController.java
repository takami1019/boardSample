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

public class MainController {

	@Autowired
	MessageRepository repository;
	
	@PersistenceContext
	EntityManager entityManager;
	
	@GetMapping("/secret")
	public ModelAndView secret(ModelAndView mav,
							@AuthenticationPrincipal UserDetails userDetails,
							@ModelAttribute("formModel") Message message) {
        mav.setViewName("secret");
        mav.addObject("title","サンプル掲示板");
        mav.addObject("formModel",message);
        String username = userDetails.getUsername();
        mav.addObject("user",username +"でログインしています。");
        mav.addObject("flag",username);
        List<Message> list =(List<Message>)repository.findAll();
        mav.addObject("data",list);
        return mav;
    }
	
  @PostMapping("/secret")
  @Transactional
  public ModelAndView msgform(ModelAndView mav,
		  					@AuthenticationPrincipal UserDetails userDetails,
		  					@ModelAttribute("formModel") @Validated Message message,
		  					BindingResult result) {
	  String username = userDetails.getUsername();
	  ModelAndView res=null;
	  if(!result.hasErrors()) {
		  mav.setViewName("secret");
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
  
  @GetMapping("/edit")
	public ModelAndView edit(@RequestParam("id") Long id,
							@ModelAttribute Message message,
							ModelAndView mav) {
		mav.setViewName("edit");
		mav.addObject("title","サンプル掲示板");
		mav.addObject("msg","編集ページです");
		Optional<Message> data=repository.findById(id);
		mav.addObject("formModel",data.get());
		return mav;
  }
  
  @PostMapping("/edit")
  @Transactional
  public ModelAndView update(@ModelAttribute("formModel") @Validated Message message,
		  					ModelAndView mav,
		  					BindingResult result) {
	  message.setDatetime(Calendar.getInstance().getTime());
	  repository.saveAndFlush(message);
	  return new ModelAndView("redirect:/secret");
  }
  
  @GetMapping("/delete")
	public ModelAndView delete(@RequestParam("id") Long id,
							@ModelAttribute Message message,
							ModelAndView mav) {
		mav.setViewName("delete");
		mav.addObject("title","サンプル掲示板");
		mav.addObject("msg","下記を削除します。");
		Optional<Message> data=repository.findById(id);
		mav.addObject("formModel",data.get());
		return mav;
}
  
  @PostMapping("/delete")
  @Transactional
  public ModelAndView remove(@RequestParam Long id,
		  					ModelAndView mav) {
	  repository.deleteById(id);
	  return new ModelAndView("redirect:/secret");
  }
  
  
  @PostConstruct
  public void init() {
	  Message m1 =new Message();
	  m1.setUserName("taro");
	  m1.setContent("サンプルメッセージです");
	  m1.setDatetime(Calendar.getInstance().getTime());
	  repository.saveAndFlush(m1);
  }
 
}
