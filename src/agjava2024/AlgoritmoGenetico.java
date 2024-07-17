package agjava2024;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class AlgoritmoGenetico {
    private int tamPopulacao;
    private int tamCarga = 0;
    private double capacidadePeso;
    private double larguraMaxima, alturaMaxima, profundidadeMaxima;
    private int probMutacao;
    private int qtdCruzamentos;
    private int numeroGeracoes;
    private ArrayList<Carga> cargas = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> populacao = new ArrayList<>();
    private ArrayList<Integer> roletaVirtual = new ArrayList<>();

    public AlgoritmoGenetico(int tamanhoPopulacao, double capacidadePeso, double larguraMaxima, double alturaMaxima, double profundidadeMaxima, int probabilidadeMutacao, int qtdCruzamentos, int numeroGeracoes) {
        this.tamPopulacao = tamanhoPopulacao;
        this.capacidadePeso = capacidadePeso;
        this.larguraMaxima = larguraMaxima;
        this.alturaMaxima = alturaMaxima;
        this.profundidadeMaxima = profundidadeMaxima;
        this.probMutacao = probabilidadeMutacao;
        this.qtdCruzamentos = qtdCruzamentos;
        this.numeroGeracoes = numeroGeracoes;
    }

    public void executar() {
        this.criarPopulacao();
        for (int i = 0; i < this.numeroGeracoes; i++) {
            operadoresGeneticos();
            novoPopulacao();
        }
        ArrayList<Integer> melhor = obterMelhor();
        System.out.println("Melhor Cromossomo: " + melhor);
        System.out.println("Avaliação do Melhor Cromossomo: " + fitness(melhor));

        // Mostrar as cargas selecionadas para o avião
        mostrarCargasSelecionadas(melhor);
    }

    public void carregaArquivo(String fileName) {
        String csvFile = fileName;
        String line = "";
        String[] carga = null;
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                carga = line.split(",");
                Carga novaCarga = new Carga();
                novaCarga.setDescricao(carga[0]);
                novaCarga.setPeso(Double.parseDouble(carga[1]));
                novaCarga.setLargura(Double.parseDouble(carga[2]));
                novaCarga.setAltura(Double.parseDouble(carga[3]));
                novaCarga.setProfundidade(Double.parseDouble(carga[4]));
                cargas.add(novaCarga);
                this.tamCarga++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Integer> criarCromossomo() {
        ArrayList<Integer> novoCromossomo = new ArrayList<>();
        for (int i = 0; i < this.tamCarga; i++) {
            if (Math.random() < 0.6)
                novoCromossomo.add(0);
            else
                novoCromossomo.add(1);
        }
        return novoCromossomo;
    }

    private void criarPopulacao() {
        for (int i = 0; i < this.tamPopulacao; i++)
            this.populacao.add(criarCromossomo());
    }

    private double fitness(ArrayList<Integer> cromossomo) {
        double pesoTotal = 0;
        double volumeTotal = 0;
        boolean penalizacao = false;

        for (int i = 0; i < this.tamCarga; i++) {
            if (cromossomo.get(i) == 1) {
                Carga carga = cargas.get(i);
                if (carga.getLargura() > this.larguraMaxima || carga.getAltura() > this.alturaMaxima || carga.getProfundidade() > this.profundidadeMaxima) {
                    penalizacao = true;
                }
                pesoTotal += carga.getPeso();
                volumeTotal += carga.getLargura() * carga.getAltura() * carga.getProfundidade();
            }
        }

        if (penalizacao || pesoTotal > this.capacidadePeso) {
            return 0;
        }
        return volumeTotal;
    }

    private void gerarRoleta() {
        ArrayList<Double> fitnessIndividuos = new ArrayList<>();
        double totalFitness = 0;
        for (int i = 0; i < this.tamPopulacao; i++) {
            double fitnessValue = fitness(this.populacao.get(i));
            fitnessIndividuos.add(fitnessValue);
            totalFitness += fitnessValue;
        }

        for (int i = 0; i < this.tamPopulacao; i++) {
            double qtdPosicoes = (fitnessIndividuos.get(i) / totalFitness) * 1000;
            for (int j = 0; j <= qtdPosicoes; j++)
                roletaVirtual.add(i);
        }
    }

    private int roleta() {
        Random r = new Random();
        int selecionado = r.nextInt(roletaVirtual.size());
        return roletaVirtual.get(selecionado);
    }

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
        int corte = (int) (Math.random() * this.tamCarga);
        for (int i = 0; i < corte; i++) {
            filho1.add(pai1.get(i));
            filho2.add(pai2.get(i));
        }
        for (int i = corte; i < this.tamCarga; i++) {
            filho1.add(pai2.get(i));
            filho2.add(pai1.get(i));
        }
        filhos.add(filho1);
        filhos.add(filho2);
        return filhos;
    }

    private void mutacao(ArrayList<Integer> cromossomo) {
        for (int i = 0; i < this.tamCarga; i++) {
            if (Math.random() < this.probMutacao) {
                if (cromossomo.get(i) == 1)
                    cromossomo.set(i, 0);
                else
                    cromossomo.set(i, 1);
            }
        }
    }

    private void operadoresGeneticos() {
        gerarRoleta();
        for (int i = 0; i < this.qtdCruzamentos; i++) {
            ArrayList<ArrayList<Integer>> filhos = cruzamento();
            mutacao(filhos.get(0));
            mutacao(filhos.get(1));
            populacao.add(filhos.get(0));
            populacao.add(filhos.get(1));
        }
    }

    private int obterPior() {
        int indicePior = 0;
        double pior = fitness(populacao.get(0));
        for (int i = 1; i < this.tamPopulacao; i++) {
            double nota = fitness(populacao.get(i));
            if (nota < pior) {
                pior = nota;
                indicePior = i;
            }
        }
        return indicePior;
    }

    protected ArrayList<Integer> obterPiorCromossomo() {
        return populacao.get(obterPior());
    }

    protected ArrayList<Integer> obterMelhor() {
        int indiceMelhor = 0;
        double melhor = fitness(populacao.get(0));
        for (int i = 1; i < this.tamPopulacao; i++) {
            double nota = fitness(populacao.get(i));
            if (nota > melhor) {
                melhor = nota;
                indiceMelhor = i;
            }
        }
        return populacao.get(indiceMelhor);
    }

    private void novoPopulacao() {
        for (int i = 0; i < this.qtdCruzamentos; i++) {
            populacao.remove(obterPior());
            populacao.remove(obterPior());
        }
    }

    // Método para mostrar as cargas selecionadas pelo melhor cromossomo
    public void mostrarCargasSelecionadas(ArrayList<Integer> melhorCromossomo) {
        System.out.println("Cargas selecionadas para o avião:");
        double pesoTotal = 0;
        double volumeTotal = 0;

        for (int i = 0; i < tamCarga; i++) {
            if (melhorCromossomo.get(i) == 1) {
                Carga carga = cargas.get(i);
                System.out.println("Descrição: " + carga.getDescricao());
                System.out.println("Peso: " + carga.getPeso());
                System.out.println("Largura: " + carga.getLargura());
                System.out.println("Altura: " + carga.getAltura());
                System.out.println("Profundidade: " + carga.getProfundidade());
                System.out.println("************************");

                pesoTotal += carga.getPeso();
                volumeTotal += carga.getLargura() * carga.getAltura() * carga.getProfundidade();
            }
        }

        System.out.println("Peso Total: " + pesoTotal + " Kg");
        System.out.println("Volume Total: " + volumeTotal + " m³");
    }

}
