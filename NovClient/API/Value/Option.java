/*
 * Decompiled with CFR 0_132.
 */
package NovClient.API.Value;

import NovClient.API.Value.Value;

public class Option<V> extends Value<V> {
	public Option(String displayName, String name, V enabled) {
		super(displayName, name);
		this.setValue(enabled);
	}
	public Option(String displayName, V enabled) {
		super(displayName, displayName);
		this.setValue(enabled);
	}
}
