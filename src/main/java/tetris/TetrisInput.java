package tetris;

public class TetrisInput {

    private GameController gameController;
    private GameState gameState;

    // Load the C++ library
    static {
        System.loadLibrary("tetris_input"); // This must match your compiled DLL name
    }

    // static {
        
    //     System.load("C:\\This PC\\Desktop\\VSCodeFolder\\tetris-game\\tetris_input.dll");
    // }
    

    public TetrisInput(GameController gameController, GameState gameState){
        this.gameController = gameController;
        this.gameState = gameState;
    }

    public void startKeyListener(){
        new Thread(new Runnable(){
            @Override
            public void run() {
                getKeyPress();
                System.out.println("listening for keys");
            }
        }).start();
    }

    public native void getKeyPress();

    public void keyDown(){
        System.out.println("trying key down");
        gameController.moveActiveBlock(Movements.DOWN);
        System.out.println("key down ");
    }

    public void keyRight(){
        gameController.moveActiveBlock(Movements.RIGHT);
        System.out.println("key right ");
    }

    public void keyLeft(){
        gameController.moveActiveBlock(Movements.LEFT);
        System.out.println("key left ");
    }

    public void keyRotateR(){
        gameController.moveActiveBlock(Movements.ROTATER);
        System.out.println("key rotate right ");
    }

    public void keyRotateL(){
        gameController.moveActiveBlock(Movements.ROTATEL);
        System.out.println("key rotate left ");
    }

    public void keySpace(){
        gameController.moveActiveBlock(Movements.DROP);
        // System.out.println("key space ");
    }

    public void keyEsc(){
        System.exit(0);
    }

    public void keyPause(){
        if (gameController.runGame != null)
            gameController.startGame();
    }

    public void keyRestart(){
        gameController.resetGame();
    }
}

