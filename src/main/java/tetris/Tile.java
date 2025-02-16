package tetris;

public enum Tile {
    BLOCK("[]"),
    INACTIVE("[]"),
    GHOST("[]"),
    EMPTY("  ");


    boolean isActive= true;

    // ANSI escape codes for colors
    public static final String ANSI_RESET = "\u001B[0m";    //no color
    public static final String ANSI_CYAN = "\u001B[36m";  // light blue
    public static final String ANSI_PINK = "\u001B[35m";   // Pink
    public static final String ANSI_GRAY = "\u001B[90m";   //gray

    private final String symbol;

    Tile(String symbol) {
        this.symbol = symbol;
        this.isActive = true;
    }


    public String getSymbol() {
        switch (this) {
            case BLOCK:
                return ANSI_PINK + symbol + ANSI_RESET;
            case INACTIVE:
                return ANSI_CYAN  + symbol + ANSI_RESET;
            case GHOST:
                return ANSI_GRAY + symbol + ANSI_RESET;
            default:
                return symbol; // No color for EMPTY or other tiles
        }
    }

    public boolean isActive(){
        return this.isActive;
    }

    public void setInactive(){
        this.isActive = false;
    }

}
