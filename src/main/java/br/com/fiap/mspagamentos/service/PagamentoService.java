package br.com.fiap.mspagamentos.service;

import br.com.fiap.mspagamentos.dto.PagamentoDTO;
import br.com.fiap.mspagamentos.http.PedidoClient;
import br.com.fiap.mspagamentos.model.Pagamento;
import br.com.fiap.mspagamentos.model.Status;
import br.com.fiap.mspagamentos.repository.PagamentoRepository;
import br.com.fiap.mspagamentos.service.exception.DatabaseException;
import br.com.fiap.mspagamentos.service.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PagamentoService {

    @Autowired
    private PedidoClient pedidoClient;

    @Autowired
    PagamentoRepository repository;

    @Transactional(readOnly = true)
    public List<PagamentoDTO> findAll(){
        return repository.findAll().stream().map(PagamentoDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PagamentoDTO findById(Long id){
        Pagamento pagamento = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Recurso não encontrado! Id:"+id)
        );
        PagamentoDTO dto = new PagamentoDTO(pagamento);
        return dto;
    }

    private void copyDtoToEntity(PagamentoDTO dto, Pagamento entity){
        entity.setValor(dto.getValor());
        entity.setNome(dto.getNome());
        entity.setNumeroDoCartao(dto.getNumeroDoCartao());
        entity.setValidade(dto.getValidade());
        entity.setCodigo(dto.getCodigo());
        entity.setStatus(dto.getStatus());
        entity.setPedidoId(dto.getPedidoId());
        entity.setFormaDePagamentoId(dto.getFormaDePagamentoId());

        entity.setStatus(Status.CRIADO);
    }

    @Transactional(readOnly = true)
    public PagamentoDTO insert(PagamentoDTO dto){
        Pagamento entity = new Pagamento();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new PagamentoDTO(entity);
    }

    @Transactional
    public PagamentoDTO update(Long id ,PagamentoDTO dto){
        try{
            Pagamento entity = repository.getReferenceById(id);
            copyDtoToEntity(dto, entity);
            entity = repository.save(entity);
            return new PagamentoDTO(entity);
        } catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Recurso não encontrado. Id: "+id);
        }
    }

    @Transactional
    public void delete(Long id){
        if(!repository.existsById(id)){
            throw new ResourceNotFoundException("Recurso não encontrado");
        }
        try{
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e){
            throw new DatabaseException("Recurso não encontrado");
        }
    }

    @Transactional
    public void confirmarPagamento(Long id){
        // Recupera o pagamento no DB
        Optional<Pagamento> pagamento = repository.findById(id);

        // Valida se existe pagamento
        if(!pagamento.isPresent()){
            throw new ResourceNotFoundException("Recurso não encontrado");
        }

        // Altera o status do pagamento como confirmado
        pagamento.get().setStatus(Status.CONFIRMADO);

        // Salva a alteração no DB
        repository.save(pagamento.get());

        // Chamamos o PedidoClient para fazer a atualização passando  o ID do pedido
        // Passamos o ID do pedido e quem tem essa informação é o pagamento.get().getPedidoId()
        // Esse getter é que possio a informação o pedido
        // pedidoClient foi injetado na dependência
        pedidoClient.atualizarPagamentoPedido(pagamento.get().getPedidoId());
    }

    @Transactional
    public void cancelarPagamento(Long id){
        Optional<Pagamento> pagamento = repository.findById(id);

        if(!pagamento.isPresent()){
            throw new ResourceNotFoundException("Recurso não encontrado");
        }

        pagamento.get().setStatus(Status.CANCELADO);
        repository.save(pagamento.get());
    }
}
