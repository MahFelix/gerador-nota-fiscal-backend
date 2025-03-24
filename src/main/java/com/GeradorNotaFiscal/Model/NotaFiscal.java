package com.GeradorNotaFiscal.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class NotaFiscal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cliente;
    private String produto;
    private Double valor;
    private String dataEmissao;

    // Getters padrão necessários
    public Long getId() {
        return id;
    }

    public Double getValor() {
        return valor;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public String getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(String dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    // Corrigir o método setValor
    public void setValor(Double valor) {
        if (valor != null && valor > 0) {
            this.valor = valor;
        } else {
            throw new IllegalArgumentException("Valor deve ser positivo e não nulo.");
        }
    }

    public void setId(Long id) {
        this.id = id;
    }
}
