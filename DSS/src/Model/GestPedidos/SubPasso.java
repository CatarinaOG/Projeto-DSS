package Model.GestPedidos;

import java.io.Serializable;

public class SubPasso implements Serializable {

    private String descricao;
    private int prevTempo;   //MINUTOS
    private float prevCusto;
    private int tempo;
    private float custo;

    /**
     * Construtor da classe SubPasso
     * @param descricao descriçao do subpasso
     * @param prevTempo previsao do tempo em que se irá realizar este subpasso
     * @param prevCusto previsao do custo em que se ira realizar este subpasso
     */
    public SubPasso (String descricao, int prevTempo, float prevCusto){
        this.descricao = descricao;
        this.prevTempo = prevTempo;
        this.prevCusto = prevCusto;
    }

    public SubPasso(SubPasso sp){
        this.descricao = sp.getDescricao();
        this.prevTempo = sp.getPrevTempo();
        this.prevCusto = sp.getPrevCusto();
        this.tempo = sp.getTempo();
        this.custo = sp.getCusto();
    }

    /**
     * Método usado para obter a descrição de um subpasso
     * @return descriçao de um subpasso
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Método responsavel por obter a previsao de tempo para realizar um passo
     * @return retorna a previsao de tempo para realizar um passo
     */
    public int getPrevTempo(){
        return this.prevTempo;
    }

    /**
     * Método responsavel por obter o custo previsto de um subpasso
     * @return custo previsto do subpasso
     */
    public float getPrevCusto(){
        return this.prevCusto;
    }

    /**
     * Método que retorna o tempo real de realização de um subpasso
     * @return tempo real para a realização de um subpasso
     */
    public int getTempo(){
        return this.tempo;
    }

    /**
     * Método que retorna o custo real de um subpasso
     * @return custo real de um subpasso
     */
    public float getCusto(){
        return this.custo;
    }

    /**
     * atualizar o tempo real para a realização de um subpasso
     * @param tempo tempo de realização de um subpasso
     */
    public void setTempo(int tempo) {
        this.tempo = tempo;
    }

    /**
     * atualizar o custo real para a realização de um subpasso
     * @param custo custo real para a realização de um subpasso
     */
    public void setCusto(float custo) {
        this.custo = custo;
    }

    public SubPasso clone(){
        return new SubPasso(this);
    }

    public String toString() {
        return "SubPasso{" +
                "descricao='" + descricao + '\'' +
                ", prevTempo=" + prevTempo +
                ", prevCusto=" + prevCusto +
                ", tempo=" + tempo +
                ", custo=" + custo +
                '}';
    }
}
