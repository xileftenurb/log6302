    import java.util.*;

    import java.io.IOException;



    /**
    Copyright (C) 2017, 2018, 2019 Ettore Merlo - All rights reserved
    */

    public class javascriptParseMain {

    public static void dfsTraverse(Integer nodeId,
                   Set<Integer> visited,
                   Map<Integer, ArrayList<Integer>> adjTable) {


    // set discovered
    visited.add(nodeId);

    // visit current node


    if (adjTable.containsKey(nodeId)) {
        for (Integer childId:
             globalOptionsCl.test("srtTraversal", "true") ?
                sortUtilsCl
                    .getSortedIterable_Integer(adjTable.get(nodeId)) :
                adjTable.get(nodeId)) {


            if (!visited.contains(childId)) {

                System.out.println("NEXT TRANS: " + nodeId + " " + childId);

                //data.parent = id;
                dfsTraverse(childId, visited, adjTable);
            } else {

            }
        }
    }

    return;
    }

    public static void main(String args[]) {

        javascriptParseTableCl astTable = new javascriptParseTableCl();
        tableReaderCl tableReader = new tableReaderCl();

        try {
            globalOptionsCl.read("options.dat");
        } catch (IOException e) {
            System.out.println("ERROR: cannot open options.dat");
            System.exit(1);
        }

        if (globalOptionsCl.test("printOptions", "true")) {
            globalOptionsCl.print();
        }
        tableReader.initParams(astTable);

        if (args.length != 1) {
            System.err.println("ERROR: missing filename");
            System.exit(1);
        }

        //String tableStr = "tripleTable.tab";
        String tableStr = args[0];

        //
        // parse Option
        //

        String fragThresholdString = globalOptionsCl.get("fragThreshold");
        int fragThreshold = 20;
        if(!fragThresholdString.equals(""))
            fragThreshold = Integer.valueOf(fragThresholdString);

        String cloneThresholdString = globalOptionsCl.get("cloneThreshold");
        int cloneThreshold = 100;
        if(!cloneThresholdString.equals(""))
            cloneThreshold = Integer.valueOf(cloneThresholdString);

        String algoDistanceString = globalOptionsCl.get("algoDistance");
        javascriptFragmentAnalyserCl.algoDistance algo = javascriptFragmentAnalyserCl.algoDistance.manhatan;
        switch(algoDistanceString){
            case "manhatan" :
                algo = javascriptFragmentAnalyserCl.algoDistance.manhatan;
                break;
            case "manhatanNormal" :
                algo = javascriptFragmentAnalyserCl.algoDistance.manhatanNormal;
                break;
            case "euclidien" :
                algo = javascriptFragmentAnalyserCl.algoDistance.euclidien;
                break;
        }

        String algoDataVec = globalOptionsCl.get("algoDataVec");
        boolean useAlgoDataVec = true;
        if(algoDataVec.equals("true")) {
            useAlgoDataVec = true;
        } else {
            useAlgoDataVec = false;
        }


        //
        // read table
        //

        tableReader.read(tableStr);

        if(globalOptionsCl.get("printTable").equals("true")) {
            astTable.print();
        }

        //
        // get AST root
        //

        Integer rootNodeId = null;

        if (!astTable.attrTable.containsKey("ast_root")) {
            System.out.println("ERROR: undefined attribute \"ast_root\"");
            System.exit(1);
        }
        try {
            rootNodeId = Integer.valueOf(astTable.attrTable.get("ast_root"));
        } catch (NumberFormatException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.err.println("ERROR: invalid parent integer " +
                               astTable.attrTable.get("ast_root"));
            System.exit(1);
        }

        if(globalOptionsCl.get("printTree").equals("true")) {
            javascriptAstPrettyPrintVisCl javascriptPrintVis =
                    new javascriptAstPrettyPrintVisCl();
            javascriptPrintVis.initParams(astTable);
            javascriptPrintVis.visit(rootNodeId, 0);
        }


        javascriptAstFragmentVisCl javascriptFragVis =
                new javascriptAstFragmentVisCl ();
        javascriptFragVis.initParams(astTable, fragThreshold);
        List<Frag> listFrag = javascriptFragVis.visit(rootNodeId);

        javascriptFragmentAnalyserCl javasriptFragAnalyse =
                new javascriptFragmentAnalyserCl();
        javasriptFragAnalyse.initParams(astTable);


        System.out.println("LIST FRAG");

        System.out.println(listFrag.toString());


        List<Clone> clones;
        if(useAlgoDataVec) {
            clones = javasriptFragAnalyse.getCloneDataVec(listFrag, algo, cloneThreshold);
        } else {
            clones = javasriptFragAnalyse.getCloneTypeNoeud(listFrag, algo, cloneThreshold);
        }

        System.out.println("LIST CLONES");

        System.out.println(clones);

        System.out.println("END LIST CLONES");

        System.out.println("nombre de fragment : " + listFrag.size());
        System.out.println("nombre de clones : " + clones.size());

        System.out.println("Successful termination");
        System.exit(0);
    }

}
