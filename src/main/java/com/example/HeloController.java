package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.repositories.MyDataRepository;

import javax.annotation.PostConstruct;

@Controller
public class HeloController {
	
	@Autowired
	MyDataRepository repository;
	
	@RequestMapping(value="/edit/{id}",method = RequestMethod.GET)
	public ModelAndView edit(
			@ModelAttribute MyData mydata,
			@PathVariable int id,
			ModelAndView mav) {
		mav.setViewName("edit");
		mav.addObject("title","edit mydata.");
		MyData data = repository.findById((long)id);
		mav.addObject("formModel",data);
		return mav;
	}
	
	@RequestMapping(value="/edit",method = RequestMethod.POST)
	@Transactional(readOnly=false)
	public ModelAndView update(
			@ModelAttribute MyData mydata,
			ModelAndView mav) {
		repository.saveAndFlush(mydata);
		return new ModelAndView("redirect:/");
	}

	@RequestMapping(value="/",method = RequestMethod.GET)
	public ModelAndView index(
			@ModelAttribute("formModel") MyData mydata,
			ModelAndView mav) {
		mav.setViewName("index");
		mav.addObject("msg","this is sample content.");
		Iterable<MyData> list = repository.findAll();
		mav.addObject("datalist",list);
		return mav;
	}
	
	@RequestMapping(value="/",method = RequestMethod.POST)
	@Transactional(readOnly=false)
	public ModelAndView form(
			@ModelAttribute("formModel")
			@Validated MyData mydata,
			BindingResult result,
			ModelAndView mov) {
		ModelAndView res = null;
		if(!result.hasErrors()) {
			repository.saveAndFlush(mydata);
			return new ModelAndView("redirect:/");			
		} else {
			mov.setViewName("index");
			mov.addObject("msg","sorry,error is occured...");
			Iterable<MyData> list = repository.findAll();
			mov.addObject("datalist",list);
			res = mov;
		}
		return res;
	}

	@RequestMapping(value="/delete/{id}",method = RequestMethod.GET)
	public ModelAndView delete(
			@PathVariable int id,
			ModelAndView mav) {
		mav.setViewName("delete");
		mav.addObject("title","delete mydata.");
		MyData data = repository.findById((long)id);
		mav.addObject("formModel",data);
		return mav;
	}
	
	@RequestMapping(value="/delete",method = RequestMethod.POST)
	@Transactional(readOnly=false)
	public ModelAndView remove(
			@RequestParam long id,
			ModelAndView mav) {
		repository.delete(id);
		return new ModelAndView("redirect:/");
	}
	
	@PostConstruct
	public void init() {
		MyData d1 = new MyData();
		d1.setName("yamada taro");
		d1.setAge(88);
		d1.setMail("yamada@taro");
		d1.setMemo("aiueo");
		repository.saveAndFlush(d1);
		MyData d2 = new MyData();
		d2.setName("sato jiro");
		d2.setAge(35);
		d2.setMail("sato@jiro");
		d2.setMemo("momomo");
		repository.saveAndFlush(d2);
		MyData d3 = new MyData();
		d3.setName("ryuubi");
		d3.setAge(24);
		d3.setMail("ryuubi@gentoku");
		d3.setMemo("syoku");
		repository.saveAndFlush(d3);
		
	}
}
