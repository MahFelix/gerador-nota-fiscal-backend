package com.GeradorNotaFiscal.Service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class ImportacaoService {

    private final Map<String, List<Map<String, Object>>> tabelas = new HashMap<>();

    private String ultimoNomeTabela;

    public List<Map<String, Object>> processarArquivo(MultipartFile arquivo) throws IOException {
        String nomeArquivo = arquivo.getOriginalFilename();
        if (nomeArquivo == null) {
            throw new IllegalArgumentException("Nome do arquivo está vazio.");
        }

        if (nomeArquivo.endsWith(".xlsx")) {
            try (Workbook workbook = new XSSFWorkbook(arquivo.getInputStream())) {
                Sheet sheet = workbook.getSheetAt(0);
                List<Map<String, Object>> dados = new ArrayList<>();

                // Normalizar nome da tabela
                String nomeTabela = normalizarNomeTabela(arquivo.getOriginalFilename());
                ultimoNomeTabela = nomeTabela; // Salva o nome da tabela

                // Ler cabeçalhos
                Row headerRow = sheet.getRow(0);
                List<String> headers = new ArrayList<>();
                for (Cell cell : headerRow) {
                    headers.add(cell.getStringCellValue().trim());
                }

                // Ler dados
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    if (row == null) continue;

                    Map<String, Object> linha = new HashMap<>();
                    for (int j = 0; j < headers.size(); j++) {
                        Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        linha.put(headers.get(j), getCellValue(cell));
                    }
                    dados.add(linha);
                }

                // Armazena a tabela processada
                tabelas.put(nomeTabela, dados);
                return dados;
            }
        } else if (nomeArquivo.endsWith(".csv")) {
            return processarCSV(arquivo);
        } else {
            throw new IllegalArgumentException("Tipo de arquivo não suportado: " + nomeArquivo);
        }
    }


    private Object getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue();
                } else {
                    double valor = cell.getNumericCellValue();
                    return (valor % 1 == 0) ? (int) valor : valor; // Converte para int se não houver decimal
                }
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    private String normalizarNomeTabela(String nomeArquivo) {
        if (nomeArquivo == null) return "tabela_sem_nome";
        return nomeArquivo.replaceAll("[^a-zA-Z0-9-_\\.]", "_") + "_" + System.currentTimeMillis();
    }

    public void atualizarLinha(String idTabela, int idLinha, Map<String, Object> dadosAtualizados) {
        if (!tabelas.containsKey(idTabela)) {
            throw new IllegalArgumentException("Tabela não encontrada: " + idTabela);
        }

        List<Map<String, Object>> tabela = tabelas.get(idTabela);

        if (idLinha < 0 || idLinha >= tabela.size()) {
            throw new IllegalArgumentException("Índice da linha inválido: " + idLinha);
        }

        tabela.get(idLinha).putAll(dadosAtualizados);
    }

    public void deletarLinha(String idTabela, int idLinha) {
        if (!tabelas.containsKey(idTabela)) {
            throw new IllegalArgumentException("Tabela não encontrada: " + idTabela);
        }

        List<Map<String, Object>> tabela = tabelas.get(idTabela);

        if (idLinha < 0 || idLinha >= tabela.size()) {
            throw new IllegalArgumentException("Índice da linha inválido: " + idLinha);
        }

        tabela.remove(idLinha);
    }


    public Map<String, List<Map<String, Object>>> getTabelas() {
        return Collections.unmodifiableMap(tabelas); // Retorna uma cópia imutável do mapa
    }

    public String getUltimoNomeTabela() {
        return ultimoNomeTabela;
    }


    private List<Map<String, Object>> processarExcel(MultipartFile arquivo) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(arquivo.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            List<Map<String, Object>> dados = new ArrayList<>();

            String nomeTabela = normalizarNomeTabela(arquivo.getOriginalFilename());
            ultimoNomeTabela = nomeTabela;

            Row headerRow = sheet.getRow(0);
            List<String> headers = new ArrayList<>();
            for (Cell cell : headerRow) {
                headers.add(cell.getStringCellValue().trim());
            }

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Map<String, Object> linha = new HashMap<>();
                for (int j = 0; j < headers.size(); j++) {
                    Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    linha.put(headers.get(j), getCellValue(cell));
                }
                dados.add(linha);
            }

            tabelas.put(nomeTabela, dados);
            return dados;
        }
    }


    private List<Map<String, Object>> processarCSV(MultipartFile arquivo) throws IOException {
        List<Map<String, Object>> dados = new ArrayList<>();

        String nomeTabela = normalizarNomeTabela(arquivo.getOriginalFilename());
        ultimoNomeTabela = nomeTabela;

        try (Scanner scanner = new Scanner(arquivo.getInputStream())) {
            if (!scanner.hasNextLine()) return dados;

            String headerLine = scanner.nextLine();
            String[] headers = headerLine.split(",");

            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine();
                String[] valores = linha.split(",", -1); // -1 mantém campos vazios

                Map<String, Object> linhaMap = new HashMap<>();
                for (int i = 0; i < headers.length; i++) {
                    linhaMap.put(headers[i].trim(), i < valores.length ? valores[i].trim() : "");
                }
                dados.add(linhaMap);
            }
        }

        tabelas.put(nomeTabela, dados);
        return dados;
    }


}
