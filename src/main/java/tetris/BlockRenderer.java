package tetris;

import java.util.Arrays;

//RENDERS OR DRAWS BLOCK ON THE TILEVIEW BASED ON THE BLOCK STATE
public class BlockRenderer {

    Block block;
    TileView tileView;
    GameView gameView;
    CollisionChecker collisionChecker;
    boolean canDrawGhost;
    int initY,initX; //topmost & leftmost point of the shape

    
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
       
        int arrayIndex = 0; //for the for loop down here:
        for (int i = initY; i<initY+block.shape.length; i++) {
            for (int j = initX; j<initX+block.shape[0].length; j++) { 

                // System.out.println("[debug] now reading col: " + j + " no. of col per row is: " + shape[0].length);
                if (block.shape[i-initY][j-initX] == 1) { 

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

        //GHOST BLOCK LOGIC
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

    //block renderer is responsible in determining logic of where to draw the block
    public void updateInitPos(){
        if (!block.inQueue && !block.isFirstSpawn) {    //resumes drawing blocks alrdy in 'main' tileView 
            initY = Arrays.stream(block.posY).min().getAsInt(); //topmost point of the pattern
            initX = Arrays.stream(block.posX).min().getAsInt(); //leftmost point of the pattern

        } else if (!block.inQueue && block.isFirstSpawn){ //draws first spawn of block in 'main' tileView
            initY = 0;
            initX = 3;

        } else {    //draws in 'queue' tileView
            initY = 0;
            initX = 0;
            canDrawGhost = false;
        }
    }

    public int getInitX(){
        return initX;
    }

    public int getInitY(){
        return initY;
    }

    public void setInitPos(int initX, int initY){
        this.initX = initX;
        this.initY=initY;
    }
    
}
