package chat;

import tuplespaces.TupleSpace;

public class ChatServer {
	// to hold the channels
	public String[] channels;
	// a tuplespace to hold tuples
	private TupleSpace tupleSpace;
	// to hold the channel names
	public String[] namesChannel;
	// the numbers of rows
	private String[] totalRows = null;
	// total number of channels
	private String[] noOfChannels;
	// message number for each message
	private String[] noMessages;
	private String[] msgFull = { "MessageFull", "NO" };
	// to hold the number of rows
	private int noRows;

	public ChatServer(TupleSpace t, int rows, String[] channelNames) {
		// putting the arguments to our variables
		this.tupleSpace = t;
		namesChannel = channelNames;
		noRows = rows;

		// A tuple for holding number of rows is created
		this.totalRows = new String[2];
		this.noOfChannels = new String[2];
		this.channels = new String[channelNames.length + 1];
		this.totalRows[0] = "Rows";
		this.totalRows[1] = Integer.toString(rows);
		// A tuple for holding number of channels is created
		this.noOfChannels[0] = "TotalChannels";
		this.noOfChannels[1] = Integer.toString(channelNames.length);
		this.channels[0] = "Channels";
		// A tuple for holding message number for each message is created
		this.noMessages = new String[3];
		for (int i = 0; i < channelNames.length; i++) {
			this.channels[i + 1] = channelNames[i];
			this.noMessages[0] = "MessageNo";
			this.noMessages[1] = channelNames[i];
			this.noMessages[2] = Integer.toString(0);
			tupleSpace.put(noMessages);
		}
		// putting all tuples created to tuplespace
		tupleSpace.put(msgFull);
		tupleSpace.put(totalRows);
		tupleSpace.put(noOfChannels);
		tupleSpace.put(channels);
	}

	public ChatServer(TupleSpace t) {
		this.tupleSpace = t;
		// following puts the names of channels to namesChannel variables
		String[] gettotalChannels = new String[2];
		gettotalChannels[0] = "TotalChannels";
		gettotalChannels[1] = null;
		int totalChannels = Integer
				.valueOf(tupleSpace.get(gettotalChannels)[1]);
		String[] newPattern = new String[totalChannels + 1];
		newPattern[0] = "Channels";
		for (int i = 1; i < totalChannels; i++) {
			newPattern[i] = null;
		}
		String[] x = t.get(newPattern);
		System.out.println(x.length);

		namesChannel = new String[x.length - 1];
		System.out.println(namesChannel.length);
		for (int i = 1; i < x.length; i++) {
			namesChannel[i - 1] = x[i];
		}
		tupleSpace.put(newPattern);
	}

	public String[] getChannels() {
		// returning channels' names
		return namesChannel;
	}

	public void writeMessage(String channel, String message) {
		// getting previous message number for the channel
		if (message != null) {
			String[] channelMessage = new String[6];
			String[] getMsgNo = { "MessageNo", channel, null };
			int MessageNo = Integer.valueOf(tupleSpace.get(getMsgNo)[2]);
			// this creates and puts a new tuple that puts the message to
			// specified
			// channel with a message number which is incremented in every write
			channelMessage[0] = "Channels";
			channelMessage[1] = channel;
			channelMessage[2] = "MessageNo";
			channelMessage[3] = Integer.toString(MessageNo + 1);
			channelMessage[4] = "Message";
			channelMessage[5] = message;
			tupleSpace.put(channelMessage);
			String[] newMsgNo = { "MessageNo", channel,
					Integer.toString(MessageNo + 1) };
			tupleSpace.put(newMsgNo);
		}
	}

	public ChatListener openConnection(String channel) {
		// this opens a new listener and puts the channel to "Channels" tuple
		ChatListener c = new ChatListener();
		c.tupleSpace = tupleSpace;
		c.nameOfChannel = channel;
		String[] put = { "Channels", channel };
		c.tupleSpace.put(put);

		// If a new listener comes and the message number has reached more than
		// the rows, then it assigns a new msg no. for new listener by
		// subtractig message no. from rows

		String[] getMsgNo = { "MessageNo", channel, null };
		String[] MessageNo = tupleSpace.get(getMsgNo);
		String[] putMessageNo = { "MessageNo", channel, MessageNo[2] };
		int msgNo = Integer.valueOf(MessageNo[2]);
		tupleSpace.put(putMessageNo);
		if (msgNo > noRows) {
			c.readMsg = (msgNo - noRows) + 1;
		}
		return c;

	}
}
