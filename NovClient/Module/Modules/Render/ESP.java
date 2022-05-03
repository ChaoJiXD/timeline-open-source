
package NovClient.Module.Modules.Render;

import java.awt.Color;
import java.io.PrintStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.lwjgl.opengl.GL11;

import NovClient.Client;
import NovClient.API.EventHandler;
import NovClient.API.Events.Render.EventRender2D;
import NovClient.API.Events.Render.EventRender3D;
import NovClient.API.Value.Mode;
import NovClient.API.Value.Numbers;
import NovClient.API.Value.Option;
import NovClient.Module.Module;
import NovClient.Module.ModuleType;
import NovClient.Module.Modules.Misc.Teams;
import NovClient.Util.Colors2;
import NovClient.Util.ETBRenderUtil;
import NovClient.Util.Render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Timer;
import net.minecraft.util.Vec3;

public class ESP
extends Module {
    private Numbers<Double> skeletonwidth = new Numbers("SkeletonWidth", 1.0, 0.5, 10.0, 0.1);
    private static Map<EntityPlayer, float[][]> entities = new HashMap<>();
    private Option<Boolean> skeleton = new Option("Skeleton", false);
    public static Mode<Enum> mode = new Mode("Mode", "mode", (Enum[])TwoD.values(), (Enum)TwoD.Box);
    public static Option<Boolean> HEALTH = new Option("Health", "Health", (Object)true);
    public static Option<Boolean> player = new Option("Players", "Players", (Object)true);
    public static Option<Boolean> invis = new Option("Invis", "Invis", (Object)true);
    private Map<EntityLivingBase, double[]> entityConvertedPointsMap;
    FontRenderer fr;

    public ESP() {
        super("ESP", new String[0], ModuleType.Render);
        this.addValues(mode, HEALTH, player, invis,skeleton,skeletonwidth);
        this.entityConvertedPointsMap = new HashMap();
        ArrayList settings = new ArrayList();
    }
    public static void addEntity(EntityPlayer e, ModelPlayer model) {
        entities.put(e, new float[][]{{model.bipedHead.rotateAngleX, model.bipedHead.rotateAngleY, model.bipedHead.rotateAngleZ}, {model.bipedRightArm.rotateAngleX, model.bipedRightArm.rotateAngleY, model.bipedRightArm.rotateAngleZ}, {model.bipedLeftArm.rotateAngleX, model.bipedLeftArm.rotateAngleY, model.bipedLeftArm.rotateAngleZ}, {model.bipedRightLeg.rotateAngleX, model.bipedRightLeg.rotateAngleY, model.bipedRightLeg.rotateAngleZ}, {model.bipedLeftLeg.rotateAngleX, model.bipedLeftLeg.rotateAngleY, model.bipedLeftLeg.rotateAngleZ}});
    }
    @EventHandler
    public void onRender(EventRender3D event) {
        try {
            this.updatePositions();
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    @EventHandler
    public void onRender2D(EventRender2D event) {
        GlStateManager.pushMatrix();
        for (Entity entity : this.entityConvertedPointsMap.keySet()) {
            EntityPlayer ent = (EntityPlayer)entity;
            double[] renderPositions = (double[])this.entityConvertedPointsMap.get((Object)ent);
            double[] renderPositionsBottom = new double[]{renderPositions[4], renderPositions[5], renderPositions[6]};
            double[] renderPositionsX = new double[]{renderPositions[7], renderPositions[8], renderPositions[9]};
            double[] renderPositionsX2 = new double[]{renderPositions[10], renderPositions[11], renderPositions[12]};
            double[] renderPositionsZ = new double[]{renderPositions[13], renderPositions[14], renderPositions[15]};
            double[] renderPositionsZ2 = new double[]{renderPositions[16], renderPositions[17], renderPositions[18]};
            double[] renderPositionsTop1 = new double[]{renderPositions[19], renderPositions[20], renderPositions[21]};
            double[] renderPositionsTop2 = new double[]{renderPositions[22], renderPositions[23], renderPositions[24]};
            boolean bl = renderPositions[3] > 0.0 && renderPositions[3] <= 1.0 && renderPositionsBottom[2] > 0.0 && renderPositionsBottom[2] <= 1.0 && renderPositionsX[2] > 0.0 && renderPositionsX[2] <= 1.0 && renderPositionsX2[2] > 0.0 && renderPositionsX2[2] <= 1.0 && renderPositionsZ[2] > 0.0 && renderPositionsZ[2] <= 1.0 && renderPositionsZ2[2] > 0.0 && renderPositionsZ2[2] <= 1.0 && renderPositionsTop1[2] > 0.0 && renderPositionsTop1[2] <= 1.0 && renderPositionsTop2[2] > 0.0 && renderPositionsTop2[2] <= 1.0;
            boolean shouldRender = bl;
            GlStateManager.pushMatrix();
            GlStateManager.scale((double)0.5, (double)0.5, (double)0.5);
            if ((((Boolean)invis.getValue()).booleanValue() || !ent.isInvisible()) && ent instanceof EntityPlayer && !(ent instanceof EntityPlayerSP)) {
                try {
                    double[] xValues = new double[]{renderPositions[0], renderPositionsBottom[0], renderPositionsX[0], renderPositionsX2[0], renderPositionsZ[0], renderPositionsZ2[0], renderPositionsTop1[0], renderPositionsTop2[0]};
                    double[] yValues = new double[]{renderPositions[1], renderPositionsBottom[1], renderPositionsX[1], renderPositionsX2[1], renderPositionsZ[1], renderPositionsZ2[1], renderPositionsTop1[1], renderPositionsTop2[1]};
                    double x = renderPositions[0];
                    double y = renderPositions[1];
                    double endx = renderPositionsBottom[0];
                    double endy = renderPositionsBottom[1];
                    for (double bdubs : xValues) {
                        if (!(bdubs < x)) continue;
                        x = bdubs;
                    }
                    for (double bdubs : xValues) {
                        if (!(bdubs > endx)) continue;
                        endx = bdubs;
                    }
                    for (double bdubs : yValues) {
                        if (!(bdubs < y)) continue;
                        y = bdubs;
                    }
                    for (double bdubs : yValues) {
                        if (!(bdubs > endy)) continue;
                        endy = bdubs;
                    }
                    double xDiff = (endx - x) / 4.0;
                    double x2Diff = (endx - x) / 4.0;
                    double yDiff = xDiff;
                    int color = Colors2.getColor((int)255, (int)255);
                    color = Teams.isOnSameTeam((Entity)ent) ? Colors2.getColor((int)0, (int)255, (int)0, (int)255) : (ent.hurtTime > 0 ? Colors2.getColor((int)255, (int)0, (int)0, (int)255) : (ent.isInvisible() ? Colors2.getColor((int)255, (int)255, (int)0, (int)255) : Colors2.getColor((int)255, (int)255, (int)255, (int)255)));
                    if (mode.getValue() == TwoD.Box) {
                        RenderUtil.rectangleBordered((double)(x + 0.5), (double)(y + 0.5), (double)(endx - 0.5), (double)(endy - 0.5), (double)1.0, (int)Colors2.getColor((int)0, (int)0, (int)0, (int)0), (int)color);
                        RenderUtil.rectangleBordered((double)(x - 0.5), (double)(y - 0.5), (double)(endx + 0.5), (double)(endy + 0.5), (double)1.0, (int)Colors2.getColor((int)0, (int)0), (int)Colors2.getColor((int)0, (int)150));
                        RenderUtil.rectangleBordered((double)(x + 1.5), (double)(y + 1.5), (double)(endx - 1.5), (double)(endy - 1.5), (double)1.0, (int)Colors2.getColor((int)0, (int)0), (int)Colors2.getColor((int)0, (int)150));
                    }
                    if (mode.getValue() == TwoD.Corner) {
                        RenderUtil.rectangle((double)(x + 0.5), (double)(y + 0.5), (double)(x + 1.5), (double)(y + yDiff + 0.5), (int)color);
                        RenderUtil.rectangle((double)(x + 0.5), (double)(endy - 0.5), (double)(x + 1.5), (double)(endy - yDiff - 0.5), (int)color);
                        RenderUtil.rectangle((double)(x - 0.5), (double)(y + 0.5), (double)(x + 0.5), (double)(y + yDiff + 0.5), (int)Colors2.getColor((int)0, (int)150));
                        RenderUtil.rectangle((double)(x + 1.5), (double)(y + 2.5), (double)(x + 2.5), (double)(y + yDiff + 0.5), (int)Colors2.getColor((int)0, (int)150));
                        RenderUtil.rectangle((double)(x - 0.5), (double)(y + yDiff + 0.5), (double)(x + 2.5), (double)(y + yDiff + 1.5), (int)Colors2.getColor((int)0, (int)150));
                        RenderUtil.rectangle((double)(x - 0.5), (double)(endy - 0.5), (double)(x + 0.5), (double)(endy - yDiff - 0.5), (int)Colors2.getColor((int)0, (int)150));
                        RenderUtil.rectangle((double)(x + 1.5), (double)(endy - 2.5), (double)(x + 2.5), (double)(endy - yDiff - 0.5), (int)Colors2.getColor((int)0, (int)150));
                        RenderUtil.rectangle((double)(x - 0.5), (double)(endy - yDiff - 0.5), (double)(x + 2.5), (double)(endy - yDiff - 1.5), (int)Colors2.getColor((int)0, (int)150));
                        RenderUtil.rectangle((double)(x + 1.0), (double)(y + 0.5), (double)(x + x2Diff), (double)(y + 1.5), (int)color);
                        RenderUtil.rectangle((double)(x - 0.5), (double)(y - 0.5), (double)(x + x2Diff), (double)(y + 0.5), (int)Colors2.getColor((int)0, (int)150));
                        RenderUtil.rectangle((double)(x + 1.5), (double)(y + 1.5), (double)(x + x2Diff), (double)(y + 2.5), (int)Colors2.getColor((int)0, (int)150));
                        RenderUtil.rectangle((double)(x + x2Diff), (double)(y - 0.5), (double)(x + x2Diff + 1.0), (double)(y + 2.5), (int)Colors2.getColor((int)0, (int)150));
                        RenderUtil.rectangle((double)(x + 1.0), (double)(endy - 0.5), (double)(x + x2Diff), (double)(endy - 1.5), (int)color);
                        RenderUtil.rectangle((double)(x - 0.5), (double)(endy + 0.5), (double)(x + x2Diff), (double)(endy - 0.5), (int)Colors2.getColor((int)0, (int)150));
                        RenderUtil.rectangle((double)(x + 1.5), (double)(endy - 1.5), (double)(x + x2Diff), (double)(endy - 2.5), (int)Colors2.getColor((int)0, (int)150));
                        RenderUtil.rectangle((double)(x + x2Diff), (double)(endy + 0.5), (double)(x + x2Diff + 1.0), (double)(endy - 2.5), (int)Colors2.getColor((int)0, (int)150));
                        RenderUtil.rectangle((double)(endx - 0.5), (double)(y + 0.5), (double)(endx - 1.5), (double)(y + yDiff + 0.5), (int)color);
                        RenderUtil.rectangle((double)(endx - 0.5), (double)(endy - 0.5), (double)(endx - 1.5), (double)(endy - yDiff - 0.5), (int)color);
                        RenderUtil.rectangle((double)(endx + 0.5), (double)(y + 0.5), (double)(endx - 0.5), (double)(y + yDiff + 0.5), (int)Colors2.getColor((int)0, (int)150));
                        RenderUtil.rectangle((double)(endx - 1.5), (double)(y + 2.5), (double)(endx - 2.5), (double)(y + yDiff + 0.5), (int)Colors2.getColor((int)0, (int)150));
                        RenderUtil.rectangle((double)(endx + 0.5), (double)(y + yDiff + 0.5), (double)(endx - 2.5), (double)(y + yDiff + 1.5), (int)Colors2.getColor((int)0, (int)150));
                        RenderUtil.rectangle((double)(endx + 0.5), (double)(endy - 0.5), (double)(endx - 0.5), (double)(endy - yDiff - 0.5), (int)Colors2.getColor((int)0, (int)150));
                        RenderUtil.rectangle((double)(endx - 1.5), (double)(endy - 2.5), (double)(endx - 2.5), (double)(endy - yDiff - 0.5), (int)Colors2.getColor((int)0, (int)150));
                        RenderUtil.rectangle((double)(endx + 0.5), (double)(endy - yDiff - 0.5), (double)(endx - 2.5), (double)(endy - yDiff - 1.5), (int)Colors2.getColor((int)0, (int)150));
                        RenderUtil.rectangle((double)(endx - 1.0), (double)(y + 0.5), (double)(endx - x2Diff), (double)(y + 1.5), (int)color);
                        RenderUtil.rectangle((double)(endx + 0.5), (double)(y - 0.5), (double)(endx - x2Diff), (double)(y + 0.5), (int)Colors2.getColor((int)0, (int)150));
                        RenderUtil.rectangle((double)(endx - 1.5), (double)(y + 1.5), (double)(endx - x2Diff), (double)(y + 2.5), (int)Colors2.getColor((int)0, (int)150));
                        RenderUtil.rectangle((double)(endx - x2Diff), (double)(y - 0.5), (double)(endx - x2Diff - 1.0), (double)(y + 2.5), (int)Colors2.getColor((int)0, (int)150));
                        RenderUtil.rectangle((double)(endx - 1.0), (double)(endy - 0.5), (double)(endx - x2Diff), (double)(endy - 1.5), (int)color);
                        RenderUtil.rectangle((double)(endx + 0.5), (double)(endy + 0.5), (double)(endx - x2Diff), (double)(endy - 0.5), (int)Colors2.getColor((int)0, (int)150));
                        RenderUtil.rectangle((double)(endx - 1.5), (double)(endy - 1.5), (double)(endx - x2Diff), (double)(endy - 2.5), (int)Colors2.getColor((int)0, (int)150));
                        RenderUtil.rectangle((double)(endx - x2Diff), (double)(endy + 0.5), (double)(endx - x2Diff - 1.0), (double)(endy - 2.5), (int)Colors2.getColor((int)0, (int)150));
                    }
                    mode.getValue();
                    if (((Boolean)HEALTH.getValue()).booleanValue()) {
                        float health = ent.getHealth();
                        float[] fractions = new float[]{0.0f, 0.5f, 1.0f};
                        Color[] colors = new Color[]{Color.RED, Color.YELLOW, Color.GREEN};
                        float progress = health / ent.getMaxHealth();
                        Color customColor = health >= 0.0f ? ESP.blendColors(fractions, colors, progress).brighter() : Color.RED;
                        double difference = y - endy + 0.5;
                        double healthLocation = endy + difference * (double)progress;
                        RenderUtil.rectangleBordered((double)(x - 6.5), (double)(y - 0.5), (double)(x - 2.5), (double)endy, (double)1.0, (int)Colors2.getColor((int)0, (int)100), (int)Colors2.getColor((int)0, (int)150));
                        RenderUtil.rectangle((double)(x - 5.5), (double)(endy - 1.0), (double)(x - 3.5), (double)healthLocation, (int)customColor.getRGB());
                        if (-difference > 50.0) {
                            for (int i = 1; i < 10; ++i) {
                                double dThing = difference / 10.0 * (double)i;
                                RenderUtil.rectangle((double)(x - 6.5), (double)(endy - 0.5 + dThing), (double)(x - 2.5), (double)(endy - 0.5 + dThing - 1.0), (int)Colors2.getColor((int)0));
                            }
                        }
                        if ((int)ESP.getIncremental(progress * 100.0f, 1.0) <= 40) {
                            GlStateManager.pushMatrix();
                            GlStateManager.scale((float)2.0f, (float)2.0f, (float)2.0f);
                            String nigger = String.valueOf((int)((int)ESP.getIncremental(health * 5.0f, 1.0))) + "HP";
                            GlStateManager.popMatrix();
                        }
                    }
                }
                catch (Exception xValues) {
                    // empty catch block
                }
            }
            GlStateManager.popMatrix();
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        }
        GL11.glScalef((float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.popMatrix();
        RenderUtil.rectangle((double)0.0, (double)0.0, (double)0.0, (double)0.0, (int)-1);
    }

    public static double getIncremental(double val, double inc) {
        double one = 1.0 / inc;
        return (double)Math.round((double)(val * one)) / one;
    }

    public static Color blendColors(float[] fractions, Color[] colors, float progress) {
        Object color = null;
        if (fractions == null) {
            throw new IllegalArgumentException("Fractions can't be null");
        }
        if (colors == null) {
            throw new IllegalArgumentException("Colours can't be null");
        }
        if (fractions.length != colors.length) {
            throw new IllegalArgumentException("Fractions and colours must have equal number of elements");
        }
        int[] indicies = ESP.getFractionIndicies(fractions, progress);
        float[] range = new float[]{fractions[indicies[0]], fractions[indicies[1]]};
        Color[] colorRange = new Color[]{colors[indicies[0]], colors[indicies[1]]};
        float max = range[1] - range[0];
        float value = progress - range[0];
        float weight = value / max;
        return ESP.blend(colorRange[0], colorRange[1], 1.0f - weight);
    }

    public static Color blend(Color color1, Color color2, double ratio) {
        float r = (float)ratio;
        float ir = 1.0f - r;
        float[] rgb1 = new float[3];
        float[] rgb2 = new float[3];
        color1.getColorComponents(rgb1);
        color2.getColorComponents(rgb2);
        float red = rgb1[0] * r + rgb2[0] * ir;
        float green = rgb1[1] * r + rgb2[1] * ir;
        float blue = rgb1[2] * r + rgb2[2] * ir;
        if (red < 0.0f) {
            red = 0.0f;
        } else if (red > 255.0f) {
            red = 255.0f;
        }
        if (green < 0.0f) {
            green = 0.0f;
        } else if (green > 255.0f) {
            green = 255.0f;
        }
        if (blue < 0.0f) {
            blue = 0.0f;
        } else if (blue > 255.0f) {
            blue = 255.0f;
        }
        Color color3 = null;
        try {
            color3 = new Color(red, green, blue);
        }
        catch (IllegalArgumentException exp) {
            NumberFormat nf = NumberFormat.getNumberInstance();
            System.out.println(String.valueOf((Object)String.valueOf((Object)nf.format((double)red))) + "; " + nf.format((double)green) + "; " + nf.format((double)blue));
            exp.printStackTrace();
        }
        return color3;
    }

    public static int[] getFractionIndicies(float[] fractions, float progress) {
        int startPoint;
        int[] range = new int[2];
        for (startPoint = 0; startPoint < fractions.length && fractions[startPoint] <= progress; ++startPoint) {
        }
        if (startPoint >= fractions.length) {
            startPoint = fractions.length - 1;
        }
        range[0] = startPoint - 1;
        range[1] = startPoint;
        return range;
    }
    @EventHandler
    public void onRender3D(EventRender3D e) {
        if (!skeleton.getValue()) return;
        startEnd(true);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glDisable(2848);
        entities.keySet().removeIf(this::doesntContain);
        mc.theWorld.playerEntities.forEach(player -> drawSkeleton(e,player));
        Gui.drawRect(0, 0, 0, 0, 0);
        startEnd(false);
    }
    private boolean doesntContain(EntityPlayer var0) {
        return !mc.theWorld.playerEntities.contains(var0);
    }
    private Vec3 getVec3(EventRender3D event, EntityPlayer var0) {
        float timer = event.getPartialTicks();
        double x = var0.lastTickPosX + (var0.posX - var0.lastTickPosX) * timer;
        double y = var0.lastTickPosY + (var0.posY - var0.lastTickPosY) * timer;
        double z = var0.lastTickPosZ + (var0.posZ - var0.lastTickPosZ) * timer;
        return new Vec3(x, y, z);
    }
    private void drawSkeleton(EventRender3D event, EntityPlayer e) {
        final Color color = new Color(Client.instance.getFriendManager().isFriend(e.getName()) ? 0xFF7FCDFF : (e.getName().equalsIgnoreCase(mc.thePlayer.getName()) ? 0xFF99ff99 : new Color(0xFFF9F8).getRGB()));
        if (!e.isInvisible()) {
            float[][] entPos = entities.get(e);
            if (entPos != null && e.isEntityAlive() && ETBRenderUtil.isInViewFrustrum(e) && !e.isDead && e != mc.thePlayer && !e.isPlayerSleeping()) {
                GL11.glPushMatrix();
                GL11.glLineWidth(skeletonwidth.getValue().floatValue());
                GlStateManager.color(color.getRed() / 255,color.getGreen() / 255,color.getBlue() / 255,1);
                Vec3 vec = getVec3(event,e);
                double x = vec.xCoord - RenderManager.renderPosX;
                double y = vec.yCoord - RenderManager.renderPosY;
                double z = vec.zCoord - RenderManager.renderPosZ;
                GL11.glTranslated(x, y, z);
                float xOff = e.prevRenderYawOffset + (e.renderYawOffset - e.prevRenderYawOffset) * event.getPartialTicks();
                GL11.glRotatef(-xOff, 0.0F, 1.0F, 0.0F);
                GL11.glTranslated(0.0D, 0.0D, e.isSneaking() ? -0.235D : 0.0D);
                float yOff = e.isSneaking() ? 0.6F : 0.75F;
                GL11.glPushMatrix();
                GlStateManager.color(1, 1, 1, 1);
                GL11.glTranslated(-0.125D, yOff, 0.0D);
                if (entPos[3][0] != 0.0F) {
                    GL11.glRotatef(entPos[3][0] * 57.295776F, 1.0F, 0.0F, 0.0F);
                }

                if (entPos[3][1] != 0.0F) {
                    GL11.glRotatef(entPos[3][1] * 57.295776F, 0.0F, 1.0F, 0.0F);
                }

                if (entPos[3][2] != 0.0F) {
                    GL11.glRotatef(entPos[3][2] * 57.295776F, 0.0F, 0.0F, 1.0F);
                }

                GL11.glBegin(3);
                GL11.glVertex3d(0.0D, 0.0D, 0.0D);
                GL11.glVertex3d(0.0D, (-yOff), 0.0D);
                GL11.glEnd();
                GL11.glPopMatrix();
                GL11.glPushMatrix();
                GlStateManager.color(1, 1, 1, 1);
                GL11.glTranslated(0.125D, yOff, 0.0D);
                if (entPos[4][0] != 0.0F) {
                    GL11.glRotatef(entPos[4][0] * 57.295776F, 1.0F, 0.0F, 0.0F);
                }

                if (entPos[4][1] != 0.0F) {
                    GL11.glRotatef(entPos[4][1] * 57.295776F, 0.0F, 1.0F, 0.0F);
                }

                if (entPos[4][2] != 0.0F) {
                    GL11.glRotatef(entPos[4][2] * 57.295776F, 0.0F, 0.0F, 1.0F);
                }

                GL11.glBegin(3);
                GL11.glVertex3d(0.0D, 0.0D, 0.0D);
                GL11.glVertex3d(0.0D, (-yOff), 0.0D);
                GL11.glEnd();
                GL11.glPopMatrix();
                GL11.glTranslated(0.0D, 0.0D, e.isSneaking() ? 0.25D : 0.0D);
                GL11.glPushMatrix();
                GlStateManager.color(1, 1, 1, 1);
                GL11.glTranslated(0.0D, e.isSneaking() ? -0.05D : 0.0D, e.isSneaking() ? -0.01725D : 0.0D);
                GL11.glPushMatrix();
                GlStateManager.color(1, 1, 1, 1);
                GL11.glTranslated(-0.375D, yOff + 0.55D, 0.0D);
                if (entPos[1][0] != 0.0F) {
                    GL11.glRotatef(entPos[1][0] * 57.295776F, 1.0F, 0.0F, 0.0F);
                }

                if (entPos[1][1] != 0.0F) {
                    GL11.glRotatef(entPos[1][1] * 57.295776F, 0.0F, 1.0F, 0.0F);
                }

                if (entPos[1][2] != 0.0F) {
                    GL11.glRotatef(-entPos[1][2] * 57.295776F, 0.0F, 0.0F, 1.0F);
                }

                GL11.glBegin(3);
                GL11.glVertex3d(0.0D, 0.0D, 0.0D);
                GL11.glVertex3d(0.0D, -0.5D, 0.0D);
                GL11.glEnd();
                GL11.glPopMatrix();
                GL11.glPushMatrix();
                GL11.glTranslated(0.375D, yOff + 0.55D, 0.0D);
                if (entPos[2][0] != 0.0F) {
                    GL11.glRotatef(entPos[2][0] * 57.295776F, 1.0F, 0.0F, 0.0F);
                }

                if (entPos[2][1] != 0.0F) {
                    GL11.glRotatef(entPos[2][1] * 57.295776F, 0.0F, 1.0F, 0.0F);
                }

                if (entPos[2][2] != 0.0F) {
                    GL11.glRotatef(-entPos[2][2] * 57.295776F, 0.0F, 0.0F, 1.0F);
                }

                GL11.glBegin(3);
                GL11.glVertex3d(0.0D, 0.0D, 0.0D);
                GL11.glVertex3d(0.0D, -0.5D, 0.0D);
                GL11.glEnd();
                GL11.glPopMatrix();
                GL11.glRotatef(xOff - e.rotationYawHead, 0.0F, 1.0F, 0.0F);
                GL11.glPushMatrix();
                GlStateManager.color(1, 1, 1, 1);
                GL11.glTranslated(0.0D, yOff + 0.55D, 0.0D);
                if (entPos[0][0] != 0.0F) {
                    GL11.glRotatef(entPos[0][0] * 57.295776F, 1.0F, 0.0F, 0.0F);
                }

                GL11.glBegin(3);
                GL11.glVertex3d(0.0D, 0.0D, 0.0D);
                GL11.glVertex3d(0.0D, 0.3D, 0.0D);
                GL11.glEnd();
                GL11.glPopMatrix();
                GL11.glPopMatrix();
                GL11.glRotatef(e.isSneaking() ? 25.0F : 0.0F, 1.0F, 0.0F, 0.0F);
                GL11.glTranslated(0.0D, e.isSneaking() ? -0.16175D : 0.0D, e.isSneaking() ? -0.48025D : 0.0D);
                GL11.glPushMatrix();
                GL11.glTranslated(0.0D, yOff, 0.0D);
                GL11.glBegin(3);
                GL11.glVertex3d(-0.125D, 0.0D, 0.0D);
                GL11.glVertex3d(0.125D, 0.0D, 0.0D);
                GL11.glEnd();
                GL11.glPopMatrix();
                GL11.glPushMatrix();
                GlStateManager.color(1,1,1,1);
                GL11.glTranslated(0.0D, yOff, 0.0D);
                GL11.glBegin(3);
                GL11.glVertex3d(0.0D, 0.0D, 0.0D);
                GL11.glVertex3d(0.0D, 0.55D, 0.0D);
                GL11.glEnd();
                GL11.glPopMatrix();
                GL11.glPushMatrix();
                GL11.glTranslated(0.0D, yOff + 0.55D, 0.0D);
                GL11.glBegin(3);
                GL11.glVertex3d(-0.375D, 0.0D, 0.0D);
                GL11.glVertex3d(0.375D, 0.0D, 0.0D);
                GL11.glEnd();
                GL11.glPopMatrix();
                GL11.glPopMatrix();
                GlStateManager.color(1, 1, 1, 1);
            }
        }
    }
    private void startEnd(boolean revert) {
        if (revert) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GL11.glEnable(2848);
            GlStateManager.disableDepth();
            GlStateManager.disableTexture2D();
            GlStateManager.blendFunc(770, 771);
            GL11.glHint(3154, 4354);
        } else {
            GlStateManager.disableBlend();
            GlStateManager.enableTexture2D();
            GL11.glDisable(2848);
            GlStateManager.enableDepth();
            GlStateManager.popMatrix();
        }

        GlStateManager.depthMask(!revert);
    }
    private void updatePositions() {
        this.entityConvertedPointsMap.clear();
        float pTicks = ESP.mc.timer.renderPartialTicks;
        for (Entity e2 : mc.theWorld.getLoadedEntityList()) {
            EntityPlayer ent;
            double topY;
            if (!(e2 instanceof EntityPlayer) || (ent = (EntityPlayer)e2) == mc.thePlayer) continue;
            double x = ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * (double)pTicks - ESP.mc.getRenderManager().viewerPosX + 0.36;
            double y = ent.lastTickPosY + (ent.posY - ent.lastTickPosY) * (double)pTicks - ESP.mc.getRenderManager().viewerPosY;
            double z = ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * (double)pTicks - ESP.mc.getRenderManager().viewerPosZ + 0.36;
            y = topY = y + ((double)ent.height + 0.15);
            double[] convertedPoints = RenderUtil.convertTo2D((double)x, (double)y, (double)z);
            double[] convertedPoints2 = RenderUtil.convertTo2D((double)(x - 0.36), (double)y, (double)(z - 0.36));
            double xd = 0.0;
            if (convertedPoints2[2] < 0.0 || convertedPoints2[2] >= 1.0) continue;
            x = ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * (double)pTicks - ESP.mc.getRenderManager().viewerPosX - 0.36;
            z = ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * (double)pTicks - ESP.mc.getRenderManager().viewerPosZ - 0.36;
            double[] convertedPointsBottom = RenderUtil.convertTo2D((double)x, (double)y, (double)z);
            y = ent.lastTickPosY + (ent.posY - ent.lastTickPosY) * (double)pTicks - ESP.mc.getRenderManager().viewerPosY - 0.05;
            double[] convertedPointsx = RenderUtil.convertTo2D((double)x, (double)y, (double)z);
            x = ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * (double)pTicks - ESP.mc.getRenderManager().viewerPosX - 0.36;
            z = ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * (double)pTicks - ESP.mc.getRenderManager().viewerPosZ + 0.36;
            double[] convertedPointsTop1 = RenderUtil.convertTo2D((double)x, (double)topY, (double)z);
            double[] convertedPointsx2 = RenderUtil.convertTo2D((double)x, (double)y, (double)z);
            x = ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * (double)pTicks - ESP.mc.getRenderManager().viewerPosX + 0.36;
            z = ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * (double)pTicks - ESP.mc.getRenderManager().viewerPosZ + 0.36;
            double[] convertedPointsz = RenderUtil.convertTo2D((double)x, (double)y, (double)z);
            x = ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * (double)pTicks - ESP.mc.getRenderManager().viewerPosX + 0.36;
            z = ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * (double)pTicks - ESP.mc.getRenderManager().viewerPosZ - 0.36;
            double[] convertedPointsTop2 = RenderUtil.convertTo2D((double)x, (double)topY, (double)z);
            double[] convertedPointsz2 = RenderUtil.convertTo2D((double)x, (double)y, (double)z);
            this.entityConvertedPointsMap.put((EntityLivingBase)ent, new double[]{convertedPoints[0], convertedPoints[1], 0.0, convertedPoints[2], convertedPointsBottom[0], convertedPointsBottom[1], convertedPointsBottom[2], convertedPointsx[0], convertedPointsx[1], convertedPointsx[2], convertedPointsx2[0], convertedPointsx2[1], convertedPointsx2[2], convertedPointsz[0], convertedPointsz[1], convertedPointsz[2], convertedPointsz2[0], convertedPointsz2[1], convertedPointsz2[2], convertedPointsTop1[0], convertedPointsTop1[1], convertedPointsTop1[2], convertedPointsTop2[0], convertedPointsTop2[1], convertedPointsTop2[2]});
        }
    }

    private String getColor(int level) {
        if (level == 2) {
            return "\u00a7a";
        }
        if (level == 3) {
            return "\u00a73";
        }
        if (level == 4) {
            return "\u00a74";
        }
        if (level >= 5) {
            return "\u00a76";
        }
        return "\u00a7f";
    }
    static enum TwoD {
        Box,
        Corner;
        
    }
}
