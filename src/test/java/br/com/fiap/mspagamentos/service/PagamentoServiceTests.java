package br.com.fiap.mspagamentos.service;

import br.com.fiap.mspagamentos.repository.PagamentoRepository;
import br.com.fiap.mspagamentos.service.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class PagamentoServiceTests {

    private Long existingId;
    private Long doesNotExistingId;

    @BeforeEach
    void setup() throws Exception{
        existingId = 1L;
        doesNotExistingId = 10L;

        Mockito.doNothing().when(repository).deleteById(existingId);
        Mockito.when(repository.existsById(existingId)).thenReturn(true);
        Mockito.when(repository.existsById(doesNotExistingId)).thenReturn(false);
    }

    @InjectMocks
    private PagamentoService service;

    @Mock
    private PagamentoRepository repository;


    @Test
    @DisplayName("delete Deveria não fazer nada quando o id existe")
    public void deleteShouldDoNothingWhenIdExists(){
        Assertions.assertDoesNotThrow(
                () -> {
                    service.delete(existingId);
                }
        );
        Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
    }

    @Test
    @DisplayName("Teste delete lança ResourceNotFoundException quando o id não existe")
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoeExists(){
        Assertions.assertThrows( ResourceNotFoundException.class, () -> {
            service.delete(doesNotExistingId);
                }
        );
    }



}
