public class GameState {
    private int state; // 0 means Home screen, 1 means Playing, 2 Means end screen
    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public GameState(int state) {
        state = state;
    }
}
