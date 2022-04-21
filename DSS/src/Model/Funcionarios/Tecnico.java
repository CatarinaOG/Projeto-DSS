package Model.Funcionarios;

public class Tecnico extends Funcionario{
    private boolean ocupado;

    /**
     * Construtor da subclasse Tecnico
     * @param codFunc codigo que identifica um tecnico
     * @param password password que o tecnico usa para se autenticar no sistema
     * @param ocupado booleano que identifica se o tecnico esta ocupado ou nao
     */
    public Tecnico(int codFunc, String password, boolean ocupado){
        super(codFunc, password);
        this.ocupado = ocupado;
    }

    public Tecnico(Tecnico t){
        super(t);
        this.ocupado = t.isOcupado();
    }

    /**
     * Retorna o valor da variavel ocupado, usado para verificar a disponibilidade do tecnico
     * @return valor booleano da variavel ocupado
     */
    public boolean isOcupado() {
        return ocupado;
    }

    /**
     * atualizar a disponibilidade do tecnico
     * @param ocupado booleano que identifica se o tecnico está ou nao ocupado
     */
    public void setOcupado(boolean ocupado) {
        this.ocupado = ocupado;
    }

    public boolean equals(Object o){
        if (o == this) return true;
        if ((o == null) || (o.getClass() != this.getClass())) return false;

        Tecnico t = (Tecnico) o;
        return (super.equals(t) && this.ocupado == t.isOcupado());
    }

    public Tecnico clone(){
        return new Tecnico(this);
    }

    public String toString() {
        return "Tecnico{" +
                "codFunc=" + super.getCodFunc() +
                ", password='" + super.getPassword() + '\'' +
                "ocupado=" + ocupado +
                '}';
    }
}
