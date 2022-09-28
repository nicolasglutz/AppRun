package ch.bfh.memory.models;

import java.io.Serializable;


public class MemoryPair implements Serializable {

    private static final long serialVersionUID = 1L;

    public int idMemoryPair;

    public MemoryCard cardOne;

    public MemoryCard cardTwo;


    public MemoryPair(MemoryCard cardOne, MemoryCard cardTwo) {
        this.cardOne = cardOne;
        this.cardTwo = cardTwo;
    }

    public MemoryPair(MemoryCard cardOne) {
        this.cardOne = cardOne;
    }

    public boolean isComplete()
    {
        return this.cardOne != null && this.cardTwo != null;
    }
}
