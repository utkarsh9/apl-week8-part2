import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import akka.actor.AbstractActor;
import akka.actor.Props;

public class BookReaderActor extends AbstractActor {

	public static Props props() {
		return Props.create(BookReaderActor.class);
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder().match(Start.class, start -> this.getAllWordsFromBook(start)).build();
	}

	private void getAllWordsFromBook(Start start) {
		List<String> allWords = new ArrayList<String>();
		try {
			allWords = Files.lines(Paths.get(start.getFilePath()))
					.flatMap(line -> Arrays.stream(line.split("[\\s,;:?._!--]+"))).map(s -> s.toLowerCase())
					.collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}
		getSender().tell(new AllWords(allWords), getSelf());
	}

}
