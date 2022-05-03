package NovClient.Util;

/**
 * Created by MiLiBlue on 2022/4/23 16:14
 */
public class Vec31 {
    private final double x;
    private final double y;
    private final double z;

    public Vec31(final double x, final double y2, final double z) {
        this.x = x;
        this.y = y2;
        this.z = z;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public Vec31 addVector(final double x, final double y2, final double z) {
        return new Vec31(this.x + x, this.y + y2, this.z + z);
    }

    public Vec31 floor() {
        return new Vec31(Math.floor(this.x), Math.floor(this.y), Math.floor(this.z));
    }

    public double squareDistanceTo(final Vec31 v) {
        return Math.pow(v.x - this.x, 2.0) + Math.pow(v.y - this.y, 2.0) + Math.pow(v.z - this.z, 2.0);
    }

    public Vec31 add(final Vec31 v) {
        return this.addVector(v.getX(), v.getY(), v.getZ());
    }

    public net.minecraft.util.Vec3 mc() {
        return new net.minecraft.util.Vec3(this.x, this.y, this.z);
    }

    @Override
    public String toString() {
        return "[" + this.x + ";" + this.y + ";" + this.z + "]";
    }
}
