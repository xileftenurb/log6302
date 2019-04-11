import java.util.*;


public class javascriptFragmentAnalyserCl {

    javascriptParseTableCl astTable = null;

    public void initParams(javascriptParseTableCl parAstTable) {
        astTable = parAstTable;
        return;
    }

    public Map<String, Integer> getTypeNoeud(Frag fragment) {
        Map<String, Integer> mapNoeud = new HashMap<>();
        for(Integer noNoeud : fragment.nodesId) {
            String currentType = astTable.getType(noNoeud);
            Integer currentNbr = mapNoeud.get(currentType);
            if(currentNbr == null) {
                currentNbr = 0;
            }
            mapNoeud.put(currentType, currentNbr+1);
        }
        return mapNoeud;
    }

    public enum algoDistance {
        manhatan,
        manhatanNormal,
        euclidien
    }

    public List<Clone> getCloneTypeNoeud(List<Frag> fragments, algoDistance algo, double theshold) {
        //pour tous les fragments
        List<List<Integer>> dataVecs = new ArrayList<>();
        Map<String, Integer> indexMap = new HashMap<>();
        int vecSize = 0;
        for(Frag frag : fragments) {
            List<Integer> newVec = new ArrayList<>(Collections.nCopies(vecSize, 0));
            Set<Map.Entry<String, Integer>> nodeNumSet = getTypeNoeud(frag).entrySet();
            for(Map.Entry<String, Integer> node : nodeNumSet) {
                Integer index = indexMap.get(node.getKey());
                if(index == null) {
                    indexMap.put(node.getKey(), vecSize);
                    newVec.add(node.getValue());
                    vecSize++;
                } else {
                    newVec.set(index, node.getValue());
                }

            }
            dataVecs.add(newVec);
        }

        return getClone(fragments, dataVecs, algo, theshold);
    }

    public List<Clone> getCloneDataVec(List<Frag> fragments, algoDistance algo, double theshold) {
        //pour tous les fragments
        List<List<Integer>> dataVecs = new ArrayList<>();
        for(Frag frag : fragments) {
            dataVecs.add(getDataVec(frag));
        }

        return getClone(fragments, dataVecs, algo, theshold);
    }

    private List<Clone> getClone(List<Frag> fragments, List<List<Integer>> dataVecs, algoDistance algo, double theshold) {
        List<Clone> clones = new ArrayList<>();

        for(int i = 0; i < dataVecs.size(); i++) {
            for(int j = i + 1; j < dataVecs.size(); j++) {
                double distance = getDistance(dataVecs.get(i), dataVecs.get(j), algo);
                if(distance <= theshold) {
                    clones.add(new Clone(astTable, fragments.get(i), fragments.get(j), distance));
                }
            }
        }

        return clones;
    }

    private double getDistance(List<Integer> vec1, List<Integer> vec2, algoDistance algo) {
        if(algo == algoDistance.manhatan) {
            return getDistanceManhatan(vec1, vec2);
        } else if(algo == algoDistance.manhatanNormal) {
            return getDistanceManhatanNormal(vec1, vec2);
        } else if(algo == algoDistance.euclidien) {
            return getDistanceEucledienne(vec1, vec2);
        }
        return 0;
    }

    public List<Integer> getDataVec(Frag fragment) {

        List<Integer> vec = new ArrayList<>();
        Map<String, Integer> nbrTypeNoeud = getTypeNoeud(fragment);
        vec.add(getComplexiterCyclomatique(nbrTypeNoeud));
        vec.add(getNombreVar(nbrTypeNoeud));
        vec.add(getNombreFctCall(nbrTypeNoeud));
        vec.add(getNombreLiteral(nbrTypeNoeud));
        return vec;
    }

    public double getDistanceManhatan(List<Integer> vec1, List<Integer> vec2) {
        int distance = 0;
        int size1 = vec1.size();
        int size2 = vec2.size();
        int max = Math.max(size1, size2);
        for(int i = 0; i < max; i++) {
            int val1 = i < size1 ? vec1.get(i) : 0;
            int val2 = i < size2 ? vec2.get(i) : 0;
            distance += Math.abs(val1 - val2);
        }
        return distance;
    }

    public double getDistanceManhatanNormal(List<Integer> vec1, List<Integer> vec2) {
        int distance = 0;
        int size1 = vec1.size();
        int size2 = vec2.size();
        int max = Math.max(size1, size2);
        for(int i = 0; i < max; i++) {
            int val1 = i < size1 ? vec1.get(i) : 0;
            int val2 = i < size2 ? vec2.get(i) : 0;
            int current = Math.abs(val1 - val2);
            if(Math.max(val1, val2) != 0) {
                distance += current / Math.max(val1, val2);
            }
        }
        return distance;
    }

    public double getDistanceEucledienne(List<Integer> vec1, List<Integer> vec2) {
        double distance = 0;
        int size1 = vec1.size();
        int size2 = vec2.size();
        int max = Math.max(size1, size2);
        for(int i = 0; i < max; i++) {
            int val1 = i < size1 ? vec1.get(i) : 0;
            int val2 = i < size2 ? vec2.get(i) : 0;
            distance += Math.pow(val1 - val2, 2);
        }
        return Math.sqrt(distance);
    }

    //je fais l'hypothèse que toute fonction inline est appeller dans le même fragment qu'il est défini en moyenne, de sorte
    //que la complexité du fragment entier est lier à la complexité de ses fonctions inline.
    private int getComplexiterCyclomatique(Map<String, Integer> nbrTypeNoeud) {
        return getNombreBranchement(nbrTypeNoeud) + getNombreWhile(nbrTypeNoeud) + 1;
    }

    private int getNombreBranchement(Map<String, Integer> nbrTypeNoeud) {

        return z(nbrTypeNoeud.get("IfStatement")) + z(nbrTypeNoeud.get("SwitchCase")) +
                z(nbrTypeNoeud.get("ConditionalExpression")) + z(nbrTypeNoeud.get("LogicalExpression"));
    }


    private int getNombreWhile(Map<String, Integer> nbrTypeNoeud) {
        return z(nbrTypeNoeud.get("WhileStatement")) + z(nbrTypeNoeud.get("ForInStatement")) +
                z(nbrTypeNoeud.get("ForOfStatement")) + z(nbrTypeNoeud.get("ForStatement"));
    }

    private int getNombreVar(Map<String, Integer> nbrTypeNoeud) {
        return z(nbrTypeNoeud.get("VariableDeclaration"));
    }

    private int getNombreFctCall(Map<String, Integer> nbrTypeNoeud) {
        return z(nbrTypeNoeud.get("CallExpression"));
    }

    private int getNombreLiteral(Map<String, Integer> nbrTypeNoeud) {
        return z(nbrTypeNoeud.get("Literal")) + z(nbrTypeNoeud.get("TemplateLiteral"));
    }

    private int z(Integer o) {
        return o != null ? o : 0;
    }
}
