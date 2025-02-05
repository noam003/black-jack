import java.awt.*; // graphics window 
import java.awt.event.*; // listener for events
import java.util.ArrayList; // resizeable arrays
import java.util.Random;
import javax.swing.*; // GUI

public class BlackJack {

    private class Card {
        String value;
        String type;

        Card(String value, String type) {
            this.value = value;
            this.type = type;
        }
    }

    public void buildDeck() {
        ArrayList<Card> deck = new ArrayList<>();
        String[] values = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        String [] types = {"S", "D", "H", "C"};

        // creating deck
        for (int i=0; i<values.length; i++) {
            for (int j=0; j<types.length; j++) {
                Card card = new Card(values[i], types[j]);
                deck.add(card);
            }
        }
        System.out.println("THE DECK: ");
        System.out.println(deck);

    }

    BlackJack() {
        startGame();
    }

    
    public void startGame() {
        buildDeck();
    }
}
