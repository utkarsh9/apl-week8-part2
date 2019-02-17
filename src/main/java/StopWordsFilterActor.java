import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import akka.actor.AbstractActor;
import akka.actor.Props;

public class StopWordsFilterActor extends AbstractActor {

	public static Props props() {
		return Props.create(StopWordsFilterActor.class);
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder().match(AllWords.class, allWords -> this.getAllNonStopWords(allWords)).build();
	}

	private void getAllNonStopWords(AllWords allWords) {
		List<String> stopWords = new ArrayList<String>();
		List<String> nonStopWords = new ArrayList<String>();
		List<String> words = allWords.getAllWords();
		try {
			stopWords = Files.lines(Paths.get("stop-words.txt")).map(line -> line.split(","))
					.flatMap(Arrays::stream).collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (String word : words) {
			if (!stopWords.contains(word) && !"".equals(word) && !"s".equals(word)) {
				nonStopWords.add(word);
			}
		}
		getSender().tell(new NonStopWords(nonStopWords), getSelf());
	}

}
