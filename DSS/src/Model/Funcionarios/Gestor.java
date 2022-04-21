package Model.Funcionarios;

public class Gestor extends Funcionario{

    /**
     * Construtor da subclasse gestor
     * @param codFunc codigo de funcionario que identifica o gestor
     * @param password password usada para autenticar o gestor
     */
    public Gestor(int codFunc, String password){
        super(codFunc, password);
    }

    /**
     * Construtor de copia da subclasse gestor
     * @param g intanciad de gestor
     */
    public Gestor(Gestor g){
        super(g);
    }

    public boolean equals(Object o){
        return super.equals(o);
    }

    public Gestor clone(){
        return new Gestor(this);
    }

}
