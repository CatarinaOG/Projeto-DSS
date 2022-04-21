package Model.Funcionarios;

public class Atendedor extends Funcionario{

    /**
     * Construtor da classe atendedor
     * @param codFunc codigo que identifica um atendedor
     * @param password password para autenticação de um atendedor
     */
    public Atendedor (int codFunc , String password){
        super(codFunc, password);
    }

    /**
     * Construtor cópia da SubClasse atendedor
     * @param t insancia da SubClasse atendedor
     */
    public Atendedor(Atendedor t){
        super(t);
    }

    /**
     * Método equals da subclasse Atendedor
     * @param o
     * @return
     */
    public boolean equals(Object o){
        return super.equals(o);
    }

    /**
     * Metodo clone da subclasse Atendedor
     * @return
     */
    public Atendedor clone(){
        return new Atendedor(this);
    }

    /**
     * Metodo toString que chama o toString da superClasse
     * @return
     */
    public String toString() {
        return super.toString();
    }
}
