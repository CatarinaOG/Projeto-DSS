package Model.GestPedidos;

public class PedidoNaoExisteException extends Exception {
    public PedidoNaoExisteException(String errorMessage){
        super(errorMessage);
    }
}
