import java.util.*;

public class BlackJack {

    private final String[] SUITS = { "C", "D", "H", "S" };
    private final String[] RANKS = { "A", "2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K" };

    private final Player player;
    private final Player computer;
    private List<Card> hand;
    private int wager;
    private final Scanner in;
    private boolean ongoingTurn = true;
    private int playerScore = 0;
    private int computerScore = 0;
    private int chips = 25;
    private boolean turn = true;

    public BlackJack() {
        this.player = new Player();
        this.computer = new Player();
        this.in = new Scanner(System.in);
    }

    public void play() {
        turn = true;
        wager();
        clearHand("player");
        clearHand("computer");
        shuffleAndDeal();
        takeTurn(false);
        endRound();
    }

    private void wager() {
        do {
            System.out.println("Your total chips: " + chips);
            System.out.println("How many chips would you like to wager? (Quantity: 1-25)");
            wager = in.nextInt();
            if (wager > chips) {
                System.out.println("Sorry, you only have " + chips + " chips remaining, and you have wagered " + wager + " chips.");
            }
        } while (wager < 1 || wager > 25 || wager > chips);
        in.nextLine();
    }

    public void shuffleAndDeal() {
        if (hand == null) {
            initializeDeck();
        }
        Collections.shuffle(hand);
        while (player.getHand(false).size() < 2) {
            player.takeCard(hand.remove(0));
            computer.takeCard(hand.remove(0));
        }

        playerScore = player.calculateScore();
        computerScore = computer.calculateScore();
    }

    ////////// PRIVATE METHODS /////////////////////////////////////////////////////
    private void initializeDeck() {
        hand = new ArrayList<>(52);

        for (String suit : SUITS) {
            for (String rank : RANKS) {
                hand.add(new Card(rank, suit));
            }
        }
    }

    private void takeTurn(boolean cpu) {
        if (!cpu) {
            showHand("initial");
            String action = "x";
            while (action.equals("H") == false && action.equals("S") == false) {
                System.out.println("Hit or Stand(H or S)");
                action = in.nextLine().trim().toUpperCase();
            }
            if (action.equals("H")) {
                player.takeCard(hand.remove(0));
                playerScore = player.calculateScore();
                if (playerScore > 21) {
                    endRound();
                } else {
                    takeTurn(false);
                }

            } else if (action.equals("S")) {
                takeTurn(true);
            }
        } else if (cpu) {
            showHand("cpu");
            while (computerScore < 17) {
                computer.takeCard(hand.remove(0));
                computerScore = computer.calculateScore();
                showHand("cpu");
            }
        }
    }

    private void endRound() {
        showHand("player");
        showHand("computer");

        if (playerScore > 21) {
            System.out.println("Sorry, your score has gone over a 21 and you have lost this round.");
            chips -= (wager);
            if (chips <= 0) {
                endGame();
            }
        } else if(computerScore > 21) {
            System.out.println("The computer has gone over 21 so you have won this round.");
            chips += wager;
        } else if (computerScore == playerScore && playerScore <= 21) {
            System.out.println("You have tied with the computer this round.");
        } else if (playerScore == 21 && player.hand.size() == 2) {
            System.out.println("Congratulations, that is a BlackJack. You have won this round.");
            chips += (int) (wager * 1.5);
        } else if (computerScore > playerScore && computerScore <= 21) {
            System.out.println("You have lost this round. " + "Your score:" + playerScore + ", Computer score:" + computerScore + ".");
            chips -= wager;
        } else if (playerScore > computerScore && playerScore <= 21) {
            System.out.println("You have won this round " + "Your score:" + playerScore + ", Computer score: " + computerScore + ".");
            chips += wager;
        }
        if (chips <= 0) {
            endGame();
        }
        String answer = "";
        do {
            System.out.println("Would you like to end the game or continue playing? (Type End to end or Continue to continue)");
            answer = in.nextLine().toUpperCase();
        } while (answer.equals("END") == false && answer.equals("CONTINUE") == false);
        if (answer.equals("END")) {
            endGame();
        } else {
            play();
        }

    }

    private void showHand(String type) {
        if (type.equals("initial")) {
            System.out.println("\nPLAYER hand: " + player.getHand(false));
            System.out.println("\nCPU hand: " + computer.getHand(true));
        } else if (type.equals("cpu")) {
            System.out.println("\nCPU hand: " + computer.getHand(false));
        } else if (type.equals("player")) {
            System.out.println("\nPLAYER hand: " + player.getHand(false));
        }
    }

    private void clearHand(String type) {
        if (type.equals("player")) {
            player.clearHand();
        } else if (type.equals("computer")) {
            computer.clearHand();
        }
    }

    private void endGame() {
        if (chips <= 0) {
            System.out.println("You ran out of chips and lost.");
        } else {
            System.out.println("You ended up with " + chips + " chips. Good job!");
        }
        System.exit(0);
    }

    ////////// MAIN METHOD /////////////////////////////////////////////////////////

    public static void main(String[] args) {
        System.out.println("BLACKJACK");
        new BlackJack().play();
    }
}