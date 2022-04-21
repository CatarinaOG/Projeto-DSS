package Model.Funcionarios;

import java.io.Serializable;
import java.util.*;

public class GestFuncionarios implements IGestFuncionarios, Serializable {

    private Map<Integer,Funcionario> funcionarios;
    private Gestor g;

    /**
     * Construtor da classe GestFuncionarios. Esta inicializa a sua variável de instância Map funcionários e cria um gestor, que será necessário para criar novos Funcionários
     */
    public GestFuncionarios(){
        this.funcionarios = new HashMap<>();
        g = new Gestor(1,"g");
    }

    // Sistema

    /**
     * Método que valida o login do funcionário ou gestor
     * @param cod Código do funcionário que efetuou o login
     * @param password Password do funcionário que efetuou o login
     * @param type Tipo do funcionário que efetuou o login
     * @return Boolean que transmite se o login foi feito com sucesso ou não
     */
    public boolean login(int cod, String password, int type){

        boolean ret;

        if( type == 1){
            ret = funcionarios.containsKey(cod) && funcionarios.get(cod).getPassword().equals(password) && funcionarios.get(cod).getClass().getName().equals("Model.Funcionarios.Atendedor");
        }else if( type == 2){
            ret = funcionarios.containsKey(cod) && funcionarios.get(cod).getPassword().equals(password) && funcionarios.get(cod).getClass().getName().equals("Model.Funcionarios.Tecnico");
        }else
            ret = (cod == g.getCodFunc() && password.equals(g.getPassword()));

        return ret;
    }

    // Atendedor

    /**
     * Método que verifica a disponibilidade dos técnicos para a realização de uma reparação expresso
     * @return Código do primeiro funcionário disponível encontrado ou -1 caso nenhum funcionário esteja disponível
     */
    public Integer verTecnicos(){
        Iterator<Funcionario> it = this.funcionarios.values().iterator();
        boolean done = false;
        int ret = -1;

        while (it.hasNext() && !done){
            Funcionario f = it.next();
            if (f.getClass().getName().equals("Model.Funcionarios.Tecnico")){
                Tecnico t = (Tecnico) f;
                if (!t.isOcupado()){
                    ret = t.getCodFunc();
                    done = true;
                }
            }
        }
        return ret;
    }


    // Tecnico

    /**
     * Método que altera o estado de ocupação de um técnico em específico
     * @param codFunc Código do funcionário que se quer alterar
     * @param state Novo estado do técnico
     */
    public void tecOcupado(int codFunc, boolean state){
        Tecnico t = (Tecnico) this.funcionarios.get(codFunc);
        t.setOcupado(state);
    }


    // Gestor

    /**
     * Método de registo de um novo funcionário
     * @param tipoFunc Tipo do funcionário a registar
     * @param password Passoword que vai ficar associada ao funcionário
     * @return código do novo funcionário
     */
    public int registaFuncionario(String tipoFunc, String password){
        Random rand = new Random();
        int novoCod = rand.nextInt(1000);

        while (this.funcionarios.containsKey(novoCod)){
            novoCod = rand.nextInt();
        }

        Funcionario f = null;
        switch(tipoFunc){
            case "Atendedor":
                f = new Atendedor(novoCod, password);
                break;
            case "Tecnico":
                f = new Tecnico(novoCod, password,false);
                break;
        }
        this.funcionarios.put(novoCod, f);
        return novoCod;
    }

    /**
     * Método que verifica se existem técnicos registados no sistema
     * @return true se existirem técnicos registados e false caso contrário
     */
    public boolean existeTec(){
        for(Funcionario f : this.funcionarios.values()){
            if(f.getClass().getName().equals("Model.Funcionarios.Tecnico")) return true;
        }
        return false;
    }

    /**
     * Método que armazena numa lista os códigos de todos os atendedores registados no sistema
     * @return lista com todos os códigos
     */
    public List<Integer> getTodosAtend(){
        List<Integer> list = new ArrayList<>();
        for(Funcionario f : this.funcionarios.values()){
            if(f.getClass().getName().equals("Model.Funcionarios.Atendedor")) list.add(f.getCodFunc());
        }
        return list;
    }

    /**
     * Método que armazena numa lista os códigos de todos os técnicos registados no sistema
     * @return lista com todos os códigos
     */
    public List<Integer> getTodosTec(){
        List<Integer> list = new ArrayList<>();
        for(Funcionario f : this.funcionarios.values()){
            if(f.getClass().getName().equals("Model.Funcionarios.Tecnico")) list.add(f.getCodFunc());
        }
        return list;
    }


}
