package TombStone;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.relauncher.Side;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.NetClientHandler;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet130UpdateSign;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;

public class DeathEventHook {
	
	public DeathEventHook()
	{
		
	}
	
	@ForgeSubscribe
	public void onEntityDeath(PlayerDropsEvent event)
	{
		EntityPlayer deadPlayer = event.entityPlayer;
		DamageSource attackSource = event.source;
		ArrayList<EntityItem> drops = event.drops;
		World world = deadPlayer.worldObj;
		
		//Calculate the spot to put the tombstone
		//TODO - Make this more intelligent
		int tombX = (int) Math.floor(deadPlayer.posX);
		int tombY = (int) Math.floor(deadPlayer.posY);
		int tombZ = (int) Math.floor(deadPlayer.posZ);
		
		String dateOfDeath = world.getCurrentDate().get(Calendar.MONTH) + "/" + world.getCurrentDate().get(Calendar.DAY_OF_MONTH) + "/" + world.getCurrentDate().get(Calendar.YEAR);
		String deathMessage = attackSource.getDeathMessage(deadPlayer) + " here\n Died " + dateOfDeath;
		
		TombStone.instance.tombOwnerList.add(deadPlayer.getEntityName());
		TombStone.instance.tombTextList.add(deathMessage);
		
		int index = TombStone.instance.tombOwnerList.size();
		
		//Place the tombstone
		world.setBlockAndMetadataWithUpdate(tombX, tombY, tombZ, TombStone.instance.tombStoneBlockId, index, true);
		TombStoneTileEntity blockTileEntity = (TombStoneTileEntity) world.getBlockTileEntity(tombX, tombY, tombZ);
		
		//Move all items from the list to the tombstone inventory
		for(int i=0; i<drops.size(); i++)
		{
			ItemStack playerItem = drops.get(i).func_92014_d();
			blockTileEntity.setInventorySlotContents(i, playerItem);
		}
		
		event.setCanceled(true);
	}
}
