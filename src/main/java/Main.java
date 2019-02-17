import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class Main {

	public static void main(String[] args) {
		try {
			ActorSystem system = ActorSystem.create("Week8Part2");
			final ActorRef bookReaderActorRef = system.actorOf(BookReaderActor.props(), "br");
			final ActorRef stopWordsFilterActorRef = system.actorOf(StopWordsFilterActor.props(), "swf");
			final ActorRef wordFrequencyMapperActor = system.actorOf(WordFrequencyMapperActor.props(), "wfm");
			final ActorRef top25ActorRef = system.actorOf(Top25Actor.props(), "t25a");
			final ActorRef masterActorRef = system.actorOf(WordFrequencyAppMasterActor.props(bookReaderActorRef,
					stopWordsFilterActorRef, wordFrequencyMapperActor,top25ActorRef), "ma");
			masterActorRef.tell(new Start(args[0]), ActorRef.noSender());
			Thread.sleep(2000);
			System.out.println("Enter to exit");
			System.in.read();
			system.terminate();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
