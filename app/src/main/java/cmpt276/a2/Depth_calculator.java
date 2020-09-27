package cmpt276.a2;

// distance < 0 || aperture  < F_num || aperture > 22 || make.equal("")
public class Depth_calculator {
    private Lens lens;
    private double distance; // distance to subject, in mm
    private double aperture; // aperture to use for the photo, it is a f_num, in mm
    private static final double circle = 0.029; // “Circle of confusion” of the camera, in mm

    // a simple test
//    public static void main(String[] argv) {
//        Lens lens = new Lens("Canon", 1.8, 50);
//        Lens lens = new Lens("Tamron", 2.8, 90);
//          // all functions' return values are in mm, remember to change them into m.
//        Depth_calculator calculator = new Depth_calculator(lens, 1.1*1000, 1.8);
//        Depth_calculator calculator = new Depth_calculator(lens, 2.0*1000, 2.8);
//        System.out.println(lens);
//        System.out.println("near: " + calculator.near_focal_point()/1000);
//        System.out.println("far : " + calculator.far_focal_point()/1000);
//        System.out.println("hPoint: " + calculator.hyper_focal_distance()/1000);
//    }

    public Depth_calculator(Lens len, double distance, double aperture) {
        this.lens = len;
        this.distance = distance;
        this.aperture = aperture;
        if(lens == null || distance < 0 || aperture < lens.getF_num() || aperture > 22 || lens.getMake().equals("")) {
            throw new IllegalArgumentException("\nPROBLEM: lens == null || distance < 0 || aperture < lens.getF_num() || aperture > 22 || lens.getMake().equals(\"\")");
        }
    }

    public double hyper_focal_distance() {
        return Math.pow(lens.getFocal_len(), 2)/(aperture*circle);
    }

    public double near_focal_point() {
        double tmp = hyper_focal_distance();
        return (tmp * distance)/(tmp + (distance - lens.getFocal_len()));
    }

    public double far_focal_point() {
        double tmp = hyper_focal_distance();
        if(distance > tmp) {
            return Double.POSITIVE_INFINITY;
        }
        return  (tmp * distance)/(tmp - (distance - lens.getFocal_len()));
    }

    public double depth_field() {
        return far_focal_point() - near_focal_point();
    }

    public Lens getLens() {
        return lens;
    }

    public double getDistance() {
        return distance;
    }

    public double getAperture() {
        return aperture;
    }

    public  double getCircle() {
        return circle;
    }
}
