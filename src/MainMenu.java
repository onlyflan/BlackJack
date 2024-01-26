import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu {

    private JFrame frame = new JFrame("Main Menu");
    private JButton playButton = new JButton("Play");
    private JButton instructionButton = new JButton("Instructions");
    private JButton quitButton = new JButton("Quit");
    public JButton continueButton = new JButton("Continue");
    public JButton endGameButton = new JButton("End game");
    private BlackJack bj;

    public MainMenu(BlackJack bj) {
        this.bj = bj;
    }

    public void showMainMenu() {
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        if (bj.isGameStarted()) {
            panel.add(continueButton);
            panel.add(endGameButton);

            continueButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.dispose();
                }
            });

            playButton = new JButton("New game");
        }

        playButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                bj.resetStats();
                bj.startGame();
                endGameButton.setVisible(false);
            }
        });

        instructionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextArea instructionTextArea = new JTextArea();
                instructionTextArea.setEditable(false);
                instructionTextArea.setLineWrap(true);
                instructionTextArea.setWrapStyleWord(true);
                instructionTextArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                String instructions = "Instruction to Blackjack:\n" +
                        "Blackjack, known by some as ''21'', is a beloved card game where players aim to beat the dealer with a hand that most closely totals 21 points. While it's mainly a game of luck and chance, Blackjack has its fair share of strategies that can help you get the most out of your bets and payouts. We'll teach you everything you need to know, including how to set up your game, how to play, and how to maximize your chances of success in a round.\n\n"
                        +
                        "- The goal of the game is to get a higher hand than the dealer without going over 21. Going over 21 is called busting.\n"
                        +
                        "- If a player has a better hand than the dealer without busting, they win their bet. If they have a worse hand, the dealer takes their bet.\n\n"
                        +
                        "The goal of Blackjack is to beat the dealer's hand without going over 21.\n\n" +
                        "In Blackjack, in a given round, the player's ultimate goal is to get a hand that's higher than the dealer's (without being higher than 21, or 'busting'). Only by beating the dealer can a player win their bet. Players determine the value of their hand by tallying up the point values of their cards.\n"
                        +
                        "+ 2 through 10: The number listed on the card (e.g., 2 is worth 2 points).\n" +
                        "Jack, Queen, King: 10 points\n" +
                        "+ Ace: 1 or 11 points (the player gets to choose)\n" +
                        "+ An Ace and a 10, Jack, Queen, or King equals 21 points and is known as a Blackjack.\n\n" +
                        "Blackjack Rules:\n" +
                        "1. The dealer gives a card to each player as well as themselves.n" +
                        "- The dealer hands a card face-up to each player and then places a card face-down in front of themselves. It's okay if the players can see each others' hands'all that matters is that they can't see the dealer's first card.\n"
                        +
                        "- A 52-card card deck is used to play Blackjack. The dealer needs to remove the Jokers and shuffle the cards before distributing them.\n"
                        +
                        "2. The dealer hands out a second card to every player.\n" +
                        "- As they did before, the dealer passes another face-up card to each player. This time, though, the dealer places their second card face-up in their hand (leaving the first card face-down still).\n"
                        +
                        "3. Decide if you want to stay or hit.\n" +
                        "- Take a look at your 2 cards and add the numerical total together, how close is it to 21, and how does it compare to the dealer's hand? If the total is pretty high (like 17 or 18, which is very close to 21),''stayin'' (leaving your hand as-is) is probably your best option; if your total is on the low end (like in the single digits), ''hitting,'' or getting another card added to your hand, could be beneficial.\n"
                        +
                        "- Staying simply means that you don't want the dealer to give you another card that'll get added to your total. This is signified by holding your hand flat and waving it.\n"
                        +
                        "- Hitting means that you'd like the dealer to add another card to your hand and is signified by tapping the game table with your pointer and middle finger. You can hit as many times as you'd like until you reach or go over 21.\n"
                        +
                        "4.Cycle through each player until they've each finished their turn\n" +
                        "- Give each player time to look over their cards and let the dealer know if they'd like to hit or stay. Players who hit too often may end up busting or getting a card total that's over 21. They automatically lose the round as well as their initial bet.\n"
                        +
                        "5.The dealer reveals their second card and winners are determined\n" +
                        "- At this point, the dealer flips over their original card to reveal their hand total. If the total is 16 or lower, they're required to hit and take another card. If the card is 17 or higher, the dealer is required to stay.\n"
                        +
                        "- If the dealer gets a Blackjack, all the players automatically lose the round unless they have a Blackjack themselves. In this case, they push:in other words, the player who got Blackjack simply gets their original bet back. A push also occurs anytime the player's hand matches the dealer's.\n\n"
                        +
                        "At first, you will have 100 points. Each win round will gives you 10 point; otherwise, each lose round will takes you 10 points. The game will end if you want or your points become 0";
                instructionTextArea.setText(instructions);

                JScrollPane scrollPane = new JScrollPane(instructionTextArea);
                scrollPane.setPreferredSize(new Dimension(600, 400));
                scrollPane.getVerticalScrollBar().setValue(0);

                JDialog dialog = new JDialog();
                dialog.setTitle("Blackjack Instructions");
                dialog.setLayout(new BorderLayout());
                dialog.add(scrollPane, BorderLayout.CENTER);
                dialog.setSize(800, 600);
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);
            }
        });

        endGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bj.endGame();
                continueButton.setVisible(false);
                endGameButton.setVisible(false);
            }
        });

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        panel.add(playButton);
        panel.add(instructionButton);
        panel.add(quitButton);

        frame.add(panel);
        frame.setVisible(true);
    }

}
