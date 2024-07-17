package agjava2024;

import java.io.*;
import java.util.*;

public class AGJava2024 {
    private int populacao = 20;
    private double limitePeso = 400; 
    private double larguraMaxima = 300; 
    private double alturaMaxima = 300; 
    private double profundidadeMaxima = 400; 
    private int probabilidadeMutacao = 5;
    private int qtdCruzamento = 5;
    private int numeroGeracoes = 10;

    public static void main(String[] args) {
        AGJava2024 meuAg = new AGJava2024();
        meuAg.executar();
    }

    public void executar() {
        AlgoritmoGenetico meuAg = new AlgoritmoGenetico(
            populacao, limitePeso, larguraMaxima, alturaMaxima, profundidadeMaxima, probabilidadeMutacao, qtdCruzamento, numeroGeracoes
        );

        meuAg.carregaArquivo("carga_aviao.csv");
        meuAg.executar();
    }
}
