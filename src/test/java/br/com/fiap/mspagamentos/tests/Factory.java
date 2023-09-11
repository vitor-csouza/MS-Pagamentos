package br.com.fiap.mspagamentos.tests;

import br.com.fiap.mspagamentos.dto.PagamentoDTO;
import br.com.fiap.mspagamentos.model.Pagamento;
import br.com.fiap.mspagamentos.model.Status;

import java.math.BigDecimal;

public class Factory {

    public static Pagamento createPagamento(){
        return new Pagamento(1L , BigDecimal.valueOf(32.25), "Bach", "332433232", "07/25", "546", Status.CRIADO, 1L, 2L);
    }

    public static PagamentoDTO createPagamentoDTO(){
        Pagamento pagamento = createPagamento();
        return new PagamentoDTO(pagamento);
    }
}
