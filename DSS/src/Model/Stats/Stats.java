package Model.Stats;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Stats implements IStats, Serializable {

    Map<Integer, List<String>> tecHist;
    Map<Integer, List<Integer>> atendStats; //List tem o formato RecepçõesNormais, RecepçõesExpress e Entregas
    Map<Integer,List<Integer>> tecReps; //codTecnico -> total de reparações, media do tempo de reparação, media do desvio, (total de pedidos normais ) -> temporario

    /**
     * Construtor da classe Stats
     */
    public Stats(){
        this.tecHist = new HashMap<>();
        this.atendStats = new HashMap<>();
        this.tecReps = new HashMap<>();
    }

    /**
     * Metodo responsavel por criar um novo funcionario
     * @param tipoFunc tipo de funcionario
     * @param codFunc codigo do funcionario
     */
    public void novoFunc(String tipoFunc, int codFunc){
        switch (tipoFunc){
            case "Atendedor":
                List<Integer> l = new ArrayList<>();
                for(int i = 0; i < 3; i++) l.add(0);
                this.atendStats.put(codFunc, l);
                break;
            case "Tecnico":
                //
                break;
        }
    }

    /**
     * Método que incrementa uma dada estatística do Atendedor.
     * @param codFunc código do funcionário
     * @param index indíce da estatística a incrementar
     */
    public void incStatsAtend(int codFunc, int index){
        if(atendStats.containsKey(codFunc)){
            List<Integer> stats = atendStats.get(codFunc);
            int stat = stats.get(index);
            stat++;
            stats.set(index,stat);
            atendStats.put(codFunc, stats);
        }
    }

    /**
     * Dado o código do funcionário e a lista de descrições de passos que realizou, vai ser adicionado ao seu histórico
     * @param codFunc códgio do funcionário que queremos acrescentar histórico
     * @param l lista de descrições dos passos que queremos adicionar ao funcionario
     */
    public void addHist(int codFunc, List<String> l){
        List<String> hist;

        if(tecHist.containsKey(codFunc)) {
            hist = tecHist.get(codFunc);
            for(String s : l)
                hist.add(s);

        }else{
            hist = new ArrayList<>();
            for(String s : l)
                hist.add(s);
        }
        tecHist.put(codFunc,hist);
    }

    /**
     * Método para calcular a lista 1 da parte do gestor do enunciado
     * @param repPorTecnico
     */
    public void tecnicoRepStats(List<List<Integer>> repPorTecnico){
        int codTecnico,tempoRep,desvioRep,totalTempo,totalDesvio,totalRep,totalPedidoNormal;
        List<Integer> tecnicoRepStat;
        for(List<Integer> p : repPorTecnico){

            codTecnico = p.get(0);
            if(p.size() == 3){ //pedido normal;
                tempoRep = p.get(1);
                desvioRep = p.get(2);
                if(!this.tecReps.containsKey(codTecnico)){
                    tecnicoRepStat = new ArrayList<>();
                    tecnicoRepStat.add(1);//total de reparações
                    tecnicoRepStat.add(tempoRep);//tempo total de reparações
                    tecnicoRepStat.add(desvioRep);//desvio total de reparações
                    tecnicoRepStat.add(1);//total de reparações programadas.
                    this.tecReps.put(codTecnico,tecnicoRepStat);//atualizar o mapa com nova informação
                }else{
                    tecnicoRepStat = this.tecReps.get(codTecnico);
                    totalRep = tecnicoRepStat.get(0);
                    totalRep++;
                    tecnicoRepStat.set(0,totalRep);//atualizar o total de reparações
                    totalTempo = tecnicoRepStat.get(1);//tirar o total do tempo ate agora
                    totalTempo += tempoRep;
                    tecnicoRepStat.set(1,totalTempo); //atualizar o total do tempo
                    totalDesvio = tecnicoRepStat.get(2);//tirar o total de desvio ate agora
                    totalDesvio += desvioRep;
                    tecnicoRepStat.set(2,totalDesvio);//atualizar o total do desvio
                    totalPedidoNormal = tecnicoRepStat.get(3);
                    totalPedidoNormal++;
                    tecnicoRepStat.set(3,totalPedidoNormal); //atualizar o total de pedidos normais
                    this.tecReps.put(codTecnico,tecnicoRepStat);//atualizar o mapa com a nova informação
                }

            }else{

                if(!this.tecReps.containsKey(codTecnico)){
                    tecnicoRepStat = new ArrayList<>();
                    tecnicoRepStat.add(1);//total de reparações
                    tecnicoRepStat.add(0);//tempo total de reparações
                    tecnicoRepStat.add(0);//desvio total de reparações
                    tecnicoRepStat.add(0);//total de reparações programadas.
                    this.tecReps.put(codTecnico,tecnicoRepStat);

                }else{
                    tecnicoRepStat = this.tecReps.get(codTecnico);
                    totalRep = tecnicoRepStat.get(0); //tirar o total de reparações
                    totalRep++;
                    tecnicoRepStat.set(0,totalRep);
                    this.tecReps.put(codTecnico,tecnicoRepStat);
                }
            }
        }

        for(Map.Entry<Integer,List<Integer>> tecnicoStat : this.tecReps.entrySet()){
            List<Integer> l = tecnicoStat.getValue();
            if(l.get(3) != 0 ){
                totalTempo = l.get(1);
                totalDesvio = l.get(2);
                totalPedidoNormal = l.get(3);
                System.out.println(totalTempo);
                System.out.println(totalPedidoNormal);
                l.set(1,totalTempo/totalPedidoNormal); //substituir o total de tempo pela media
                l.set(2,totalDesvio/totalPedidoNormal); //substituir o total de desvio pela media
            }
            l.remove(3); //remover o numero total de pedidos normais que era uma variavel auxiliar para o calculo da media
            this.tecReps.put(tecnicoStat.getKey(),l);
        }
        
    }


    public Map<Integer, List<Integer>> getAtendStats(){
        return this.atendStats;
    }

    public Map<Integer,List<Integer>> getTecReps(){
        return this.tecReps;
    }

    public Map<Integer, List<String>> getTecHist(){
        return this.tecHist;
    }

}

