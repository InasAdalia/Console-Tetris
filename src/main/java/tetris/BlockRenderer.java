package tetris;

import java.util.Arrays;

//RENDERS OR DRAWS BLOCK ON THE TILEVIEW BASED ON THE BLOCK STATE
public class BlockRenderer {

    Block block;
    TileView tileView;
    GameView gameView;
    CollisionChecker collisionChecker;
    boolean canDrawGhost;

    
    public BlockRenderer (Block block, TileView tileView, GameView gameView, CollisionChecker collisionChecker){
        this.block = block;
        this.tileView = tileView;
        this.gameView = gameView;
        this.collisionChecker = collisionChecker;
    }

    public void setBlock(Block block){
        this.block = block;
    }

    public void eraseBlock() {
       
        for (int k = 0; k < 4; k++) { // Iterate through the "pieces" of the block
            tileView.setPatternAt(Tile.EMPTY, block.posY[k], block.posX[k]);
        }
        tileView.eraseByPattern(Tile.GHOST);
    }

    public void drawBlock(){

        int[] oldPosY = block.posY;
        int[] oldPosX = block.posX;
        int[] heightDiffs = {-1,-1,-1,-1};
        canDrawGhost = true;
       

        int initRow,initCol;
        if (!block.inQueue && !block.isFirstSpawn) {    //to continue drawing blocks alrdy in 'main' tileView 
            initRow = Arrays.stream(block.posY).min().getAsInt();     // has to get the topmost point of the pattern
            initCol = Arrays.stream(block.posX).min().getAsInt();     //has to get the leftmost point of the pattern
        } else if (!block.inQueue && block.isFirstSpawn){ //for drawing first spawn of block in 'main' tileView
            initRow = 0;
            initCol = 3;
        } else {    //for drawing in 'queue' tileView
            initRow = 0;
            initCol = 0;
            canDrawGhost = false;
        }
        // System.out.println("[debug] inQueue: " + inQueue + " isFirstSpawn: " + isFirstSpawn);
        // System.out.println("[debug] all posX: " + Arrays.toString(posX));
        // System.out.println("[debug] all posY: " + Arrays.toString(posY));
        // System.out.println("[debug] initial row: " + initRow + " initial col: " + initCol );
        
        //prevents collision by adjusting initial drawing points to be within the bounds
        if (collisionChecker.exceedsHeightBoundaries(initRow)) {
            initRow = initRow - collisionChecker.exceededHeight(initRow);
        } else if(collisionChecker.exceedsWidthBoundaries(initCol)) {
            initCol = initCol - collisionChecker.exceededWidth(initCol);
        }
        
        // System.out.println("[debug] initial row: " + initRow + " initial col: " + initCol );

        int arrayIndex = 0; //for the for loop down here:
        for (int i = initRow; i<initRow+block.shape.length; i++) {
            for (int j = initCol; j<initCol+block.shape[0].length; j++) { 

                // System.out.println("[debug] now reading col: " + j + " no. of col per row is: " + shape[0].length);
                if (block.shape[i-initRow][j-initCol] == 1) { 

                    if(!collisionChecker.willCollideAt(i, j)){
                        //stores each piece
                        block.posX[arrayIndex] = j; 
                        block.posY[arrayIndex] = i;

                        //draws each piece
                        if(!block.inQueue)
                            tileView.setPatternAt(block.tile, i, j);
                        else
                            gameView.setPatternAt(i, j);

                        //collecting height diff of each tile, later will choose the shortest height diff
                        block.ghostDiff = tileView.getHeightDiff(block.posX[arrayIndex], block.posY[arrayIndex]);
                        if(!block.inQueue && block.ghostDiff!=-1){  
                            // System.out.println("[debug] adding into hieght diff array");
                            heightDiffs[arrayIndex] = block.ghostDiff;
                        }

                    } else { //if the new shape collides with blocks, do not draw it and keep the prev shape.
                        
                        //stores the prev shape
                        block.posX = oldPosX;
                        block.posY = oldPosY;

                        //draws all pieces of prev shape
                        for (int k = 0; k < 4; k++) { // Iterate through the "pieces" of the block
                            tileView.setPatternAt(block.tile, block.posY[k], block.posX[k]);
                        }
                        break;
                    }

                    arrayIndex++;
                }
            }
        }

        // System.out.println("[debug] heightDiffs: " + Arrays.toString(heightDiffs));
        block.ghostDiff = Arrays.stream(heightDiffs).min().getAsInt(); //fetches the shortest height diff 
        if (block.ghostDiff>0){
            drawGhost(block.ghostDiff);
        }

        block.isFirstSpawn = false;
        gameView.showGameView();
        tileView.checkRows();
        // System.out.println("[debug] drew block at Pos X: " + posX[0] + " Pos Y: " + posY[0]);
        // showBlockFields();
    }

    public void drawGhost(int heightDiff){
        for (int i = 0; i < 4; i++) {
            if (!tileView.hasBlockAt(Tile.BLOCK, block.posY[i]+heightDiff, block.posX[i]))
                tileView.setPatternAt(Tile.GHOST, block.posY[i]+heightDiff, block.posX[i]);
        }
        canDrawGhost=true;
    }

    
}
