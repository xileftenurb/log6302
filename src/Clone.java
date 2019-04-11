import java.util.ArrayList;
import java.util.List;

/**
 * Created by febru on 19-04-10.
 */
public class Clone {
    public List<Frag> frags;
    public javascriptParseTableCl astTable;

    public Clone(javascriptParseTableCl pastTable) {
        astTable = pastTable;
        frags = new ArrayList<>();
    }

    public void add(Frag f) {
        frags.add(f);
    }

    public String toString() {
        //première élément d'un fragment
        String str = "\n\nClone :";
        for(Frag f : frags) {
            str += "\n" + f.shortPrint();
        }
        return str;
    }
}