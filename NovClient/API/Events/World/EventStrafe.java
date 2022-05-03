
package NovClient.API.Events.World;

import NovClient.API.Event;

public final class EventStrafe
extends Event {
    private final float strafe;
    private final float forward;
    private final float friction;

    public final float getStrafe() {
        return this.strafe;
    }

    public final float getForward() {
        return this.forward;
    }

    public final float getFriction() {
        return this.friction;
    }

    public EventStrafe(float strafe, float forward, float friction) {
        this.strafe = strafe;
        this.forward = forward;
        this.friction = friction;
    }
}
