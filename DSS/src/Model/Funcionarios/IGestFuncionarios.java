package Model.Funcionarios;

import java.util.List;

public interface IGestFuncionarios {

    // Sistema
    boolean login(int codFunc, String password, int type);

    // Atendedor
    Integer verTecnicos();
    List<Integer> getTodosAtend();

    // Tecnico
    void tecOcupado(int codFunc, boolean state);

    List<Integer> getTodosTec();

    // Gestor
    int registaFuncionario(String tipoFunc, String password);

    boolean existeTec();

}
