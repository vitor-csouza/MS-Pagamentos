package br.com.fiap.mspagamentos.service;

import br.com.fiap.mspagamentos.repository.PagamentoRepository;
import br.com.fiap.mspagamentos.service.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class PagamentoServiceIntegrationTest {

    @Autowired
    private PagamentoService service;

    @Autowired
    private PagamentoRepository repository;

    private Long existingId;
    private Long doesNotExistingId;
    private Long totalRegisters;

    @BeforeEach
    void setup() throws Exception{
        existingId = 1L;
        doesNotExistingId = 50L;
        totalRegisters = 6L;
    }

    @Test
    @DisplayName("Deve deletar um pagamento pelo id")
    public void deleteShouldDeletePaymentWhenIdExists(){
        service.delete(existingId);
        Assertions.assertEquals(totalRegisters-1, repository.count());
    }

    @Test
    @DisplayName("Deve lançar ReosurceNotFoundException quando id não existir")
    public void deleteShouldThrowsResourceNotFoundExceptionWhenDoesNotExistingId(){
        Assertions.assertThrows( ResourceNotFoundException.class, () -> {
                    service.delete(doesNotExistingId);
                }
        );
    }

    @Test
    @DisplayName("Deve listas todos os pagamentos e retornar status 200")
    public void findAllShouldListAllPayments(){
        var result = service.findAll();
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(totalRegisters, result.size());
        Assertions.assertEquals(Double.valueOf(1200.00), result.get(0).getValor().doubleValue());
        Assertions.assertEquals("Amadeus", result.get(1).getNome());
        Assertions.assertEquals(null, result.get(5).getNome());
    }
}
