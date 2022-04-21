package View;

import java.util.*;

public class View implements IView{
    private Scanner sc = new Scanner(System.in);


    // Sistema

    public void printMessage(String s){
        System.out.println(s);
    }

    /**
     * Método que pede as credencias do utilizador, fazendo print das mensagens necessárias.
     * Caso o utilizador insira um código inválido, o método imprime uma mensagem de erro e volta a pedir o código,
     * até ao utilizador fornecer um código válido.
     * @return Map.Entry com o código e a password do funcionário
     */
    public Map.Entry<Integer, String> getCredenciais(){
        int cod = 0;
        boolean done = false;
        while(!done){
            System.out.println("Introduza o seu código: ");
            String codigo = sc.nextLine();
            try{
                cod = Integer.parseInt(codigo);
                done = true;
            }catch (NumberFormatException e){
                System.out.println("ERRO :: Código inválido!");
            }
        }

        System.out.println("Introduza a password: ");
        String pass = sc.nextLine();
        return new AbstractMap.SimpleEntry<>(cod, pass);
    }


    // Menu

    /**
     * Método que imprime os códigos dos Atendedores
     * @param l lista com os códigos dos Atendedores
     */
    public void printCodAtends(List<Integer> l) {
        for (Integer i : l) {
            System.out.println("Atendedor: " + i);
        }
    }

    /**
     * Método que imprime os códigos dos Técnicos
     * @param l lista com os códigos dos Técnicos
     */
    public void printCodTecnicos(List<Integer> l){
        for(Integer i : l){
            System.out.println("Tecnico: " + i);
        }
    }


    // Atendedor

    /**
     * Método responsável por pedir os dados do pedido a registar. Começa por pedir o NIF, Email e telemovel, adicionando os dados
     * sob formato de string numa List pedido. Caso o pedido a registar seja normal, é ainda pedido ao Atendedor para introduzir a descriçao
     * do problema.
     * @param pedidoNormal boolean que indica se o pedido é um pedido normal ou não
     * @return List com os inputs do Atendedor
     */
    public List<String> getPed(boolean pedidoNormal){
        List<String> pedido = new ArrayList<>();

        System.out.println("Introduza o NIF do cliente: ");
        String nif = sc.nextLine();
        System.out.println("Introduza o email do cliente: ");
        String email = sc.nextLine();
        System.out.println("Introduza o telemovel do cliente: ");
        String telemovel = sc.nextLine();

        pedido.add(nif);
        pedido.add(email);
        pedido.add(telemovel);

        if(pedidoNormal) {
            System.out.println("Introduza o problema: ");
            String problema = sc.nextLine();
            pedido.add(problema);
        }

        return pedido;

    }

    /**
     * Método que pede ao Atendedor para escolher o serviço do Pedido Expresso.
     * @return int que indica o nº corresponde do serviço
     */
    public int getServico(){
        System.out.println("Escolha o servico: ");
        System.out.println("1- Trocar Ecra\n2- Instalar Sistema Operativo");
        return Integer.parseInt(sc.nextLine());
    }

    /**
     * Método que pede ao Atendedor para indicar o código do pedido
     * @return código do pedido sob formato de String
     */
    public String getCodPed(){

        System.out.println("Qual é o id do pedido?");
        return sc.nextLine();

    }


    // Tecnico

    /**
     * Método que pergunta ao Técnico se o aparelho é reparável ou não
     * @return true caso a resposta seja afirmativa
     */
    public boolean reparavel(){
        System.out.println("O aparelho é reparavel? (S/N)");
        String resp = sc.nextLine();
        String lResp = resp.toLowerCase(Locale.ROOT);
        if( lResp.equals("s") || lResp.equals("sim")) return true;
        else return false;
    }


    /**
     * Método que pede ao Técnico para preencher os dados sobre um passo de orçamento. Começa por imprimir qual o nº do passo.
     * De seguida pede a descrição do passo.Depois, pede a previsão de tempo e custo do passo descrito. Tanto para o tempo como o custo,
     * caso o Tecnico forneça inputs inválidos, imprime uma mensagem de erro e volta a pedir o dado até que o input seja válido.
     * De seguida imprime uma mensagem a inquirir sobre o nº de subpassos , executando um ciclo com o nº inserido de iterações em que o procedimento
     * é análogo ao dos passos
     * @param passo número do passo
     * @return List com as Strings do input do Tecnico
     */
    public List<String> getPasso(int passo){
        boolean done = false;
        int tempo_int;
        float custo_float;
        String tempo = null, custo = null;

        System.out.println("*** Passo " + (passo+1) + " ***");
        System.out.println("Descriçao: ");
        String descricao = sc.nextLine();
        while(!done){
            System.out.println("Tempo (em minutos) : ");
            tempo = sc.nextLine();
            try{
                tempo_int = Integer.parseInt(tempo);
                done = true;
            }catch (NumberFormatException e){
                this.printMessage("ERRO :: Introduza o tempo em minutos!");
            }

        }

        done = false;

        while(!done){
            System.out.println("Custo: ");
            custo = sc.nextLine();
            try{
                custo_float = Float.parseFloat(custo);
                done = true;
            }catch (NumberFormatException e){
                this.printMessage("ERRO :: Introduza um custo valido !");
            }
        }


        System.out.println("Quantos subpassos tem?");
        int subpassos = Integer.parseInt(sc.nextLine());
        String descricao2;

        //List<Map.Entry<Map.Entry<Descricao,Custo>,Tempo>>;
        List<String> l =  new ArrayList<>();
        l.add(descricao);
        l.add(tempo);
        l.add(custo);

        for(int i = 0 ; i < subpassos ; i++) {
            System.out.println("** Subpasso "+(i+1)+" **");
            System.out.println("Descriçao: ");
            descricao = sc.nextLine();
            done = false;
            while(!done){
                try{
                    System.out.println("Tempo (em minutos) : ");
                    tempo = sc.nextLine();
                    tempo_int = Integer.parseInt(tempo);
                    done = true;
                }catch(NumberFormatException e){
                    this.printMessage("Introduza o tempo em minutos!");
                }
            }

            done = false;
            while(!done){
                try{
                    System.out.println("Custo: ");
                    custo = sc.nextLine();
                    custo_float = Integer.parseInt(custo);
                    done = true;
                }catch(NumberFormatException e){
                    this.printMessage("Introduza o tempo em minutos!");
                }
            }

            l.add(descricao);
            l.add(tempo);
            l.add(custo);
        }

        return l;
    }


    /**
     * Método que pergunta ao Técnico se este pretende adicionar mais um passo
     * @return boolean representante da resposta do utilizador, true caso a resposta do mesmo seja afirmativa
     */
    public boolean novoPasso(){
        System.out.println("Deseja adicionar um passo? (S/N)");
        String resp2 = sc.nextLine();
        if(resp2.equals("S") || resp2.equals("s")) return true;
        else return false;
    }

    /**
     * Método que pergunta ao Técnico qual será a data de finalização , sendo esta indicada no formato YYYY-MM-DD
     * Essa pergunta será repetida até que a data fornecida seja válida
     * @return data de finalização sob o formato de String
     */
    public String getPrazo(){

        String ret;

        do {
            System.out.println("Qual será o dia de finalização? (Introduza no seguinte formato):");
            System.out.println("YYYY-MM-DD");
            ret = sc.nextLine();
        }while (ret.length() != 10);

        return ret + "T00:00:00";
    }

    /**
     * Método que pede ao Técnico para inserir os resultados da execução de um dado passo da reparação. Caso o passo em questão tenha subpassos,
     * é executado um ciclo que começa por imprimir a descrição do subpasso em questão, pedindo ao Técnico para indicar a duração real do subpasso e o seu custo
     * Acabados os subpassos, é impressa a descrição do passo em questão e perguntado ao Técnico qual a duração e custo real
     * @param descricoes List de String com os dados dos passos e (caso existam) subpassos
     * @param passo número do passo em questão
     * @return
     */
    public List<String> getResults(List<String> descricoes, int passo){

        List<String> results = new ArrayList<>();

        String tempo;
        String custo;
        int i_res = 2;

        results.add(0, "");
        results.add(1, "");

        System.out.println("***Passo " + passo + "***");

        for( int i=1 ; i<descricoes.size() ; i++, i_res+=2 ) {
            System.out.println("***SubPasso " + i + "***");
            System.out.println("Descricao: " + (descricoes.get(i)));
            System.out.println("Qual foi a duração (em minutos): ");
            tempo = sc.nextLine();
            results.add(tempo);
            System.out.println("Qual foi o custo: ");
            custo = sc.nextLine();
            results.add(custo);
        }

        System.out.println("Descricao Passo "+passo+" : "+descricoes.get(0));
        System.out.println("Qual foi a duração (em minutos): ");
        tempo = sc.nextLine();
        results.set(0, tempo);
        System.out.println("Qual foi o custo: ");
        custo = sc.nextLine();
        results.set(1, custo);

        return results;
    }

    /**
     * Método que pergunta ao Técnico se este pretende continuar com a reparação
     * @return
     */
    public boolean continuarRep(){
        System.out.println("Deseja continuar a reparação? (S/N)");
        String resp = sc.nextLine();
        if(resp.equals("S") || resp.equals("s")) return true;
        else return false;
    }

    /**
     * Método que avisa o técnico que este tem uma reparação expresso para efetuar,
     * imprimindo, juntamente com essa informação, o id do pedido e a descrição do pedido
     * @param idPedido
     * @param descricao
     */
    public void avisarRepExp(int idPedido, String descricao){
        System.out.println("Tem uma reparação expresso a fazer: ");
        System.out.println("Id do pedido = " + idPedido);
        System.out.println("Descrição do Pedido = " + descricao);
    }

    /**
     * Método que pergunta ao técnico se a reparção está completa
     * @return
     */
    public boolean estaReparado(){
        System.out.println("A reparacao esta completa? (S/N)");
        String resp = sc.nextLine();
        if(resp.equals("S") || resp.equals("s")) return true;
        else return false;
    }

    /**
     * Método que pergunta ao utilizador qual é o id do pedido a ser confirmado e regista a resposta do cliente
     * @return Map.Entry em que a key é o id inserido e a resposta (no formato integer)
     */
    public Map.Entry<Integer, Integer> registarRespCliente(){
        System.out.println("Qual é o pedido que quer confirmar?");
        int id = Integer.parseInt(sc.nextLine());
        System.out.println("Qual a resposta do cliente? Sim(1)/Não(2)");
        int res = Integer.parseInt(sc.nextLine());
        return new AbstractMap.SimpleEntry<>(id, res);
    }


    // Gestor
    /**
     * Método que pergunta ao Gestor qual é o tipo de funcionário que pretende registar, imprimindo uma mensagem de erro caso o input do Gestor seja inválido
     * e voltando a fazer a pergunta até que o input seja válido. No fim, pede ao Gestor para introduzir qual a password associada ao funcionário.
     * @return Map.Entry com o input do Gestor sob formato de string em que a key é o tipo de funcionário e o value a password
     */
    public Map.Entry<String, String> getPassAtend(){
        String tipoFunc = null;
        boolean done = false;
        while(!done){
            System.out.println("Que tipo de funcionário quer introduzir? (Tecnico/Atendedor)");
            tipoFunc = sc.nextLine();
            if(tipoFunc.equals("Atendedor") || tipoFunc.equals("Tecnico")) done = true;
            else System.out.println("ERRO :: Introduza um tipo válido de Funcionário (Tecnico/Atendedor)");
        }

        System.out.println("Introduza a palavra passe pretendida: ");
        String pass = sc.nextLine();
        return new AbstractMap.SimpleEntry<>(tipoFunc, pass);
    }

    /**
     * Método que faz a impressão das stats dos técnicos registados no sistema
     * @param tecStats Map com as estatísticas dos técnicos
     */
    public void showTecStats(Map<Integer,List<Integer>> tecStats){
        //cod -> totalRep, mediaTempoRep, mediaDesvioTempo
        for(Map.Entry<Integer, List<Integer>> entry: tecStats.entrySet()){
            System.out.println("Tecnico: " + entry.getKey());
            System.out.println("Total de reparações: " + entry.getValue().get(0));
            System.out.println("Média do tempo de todas as reparações: " + entry.getValue().get(1));
            System.out.println("Média do desvio do tempo de reparações em relação ao tempo previsto: " + entry.getValue().get(2) + "\n");
        }
    }

    /**
     * Método que faz a impressão das stats dos atendedores registados no sistema
     * @param atendStats Map com as estatísticas dos atendedores
     */
    public void showAtendStats(Map<Integer, List<Integer>> atendStats){
        //pedidos, pedidosexpresso, entregas
        for(Map.Entry<Integer, List<Integer>> entry : atendStats.entrySet()){
            System.out.println("Atendedor: " + entry.getKey());
            System.out.println("Número de pedidos normais realizados: " + entry.getValue().get(0));
            System.out.println("Número de pedidos expresso realizados: " + entry.getValue().get(1));
            System.out.println("Número de levantamentos: " + entry.getValue().get(2) + "\n");
        }
    }

    /**
     * Método que faz a impressão do histórico de cada Técnico, indicando o código de funcionário e a lista de serviços efectuados
     * @param tecHist
     */
    public void showTecHist(Map<Integer, List<String>> tecHist){

        for(Map.Entry<Integer, List<String>> entry : tecHist.entrySet()){
            System.out.print("Técnico: " + entry.getKey());
            System.out.print(" | Serviços: ");
            for(String servico : entry.getValue() )
             System.out.print(servico);
            System.out.print("\n");
        }
    }

    /**
     * Método que imprime o código do novo funcionário
     * @param cod código do novo funcionário
     */
    public void showCod(int cod){
        System.out.println("Código do novo funcionario: "+cod);
    }

    /**
     * Método que imprime todos os pedidos orçamentados
     * @param l
     */
    public void showPedOrcamentados(List<Integer> l){
        System.out.println("Pedidos orcamentados: ");
        for(Integer i : l){
            System.out.println("Código: " + i);
        }
    }

}
