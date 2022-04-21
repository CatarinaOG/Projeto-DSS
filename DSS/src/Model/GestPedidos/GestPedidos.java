package Model.GestPedidos;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class GestPedidos implements IGestPedidos, Serializable {

    Map<Integer, Pedido> recebidos;    //pedidos recebidos por um atendedor
    Map<Integer, Pedido> orcamentados; //pedidos com orçamento feito
    Map<Integer, Pedido> arquivados;   //pedidos orcamentos que nao foram respondidos em 30 dias
    Map<Integer, Pedido> respondidos;  //pedidos em  espera de reparação
    Map<Integer, Pedido> reparados;    //pedidos com reparação concluida
    Map<Integer, Pedido> entregues;    //pedidos cujos equipamentos já foram entregues
    Map<Integer, Pedido> abandonados;  //pedidos cujos equipamentos nao foram levantados em 90 dias
    Map<Integer, Pedido> rejeitados;   //pedidos cujos orçamentos foram rejeitados e os equipamentos têm de ser levantados pelo cliente

    List<Integer> idsPedidosExistentes;

    /**
     * Construtor da classe GestPedidos
     */
    public GestPedidos(){
        this.recebidos    = new HashMap<>();
        this.arquivados   = new HashMap<>();
        this.respondidos  = new HashMap<>();
        this.orcamentados = new HashMap<>();
        this.reparados    = new HashMap<>();
        this.entregues    = new HashMap<>();
        this.abandonados  = new HashMap<>();
        this.rejeitados   = new HashMap<>();
        this.idsPedidosExistentes = new ArrayList<>();
    }


    // Sistema

    /**
     * Usado para verificar se não se passaram 30 dias depois da criação de um orçamento ou se nao passaram 90 dias desde a data de conclusao da reparação
     */
    public  void checkPedidos(){
        LocalDateTime dataAtual = LocalDateTime.now();

        for( Pedido p : this.orcamentados.values()){
            PedidoNormal pn = (PedidoNormal) p;
            LocalDateTime dataLimite = pn.getOrcamento().getData().plusDays(30);
            if(dataLimite.isBefore(dataAtual)){
                this.orcamentados.remove(p.getIdPedido());
                this.arquivados.put(p.getIdPedido(),p);
            }
        }


        for(Pedido p: this.reparados.values()) {
            LocalDateTime dataLimite = p.getData().plusDays(90);
            if (dataLimite.isBefore(dataAtual)) {
                this.reparados.remove(p.getIdPedido());
                this.abandonados.put(p.getIdPedido(), p);
            }
        }
    }


    // Atendedor

    /**
     * Metodo que regista um novo podido normal
     * @param nif nif do cliente
     * @param email email do cliente
     * @param nrTelemovel numero de telemovel do cliente
     * @param problema descrição do problema a reparar
     */
    public void regPed(String nif, String email, int nrTelemovel, String problema){
        LocalDateTime data = LocalDateTime.now();
        Random rand = new Random();
        int codPed = rand.nextInt(1000);

        while(this.idsPedidosExistentes.contains(codPed)){
            codPed = rand.nextInt(1000);
        }

        idsPedidosExistentes.add(codPed);

        this.recebidos.put(codPed, new PedidoNormal(nif, email, nrTelemovel, data, codPed,problema));

    }

    /**
     * Método usado para registar um pedido expresso
     * @param nif nif do cliente
     * @param email email do cliente
     * @param nrTelemovel numero de telemovel do cliente
     * @param servico tipo de serviço a realizar
     * @param codFunc codigo do funcionario que irá executar o serviço expresso
     */
    public void regPedExpress(String nif, String email, int nrTelemovel,int servico,int codFunc){

        LocalDateTime data = LocalDateTime.now();
        Random rand = new Random();
        int codPed = rand.nextInt(1000);

        while(this.idsPedidosExistentes.contains(codPed)){
            codPed = rand.nextInt(1000);
        }

        idsPedidosExistentes.add(codPed);

        this.respondidos.put(codPed, new PedidoExp(nif, email, nrTelemovel, data, codPed,servico,codFunc));

    }

    /**
     * Metodo que calcula o preço de uma reparação
     * @param idPedido id do pedido sobre o qual se pretende calcular o preço de reparaçao
     * @return valor total do preço da reparaçao
     */
    public float getPreco(int idPedido){
        Pedido p = this.reparados.get(idPedido);

        if( p.getClass().getName().equals("Model.GestPedidos.PedidoExp")) {
            PedidoExp pe = (PedidoExp) p;
            return pe.getPreco();
        }else {
            PedidoNormal pn = (PedidoNormal) p;
            return pn.getOrcamento().getPrecoOrc();
        }
    }

    /**
     * Método usado para registar o levantamento de um pedido, ou seja , a entrega do equipamento ao cliente
     * @param codPed codigo do pedido ao qual se quer proceder ao levantamento
     * @throws PedidoNaoExisteException No caso de o pedido nao existir é atirada uma exceção
     */
    public void registarLevantamento(int codPed) throws PedidoNaoExisteException{

        Pedido p = this.reparados.remove(codPed);
        if (p == null) p = this.rejeitados.remove(codPed);
        if(p == null) throw new PedidoNaoExisteException("Pedido não existe");

        this.entregues.put(p.getIdPedido(),p);
    }




    // Tecnico

    /**
     * Método que verifica se um dado funcionario tem de realizar alguma reparação expresso
     * @param codFunc codigo do funcionario sobre o qual se pretende realizar essa verificação
     * @return true caso o funcionario tenha reparações expresso a fazer, false caso contrario
     */
    public int existeRepExp(int codFunc){

        int id = -1;

        for( Pedido p : this.respondidos.values()){
            if( p.getClass().getName().equals("Model.GestPedidos.PedidoExp")){
                PedidoExp pe = (PedidoExp) p;
                if( pe.getCodFunc() == codFunc )
                    id = pe.getIdPedido();
            }
        }
        return id;
    }

    /**
     * Método que identifica qual o pedido mais antigo para ser orçamentado
     * @return retorna o id do pedido mais antigo para ser orçamentado
     */
    public Integer maisAntigo(){

        Pedido maisAntigo = (Pedido) this.recebidos.values().toArray()[0];

        for (Pedido p : this.recebidos.values()){
            if (p.getData().isBefore(maisAntigo.getData())){
                if(p.getClass().getName().equals("Model.GestPedidos.PedidoNormal"))
                    maisAntigo = p;
            }
        }
        return maisAntigo.getIdPedido();
    }

    /**
     * Método usado para retornar uma string para ser apresentada na interface do tecnico ao gerar o orçamento para o pedido
     * @param idPedido identificador do pedido
     * @return retorna uma string com a irformação do pedido , nomeadamente descrição e qual o problema a reparar
     */
    public String getInfoPedido(int idPedido){
        PedidoNormal pn = (PedidoNormal) this.recebidos.get(idPedido);
        return pn.toStringToGerOrc();
    }

    /**
     * Método que pesquisa o email de um pedido
     * @param idPedido codigo do pedido
     * @param estado identificador qual o estado do pedido sobre o qual se pretende saber o email
     * @return email do pedido
     */
    public String getEmail(int idPedido, String estado){
        if (estado.equals("Recebido")) return this.recebidos.get(idPedido).getEmail();
        else return this.respondidos.get(idPedido).getEmail();
    }

    /**
     * Método que irá gerar o orçamento e plano de trabalhos de um pedido, usando para isso metodos das classes Orcamento e Pedido
     * @param pedido identificador do pedido
     * @param passosFornecidos lista dos passos recolhidos da view
     * @param prazo prazo  para a realização da reparação
     */
    public void geraOrc(int pedido , List<Passo> passosFornecidos, LocalDateTime prazo){
        Orcamento orc = new Orcamento(passosFornecidos, prazo,0);
        PedidoNormal p = (PedidoNormal) this.recebidos.remove(pedido);
        p.setOrcamento(orc);

        this.orcamentados.put(pedido, p);
    }

    /**
     * Método usado para transferir um pedido para o map de pedi
     * @param idPedido
     */
    public void irreparavel(int idPedido){
        Pedido p = this.recebidos.remove(idPedido);
        this.reparados.put(idPedido,p);
    }

    public int pedidoUrgente(){

        PedidoNormal maisUrgente = null;
        for( Pedido p : respondidos.values()){
            if (p.getClass().getName().equals("Model.GestPedidos.PedidoNormal")) {
                if(maisUrgente == null) {
                    maisUrgente = (PedidoNormal) p;
                }

                PedidoNormal pn = (PedidoNormal) p;

                if(pn.getOrcamento().getPrazo().isBefore(maisUrgente.getOrcamento().getPrazo()))
                    maisUrgente = pn;
            }
        }
        return maisUrgente.getIdPedido();
    }

    /**
     * Método que devolve todos as descrições dos passos de um pedido em específico
     * @param idPedido ID do pedido em questão
     * @return Lista com a descrição de todos os passos e dos respetivos subpassos
     */
    public List<List<String>> getPassosDescricao(int idPedido){
        PedidoNormal p = (PedidoNormal) this.respondidos.get(idPedido);
        return p.getPassosDescricao();
    }

    /**
     * Método que devolve o número do passso em que uma reparação ficou (será 0 caso a reparação ainda não tenha sido iniciada
     * @param idPedido ID do pedido em questão
     * @return índice do passo onde a reparação foi interrompida (ou onde será iniciada)
     */
    public int getPassoInicio(int idPedido){
        PedidoNormal pn = (PedidoNormal) this.respondidos.get(idPedido);
        return pn.getOrcamento().getInicio();
    }

    /**
     * Método que retorna o custo previsto do orcamento de um dado pedido
     * @param idPedido ID do pedido em questão
     * @return custo previsto do orcamento em questão
     */
    public float getOrcPrevCusto(int idPedido){
        PedidoNormal pn = (PedidoNormal) this.respondidos.get(idPedido);
        return pn.getOrcamento().getPrevPrecoOrc();
    }

    /**
     * Método que, depois de obter os inputs certos do técnico, introduz o tempo/custo que cada passo e subpasso demoraram/custaram
     * @param idPedido ID do pedido que está a ser reparado
     * @param input Input dos tempos e custos dos passos que foram sendo introduzidos pelo técnico aquando da reparação
     * @param codTecnico Código do técnico que efetuou a reparação (será usado para atualizar as estatísticas)
     * @return true que indica que a operação foi bem sucedida
     */
    public boolean reparar(int idPedido, List<List<String>> input, int codTecnico){
        PedidoNormal pn = (PedidoNormal) this.respondidos.get(idPedido);
        pn.setCodTecnico(codTecnico);
        Orcamento orc = pn.getOrcamento();
        int antigoInicio;
        int inicio = orc.getInicio();
        List<Passo> passos = orc.getPassos();
        List<SubPasso> subpassos;
        Passo p;
        for(int i = inicio ; i < input.size() ; i++){
            subpassos = passos.get(i).getSubPassos();

            p = passos.get(i);
            p.setTempo(Integer.parseInt(input.get(i).get(0)));
            p.setCusto(Float.parseFloat(input.get(i).get(1)));
            passos.set(i,p);//novo

            for(int j=2 ; j<subpassos.size() ; j+=2){
                SubPasso sp = subpassos.get(j-2);
                sp.setTempo(Integer.parseInt(input.get(i).get(j)));
                sp.setCusto(Float.parseFloat(input.get(i).get(j+1)));
                subpassos.set(j-2,sp); //novo
            }
            p.setSubpassos(subpassos);//novo

        }
        antigoInicio = orc.getInicio(); //novo
        orc.setInicio(antigoInicio + input.size());

        int novoInicio = orc.getInicio();
        if(novoInicio == passos.size() ) {
            this.respondidos.remove(idPedido);
            this.reparados.put(idPedido, pn);
        }

        orc.setPassos(passos);//novo
        System.out.println(passos);

        return true;
    }

    /**
     * Dado um identificador do pedido Expresso, devolverá o descrição do problema do pedido
     * @param idPedidoExp identificador do pedido expresso
     * @return descrição do problema do pedido
     */
    public String getDescricaoRepExp(int idPedidoExp){
        PedidoExp pe = (PedidoExp) this.respondidos.get(idPedidoExp);
        return pe.getNomeServico();
    }

    /**
     * Dado um identificador do pedido expressom, vai remove-lo da lista de respondidos e coloca-lo
     * na lista de pedidos reparados
     * @param idPedido identificador do pedido expresso do qual queremos reparar
     */
    public void repararExp(int idPedido){
        Pedido p = this.respondidos.remove(idPedido);
        if(p!=null) this.reparados.put(p.getIdPedido(),p);
    }

    /**
     * Dado um identificador do pedido, se for um pedido Normal irá retornar uma lista de descrições
     * dos passos correspondentes a esse pedido. Se for um pedido Expresso, retornará apenas uma lista
     * com um único elemento, a descrição do problema
     * @param idPed identificador do pedido do qual queremos as descrições dos pedidos
     * @return lista de descrições
     */
    public List<String> getDescricao(int idPed){

        Pedido p = this.respondidos.get(idPed);
        if(p==null) p = this.reparados.get(idPed);
        List<String> l = new ArrayList<>();

        if( p.getClass().getName().equals("Model.GestPedidos.PedidoExp") ){
            PedidoExp pe = (PedidoExp) p;
            l.add(pe.getNomeServico());
        }
        else {
            PedidoNormal pn = (PedidoNormal) p;
            l = pn.getDescricoes();
        }

        return l;
    }

    /**
     * Dado um identificador do pedido, irá retornar o número de telefone a ele associado, que
     * estará presente na lista de pedidos reparados
     * @param idPedido identificador do pedido
     * @return número de telemovel
     */
    public int getNrTelemovel(int idPedido){
        return this.reparados.get(idPedido).getNrTelemovel();
    }

    /**
     * Dado um identificador do pedido normal, vai remove-lo da lista de pedidos orcamentados, e
     * coloca-lo na lista de pedidos respondidos
     * @param idPedido identificador do pedido
     */
    public void confirmaOrc (int idPedido){
        Pedido p = this.orcamentados.remove(idPedido);
        this.respondidos.put(idPedido, p);
    }

    /**
     * Dado um identificador do pedido normal, vai remove-lo da lista de pedidos orcamentados, e
     * coloca-lo na lista de pedidos rejeitados
     * @param idPedido identificador do pedido
     */
    public boolean rejeitaOrc(int idPedido){
        Pedido p;

        if ((p = this.recebidos.remove(idPedido)) == null) return false;
        this.rejeitados.put(idPedido, p);
        return true;
    }

    /**
     * Este método devolverá true se existirem reparações a realizar, e false caso contrario
     * @return booleano a confirmar se existem reparações a fazer
     */
    public boolean temReparacoes() {return this.respondidos.size()>0;}

    /**
     * Método responsável por verificar se existem pedidos para orçamentar
     * @return true se existirem, false caso contrario
     */
    public boolean temPedidosParaOrcamentar(){
        return this.recebidos.size() > 0;
    }

    /**
     * Método reponsável por verificar se existem orçamentos para responder
     * @return true se existirem, false caso contrário
     */
    public boolean temOrcamentosParaResponder(){
        return this.orcamentados.size() > 0;
    }

    /**
     * Método que verifica se existem equipamentos para entregar, isto é, pedidos sobre os quais é possivel fazer o levantamento
     * @return true no caso de existirem , false caso nao existam
     */
    public boolean temEquipamentosParaEntregar(){
        return this.reparados.size() > 0;
    }


    // Gestor

    /**
     * metodo que ira retornar uma lista de listas, estas ultimas terao o codigo do tecnico , o tempo de duração e o desvio do tempo previsto de uma reparação programada
     * No caso de se tratar de uma reparação expresso a lista tem apenas o codigo do tecnico.
     * @return
     */
    public List<List<Integer>> getReparados(){
        List<List<Integer>> res = new ArrayList<>();
        int codTecnico,tempoRep, tempoPrevRep;
        List<Integer> aux;
        for(Pedido p : this.reparados.values()){
            aux = new ArrayList<>();
            codTecnico = p.getCodTecnico();
            if(p.getClass().getName().equals("Model.GestPedidos.PedidoNormal")){ // se for um pedido normal são necessarios três campos
                PedidoNormal pn = (PedidoNormal) p;
                tempoRep = (int) pn.getTempoRep();
                tempoPrevRep = (int) pn.getPrevTempo();
                aux.add(codTecnico);//primeiro bem o codigo
                aux.add(tempoRep);//
                aux.add(tempoPrevRep - tempoRep);
            } else{ //caso nao seja um pedido normal é um pedido expresso e entao só é necessário o codigo do tecnico que o realizou
                aux.add(codTecnico);
            }
            res.add(aux);
        }
        return res;
    }

    /**
     * Metodo que retorna qual o tecnico que fez a reparação com o codigo de pedido codPed (que é passado como argumento).
     * @param codPed
     * @return
     */
    public int getCodTecnico(int codPed){
        Pedido p = this.reparados.get(codPed);
        return p.getCodTecnico();
    }

    /**
     * Método responvsavel por obter os ids dos pedidos orçamentados
     * @return lista com os ids dos pedidos orçamentados
     */
    public List<Integer> getOrcamentados(){
        return this.orcamentados.values().stream().map(Pedido::getIdPedido).collect(Collectors.toList());
    }


}
