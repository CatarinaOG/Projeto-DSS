package Model.GestPedidos;

import java.time.LocalDateTime;
import java.util.List;

public interface IGestPedidos {

    // Sistema

    void checkPedidos();

    // Atendedor

    void regPed(String nif, String email, int nrTelemovel,String problema);
    void regPedExpress(String nif, String email, int nrTelemovel,int servico,int codFunc);
    float getPreco(int idPedido);
    void registarLevantamento(int codPed) throws PedidoNaoExisteException;

    // Tecnico

    int existeRepExp(int codFunc);
    Integer maisAntigo();
    String getInfoPedido(int idPedido);
    String getEmail(int idPedido, String map);
    void geraOrc(int pedido , List<Passo> passosFornecidos, LocalDateTime prazo);
    void irreparavel(int idPedido);
    int pedidoUrgente();
    List<List<String>> getPassosDescricao(int idPedido);
    int getPassoInicio(int idPedido);
    float getOrcPrevCusto(int idPedido);
    boolean reparar(int idPedido, List<List<String>> input, int codTecnico);
    String getDescricaoRepExp(int idPedidoExp);
    void repararExp(int idPedido);
    List<String> getDescricao(int idPed);
    int getNrTelemovel(int idPedido);
    void confirmaOrc(int idPedido);
    boolean rejeitaOrc(int idPedido);
    boolean temReparacoes();
    boolean temPedidosParaOrcamentar();
    boolean temOrcamentosParaResponder();
    boolean temEquipamentosParaEntregar();
    List<Integer> getOrcamentados();


    // Gestor

    List<List<Integer>> getReparados();


 }
