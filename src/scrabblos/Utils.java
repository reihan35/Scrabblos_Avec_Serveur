package scrabblos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


import net.i2p.crypto.eddsa.EdDSAPublicKey;

public class Utils {

	public static String bytesToHex(byte[] hashInBytes) {

        StringBuilder sb = new StringBuilder();
        for (byte b : hashInBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
	
	public static HashMap<String, Integer> authors_score(HashMap<Word,Integer> scores,ArrayList<Block> blocks) {
		Word w = word_with_best_score(scores);
		HashMap<String, Integer> scores2 = new HashMap<String, Integer>();
		//System.out.println("je suis le mot MOI MOI MOI" + w);
		
		for(Letter l : w.getWord()) {
			//System.out.println("je viens ici");
			if (scores2.get(l.getAuthor()) == null) {
				scores2.put(l.getAuthor(),0);
			}
			scores2.put(l.getAuthor(),scores2.get(l.getAuthor())+1);
		}

		while(find_prev(w,blocks)!=null) {
			w = find_prev(w,blocks);
			for(Letter l : w.getWord()) {
				//System.out.println("je viens ici");
				if (scores2.get(l.getAuthor()) == null) {
					scores2.put(l.getAuthor(),0);
				}
				scores2.put(l.getAuthor(),scores2.get(l.getAuthor())+1);
			}

		}
		return scores2;
	}
	
	public static byte[] longToBytes(long x) {
	    ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
	    buffer.putLong(x);
	    return buffer.array();
	}
	
	public static byte[] signature2(String lettre,byte[] hash,long p,KeyPair kp) throws NoSuchAlgorithmException, IOException, InvalidKeyException, SignatureException {
		//Faut rajouter une condition comme quoi si le wordpool est vide on passe ici
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		outputStream.write(lettre.getBytes());
		outputStream.write(Utils.longToBytes(p));
		outputStream.write(hash);
		
		EdDSAPublicKey public_k = (EdDSAPublicKey) kp.getPublic();
		outputStream.write(public_k.getAbyte());
		byte[] hashf = digest.digest(outputStream.toByteArray());
		
		return ED25519.sign(kp,hashf);
	}
	
	public static Word find_prev(Word w, ArrayList<Block> blocks) {
		for(Block b : blocks) {
			if (b.getCurrent() == w) {
				return b.getBefor();
			}
		}
		return null;
	}
	
	
	
	public static int score(Word w, ArrayList<Block> blocks) {
		int score = w.getWord().size();
		while(find_prev(w,blocks)!=null) {
			w = find_prev(w,blocks);
			score = score + w.getWord().size();
		}
		return score;
	}
	public static int scoreP(Politicien p) {
		return score(p.getCurrentBlokc().getCurrent(),p.get_blocks());
	}
	
	public static HashMap<Word, Integer> score_each_word(ArrayList<Word> wp,ArrayList<Block> blocks) {
		HashMap<Word, Integer> hm = new HashMap<Word, Integer>();
		for (Word w : wp){
			hm.put(w, score(w,blocks));
		}
		System.out.println("score de chaque mot " + hm);
		return hm;
	}
	
	public static Word word_with_best_score(HashMap<Word, Integer> scores) {
		return Collections.max(scores.entrySet(), (a, b) -> a.getValue() - b.getValue()).getKey();
	}
	
	public static byte[] hashLetter(ArrayList<Letter> w) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		for(int i = 0; i<w.size(); i++) {
			outputStream.write(w.get(i).getLetter().getBytes());
			outputStream.write(Utils.longToBytes(w.get(i).getPeriod()));
			outputStream.write(hexStringToByteArray(w.get(i).getHead()));
			outputStream.write(hexStringToByteArray(w.get(i).getAuthor()));
			outputStream.write(hexStringToByteArray(w.get(i).getSignature()));
		}
		return outputStream.toByteArray();
	}
	
	public static byte[] signature2Poli(Word w,byte[] hash,KeyPair kp) throws NoSuchAlgorithmException, IOException, InvalidKeyException, SignatureException {
		//Faut rajouter une condition comme quoi si le wordpool est vide on passe ici
		
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		for(int i = 0; i<w.getWord().size(); i++) {
			outputStream.write(w.getWord().get(i).getLetter().getBytes());
			outputStream.write(Utils.longToBytes(w.getWord().get(i).getPeriod()));
			outputStream.write(hexStringToByteArray(w.getWord().get(i).getHead()));
			outputStream.write(hexStringToByteArray(w.getWord().get(i).getAuthor()));
			outputStream.write(hexStringToByteArray(w.getWord().get(i).getSignature()));
			
		}
		outputStream.write(hash);
		
		EdDSAPublicKey public_k = (EdDSAPublicKey) kp.getPublic();
		outputStream.write(public_k.getAbyte());
		byte[] hashf = digest.digest(outputStream.toByteArray());
		
		return ED25519.sign(kp,hashf);
	}
	
	public static byte[] hexStringToByteArray(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}
	
	public static byte[] intToBigEndian(int numero) {
		ByteBuffer bb = ByteBuffer.allocate(8);
		bb.rewind();
		bb.order(ByteOrder.LITTLE_ENDIAN);
		bb.putInt((int) numero);
		//System.out.println(bb);
		return bb.array();
		
	}
	
	public static ArrayList<String> makeDictionnary(String fileName) {
		
		String file = "src/dict_dict_100000_1_10.txt";
		try {
			String line = null;
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			ArrayList<String> strings = new ArrayList<String>();

			while ((line = bufferedReader.readLine()) != null) {
				strings.add(line);
			}
			return strings;

		} catch (Exception e) {
			return null;
		}
	}
	
	public static String readAnswer(DataInputStream is) {
		byte size_of_msg_bytes[] = new byte[8];
		String msgreadtostr = "";
		try {
			is.read(size_of_msg_bytes);
			ByteBuffer wrapped = ByteBuffer.wrap(size_of_msg_bytes);
			long size_of_msg = wrapped.getLong();
			byte msgread[] = new byte[(int) size_of_msg];
			is.read(msgread);
			msgreadtostr = new String(msgread);
		}catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return msgreadtostr;
	}
}
