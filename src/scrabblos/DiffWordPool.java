package scrabblos;

public class DiffWordPool {
	long since;
	WordPool wordpool;
	
	public DiffWordPool(long since, WordPool wordpool) {
		super();
		this.since = since;
		this.wordpool = wordpool;
	}

	public long getSince() {
		return since;
	}

	public void setSince(long since) {
		this.since = since;
	}

	public WordPool getWordpool() {
		return wordpool;
	}

	public void setWordpool(WordPool wordpool) {
		this.wordpool = wordpool;
	}
	
	
}
