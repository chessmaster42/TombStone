package TombStone;

import java.util.ArrayList;

import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.relauncher.Side;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;

public class DeathEventHook {
	
	DeathEventHook()
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
		
		TombStone.instance.tombOwnerList.add(deadPlayer.getEntityName());
		TombStone.instance.tombTextList.add(attackSource.getDeathMessage(deadPlayer));
		
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
