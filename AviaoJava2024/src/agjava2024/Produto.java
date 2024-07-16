package agjava2024;

public class Produto {
    private String descricao;
    private double peso;
    private double valor;
    private double largura;
    private double altura;
    private double profundidade;

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public double getLargura() {
        return largura;
    }

    public void setLargura(double largura) {
        this.largura = largura;
    }

    public double getAltura() {
        return altura;
    }

    public void setAltura(double altura) {
        this.altura = altura;
    }

    public double getProfundidade() {
        return profundidade;
    }

    public void setProfundidade(double profundidade) {
        this.profundidade = profundidade;
    }

    @Override
    public String toString() {
        return "Produto{Descricao: " + this.descricao + ", Peso: " + this.peso + ", Largura: " + this.largura + ", Altura: " + this.altura + ", Profundidade: " + this.profundidade + "}";
    }
}