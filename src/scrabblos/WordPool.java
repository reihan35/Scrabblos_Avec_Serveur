package scrabblos;

import java.util.ArrayList;

public class WordPool {
	long current_period;
	long next_period;
	ArrayList<Word> words;
	
	public WordPool(long current_period, long next_period, ArrayList<Word> words) {
		this.current_period = current_period;
		this.next_period = next_period;
		this.words = words;
	}

	public long getCurrent_period() {
		return current_period;
	}

	public void setCurrent_period(long current_period) {
		this.current_period = current_period;
	}

	public long getNext_period() {
		return next_period;
	}

	public void setNext_period(long next_period) {
		this.next_period = next_period;
	}

	public ArrayList<Word> getWords() {
		return words;
	}

	public void setWords(ArrayList<Word> words) {
		this.words = words;
	}
	
	
	
}
