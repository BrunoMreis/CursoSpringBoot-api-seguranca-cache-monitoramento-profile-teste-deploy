package br.com.alura.forum.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.TopicoRepository;

public class AtualizacaoFormDTO {

	@NotNull
	@NotEmpty
	@Length(min = 5)
	private String titulo;
	@NotNull
	@NotEmpty
	@Length(min = 5)
	private String mensagem;



	public AtualizacaoFormDTO(@NotNull @NotEmpty @Length(min = 5) String titulo,
			@NotNull @NotEmpty @Length(min = 5) String mensagem) {
		super();
		this.titulo = titulo;
		this.mensagem = mensagem;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public Topico atualizar(Long id, TopicoRepository topicoRepository) {
		Topico topico = topicoRepository.getById(id);

		topico.setMensagem(mensagem);
		topico.setTitulo(titulo);
		return topico;
	}

}
