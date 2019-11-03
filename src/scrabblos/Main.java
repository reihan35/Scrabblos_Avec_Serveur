package scrabblos;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;

import net.i2p.crypto.eddsa.EdDSAPublicKey;

public class Main {

	private static Socket socket;
	private static final String HOST = "localhost";
	private static final int PORT = 12345;
	private static int periode = 10;// loneur de periode;

	public static void main(String args[]) throws IOException, InvalidKeyException, NoSuchAlgorithmException,
			SignatureException, JSONException, NoSuchProviderException {

		InetAddress address = InetAddress.getByName(HOST);
		socket = new Socket(address, PORT);
		OutputStream os = socket.getOutputStream();
		OutputStreamWriter osw = new OutputStreamWriter(os);
		BufferedWriter bw = new BufferedWriter(osw);
		 
		Client c = new Client();
		ArrayList<String> LetterBag = c.register(socket);
		c.inject_Letter(socket, LetterBag, bw);
		System.out.println("inject_Letter : ");
		c.inject_Letter(socket,LetterBag,bw);
		System.out.println("inject_Letter : " );
		c.inject_Letter(socket,LetterBag,bw);
		System.out.println("inject_Letter : " );
        
		LetterPool lp = CommonOperations.get_full_letterpool(socket, bw);
		System.out.println("LetterPool : " +lp.getLetters());
		ArrayList<String> dictionaire = Utils.makeDictionnary("src/dict_dict_100000_1_10.txt");
		
		
		Politicien po1 = new Politicien();
		po1.register(socket);
		DiffLetterPool lp2 = CommonOperations.get_letterpool_since(socket, bw,0);
			
		Word w = po1.make_word(lp2);
		
		po1.inject_word(socket,w,bw);
		WordPool wp = CommonOperations.get_full_wordpool(socket, bw);
		System.out.println("Voila ce que l'on a recu" + wp.getWords());
		c.recive_word(socket,bw);
		po1.recive_word(socket,bw);
		System.out.println("je sauvgarde ce mot " + c.getCurrentBlokc().getCurrent());
		//System.out.println("Et son mot d'avant est " + c.getCurrentBlokc().getBefor());
		c.inject_Letter(socket, LetterBag, bw);
		CommonOperations.get_full_letterpool(socket, bw);
		po1.inject_word(socket, w, bw);
		WordPool wp2 = CommonOperations.get_full_wordpool(socket, bw);
		System.out.println("Voila ce que l'on a recu 2 " + wp2.getWords());
		System.out.println("ma liste de block est " + c.get_blocks());
		System.out.println(Utils.authors_score(Utils.score_each_word(wp2.getWords(), c.get_blocks()),c.get_blocks()));

	}

}
