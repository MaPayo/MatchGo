package es.ucm.fdi.iw.control;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class PrivateUtilities {
	private static final List<String> badWords = new ArrayList(List.of("select","update","delete"));
	private static final Logger log = LogManager.getLogger(PrivateUtilities.class);
	public PrivateUtilities(){
	}
	public boolean checkStrings(List<String> words){
		log.info("i will check words");
		for(String word : words){
			log.info("checking {}",word);
			if(badWords.contains(word)){
				log.warn("They want do something bad");
				return true;
			}
		}
		log.info("All clean");
		return false;
	}
}
