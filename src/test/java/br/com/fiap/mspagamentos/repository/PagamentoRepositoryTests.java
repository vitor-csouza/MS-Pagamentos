package br.com.fiap.mspagamentos.repository;

import br.com.fiap.mspagamentos.model.Pagamento;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

@DataJpaTest
public class PagamentoRepositoryTests {

    @Autowired
        private PagamentoRepository repository;

    private Long existingId = 1L;
    private Long doesNotExistingId = 10l;

    @Test
    @DisplayName("Deveria excluir pagamento quando o Id existe")
    public void deleteShouldDeleteObjectWhenIdExists(){

        //Act
        repository.deleteById(existingId);

        //Assert
        Optional<Pagamento> result = repository.findById(existingId);
        Assertions.assertFalse(result.isPresent()); //Testa se existe um objeto dentro do Optional
    }

    @Test
    @DisplayName("Deveria lançar EmptyResultAccessException quando o id não existe")
    public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExisting(){

        Assertions.assertThrows(EmptyResultDataAccessException.class, () ->{
            repository.deleteById(doesNotExistingId);
        });
    }
}
