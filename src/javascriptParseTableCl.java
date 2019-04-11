/**
Copyright (C) Dec 2017, Jan 2019 Ettore Merlo - All rights reserved
*/

import java.util.*;
import java.util.Map.Entry;

class javascriptParseTableCl implements defsInt {

    public Map<String, String> attrTable = new HashMap<>();

    public Map<Integer, Map<String, String>> nodeAttrTable = new HashMap<>();
    public Map<Integer, String> nodeTypeTable = new HashMap<>();

    public Map<Integer, String> literalTable = new HashMap<>();

    //public HashMap<Integer, String> tkImageTable = new HashMap<>();

    //public HashMap<Integer, Integer> tkTypeTable = new HashMap<>();
    
    //
    // source line related info
    //
    protected Map<Integer, Integer> charBeginTable = new HashMap<>();
    protected Map<Integer, Integer> charEndTable = new HashMap<>();
    protected Map<Integer, Integer> lineBeginTable = new HashMap<>();
    protected Map<Integer, Integer> lineEndTable = new HashMap<>();
    protected Map<Integer, Integer> columnBeginTable = new HashMap<>();
    protected Map<Integer, Integer> columnEndTable = new HashMap<>();

    //AST Graph with node_edge
    public Map<Integer, Integer> predTable = new HashMap<>();

    public Map<Integer, List<Entry<Integer, String>>> succTable = new HashMap<>();

    public String getType(Integer nodeId) {
        String curType = nodeTypeTable.get(nodeId);
        if (curType == null) {
            System.err.println("ERROR: missing type for node " +
                    nodeId);
            System.exit(1);
        }
        return(curType);
    }

    public String getLiteral(Integer nodeId) {
        String curStr = literalTable.get(nodeId);
        if (curStr == null) {
            System.err.println("ERROR: missing literal for node " +
                    nodeId);
            System.exit(1);
        }
        return(curStr);
    }


    public List<Entry<Integer, String>> getSuccArr(Integer nodeId) {
        List<Entry<Integer, String>> curArr = succTable.get(nodeId);
        if (curArr == null) {
            System.err.println("ERROR: missing successors for node " +
                    nodeId);
            System.exit(1);
        }
        return curArr;
    }

    public List<Entry<Integer, String>> getSuccArr(Integer nodeId, int curCard) {
        List<Entry<Integer, String>> curArr = getSuccArr(nodeId);
        if (curArr.size() != curCard) {
            System.err.println("ERROR: invalid succ size " +
                    curArr.size() +
                    " for add node " +
                    nodeId +
                    " (" +
                    curCard +
                    " expected)");
            System.exit(1);
        }
        return curArr;
    }


    public Integer getSucc(Integer nodeId, int index, int curCard) {
        List<Entry<Integer, String>> curArr = getSuccArr(nodeId, curCard);
        Integer curNode = curArr.get(index - 1).getKey();
        if (curNode == null) {
            System.err.println("ERROR: missing succ " +
                    index +
                    " for node " +
                    nodeId);
            System.exit(1);
        }
        return curNode;
    }

    public Integer getSucc(ArrayList<Entry<Integer, String>> curArr, int index) {
        if (curArr == null) {
            System.err.println("ERROR: invalid null successors");
            System.exit(1);
        }

        if ((index <= 0) || (index > curArr.size())) {
            System.err.println("ERROR: index " +
                    index +
                    " is out of range [1, " +
                    curArr.size() +
                    "]");
            System.exit(1);
        }

        Integer curNode = curArr.get(index - 1).getKey();
        if (curNode == null) {
            System.err.println("ERROR: missing succ at index " +
                    index);
            System.exit(1);
        }

        return(curNode);
    }

    public void readLine_ast_edge(ArrayList<String> lineArr, Integer lineNo) {

        int iVal = UNDEF_VAL;

        if (lineArr.size() != 4) {
            System.err.println("ERROR: inconsistent length " +
                    lineArr.size() +
                    " for ast edge " +
                    lineArr +
                    "(4 expected)");
            System.exit(1);
        }

        try {
            iVal = Integer.valueOf(lineArr.get(1));
        } catch (NumberFormatException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.err.println("ERROR: invalid parent integer " +
                                lineArr.get(1));
            System.exit(1);
        }
        Integer parentNodeId = iVal;

        try {
            iVal = Integer.valueOf(lineArr.get(2));
        } catch (NumberFormatException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.err.println("ERROR: invalid child integer " +
                                lineArr.get(2));
            System.exit(1);
        }
        Integer childNodeId = iVal;

        String linkType = lineArr.get(3);

        if (!succTable.containsKey(parentNodeId)) {
            ArrayList<Entry<Integer, String>> auxArr = new ArrayList<> ();
            auxArr.add(new AbstractMap.SimpleEntry<>(childNodeId, linkType));
            succTable.put(parentNodeId, auxArr);
        } else {
            succTable.get(parentNodeId).add(new AbstractMap.SimpleEntry<>(childNodeId, linkType));
        }

        if (!predTable.containsKey(childNodeId)) {
            /*

            // useless since it's a tree

            ArrayList<Integer> auxArr =
            new ArrayList<Integer> ();
            auxArr.add(childNodeId);
            */
            predTable.put(childNodeId, parentNodeId);
        } else {

        /*
        // no multiple predecessors allowed in a tree
        */
        
            System.err.println("ERROR: child node " +
                childNodeId +
                " has already parent " +
                predTable.get(childNodeId));
            System.exit(1);
        }

        return;
    }

    public void readLine_char_begin(ArrayList<String> lineArr,
                    Integer lineNo) {
        tableReaderCl.readLine_func_Integer_Integer(lineArr,
                                lineNo,
                                charBeginTable);
        return;
    }

    public void readLine_char_end(ArrayList<String> lineArr,
                               Integer lineNo) {
        tableReaderCl.readLine_func_Integer_Integer(lineArr,
                lineNo,
                charEndTable);
        return;
    }

    public void readLine_line_begin(ArrayList<String> lineArr,
                                    Integer lineNo) {

        tableReaderCl.readLine_func_Integer_Integer(lineArr,
                lineNo,
                lineBeginTable);

        return;
    }

    public void readLine_column_begin(ArrayList<String> lineArr,
                                      Integer lineNo) {

        tableReaderCl.readLine_func_Integer_Integer(lineArr,
                lineNo,
                columnBeginTable);

        return;
    }

    public void readLine_line_end(ArrayList<String> lineArr,
                                    Integer lineNo) {

        tableReaderCl.readLine_func_Integer_Integer(lineArr,
                lineNo,
                lineEndTable);

        return;
    }

    public void readLine_column_end(ArrayList<String> lineArr,
                                      Integer lineNo) {

        tableReaderCl.readLine_func_Integer_Integer(lineArr,
                lineNo,
                columnEndTable);

        return;
    }

    public void readLine_type(ArrayList<String> lineArr,
                Integer lineNo) {
        tableReaderCl.readLine_func_Integer_String(lineArr,
                            lineNo,
                            nodeTypeTable);
        return;
    }

    public void readLine_literal(ArrayList<String> lineArr,
                Integer lineNo) {
        tableReaderCl.readLine_func_Integer_String(lineArr,
                            lineNo,
                            literalTable);
        return;
    }

    public void readLine_node_attr(ArrayList<String> lineArr,
                            Integer lineNo) {
        //
        // func String String
        //
        int iVal = UNDEF_VAL;

        if (lineArr.size() != 4) {
            System.err.println("ERROR: inconsistent length " +
                    lineArr.size() +
                    " for attr " +
                    lineArr +
                    "(4 expected)");
            System.exit(1);
        }

        try {
            iVal = Integer.valueOf(lineArr.get(1));
        } catch (NumberFormatException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.err.println("ERROR: invalid parent integer " +
                    lineArr.get(1));
            System.exit(1);
        }
        Integer parentNodeId = iVal;

        String attrKey = lineArr.get(2);
        String attrValue = lineArr.get(3);

        if (!nodeAttrTable.containsKey(parentNodeId)) {
            HashMap<String, String> auxMap = new HashMap<>();
            auxMap.put(attrKey, attrValue);
            nodeAttrTable.put(parentNodeId, auxMap);
        } else {
            nodeAttrTable.get(parentNodeId).put(attrKey, attrValue);
        }
        return;
    }

    public void readLine_attr(ArrayList<String> lineArr,
                                   Integer lineNo) {
        if (lineArr.size() != 3) {
            System.err.println("ERROR: invalid line length " +
                            lineArr.size() +
                            " at line " +
                            lineNo +
                            " (3 expected) for line: " +
                            lineArr);
            System.exit(1);
        }

        if (attrTable.containsKey(lineArr.get(1))) {
            System.out.println("ERROR: duplicate key at line " +
                            lineNo +
                            ": " +
                            lineArr);
            System.exit(1);
        }

        if (globalOptionsCl.test("printTableReaderTests", "true")) {
            System.out.println("READ: " +
                            lineArr.get(1) +
                            " " +
                            lineArr.get(2));
        }

        attrTable.put(lineArr.get(1), lineArr.get(2));

        return;
    }

    @SuppressWarnings("rawtypes")
    public void print(Map table, String keyType) {
        print(table, keyType, "\t");
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void print(Map table, String keyType, String indent) {


        if(keyType.equals("String")) {
            Iterable<String> it = (Iterable<String>) table.keySet();
            sortUtilsCl.getSortedIterable_String(it).forEach(key ->
                System.out.println(indent +
                    key +
                    " " +
                    table.get(key)));
        } else if(keyType.equals("Integer")) {
            Iterable<Integer> it = (Iterable<Integer>) table.keySet();
            sortUtilsCl.getSortedIterable_Integer(it).forEach(nodeId ->
                System.out.println(indent +
                    nodeId +
                    " " +
                    table.get(nodeId)));
        } else if(keyType.equals("Entry")) {
            Iterable<Integer> it = (Iterable<Integer>) table.keySet();
            sortUtilsCl.getSortedIterable_Integer(it).forEach(parentId ->
                ((List<Entry<Integer, String>>) table.get(parentId)).forEach(childPair ->
                    System.out.println(indent + "SUCC: " +
                            parentId +
                            " " +
                            childPair.getKey() + " " + childPair.getValue())
                )
            );
        } else if(keyType.equals("Map")) {
            Iterable<Integer> it = (Iterable<Integer>) table.keySet();
            sortUtilsCl.getSortedIterable_Integer(it).forEach(nodeId -> {
                System.out.println(indent +
                        nodeId + " :");
                print((Map) table.get(nodeId), "String", indent + "\t");
            });
        }
    }

    public void print() {
        System.out.println("PRINT --->");
        System.out.println();

        System.out.println("ATTRS --->");
        print(nodeAttrTable, "Map");
        System.out.println("<--- ATTRS");
        System.out.println();


        System.out.println("NODE TYPE --->");
        print(nodeTypeTable, "Integer");
        System.out.println("<--- NODE TYPE");
        System.out.println();

        System.out.println("CHAR BEGIN --->");
        //print(charBeginTable, "Integer");
        System.out.println("<--- CHAR BEGIN");
        System.out.println();


        System.out.println("CHAR END --->");
        //print(charEndTable, "Integer");
        System.out.println("<--- CHAR END");
        System.out.println();

        System.out.println("AST SUCC --->");
        //print(succTable, "Entry");
        System.out.println("<--- AST SUCC");
        System.out.println();

        System.out.println("PRED --->");
        //print(predTable, "Integer");
        System.out.println("<--- PRED");
        System.out.println();

        System.out.println("LITERAL --->");
        //print(literalTable, "Integer");
        System.out.println("<--- LITERAL");
        System.out.println();

        System.out.println("<--- PRINT");
        System.out.println();
        return;
    }






    

}
