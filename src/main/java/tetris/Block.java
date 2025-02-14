package tetris;

import java.util.Arrays;

public abstract class Block {

    protected TileLayout layout;
    protected GameView gameView;
    protected String type;
    protected int[] posX, posY;  // X-coordinates for each block piece
    protected int[][] shape;
    protected int[][][] shapes;
    protected Tile tile;
    protected boolean deactivateOnNext, hasCollided, inQueue, isFirstSpawn;

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

        eraseBlock();
        // layout.checkRows();
        //TODO: refactor this logic
        int initRow,initCol;
        if (!inQueue && !isFirstSpawn) {
            initRow = Arrays.stream(posY).min().getAsInt(); // has to get the topmost point of the pattern
            initCol = Arrays.stream(posX).min().getAsInt();  //has to get the leftmost point of the pattern
        } else if (!inQueue && isFirstSpawn){
            initRow = 0;
            initCol = 3;
        } else {
            initRow = 0;
            initCol = 0;
        }
        
        // gameView.addMessage("init row: " + initRow + " init col: " + initCol);

        int arrayIndex = 0;

        for (int i = initRow; i<initRow+shape.length; i++) {
            // System.out.println("now reading row " + i);
            for (int j = initCol; j<initCol+shape[0].length; j++) { // shape[0].length to handle non-square shapes

                // System.out.println("now reading col: " + j + " no. of col per row is: " + shape[0].length);
                if (shape[i-initRow][j-initCol] == 1) { 

                    if(!layout.hasBlockAt(i, j)){

                        // eraseBlock();

                        if(!inQueue)
                            layout.setPatternAt(this.tile, i, j);
                        else
                            gameView.setPatternAt(i, j);

                        posX[arrayIndex] = j; 
                        posY[arrayIndex] = i;
                    }

                    // System.out.println("posX[" + (arrayIndex) + "] = " + j);
                    // System.out.println("posY[" + (arrayIndex) + "] = " + i);

                    arrayIndex++;
                }
            }
        }
        layout.checkRows();
        isFirstSpawn = false;
        gameView.showGameLayout();
        System.out.println("drew block at Pos X: " + posX[0] + " Pos Y: " + posY[0]);
        // showBlockFields();
    };

    public void eraseBlock() {
        for (int k = 0; k < 4; k++) { // Iterate through the "pieces" of the block
            int i = posY[k]; // Get the row of the k-th piece
            int j = posX[k]; // Get the column of the k-th piece
            layout.setPatternAt(Tile.EMPTY, i, j);
            // System.out.println("erasing Pos X: " + posX[k] + " Pos Y: " + posY[k]);
        }
        // System.out.println("finished eraseBlock()");
        // showBlockFields();
    }

    public void deactivateBlock(){ //sets all tiles to INACTIVE
        hasCollided = true;
        tile= Tile.INACTIVE;
        // drawBlock();
        // layout.checkRows(); //check rows to be destroyed
        System.out.println("deactivateBlock() finished");
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

    public void move(String direction) {
        System.out.println("moving");

        if (tile != Tile.INACTIVE){
            eraseBlock();
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

    public boolean willColideBottom(){
        for (int i = 0; i < 4; i++) {
            if (posY[i] + 1 >= 21 || layout.hasBlockAt(posY[i] + 1, posX[i])) {
                return true;
            }
        }
        return false;
    }

    private boolean willCollideWall(String direction){
        if (direction.toUpperCase().equals("RIGHT")) {
            for (int i = 0; i < 4; i++) {
                if (posX[i] + 1 > 10 || layout.hasBlockAt(posY[i], posX[i]+1)) {
                    return true;
                }
            }
        } else if (direction.toUpperCase().equals("LEFT")) {
            for (int i = 0; i < 4; i++) {
                if (posX[i] - 1 < 0 || layout.hasBlockAt(posY[i], posX[i]-1)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void isValidRotation(){
        
    }

    public void rotate(String direction){
        System.out.println("rotating   ");

        int curShapeIndex = Arrays.asList(shapes).indexOf(shape);

        if (direction.toUpperCase().equals("ROTATER")) {
            shape = shapes[(curShapeIndex + 1) % shapes.length];
            //TODO: check if next shape is valid/blocked

        } else if (direction.toUpperCase().equals("ROTATEL")) {
            shape = (curShapeIndex-1 == -1)? shapes[shapes.length-1] : shapes[curShapeIndex-1];
        }

        drawBlock();     
        gameView.addMessage("direction: " + direction + "\n");       

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

