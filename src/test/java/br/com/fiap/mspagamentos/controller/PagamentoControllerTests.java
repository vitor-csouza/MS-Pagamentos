package br.com.fiap.mspagamentos.controller;

import br.com.fiap.mspagamentos.dto.PagamentoDTO;
import br.com.fiap.mspagamentos.model.Pagamento;
import br.com.fiap.mspagamentos.model.Status;
import br.com.fiap.mspagamentos.service.PagamentoService;
import br.com.fiap.mspagamentos.service.exception.ResourceNotFoundException;
import br.com.fiap.mspagamentos.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(PagamentoController.class)
public class PagamentoControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PagamentoService service;
    private PagamentoDTO pagamentoDTO;
    private Long existingId;
    private Long doesNotExistingId;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() throws Exception{
        pagamentoDTO = Factory.createPagamentoDTO();

        List<PagamentoDTO> list = List.of(pagamentoDTO);

        existingId = 1L;
        doesNotExistingId = 10L;

        //findAll
        when(service.findAll()).thenReturn(list);

        //findById
        when(service.findById(existingId)).thenReturn(pagamentoDTO);
        when(service.findById(doesNotExistingId)).thenThrow(ResourceNotFoundException.class);

        //insert
        when(service.insert(any())).thenReturn(pagamentoDTO);

        //update
        when(service.update(eq(existingId), any())).thenReturn(pagamentoDTO);
        when(service.update(eq(doesNotExistingId), any())).thenThrow(ResourceNotFoundException.class);

        //delete
        doNothing().when(service).delete(existingId);
        doThrow(ResourceNotFoundException.class).when(service).delete(doesNotExistingId);
    }

    @Test
    @DisplayName("findAll Deveria retornar uma lista do tipo de PagamentoDTO ")
    public void findAllShouldReturnTipoPagamentoDTOList() throws Exception{
        // 200
        ResultActions result = mockMvc.perform(get("/pagamentos")
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
    }

    @Test
    @DisplayName("findById deve retornar PagamentoDTO quando id existe")
    public void findByIdShouldReturnPagamentoDTOWhenExistingId() throws Exception{
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
    @DisplayName("findById deve retornar NotFound quando id não existe")
    public void findByIdShouldReturnNotFoundWhenDoesNotExistingId() throws Exception{
        // 404
        ResultActions result = mockMvc.perform(get("/pagamentos/{id}", doesNotExistingId)
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("insert deve retornar created PagamentoDTO com id")
    public void insertShouldReturnPagamentoDTOWithIDCreated() throws Exception{
        // 201
        PagamentoDTO dto = new PagamentoDTO(new Pagamento(null , BigDecimal.valueOf(32.25), "Bach",
                "332433232", "07/25", "546", Status.CRIADO, 1L, 2L));
        String jsonBody = objectMapper.writeValueAsString(dto);

        ResultActions result = mockMvc.perform(post("/pagamentos")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isCreated());
        result.andDo(print());
        result.andExpect(header().exists("Location"));
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.valor").exists());
        result.andExpect(jsonPath("$.status").exists());

    }

    @Test
    @DisplayName("update deve retornar PagamentoDTO quando id existe")
    public void updateShouldReturnPagamentoDTOWhenExistingID() throws Exception{
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

        ResultActions result = mockMvc.perform(put("/pagamentos/{id}", doesNotExistingId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isNotFound());
        result.andDo(print());
    }

    @Test
    @DisplayName("delete deve retornar NoContent quando id  existe")
    public void deleteShouldReturnNoContentWhenExistingID() throws Exception{
        // 204
        ResultActions result = mockMvc.perform(delete("/pagamentos/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isNoContent());
        result.andDo(print());
    }

    @Test
    @DisplayName("delete deve retornar NotFound quando id não existe")
    public void deleteShouldReturnNotFoundWhenDoesNotExistingID() throws Exception{
        // 404
        ResultActions result = mockMvc.perform(delete("/pagamentos/{id}", doesNotExistingId)
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isNotFound());
        result.andDo(print());
    }
}
