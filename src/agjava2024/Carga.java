package agjava2024;

public class Carga {
    private String descricao;
    private double peso;
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
        return "Carga{" + "descricao=" + descricao + ", peso=" + peso + ", largura=" + largura + ", altura=" + altura + ", profundidade=" + profundidade + '}';
    }
}