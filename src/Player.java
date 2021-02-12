import java.util.ArrayList;
import java.util.List;

public class Player {

    public final List<Card> hand;
    private int chips = 25;

    public Player() {
        this.hand = new ArrayList<>();
    }

    public List<Card> getHand(boolean partial) {
        if (partial == true) {
            return hand.subList(0,1);
        } else {
            return hand;
        }
    }

    public void takeCard(Card card) {
        hand.add(card);
        sortHand();
    }

    public void clearHand() {
        hand.clear();
    }

    public int calculateScore() {
        int score = 0;
        for (int k = 0; k < hand.size(); k++) {
            if (hand.get(k).getRank() != "A" && hand.get(k).getRank() != "11" && hand.get(k).getRank() != "1") {
                score += Card.getOrderedRank(hand.get(k).getRank());
            }
        }
        for (int h = 0; h<hand.size(); h++) {
            if (hand.get(h).getRank() == "A" || hand.get(h).getRank() == "11" || hand.get(h).getRank() == "1") {
                if (score + 11 > 21) {
                    hand.get(h).setRank("1");
                } else {
                    hand.get(h).setRank("11");
                }
                score += Card.getOrderedRank(hand.get(h).getRank());
            }
        }
        return score;
    }

    private int addList(ArrayList<Integer> list) {
        int total = 0;
        for (int j = 0; j < list.size(); j++) {
            total += list.get(j);
        }
        return total;
    }

    private void sortHand() {
        hand.sort((a, b) -> {
            if (Card.getOrderedRank(a.getRank()) == Card.getOrderedRank(b.getRank())) {
                return Card.getOrderedSuit(a.getSuit()) - Card.getOrderedSuit(b.getSuit());     
            }

            return Card.getOrderedRank(a.getRank()) - Card.getOrderedRank(b.getRank());
        });
    }

}