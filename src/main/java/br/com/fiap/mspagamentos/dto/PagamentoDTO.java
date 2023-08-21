package br.com.fiap.mspagamentos.dto;

import br.com.fiap.mspagamentos.model.Pagamento;
import br.com.fiap.mspagamentos.model.Status;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class PagamentoDTO {

    private Long id;

    @NotNull(message = "Campo requerido")
    @Positive(message = "O valor deve ser um número positivo")
    private BigDecimal valor;
    private String nome; //nome
    private String numeroDoCartao; //número
    private String validade; //data de validade
    private String codigo; //cod. segurança
    private Status status;


    @NotNull(message = "Campo requerido")
    @Positive
    private Long pedidoId;


    @NotNull(message = "Campo requerido")
    @Positive
    private Long formaDePagamentoId; // 1 - cartão - 2 - dinheiro

    public PagamentoDTO() {
    }

    public PagamentoDTO(Pagamento entity) {
        this.id = entity.getId();
        this.valor = entity.getValor();
        this.nome = entity.getNome();
        this.numeroDoCartao = entity.getNumeroDoCartao();
        this.validade = entity.getValidade();
        this.codigo = entity.getCodigo();
        this.status = entity.getStatus();
        this.pedidoId = entity.getPedidoId();
        this.formaDePagamentoId = entity.getFormaDePagamentoId();
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public String getNome() {
        return nome;
    }

    public String getNumeroDoCartao() {
        return numeroDoCartao;
    }

    public String getValidade() {
        return validade;
    }

    public String getCodigo() {
        return codigo;
    }

    public Status getStatus() {
        return status;
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public Long getFormaDePagamentoId() {
        return formaDePagamentoId;
    }
}
