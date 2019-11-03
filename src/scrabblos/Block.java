package scrabblos;

public class Block {
	Word current;
	Word befor;
	
	public Block(Word current, Word befor) {
		super();
		this.current = current;
		this.befor = befor;
	}

	public Word getCurrent() {
		return current;
	}

	public void setCurrent(Word current) {
		this.current = current;
	}

	public Word getBefor() {
		return befor;
	}

	public void setBefor(Word befor) {
		this.befor = befor;
	}
	
	public String toString() {
		return "Curren Word" + current + "Befor Word" + befor;
	}
	
	
}
