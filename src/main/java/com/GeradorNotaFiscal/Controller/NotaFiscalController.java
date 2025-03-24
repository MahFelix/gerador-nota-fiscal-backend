package com.GeradorNotaFiscal.Controller;

import com.GeradorNotaFiscal.Model.NotaFiscal;
import com.GeradorNotaFiscal.Service.NotaFiscalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notas-fiscais") // Adicionei /api para consistência
@CrossOrigin(origins = "http://localhost:5173")
public class NotaFiscalController {

    private static final Logger logger = LoggerFactory.getLogger(NotaFiscalController.class);

    @Autowired
    private NotaFiscalService service;

    @PostMapping("/salvar")
    public ResponseEntity<NotaFiscal> salvar(@RequestBody NotaFiscal notaFiscal) {
        logger.info("Recebendo nota fiscal para salvar: {}", notaFiscal);
        NotaFiscal salva = service.salvar(notaFiscal);
        return ResponseEntity.ok(salva);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<NotaFiscal>> listar() {
        List<NotaFiscal> notas = service.listar();
        return ResponseEntity.ok(notas);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotaFiscal> atualizar(@PathVariable Long id, @RequestBody NotaFiscal notaFiscal) {
        logger.info("Atualizando nota fiscal ID {}: {}", id, notaFiscal);
        notaFiscal.setId(id); // Garante que o ID seja o mesmo
        NotaFiscal atualizada = service.salvar(notaFiscal);
        return ResponseEntity.ok(atualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        logger.info("Deletando nota fiscal ID: {}", id);
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("delete-all")
    public ResponseEntity<Void> deletarTodas() {
        logger.info("Deletando todas as notas fiscais");
        service.deletarTodas();
        return ResponseEntity.noContent().build();
    }
}