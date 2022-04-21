package View;

import java.util.*;


public class Menu {

    public interface Handler {
        void execute();
    }
    public interface PreCondition {
        boolean validate();
    }
    private static Scanner is = new Scanner(System.in);


    private String titulo;                  // Titulo do menu (opcional)
    private List<String> opcoes;            // Lista de opções
    private List<PreCondition> disponivel;  // Lista de pré-condições
    private List<Handler> handlers;         // Lista de handlers


    public Menu() {
        this.titulo = "Menu";
        this.opcoes = new ArrayList();
        this.disponivel = new ArrayList();
        this.handlers = new ArrayList();
    }


    public Menu(String titulo, List<String> opcoes) {
        this.titulo = titulo;
        this.opcoes = new ArrayList(opcoes);
        this.disponivel = new ArrayList();
        this.handlers = new ArrayList();
        this.opcoes.forEach(s-> {
            this.disponivel.add(()->true);
            this.handlers.add(()->System.out.println("\nATENÇÃO: Opção não implementada!"));
        });
    }

    public Menu(List<String> opcoes) { this("Menu", opcoes); }

    public Menu(String titulo, String[] opcoes) {
        this(titulo, Arrays.asList(opcoes));
    }


    public Menu(String[] opcoes) {
        this(Arrays.asList(opcoes));
    }

    // Métodos de instância

    /**
     * Método que regista uma opção
     * @param name nome da opção
     * @param p pré condição associada a opção
     * @param h handler associado a opçao
     */
    public void option(String name, PreCondition p, Handler h) {
        this.opcoes.add(name);
        this.disponivel.add(p);
        this.handlers.add(h);
    }

    /**
     * Método que executa um Menu
     */
    public void run(){
        int op;
        do {
            show();
            op = readOption();
            // testar pré-condição
            if (op>0 && !this.disponivel.get(op-1).validate()) {
                System.out.println("Opção indisponível! Tente novamente.");
            } else if (op>0) {
                // executar handler
                this.handlers.get(op-1).execute();
            }
        } while (op != 0);
    }

    /**
     * Método que define a Pré Condição associada a opção com indice i
     * @param i indice da opção
     * @param b pré condição
     */
    public void setPreCondition(int i, PreCondition b) {
        this.disponivel.set(i-1,b);
    }

    /**
     * Método que define o handler de uma dada opção
     * @param i indice da opção
     * @param h handler
     */
    public void setHandler(int i, Handler h) {
        this.handlers.set(i-1, h);
    }

    /**
     * Método que faz a impressão do Menu
     */
    private void show() {
        System.out.println("\n *** "+this.titulo+" *** ");
        for (int i=0; i<this.opcoes.size(); i++) {
            System.out.print(i+1);
            System.out.print(" - ");
            System.out.println(this.disponivel.get(i).validate()?this.opcoes.get(i):"---");
        }
        System.out.println("0 - Sair");
    }


    /**
     * Método que faz a leitura da opção selecionada pelo utilizador, caso o input não seja válida, é indicada uma exceção
     * @return
     */
    private int readOption() {
        int op;

        System.out.print("Opção: ");
        try {
            String line = is.nextLine();
            op = Integer.parseInt(line);
        }
        catch (NumberFormatException e) { // Não foi inscrito um int
            op = -1;
        }
        if (op<0 || op>this.opcoes.size()) {
            System.out.println("Opção Inválida!!!");
            op = -1;
        }
        return op;
    }
}
