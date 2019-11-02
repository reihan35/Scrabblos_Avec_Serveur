package scrabblos;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import net.i2p.crypto.eddsa.EdDSAPublicKey;

public class Politicien {

	private static Socket socket;
	private static String pk;
	private static KeyPair kp;
	
	/*public Politicien(Socket socket) {
		this.socket = socket;

	}
	
	public Politicien(Socket socket, String pk,KeyPair kp) {
		this.socket = socket;
		this.pk = pk;
		this.kp = kp;
		
	}*/

	public static String getPk() {
		return pk;
	}

	public static String getSign() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, IOException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		return Utils.bytesToHex(Utils.signature2("a",digest.digest(("").getBytes()), 0, kp));
	}

	
	public static void register(Socket socket) throws IOException, JSONException, NoSuchAlgorithmException, NoSuchProviderException {

		OutputStream os = socket.getOutputStream();
		OutputStreamWriter osw = new OutputStreamWriter(os);
		BufferedWriter bw = new BufferedWriter(osw);
		JSONObject json = new JSONObject();
		
		//CREATION DE LA CLE PUBLIQUE
		ED25519 ed = new ED25519();
		kp = ed.generateKeys();
		EdDSAPublicKey public_k = (EdDSAPublicKey) kp.getPublic();
		pk = Utils.bytesToHex(public_k.getAbyte());
		
		//ENVOIE DU MESSAGE
		json.put("register",pk);
		byte[] a = Utils.intToBigEndian(json.toString().length());
		for (int i = a.length - 1; i >= 0; i--) {
			bw.write((char) (a[i]));
		}
		bw.write(json.toString());
		bw.flush();
		InputStream is = socket.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		DataInputStream di = new DataInputStream(is);
		String res = Utils.readAnswer(di);
		// TRANSFORMATION DE LA REPONSE SOUS FORME DE LISTE DE LETTER
		System.out.println("je suis res" + res);
		
	}

	public static Word make_word(DiffLetterPool dffl) throws IOException, JSONException, InvalidKeyException, NoSuchAlgorithmException, SignatureException {
		//dans cette class nous devons ecrire l'algo de creation d'un mot
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		Word wordAct = new Word();
		LetterPool lettepool =  dffl.getLetterpool();
		ArrayList<Letter> letters = lettepool.getLetters();
		//Word word = wordAct.getWord();
		//ArrayList<Letter> wordletters = (ArrayList<Letter>) wordAct.getWord().clone();
		ArrayList<Letter> wordletters = new ArrayList<Letter>();
		//int i = wordletters.size();
		for(Letter l:letters) {
			wordletters.add(l);		
		}

		wordAct.setWord(wordletters);
	    wordAct.setHead(Utils.bytesToHex(digest.digest(("").getBytes())));
	    wordAct.setPoliticien(pk);
		String signture =  Utils.bytesToHex(Utils.signature2Poli(wordAct,digest.digest(("").getBytes()), kp));
	    wordAct.setSignature(signture);

       return wordAct;
	}

	public static void inject_word(Socket s, Word w,BufferedWriter bw) throws IOException, JSONException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
		
		JSONObject json = new JSONObject();
		json.put("word", w.getWord());
		json.put("politician", w.getPoliticien());
		json.put("signature",w.getSignature() );
		json.put("head", w.getHash());
		
		JSONObject json2 = new JSONObject();
		json2.put("inject_word",json );
		System.out.println("je VEUX VOIR CA" + json.toString());
		System.out.println("je VEUX VOIR CArrrrrrrrrr" + json2.toString());

		//String s1 = "{ \"inject_word\" : " + w.toString() + " }";
		//System.out.println("je suis S1 " + s1);

		//byte[] a = Utils.intToBigEndian(json2.toString().length());
		byte[] a = Utils.intToBigEndian(json2.toString().length());
		for (int i = a.length - 1; i >= 0; i--) {
			System.out.println("what" + (char) (a[i]));
			bw.write((char) (a[i]));
		}
		//System.out.println("inject word:" + w.getWord());
		bw.write(json2.toString());
		bw.flush();
	}
	
	public static boolean isWord(ArrayList<String> dictionaire,Word wordAct) {
		ArrayList<Letter> word = wordAct.getWord();
		for(String s : dictionaire) {
			if(s.length() == word.size()) {
				boolean is = true;
				for(int i = 0;i<s.length() ;i++) {
					if(s.charAt(i) != word.get(i).getLetter().charAt(0)) {
						is = false;
						break;
					}
				}
				if(is) return true;
			}
		}
		
		return false;
	}
	
	public static String findWordDestinaire(ArrayList<String> dictionaire,Word wordAct) {
		System.out.println(" po find   ----> wordAct = " + wordAct.getWord());
		ArrayList<Letter> letters = wordAct.getWord();
		for(String s: dictionaire) {
			int i = 0;
			System.out.print(s);
			if(s.length()<=letters.size()) {
				System.out.println(" --> too short!!!");
				continue;
			}else {
				//there,we can chose the longest or the first accord or random--i chose first accord
				for(Letter letter : letters) {
					if(letter.getLetter().charAt(0) == s.charAt(i)) {
						i++;
						continue;
					}else {
						System.out.println(" --> not match!!!");
						break;
					}
				}
				if(i == letters.size()) return s;
			}
		}
		return null;	
	}

/*
	public static void main(String args[]) {
		try {
			//InetAddress address = InetAddress.getByName(HOST);
			MessageDigest digest = MessageDigest.getInstance("SHA-256");

			// System.err.print(address);
			//socket = new Socket(address, PORT);
			OutputStream os = socket.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os);
			BufferedWriter bw = new BufferedWriter(osw);
			// Send the message to the server
			// generatePublicKey();
			register(socket);
			System.out.println("OUI JE RENTRE");
			DiffWordPool wp = CommonOperations.get_wordpool_since(socket,bw,0);
			System.out.println(wp.getWordpool().getWords());
			//Word w = new Word(word, hash, politicien, signature);
			//inject_word()
			/*CommonOperations.get_letterpool_since(socket, bw, 0);
			// System.out.println(LetterBag);
			// inject_Letter(socket,LetterBag,bw);
			// inject_Letter(socket,LetterBag);
			// continous_listen(socket,bw);
			// get_full_letterpool(socket,bw);
			
			 ArrayList<Letter> first =  CommonOperations.get_full_letterpool(socket,bw);
			    int length =  first.size();
			    Letter letter  = first.get((int) (Math.random()*length));
			    char lettre = letter.getLetter().charAt(0);
			 ArrayList<String> dictionairy = Utils.makeDictionnary("src/dict_dict_100000_1_10.txt");
			    //pour meme lettre de commence, il y a des mots different a choisir
			    ArrayList<String> choix = new  ArrayList<String>();
			    for(String s : dictionairy) {
			    	if(s.charAt(0) == lettre) {
			    		choix.add(s);
			    	}
			    }
			    int clength =  choix.size();
			    String motDes  = choix.get((int) (Math.random()*clength));//choisir le mot destinaire
			
			
			int i=0;
			ArrayList<Letter> letters = new ArrayList<Letter>();
			String hash ="";
			String signture =  Utils.bytesToHex(Utils.signature2("a",digest.digest(("").getBytes()), 0, kp));
			Word word = new Word(letters,hash,pk,signture);
			while(i<10) {//10 tour
				ArrayList<Letter> letterpool = CommonOperations.get_letterpool_since(socket,bw,periode*i); 
				LetterPool newletterpool = new LetterPool(periode*i,periode*(i+1),letters);
				DiffLetterPool diff = new DiffLetterPool(periode*i,newletterpool);
				Word makeword = make_word(diff,motDes,word);
				ArrayList<Word> Words = CommonOperations.get_wordpool_since(socket,bw,periode*i);
				for(int k = 0 ;k<Words.size();k++) {
					if(Words.get(k).getWord().size()>= makeword.getWord().size()) {
						word = Words.get(k);
					}
				}
				motDes =  findWordDestinaire(dictionairy,word);
				i++;
			}*/
			
/*
		} catch (IOException exception) {
			exception.printStackTrace();
			System.out.println("exception : " + exception.getMessage());
		} catch (Exception exception) {
			exception.printStackTrace();
			System.out.println("exception : " + exception.getMessage());
		} finally {
			// Closing the socket
			try {
				socket.close();
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("e : " + e.getMessage());
			}
		}
	}
*/
}
