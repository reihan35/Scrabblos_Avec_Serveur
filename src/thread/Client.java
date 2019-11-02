package thread;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.json.JSONException;
import org.json.JSONObject;

import net.i2p.crypto.eddsa.EdDSAPublicKey;
import scrabblos.ED25519;
import scrabblos.Letter;
import scrabblos.Utils;

public class Client implements Runnable {
	private static String pk;
	private static KeyPair kp;
	private ArrayList<String> LetterBag = new ArrayList<String>();
	private String hash = ""+Math.random()*100;
	private char[] bags = { 'b', 'd', 'u', 'q', 's', 'y', 'o', 'r', 'r', 'p', 'm', 'e', 'p', 'y', 's', 'l', 't', 'h',
			'u', 'i', 'n', 'p', 'w', 't', 'w', 'a', 'e', 's', 'r', 'c', 'y', 'c', 'u', 'j', 'x', 't', 'i', 'o', 'k',
			'k', 'c', 'c', 'l', 'w', 'y', 'c', 'w', 'o', 'y', 'x', 'g', 'c', 'u', 'y', 'g', 's', 's', 'c', 'q', 'q',
			'a', 'x', 'd', 'm', 'j', 'e', 'l', 'f', 'f', 'g', 'k', 'x', 'p', 'm', 'j', 'x', 'a', 'y', 'g', 'p', 'd',
			'g', 'g', 'i', 'j', 'o', 'g', 'w', 'r', 'a', 'd', 'b', 'p', 'm', 'o', 'e', 'p', 'v', 't', 'h', 'a', 'm',
			'v', 'm', 'f', 'f', 'e', 's', 'v', 'r', 'o', 'v', 'h', 'o', 'v', 'q', 'a', 'm', 'c', 'b', 'e', 'q', 'g',
			'd', 'p', 'd', 'x', 'q', 'k', 'f', 'p', 'k', 'f', 'a', 's', 'k', 'c', 'x', 'q', 'h', 'i', 'r', 'w', 's',
			's', 'r', 'r', 'q', 'u', 'g', 's', 'n', 'x', 'f', 't', 'q', 'a', 'v', 'r', 'p', 't', 'n', 'h', 'p', 's',
			'j', 'w', 'p', 'y', 'x', 'b', 't', 'v', 'v', 'g', 'a', 'y', 'q', 'm', 'k', 'm', 'x', 'c', 't', 'u', 'n',
			'g', 'e', 'o', 'w', 'o', 'l', 'l', 'f', 'o', 'd', 'l', 'b', 'p', 'x' };

	@Override
	public void run() {
		try {
			// Displaying the thread that is running
			System.out.println("Thread Client" + Thread.currentThread().getId() + " is running");
			creatKey();
			// LetterBag = new ArrayList<String>();

			for(char c:bags) {
				LetterBag.add(c+"");
			}

			updateLetterPool();
			/*
			 * while(true) { TimeUnit.SECONDS.sleep(10); updateLetterPool(); }
			 */
		} catch (Exception e) {
			// Throwing an exception
			System.out.println("Exception is caught");
		}

	}

	private void creatKey() {
		// CREATION DE LA CLE PUBLIQUE
		ED25519 ed;
		try {
			ed = new ED25519();
			kp = ed.generateKeys();
			EdDSAPublicKey public_k = (EdDSAPublicKey) kp.getPublic();
			pk = Utils.bytesToHex(public_k.getAbyte());
		} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Letter choose_Letter(ArrayList<String> LetterBag, String hash) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-256");
			Collections.shuffle(LetterBag);
			String letter = LetterBag.get(0);
			String signature = Utils.bytesToHex(Utils.signature2(letter, digest.digest((hash).getBytes()), 0, kp));
			String head = Utils.bytesToHex(digest.digest((hash).getBytes()));
			long period = 0;
			return new Letter(letter, period, head, pk, signature);
		} catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	protected void updateLetterPool() {
		
			System.out.println("update letter pool");
			Letter l = choose_Letter(LetterBag, hash);

			//String j = "{ \"inject_letter\": { \"letter\":\"a\", \"period\":0, \"head\":\"e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855\", \"author\":\"b7b597e0d64accdb6d8271328c75ad301c29829619f4865d31cc0c550046a08f\", \"signature\":\"8b6547447108e11c0092c95e460d70f367bc137d5f89c626642e1e5f2ce\" }}";

			synchronized (MotorA.getMotorA()) {
				MotorA motor = MotorA.getMotorA();
				motor.addLetter(l);
				motor.showLetterPool();
			}

	}

}
