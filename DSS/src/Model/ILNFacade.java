package Model;

import Model.Funcionarios.NaoExisteFunc;
import Model.GestPedidos.PedidoNaoExisteException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface ILNFacade {

    // Sistema

    void regularTimer();
    boolean login(int codFunc, String password, int type);

    // Atendedor

    void regPed(String nif, String email, int nrTelemovel, String problema);
    void regPedExpress(String nif, String email, int nrTelemovel, int servico) throws NaoExisteFunc;
    float getPreco(int idPedido);
    void registarLevantamento(int codPed) throws PedidoNaoExisteException;
    boolean temEquipamentosParaEntregar();
    List<Integer> getTodosAtend();

    // Tecnico

    int existeRepExp();
    int getPedidoMaisAntigo();
    String getInfoPedido(int idPedido);
    String getEmail(int idPedido,String map);
    void geraOrc(int pedido, List<List<String>> passosFornecidos, LocalDateTime prazo);
    void irreparavel(int idPedido);
    int pedidoMaisUrgente();
    List<List<String>> getPassosDescricao(int idPedido);
    int getPassoInicio(int IdPed);
    float getOrcPrevCusto(int idPedido);
    void setTecnicoOcupado(boolean state);
    void reparar(int idPedido, List<List<String>> input);
    String getDescricaoRepExp(int idPedidoExp);
    void repararExpresso(int idPedido);
    int getNrTelemovel(int idPedido);
    void confirmaOrc(int idPedido);
    void rejeitaOrc (int idPedido);
    boolean temReparacoes();
    boolean temPedidosParaOrcamentar();
    boolean temOrcamentosParaResponder();
    boolean existeTec();
    List<Integer> getTodosTec();
    List<Integer> getOrcamentados();

    // Gestor

    int novoFuncionario(Map.Entry<String, String> cred);
    Map<Integer,List<Integer>> tecnicoStats();
    Map<Integer, List<Integer>> atendStats();
    Map<Integer, List<String>> tecHist();


    void saveToObjFile(String filename) throws FileNotFoundException, IOException;

}
