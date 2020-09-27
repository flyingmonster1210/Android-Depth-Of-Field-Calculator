package cmpt276.a2;

import java.util.ArrayList;
import java.util.Iterator;

public class Lens_manager implements Iterable<Lens>{
    private ArrayList<Lens> manager = new ArrayList<>();
    private static Lens_manager instance;

    // a simple test
    public static void main(String[] argv) {
        Lens[] lenses = {
            (new Lens("Canon", 1.8, 50)),
            (new Lens("Tamron", 2.8, 90)),
            (new Lens("Sigma", 2.8, 200)),
            (new Lens("Nikon", 4, 200)),
            (new Lens("ElCheepo", 12, 24)),
            (new Lens("Leica", 5.6, 1600)),
            (new Lens("TheWide", 1.0, 16)),
            (new Lens("IWish", 1.0, 200)),
        };


    }

    // create a manager only once
    public static Lens_manager getInstance(Lens[] lenses) {
        if(instance == null) {
            instance = new Lens_manager(lenses);
        }
        return instance;
    }

//    private Lens_manager() {
//        // Private to prevent anyone else from instantiating
//    }

    // the only constructor we will use
    private Lens_manager(Lens[] lens) {
        for(Lens tmp : lens)
            manager.add(tmp);
    }

    private Lens_manager(ArrayList<Lens> manager) {
        this.manager = manager;
    }

    public ArrayList<Lens> getManager() {
        return manager;
    }

    public void setManager(ArrayList<Lens> manager) {
        this.manager = manager;
    }

    public void add(Lens len) {
        manager.add(len);
    }

    public int getSize(){
        return manager.size();
    }

    @Override
    public Iterator<Lens> iterator() {
        return manager.iterator();
    }

    public Lens getByIndex(int i) {
        if(i > manager.size() || i < 0) {
            System.out.println("PROBLEM: in getByIndex, i should be [0, " + manager.size() + ").");
            return new Lens("", 0, 0);
        }

        return manager.get(i);
    }

    public void printAll() {
        int cnt = 0;
        for(Lens tmp : manager) {
            System.out.println(cnt++ + ": " + tmp);
        }
    }
}
