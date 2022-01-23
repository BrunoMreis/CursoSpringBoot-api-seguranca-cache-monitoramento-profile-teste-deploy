package br.com.alura.forum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.alura.forum.repository.UsuarioRepository;

@Controller
public class HelloController {
	
	@Autowired
	private UsuarioRepository repository;
	
	
	@RequestMapping("/")
	@ResponseBody
	public String hello() {
		
		System.out.println(repository.findAll());
		return repository.findAll().toString();
	}

}
