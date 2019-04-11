import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by febru on 19-04-10.
 */
public class Frag {
    public List<Integer> nodesId;

    public javascriptParseTableCl astTable;

    public Frag(javascriptParseTableCl pastTable) {
        astTable = pastTable;
        nodesId = new ArrayList<>();
    }

    public Integer getIndex() {
        if(nodesId == null || nodesId.size() == 0) {
            return null;
        } else {
            return nodesId.get(nodesId.size() -1);
        }
    }
    public String getName() {
        Integer index = getIndex();
        if(index != null && nodesId.size() > 0) {
            if(nodesId.get(0) == index + 1) {
                List<Map.Entry<Integer, String>> succList = astTable.succTable.get(index);
                Integer theId = null;
                for(Map.Entry<Integer, String> succ : succList) {
                    if(succ.getValue().equals("id")) {
                        theId = succ.getKey();
                    }
                }
                if(theId != null) {
                    Map<String, String> attr = astTable.nodeAttrTable.get(theId);
                    if(attr != null) {
                        String name = attr.get("name");
                        if(name != null) {
                            return name;
                        }
                    }
                }
            }
        }
        return "[anonymous]";
    }

    @Override
    public String toString() {
        Integer index = getIndex();
        if(index == null) {
            return "Frag : Empty";
        } else {
            return "\nFrag no : " + index + ", name : " + getName() + ", loc : [" +
                    astTable.lineBeginTable.get(index) + ", " +
                    astTable.columnBeginTable.get(index ) + "]  to [" +
                    astTable.lineEndTable.get(index ) + ", " +
                    astTable.columnEndTable.get(index) + "]" + ", taille : " + nodesId.size();
        }
    }
}
