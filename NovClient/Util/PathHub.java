package NovClient.Util;

import java.util.ArrayList;

/**
 * Created by MiLiBlue on 2022/4/23 16:16
 */
public class PathHub {
    private Vec31 loc;
    private PathHub parentPathHub;
    private ArrayList<Vec31> pathway;
    private double sqDist;
    private double currentCost;
    private double maxCost;

    public PathHub(final Vec31 loc, final PathHub parentPathHub, final ArrayList<Vec31> pathway,
                   final double sqDist, final double currentCost, final double maxCost) {
        this.loc = loc;
        this.parentPathHub = parentPathHub;
        this.pathway = pathway;
        this.sqDist = sqDist;
        this.currentCost = currentCost;
        this.maxCost = maxCost;
    }

    public Vec31 getLoc() {
        return this.loc;
    }

    public PathHub getLastHub() {
        return this.parentPathHub;
    }

    public ArrayList<Vec31> getPathway() {
        return this.pathway;
    }

    public double getSqDist() {
        return this.sqDist;
    }

    public double getCurrentCost() {
        return this.currentCost;
    }

    public void setLoc(final Vec31 loc) {
        this.loc = loc;
    }

    public void setParentPathHub(final PathHub parentPathHub) {
        this.parentPathHub = parentPathHub;
    }

    public void setPathway(final ArrayList<Vec31> pathway) {
        this.pathway = pathway;
    }

    public void setSqDist(final double sqDist) {
        this.sqDist = sqDist;
    }

    public void setCurrentCost(final double currentCost) {
        this.currentCost = currentCost;
    }

    public double getMaxCost() {
        return this.maxCost;
    }

    public void setMaxCost(final double maxCost) {
        this.maxCost = maxCost;
    }
}
