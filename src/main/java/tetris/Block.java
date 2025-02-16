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
    protected boolean deactivateOnNext, hasCollided, inQueue, isFirstSpawn;
    int ghostDiff;
    boolean drawGhost;
    

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
        drawGhost = false;

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
        }
        
        // gameView.addMessage("init row: " + initRow + " init col: " + initCol);

        preventCollision(initRow, initCol);         //adjusts the initRow and initCol to prevent collision
        int arrayIndex = 0;     //for the for loop down here:
        for (int i = initRow; i<initRow+shape.length; i++) {
            // System.out.println("now reading row " + i);
            for (int j = initCol; j<initCol+shape[0].length; j++) { // shape[0].length to handle non-square shapes

                // System.out.println("now reading col: " + j + " no. of col per row is: " + shape[0].length);
                if (shape[i-initRow][j-initCol] == 1) { 

                    if(!layout.hasBlockAt( Tile.INACTIVE, i, j)){

                        // if (layout.hasBlockAt(Tile.GHOST, i, j))
                        //     drawGhost=false;

                        if(!inQueue)
                            layout.setPatternAt(this.tile, i, j);
                        else
                            gameView.setPatternAt(i, j);

                        posX[arrayIndex] = j; 
                        posY[arrayIndex] = i;

                        
                        ghostDiff = layout.getHeightDiff(posX[arrayIndex], posY[arrayIndex]);
                        System.out.println("ghost diff: " + ghostDiff);
                        if(!inQueue && !isFirstSpawn && ghostDiff!=-1){
                            System.out.println("[debug] adding into hieght diff array");
                            heightDiffs[arrayIndex] = ghostDiff;
                        }

                        
                            

                        //drawing ghost block
                        if(drawGhost){
                            System.out.println("[debug] drawing ghost block, x: " + j + " y: " + (i+ghostDiff) );
                            ghostX[arrayIndex] = j;
                            ghostY[arrayIndex] = i+ghostDiff;
                            layout.setPatternAt(Tile.GHOST, i+ghostDiff, j);
                        }
                    } else { //if the new shape collides with blocks, do not draw it and keep the prev shape.
                        
                        //cancel tracing new block and draw prev block
                        posX = oldPosX;
                        posY = oldPosY;
                        for (int k = 0; k < 4; k++) { // Iterate through the "pieces" of the block
                            layout.setPatternAt(tile, posY[k], posX[k]);
                        }
                        break;
                    }

                    // System.out.println("posX[" + (arrayIndex) + "] = " + j);
                    // System.out.println("posY[" + (arrayIndex) + "] = " + i);

                    arrayIndex++;
                }
            }
        }

        System.out.println("[debug] heightDiffs: " + Arrays.toString(heightDiffs));
        ghostDiff = Arrays.stream(heightDiffs).min().getAsInt(); 
        if (ghostDiff>0){
            ghostDiff = Arrays.stream(heightDiffs).min().getAsInt(); 
            System.out.println("[debug] min height diff: " + ghostDiff); 
            // drawGhost = true;
            drawGhost(ghostDiff);
        }

        layout.checkRows();
        isFirstSpawn = false;
        gameView.showGameLayout();
        System.out.println("[debug] drew block at Pos X: " + posX[0] + " Pos Y: " + posY[0]);
        // showBlockFields();


        //TODO: cast a ghost block for instant dropping
        //find lowest y point of shape (get shape's highest Y, get its coordinate), and check bottom collision for that point
    };

    public void drawGhost(int heightDiff){
        for (int i = 0; i < 4; i++) {
            layout.setPatternAt(Tile.GHOST, posY[i]+heightDiff, posX[i]);
        }
        drawGhost=true;
    }

    public void eraseBlock(boolean blockOnly) {
        System.out.println("[debug] before erasing, checking posX: " + Arrays.toString(posX));
        System.out.println("[debug] before erasing, checking posY: " + Arrays.toString(posY));
        for (int k = 0; k < 4; k++) { // Iterate through the "pieces" of the block
            int i = posY[k]; // Get the row of the k-th piece
            int j = posX[k]; // Get the column of the k-th piece
            layout.setPatternAt(Tile.EMPTY, i, j);
            // System.out.println("erasing Pos X: " + posX[k] + " Pos Y: " + posY[k]);
        }

        // if (!blockOnly)
        layout.eraseByPattern(Tile.GHOST);
        // System.out.println("finished eraseBlock()");
        // showBlockFields();
    }

    public void deactivateBlock(){ //sets all tiles to INACTIVE
        hasCollided = true;
        tile = Tile.INACTIVE;
        // drawBlock();
        // layout.checkRows(); //check rows to be destroyed
        System.out.println("[debug] deactivateBlock() finished");
    }

    public void reactivateBlock(){
        hasCollided=false;
        tile= Tile.BLOCK;
    }

    public void showBlockFields(){  //for debuggin purposes
        for (int k = 0; k < 4; k++) { // Iterate through the "pieces" of the block
            System.out.println("index: " + k + " posX: "+ posX[k] + " Pos Y: " + posY[k]);
        }
    }

    public void dropBlock(){
        System.out.println("[debug] called dropBlock()");
        //erase original block
        // eraseBlock(true);
        if (drawGhost){
            // layout.switchPattern(Tile.GHOST, Tile.INACTIVE);
            for (int i = 0; i < 4; i++) {
                posX[i] = posX[i]+ghostDiff;
                posY[i] = posY[i]+ghostDiff;
                // layout.setPatternAt(Tile.INACTIVE, posY[i]+ghostDiff, posX[i]);
            }
            deactivateOnNext=true;
            // deactivateBlock();
            System.out.println("[debug] should be dropping");
        }
        
    }

    public void move(String direction) {
        System.out.println("moving");
        int curShapeIndex = Arrays.asList(shapes).indexOf(shape); //for rotation

        if (tile != Tile.INACTIVE){
            eraseBlock(false);
            if(direction.toUpperCase().equals("DOWN")){ //move down
                if (!willColideBottom()) {
                    //if deactivated, reactivate it.
                    if (tile == Tile.INACTIVE){
                        reactivateBlock();
                    }
                    // If there's no collision, move the block
                    for (int i = 0; i < 4; i++) {
                        posY[i]++; 
                    }
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

            } else if (direction.toUpperCase().equals("ROTATER")) {
                shape = shapes[(curShapeIndex + 1) % shapes.length];
                deactivateOnNext = false;
    
            } else if (direction.toUpperCase().equals("ROTATEL")) {
                shape = (curShapeIndex-1 == -1)? shapes[shapes.length-1] : shapes[curShapeIndex-1];
                deactivateOnNext = false;

            } else if (direction.toUpperCase().equals("SPACE")) {
                System.out.println("SPACE PRESSED");    
                System.out.println("[debug] called dropBlock()");
                if (drawGhost){
                    for (int i = 0; i < 4; i++) {
                        posX[i] = posX[i];
                        posY[i] = posY[i]+ghostDiff;
                    }
                    deactivateBlock();
                    System.out.println("[debug] should be dropping");
                }
            }



            
            if (!willColideBottom()) {
                deactivateOnNext=false;
                //if deactivated, reactivate it.
                if (tile == Tile.INACTIVE){
                    reactivateBlock();
                }
            } else if (willColideBottom() && !deactivateOnNext){
                deactivateOnNext = true;
                System.out.println("gonna deactivate next tick");
                // layout.checkRows();
            } else if (willColideBottom() && deactivateOnNext){
                deactivateBlock();
            }
            drawBlock();
        }
        
            // 
        
        // System.out.println("finished moveDown()");
    }

    private void preventCollision(int initRow, int initCol){
        if (initRow+shape.length>21) {
            initRow = initRow - (initRow+shape.length-21)-2;
        } else if(initCol + shape[0].length>11) {
            initCol = initCol - (initCol+shape[0].length-11)-2;
        }
    }

    public boolean willColideBottom(){
        for (int i = 0; i < 4; i++) {
            if (posY[i] + 1 >= 21 || layout.hasBlockAt(Tile.INACTIVE, posY[i] + 1, posX[i])) {
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

    public String getType(){
        return type;
    }

    public void setForQueue(boolean inQueue){
        this.inQueue = inQueue;
        this.isFirstSpawn = !inQueue;
    }

    public boolean getInQueue(){
        return inQueue;
    }
}

