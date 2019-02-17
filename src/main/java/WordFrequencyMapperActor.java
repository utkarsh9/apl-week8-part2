import java.util.HashMap;
import java.util.List;
import java.util.Map;

import akka.actor.AbstractActor;
import akka.actor.Props;

public class WordFrequencyMapperActor extends AbstractActor {

	public static Props props() {
		return Props.create(WordFrequencyMapperActor.class);
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder().match(NonStopWords.class, nonStopWords -> this.getWordMap(nonStopWords)).build();
	}

	private void getWordMap(NonStopWords nonStopWords) {
		Map<String, Integer> wordMap = new HashMap<String, Integer>();
		List<String> allNonStopWords = nonStopWords.getNonStopWords();
		for (String word : allNonStopWords) {
			int count = 1;
			if (wordMap.containsKey(word)) {
				count = wordMap.get(word);
				count++;
			}
			wordMap.put(word, count);
		}
		getSender().tell(new WordMap(wordMap), getSelf());
	}

}
