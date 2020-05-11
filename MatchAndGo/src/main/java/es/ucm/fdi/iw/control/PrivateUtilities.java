package es.ucm.fdi.iw.control;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class PrivateUtilities {
	private static final String badWords = "drop database|drop datatable|select\\W*from|delete\\W*from|update\\W*set|http|https|porno|viagra";
	private static final Logger log = LogManager.getLogger(PrivateUtilities.class);
	public PrivateUtilities(){
	}
	public boolean checkStrings(List<String> words){
		log.info("i will check words");
		Pattern pat = Pattern.compile(badWords);
		for(String word : words){
			log.info("checking {}",word);
				if(pat.matcher(word).find()){
					log.warn("They want do something bad");
					return true;
				}
		}
		log.info("All clean");
		return false;
	}
}
