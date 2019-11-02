package scrabblos;

import java.util.ArrayList;


public class DiffLetterPool {
	private long since;
	private LetterPool letterpool;
	
	public DiffLetterPool(long since, LetterPool letterpool) {
		this.since = since;
		this.letterpool = letterpool;
	}

	public long getSince() {
		return since;
	}

	public void setSince(int since) {
		this.since = since;
	}

	public LetterPool getLetterpool() {
		return letterpool;
	}

	public void setLetterpool(LetterPool letterpool) {
		this.letterpool = letterpool;
	}
}
