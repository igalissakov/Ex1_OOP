public class Position {

    //fields represent the rows and columns at the board game
    private final int x, y;

    //Constructor, getting row and col, coordinates on the map.
    public Position(int y, int x){
        this.x = x;
        this.y = y;
    }

    public Position(Position position){
        this.x = position.getX();
        this.y = position.getY();
    }
    //from position to another position
    public Position(Position a, int x, int y){
        this.x = a.getX() + x;
        this.y = a.getY() + y;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public int dist(Position pos){
            int low, high;
            if (pos.getX() == this.x) {
            low = pos.getY() < this.y ? pos.getY() : this.y;
            high = pos.getY() > this.y ? pos.getY() : this.y;
            return high - low;
            } else {
                low = pos.getX() < this.x ? pos.getX() : this.x;
                high = pos.getX() > this.x ? pos.getX() : this.x;
                return high - low;
            }
    }

    public boolean equals(Position pos){
        return this.x == pos.getX() && this.y == pos.getY();
    }

    @Override
    public String toString(){
        return "(" + this.y + ", " + this.x + ")";
    }


}
