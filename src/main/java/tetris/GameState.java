package tetris;

public class GameState {
    
    int score;
    boolean pause;
    boolean gameover;
    Double refreshTime = (double)(100);
    Double gameSpeed = 5d;

    public GameState(){ 
        this.score=0;
        this.pause=false;
        this.gameover=false;
    }

    public Double getGameSpeed(){
        return this.gameSpeed;
    }

    public Double getRefreshTime(){
        return this.refreshTime;
    }

    public void incScore(){
        this.score++;
    }

    public void resetScore(){
        this.score=0;
    }

    public int getScore(){
        return this.score;
    }

    public boolean isPaused(){
        return this.pause;
    }

    public void setPause(boolean pause){
        this.pause=pause;
    }

    public void setGameover(boolean bool){
        this.gameover=bool;
    }

}
