package com.cubeia.poker.variant.telesina;

import com.cubeia.poker.hand.Card;
import com.cubeia.poker.hand.Deck;
import com.cubeia.poker.hand.Rank;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Telesina deck. The size of the deck will vary depending on the
 * number of participants.
 *
 * @author w
 */
public class TelesinaDeck implements Deck {
    private static final long serialVersionUID = -5030565526818602010L;
    private final List<Card> cards;
    private final List<Card> dealtCards = new ArrayList<Card>();
    private int cardIdSequence = 0;
    private final int deckSize;

    private final Rank deckLowestRank;
    private final Random rng;

    /**
     * Constructs a deck with a size calculated by the number of participants given.
     * The lowest card in the deck will be 11 - <number of participants>.
     *
     * @param numberOfParticipants
     */
    public TelesinaDeck(TelesinaDeckUtil telesinaDeckUtil, Random rng, int numberOfParticipants) {
        this.rng = rng;
        checkArgument(numberOfParticipants >= 2, "participants must be >= 2");
        checkArgument(numberOfParticipants <= 10, "participants must be <= 10");
        deckLowestRank = telesinaDeckUtil.calculateLowestRank(numberOfParticipants);
        cards = new LinkedList<Card>(telesinaDeckUtil.createDeckCards(numberOfParticipants));
        deckSize = cards.size();
    }

    public int getTotalNumberOfCardsInDeck() {
        return deckSize;
    }

    /**
     * This method resets that cards to allow the rigged deck subclass to inject it's own cards.
     * Cannot be called if a cards has been dealt from the deck.
     *
     * @param newCards the cards to inject, must be of the correct size
     */
    protected void resetCards(List<Card> newCards) {
        if (!dealtCards.isEmpty()) {
            throw new IllegalStateException("deck is in use, can't reset the cards");
        }

        if (deckSize != newCards.size()) {
            throw new IllegalArgumentException("cannot reset deck of " + deckSize + " with " + newCards.size() + " number of cards");
        }

        cards.clear();
        cards.addAll(newCards);
    }

    @Override
    public Card deal() {
        if (isEmpty()) {
            throw new IllegalStateException("no more cards in deck");
        }

        int randomIndex = rng.nextInt(cards.size());
        Card card = cards.remove(randomIndex);
        card = card.makeCopyWithId(cardIdSequence++);
        dealtCards.add(card);
        return card;
    }

    @Override
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    @Override
    public List<Card> getAllCards() {
        ArrayList<Card> allCards = new ArrayList<Card>(cards);
        allCards.addAll(dealtCards);
        return allCards;
    }

    public Rank getDeckLowestRank() {
        return deckLowestRank;
    }
}
