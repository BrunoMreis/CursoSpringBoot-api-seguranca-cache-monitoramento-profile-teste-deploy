package br.com.alura.forum;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.com.alura.forum.modelo.Curso;
import br.com.alura.forum.repository.CursoRepository;


@DataJpaTest(showSql = true)
 class ForumApplicationTests {

	@Autowired
	private CursoRepository repository;
	
	@Test
	void  deveCarregarUmcurso() {
		Curso curso = repository.findByNome("HTML 5");
		Assertions.assertNotNull(curso);
		Assertions.assertEquals("HTML 5",curso.getNome());
	}

}
