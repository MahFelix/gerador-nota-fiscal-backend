package com.GeradorNotaFiscal.Service;

import com.GeradorNotaFiscal.Model.NotaFiscal;
import com.GeradorNotaFiscal.Repository.NotaFiscalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotaFiscalService {

    @Autowired
    private NotaFiscalRepository repository;

    public NotaFiscal salvar(NotaFiscal notaFiscal) {
        // Adicione validação simples
        if(notaFiscal.getValor() <= 0) {
            throw new IllegalArgumentException("Valor deve ser positivo");
        }
        return repository.save(notaFiscal);
    }

    public List<NotaFiscal> listar() {
        List<NotaFiscal> todas = repository.findAll();
        System.out.println("Total de notas no banco: " + todas.size()); // Log simples
        return todas;
    }

    public void deletar(Long id) {
        Optional<NotaFiscal> notaFiscalExistente = repository.findById(id);
        if (notaFiscalExistente.isPresent()) {
            repository.delete(notaFiscalExistente.get());
        } else {
            throw new IllegalArgumentException("Nota fiscal não encontrada com o ID: " + id);
        }
    }

    public NotaFiscal atualizar(Long id, NotaFiscal notaFiscal) {
        Optional<NotaFiscal> notaFiscalExistente = repository.findById(id);
        if (notaFiscalExistente.isPresent()) {
            NotaFiscal notaAtualizada = notaFiscalExistente.get();
            // Atualiza os campos da nota fiscal
            if (notaFiscal.getCliente() != null) {
                notaAtualizada.setCliente(notaFiscal.getCliente());
            }
            if (notaFiscal.getProduto() != null) {
                notaAtualizada.setProduto(notaFiscal.getProduto());
            }
            if (notaFiscal.getValor() > 0) {
                notaAtualizada.setValor(notaFiscal.getValor());
            }
            if (notaFiscal.getDataEmissao() != null) {
                notaAtualizada.setDataEmissao(notaFiscal.getDataEmissao());
            }
            return repository.save(notaAtualizada);
        } else {
            throw new IllegalArgumentException("Nota fiscal não encontrada com o ID: " + id);
        }
    }


    public void deletarTodas() {
        repository.deleteAll();
    }
}
