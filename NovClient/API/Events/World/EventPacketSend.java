/*
 * Decompiled with CFR 0_132.
 */
package NovClient.API.Events.World;

import NovClient.API.Event;
import net.minecraft.network.Packet;

public class EventPacketSend extends Event {
	public Packet packet;

	public EventPacketSend(Packet packet) {
		this.packet = packet;
	}

	public Packet getPacket() {
		return this.packet;
	}

	public void setPacket(Packet packet) {
		this.packet = packet;
	}
}
