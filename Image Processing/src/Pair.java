public class Pair<F, S> {
    private F first; //first member of pair
    private S second; //second member of pair

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public void setFirst(F first) {
        this.first = first;
    }

    public void setSecond(S second) {
        this.second = second;
    }

    public F getFirst() {
        return first;
    }

    public S getSecond() {
        return second;
    }

    public boolean equals(Object o){
        //this is called in hashmap functions if the hashcodes are the same
        if(o == null || o.getClass() != this.getClass()){
            return false;
        }
        Pair<F, S> v = (Pair<F, S>) o;
        if(v.getFirst().equals(this.first) && v.getSecond().equals(this.second)){
            return true;
        }
        return false;
    }

    public int hashCode(){
        return this.first.hashCode() * this.second.hashCode(); //this is so that vertices with the same coordinates will have the same hash
        //using multiplication over addition reduced the number of different verts that have the same hash
    }
}