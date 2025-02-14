package tetris;

import java.util.Random;

public class Tee extends Block{
     TileLayout layout;
    int row, col;
    int[] posX = {0,0,0,0}; 
    int[] posY = {0,0,0,0};  
    int[][] shape;

    //array containing shape1, shape2, shape3, shape4
    int[][][] shapes = {
        {   //shape 1
            {0,1,0},
            {1,1,1}
        },
        {   //shape 2
            {1,0},
            {1,1},
            {1,0}
        },
        {   //shape 3
            {1,1,1},
            {0,1,0}
        },
        {   //shape 4
            {0,1},
            {1,1},
            {0,1}
        }
    };    
    
    public Tee(GameView gameView, TileLayout layout) {
        super(gameView, layout);   

        final Random random = new Random();
        this.shape = shapes[random.nextInt(shapes.length)];
        
       setBlock(shapes, shape, "Tee", posX, posY);
        }
}
