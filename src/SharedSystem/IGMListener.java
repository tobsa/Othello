package SharedSystem;

public interface IGMListener {
    public void updateWinner(int result, String name1, String name2);
    public void updateTurn(String name);
    public void updateLostConnection();
}
