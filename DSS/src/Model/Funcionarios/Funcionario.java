package Model.Funcionarios;

import java.io.Serializable;

public abstract class Funcionario implements Serializable {

    private int codFunc;
    private String password;

    /**
     * Construtor da superclasse Funcionário
     * @param codFunc codigo que identifica um tecnico
     * @param password password do funcionario para se autenticar no sistema
     */
    public Funcionario(int codFunc , String password){
        this.codFunc = codFunc;
        this.password = password;
    }

    /**
     * Construtor cópia da superclasse funcionario
     * @param f instancia de funcionario
     */
    public Funcionario( Funcionario f){
        this.codFunc = f.getCodFunc();
        this.password = f.getPassword();
    }

    /**
     * Metodo que retorna o codigo do funcionario
     * @return codigo do funcionario
     */
    public int getCodFunc() {
        return codFunc;
    }

    /**
     * Metodo que atualiza o codigo do funcionario para o que é passado como argumento
     * @param codFunc codigo do funcionario
     */
    public void setCodFunc(int codFunc) {
        this.codFunc = codFunc;
    }

    /**
     * Método que retorna a password do funcionario
     * @return password do funcionario
     */
    public String getPassword() {
        return password;
    }

    public abstract Funcionario clone();


    public boolean equals(Object o ){
        if(this == o) return true;
        if ((o == null) || (o.getClass() != this.getClass())) return false;

        Funcionario f = (Funcionario) o;

        return f.getCodFunc() == this.codFunc;
    }

    public String toString() {
        return "Funcionario{" +
                ", codFunc=" + codFunc +
                ", password='" + password + '\'' +
                '}';
    }
}
