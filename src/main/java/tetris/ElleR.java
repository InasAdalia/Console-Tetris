package tetris;

import java.util.Random;
import java.util.random.RandomGenerator;

public class ElleR extends Block{

    TileView tileView;
    int row, col;
    int[] posX = {0,0,0,0}; 
    int[] posY = {0,0,0,0};  
    int[][] shape;

    //array containing shape1, shape2, shape3, shape4
    int[][][] shapes = {
        {   //shape 1
            {0,0,1},
            {1,1,1}
        },
        {   //shape 2
            {1,0},
            {1,0},
            {1,1}
        },
        {   //shape 3
            {1,1,1},
            {1,0,0}
        },
        {   //shape 4
            {1,1},
            {0,1},
            {0,1}
        }
    };    
    
    public ElleR(GameView gameView, TileView tileView) {
        super(gameView, tileView);   
        

        final Random random = new Random();
        this.shape = shapes[random.nextInt(shapes.length)];
        setBlock(shapes, shape,"ElleR", posX, posY);
    }
}