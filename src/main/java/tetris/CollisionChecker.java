package tetris;

public class CollisionChecker {
    
    Block block;
    TileView tileView;

    public CollisionChecker(Block block, TileView tileView) {
        this.block = block;
        this.tileView=tileView;
    }

    public boolean willCollideBottom() {
        for (int i = 0; i < 4; i++) { //isit normal to make posY[i] accesible this way, or should I create getY() and getX() methods?
            if (block.posY[i] + 1 >= tileView.TileView.length || tileView.hasBlockAt(Tile.INACTIVE, block.posY[i] + 1, block.posX[i])) {
                return true;
            }
        }
        return false;
    }

    public boolean willCollideWall(String direction){
        if (direction.toUpperCase().equals("RIGHT")) {
            for (int i = 0; i < 4; i++) {
                if (block.posX[i] + 1 > tileView.TileView[0].length || tileView.hasBlockAt(Tile.INACTIVE,block.posY[i], block.posX[i]+1)) {
                    return true;
                }
            }
        } else if (direction.toUpperCase().equals("LEFT")) {
            for (int i = 0; i < 4; i++) {
                if (block.posX[i] - 1 < 0 || tileView.hasBlockAt(Tile.INACTIVE, block.posY[i], block.posX[i]-1)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean exceedsHeightBoundaries(int initRow ){
        return (initRow+block.shape.length>tileView.TileView.length);
    }

    public boolean exceedsWidthBoundaries(int initCol ){
        return (initCol+block.shape[0].length>tileView.TileView[0].length);
    }

    public int exceededHeight(int initRow){
        return (initRow+block.shape.length-(tileView.TileView.length));
    }

    public int exceededWidth(int initCol){
        return (initCol+block.shape[0].length-(tileView.TileView[0].length));
    }

    public boolean willCollideAt(int i, int j){
        return tileView.hasBlockAt( Tile.INACTIVE, i, j);
    }
    
}
