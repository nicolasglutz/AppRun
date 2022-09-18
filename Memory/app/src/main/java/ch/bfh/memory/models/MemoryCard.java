package ch.bfh.memory.models;

public class MemoryCard {
    String word;
    String path;

    public MemoryCard(String word, String path) {
        this.word = word;
        this.path = path;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
