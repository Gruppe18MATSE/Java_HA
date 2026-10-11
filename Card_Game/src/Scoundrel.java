
import java.util.Collections;
import java.util.Scanner;
public class Scoundrel {
    CardDeckArray deck;
    int health;
    int weapon;
    int weaponBound;
    boolean avoidedLastTurn;
    CardDeckArray.Card[] currentRooms;
    CardDeckArray.Card[] remainingRooms;
    public Scoundrel() {
        health = 20;
        weapon = 0;
        avoidedLastTurn = false;
        weaponBound = 14;
        deck = CardDeckArray.createDeckForScoundrel();
        currentRooms = new CardDeckArray.Card[0];
        remainingRooms = new CardDeckArray.Card[0];
    }

    /**
     * @param a Deck 1
     * @param b Deck 2
     * @return Merged Deck of a and b
     */
    private static CardDeckArray.Card[] mergeCards(CardDeckArray.Card[] a,
                                                   CardDeckArray.Card[] b) {
        CardDeckArray.Card[] result = new CardDeckArray.Card[a.length + b.length];
        //int index = 0;
        for (int i = 0; i < a.length; i++) {
            result[i] = a[i];
        }
        for (int i = 0; i < b.length; i++) {
            result[i + a.length] = b[i];
        }
        return result;
    }

    /**
     * Creates new rooms from the deck and current rooms
     */
    public void newRooms() {
        int visibleRooms = currentRooms.length;
        CardDeckArray.Card[] newRooms = deck.drawXCards(4- visibleRooms);
        currentRooms = mergeCards(currentRooms, newRooms);
    }

    /**
     * add remaining rooms back to the deck
     */
    public void avoidRooms() {
        avoidedLastTurn = true;
        remainingRooms = mergeCards(remainingRooms, currentRooms);
        currentRooms = new CardDeckArray.Card[0];
        newRooms();
    }

    /**
     *
     * @return Gives out an overview of the current gamestate
     */
    @Override
    public String toString() {
        return
        "--------\n"+
        "Health: " +health+"\n"+
        "Weapon damage: "+this.weapon+"\n"+
        "Weapon binding: "+this.weaponBound+"\n"+
        "Remaining rooms: "+this.remainingRooms.length+"\n"+
        "Remaining cards: "+this.deck.getNumberOfRemainingCards()+"\n"+
        "--------\n";
    }

    /**
     * Outputs the current rooms and the avoided rooms
     * @return the number of possible decisions
     */
    private int printDecisions() {
        System.out.println(this);
        int i = 0;
        for (; i < currentRooms.length; i++) {
            System.out.println(i + ": " + currentRooms[i]);
        }
        if (!avoidedLastTurn) {
            System.out.println(i + ": avoid Rooms");
            i++; // i is now the number of possible decisions
        }
        return i;
    }

    /**
     * Gets user input to select a room
     */
    public void chooseRoom() {
        int numberOfDecisions = printDecisions();// gives the number of possible decisions
// Player chooses a room
        Scanner scanner = new Scanner(System.in);
        int decision = 0;
        boolean noInput = true;
        while (noInput) {
            try {
                decision = Integer.parseInt(scanner.nextLine());
                if (decision < numberOfDecisions && numberOfDecisions >= 0) {
                    noInput = false;
                } else {
                    System.out.println("You have to choose one of the " +
                            numberOfDecisions + " decisions.");
                }
            } catch (RuntimeException e) {
                System.out.println("You have to choose one of the " +
                        numberOfDecisions + " decisions.");
            }
        }
        System.out.println();
// Chosen room is evaluated or current rooms are avoided
        try {
            playRoom(currentRooms[decision]);
            removeRoom(currentRooms[decision]);
        } catch (RuntimeException e) {
            avoidRooms();
        }
    }

    /**
     * remove the given card from the Array currentRooms
     * @param card Card to remove from deck
     */
    private void removeRoom(CardDeckArray.Card card) {
        //CardDeckArray.Card[] result = new CardDeckArray.Card[currentRooms.length -1];
        this.deck.removeCard(card);

    }

    /**
     * Plays a card in the current room
     * @param card Card to play in current room
     */
    private void playRoom(CardDeckArray.Card card) {
// "Spades","Clubs","Diamonds","Hearts"
        switch (card.getSuit()) {
            case "Spades", "Clubs"-> {
                int monsterStrength = card.getValue();
                if (monsterStrength <= weaponBound) {
// Weapon can be used
                    int damage = monsterStrength- weapon;
                    if (damage < 0) {
                        damage = 0;
                    }
                    health-= damage;
                    weaponBound = monsterStrength;
                } else {
                    System.out.println("Weapon is to broken to fight this monster");
                }
            }
            case "Diamonds"-> {
                weapon = card.getValue();
                weaponBound = 14; // the first time a weapon is used, it is unbound. So we set this value to the maximum monster strength.
            }
            case "Hearts"-> {
                health += card.getValue();
                if (health > 20) {
                    health = 20;
                }
            }
            default-> {
                throw new IllegalArgumentException("Unexpected value: ");
            }
        }
    }

    /**
     * Starts the game
     * Ends the game when health or cards reach 0
     */
    public static void playScoundrel() {
        Scoundrel game = new Scoundrel();
        while (game.health > 0 && (game.deck.getNumberOfRemainingCards() +
                game.currentRooms.length + game.remainingRooms.length) > 0) {
            if (game.currentRooms.length < 2) {
// last room was finished, we need new Cards. Player did not avoid rooms
                game.newRooms();
                game.avoidedLastTurn = false;
            }
            if(game.deck.getNumberOfRemainingCards() == 0) {
// no remaining cards in deck. Avoided rooms need to become a new deck.
                game.deck = new CardDeckArray(game.remainingRooms);
                game.remainingRooms = new CardDeckArray.Card[0];
            }
            game.chooseRoom();
        }
        if(game.health > 0) {
            System.out.println("Congratulations, You won!");
        } else {
            System.out.println("Game Over");
        }
    }
    public static void main(String[] args) {
        Scoundrel.playScoundrel();
       Scoundrel spiel1 = new Scoundrel();
        System.out.println(spiel1);
        System.out.println(spiel1.printDecisions());
        Scoundrel spiel2 = new Scoundrel();
        //CardDeckArray.Card[] hi = Scoundrel.mergeCards(spiel1.deck,spiel2);

    }
}