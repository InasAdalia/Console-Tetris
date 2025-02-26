package tetris;

import java.util.Random;

public class ZackL  extends Block{
    TileView tileView;
    int row, col;
    int[] posX = {0,0,0,0}; 
    int[] posY = {0,0,0,0};  
    int[][] shape;

    //array containing shape1, shape2, shape3, shape4
    int[][][] shapes = {
        {   //shape 1
            {1,1,0},
            {0,1,1}
        },
        {   //shape 2
            {0,1},
            {1,1},
            {1,0}
        },
    };    
    
    public ZackL(GameView gameView, TileView tileView) {
        super(gameView, tileView);   
        
        final Random random = new Random();
        this.shape = shapes[random.nextInt(shapes.length)];
    
       setBlock(shapes, shape, "ZackL", posX, posY);
        }

}
