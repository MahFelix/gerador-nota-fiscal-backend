package com.GeradorNotaFiscal.Controller;

import com.GeradorNotaFiscal.Model.DadosImportacao;
import com.GeradorNotaFiscal.Service.ImportacaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/importacao")
@CrossOrigin(origins = "http://localhost:5173")
public class ImportacaoController {

    private static final Logger logger = LoggerFactory.getLogger(ImportacaoController.class);

    @Autowired
    private ImportacaoService importacaoService;

    @PostMapping("/upload")
    public ResponseEntity<DadosImportacao> uploadArquivo(@RequestParam("arquivo") MultipartFile arquivo) {
        try {
            String nomeArquivo = arquivo.getOriginalFilename();
            if (nomeArquivo == null || !nomeArquivo.endsWith(".xlsx")) {
                return ResponseEntity.badRequest().body(null);
            }

            logger.info("Recebendo arquivo para importação: {}", nomeArquivo);

            // Processa o arquivo e armazena os dados
            List<Map<String, Object>> linhas = importacaoService.processarArquivo(arquivo);
            String idTabela = importacaoService.getUltimoNomeTabela();

            DadosImportacao dados = new DadosImportacao();
            dados.setIdTabela(idTabela); // Define o ID correto da tabela
            dados.setNomeArquivo(nomeArquivo);
            dados.setLinhas(linhas);

            return ResponseEntity.ok(dados);
        } catch (Exception e) {
            logger.error("Erro ao processar arquivo", e);
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/atualizar-linha/{idTabela}/{idLinha}")
    public ResponseEntity<String> atualizarLinha(
            @PathVariable String idTabela,
            @PathVariable int idLinha,
            @RequestBody Map<String, Object> dadosAtualizados) {
        try {
            // Verificação corrigida
            if (!importacaoService.getTabelas().containsKey(idTabela)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro: Tabela não encontrada: " + idTabela);
                // Ou alternativa:
                // return new ResponseEntity<>("Erro: Tabela não encontrada: " + idTabela, HttpStatus.NOT_FOUND);
            }

            importacaoService.atualizarLinha(idTabela, idLinha, dadosAtualizados);
            return ResponseEntity.ok("Linha atualizada com sucesso!");
        } catch (Exception e) {
            logger.error("Erro ao atualizar linha", e);
            return ResponseEntity.badRequest().body("Erro ao atualizar linha: " + e.getMessage());
        }
    }

    @DeleteMapping("/deletar-linha/{idTabela}/{idLinha}")
    public ResponseEntity<String> deletarLinha(
            @PathVariable String idTabela,
            @PathVariable int idLinha) {
        try {
            // Verificação corrigida
            if (!importacaoService.getTabelas().containsKey(idTabela)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro: Tabela não encontrada: " + idTabela);
            }

            importacaoService.deletarLinha(idTabela, idLinha);
            return ResponseEntity.ok("Linha deletada com sucesso!");
        } catch (Exception e) {
            logger.error("Erro ao deletar linha", e);
            return ResponseEntity.badRequest().body("Erro ao deletar linha: " + e.getMessage());
        }
    }
}
