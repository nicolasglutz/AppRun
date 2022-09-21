package ch.bfh.memory.models;

public class MemoryPair {
    MemoryCard cardOne;
    MemoryCard cardTwo;

    public MemoryPair(MemoryCard cardOne, MemoryCard cardTwo) {
        this.cardOne = cardOne;
        this.cardTwo = cardTwo;
    }

    public MemoryPair(MemoryCard cardOne) {
        this.cardOne = cardOne;
    }

    public MemoryCard getCardOne() {
        return cardOne;
    }

    public void setCardOne(MemoryCard cardOne) {
        this.cardOne = cardOne;
    }

    public MemoryCard getCardTwo() {
        return cardTwo;
    }

    public void setCardTwo(MemoryCard cardTwo) {
        this.cardTwo = cardTwo;
    }

    public boolean isComplete()
    {
        return getCardOne() != null && getCardTwo() != null;
    }
}
