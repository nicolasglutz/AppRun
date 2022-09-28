package ch.bfh.memory.models;


import java.io.Serializable;


public class MemoryCard implements Serializable {

    private static final long serialVersionUID = 2L;

    public long idMemoryCard;

    public String word;

    public String path;


    public MemoryCard(String word, String path, int idMemoryCard) {
        this.word = word;
        this.path = path;
        this.idMemoryCard = idMemoryCard;
    }

    public MemoryCard(String word, String path){
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

    public void setId(long id) {
        this.idMemoryCard = id;
    }

    public long getId()
    {
        return idMemoryCard;
    }
}
