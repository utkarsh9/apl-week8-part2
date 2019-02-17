import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

public class WordFrequencyAppMasterActor extends AbstractActor {

	private final ActorRef bookReaderActor;
	private final ActorRef stopWordsFilterActor;
	private final ActorRef wordFrequencyMapperActor;
	private final ActorRef top25Actor;

	public WordFrequencyAppMasterActor(ActorRef bookReaderActor, ActorRef stopWordsFilterActor,
			ActorRef wordFrequencyMapperActor, ActorRef top25Actor) {
		this.bookReaderActor = bookReaderActor;
		this.stopWordsFilterActor = stopWordsFilterActor;
		this.wordFrequencyMapperActor = wordFrequencyMapperActor;
		this.top25Actor = top25Actor;
	}

	public static Props props(ActorRef bookReaderActor, ActorRef stopWordsFilterActor,
			ActorRef wordFrequencyMapperActor, ActorRef top25Actor) {
		return Props.create(WordFrequencyAppMasterActor.class, () -> new WordFrequencyAppMasterActor(bookReaderActor,
				stopWordsFilterActor, wordFrequencyMapperActor, top25Actor));
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder().match(Start.class, start -> bookReaderActor.tell(start, getSelf()))
				.match(AllWords.class, allWords -> stopWordsFilterActor.tell(allWords, getSelf()))
				.match(NonStopWords.class, nonStopWords -> wordFrequencyMapperActor.tell(nonStopWords, getSelf()))
				.match(WordMap.class, wordMap -> top25Actor.tell(wordMap, getSelf())).build();
	}

}
