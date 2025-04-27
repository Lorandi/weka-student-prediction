package org.example;

import weka.classifiers.bayes.NaiveBayes;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

import java.io.InputStream;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws Exception {

        InputStream arffStream = Main.class.getClassLoader().getResourceAsStream("alunos.arff");


        if (arffStream == null) {
            System.out.println("Arquivo ARFF não encontrado no JAR.");
            return;
        }

        // Carrega o arquivo ARFF usando o InputStream
        DataSource ds = new DataSource(arffStream);

        //carrega os dados
        Instances ins = ds.getDataSet();

        //seta o índice da classe
        ins.setClassIndex(ins.numAttributes() - 1);

        //seleciona o algoritmo de classificação
        NaiveBayes nb = new NaiveBayes();
        //treina o algoritmo
        nb.buildClassifier(ins);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("\nDigite a idade: ");
            int idade = scanner.nextInt();

            System.out.print("Digite a nota média (70 a 100): ");
            int nota = scanner.nextInt();

            System.out.print("Digite a taxa de presença (75 a 100): ");
            int frequencia = scanner.nextInt();

            System.out.print("Digite o número de reprovações (0 a 10): ");
            int reprovacoes = scanner.nextInt();

            //cria uma nova instância
            Instance novo = new DenseInstance(ins.numAttributes());
            //associa o novo objeto a instância
            novo.setDataset(ins);
            //seta os valores da nova instância
            novo.setValue(0, idade);
            novo.setValue(1, nota);
            novo.setValue(2, frequencia);
            novo.setValue(3, reprovacoes);

            // Classificação
            double[] probabilidade = nb.distributionForInstance(novo);
            double probPermanencia = Math.round(probabilidade[0] * 10000) / 100.0;
            double probEvasao = Math.round(probabilidade[1] * 10000) / 100.0;

            System.out.println("\nResultado:");
            System.out.println("Probabilidade de permanência: " + probPermanencia + "%");
            System.out.println("Probabilidade de evasão: " + probEvasao + "%");

            // Pergunta se deseja continuar
            System.out.print("\nDeseja testar outro aluno? (s/n): ");
            String resposta = scanner.next();
            if (resposta.equalsIgnoreCase("n")) {
                System.out.println("Encerrando o programa.");
                break;
            }
        }

        scanner.close();
    }
}