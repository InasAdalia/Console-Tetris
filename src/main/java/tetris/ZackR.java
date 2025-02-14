package tetris;

import java.util.Random;

public class ZackR extends Block {
    TileLayout layout;
    int row, col;
    int[] posX = {0,0,0,0}; 
    int[] posY = {0,0,0,0};  
    int[][] shape;

    //array containing shape1, shape2, shape3, shape4
    int[][][] shapes = {
        {   //shape 1
            {0,1,1},
            {1,1,0}
        },
        {   //shape 2
            {1,0},
            {1,1},
            {0,1}
        },
    };    
    
    public ZackR(GameView gameView, TileLayout layout) {
        super(gameView, layout);   
        

        final Random random = new Random();
        this.shape = shapes[random.nextInt(shapes.length)];
        
       setBlock(shapes, shape, "ZackR", posX, posY);
        }

}
