package scrabblos;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Word {
	private ArrayList<Letter> word;
	private String head;
	private String politicien;
	private String signature;
	
	public Word() {
		
	}
	
	public Word(ArrayList<Letter> word, String head, String politicien, String signature) {
		this.word = word;
		this.head = head;
		this.politicien = politicien;
		this.signature = signature;
	}
	
	public ArrayList<Letter> getWord() {
		return word;
	}
	public void setWord(ArrayList<Letter> word) {
		this.word = word;
	}
	public String getHash() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
	public String getPoliticien() {
		return politicien;
	}
	public void setPoliticien(String politicien) {
		this.politicien = politicien;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	
	public String wordArray() throws JSONException {
		String wordArray ="";
		for(Letter l : word) {
			wordArray += l.toString();
		}
		return wordArray;
	}
	
	/*public String toString() {
		String wordArray ="";
		for(Letter l : this.word) {
			wordArray += l.getLetter();
		}
		return "word : "+ wordArray+" ,head : "+this.head+" ,politicien : "+this.politicien + " ,signature : "+this.signature;
				
	}*/
	
	public String toString() {
	String wordArray ="[ ";
	for(Letter l : this.word) {
		System.out.println("je fais la bonne chose" + l.getLetter().toString());
		wordArray += l.toString();
		wordArray+=", ";
	}
	wordArray = wordArray.substring(0, wordArray.length()-2) + " ]";
	System.out.println("JE RENTRE ICIIII" + "{ \"word\" :" + wordArray + ", \"head\" : \""+this.head+ "\" ,\"politicien\" : \""+this.politicien + "\" , \"signature\" : "+this.signature);
	return "{ \"word\":" + wordArray + ", \"head\":\""+this.head+ "\", \"politicien\":\""+this.politicien + "\", \"signature\":\""+this.signature+"\"}";
			
}
	
}
