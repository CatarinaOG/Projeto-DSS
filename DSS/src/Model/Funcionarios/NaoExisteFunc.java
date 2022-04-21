package Model.Funcionarios;

public class NaoExisteFunc extends Exception{
    public NaoExisteFunc(String message){
        super(message);
    }
    public NaoExisteFunc(){
        super();
    }
}
