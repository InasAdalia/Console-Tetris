package tetris;

public class GameView {
    
    GameState gameState;
    TileView tileView;
    boolean hasStarted, resetMessage=true;
    String[][] qtileView = {
        {"  ","  ","  ","  "},
        {"  ","  ","  ","  "},
        {"  ","  ","  ","  "},
        {"  ","  ","  ","  "}
    };
    Block qBlock; //block in queue
    StringBuilder messageSB = new StringBuilder();

    public GameView(TileView tileView, GameState gameState) {
        this.tileView = tileView;
        this.gameState = gameState;
        hasStarted = false;
    }

    public void showGameView(){

        //reset message area
        if (resetMessage)
            messageSB.setLength(0);
        else
            resetMessage = true;

        System.out.print("\033c");


        if (!hasStarted) {
            System.out.println("PRESS ENTER TO PLAY");
            hasStarted = true;
        } else{
            showQueue();
            System.out.println("SCORE : " + gameState.getScore());
            System.out.println("SPEED : " + gameState.getGameSpeed());
        }

        tileView.showtileView();

        System.out.println(messageSB.toString());
        

    }

    public String addMessage(String message) {
        return messageSB.append(message).toString();
    }

    public void setQueuetileView(Block block){
        //erase the previous block in queue
        eraseQueuetileView();

        //draw?

        qBlock = block;
    }

    private void eraseQueuetileView(){
        for (int i=0; i<qtileView.length; i++) {
            for (int j=0; j<qtileView[i].length; j++) {
                qtileView[i][j] = "  ";
            }
        }
    }

    public void resetMessage(boolean bool){
        resetMessage = bool;
    }

    public void showQueue(){
        System.out.println("NEXT:");

        for (int i=0; i<qtileView.length; i++) {
            // System.out.printf("%2d |", i);
            for (int j=0; j<qtileView[i].length; j++) {
                System.out.print(qtileView[i][j]);
            }
            System.out.println();
        }
    }

    public void setPatternAt(int row, int col) {    //this is for drawing block in queue
        qtileView[row][col] = Tile.BLOCK.getSymbol();
    }

    public void resetGameView(){
        tileView.clearTileView();
        eraseQueuetileView();
        gameState.resetScore();

        showGameView();
    }
}
