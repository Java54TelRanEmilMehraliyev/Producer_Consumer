package telran.multithreading;

import java.util.concurrent.BlockingQueue;
import java.util.stream.IntStream;

public class ProducerSender extends Thread {
	//HW #44 definition
	//dispatching functionality
	//two message boxes
	//even messages should be put to even message box
	//odd messages should be put to odd message box
	private BlockingQueue<String> evenBox;
	private BlockingQueue<String> oddBox;
	private int nMessages;
	
	public ProducerSender(BlockingQueue<String> evenBox,BlockingQueue<String> oddBox , int nMessages) {
		this.evenBox = evenBox;
		this.oddBox = oddBox;
		this.nMessages = nMessages;
	}
	
	public void run() {
		IntStream.rangeClosed(1, nMessages)
		.mapToObj(i -> "message" + i).forEach(m -> {
			try {
				if (Integer.parseInt(m.replace("message", "")) % 2 == 0) {
					evenBox.put(m);
				} else {
					oddBox.put(m);
				}
			} catch (InterruptedException e) {
				// no interrupt logics
			}
		});
	}
}