package tetris;

import java.util.Random;

public class Tee extends Block{
     TileView tileView;
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
    
    public Tee(GameView gameView, TileView tileView) {
        super(gameView, tileView);   

        final Random random = new Random();
        this.shape = shapes[random.nextInt(shapes.length)];
        
       setBlock(shapes, shape, "Tee", posX, posY);
        }
}
