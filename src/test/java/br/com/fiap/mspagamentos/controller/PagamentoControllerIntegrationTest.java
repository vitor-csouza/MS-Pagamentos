package br.com.fiap.mspagamentos.controller;

import br.com.fiap.mspagamentos.dto.PagamentoDTO;
import br.com.fiap.mspagamentos.model.Pagamento;
import br.com.fiap.mspagamentos.model.Status;
import br.com.fiap.mspagamentos.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PagamentoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private Long existingId;
    private Long doesNotExistingId;
    private Long totalRegisters;
    private PagamentoDTO pagamentoDTO;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() throws Exception{
        existingId = 1L;
        doesNotExistingId = 50L;
        totalRegisters = 6L;
        pagamentoDTO = Factory.createPagamentoDTO();
    }

    @Test
    @DisplayName("Deve listar todos os pegamentos e retornar staus 200")
    public void findAllShouldListAllPayments() throws Exception{
        //200
        mockMvc.perform(get("/pagamentos")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nome").isString())
                .andExpect(jsonPath("$[0].nome").value("Nicodemus"))
                .andExpect(jsonPath("$[0].status").value("CRIADO"));
    }

    @Test
    @DisplayName("Deve retornar um pagamento pelo ID e com status 200")
    public void findByIdShouldFindPaymentBydI() throws Exception{
        // 200
        ResultActions result = mockMvc.perform(get("/pagamentos/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());

        // $ - acessar o obejeto da resposta
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.valor").exists());
        result.andExpect(jsonPath("$.status").exists());
    }

    @Test
    @DisplayName("findById deve retornar Not Found quando ID não existe")
    public void findByIdShouldResoutceNotFoundWhenDoesNotExistingID() throws Exception{
        // 404
        ResultActions result = mockMvc.perform(get("/pagamentos/{id}", doesNotExistingId)
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("Deve salvar um pagamento, retornar status 201 e Location on Header")
    public void insertShouldSavePayment() throws Exception{
        //201
        Pagamento entity = Factory.createPagamento();
        entity.setId(null);

        String jsonBody = objectMapper.writeValueAsString(entity);

        mockMvc.perform(post("/pagamentos")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.valor").exists())
                .andExpect(jsonPath("$.nome").value("Bach"));

    }

    @Test
    @DisplayName("Deve salvar um pagamento com os campos obrigatórios, retornar status 201 e Location on Header")
    public void insertShouldSavePaymentWithMandatoryFields() throws Exception{
        // 201
        Pagamento entity =new Pagamento(null , BigDecimal.valueOf(32.25), null,
                null, null, null, Status.CRIADO, 1L, 2L);

        String jsonBody = objectMapper.writeValueAsString(entity);

        mockMvc.perform(post("/pagamentos")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").isEmpty());

    }


    @Test
    @DisplayName("Insert deve lançar exception quando dados inválids e retornar status 422")
    public void insertShouldSThrowExceptionWhenInvalidData() throws Exception{
        // 422
        Pagamento entity =new Pagamento();
        entity.setValor(BigDecimal.valueOf(25.25));

        String jsonBody = objectMapper.writeValueAsString(entity);

        mockMvc.perform(post("/pagamentos")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());

    }

    @Test
    @DisplayName("update deve atualizar pagamento quando ID existe e retornar status 200")
    public void updateShouldUpdatePaymentWhenExistingID() throws Exception{
        // 200
        String jsonBody = objectMapper.writeValueAsString(pagamentoDTO);

        ResultActions result = mockMvc.perform(put("/pagamentos/{id}", existingId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
        result.andDo(print());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.valor").exists());
        result.andExpect(jsonPath("$.status").exists());
    }


    @Test
    @DisplayName("update deve retornar NotFound quando id não existe")
    public void updateShouldReturnNotFoundWhenDoesNotExistingID() throws Exception{
        // 404
        String jsonBody = objectMapper.writeValueAsString(pagamentoDTO);

        mockMvc.perform(put("/pagamentos/{id}", doesNotExistingId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }


    @Test
    @DisplayName("delete deve retornar NoContent quando id  existe")
    public void deleteShouldReturnNoContentWhenExistingID() throws Exception{
        // 204
        mockMvc.perform(delete("/pagamentos/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    @DisplayName("delete deve retornar NotFound quando id não existe")
    public void deleteShouldReturnNotFoundWhenDoesNotExistingID() throws Exception{
        // 404
        mockMvc.perform(delete("/pagamentos/{id}", doesNotExistingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("Deve alterar status para confirmado quando ID existe, status 200")
    public void pathShouldAlterStatusToConfirmedWhenExistingID() throws Exception{
        // 200
        mockMvc.perform(patch("/pagamentos/{id}/confirmar", existingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("Deve alterar status para cancelado quando ID existe, status 200")
    public void pathShouldAlterStatusToCanceledWhenExistingID() throws Exception{
        // 200
        mockMvc.perform(patch("/pagamentos/{id}/cancelar", existingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("Deve retornar status 404 quando ID não existe")
    public void pathShouldReturnNotFoundWhenDoesNotExistingID() throws Exception{
        // 200
        mockMvc.perform(patch("/pagamentos/{id}/cancelar", doesNotExistingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }


}
