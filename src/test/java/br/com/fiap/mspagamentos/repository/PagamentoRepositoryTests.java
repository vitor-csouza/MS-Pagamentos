package br.com.fiap.mspagamentos.repository;

import br.com.fiap.mspagamentos.model.Pagamento;
import br.com.fiap.mspagamentos.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

    private Long existingId;
    private Long doesNotExistingId;
    private Long countTotalpagamentos;

    @BeforeEach
    void setup() throws Exception{
        existingId = 1L;
        doesNotExistingId = 10L;
        countTotalpagamentos = 2L;
    }

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

    @Test
    @DisplayName("save deveria salvar objeto com auto incremento quando id é null")
    public void saveShouldPersistWithAutoIncrementWhenIdIsNull(){
        Pagamento pagamento = Factory.createPagamento();
        pagamento.setId(null);
        pagamento = repository.save(pagamento);
        Assertions.assertNotNull(pagamento.getId());

        Assertions.assertEquals(countTotalpagamentos + 1, pagamento.getId());
    }

    @Test
    @DisplayName("findById deveria retornar um Optional<Pagamento> não vazio quando o id existir")
    public void findByIdShouldReturnDoesEmptyOptionalWhenIdExisting(){
        Optional<Pagamento> pagamento = repository.findById(existingId);
        Assertions.assertTrue(pagamento.isPresent());
    }

    @Test
    @DisplayName("findById deveria retornar um Optional<Pagamento> vazio quando o id não existir")
    public void findByIdShouldReturnEmptyOptionalWhenIdDoesNoteExisting(){
        Optional<Pagamento> pagamento = repository.findById(doesNotExistingId);
        Assertions.assertFalse(pagamento.isPresent());
    }
}
