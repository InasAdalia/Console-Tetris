package tetris;

import java.util.Arrays;

public abstract class Block {

    //should handle block 

    protected TileView tileView;
    protected CollisionChecker collisionChecker;
    protected BlockRenderer blockRenderer;
    protected GameView gameView;
    protected String type;
    protected int[] posX, posY;  // X-coordinates for each block piece
    protected int[] ghostX = {0,0,0,0};
    protected int[] ghostY = {0,0,0,0};
    protected int[][] shape;
    protected int[][][] shapes;
    protected Tile tile;
    protected boolean deactivateOnNext, hasCollided, inQueue, isFirstSpawn, canDrawGhost;
    protected int ghostDiff;
    

    public Block(GameView gameView, TileView tileView) {
        this.gameView = gameView;
        this.tileView = tileView;
        collisionChecker = new CollisionChecker(this, tileView);
        blockRenderer = new BlockRenderer(this, tileView, gameView, collisionChecker);
        
    }

    protected void setBlock(int[][][] shapes, int[][] shape, String type, int[] posX, int[] posY){
        this.shapes = shapes;
        this.shape=shape;
        this.type = type;
        this.posX = posX;
        this.posY = posY;
        this.tile= Tile.BLOCK;
        this.hasCollided = false;
        this.deactivateOnNext = false;
        this.inQueue = false;
        this.isFirstSpawn = true;
    }

    public void move(String direction) {
        System.out.println("moving");
        int curShapeIndex = Arrays.asList(shapes).indexOf(shape); //for rotation
        blockRenderer.eraseBlock();
        if (tile != Tile.INACTIVE){
            
            if(direction.toUpperCase().equals("DOWN")){ //move down
                if (!collisionChecker.willCollideBottom()) { // If there's no collision, move the block
                    //if deactivated, reactivate it.
                    if (tile == Tile.INACTIVE){
                        reactivateBlock();
                    }
                    
                    for (int i = 0; i < 4; i++) { 
                        posY[i]++; 
                    }
                    System.out.println("should have moved");
                } 
                
            } else if(direction.toUpperCase().equals("LEFT")){ //move left
                if (!collisionChecker.willCollideWall("LEFT"))
                    for (int i = 0; i < 4; i++) {
                        posX[i]--; 
                    }
                
            } else if(direction.toUpperCase().equals("RIGHT")){ //move right
                if (!collisionChecker.willCollideWall("RIGHT"))
                    for (int i = 0; i < 4; i++) {
                        posX[i]++;
                    }

            } else if (direction.toUpperCase().equals("ROTATER")) { //rotate right
                shape = shapes[(curShapeIndex + 1) % shapes.length];
                deactivateOnNext = false;
    
            } else if (direction.toUpperCase().equals("ROTATEL")) { //rotate left
                shape = (curShapeIndex-1 == -1)? shapes[shapes.length-1] : shapes[curShapeIndex-1];
                deactivateOnNext = false;

            } else if (direction.toUpperCase().equals("SPACE")) { //drop block

                //replaces ghost with actual tiles.
                for (int i = 0; i < 4; i++) {
                    posY[i] = posY[i]+ghostDiff;
                }
                deactivateBlock();                
            }

            if (!collisionChecker.willCollideBottom()) {   //checks collision & deactivation logic
                deactivateOnNext=false;
    
                if (tile == Tile.INACTIVE){ //if deactivated, reactivate it. allows last minute movements made by players.
                    reactivateBlock();
                }
            } else if (collisionChecker.willCollideBottom() && !deactivateOnNext){
                deactivateOnNext = true;
            } else if (collisionChecker.willCollideBottom() && deactivateOnNext){
                deactivateBlock();
            }
            // System.out.println("[debug] move() calling drawBlock()");
            blockRenderer.drawBlock();
        }        
    }

    public void deactivateBlock(){ //sets all tiles to INACTIVE
        hasCollided = true;
        tile = Tile.INACTIVE;
        for (int k = 0; k < 4; k++) { // Iterate through the "pieces" of the block
            tileView.setPatternAt(this.tile, posY[k], posX[k]);
        }

        // System.out.println("[debug] deactivateBlock() finished");
    }

    public void reactivateBlock(){
        hasCollided=false;
        tile= Tile.BLOCK;
    }

    public void setForQueue(boolean inQueue){
        this.inQueue = inQueue;
        if (inQueue)
            gameView.setQueuetileView(this);
    }

    public boolean getInQueue(){
        return inQueue;
    }

    public void setIsFirstSpawn(boolean isFirstSpawn){
        this.isFirstSpawn = isFirstSpawn;
    }


}

