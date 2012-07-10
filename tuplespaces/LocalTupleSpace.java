package tuplespaces;

import java.util.ArrayList;

public class LocalTupleSpace implements TupleSpace {
	// tuplespace to hold the tuples
	ArrayList<String[]> tupleSpace = new ArrayList<String[]>();

	public String[] get(String[] pattern) {
		synchronized (tupleSpace) {
			while (true) {
				// matching of tuple i.e. seeked tuple's length and contents are
				// matched with tuples in tuplespace, null in content acts as
				// wildcard
				for (int i = 0; i < tupleSpace.size(); i++) {
					if (tupleSpace.get(i).length == pattern.length) {
						for (int j = 0; j < pattern.length; j++) {
							if (pattern[j] == null
									|| pattern[j].equals(tupleSpace.get(i)[j])) {
								if (j == (pattern.length - 1)) {
									// if everything matches then that tuple is
									// returned
									return tupleSpace.remove(i);
								}

							} else {
								break;
							}
						}
					}
				}
				tupleSpace.notifyAll();
				// if no tuple matches then it enters into wait mode
				try {
					tupleSpace.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void put(String[] tuple) {
		boolean notNull = true;
		if (tuple == null)
			notNull = false;
		for (int i = 0; i < tuple.length; i++) {
			if (tuple[i] == null)
				notNull = false;
		}
		if (notNull == true) {
			// a clone of passed tuple is inserted into tuple space so that it
			// won't
			// be affected after it has been put into tuplespace
			synchronized (tupleSpace) {
				String[] tupleClone = tuple.clone();
				tupleSpace.add(tupleClone);
				tupleSpace.notifyAll();
			}
		}
	}
}
