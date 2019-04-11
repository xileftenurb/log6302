import java.util.List;

/**
 * Created by febru on 19-04-10.
 */
public class Clone {
    public List<Frag> frags;
    public javascriptParseTableCl astTable;

    public Clone(javascriptParseTableCl pastTable) {
        astTable = pastTable;
    }

    public void add(Frag f) {
        frags.add(f);
    }

    public String toString() {
        //première élément d'un fragment
        String str = "\nClone : \n";
        for(Frag f : frags) {
            str += f.shortPrint() + "\n";
        }
        return str;
    }
}