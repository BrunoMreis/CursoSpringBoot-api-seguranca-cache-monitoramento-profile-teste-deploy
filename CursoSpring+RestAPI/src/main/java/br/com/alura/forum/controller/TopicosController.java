package br.com.alura.forum.controller;

import java.net.URI;
import java.util.Optional;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.forum.controller.dto.AtualizacaoFormDTO;
import br.com.alura.forum.controller.dto.DetalhesTopicoDTO;
import br.com.alura.forum.controller.dto.TopicoDto;
import br.com.alura.forum.controller.form.TopicoForm;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;

@RestController
@RequestMapping("/topicos")
public class TopicosController {

	@Autowired
	private TopicoRepository topicoRepository;

	@Autowired
	private CursoRepository cursoRepository;

	@GetMapping
	@Cacheable(value = "listaDeTopicos")
	public Page<TopicoDto> lista(@RequestParam(required = false) String nomeCurso, @RequestParam(required = false) Integer page,
			@PageableDefault(sort = "id", direction = Direction.DESC, page = 0, size = 10 ) Pageable paginacao /*
																							 * @RequestParam int
																							 * quantidadeDePaginas, @RequestParam
																							 * String ordenacao
																							 */) {

//		Pageable paginacao = PageRequest.of(pagina, quantidadeDePaginas, Direction.DESC, ordenacao);

		if (nomeCurso == null) {

			Page<Topico> topicos = topicoRepository.findAll(paginacao);
			return TopicoDto.converter(topicos);
		} else {
			Page<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso, paginacao);
			return TopicoDto.converter(topicos);
		}
	}

	@PostMapping
	@Transactional
	public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder) {
		Topico topico = form.converter(cursoRepository);
		topicoRepository.save(topico);

		URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
		return ResponseEntity.created(uri).body(new TopicoDto(topico));
	}

	@GetMapping("/{id}")
	public ResponseEntity<DetalhesTopicoDTO> detalharTopico(@PathVariable Long id) {
		Optional<Topico> optionalTopico = topicoRepository.findById(id);
		if (optionalTopico.isPresent()) {
			return ResponseEntity.ok(new DetalhesTopicoDTO(optionalTopico.get()));
		}
		return ResponseEntity.badRequest().build();
	}

	@PutMapping("/{id}")
	@Transactional
	@CacheEvict(value ="listaDeTopicos", allEntries = true )
	public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizacaoFormDTO form) {
		Optional<Topico> optionalTopico = topicoRepository.findById(id);
		if (optionalTopico.isPresent()) {

			Topico topico = form.atualizar(id, topicoRepository);

			return ResponseEntity.ok(new TopicoDto(topico));
		}
		return ResponseEntity.badRequest().build();

	}

	@DeleteMapping("/{id}")
	@Transactional
	@CacheEvict(value ="listaDeTopicos", allEntries = true )
	public ResponseEntity<?> deletar(@PathVariable Long id) {
		Optional<Topico> optionalTopico = topicoRepository.findById(id);
		if (optionalTopico.isPresent()) {

			topicoRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.badRequest().build();
	}

}
