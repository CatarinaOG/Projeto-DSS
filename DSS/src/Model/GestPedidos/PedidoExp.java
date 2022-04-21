package Model.GestPedidos;

import java.time.LocalDateTime;

public class PedidoExp extends Pedido{

    private String nomeServico;
    private int nrServico;
    private float preco;
    private int codFunc;

    /**
     * Construtor da subclasse PedidoExp
     * @param nifCliente nif do cliente
     * @param email email do cliente
     * @param nrTelemovel numero de telemovel
     * @param data data do pedido
     * @param idPedido identificador do pedido
     * @param servico tipo de servico a realizar
     * @param codFunc codigo do funcionario responsavel por realizar este pedido
     */
    public PedidoExp(String nifCliente, String email, int nrTelemovel, LocalDateTime data, int idPedido, int servico,int codFunc){
        super(nifCliente, email, nrTelemovel, data, idPedido);
        this.nrServico = servico;
        this.codFunc = codFunc;

        switch(servico){
            case 1: {
                this.preco = 30;
                this.nomeServico = "Trocar Ecra";
                break;
            }
            case 2:{
                this.preco = 15;
                this.nomeServico = "Instalar Sistema Operativo";
                break;
            }
        }
    }

    /**
     * Construtor copia da sub classe PedidoExp
     * @param pe Instancia de PedidoExp
     */
    public PedidoExp(PedidoExp pe){
        super(pe);
        this.nrServico = pe.getNrServico();
        this.nomeServico = pe.getNomeServico();
        this.preco = pe.getPreco();
    }

    /**
     * reponsavel por obter o tipo de serviço a realizar
     * @return retorna um inteiro que identifica o tipo de serviço a a realizar: 1 -> "trocar o Ecra" , 2 -> "Instalar o sistema operativo"
     */
    public int getNrServico(){
        return this.nrServico;
    }

    /**
     * obter o nome do serviço
     * @return nome do serviço
     */
    public String getNomeServico(){
        return this.nomeServico;
    }


    /**
     * metodo responsavel por obter o preço do menu
     * @return retorna o valor do preço do servico a realizar
     */
    public float getPreco() {
        return preco;
    }

    /**
     * Metodo responsavel por atualizar o codigo do funcionario que ira realizar o servico
     * @param codFunc codigo do funcionario que ira realizar o servico
     */
    public void setCodFunc( int codFunc){
        this.codFunc = codFunc;
    }

    /**
     * metodo responsavel por obter o codigo do funcionario que realiza o servico
     * @return
     */
    public int getCodFunc(){
        return this.codFunc;
    }

    public boolean equals(Object o) {
        return super.equals(o);
    }

    public PedidoExp clone(){
        return new PedidoExp(this);
    }

}
