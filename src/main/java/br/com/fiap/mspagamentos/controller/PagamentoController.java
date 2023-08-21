package br.com.fiap.mspagamentos.controller;

import br.com.fiap.mspagamentos.dto.PagamentoDTO;
import br.com.fiap.mspagamentos.model.Pagamento;
import br.com.fiap.mspagamentos.service.PagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

	@Autowired
	PagamentoService service;
	
	@GetMapping
	public ResponseEntity<List<PagamentoDTO>> findAll() {
		List<PagamentoDTO> dto = service.findAll();
		return ResponseEntity.ok(dto);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<PagamentoDTO> findById(@PathVariable Long id){
		PagamentoDTO dto = service.findById(id);
		return ResponseEntity.ok(dto);
	}

	@PostMapping
	public  ResponseEntity<PagamentoDTO> insert(@Valid @RequestBody PagamentoDTO dto){
		dto = service.insert(dto);

		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/id").buildAndExpand(dto.getId()).toUri();

		return ResponseEntity.created(uri).body(dto);
	}

	@PutMapping("/{id}")
	public ResponseEntity<PagamentoDTO> upadate(@Valid @Positive @PathVariable Long id, @Valid @RequestBody PagamentoDTO dto){
		dto = service.update(id, dto);

		return ResponseEntity.ok(dto);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id){
		service.delete(id);

		return ResponseEntity.noContent().build();
	}

}


