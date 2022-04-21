package Model.GestPedidos;

import java.io.Serializable;
import java.time.LocalDateTime;

public abstract class Pedido implements Serializable {
    private String nifCliente;
    private String email;
    private int nrTelemovel;
    private LocalDateTime data;
    private int idPedido;
    private int codTecnico;

    /**
     * Construtor da super classe pedido
     * @param nifCliente nif do cliente
     * @param email email do cliente
     * @param nrTelemovel numero de telemovel do cliente
     * @param data data de registo do pedido
     * @param idPedido identificador do pedido
     */
    public Pedido(String nifCliente, String email, int nrTelemovel, LocalDateTime data, int idPedido){
        this.nifCliente = nifCliente;
        this.email = email;
        this.nrTelemovel = nrTelemovel;
        this.data = data;
        this.idPedido = idPedido;
    }

    /**
     * Construtor de copia da super classe pedido
     * @param p instancia de pedido
     */
    public Pedido(Pedido p){
        this.nifCliente = p.getNifCliente();
        this.email = p.getEmail();
        this.nrTelemovel = p.getNrTelemovel();
        this.data = p.getData();
        this.idPedido = p.getIdPedido();

    }

    /**
     * Método responsavel por obter o nif de um cliente
     * @return nif do cliente sob o formato de string
     */
    public String getNifCliente() {
        return nifCliente;
    }

    /**
     * Metodo reponsavel por obter o email do cliente
     * @return email do cliente
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Metodo responsavel por obter o numero de telemovel do clinete
     * @return numero de telemovel do cliente
     */
    public int getNrTelemovel() {
        return this.nrTelemovel;
    }

    /**
     * Metodo responsavel por obter a data de um pedido
     * @return data de um pedido
     */
    public LocalDateTime getData() {
        return data;
    }

    /**
     * Metodo reponsavel por obter o identificador de um pedido
     * @return identificador de um pedido
     */
    public int getIdPedido() {
        return idPedido;
    }

    /**
     * metodo responsavel por obter o codigo do tecnico que realizou o pedido
     * @return codigo do tecnico que realizou o pedido
     */
    public int getCodTecnico(){
        return this.codTecnico;
    }

    /**
     * Atualizar o codigo do tecnico que realizou o pedido
     * @param codTecnico codigo do tecnico que realizou o pedido
     */
    public void setCodTecnico(int codTecnico){
        this.codTecnico = codTecnico;
    }

    public String toString() {
        return "Pedido{" +
                ", nifCliente='" + nifCliente + '\'' +
                ", email='" + email + '\'' +
                ", nrTelemovel=" + nrTelemovel +
                ", data=" + data +
                ", idPedido='" + idPedido + '\'' +
                '}';
    }

    public boolean equals(Object o){
        if (o == this) return true;
        if ((o == null) || (o.getClass() != this.getClass())) return false;

        Pedido a = (Pedido) o;

        return (a.getIdPedido() == this.idPedido);
    }

    public abstract Pedido clone();

}
