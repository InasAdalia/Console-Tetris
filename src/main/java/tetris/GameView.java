package tetris;

public class GameView {
    
    GameState gameState;
    TileLayout layout;
    boolean hasStarted;
    String[][] qLayout = {
        {"  ","  ","  ","  "},
        {"  ","  ","  ","  "},
        {"  ","  ","  ","  "},
        {"  ","  ","  ","  "}
    };
    Block qBlock; //block in queue
    StringBuilder messageSB = new StringBuilder();

    public GameView(TileLayout layout, GameState gameState) {
        this.layout = layout;
        this.gameState = gameState;
        hasStarted = false;
    }

    public void showGameLayout(){

        //reset message area
        messageSB.setLength(0);

        System.out.print("\033c");


        if (!hasStarted) {
            System.out.println("PRESS ENTER TO PLAY");
            hasStarted = true;
        } else{
            showQueue();
            System.out.println("SCORE : " + gameState.getScore());
        }

        layout.showLayout();

        System.out.println(messageSB.toString());
        

    }

    public String addMessage(String message) {
        return messageSB.append(message).toString();
    }

    public void setQueueLayout(Block block){
        //erase the previous block in queue
        for (int i=0; i<qLayout.length; i++) {
            for (int j=0; j<qLayout[i].length; j++) {
                qLayout[i][j] = "  ";
            }
        }

        //draw?

        qBlock = block;
    }

    public void showQueue(){
        System.out.println("NEXT:");

        for (int i=0; i<qLayout.length; i++) {
            // System.out.printf("%2d |", i);
            for (int j=0; j<qLayout[i].length; j++) {
                System.out.print(qLayout[i][j]);
            }
            System.out.println();
        }
    }

    public void setPatternAt(int row, int col) {    //this is for drawing block in queue
        qLayout[row][col] = Tile.BLOCK.getSymbol();
    }
}
