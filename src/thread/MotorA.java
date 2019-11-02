package thread;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;

import scrabblos.Letter;
import scrabblos.Word;

class MotorA {

	private static MotorA motor = new MotorA();
	private static boolean flag = true;
	private static ArrayList<Letter> letter_pool = new ArrayList<Letter>();
	private static ArrayList<Word> word_pool = new ArrayList<Word>();
	private static final java.util.concurrent.locks.Lock lock = new java.util.concurrent.locks.ReentrantLock();

	public static boolean isFlag() {
		lock.lock();
		boolean f = flag;
		lock.unlock();
		return f;
	}

	public static void setFlag(boolean flag) {
		lock.lock();
		MotorA.flag = flag;
		lock.unlock();

	}

	public static ArrayList<Letter> getLetter_pool() {
		lock.lock();
		ArrayList<Letter> lp = letter_pool;
		lock.unlock();
		return lp;
	}

	public static void setLetter_pool(ArrayList<Letter> letter_pool) {
		lock.lock();
		MotorA.letter_pool = letter_pool;
		lock.unlock();
	}

	public static ArrayList<Word> getWord_pool() {
		lock.lock();
		ArrayList<Word> wp = word_pool;
		lock.unlock();
		return wp;
	}

	public static void setWord_pool(ArrayList<Word> word_pool) {
		lock.lock();
		MotorA.word_pool = word_pool;
		lock.unlock();
	}

	private MotorA() {
	}

	public static MotorA getMotorA() {
		return motor;
	}

	public static Lock getLock() {
		return lock;
	}

	public static void addLetter(Letter s) {
		lock.lock();
		letter_pool.add(s);
		lock.unlock();
	}

	public void showLetterPool() {
		lock.lock();
		try {
			System.out.println("show letter pool-------------------------------------------------");
			if (letter_pool.size() > 0) {
				for (Letter s : letter_pool) {
					System.out.println(s.toString());
				}
			}
			System.out.println("finish show letter pool------------------------------------------");
		} finally {
			lock.unlock();
		}

	}
	/* here go business methods for MotorA */

	public void addWord(Word w) {
		lock.lock();
		word_pool.add(w);
		lock.unlock();

	}

	public void showWordPool() {
		lock.lock();
		try {
			System.out.println("show WORD pool-------------------------------------------------");
			if (word_pool.size() > 0) {
				for (Word w : word_pool) {
					System.out.println(w.toString());
				}
			}
			System.out.println("finish show WORD pool------------------------------------------");
		} finally {
			lock.unlock();
		}

	}

}
