package TombStone;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import TombStone.client.ClientProxy;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid="TombStone", name="TombStone", version="0.0.1")
@NetworkMod(clientSideRequired=true, serverSideRequired=false)
public class TombStone {
	public final static int tombStoneBlockId = 3000;
	
	public static TombStoneBlock tombStoneBlock = new TombStoneBlock(tombStoneBlockId);
	
	public static List<String> tombOwnerList = new ArrayList<String>();
	public static List<String> tombTextList = new ArrayList<String>();
	
	// The instance of your mod that Forge uses.
	@Instance("TombStone")
	public static TombStone instance;
	
	// Says where the client and server 'proxy' code is loaded.
	@SidedProxy(clientSide="tombStone.client.ClientProxy", serverSide="tombStone.CommonProxy")
	public static CommonProxy proxy;
	
	@PreInit
	public void preInit(FMLPreInitializationEvent event) {
		// Stub Method
	}
	
	@Init
	public void load(FMLInitializationEvent event) {
		proxy.registerRenderers();
		
		if(event.getSide().isClient())
			ClientProxy.setCustomRenderers();
		
		//Register the tombstone block
		GameRegistry.registerBlock(tombStoneBlock, "tombStoneBlock");
		LanguageRegistry.addName(tombStoneBlock, "TombStone Block");
		MinecraftForge.setBlockHarvestLevel(tombStoneBlock, "pickaxe", 0);
		
		//Register the death hook
		MinecraftForge.EVENT_BUS.register(new DeathEventHook());
		
		//Register the tombstone tile entity
		GameRegistry.registerTileEntity(TombStoneTileEntity.class, "tombStoneContainer");

		//Register the tombstone gui
		NetworkRegistry.instance().registerGuiHandler(this, new TombStoneGUIHandler());
		    
		//Item stack (ID, Count, Meta)
		ItemStack stoneStack = new ItemStack(Block.stone);
		ItemStack signStack = new ItemStack(Item.sign);
		
		//3x3 shaped crafting
		GameRegistry.addRecipe(new ItemStack(tombStoneBlock), " x ", "xyx", "xxx",
		    'x', stoneStack, 'y', signStack);    
	}
	
	@PostInit
	public void postInit(FMLPostInitializationEvent event) {
	        // Stub Method
	}
}