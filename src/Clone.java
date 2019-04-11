import java.util.List;

/**
 * Created by febru on 19-04-10.
 */
public class Clone {
    public Frag frag1;
    public Frag frag2;
    public double distance;
    public javascriptParseTableCl astTable;

    public Clone(javascriptParseTableCl pastTable, Frag pfrag1, Frag pfrag2, double pdistance) {
        astTable = pastTable;
        frag1 = pfrag1;
        frag2 = pfrag2;
        distance = pdistance;
    }

    public String toString() {
        //première élément d'un fragment
        return "\nClone : \n" +
                "distance : " + distance + "\n" +
                frag1.shortPrint() + "\nvs\n" + frag2.shortPrint();
    }
}