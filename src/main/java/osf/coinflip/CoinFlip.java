package osf.coinflip;

import java.util.HashMap;
import java.util.UUID;

public class CoinFlip {

    public static HashMap<UUID, Bet> bets = new HashMap<>();
    public static CoinFlipGUI coinFlipGUI;


    public void CoinFlip() {
        coinFlipGUI = new CoinFlipGUI();
    }

    public static HashMap<UUID, Bet> getBets() {
        return bets;
    }

    public static void addBet(Bet bet) {
        bets.put(bet.getPlayer().getUniqueId(), bet);
    }
    public static void removeBet(UUID uuid) {
        bets.remove(uuid);
    }
}
