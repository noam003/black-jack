// Add replay button
// new background (bully albert)
// if heart, send cute message to albert
// make an html so i can send it to him??? or he can host locally
// SQL db of cute things about albert!!!!
import java.awt.*; // graphics window 
import java.awt.event.*; // listener for events
import java.util.ArrayList; // resizeable arrays
import java.util.Random;
import javax.swing.*; // GUI

public class BlackJack {

    // card class for value and suit
    private class Card {
        String value;
        String suit;

        Card(String value, String suit) {
            this.value = value;
            this.suit = suit;
        }

        public String toString() {
            return value + "-" + suit;
        }

        public int getValue() {
            if ("AKJQ".contains(value)) {
                if (value == "A") {
                    return 11;
                } else {
                    return 10;
                }
            }
            return Integer.parseInt(value);
        }

        public boolean isAce() {
            return value == "A";
        }

        public String getImgPath() {
            return "./cards/" + toString() + ".png";
        }

    }

    ArrayList<Card> deck;
    Random random = new Random();

    // Dealer
    Card hiddenCard;
    ArrayList<Card> dealerHand;
    int dealerSum;
    int dealerAceCount; // dealer required to go past 17 so aces 1/11 matter

    // Player
    ArrayList<Card> playerHand;
    int playerSum;
    int playerAceCount;

    // Window
    int BoardWidth = 600;
    int BoardHeight =  BoardWidth;

    int cardWidth = 110; // ratio should be 1/1.4
    int cardHeight = 154;

    JFrame frame = new JFrame("Black Jack");
    JPanel gamePanel = new JPanel() {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            
            try {
                // draw hidden card
                Image hiddenCardImg = new ImageIcon(getClass().getResource("./cards/BACK.png")).getImage();
                if (!stayButton.isEnabled()) {
                    hiddenCardImg = new ImageIcon(getClass().getResource(hiddenCard.getImgPath())).getImage();
                }
                g.drawImage(hiddenCardImg, 20, 20, cardWidth, cardHeight, null); 

                // draw dealers hand
                for (int i=0; i<dealerHand.size(); i++) {
                    Card card = dealerHand.get(i);
                    Image dealImg = new ImageIcon(getClass().getResource(card.getImgPath())).getImage();
                    // i starts at zero, then moves over the distance of that card by cardwidth + 5
                    g.drawImage(dealImg, cardWidth + 25 + (cardWidth + 5)*i, 20, cardWidth, cardHeight, null); 
                }

                // draw players hand
                for (int i=0; i<playerHand.size(); i++) {
                    Card card = playerHand.get(i);
                    Image playerImg = new ImageIcon(getClass().getResource(card.getImgPath())).getImage();
                    // i starts at zero, then moves over the distance of that card by cardwidth + 5
                    g.drawImage(playerImg, 20 + (cardWidth + 5)*i, 320, cardWidth, cardHeight, null); 
                }

                if (!stayButton.isEnabled()) {
                    dealerSum = reduceDealerAce();
                    playerSum = reducePlayerAce();
                    System.out.println("STAY:");
                    System.out.println(dealerSum);
                    System.out.println(playerSum);

                    String message = "";
                    if (playerSum > 21) {
                        message = "You lose!";
                    } else if (dealerSum > 21) {
                        message = "You win!";
                    } else if (playerSum == dealerSum) {
                        message = "It is a tie.";
                    } else if (playerSum > dealerSum) {
                        message = "You win!";
                    } else {
                        message = "You lose!";
                    }

                    g.setFont(new Font("Arial", Font.PLAIN, 30));
                    g.setColor(Color.white);
                    g.drawString(message, 220, 250);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    JPanel buttonPanel = new JPanel();
    JButton hitButton = new JButton("Hit");
    JButton stayButton = new JButton("Stay");


    BlackJack() {
        startGame();

        frame.setVisible(true);
        frame.setSize(BoardWidth, BoardHeight);
        frame.setLocationRelativeTo(null); // center screen
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gamePanel.setLayout(new BorderLayout());
        gamePanel.setBackground(new Color(53, 101, 77));
        frame.add(gamePanel);

        hitButton.setFocusable(false);
        buttonPanel.add(hitButton);
        stayButton.setFocusable(false);
        buttonPanel.add(stayButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        hitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Card card = deck.remove(deck.size() - 1);
                playerSum += card.getValue();
                playerAceCount += card.isAce() ? 1 : 0;
                playerHand.add(card);
                
                if (reducePlayerAce() > 21) {
                    hitButton.setEnabled(false);
                    stayButton.setEnabled(false);
                } 
                
                gamePanel.repaint();

            }
        });

        stayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                hitButton.setEnabled(false);
                stayButton.setEnabled(false);

                while (dealerSum < 17) {
                    Card card = deck.remove(deck.size() - 1);
                    dealerSum += card.getValue();
                    dealerAceCount += card.isAce() ? 1 : 0;
                    dealerHand.add(card);
                    gamePanel.repaint();
                }
            }
        });

        gamePanel.repaint();
    }

    
    public void startGame() {
        buildDeck();
        shuffleDeck();
        dealerHand = new ArrayList<Card>();
        dealerSum = 0;
        dealerAceCount = 0;

        hiddenCard = deck.remove(deck.size() - 1); // shuffled first, now removing last card
        dealerSum += hiddenCard.getValue();
        dealerAceCount += hiddenCard.isAce() ? 1 : 0;

        Card card = deck.remove(deck.size()-1);
        dealerSum += card.getValue();
        dealerAceCount += card.isAce() ? 1 : 0;
        dealerHand.add(card);
         
        System.out.println("DEALER:");
        System.out.println(hiddenCard);
        System.out.println(dealerHand);
        System.out.println(dealerSum);
        System.out.println(dealerAceCount);

        playerHand = new ArrayList<Card>();
        playerSum = 0;
        playerAceCount = 0;

        for (int i=0; i<2; i++) {
            card = deck.remove(deck.size()-1);
            playerSum += card.getValue();
            playerAceCount += card.isAce() ? 1: 0;
            playerHand.add(card);
        }

        System.out.println("Player:");
        System.out.println(playerHand);
        System.out.println(playerSum);
        System.out.println(playerAceCount);



    }

    public void buildDeck() {
        deck = new ArrayList<>();
        String[] values = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        String [] suits = {"S", "D", "H", "C"};

        // creating deck
        for (int i=0; i<values.length; i++) {
            for (int j=0; j< suits.length; j++) {
                Card card = new Card(values[i], suits[j]);
                deck.add(card);
            }
        }
        System.out.println("THE DECK: ");
        System.out.println(deck);

    }

    public void shuffleDeck() {
        for (int i=0; i<deck.size(); i++) {
            int j = random.nextInt(deck.size());
            Card currCard = deck.get(i);
            Card randCard = deck.get(j);
            deck.set(i, randCard);
            deck.set(j, currCard);
        }
        System.out.println("POST-SHUFFLE DECK:");
        System.out.println(deck);
        }
    
    public int reducePlayerAce() {
        while (playerAceCount > 0 && playerSum > 21) {
            playerAceCount -= 1;
            playerSum -= 10;
        }
        return playerSum;
    }

    public int reduceDealerAce() {
        while (dealerAceCount > 0 && dealerSum > 21) {
            dealerAceCount -= 1;
            dealerSum -= 10;
        }
        return dealerSum;
    }
    
}
