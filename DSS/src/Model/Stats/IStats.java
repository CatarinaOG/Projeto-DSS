package Model.Stats;

import java.util.List;
import java.util.Map;

public interface IStats {

    void novoFunc(String tipoFunc, int codFunc);
    void incStatsAtend(int codFunc,int index);
    void addHist(int codFunc, List<String> l);
    void tecnicoRepStats(List<List<Integer>> repPorTecnico);
    Map<Integer,List<Integer>> getTecReps();
    Map<Integer, List<Integer>> getAtendStats();
    Map<Integer, List<String>> getTecHist();

}
