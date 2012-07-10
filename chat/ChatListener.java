package chat;

import tuplespaces.TupleSpace;

public class ChatListener {
	// variables to hold tuplespace,channel name; readMsg tracks the number of
	// msgs read and which msg is to be read next
	TupleSpace tupleSpace;
	String nameOfChannel = "";
	public int readMsg = 1;

	public String getNextMessage() {
		// // this gets the
		// String[] getMsgNo = { "MessageNo", nameOfChannel, null };
		// String[] MessageNo = tupleSpace.get(getMsgNo);
		// String[] putMessageNo = { "MessageNo", nameOfChannel, MessageNo[2] };
		// tupleSpace.put(putMessageNo);

		// fetches the messages from the channel with message no. starting with
		// readMsg
		String[] pattern = { "Channels", nameOfChannel, "MessageNo",
				Integer.toString(readMsg++), "Message", null };
		String[] getPattern = tupleSpace.get(pattern);
		// fetched messages are put back to tuplespace
		tupleSpace.put(getPattern);
		return getPattern[5];

	}

	public void closeConnection() {

	}
}
