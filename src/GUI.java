import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI {
    private int boardWidth = 900;
    private int boardHeight = 650;

    private int cardWidth = 110; // ratio should be 1/1.4
    private int cardHeight = 154;

    private Image backgroundImage;

    private boolean gameOverDialogShown = false;

    private JFrame frame = new JFrame("Black Jack");
    private JFrame gameOverFrame = new JFrame("Game Over");

    private JPanel gamePanel = new JPanel() {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }

            try {
                if (blackjack != null && blackjack.isGameStarted()) {
                    Image hiddenCardImg = new ImageIcon(getClass().getResource("./card_images/Back.png")).getImage();
                    if (!stayButton.isEnabled()) {
                        hiddenCardImg = new ImageIcon(getClass().getResource(blackjack.hiddenCard.getImagePath()))
                                .getImage();
                    }
                    g.drawImage(hiddenCardImg, 20, 20, cardWidth, cardHeight, null);

                    if (blackjack != null && blackjack.dealerHand != null) {
                        for (int i = 0; i < blackjack.dealerHand.size(); i++) {
                            BlackJack.Card card = blackjack.dealerHand.get(i);
                            Image cardImage = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
                            g.drawImage(cardImage, cardWidth + 25 + (cardWidth + 5) * i, 20, cardWidth, cardHeight,
                                    null);
                        }
                    }

                    if (blackjack != null && blackjack.playerHand != null) {
                        for (int i = 0; i < blackjack.playerHand.size(); i++) {
                            BlackJack.Card card = blackjack.playerHand.get(i);
                            Image cardImage = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
                            g.drawImage(cardImage, 20 + (cardWidth + 5) * i, 320, cardWidth, cardHeight, null);
                        }

                        int playerSum = blackjack.reducePlayerAce();

                        // Display current player score
                        g.setFont(new Font("Arial", Font.PLAIN, 20));
                        g.setColor(Color.white);
                        g.drawString("Player Score: " + playerSum, 20, 500);

                        g.drawString("Round " + blackjack.getRound() + ":", getWidth() - 200, 40);
                        g.drawString("Score: " + blackjack.getScore(), getWidth() - 200, 80);
                        g.drawString("Wins: " + blackjack.getWins(), getWidth() - 200, 100);
                        g.drawString("Losses: " + blackjack.getLosses(), getWidth() - 200, 120);
                        g.drawString("Ties: " + blackjack.getTies(), getWidth() - 200, 140);

                        if (!stayButton.isEnabled()) {
                            int dealerSum = blackjack.reduceDealerAce();

                            // Display current dealer score
                            g.drawString("Dealer Score: " + dealerSum, 20, 200);

                            String message = "";
                            if (dealerSum == 21 && blackjack.dealerHand.size() == 1 && playerSum < 21) {
                                message = "Dealer has BlackJack. You Lose!";
                            } else if (dealerSum < 21 && blackjack.playerHand.size() == 2 && playerSum == 21) {
                                message = "You has BlackJack. You Win!";
                            } else if (dealerSum == 21 && blackjack.playerHand.size() == 2 && playerSum == 21) {
                                message = "Both dealer and player have BlackJack!";
                                String tieMessage = "Push!";
                                g.setFont(new Font("Arial", Font.PLAIN, 30));
                                g.setColor(Color.white);
                                int tieMessageWidth = g.getFontMetrics().stringWidth(tieMessage);
                                g.drawString(tieMessage, (frame.getWidth() - tieMessageWidth) / 2, 300);
                            } else {
                                if (playerSum > 21) {
                                    message = "Busted! You Lose!";
                                } else if (dealerSum > 21) {
                                    message = "You Win!";
                                } else if (playerSum == dealerSum) {
                                    message = "Push!";
                                } else if (dealerSum < playerSum) {
                                    message = "You Win!";
                                } else if (playerSum < dealerSum) {
                                    message = "You Lose!";
                                }
                            }

                            g.setFont(new Font("Arial", Font.PLAIN, 30));
                            g.setColor(Color.white);
                            int messageWidth = g.getFontMetrics().stringWidth(message);
                            g.drawString(message, (frame.getWidth() - messageWidth) / 2, 270);
                            enableNextRoundButton();
                        }
                    }

                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    };

    JPanel menuPanel = new JPanel();
    private JPanel buttonPanel = new JPanel();
    private JButton hitButton = new JButton("Hit");
    private JButton stayButton = new JButton("Stand");
    private JButton nextRoundButton = new JButton("Next Round");
    private JButton mainMenuButton = new JButton("Main Menu");

    private BlackJack blackjack;

    public GUI(BlackJack blackjack) {
        this.blackjack = blackjack;
        // Load background image
        backgroundImage = new ImageIcon(getClass().getResource("/card_images/background.jpg")).getImage();
        initializeGUI();

        nextRoundButton.setFocusable(false);
        nextRoundButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                blackjack.startNewRound();
                updateUI();
            }
        });

        mainMenuButton.setFocusable(false);
        mainMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showMainMenu();
            }
        });
        buttonPanel.add(mainMenuButton);
        frame.revalidate();

    }

    private void initializeGUI() {
        gamePanel.setLayout(new BorderLayout());
        gamePanel.setBackground(new Color(53, 101, 77));
        frame.add(gamePanel);

        int messagePanelHeight = 50;

        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight + messagePanelHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gamePanel.setVisible(true);
        GUI.this.enableHitButton();
        GUI.this.enableStayButton();
        GUI.this.disableNextRoundButton();
        updateUI();
        buttonPanel.add(hitButton);
        buttonPanel.add(stayButton);
        buttonPanel.add(nextRoundButton);
        buttonPanel.add(menuPanel);

        hitButton.setFocusable(false);
        stayButton.setFocusable(false);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.revalidate();

        hitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                blackjack.playerHit();
                updateUI();
            }
        });

        stayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                hitButton.setEnabled(false);
                stayButton.setEnabled(false);

                blackjack.dealerTurn();
                updateUI();
            }
        });

        updateUI();
    }

    private void checkGameOver() {
        if (blackjack.getScore() <= 0 && !gameOverDialogShown) {
            showGameOverDialog();
            gameOverDialogShown = true;
        }
    }

    public void showGameOverDialog() {
        gameOverFrame.setSize(300, 150);
        gameOverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameOverFrame.setLocationRelativeTo(null);
        gameOverFrame.setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel messageLabel = new JLabel("Game Over! Your Highest Score was " + blackjack.getHighestScore() + ".");
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(messageLabel);

        panel.add(Box.createVerticalStrut(10));

        JButton okButton = new JButton("OK");
        okButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameOverFrame.dispose();
                showMainMenu();
            }
        });
        panel.add(okButton);

        gameOverFrame.add(panel);
        gameOverFrame.setVisible(true);
    }

    public void showEndGameDialog() {
        gameOverFrame.setSize(300, 150);
        gameOverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameOverFrame.setLocationRelativeTo(null);
        gameOverFrame.setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel messageLabel = new JLabel("Game Ended! Your Highest Score was " + blackjack.getHighestScore() + ".");
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(messageLabel);

        panel.add(Box.createVerticalStrut(10));

        JButton okButton = new JButton("OK");
        okButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameOverFrame.dispose();
            }
        });
        panel.add(okButton);

        gameOverFrame.add(panel);
        gameOverFrame.setVisible(true);
    }

    private void showMainMenu() {
        MainMenu mainMenu = new MainMenu(blackjack);
        mainMenu.showMainMenu();
    }

    public void updateUI() {
        gamePanel.repaint();
        checkGameOver();

    }

    public void showStartScreen() {
        gamePanel.setVisible(false);
        updateUI();
    }

    public void disableHitButton() {
        hitButton.setEnabled(false);
    }

    public void disableStayButton() {
        stayButton.setEnabled(false);
    }

    public void disableNextRoundButton() {
        nextRoundButton.setEnabled(false);
    }

    public void enableHitButton() {
        hitButton.setEnabled(true);
    }

    public void enableStayButton() {
        stayButton.setEnabled(true);
    }

    public void enableNextRoundButton() {
        nextRoundButton.setEnabled(true);
    }

    public int getBoardWidth() {
        return boardWidth;
    }

    public void setBoardWidth(int boardWidth) {
        this.boardWidth = boardWidth;
    }

    public int getBoardHeight() {
        return boardHeight;
    }

    public void setBoardHeight(int boardHeight) {
        this.boardHeight = boardHeight;
    }

    public int getCardWidth() {
        return cardWidth;
    }

    public void setCardWidth(int cardWidth) {
        this.cardWidth = cardWidth;
    }

    public int getCardHeight() {
        return cardHeight;
    }

    public void setCardHeight(int cardHeight) {
        this.cardHeight = cardHeight;
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public JPanel getGamePanel() {
        return gamePanel;
    }

    public void setGamePanel(JPanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public JPanel getButtonPanel() {
        return buttonPanel;
    }

    public void setButtonPanel(JPanel buttonPanel) {
        this.buttonPanel = buttonPanel;
    }

    public JButton getHitButton() {
        return hitButton;
    }

    public void setHitButton(JButton hitButton) {
        this.hitButton = hitButton;
    }

    public JButton getStayButton() {
        return stayButton;
    }

    public void setStayButton(JButton stayButton) {
        this.stayButton = stayButton;
    }

    public BlackJack getBlackjack() {
        return blackjack;
    }

    public void setBlackjack(BlackJack blackjack) {
        this.blackjack = blackjack;
    }
}
