import Model.ILNFacade;
import Model.LNFacade;
import Model.Funcionarios.NaoExisteFunc;
import Model.GestPedidos.PedidoNaoExisteException;
import View.Menu;
import View.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.*;
import java.util.Map;

public class App {
    private ILNFacade facade;
    private IView view;


    public App(){
        this.facade = new LNFacade();
        this.view = new View();
    }

    public void runApp(){
        try{
            this.facade = LNFacade.fromObjFile("Estado");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Não existe estado ainda. Depois da aplicação fechar, um novo estado será criado");
        }

        Thread timer = new Thread(()->{
            this.facade.regularTimer();
        });
        timer.run();


        //imprimir os funcionarios existentes e os seus codigos?
        //ou criar novos handlers que imprimem todos os Atendedores/Tecnicos antes de pedirem para introduzir o código e password


        String[] opcoes1 = {"Atendedor", "Tecnico","Gestor"};
        Menu menu1 = new Menu("Iniciar Sessão", opcoes1);

        menu1.setHandler(1, this::menuAtendedor);
        menu1.setHandler(2, this::menuTecnico);
        menu1.setHandler(3, this::menuGestor);
        menu1.run();
        try{
            this.facade.saveToObjFile("Estado");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Menus

    /**
     * Interface associada ao atendedor
     */
    public void menuAtendedor(){
        List<Integer> listaCod = this.facade.getTodosAtend();
        view.printCodAtends(listaCod);

        Map.Entry<Integer, String> credenciais = view.getCredenciais();

        String titulo = "Menu Atendedor";
        String[] opcoes = {"Registar Pedido Normal", "Registar Pedido Expresso", "Registar Levantamento do Equipamento"};
        Menu menuAtend = new Menu(titulo,opcoes);

        menuAtend.setPreCondition(1,() -> this.facade.existeTec());
        menuAtend.setPreCondition(2,() -> this.facade.existeTec());

        menuAtend.setHandler(1,this::registarPed);
        menuAtend.setHandler(2,this::registarPedExp);
        menuAtend.setHandler(3,this::registarLevantamento);

        menuAtend.setPreCondition(3,() -> this.facade.temEquipamentosParaEntregar());

        if(facade.login(credenciais.getKey(),credenciais.getValue(),1)) menuAtend.run();
        else view.printMessage("Credenciais Inválidas ");
    }

    /**
     * Interface associada ao tecnico
     */
    public void menuTecnico(){
        List<Integer> listaCod = this.facade.getTodosTec();
        view.printCodTecnicos(listaCod);

        Map.Entry<Integer, String> credenciais = view.getCredenciais();

        String titulo = "Menu Tecnico";
        String[] opcoes = {"Orcamentar", "Reparar", "Registo da Resposta de um Cliente"};
        Menu menuTecn = new Menu(titulo,opcoes);

        menuTecn.setHandler(1, this::geraOrc);
        menuTecn.setHandler(2, this::reparar);
        menuTecn.setHandler(3, this::registarRespostaCliente);

        menuTecn.setPreCondition(1,() -> this.facade.temPedidosParaOrcamentar());
        menuTecn.setPreCondition(2,() -> this.facade.temReparacoes());
        menuTecn.setPreCondition(3, () -> this.facade.temOrcamentosParaResponder());

        if(facade.login(credenciais.getKey(), credenciais.getValue(),2))
            menuTecn.run();
        else view.printMessage("Credenciais inválidas");
    }

    public void menuGestor(){
        Map.Entry<Integer, String> credenciais = view.getCredenciais();

        String titulo = "Menu Gestor";
        String[] opcoes = {"Consultar performance dos técnicos de reparações", "Consultar histórico de cada atendedor", "Consultar histórico de cada técnico","Criar Funcionario"};
        Menu menuGest = new Menu(titulo, opcoes);

        menuGest.setHandler(1, this::consultarPerfTec);
        menuGest.setHandler(2, this::consultarHistAtend);
        menuGest.setHandler(3, this::consultarHistTec);
        menuGest.setHandler(4, this::criarFunc);

        if(facade.login(credenciais.getKey(), credenciais.getValue(),3)) menuGest.run();
        else view.printMessage("Credenciais inválidas");
    }


    // Atendedor

    public void registarPed(){

        List<String> pedido = view.getPed(true);
        int size = pedido.get(2).length();
        int telemovel = 0;
        try {
            telemovel = Integer.parseInt(pedido.get(2));
            if(size == 9)
                facade.regPed(pedido.get(0),pedido.get(1),telemovel,pedido.get(3));
            else view.printMessage("ERRO :: Introduza um número de telemóvel válido!");
        }catch (NumberFormatException e){
            view.printMessage("ERRO :: Introduza um número de telemóvel válido!");
        }

    }

    public void registarPedExp() {

        List<String> pedido = view.getPed(false);
        int size = pedido.get(2).length();
        int servico = view.getServico();
        int telemovel = 0;

        try{
            telemovel = Integer.parseInt(pedido.get(2));
            if(size == 9){
                facade.regPedExpress(pedido.get(0), pedido.get(1), telemovel, servico);
            }
            else view.printMessage("ERRO :: Introduza um número de telemóvel válido");
        }catch(NumberFormatException e){
            view.printMessage("ERRO :: Introduza um número de telemóvel válido");
        }catch(NaoExisteFunc e){
            view.printMessage("ERRO :: Não existem Técnicos disponíveis!");
        }

    }

    public void registarLevantamento(){
        String idPed = view.getCodPed();
        int id = 0;
        try{
            id = Integer.parseInt(idPed);
            Float preco = facade.getPreco(id);
            view.printMessage("Preço da reparação = " + preco + "€");
            facade.registarLevantamento(id);
        }
        catch(NumberFormatException e){
            view.printMessage("ERRO :: Introduza um código válido");
        }catch (PedidoNaoExisteException e){
            view.printMessage("ERRO:: O pedido com código " + id + " não existe!");
        }
    }


    // Tecnico

    public void geraOrc(){

        int idPedidoExp = facade.existeRepExp();
        if( idPedidoExp != -1 ){
            this.repararExp(idPedidoExp);
        }else {

            int idPedido = facade.getPedidoMaisAntigo();
            String infoPedido = facade.getInfoPedido(idPedido);
            String email = facade.getEmail(idPedido,"Recebido");

            view.printMessage(infoPedido);
            boolean reparavel = view.reparavel();

            if(reparavel) {

                int nrPasso = 0;
                List<List<String>> passos = new ArrayList<>();
                boolean novo = true;
                while (novo) {
                    List<String> passo = view.getPasso(nrPasso);
                    passos.add(passo);
                    nrPasso++;
                    novo = view.novoPasso();
                }

                String s = view.getPrazo();
                LocalDateTime prazo = LocalDateTime.parse(s);

                facade.geraOrc(idPedido, passos, prazo);
                view.printMessage("Mande orcamento para o cliente: (Email) "+email);
            }
            else{
                view.printMessage("Notifique cliente da impossibilidade de reparação: (Email) "+email);
                facade.irreparavel(idPedido);
            }
        }
    }

    public void reparar(){

        int idPedidoExp = facade.existeRepExp();
        if( idPedidoExp != -1 ){
            this.repararExp(idPedidoExp);
        }else {

            int idPedido = facade.pedidoMaisUrgente();
            List<List<String>> descricoes = facade.getPassosDescricao(idPedido);

            List<List<String>> passos = new ArrayList<>();
            boolean continuar = true;

            int nrPasso = facade.getPassoInicio(idPedido);
            float custoVerdadeiro = 0;
            float custoPrevisao = facade.getOrcPrevCusto(idPedido);

            facade.setTecnicoOcupado(true);

            while (nrPasso < descricoes.size() && continuar) {
                view.printMessage("nrPasso : " + nrPasso + " | size: " + descricoes.size());
                List<String> passo = view.getResults(descricoes.get(nrPasso), nrPasso + 1);
                passos.add(passo);

                custoVerdadeiro += Float.parseFloat(passo.get(1));

                view.printMessage("Custo previsto= " + custoPrevisao);
                view.printMessage("Custo alcançado= " + custoVerdadeiro);

                if (custoVerdadeiro > 1.2 * custoPrevisao) view.printMessage("!CUSTO EXCEDEU O CUSTO PREVISTO: NOTIFICAR CLIENTE!");

                nrPasso++;
                if(nrPasso != descricoes.size()) continuar = view.continuarRep();

            }

            facade.setTecnicoOcupado(false);

            if (nrPasso == descricoes.size()){
                view.printMessage("Reparação Concluida");
                String email = facade.getEmail(idPedido,"Reparado");
                view.printMessage("Mande email para o cliente: (Email) "+email);
            }
            facade.reparar(idPedido, passos);
        }
    }

    public void repararExp(int idPedidoExp){
        String descricao = facade.getDescricaoRepExp(idPedidoExp);
        view.avisarRepExp(idPedidoExp,descricao);
        boolean reparado = view.estaReparado();
        while(!reparado) {
            reparado = view.estaReparado();
            if (reparado) {
                facade.repararExpresso(idPedidoExp);
                int nrTelemovel = facade.getNrTelemovel(idPedidoExp);
                view.printMessage("Enviar notificacao de reparacao ao cliente: (Nr de telemovel) " + nrTelemovel);
            }
        }
    }

    public void registarRespostaCliente(){
        view.showPedOrcamentados(facade.getOrcamentados());
        Map.Entry<Integer, Integer> res = view.registarRespCliente();
        //saber quais os pedidos que podem ser respondidos
        if (res.getValue() == 1)
            facade.confirmaOrc(res.getKey());
        else facade.rejeitaOrc(res.getKey());
    }


    // Gestor

    public void criarFunc() {
        Map.Entry<String, String> input = view.getPassAtend();
        view.showCod(this.facade.novoFuncionario(input));
    }

    public void consultarPerfTec(){
        this.view.showTecStats(this.facade.tecnicoStats());
    }

    public void consultarHistAtend(){
        this.view.showAtendStats(this.facade.atendStats());
    }

    public void consultarHistTec(){
        this.view.showTecHist(this.facade.tecHist());
    }

}


