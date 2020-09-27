package cmpt276.a2;

import java.util.Scanner;

public class Lens {
    private String make;
    // The smaller F_num, the greater(max) aperture,
    // so always find the smallest F_num
    private double F_num;
    // Smaller focal lengths are wide angle,
    // larger focal lengths are more zoomed in
    private double focal_len;

    // a simple test
    public static void main(String[] argv) {
        int isDone = 0;
        while(isDone != 1) {
            System.out.println("inside the loop");
            Scanner in = new Scanner(System.in);
            Lens lens = new Lens("", 1.8, 50);
            isDone = in.nextInt();
        }
    }

    public Lens(Lens lens) {
        if(lens == null || lens.make == null) {
            throw new IllegalArgumentException("\nPROBLEM: in Lens constructor, lens == null || lens.make == null.");
        }
        this.make = lens.make;
        this.F_num = lens.F_num;
        this.focal_len = lens.focal_len;
        if(F_num <= 0 || focal_len <= 0 || make == null || make.equals("")) {
            throw new IllegalArgumentException("\nPROBLEM: in Lens constructor, f_num <= 0 || focal_len <= 0 || make == null || make.equals(\"\").");
        }
    }

    public Lens(String make, double f_num, double focal_len) {
        if(f_num <= 0 || focal_len <= 0 || make == null || make.equals("")) {
            throw new IllegalArgumentException("\nPROBLEM: in Lens constructor, f_num <= 0 || focal_len <= 0 || make == null || make.equals(\"\").");
        }
        this.make = make;
        F_num = f_num;
        this.focal_len = focal_len;

    }

    public String getMake() {
        return make;
    }

    public double getF_num() {
        return F_num;
    }

    public double getFocal_len() {
        return focal_len;
    }

    public String toString() {
        return make + " " + focal_len + "mm F" + F_num;
    }
}

