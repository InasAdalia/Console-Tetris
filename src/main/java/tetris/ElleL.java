package tetris;

import java.util.Random;

public class ElleL extends Block{

    TileLayout layout;
    int row, col;
    int[] posX = {0,0,0,0}; 
    int[] posY = {0,0,0,0};  
    int[][] shape;

    //array containing shape1, shape2, shape3, shape4
    int[][][] shapes = {
        {   //shape 1
            {1,1,1},
            {0,0,1}
        },
        {   //shape 2
            {0,1},
            {0,1},
            {1,1}
        },
        {   //shape 3
            {1,0,0},
            {1,1,1}
        },
        {   //shape 4
            {1,1},
            {1,0},
            {1,0}
        }
    };    
    
    public ElleL(GameView gameView, TileLayout layout) {
        super(gameView, layout);   
        
        final Random random = new Random();
        this.shape = shapes[random.nextInt(shapes.length)];
        
        setBlock(shapes, shape,"ElleL",posX, posY);
    }
}