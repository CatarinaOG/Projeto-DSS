package Model.GestPedidos;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Orcamento implements Serializable {
    private LocalDateTime data;
    private LocalDateTime prazo;
    private List<Passo> passos;
    private int inicio;

    public Orcamento(List<Passo> passos, LocalDateTime prazo, int inicio) {
        this.data = LocalDateTime.now();
        this.prazo = prazo;
        setPassos(passos);
        this.inicio = inicio;
    }


    public Orcamento(Orcamento o) { // onde se usa isto?
        this.passos = o.getPassos();
    }

    /**
     * Devolve o custo total da reparação
     * @return
     */
    public float getPrecoOrc() {
        float total = 0;
        for (Passo p : passos) {
            total += p.getCusto();
        }
        return total;
    }

    /**
     * Devolve a previsão do custo total da reparação
     * @return
     */
    public float getPrevPrecoOrc() {
        float total = 0;
        for (Passo p : passos) {
            total += p.getPrevCusto();
        }
        return total;
    }

    /**
     * Devolve a duração total da reparação
     * @return
     */
    public float getTempoOrc() {
        float total = 0;
        for (Passo p : passos) {
            total += p.getTempo();
        }
        return total;
    }

    /**
     * Devolve a previsão de tempo total da reparação
     * @return
     */
    public float getPrevTempoOrc() {
        float total = 0;
        for (Passo p : passos) {
            total += p.getPrevTempo();
        }
        return total;
    }

    /**
     * Devolve uma lista composta por todas as descrições de cada passo
     * @return
     */
    public List<String> getDescricoes() {
        return passos.stream().map(p -> p.getDescricao()).collect(Collectors.toList());
    }

    /**
     * Devolve uma lista de clones dos passos do orcamento
     * @return
     */
    public List<Passo> getPassos() {
        List<Passo> list = new ArrayList<>();
        for (Passo p : this.passos) {
            list.add(p.clone());
        }
        return list;
    }

    /**
     * Coloca como lista de passos do orcamento uma lista de clones apartir da fornecida
     * @param ps lista de passos
     */
    public void setPassos(List<Passo> ps) {
        this.passos = new ArrayList<>();
        for (Passo p : ps) {
            this.passos.add(p.clone());
        }
    }

    public int getInicio() {
        return inicio;
    }

    public void setInicio(int inicio) {
        this.inicio = inicio;
    }

    public LocalDateTime getData(){
        return this.data;
    }

    public LocalDateTime getPrazo() {
        return prazo;
    }

    public String toString() {
        return "Orcamento{" +
                ", passos=" + passos +
                '}';
    }

    public Orcamento clone() {
        return new Orcamento(this);
    }
}
