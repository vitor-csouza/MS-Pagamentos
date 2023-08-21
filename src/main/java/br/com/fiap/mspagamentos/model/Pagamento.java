package br.com.fiap.mspagamentos.model;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Entity
@Table(name = "tb_pagamento")
public class Pagamento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "Campo requerido")
	@Positive(message = "O valor deve ser positivo")
	private BigDecimal valor;
	@Size(max = 150, message = "Tamano máximo de campo: 150 caracteres")
	private String nome; //nome
	@Size(max = 20, message = "Tamano máximo de campo: 20 caracteres")
	private String numeroDoCartao; //número
	@Size(max = 7, message = "Tamano máximo de campo: 150 caracteres")
	private String validade; //data de validade
	@Size(max = 3, message = "Tamano máximo de campo: 150 caracteres")
	private String codigo; //cod. segurança
	@NotNull(message = "Campo requerido")
	@Enumerated (EnumType.STRING)
	private Status status;
	@NotNull(message = "Campo requerido")
	@Positive
	private Long pedidoId;
	@NotNull(message = "Campo requerido")
	@Positive
	private Long formaDePagamentoId; // 1 - cartão - 2 - dinheiro

	public Pagamento() {

	}

	public Pagamento(Long id, BigDecimal valor, String nome, String numeroDoCartao, String validade, String codigo,
					 Status status, Long pedidoId, Long formaDePagamentoId) {
		super();
		this.id = id;
		this.valor = valor;
		this.nome = nome;
		this.numeroDoCartao = numeroDoCartao;
		this.validade = validade;
		this.codigo = codigo;
		this.status = status;
		this.pedidoId = pedidoId;
		this.formaDePagamentoId = formaDePagamentoId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNumeroDoCartao() {
		return numeroDoCartao;
	}

	public void setNumeroDoCartao(String numeroDoCartao) {
		this.numeroDoCartao = numeroDoCartao;
	}

	public String getValidade() {
		return validade;
	}

	public void setValidade(String validade) {
		this.validade = validade;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}



	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Long getPedidoId() {
		return pedidoId;
	}

	public void setPedidoId(Long pedidoId) {
		this.pedidoId = pedidoId;
	}

	public Long getFormaDePagamentoId() {
		return formaDePagamentoId;
	}

	public void setFormaDePagamentoId(Long formaDePagamentoId) {
		this.formaDePagamentoId = formaDePagamentoId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pagamento other = (Pagamento) obj;
		return Objects.equals(id, other.id);
	}






}
