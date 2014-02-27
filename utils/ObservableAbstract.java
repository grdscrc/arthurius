package utils;

import java.util.ArrayList;

public class ObservableAbstract<S> implements Observable<S> {
	private ArrayList<Observer<S>> observers = new ArrayList<Observer<S>>();

	public void register(Observer<S> ob) {
		observers.add(ob);
	}

	public void unregister(Observer<S> ob) {
		observers.remove(ob);
	}

	public void notify(S s) {
		for (Observer<S> ob : observers)
			ob.update(s);
	}
}
