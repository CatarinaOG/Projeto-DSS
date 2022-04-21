package View;

import java.util.List;
import java.util.Map;

public interface IView {

    // Sistema

    void printMessage(String s);
    Map.Entry<Integer, String> getCredenciais();


    // Atendedor

    List<String> getPed(boolean pedidoNormal);
    int getServico();
    String getCodPed();


    // Tecnico

    boolean reparavel();
    List<String> getPasso(int passo);
    boolean novoPasso();
    String getPrazo();
    List<String> getResults(List<String> descricoes, int passo);
    boolean continuarRep();
    void avisarRepExp(int idPedido, String descricao);
    boolean estaReparado();
    Map.Entry<Integer, Integer> registarRespCliente();
    Map.Entry<String, String> getPassAtend();
    void showPedOrcamentados(List<Integer> l);

    // Gestor

    void showTecStats(Map<Integer,List<Integer>> tecStats);
    void showAtendStats(Map<Integer, List<Integer>> atendStats);
    void showTecHist(Map<Integer, List<String>> tecHist);
    void showCod(int cod);
    void printCodAtends(List<Integer> l);
    void printCodTecnicos(List<Integer> l);
}
