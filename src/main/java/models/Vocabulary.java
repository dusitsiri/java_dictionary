package models;

public class Vocabulary {
    private int id;
    private String word;
    private String type;
    private String meaning;
    
	public Vocabulary(int id, String word, String type, String meaning) {
		this.id = id;
		this.word = word;
		this.type = type;
		this.meaning = meaning;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMeaning() {
		return meaning;
	}

	public void setMeaning(String meaning) {
		this.meaning = meaning;
	}

    
}
