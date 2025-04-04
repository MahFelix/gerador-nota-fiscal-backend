package com.GeradorNotaFiscal.Model;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DadosImportacao {
    private String idTabela;  // Adicionando o campo idTabela
    private String nomeArquivo;
    private List<String> colunas;
    private List<Map<String, Object>> linhas;

    // Construtor padrão
    public DadosImportacao() {
    }

    // Construtor parametrizado
    public DadosImportacao(String idTabela, String nomeArquivo, List<String> colunas, List<Map<String, Object>> linhas) {
        this.idTabela = idTabela;
        this.nomeArquivo = nomeArquivo;
        this.colunas = colunas;
        this.linhas = linhas;
    }

    // Getters e Setters
    public String getIdTabela() {
        return idTabela;
    }

    public void setIdTabela(String idTabela) {
        this.idTabela = idTabela;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public List<String> getColunas() {
        return colunas;
    }

    public void setColunas(List<String> colunas) {
        this.colunas = colunas;
    }

    public List<Map<String, Object>> getLinhas() {
        return linhas;
    }

    public void setLinhas(List<Map<String, Object>> linhas) {
        this.linhas = linhas;
    }

    // Método toString para facilitar debug
    @Override
    public String toString() {
        return "DadosImportacao{" +
                "idTabela='" + idTabela + '\'' +
                ", nomeArquivo='" + nomeArquivo + '\'' +
                ", colunas=" + colunas +
                ", linhas=" + linhas +
                '}';
    }

    // Equals e HashCode para comparações corretas de objetos
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DadosImportacao that = (DadosImportacao) o;
        return Objects.equals(idTabela, that.idTabela) &&
                Objects.equals(nomeArquivo, that.nomeArquivo) &&
                Objects.equals(colunas, that.colunas) &&
                Objects.equals(linhas, that.linhas);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTabela, nomeArquivo, colunas, linhas);
    }
}
