
import java.util.Map.Entry;
import java.util.List;

/**
  Copyright (C) Jan 2019 Ettore Merlo - All rights reserved
*/

public class javascriptAstPrettyPrintVisCl implements defsInt {

    //int cfgNodeSeed = 0;
    //Set<Integer> visited = null;
    //Map<Integer, ArrayList<Integer>> adjTable = null;
    javascriptParseTableCl astTable = null;

    private int curIndent = 0;

    public void initParams(javascriptParseTableCl parAstTable) {
        //visited = parVisited;
        //adjTable = parAdjTable;
        astTable = parAstTable;
        return;
    }

    private String getIndentStr() {
        StringBuffer indentStr = new StringBuffer();
        for (int i = 0; i < curIndent; ++i) {
            indentStr.append("   ");
        }
        return indentStr.toString();
    }

    private void visitChildren(Integer astNodeId, int depth) {
        List<Entry<Integer, String>> children = astTable.succTable.get(astNodeId);
        if (children != null) {
            for (Entry<Integer, String> childEntry: children) {
                visit(childEntry.getKey(), depth);
            }
        }

        return;
    }

    /////////////////////////////////////////////////////

    private void visit_default(Integer astNodeId, int depth, String nodeType) {

    System.out.println(getIndentStr() +
                            nodeType +
                            " (" +
                            astNodeId +
                            " " +
                            depth+
                            ")");
        curIndent++;
        visitChildren(astNodeId, (depth + 1));
        curIndent--;
        return;
    }

    /////////////////////////////////////////////////////

    public Integer visit(Integer astNodeId, int depth) {
        Integer retVal = null;

        String curType = astTable.getType(astNodeId);
        if (curType == null) {
            System.err.println("ERROR: missing node " +
                       astNodeId);
            System.exit(1);
        }

        switch (curType) {
            default :
                visit_default(astNodeId, depth, curType);
                break;
        }
        return(retVal);
    }
}
