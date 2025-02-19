package tetris;

import java.util.Arrays;

public abstract class Block {

    protected TileLayout layout;
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
    

    public Block(GameView gameView, TileLayout layout) {
        this.gameView = gameView;
        this.layout = layout;
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

    public void drawBlock(){

        // eraseBlock(false);
        int[] oldPosY = posY;
        int[] oldPosX = posX;
        int[] heightDiffs = {-1,-1,-1,-1};
        canDrawGhost = true;

        //TODO: refactor this logic
        int initRow,initCol;
        if (!inQueue && !isFirstSpawn) {    //to continue drawing blocks alrdy in 'main' layout 
            initRow = Arrays.stream(posY).min().getAsInt();     // has to get the topmost point of the pattern
            initCol = Arrays.stream(posX).min().getAsInt();     //has to get the leftmost point of the pattern
        } else if (!inQueue && isFirstSpawn){ //for drawing first spawn of block in 'main' layout
            initRow = 0;
            initCol = 3;
        } else {    //for drawing in 'queue' layout
            initRow = 0;
            initCol = 0;
            canDrawGhost = false;
        }
        // System.out.println("[debug] inQueue: " + inQueue + " isFirstSpawn: " + isFirstSpawn);
        // System.out.println("[debug] all posX: " + Arrays.toString(posX));
        // System.out.println("[debug] all posY: " + Arrays.toString(posY));
        // System.out.println("[debug] initial row: " + initRow + " initial col: " + initCol );
        
        //prevents collision by adjusting initial drawing points to be within the bounds
        if (initRow+shape.length>21) {
            initRow = initRow - (initRow+shape.length-21);
        } else if(initCol + shape[0].length>11) {
            initCol = initCol - (initCol+shape[0].length-11);
        }
        
        // System.out.println("[debug] initial row: " + initRow + " initial col: " + initCol );
        int arrayIndex = 0;     //for the for loop down here:
        for (int i = initRow; i<initRow+shape.length; i++) {
            // System.out.println("[debug] now reading row " + i);
            for (int j = initCol; j<initCol+shape[0].length; j++) { // shape[0].length to handle non-square shapes

                // System.out.println("[debug] now reading col: " + j + " no. of col per row is: " + shape[0].length);
                if (shape[i-initRow][j-initCol] == 1) { 

                    if(!layout.hasBlockAt( Tile.INACTIVE, i, j)){

                        // if (layout.hasBlockAt(Tile.GHOST, i, j))
                        //     canDrawGhost=false;

                        if(!inQueue)
                            layout.setPatternAt(this.tile, i, j);
                        else
                            gameView.setPatternAt(i, j);

                        posX[arrayIndex] = j; 
                        posY[arrayIndex] = i;

                        
                        ghostDiff = layout.getHeightDiff(posX[arrayIndex], posY[arrayIndex]);
                        // System.out.println("ghost diff: " + ghostDiff);

                        //collecting height diff of each tile, later will choose the shortest height diff
                        if(!inQueue && ghostDiff!=-1){  
                            // System.out.println("[debug] adding into hieght diff array");
                            heightDiffs[arrayIndex] = ghostDiff;
                        }

                    } else { //if the new shape collides with blocks, do not draw it and keep the prev shape.
                        
                        posX = oldPosX;
                        posY = oldPosY;
                        for (int k = 0; k < 4; k++) { // Iterate through the "pieces" of the block
                            layout.setPatternAt(tile, posY[k], posX[k]);
                        }
                        break;
                    }

                    arrayIndex++;
                }
            }
        }

        // System.out.println("[debug] heightDiffs: " + Arrays.toString(heightDiffs));
        ghostDiff = Arrays.stream(heightDiffs).min().getAsInt(); //fetches the shortest height diff 
        if (ghostDiff>0){
            drawGhost(ghostDiff);
        }

        isFirstSpawn = false;
        layout.checkRows();
        gameView.showGameLayout();
        // System.out.println("[debug] drew block at Pos X: " + posX[0] + " Pos Y: " + posY[0]);
        // showBlockFields();
    };

    public void drawGhost(int heightDiff){
        for (int i = 0; i < 4; i++) {
            if (!layout.hasBlockAt(Tile.BLOCK, posY[i]+heightDiff, posX[i]))
                layout.setPatternAt(Tile.GHOST, posY[i]+heightDiff, posX[i]);
        }
        canDrawGhost=true;
    }

    public void eraseBlock() {
       
        for (int k = 0; k < 4; k++) { // Iterate through the "pieces" of the block
            layout.setPatternAt(Tile.EMPTY, posY[k], posX[k]);
        }
        layout.eraseByPattern(Tile.GHOST);
    }

    public void deactivateBlock(){ //sets all tiles to INACTIVE
        hasCollided = true;
        tile = Tile.INACTIVE;
        for (int k = 0; k < 4; k++) { // Iterate through the "pieces" of the block
            layout.setPatternAt(this.tile, posY[k], posX[k]);
        }
        // drawBlock();
        // layout.checkRows(); //check rows to be destroyed
        System.out.println("[debug] deactivateBlock() finished");
    }

    public void reactivateBlock(){
        hasCollided=false;
        tile= Tile.BLOCK;
    }

    public void move(String direction) {
        System.out.println("moving");
        int curShapeIndex = Arrays.asList(shapes).indexOf(shape); //for rotation
        eraseBlock();
        if (tile != Tile.INACTIVE){
            
            if(direction.toUpperCase().equals("DOWN")){ //move down
                if (!willColideBottom()) { // If there's no collision, move the block
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
                if (!willCollideWall("LEFT"))
                    for (int i = 0; i < 4; i++) {
                        posX[i]--; 
                    }
                
            } else if(direction.toUpperCase().equals("RIGHT")){ //move right
                if (!willCollideWall("RIGHT"))
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

            if (!willColideBottom()) {   //checks collision & deactivation logic
                deactivateOnNext=false;
    
                if (tile == Tile.INACTIVE){ //if deactivated, reactivate it. allows last minute movements made by players.
                    reactivateBlock();
                }
            } else if (willColideBottom() && !deactivateOnNext){
                deactivateOnNext = true;
            } else if (willColideBottom() && deactivateOnNext){
                deactivateBlock();
            }
            // System.out.println("[debug] move() calling drawBlock()");
            drawBlock();
        }        
    }

    public boolean willColideBottom(){
        for (int i = 0; i < 4; i++) {
            if (posY[i] + 1 >= layout.tileLayout.length || layout.hasBlockAt(Tile.INACTIVE, posY[i] + 1, posX[i])) {
                return true;
            }
        }
        return false;
    }

    private boolean willCollideWall(String direction){
        if (direction.toUpperCase().equals("RIGHT")) {
            for (int i = 0; i < 4; i++) {
                if (posX[i] + 1 > 10 || layout.hasBlockAt(Tile.INACTIVE,posY[i], posX[i]+1)) {
                    return true;
                }
            }
        } else if (direction.toUpperCase().equals("LEFT")) {
            for (int i = 0; i < 4; i++) {
                if (posX[i] - 1 < 0 || layout.hasBlockAt(Tile.INACTIVE, posY[i], posX[i]-1)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setForQueue(boolean inQueue){
        this.inQueue = inQueue;
        if (inQueue)
            gameView.setQueueLayout(this);
    }

    public boolean getInQueue(){
        return inQueue;
    }

    public void setIsFirstSpawn(boolean isFirstSpawn){
        this.isFirstSpawn = isFirstSpawn;
    }
    
}

