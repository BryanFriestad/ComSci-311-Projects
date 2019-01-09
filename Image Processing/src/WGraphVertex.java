public class WGraphVertex {
    private int x;
    private int y;

    public WGraphVertex(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean equals(Object o){
        //this is called in hashmap functions if the hashcodes are the same
        if(o == null || o.getClass() != this.getClass()){
            return false;
        }
        WGraphVertex v = (WGraphVertex) o;
        if(v.getY() == this.y && v.getX() == this.x){
            return true;
        }
        return false;
    }

    public int hashCode(){
        return this.x * this.y; //this is so that vertices with the same coordinates will have the same hash
        //using multiplication over addition reduced the number of different verts that have the same hash
    }

    public String toString(){
        return "(" + this.x + ", " + this.y + ")";
    }
}
