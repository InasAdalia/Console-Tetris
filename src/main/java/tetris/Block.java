package tetris;

import java.util.Arrays;

public abstract class Block {

    //should handle block 

    protected TileView tileView;
    protected CollisionChecker collisionChecker;
    protected BlockRenderer blockRenderer;
    protected GameView gameView;
    protected String type;
    protected int initX, initY;
    protected int[] posX, posY;  // X-coordinates for each block piece
    protected int[] ghostX = {0,0,0,0};
    protected int[] ghostY = {0,0,0,0};
    protected int[][] shape;
    protected int[][][] shapes;
    protected Tile tile;
    protected boolean deactivateOnNext, hasCollided, inQueue, isFirstSpawn;
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

    public void move(Movements direction) {
        // System.out.println("moving");

        int curShapeIndex = Arrays.asList(shapes).indexOf(shape); //fetches the cur shape index for rotation
        blockRenderer.eraseBlock();

        if (tile != Tile.INACTIVE){
            
            if(direction.equals( Movements.DOWN )&& !collisionChecker.willCollide(0,1)){ //move down
                for (int i = 0; i < 4; i++) { posY[i]++; }
                // System.out.println("[debug] should have moved");
                
            } else if(direction.equals( Movements.LEFT) && !collisionChecker.willCollide((-1),0)){ //move left
                for (int i = 0; i < 4; i++) { posX[i]--;}
                
            } else if(direction.equals( Movements.RIGHT) && !collisionChecker.willCollide(1,0)){ //move right
                for (int i = 0; i < 4; i++) {posX[i]++;}

            } else if(direction.equals( Movements.ROTATER)) { //rotate right

                shape = shapes[(curShapeIndex + 1) % shapes.length];
                deactivateOnNext = false;
    
            } else if(direction.equals( Movements.ROTATEL)) { //rotate left

                shape = (curShapeIndex-1 == -1)? shapes[shapes.length-1] : shapes[curShapeIndex-1];
                deactivateOnNext = false;

            } else if(direction.equals(Movements.DROP)) { //drop block

                //replaces ghost with actual tiles.
                for (int i = 0; i < 4; i++) {
                    posY[i] = posY[i]+ghostDiff;
                }
                deactivateOnNext = true;  //without this it will delay the deactivation effect by one tick.   
            }


            //Handles deactivation logic
            if (!collisionChecker.willCollide(0,1)) { //offset one down
                deactivateOnNext=false;
    
                if (tile == Tile.INACTIVE) //if deactivated, reactivate it. allows last minute movements made by players.
                    reactivateBlock();
                
            } else if (collisionChecker.willCollide(0,1) && !deactivateOnNext){
                deactivateOnNext = true;
            } else if (collisionChecker.willCollide(0,1) && deactivateOnNext){
                deactivateBlock();
            }

            //adjusts drawing points based on collision
            blockRenderer.updateInitPos();  //the one responsible in determining where init draw points are on view.
            initX = collisionChecker.adjustWithinWidth(blockRenderer.getInitX());
            initY = collisionChecker.adjustWithinHeight(blockRenderer.getInitY());

            //render
            blockRenderer.setInitPos(initX, initY);
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

