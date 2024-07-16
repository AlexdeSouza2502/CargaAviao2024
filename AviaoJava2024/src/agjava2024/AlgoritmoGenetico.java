// AlgoritmoGenetico.java
package agjava2024;

import java.io.*;
import java.util.*;

public class AlgoritmoGenetico {
    private int tamPopulacao;
    private int tamCromossomo = 0;
    private double capacidadePeso;
    private double larguraMaxima;
    private double alturaMaxima;
    private double profundidadeMaxima;
    private int probMutacao;
    private int qtdeCruzamentos;
    private int numeroGeracoes;
    private ArrayList<Produto> produtos = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> populacao = new ArrayList<>();
    private ArrayList<Integer> roletaVirtual = new ArrayList<>();

    public AlgoritmoGenetico(int tamanhoPopulacao, double capacidadePeso, double larguraMaxima, double alturaMaxima, double profundidadeMaxima, int probabilidadeMutacao, int qtdeCruzamentos, int numeroGeracoes) {
        this.tamPopulacao = tamanhoPopulacao;
        this.capacidadePeso = capacidadePeso;
        this.larguraMaxima = larguraMaxima;
        this.alturaMaxima = alturaMaxima;
        this.profundidadeMaxima = profundidadeMaxima;
        this.probMutacao = probabilidadeMutacao;
        this.qtdeCruzamentos = qtdeCruzamentos;
        this.numeroGeracoes = numeroGeracoes;
    }

    public void executar() {
        this.criarPopulacao();
        for (int i = 0; i < this.numeroGeracoes; i++) {
            System.out.println("Geracao:" + i);
            mostraPopulacao();
            operadoresGeneticos();
            novoPopulacao();
        }

        int indiceMelhor = obterIndiceMelhorAvaliacao();
        System.out.println("Indivíduo com melhor avaliação: Cromossomo " + indiceMelhor + ": " + populacao.get(indiceMelhor));
        System.out.println("Melhor Avaliacao: " + fitness(populacao.get(indiceMelhor)));
        mostrarMochila(populacao.get(indiceMelhor));
    }

    public void mostraPopulacao() {
        for (int i = 0; i < this.tamPopulacao; i++) {
            System.out.println("Cromossomo: " + i + ": " + populacao.get(i));
            System.out.println("Avaliacao: " + fitness(populacao.get(i)));
        }
    }

    public void carregaArquivo(String fileName) {
        String csvFile = fileName;
        String line = "";
        String[] produto = null;
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                produto = line.split(",");
                Produto novoProduto = new Produto();
                novoProduto.setDescricao(produto[0]);
                novoProduto.setPeso(Double.parseDouble(produto[1]));
                novoProduto.setLargura(Double.parseDouble(produto[2]));
                novoProduto.setAltura(Double.parseDouble(produto[3]));
                novoProduto.setProfundidade(Double.parseDouble(produto[4]));
                produtos.add(novoProduto);
                System.out.println(novoProduto);
                this.tamCromossomo++;
            } // fim percurso no arquivo

            System.out.println("Tamanho do cromossomo: " + this.tamCromossomo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Integer> criarCromossomo() {
        ArrayList<Integer> novoCromossomo = new ArrayList<>();
        for (int i = 0; i < this.tamCromossomo; i++) {
            if (Math.random() < 0.6)
                novoCromossomo.add(0);
            else
                novoCromossomo.add(1);
        } // fim for
        return novoCromossomo;
    }

    private void criarPopulacao() {
        for (int i = 0; i < this.tamPopulacao; i++)
            this.populacao.add(criarCromossomo());
    }

    private double fitness(ArrayList<Integer> cromossomo) {
        double pesoTotal = 0, volumeTotal = 0;
        boolean excedeDimensoes = false;
        for (int i = 0; i < this.tamCromossomo; i++) {
            if (cromossomo.get(i) == 1) {
                Produto produto = produtos.get(i);
                pesoTotal += produto.getPeso();
                if (produto.getLargura() > this.larguraMaxima || produto.getAltura() > this.alturaMaxima || produto.getProfundidade() > this.profundidadeMaxima) {
                    excedeDimensoes = true;
                }
                volumeTotal += produto.getLargura() * produto.getAltura() * produto.getProfundidade();
            } // fim do teste     
        }
        if (pesoTotal <= this.capacidadePeso && !excedeDimensoes) {
            return volumeTotal;
        } else {
            return 0;
        }
    }

    private void gerarRoleta() {
        ArrayList<Double> fitnessIndividuos = new ArrayList<>();
        double totalFitness = 0;
        for (int i = 0; i < this.tamPopulacao; i++) {
            fitnessIndividuos.add(i, fitness(this.populacao.get(i)));
            totalFitness += fitnessIndividuos.get(i);
        }
        System.out.println("Soma total fitness: " + totalFitness);
        System.out.println("Notas: " + fitnessIndividuos);
        for (int i = 0; i < this.tamPopulacao; i++) {
            double qtdPosicoes = (fitnessIndividuos.get(i) / totalFitness) * 1000;
            for (int j = 0; j <= qtdPosicoes; j++)
                roletaVirtual.add(i);
        }
    } // fim gerarRoleta

    private int roleta() {
        Random r = new Random();
        int selecionado = r.nextInt(roletaVirtual.size());
        return roletaVirtual.get(selecionado);
    } // fim roleta

    private ArrayList<ArrayList<Integer>> cruzamento() {
        ArrayList<Integer> filho1 = new ArrayList<>();
        ArrayList<Integer> filho2 = new ArrayList<>();
        ArrayList<ArrayList<Integer>> filhos = new ArrayList<>();
        ArrayList<Integer> pai1, pai2;
        int indice_pai1, indice_pai2;
        indice_pai1 = roleta();
        indice_pai2 = roleta();
        pai1 = populacao.get(indice_pai1);
        pai2 = populacao.get(indice_pai2);
        Random r = new Random();
        int pos = r.nextInt(this.tamCromossomo); // ponto de corte
        for (int i = 0; i <= pos; i++) {
            filho1.add(pai1.get(i));
            filho2.add(pai2.get(i));
        }
        for (int i = pos + 1; i < this.tamCromossomo; i++) {
            filho1.add(pai2.get(i));
            filho2.add(pai1.get(i));
        }
        filhos.add(filho1);
        filhos.add(filho2);
        return filhos;
    }

    private void mutacao(ArrayList<Integer> filho) {
        Random r = new Random();
        int v = r.nextInt(100);
        if (v < this.probMutacao) {
            int ponto = r.nextInt(this.tamCromossomo);
            if (filho.get(ponto) == 1)
                filho.set(ponto, 0);
            else
                filho.set(ponto, 1);

            int ponto2 = r.nextInt(this.tamCromossomo);
            if (filho.get(ponto2) == 1)
                filho.set(ponto2, 0);
            else
                filho.set(ponto2, 1);
            System.out.println("Ocorreu mutação!");
        } // fim if mutacao
    }

    private void operadoresGeneticos() {
        ArrayList<Integer> filho1, filho2;
        ArrayList<ArrayList<Integer>> filhos;
        gerarRoleta();
        for (int i = 0; i < this.qtdeCruzamentos; i++) {
            filhos = cruzamento();
            filho1 = filhos.get(0);
            filho2 = filhos.get(1);
            mutacao(filho1);
            mutacao(filho2);
            populacao.add(filho1);
            populacao.add(filho2);
        }
    }

    private int obterIndiceMenorAvaliacao() {
        int indiceMenor = 0;
        double menorAvaliacao = fitness(populacao.get(0));
        for (int i = 1; i < populacao.size(); i++) {
            double avaliacaoAtual = fitness(populacao.get(i));
            if (avaliacaoAtual < menorAvaliacao) {
                menorAvaliacao = avaliacaoAtual;
                indiceMenor = i;
            }
        }
        return indiceMenor;
    }

    private int obterIndiceMelhorAvaliacao() {
        int indiceMelhor = 0;
        double melhorAvaliacao = fitness(populacao.get(0));
        for (int i = 1; i < populacao.size(); i++) {
            double avaliacaoAtual = fitness(populacao.get(i));
            if (avaliacaoAtual > melhorAvaliacao) {
                melhorAvaliacao = avaliacaoAtual;
                indiceMelhor = i;
            }
        }
        return indiceMelhor;
    }

    private void novoPopulacao() {
        for (int i = 0; i < this.qtdeCruzamentos; i++) {
            populacao.remove(obterIndiceMenorAvaliacao());
            populacao.remove(obterIndiceMenorAvaliacao());
        }
    }

    public void mostrarMochila(ArrayList<Integer> resultado) {
        System.out.println("Avaliacao do Melhor:" + this.fitness(resultado));
        System.out.println("Produtos levados na mochilha:");
        for (int i = 0; i < resultado.size(); i++) {
            int leva = resultado.get(i);
            if (leva == 1) {
                Produto p = produtos.get(i);
                System.out.println(p.getDescricao() +
                        " Valor: " + p.getValor() + " Peso: " + p.getPeso() + " Largura: " + p.getLargura() + " Altura: " + p.getAltura() + " Profundidade: " + p.getProfundidade());
            }
        }
    }
}
