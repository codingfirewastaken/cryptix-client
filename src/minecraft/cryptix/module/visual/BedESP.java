package cryptix.module.visual;

import java.util.ArrayList;
import java.util.List;

import cryptix.Client;
import cryptix.gui.clickgui.Setting;
import cryptix.module.Category;
import cryptix.module.Module;
import cryptix.module.ModuleManager;
import cryptix.module.player.BedNuker;
import cryptix.utils.render.EspUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.BlockPos;

public class BedESP extends Module{
	private int ticks, bed;
	private BlockPos[] beds;
	private Setting range, showObby, red, green, blue, bednuker;
	public BedESP() {
		super("BedESP", 0, Category.VISUAL);
		Client.instance.settingsManager.rSetting(bednuker = new Setting("Only BedNuker", this, false));
		Client.instance.settingsManager.rSetting(showObby = new Setting("Show Obsidian", this, false));
		Client.instance.settingsManager.rSetting(range = new Setting("Range", this, 100.0, 10.0, 500.0, true));
		Client.instance.settingsManager.rSetting(red = new Setting("Red", this, 255, 0.0, 255.0, true));
		Client.instance.settingsManager.rSetting(green = new Setting("Green", this, 255, 0.0, 255.0, true));
		Client.instance.settingsManager.rSetting(blue = new Setting("Blue", this, 255, 0.0, 255.0, true));
	}
	
	@Override
	public void onDisable() {
		ticks = 0;
	}
	
	@Override
	public void onEnable() {
		
	}
	
	@Override
	public void onRender3D() {
	    ticks++;

	    double playerX = mc.thePlayer.posX;
	    double playerY = mc.thePlayer.posY;
	    double playerZ = mc.thePlayer.posZ;
	    
	    if(bednuker.getBoolean()) {
	    	BlockPos bedPos = Client.instance.moduleManager.bedNuker.bedPos;
	    	if(bedPos != null) {
	    		EspUtils.drawBedESP(bedPos, (float) red.getValue(), (float) green.getValue(), (float) blue.getValue());
	    	}
	    }else {
		    int rangeValue = (int) range.getValue();
		    for (int x = (int) -rangeValue; x <= rangeValue; x++) {
		        for (int y = -10; y <= 10; y++) {
		            for (int z = (int) -rangeValue; z <= rangeValue; z++) {
		                BlockPos blockPos = new BlockPos(playerX + (double) x, playerY + (double) y, playerZ + (double) z);
		                IBlockState blockState = mc.theWorld.getBlockState(blockPos);
		                Block block = blockState.getBlock();
	
		                if (block instanceof BlockBed) {
		                    EspUtils.drawBedESP(blockPos, (float) red.getValue(), (float) green.getValue(), (float) blue.getValue());
		                }
	
		                if (showObby.getBoolean() && block instanceof BlockObsidian) {
		                    EspUtils.drawObby(blockPos, 1.0f, 0.0f, 1.0f);
		                }
		            }
		        }
		    }
	    }
	}

}
