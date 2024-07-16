package agjava2024;

public class AGJava2024 {

    public static void main(String[] args) {
        int populacao = 20;
        double limitePeso = 8000;  // Capacidade de carga em kg
        int probabilidadeMutacao = 5;
        int qtdeCruzamentos = 4;
        int numeroGeracoes = 10;
        double larguraMaxima = 300;  // Largura máxima em cm
        double alturaMaxima = 200;   // Altura máxima em cm
        double profundidadeMaxima = 1000;  // Profundidade máxima em cm

        AlgoritmoGenetico meuAg = new AlgoritmoGenetico(populacao, limitePeso, larguraMaxima, alturaMaxima, profundidadeMaxima, probabilidadeMutacao, qtdeCruzamentos, numeroGeracoes);
        meuAg.carregaArquivo("carga_aviao.csv");
        meuAg.executar();
    }
}