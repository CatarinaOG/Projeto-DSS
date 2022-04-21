package Model.GestPedidos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Passo implements Serializable {

    private String descricao;
    private int prevTempo; //MINUTOS
    private float prevCusto;
    private int tempo;
    private float custo;
    private List<SubPasso> subpassos;


    public Passo(String descricao, int prevTempo, float prevCusto) {
        this.descricao = descricao;
        this.prevTempo = prevTempo;
        this.prevCusto = prevCusto;
        this.tempo = 0;
        this.custo = 0;
        this.subpassos = new ArrayList<>();
    }

    public Passo(Passo p){
        this.descricao = p.getDescricao();
        this.prevTempo = p.getPrevTempo();
        this.prevCusto = p.getPrevCusto();
        this.tempo     = p.getTempo();
        this.custo     = p.getCusto();
        this.subpassos = p.getSubPassos();
    }


    /**
     * Devolve uma lista de clones dos subpassos
     * @return
     */
    public List<SubPasso> getSubPassos() {
        List<SubPasso> list = new ArrayList<>();
        for(SubPasso sp : this.subpassos){
            list.add(sp.clone());
        }
        return list;
    }

    /**
     * Coloca com subpassos, uma lista de clones da lista fornecida
     * @param subpassos lista de subpassos fornecida
     */
    public void setSubpassos(List<SubPasso> subpassos) {
        List<SubPasso> list = new ArrayList<>();
        for(SubPasso sp : subpassos){
            list.add(sp.clone());
        }
        this.subpassos = list;
    }

    /**
     * Devolve true se um passo possuir subpassos
     * @return
     */
    public boolean containsSubPassos(){
        return this.subpassos.size() > 0;
    }

    public String getDescricao(){
        return this.descricao;
    }

    public int getPrevTempo() {
        return prevTempo;
    }//não é preciso calcular o total dos tempos dos subspassos?

    public float getPrevCusto() {
        return prevCusto;
    }

    public int getTempo() {
        return tempo;
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
    }

    public float getCusto() {
        return custo;
    }

    public void setCusto(float custo) {
        this.custo = custo;
    }

    public Passo clone(){
        return new Passo(this);
    }

    public String toString() {
        return "Passo{" +
                "PrevTempo=" + prevTempo +
                ", PrevCusto=" + prevCusto +
                ", Tempo=" + tempo +
                ", custo=" + custo +
                '}';
    }
}
