public class ImagePixel {

    private int r,g,b;

    public ImagePixel(int r, int g, int b){
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public int getR() {
        return r;
    }

    public int getG() {
        return g;
    }

    public int getB() {
        return b;
    }

    public static int PixelDistance(ImagePixel p, ImagePixel q){
        return (q.r - p.r)*(q.r - p.r) + (q.g - p.g)*(q.g - p.g) + (q.b - p.b)*(q.b - p.b);
    }

    public String toString(){
        return "(" + r + ", " + g + ", " + b + ")";
    }
}
