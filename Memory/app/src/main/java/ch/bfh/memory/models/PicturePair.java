package ch.bfh.memory.models;

public class PicturePair {
    MemoryCard CardOne;
    MemoryCard CardTwo;

    public PicturePair(MemoryCard cardOne, MemoryCard cardTwo) {
        CardOne = cardOne;
        CardTwo = cardTwo;
    }

    public MemoryCard getCardOne() {
        return CardOne;
    }

    public void setCardOne(MemoryCard cardOne) {
        CardOne = cardOne;
    }

    public MemoryCard getCardTwo() {
        return CardTwo;
    }

    public void setCardTwo(MemoryCard cardTwo) {
        CardTwo = cardTwo;
    }
}
