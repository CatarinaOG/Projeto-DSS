package Model;

import Model.Funcionarios.*;
import Model.Stats.*;
import Model.GestPedidos.*;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

public class LNFacade implements ILNFacade, Serializable{

    private IGestFuncionarios gFuncionarios;
    private IGestPedidos gPedidos;
    private IStats stats;
    private int codFuncionario;

    /**
     * Construtor da classe LNFacade. Cria a classe de gestão de funcionários, a classe da gestão de pedidos e da classe das estatísticas
     */
    public LNFacade(){
        this.gFuncionarios = new GestFuncionarios();
        this.gPedidos = new GestPedidos();
        this.stats = new Stats();
    }


    // Sistema

    /**
     * Método que verifica diariamente se algum pedido está fora do prazo
     */
    public void regularTimer(){

        TimerTask repeatedTask = new TimerTask() {
            public void run() {
                LNFacade.this.gPedidos.checkPedidos();
            }
        };

        Timer timer = new Timer("Timer");

        long delay = 1000L;
        long period = 1000L * 60L * 60L * 24L;
        timer.scheduleAtFixedRate(repeatedTask, delay, period);

    }

    /**
     * Método que recorre ao método da classe GestFuncionários para validar o login
     * @param codFunc Código do funcionário/gestor em questão
     * @param password Password do funcionário/gestor em questão
     * @param type Tipo do funcionário/gestor em questão
     * @return true se as credenciais forem válidas, false caso contrário
     */
    public boolean login(int codFunc, String password,int type){
        boolean success = gFuncionarios.login(codFunc,password,type);
        if(success) this.codFuncionario = codFunc;

        return success;
    }


    // Atendedor

    /**
     * Método que recorre ao método regPeg da classe GestPedidos para efetuar o registo de um novo pedido normal. Incrementa
     * também as estatísticas do atendedor que efetuou este pedido
     * @param nif NIF do cliente
     * @param email Email do cliente
     * @param nrTelemovel nrTelemovel do cliente
     * @param problema Problema do equipamento
     */
    public void regPed(String nif, String email, int nrTelemovel, String problema ){
        this.gPedidos.regPed(nif, email, nrTelemovel,problema);
        this.stats.incStatsAtend(this.codFuncionario,0);
    }

    /**
     * Método que recorre ao método regPedExpress da classe GestPedidos para efetuar o registo de um novo pedido Expresso.
     * Antes de o fazer, recorre ao método verTecnicos dessa mesma classe para verificar se o pedido expresso poderá mesmo ser executado
     * @param nif NIF do cliente
     * @param email Email do cliente
     * @param nrTelemovel nrTelemovel do cliente
     * @param servico Servico que será feito
     * @throws NaoExisteFunc Caso não existam técnicos disponíveis para efetuar o pedido expresso
     */
    public void regPedExpress(String nif, String email, int nrTelemovel, int servico) throws NaoExisteFunc{

        int cod = gFuncionarios.verTecnicos();
        if (cod == -1) throw new NaoExisteFunc();

        this.gPedidos.regPedExpress(nif, email, nrTelemovel,servico,cod);
        this.stats.incStatsAtend(this.codFuncionario,1);

    }

    /**
     * Método que recorre ao método getPreco do GestPedidos que devolve o preço de um determinado pedido expresso.
     * @param idPedido ID do pedido em questão
     * @return preço do pedido em questão
     */
    public float getPreco(int idPedido){
        return this.gPedidos.getPreco(idPedido);
    }

    /**
     * Método que recorre ao método registarLevantamento da GestPedidos para registar o levantamento de um equipamento.
     * Também incrementa o número de pedidos expresso registados pelo atendedor que o registou
     * @param codPed ID do pedido em questão
     * @throws PedidoNaoExisteException exceção que é enviada caso o pedido selecionado não esteja registado no sistema
     */
    public void registarLevantamento(int codPed) throws PedidoNaoExisteException{
        this.gPedidos.registarLevantamento(codPed);
        this.stats.incStatsAtend(this.codFuncionario, 2);
    }

    /**
     * Método que invoca o método temEquipamentosParaEntregar da classe gPedidos para verificar se exitem Equipamentos para Entregar
     * @return
     */
    public boolean temEquipamentosParaEntregar(){
        return this.gPedidos.temEquipamentosParaEntregar();
    }


    // Tecnico

    /**
     * Método que invoca o método existeRepExp da classe GestPedidos para verificar a existencia de reparações expresso a realizar pelo técnico em questão
     * @return
     */
    public int existeRepExp(){
        return this.gPedidos.existeRepExp(this.codFuncionario);
    }

    /**
     * Metodo que retorna o pedido mais antigo para orçamentar
     * @return identificador do pedido mais antigo
     */
    public int getPedidoMaisAntigo(){
        return gPedidos.maisAntigo();
    }

    /**
     * Metodo para obter a info de um pedido
     * @param idPedido identificador do pedido
     * @return info do pedido em forma de string
     */
    public String getInfoPedido(int idPedido){
        return gPedidos.getInfoPedido(idPedido);
    }

    /**
     * Metodo responsavel por obter o email de um cliente associado a um pedido
     * @param idPedido identificador de um pedido
     * @param status status do pedido
     * @return email do pedido
     */
    public String getEmail(int idPedido, String status){
        return this.gPedidos.getEmail(idPedido,status);
    }

    /**
     * Metodo reponsavel por gerar um orçamento e o plano de trabalhos
     * @param pedido identificador de um pedido
     * @param passosFornecidos passos recolhidos na view
     * @param prazo data prazo para a realização da reparação
     */
    public void geraOrc(int pedido, List<List<String>> passosFornecidos, LocalDateTime prazo) {

        List<Passo> passos = new ArrayList<>();
        List<SubPasso> subpassos;

        for( int i=0 ; i<passosFornecidos.size() ; i++ ){
            subpassos = new ArrayList<>();
            String descricao = passosFornecidos.get(i).get(0);
            int tempo = Integer.parseInt(passosFornecidos.get(i).get(1));
            float custo = Float.parseFloat(passosFornecidos.get(i).get(2));

            Passo p = new Passo(descricao,tempo,custo);

            for( int j = 3 ; j<passosFornecidos.get(i).size() ; j+=3 ){

                descricao = passosFornecidos.get(i).get(j);
                tempo = Integer.parseInt(passosFornecidos.get(i).get(j+1));
                custo = Float.parseFloat(passosFornecidos.get(i).get(j+2));

                SubPasso sp = new SubPasso(descricao, tempo, custo);
                subpassos.add(sp);
            }
            p.setSubpassos(subpassos);
            passos.add(p);
        }

        this.gPedidos.geraOrc(pedido,passos,prazo);
    }

    /**
     * Método que recorre ao método irreparavel da classe GestPedidos
     * @param idPedido ID do pedido em questão
     */
    public void irreparavel(int idPedido){
        this.gPedidos.irreparavel(idPedido);
    }

    /**
     * Método que recorre ao método pedidoUrgente da classe GestPedidos para conseguir o codigo do pedido mais urgente
     * @return código do pedido mais urgente
     */
    public int pedidoMaisUrgente(){
        return this.gPedidos.pedidoUrgente();
    }

    /**
     * Método que recorre ao método getPassosDescricao da classe GestPedidos
     * @param idPedido ID do pedido em questão
     * @return Lista com as descrições de todos os passos e dos respetivos subpassos
     */
    public List<List<String>> getPassosDescricao(int idPedido){
        return this.gPedidos.getPassosDescricao(idPedido);
    }

    /**
     * Método que recorre ao método getPassoInicio da classe GestPedidos
     * @param IdPed ID do pedido em questão
     * @return índice do passo onde deve ser começada (ou retomada) a reparação
     */
    public int getPassoInicio(int IdPed){
        return this.gPedidos.getPassoInicio(IdPed);
    }

    /**
     * Método responsavel por obter o custo previsto de um pedido
     * @param idePedido identificador de um pedido
     * @return custo previsto de um pedido
     */
    public float getOrcPrevCusto(int idePedido){
        return gPedidos.getOrcPrevCusto(idePedido);
    }

    /**
     * Metodo responsavel por atualizar o estado do tecnico
     * @param state estado do tecnico, true para ocupado , false para ocupado
     */
    public void setTecnicoOcupado(boolean state){
        gFuncionarios.tecOcupado(codFuncionario,state);
    }


    /**
     * Método que verifica, recorrendo à temReparacoe da classe GestPedidos, se existem pedidos para reparar
     * @return true se existirem, falso se não existirem
     */
    public boolean temReparacoes(){
        return this.gPedidos.temReparacoes();
    }

    /**
     * Método que verifica se existem pedidos para orcamentar
     * @return true se existirem, falso caso contrario
     */
    public boolean temPedidosParaOrcamentar(){
        return this.gPedidos.temPedidosParaOrcamentar();
    }

    /**
     * Metodo responsavel por verificar se existem orcamentos para responder
     * @return true se existirem , false caso contrario
     */
    public boolean temOrcamentosParaResponder(){
        return this.gPedidos.temOrcamentosParaResponder();
    }

    /**
     * Metodo responsavel por verificar se existem tecnicos
     * @return true caso existam , false caso contrario
     */
    public boolean existeTec(){
        return this.gFuncionarios.existeTec();
    }

    /**
     * Metodo usado para realizar a reparação de um pedido,  recebendo informações introduzidas pelo tecnico na view , como o custo e tempo real de cada passo
     * @param idPedido identificador de um pedido
     * @param input informações introduzidas pelo tecnico na view
     */
    public void reparar(int idPedido, List<List<String>> input) {
        this.gPedidos.reparar(idPedido, input, this.codFuncionario);
        List<String> descricoes = gPedidos.getDescricao(idPedido);
        System.out.println("descricoes: " + descricoes.toString());
        stats.addHist(this.codFuncionario, descricoes);
    }

    /**
     * Método que recorre ao método getDescricaoRepExp da classe GestPedidos.
     * @param idPedidoExp ID do pedido expresso em questão
     * @return descrição do pedido expresso em questão
     */
    public String getDescricaoRepExp(int idPedidoExp){
        return this.gPedidos.getDescricaoRepExp(idPedidoExp);
    }

    /**
     * Método que recorre ao método repararExp da classe GestPedidos. Obtém também as descrições dos passos e subpassos desta reparação
     * recorrendo ao método getDescrição da mesma classe. Por fim, usando o código do técnico que está a efetuar esta reparação e a lista
     * de descrições de passos e subpassos da reparação, recorrerá ao método addHist da classe stats para atualizar as estatísticas do técnico em questão.
     * @param idPedido ID do pedido a reparar
     */
    public void repararExpresso(int idPedido){
        gPedidos.repararExp(idPedido);
        List<String> descricoes = gPedidos.getDescricao(idPedido);
        stats.addHist(this.codFuncionario,descricoes);
    }

    /**
     * Metodo responsavel por obter o numero de telemovel associado a um pedido
     * @param idPedido identificador de um pedido
     * @return numero de telemovel
     */
    public int getNrTelemovel(int idPedido){
        return this.gPedidos.getNrTelemovel(idPedido);
    }

    /**
     * Metodo usado para confirmar um orçamento , ou seja , registar a confirmação de um cliente ao orçamento
     * @param idPedido identificador do pedido
     */
    public void confirmaOrc(int idPedido){
        this.gPedidos.confirmaOrc(idPedido);
    }

    /**
     * Metodo usado para registar a rejeiçao de um orçamento por parte de um cliente
     * @param idPedido identificador do pedido
     */
    public void rejeitaOrc (int idPedido){
        this.gPedidos.rejeitaOrc(idPedido);
    }


    // Gestor

    /**
     * Metodo responsavel por registar um novo funcionario
     * @param cred credenciais de acesso ao sistema
     * @return codigo do funcionario geriado pelo sistema
     */
    public int novoFuncionario(Map.Entry<String, String> cred){
        int cod = this.gFuncionarios.registaFuncionario(cred.getKey(), cred.getValue());
        this.stats.novoFunc(cred.getKey(), cod);
        return cod;
    }

    /**
     * Metodo para obter as listagens de performance dos tecnicos, associando a cada tecnico o numero de reparações feitas
     * o tempo medio das reparações programadas e d desvio medio do tempo estimado das reparações programadas.
     * @return retorna um mapa que a cada codigo identificador de um tecnico , associa uma lista com os três valores de performance referidos acima.
     */
    public Map<Integer,List<Integer>> tecnicoStats(){
        List<List<Integer>> aux = this.gPedidos.getReparados();
        this.stats.tecnicoRepStats(aux);
        return this.stats.getTecReps();
    }

    /**
     * Método que recorre ao método getAtendStats da classe Stats.
     * @return estrutura de dados com a informação pertinente
     */
    public Map<Integer, List<Integer>> atendStats(){
        return this.stats.getAtendStats();
    }

    /**
     * Método que recorre ao método getTecHist da classe Stats.
     * @return estrutura de dados com a informação das estatísticas dos técnicos
     */
    public Map<Integer, List<String>> tecHist(){
        return this.stats.getTecHist();
    }

    /**
     * Método que recorre ao método getTodosAtend da classe GestFuncionarios para construir a lista de códigos de todos os atendedores
     * registados no sistema.
     * @return Lista com todos os códigos
     */
    public List<Integer> getTodosAtend(){
        return this.gFuncionarios.getTodosAtend();
    }

    /**
     * Método que recorre ao método getTodosTec da classe GestFuncionarios para construir a lista de códigos de todos os técnicos
     * registados no sistema
     * @return Lista com todos os códigos
     */
    public List<Integer> getTodosTec(){
        return this.gFuncionarios.getTodosTec();
    }

    /**
     * Metodo responsavel por obter todos os pedidos que já tem um orçamento gerado
     * @return Lista de identificadores de pedido
     */
    public List<Integer> getOrcamentados(){
        return this.gPedidos.getOrcamentados();
    }

    /**
     * Metodo usado para guardar o estado da aplicação num ficheiro binario de objetos
     * @param filename nome do ficheiro
     * @throws FileNotFoundException exceção caso o ficheiro nao exista
     * @throws IOException
     */
    public void saveToObjFile(String filename) throws FileNotFoundException, IOException {
        FileOutputStream fos = new FileOutputStream(filename);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(this);
        oos.flush();
        oos.close();
    }

    /**
     * Metodo para ler o estado do programa num a partir de um ficheiro binario de objetos
     * @param filename nome do ficheiro
     * @return retorna uma instancia de ILNFacade
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static ILNFacade fromObjFile(String filename) throws IOException, ClassNotFoundException{
        FileInputStream fis = new FileInputStream(filename);
        ObjectInputStream ois = new ObjectInputStream(fis);
        ILNFacade lnf = (ILNFacade) ois.readObject();
        ois.close();
        return lnf;
    }

}
