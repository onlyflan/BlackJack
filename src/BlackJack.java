import java.util.ArrayList;
import java.util.Random;

public class BlackJack {

    public class Card {
        String value;
        String type;

        Card(String value, String type) {
            this.value = value;
            this.type = type;
        }

        public String toString() {
            return value + "-" + type;
        }

        // change the card into the point value
        public int getValue() {
            if ("AJQK".contains(value)) { // value A -> K
                if (value.equals("A")) {
                    return 11;
                }
                return 10;
            }
            return Integer.parseInt(value); // value from 2 - 10
        }

        // check if card is Ace or not
        public boolean isAce() {
            return value.equals("A");
        }

        public String getImagePath() {
            return "./card_images/" + toString() + ".png";
        }
    }

    ArrayList<Card> deck; // create deck
    Random r = new Random(); // shuffle deck

    // dealer init
    Card hiddenCard;
    ArrayList<Card> dealerHand;
    int dealerSum;
    int dealerAceCount;

    // player init
    ArrayList<Card> playerHand;
    int playerSum;
    int playerAceCount;

    private GUI gui;

    private int round = 1;
    private int wins = 0;
    private int losses = 0;
    private int ties = 0;
    private int currentScore = 100;
    private int highestScore = 100;

    private boolean gameOver = false;

    public void setGUI(GUI gui) {
        this.gui = gui;
    }

    public void playerHit() {
        Card card = deck.remove(deck.size() - 1);
        playerSum += card.getValue();
        playerAceCount += card.isAce() ? 1 : 0;
        playerHand.add(card);
        if (reducePlayerAce() >= 21) {
            gui.disableHitButton();
            gui.disableStayButton();
            if (playerSum == 21) {
                dealerTurn();
            } else {
                determineWinner();
            }
            gui.enableNextRoundButton();
        }
        gui.updateUI(); // Notify the GUI to update the UI after the player hits
    }

    public void dealerTurn() {
        while (dealerSum < 17) {
            Card card = deck.remove(deck.size() - 1);
            dealerSum += card.getValue();
            dealerAceCount += card.isAce() ? 1 : 0;
            dealerHand.add(card);
        }
        gui.updateUI();
        determineWinner();
    }

    private void determineWinner() {
        // Determine winner logic and update GUI accordinglyb

        if (dealerSum == 21 && dealerHand.size() == 1 && playerSum < 21) {
            losses++;
            updateScore(-10);
        } else if (dealerSum < 21 && playerHand.size() == 2 && playerSum == 21) {
            wins++;
            updateScore(10);
        } else if (dealerSum == 21 && playerHand.size() == 2 && playerSum == 21) {
            ties++;
        } else {
            if (playerSum > 21) {
                losses++;
                updateScore(-10);
            } else if (dealerSum > 21) {
                wins++;
                updateScore(10);
            } else if (playerSum == dealerSum) {
                ties++;
            } else if (dealerSum < playerSum) {
                wins++;
                updateScore(10);
            } else if (playerSum < dealerSum) {
                losses++;
                updateScore(-10);
            }
        }

        gui.updateUI();
    }

    public void startGame() {
        gameStarted = true;

        // build and shuffle the deck
        buildDeck();
        shuffleDeck();

        // dealer cards info
        dealerHand = new ArrayList<Card>();
        dealerSum = 0;
        dealerAceCount = 0;

        // dealer gets 2 card at first
        hiddenCard = deck.remove(deck.size() - 1); // remove card at last index
        dealerSum += hiddenCard.getValue();
        dealerAceCount += hiddenCard.isAce() ? 1 : 0;
        // dealerSum += 21; // test when dealer has BlackJack
        Card card = deck.remove(deck.size() - 1);
        dealerSum += card.getValue();
        dealerAceCount += card.isAce() ? 1 : 0;
        dealerHand.add(card);
        if (dealerSum == 21) {
            gui.disableHitButton();
            gui.disableStayButton();
            gui.enableNextRoundButton();
            gui.updateUI();
            determineWinner();
        }
        // player cards info
        playerHand = new ArrayList<Card>();
        playerSum = 0;
        playerAceCount = 0;

        for (int i = 0; i < 2; i++) {
            card = deck.remove(deck.size() - 1);
            // // test when player has BlackJack
            // if (i == 0) {
            // playerSum += 10;
            // } else {
            // playerSum += 11;
            // }
            playerSum += card.getValue();
            playerAceCount += card.isAce() ? 1 : 0;
            playerHand.add(card);
        }
        if (playerSum == 21) {
            gui.disableHitButton();
            gui.disableStayButton();
            gui.enableNextRoundButton();
            gui.updateUI();
            determineWinner();
        }

        gui.updateUI();
    }

    public void startNewRound() {
        // clear hand after a round finished
        if (currentScore > highestScore) {
            highestScore = currentScore;
        }
        checkGameOver();
        round++;
        playerHand.clear();
        dealerHand.clear();

        // Build and shuffle the deck again to start new round
        buildDeck();
        shuffleDeck();

        dealerHand.add(hiddenCard);

        gui.enableHitButton();
        gui.enableStayButton();
        gui.disableNextRoundButton();
        startGame();

        gui.updateUI();
    }

    private boolean gameStarted = false;

    public boolean isGameStarted() {
        return gameStarted;
    }

    public void resetStats() {
        round = 1;
        currentScore = 100;
        highestScore = 100;
        wins = 0;
        losses = 0;
        ties = 0;
    }

    private void checkGameOver() {
        if (currentScore == 0 && !gameOver) {
            gui.showGameOverDialog();
            gameOver = true;
        }
    }

    public void endGame() {
        gui.showEndGameDialog();
        gameOver = true;
    }

    public void buildDeck() {
        deck = new ArrayList<Card>();
        String[] values = { "A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K" };
        String[] types = { "C", "D", "H", "S" };

        for (int i = 0; i < types.length; i++) {
            for (int j = 0; j < values.length; j++) {
                Card card = new Card(values[j], types[i]);
                deck.add(card);
            }
        }
    }

    public void shuffleDeck() {
        for (int i = 0; i < deck.size(); i++) {
            int j = r.nextInt(deck.size());
            Card currCard = deck.get(i);
            Card randomCard = deck.get(j);
            deck.set(i, randomCard);
            deck.set(j, currCard);
        }
    }

    // check if player or dealer has Ace and point > 21 => Ace from 11 turns into 1
    public int reducePlayerAce() {
        while (playerSum > 21 && playerAceCount > 0) {
            playerSum -= 10;
            playerAceCount -= 1;
        }
        return playerSum;
    }

    public int reduceDealerAce() {
        while (dealerSum > 21 && dealerAceCount > 0) {
            dealerSum -= 10;
            dealerAceCount -= 1;
        }
        return dealerSum;
    }

    public int getRound() {
        return round;
    }

    public int getScore() {
        return currentScore;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public int getTies() {
        return ties;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public int getHighestScore() {
        return highestScore;
    }

    public void updateScore(int amount) {
        currentScore += amount;
    }
}
