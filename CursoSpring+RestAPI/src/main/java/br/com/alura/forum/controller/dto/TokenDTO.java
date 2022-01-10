package br.com.alura.forum.controller.dto;

public class TokenDTO {

	private String token;
	private String tipo;

	public TokenDTO(String token, String tipoAutenticacao) {
		this.token = token;
		this.tipo = tipoAutenticacao;
	}

	public String getToken() {
		return token;
	}

	public String getTipoAutenticacao() {
		return tipo;
	}

	
	

}
