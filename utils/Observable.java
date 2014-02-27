package utils;

public interface Observable<S> {
	void register(Observer<S> ob);
	void unregister(Observer<S> ob);
	void notify(S s);
}
