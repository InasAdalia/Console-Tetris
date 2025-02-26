package tetris;

import java.util.Random;

public class Long extends Block {
    TileView tileView;
    int row, col;
    int[] posX = {0,0,0,0}; 
    int[] posY = {0,0,0,0};  
    int[][] shape;

    //array containing shape1, shape2, shape3, shape4
    int[][][] shapes = {
        {   //shape 1
            {1,1,1,1}
        },
        {   //shape 2
            {1},
            {1},
            {1},
            {1}
        },
    };    
    
    public Long(GameView gameView, TileView tileView) {
        super(gameView, tileView);   
        this.shape= shapes[0]; //fixed to spawn as shape[0]
        
       setBlock(shapes, shape, "LONG", posX, posY);
    }
}
