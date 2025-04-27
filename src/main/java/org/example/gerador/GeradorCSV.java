package org.example.gerador;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class GeradorCSV {
    public static void main(String[] args) {
        String nomeArquivo = "src/main/java/org/example/dados.csv";
        int quantidadeRegistros = 200; // Altere conforme quiser

        try (FileWriter writer = new FileWriter(nomeArquivo)) {
            // Cabeçalho
            writer.append("idade,nota_media,frequencia,reprovacoes,matriculado\n");

            Random random = new Random();

            for (int i = 0; i < quantidadeRegistros; i++) {
                int idade = gerarIdadeComProbabilidade();
                int notaMedia = gerarNotaComProbabilidade();
                int frequencia = gerarFrequenciaPorNota(notaMedia);
                int reprovacoes = gerarReprovacoesPorNotaEFrequencia(notaMedia, frequencia);
                String matriculado = gerarStatusMatriculado(notaMedia, frequencia, reprovacoes);

                String linha = String.format("%d,%d,%d,%d,%s\n", idade, notaMedia, frequencia, reprovacoes, matriculado);

                writer.append(linha);

            }

            System.out.println("Arquivo CSV gerado com sucesso: " + nomeArquivo);
        } catch (IOException e) {
            System.err.println("Erro ao escrever o arquivo: " + e.getMessage());
        }
    }

    private static int gerarIdadeComProbabilidade() {
        Random random = new Random();

        // Probabilidade de 50% para idades entre 18-25, 50% para idades entre 26-60
        if (random.nextDouble() < 0.5) {
            // Gera uma idade entre 18 e 25
            return random.nextInt(8) + 18; // 18 a 25
        } else {
            // Gera uma idade entre 26 e 60
            return random.nextInt(35) + 26; // 26 a 60
        }
    }

    private static int gerarNotaComProbabilidade() {
        Random random = new Random();
        double chance = random.nextDouble();

        if (chance < 0.1) {
            // 10%: nota entre 91 e 100
            return random.nextInt(10) + 91; // 91 a 100
        } else if (chance < 0.6) {
            // 50%: nota entre 80 e 90
            return random.nextInt(11) + 80; // 80 a 90
        } else {
            // 40%: nota entre 70 e 79
            return random.nextInt(10) + 70; // 70 a 79
        }
    }

    private static int gerarFrequenciaPorNota(int nota) {
        Random random = new Random();

        // Base da frequência proporcional à nota
        int baseFrequencia = (int) ((nota - 70) * (25.0 / 30.0)) + 75;
        // Ex: nota 70 → 75, nota 100 → 100 (escala linear)

        // Adiciona pequena variação randômica de -2 a +2, limitado a 75-100
        int variacao = random.nextInt(5) - 2;
        int frequencia = Math.min(100, Math.max(75, baseFrequencia + variacao));

        return frequencia;
    }

    private static int gerarReprovacoesPorNotaEFrequencia(int nota, int freq) {
        Random rand = new Random();
        double risco = ((100 - nota) / 30.0) * 0.6 + ((100 - freq) / 25.0) * 0.4;
        int teto = (int) (risco * 10);
        return rand.nextInt(teto + 1);
    }

    private static String gerarStatusMatriculado(int nota, int frequencia, int reprovacoes) {
        Random rand = new Random();

        // Normaliza e aplica pesos
        double notaScore = (nota - 70) / 30.0;        // 0 a 1
        double freqScore = (frequencia - 75) / 25.0;  // 0 a 1
        double reproScore = 1 - (reprovacoes / 10.0); // 1 = nenhuma reprovação

        // Aplica pesos (ajustáveis)
        double chance = notaScore * 0.5 + freqScore * 0.3 + reproScore * 0.2;

        // Sorteia se será "sim" ou "não" baseado na chance
        return rand.nextDouble() < chance ? "sim" : "não";
    }
}
