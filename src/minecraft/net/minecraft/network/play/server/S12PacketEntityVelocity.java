package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.ArrayList;

import cryptix.Client;
import cryptix.gui.clickgui.Setting;
import cryptix.module.Module;
import cryptix.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S12PacketEntityVelocity implements Packet<INetHandlerPlayClient>
{
    private int entityID;
    private int motionX;
    private int motionY;
    private int motionZ;

    public S12PacketEntityVelocity()
    {
    }

    public S12PacketEntityVelocity(Entity entityIn)
    {
        this(entityIn.getEntityId(), entityIn.motionX, entityIn.motionY, entityIn.motionZ);
    }

    public S12PacketEntityVelocity(int entityIDIn, double motionXIn, double motionYIn, double motionZIn)
    {
        this.entityID = entityIDIn;
        double d0 = 3.9D;

        if (motionXIn < -d0)
        {
            motionXIn = -d0;
        }

        if (motionYIn < -d0)
        {
            motionYIn = -d0;
        }

        if (motionZIn < -d0)
        {
            motionZIn = -d0;
        }

        if (motionXIn > d0)
        {
            motionXIn = d0;
        }

        if (motionYIn > d0)
        {
            motionYIn = d0;
        }

        if (motionZIn > d0)
        {
            motionZIn = d0;
        }

        this.motionX = (int)(motionXIn * 8000.0D);
        this.motionY = (int)(motionYIn * 8000.0D);
        this.motionZ = (int)(motionZIn * 8000.0D);
    }

    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.entityID = buf.readVarIntFromBuffer();
        this.motionX = buf.readShort();
        this.motionY = buf.readShort();
        this.motionZ = buf.readShort();
    }

    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeVarIntToBuffer(this.entityID);
        buf.writeShort(this.motionX);
        buf.writeShort(this.motionY);
        buf.writeShort(this.motionZ);
    }

    public void processPacket(INetHandlerPlayClient handler)
    {
    	Module velo = Client.instance.moduleManager.getModuleByName("Velocity");
    	if(!velo.isToggled()) {
    		handler.handleEntityVelocity(this);
    	}else {
    		int h = (int) Client.instance.settingsManager.getSettingByName(velo, "Horizontal").getValue();
    		int v = (int) Client.instance.settingsManager.getSettingByName(velo, "Vertical").getValue();
    		double motionX = (double)getMotionX() / 8000.0;
            double motionY = (double)getMotionY() / 8000.0;
            double motionZ = (double)getMotionZ() / 8000.0;
            double finalMotionX = this.getMotion(Client.mc.thePlayer.motionX, motionX *= h / 100.0);
            double finalMotionY = this.getMotion(Client.mc.thePlayer.motionY, motionY *= v / 100.0);
            double finalMotionZ = this.getMotion(Client.mc.thePlayer.motionZ, motionZ *= h / 100.0);
            handler.handleEntityVelocity(new S12PacketEntityVelocity(this.entityID, finalMotionX, finalMotionY, finalMotionZ));
    	}
    }
    
    private double getMotion(double curMotion, double packetMotion) {
        if (packetMotion == 0.0) {
            return curMotion;
        }
        if (Math.signum(curMotion) != Math.signum(packetMotion)) {
            return Math.abs(curMotion) > Math.abs(packetMotion) ? curMotion : packetMotion;
        }
        if (Math.abs(curMotion) > Math.abs(packetMotion)) {
            return curMotion;
        }
        return packetMotion;
    }

    public int getEntityID()
    {
        return this.entityID;
    }

    public int getMotionX()
    {
        return this.motionX;
    }

    public int getMotionY()
    {
        return this.motionY;
    }

    public int getMotionZ()
    {
        return this.motionZ;
    }
}
