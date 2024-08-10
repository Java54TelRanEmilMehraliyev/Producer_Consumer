package telran.multithreading;

import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.IntStream;

public class SenderReceiverAppl {
	// TODO for HW #44 (ConsumerReceiver should not be updated)
	// Provide functionality of dispatching
	// Even messages must be processed by receiver threads with even id
	// Odd messages must be processed by receiver threads with odd id
	// Hints two message boxes: one for even messages and other for odd messages

	private static final int N_MESSAGES = 2000000;
	private static final int N_RECEIVERS_EVEN = 5;
	private static final int N_RECEIVERS_ODD = 5;


	public static void main(String[] args) throws InterruptedException {
		BlockingQueue<String> evenBox = new LinkedBlockingQueue<String>();
		BlockingQueue<String> oddBox = new LinkedBlockingQueue<String>();
		
		ProducerSender sender = startSender(evenBox,oddBox, N_MESSAGES);
		ConsumerReceiver[] evenReceivers = startReceivers(evenBox, N_RECEIVERS_EVEN);
		ConsumerReceiver[] oddReceivers = startReceivers(oddBox, N_RECEIVERS_ODD);
		sender.join();
		stopReceivers(evenReceivers);
        stopReceivers(oddReceivers);
		displayResult();
	}

	private static void displayResult() {
		System.out.printf("counter of processed messsages is %d\n", ConsumerReceiver.getMessagesCounter());

	}

	private static void stopReceivers(ConsumerReceiver[] receivers) throws InterruptedException {
		for (ConsumerReceiver receiver : receivers) {
			receiver.interrupt();
			receiver.join();
		}

	}

	private static ConsumerReceiver[] startReceivers(BlockingQueue<String> messageBox, int nReceivers) {
	    ConsumerReceiver[] receivers = IntStream.range(0, nReceivers)
	        .mapToObj(i -> {
	            ConsumerReceiver receiver = new ConsumerReceiver();
	            receiver.setMessageBox(messageBox);
	            receiver.start();
	            return receiver;
	        })
	        .toArray(ConsumerReceiver[]::new);
	    return receivers;
	}

	private static ProducerSender startSender(BlockingQueue<String> evenBox,BlockingQueue<String> oddBox, int nMessages) {
		ProducerSender sender = new ProducerSender(evenBox,oddBox, nMessages);
		sender.start();
		return sender;
	}

}