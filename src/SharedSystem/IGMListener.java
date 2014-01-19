package SharedSystem;

public interface IGMListener {
    public void updateWinner(int result, String name1, String name2);
    public void updateTurn(String name);
    public void updateLostConnection();
    public void updatePassButton(boolean enable);
    public void updateScore(String name1, int score1, String name2, int score2);
}
