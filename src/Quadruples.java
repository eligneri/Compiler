import java.util.Vector;
import java.util.Enumeration;

/*
 * This class stores and creates TVI codes
 */
public class Quadruples {
    private Vector<String[]> Quadruple;
    private int NextQuad;

    public Quadruples()
    {
        Quadruple = new Vector<String[]>();
        NextQuad = 0;
        String[] dummy_quadruple = new String[4];
        dummy_quadruple[0] = dummy_quadruple[1] = dummy_quadruple[2] = dummy_quadruple[3] = null;
        Quadruple.add(NextQuad,dummy_quadruple);
        NextQuad++;
    }

    /*
     * Getters and Setters
     */
    public String GetField(int quadindex, int field)
    {
        return Quadruple.elementAt(quadindex)[field];
    }

    public void SetField(int quadindex, int index, String field)
    {
        Quadruple.elementAt(quadindex)[index] = field;
    }

    public int GetNextQuad()
    {
        return NextQuad;
    }

    public void IncrementNextQuad()
    {
        NextQuad++;
    }

    public String[] GetQuad(int index)
    {
        return Quadruple.elementAt(index);
    }

    //adds quad to quadlist
    public void AddQuad(String[] quad)
    {
        Quadruple.add(NextQuad, quad);
        NextQuad++;
    }

    //prints the quad list, quad by quad
    public void Print()
    {
        int quadlabel = 1;
        String separator;

        System.out.println("CODE");

        Enumeration<String[]> e = this.Quadruple.elements();
        e.nextElement();

        while (e.hasMoreElements())
        {
            String[] quad = e.nextElement();
            System.out.print(quadlabel + ":  " + quad[0]);

            if (quad[1] != null)
                System.out.print(" " + quad[1]);

            if (quad[2] != null)
                System.out.print(", " + quad[2]);

            if (quad[3] != null)
                System.out.print(", " + quad[3]);

            System.out.println();
            quadlabel++;

        }
    }
}
