import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

/**
  Copyright (C) Jan 2019 Félix Brunet - All rights reserved
*/

public class javascriptAstFragmentVisCl implements defsInt {


    javascriptParseTableCl astTable = null;

    private int threshold;

    public void initParams(javascriptParseTableCl parAstTable, int pthreshold) {
        threshold = pthreshold;
        astTable = parAstTable;
        return;
    }

    private List<Frag> mergeFrag(List<List<Frag>> listOfSubFrag) {
        List<Frag> listFrag = new ArrayList<>();
        Frag incompleteFrag = new Frag(astTable);
        for(List<Frag> subFrag : listOfSubFrag) {
            listFrag.addAll(subFrag.subList(0, subFrag.size() -1));
            incompleteFrag.nodesId.addAll(subFrag.get(subFrag.size() -1).nodesId);
        };
        listFrag.add(incompleteFrag);
        return listFrag;
    }

    private List<Frag> visitChildren(Integer astNodeId) {
        List<List<Frag>> listOfSubFrag = new ArrayList<>();
        List<Entry<Integer, String>> children = astTable.succTable.get(astNodeId);
        if (children != null) {
            for (Entry<Integer, String> childEntry: children) {
                listOfSubFrag.add(visit(childEntry.getKey()));
            }
        }
        return mergeFrag(listOfSubFrag);
    }

    /////////////////////////////////////////////////////

    private List<Frag> visit_default(Integer astNodeId) {
        List<Frag> listFrag = visitChildren(astNodeId);
        listFrag.get(listFrag.size()-1).nodesId.add(astNodeId);
        return listFrag;
    }

    private List<Frag> visit_fragment(Integer astNodeId) {
        List<Frag> listFrag = visitChildren(astNodeId);
        Frag incompleteFrag = listFrag.get(listFrag.size()-1);
        incompleteFrag.nodesId.add(astNodeId);
        if(incompleteFrag.nodesId.size() >= threshold) {
            //new fragment
            listFrag.add(new Frag(astTable));
        }
        return listFrag;
    }

    /////////////////////////////////////////////////////

    public List<Frag> visit(Integer astNodeId) {
        List<Frag> retVal;
        String curType = astTable.getType(astNodeId);
        if (curType == null) {
            System.err.println("ERROR: missing node " +
                       astNodeId);
            System.exit(1);
        }

        switch (curType) {
            case "FunctionDeclaration":
            case "FunctionExpression":
            case "ArrowFunctionExpression":
                retVal = visit_fragment(astNodeId);
                break;
            default:
                retVal = visit_default(astNodeId);
                break;
        }
        return retVal;
    }


}
