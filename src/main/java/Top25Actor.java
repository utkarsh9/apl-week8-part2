import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import akka.actor.AbstractActor;
import akka.actor.Props;

public class Top25Actor extends AbstractActor {

	public static Props props() {
		return Props.create(Top25Actor.class);
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder().match(WordMap.class, wordMap -> this.display(wordMap)).build();
	}

	private void display(WordMap wordMap) {
		Map<String, Integer> result = wordMap.getWordMap().entrySet().stream()
				.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).collect(Collectors.toMap(
						Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
		Set<Entry<String, Integer>> treeMapSet = result.entrySet();
		Iterator<Entry<String, Integer>> it = treeMapSet.iterator();
		int entryCount = 0;
		while (it.hasNext() && entryCount < 25) {
			entryCount++;
			Map.Entry<String, Integer> mapEntry = (Map.Entry<String, Integer>) it.next();
			System.out.println(mapEntry.getKey() + " - " + mapEntry.getValue());
		}
	}

}
