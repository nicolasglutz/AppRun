package ch.bfh.memory.models;

public class MemoryCard {
    String word;
    String path;
    String id;

    public MemoryCard(String word, String path, String id) {
        this.word = word;
        this.path = path;
        this.id = id;
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

    public void setId(String id) {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }
}
