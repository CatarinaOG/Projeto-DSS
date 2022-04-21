package Model.GestPedidos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PedidoNormal extends Pedido{

    private String problema;
    private Orcamento orcamento;

    public PedidoNormal(String nifCliente, String email, int nrTelemovel, LocalDateTime data, int idPedido, String problema){
        super(nifCliente,email,nrTelemovel, data, idPedido);
        this.problema = problema;
        this.orcamento = null;
    }

    public PedidoNormal(PedidoNormal p){
        super(p);
        this.orcamento = p.getOrcamento();
        this.problema = p.getProblema();
    }


    /**
     * Devolvera uma lista de listas, cujas listas interiores representaram descrições de subpassoss
     * e a lista exterior a descrições de passos
     * @return
     */
    public List<List<String>> getPassosDescricao(){
        List<List<String>> res = new ArrayList<>();
        List<Passo> passos = this.orcamento.getPassos();
        for(Passo p : passos){
            List<String> descricaoPasso = new ArrayList<>();
            descricaoPasso.add(p.getDescricao());

            if(p.containsSubPassos()){
                List<String> descSubPassos = p.getSubPassos().stream().map(sp -> sp.getDescricao()).collect(Collectors.toList());
                descricaoPasso.addAll(descSubPassos);
            }
            res.add(descricaoPasso);
        }

        return res;
    }

    /**
     * Devolvera uma lista de descrições unicamente dos passos
     * @return
     */
    public List<String> getDescricoes(){
        return this.orcamento.getDescricoes();
    }

    /**
     * Retorna a duração da reparação que está presente no orçamento
     * @return
     */
    public float getTempoRep(){
        return this.orcamento.getTempoOrc();
    }

    /**
     * Retorna a previsao de tempo que esta presente no orcamento
     * @return
     */
    public float getPrevTempo(){
        return this.orcamento.getPrevTempoOrc();
    }

    public String getProblema(){return this.problema;}

    public Orcamento getOrcamento() {
        return this.orcamento;
    }

    public void setOrcamento(Orcamento orcamento) {
        this.orcamento = orcamento;
    }

    public PedidoNormal clone() {
        return new PedidoNormal(this);
    }

    /**
     * Returnará uma string, já com o formato desejado para informar o técnico do problema de um pedido
     * @return
     */
    public String toStringToGerOrc(){
        return "Pedido: "+super.getIdPedido()+" | Problema: "+this.problema;
    }

}
