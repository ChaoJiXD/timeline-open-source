package NovClient.Module.Modules.Render;


import java.awt.*;

import org.lwjgl.opengl.GL11;

import NovClient.API.EventHandler;
import NovClient.API.Events.Render.EventPostRenderPlayer;
import NovClient.API.Events.Render.EventPreRenderPlayer;
import NovClient.API.Value.Numbers;
import NovClient.API.Value.Option;
import NovClient.Module.Module;
import NovClient.Module.ModuleType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;

public class Chams extends Module {
	
	
    public Numbers<Double> visiblered = new Numbers<Double>("VisibleRed","VisibleRed", 1.0, 0.001, 1.0, 0.001);
    public Numbers<Double> visiblegreen = new Numbers<Double>("VisibleGreen","VisibleGreen", 0.0, 0.001, 1.0, 0.001);
    public Numbers<Double> visibleblue = new Numbers<Double>("VisibleBlue","VisibleBlue", 0.0, 0.001, 1.0, 0.001);
    public Numbers<Double> hiddenred = new Numbers<Double>("HiddenRed","HiddenRed", 1.0, 0.001, 1.0, 0.001);
    public Numbers<Double> hiddengreen = new Numbers<Double>("HiddenGreen","HiddenGreen", 0.0, 0.001, 1.0, 0.001);
    public Numbers<Double> hiddenblue = new Numbers<Double>("HiddenBlue","HiddenBlue", 1.0, 0.001, 1.0, 0.001);
    public Numbers<Double> alpha = new Numbers<Double>("Alpha","Alpha", 1.0, 0.001, 1.0, 0.001);
    private Option<Boolean> players = new Option<Boolean>("Players","Players", true);
    private Option<Boolean> animals = new Option<Boolean>("Animals","Animals", true);
    private Option<Boolean> mobs = new Option<Boolean>("Mobs","Mobs", false);
    public Option<Boolean> invisibles = new Option<Boolean>("Invisibles","Invisibles", false);
    private Option<Boolean> passives = new Option<Boolean>("Passives","Passives", true);
    public Option<Boolean> colored = new Option<Boolean>("Colored","Colored", false);
    //public Option<Boolean> hands = new Option<Boolean>("Hands","Hands", false);
    public Option<Boolean> rainbow = new Option<Boolean>("Raindow","Raindow", false);

    public Chams() {
    	super("Chams", new String[]{"Chams"}, ModuleType.Render);
        addValues(visiblered, visiblegreen, visibleblue, hiddenred, hiddengreen, hiddenblue, alpha, players, animals, mobs, invisibles, passives,colored, rainbow);
    }
	@EventHandler
	private void preRenderPlayer(EventPreRenderPlayer e) {
		if(colored.getValue())return;
		GL11.glEnable((int) 32823);
		GL11.glPolygonOffset((float) 1.0f, (float) -1100000.0f);
	}

	@EventHandler
	private void postRenderPlayer(EventPostRenderPlayer e) {
		if(colored.getValue())return;
		GL11.glDisable((int) 32823);
		GL11.glPolygonOffset((float) 1.0f, (float) 1100000.0f);
	}
    public boolean isValid(EntityLivingBase entity) {
        return isValidType(entity) && entity.isEntityAlive() && (!entity.isInvisible() || invisibles.getValue());
    }

    private boolean isValidType(EntityLivingBase entity) {
        return (players.getValue() && entity instanceof EntityPlayer) || (mobs.getValue() && (entity instanceof EntityMob || entity instanceof EntitySlime) || (passives.getValue() && (entity instanceof EntityVillager || entity instanceof EntityGolem)) || (animals.getValue() && entity instanceof EntityAnimal));
    }


}
