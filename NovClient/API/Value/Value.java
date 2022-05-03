package NovClient.API.Value;

public abstract class Value<V> {
	private String displayName;
	private String name;
	public V value;

	public Value(String displayName, String name) {
		this.displayName = displayName;
		this.name = name;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public String getName() {
		return this.name;
	}

	public V getValue() {
		return this.value;
	}

	public void setValue(V value) {
		this.value = value;
	}
}
