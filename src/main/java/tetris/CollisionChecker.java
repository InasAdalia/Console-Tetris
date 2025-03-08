package tetris;

public class CollisionChecker {
    
    Block block;
    TileView tileView;

    public CollisionChecker(Block block, TileView tileView) {
        this.block = block;
        this.tileView=tileView;
    }

    public boolean willCollide(int offsetX, int offsetY){
        for (int i = 0; i < 4; i++) { //isit normal to make posY[i] accesible this way, or should I create getY() and getX() methods?
            if (block.posY[i] + offsetY >= tileView.TileView.length || block.posX[i] + offsetX > tileView.TileView[0].length  || tileView.hasBlockAt(Tile.INACTIVE, block.posY[i] + offsetY, block.posX[i] + offsetX)) {
                return true;
            }
        }
        return false;
    }

    
    public boolean willCollideAt(int i, int j){
        return tileView.hasBlockAt( Tile.INACTIVE, i, j);
    }

    public int adjustWithinHeight(int initRow){
        return (initRow+block.shape.length>tileView.TileView.length)?
        (initRow+block.shape.length-(tileView.TileView.length)) : initRow;
    }

    public int adjustWithinWidth(int initCol){
        return (initCol+block.shape[0].length>tileView.TileView[0].length)?(initCol+block.shape[0].length-(tileView.TileView[0].length)) : initCol;
    }

    
}
