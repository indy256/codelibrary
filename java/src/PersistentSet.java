import java.util.NavigableSet;

public interface PersistentSet<K> extends NavigableSet<K> {
	public void markState(Object marker);

	public PersistentSet<K> getState(Object marker);
}
