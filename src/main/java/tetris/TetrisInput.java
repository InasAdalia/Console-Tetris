package tetris;

public class TetrisInput {

    private GameController gameController;

    // // Load the C++ library
    static {
        System.loadLibrary("tetris_input"); // This must match your compiled DLL name
    }

    // static {
        
    //     System.load("C:\\This PC\\Desktop\\VSCodeFolder\\tetris-game\\tetris_input.dll");
    // }
    

    public TetrisInput(GameController gameController){
        this.gameController = gameController;
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
        gameController.moveActiveBlock("DOWN");
        System.out.println("key down ");
    }

    public void keyRight(){
        gameController.moveActiveBlock("RIGHT");
        System.out.println("key right ");
    }

    public void keyLeft(){
        gameController.moveActiveBlock("LEFT");
        System.out.println("key left ");
    }

    public void keyRotateR(){
        gameController.moveActiveBlock("ROTATER");
        System.out.println("key rotate right ");
    }

    public void keyRotateL(){
        gameController.moveActiveBlock("ROTATEL");
        System.out.println("key rotate left ");
    }

    public void keySpace(){
        gameController.moveActiveBlock("SPACE");
        // System.out.println("key space ");
    }

    public void keyEsc(){
        System.exit(0);
    }
}

