package NeuesBlatt0;

public class CardDeckArray {
	
	private Card[] deck;
	
	public static class Card{
		private int value;
		private String suit;
		
		public Card(int value, String suit) {
			this.value = value;
			this.suit = suit;
		}
		@Override
		public String toString() {
			return value + " of " + suit;
		}
		
		public String getSuit() {
			return suit;
		}

		public int getValue() {
			return value;
		}
	}
	
	public CardDeckArray(Card[] deck) {
		this.deck = new Card[deck.length];
		for (int i = 0; i < deck.length; i++) {
			this.deck[i] = deck[i];
		}
	}
	
	public CardDeckArray(int[] values, String[] suits) {
		deck= new Card[values.length*suits.length];
		int index = 0;
		
		for (int value: values) {
			for(String suit: suits) {
				deck[index] = new Card(value,suit);
				index++;
			}
		}
	}
	
	private void cleanDeck() {
		// Vorkommen von null zählen
		int counter = 0;
		for (int i = 0; i < deck.length; i++) {
			if(deck[i] == null) {
				counter++;
			}
		}
		Card[] newDeck = new Card[deck.length-counter];
		int index = 0;
		for (int i = 0; i < deck.length; i++) {
			if(deck[i] != null) {
				newDeck[index] = deck[i];
				index++;
			}
		}
		deck = newDeck;
	}
	
	public void removeCard(Card cardToRemove) {
		boolean cardFound = false;
		for (int i = 0; i < deck.length; i++) {
			// Wichtig, der Vergleich muss hier manuell passieren, nicht per == oder equals, da noch Fähigkeiten fehlen
			if(deck[i].value == cardToRemove.value && deck[i].suit == cardToRemove.suit) {
				cardFound = true;
				deck[i] = null;
			}
		}
		cleanDeck();
	}
	
	public Card[] drawXCards(int x) {
		if(x > deck.length) {
			x = deck.length;
		}
		Card[] result = new Card[x];
		for (int i = 0; i < x; i++) {
			int randomNumber = (int) (Math.random()*deck.length);
			
			result[i] = deck[randomNumber];
			deck[randomNumber] = null;
			cleanDeck();
		}
		return result;
	}
	
	public static CardDeckArray createDeckForScoundrel() {
		int[] values = {2,3,4,5,6,7,8,9,10,11,12,13,14};
		String[] suits = {"Spades","Clubs","Diamonds","Hearts"};
		CardDeckArray deck = new CardDeckArray(values,suits);
		
		for (int i = 11; i <= 14; i++) {
			Card temp1 = new Card(i,"Hearts");
			Card temp2 = new Card(i,"Diamonds");
			
			deck.removeCard(temp1);
			deck.removeCard(temp2);
		}
		return deck;
	}
	
	public int getNumberOfRemainingCards() {
		return deck.length;
	}
	
	public static void main(String[] args) {
		CardDeckArray deck = createDeckForScoundrel();
		
		System.out.println(deck.deck.length);
		
		Card[] test = deck.drawXCards(4);
		for (int j = 0; j < test.length; j++) {
			System.out.println(test[j]);
		}
		System.out.println(deck.deck.length);
	}
}