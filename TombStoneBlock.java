package tombStone;

import java.util.Random;

import tombStone.client.ClientProxy;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class TombStoneBlock extends BlockContainer {

	public TombStoneBlock (int id) {
		super(id, Material.rock);
		setHardness(2.0F);
		setResistance(50.0F);
		setStepSound(Block.soundStoneFootstep);
		setBlockName("tombStoneBlock");
		setCreativeTab(CreativeTabs.tabBlock);
	}
	
	@Override
	public int getBlockTextureFromSide(int side)
	{
		//We use textures 0-5 for this block
		return side;
	}
	
	@Override
	public String getTextureFile () {
		return CommonProxy.BLOCK_PNG;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int idk, float what, float these, float are) {
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		if (tileEntity == null || player.isSneaking()) {
			return false;
		}
		
		player.openGui(TombStone.instance, 0, world, x, y, z);
		return true;
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, int par5, int par6) {
		dropItems(world, x, y, z);
		super.breakBlock(world, x, y, z, par5, par6);
	}
	
	@Override
    public int idDropped(int par1, Random par2Random, int par3)
    {
        return 0;
    }

	
	private void dropItems(World world, int x, int y, int z){
		Random rand = new Random();
		
		//Capture the TombStoneTileEntity based on the position of the block
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		if (!(tileEntity instanceof IInventory)) {
			return;
		}
		
		//And get the inventory space of the tile entity
		IInventory inventory = (IInventory) tileEntity;
		
		//Loop through all slots in the tombstone
		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			ItemStack item = inventory.getStackInSlot(i);
			
			//Only do something if the slot is valid
			if (item != null && item.stackSize > 0) {
				//Calculate a random velocity for the item
				float rx = rand.nextFloat() * 0.8F + 0.1F;
				float ry = rand.nextFloat() * 0.8F + 0.1F;
				float rz = rand.nextFloat() * 0.8F + 0.1F;
				
				//Create the item
				EntityItem entityItem = new EntityItem(world,
				                x + rx, y + ry, z + rz,
				                new ItemStack(item.itemID, item.stackSize, item.getItemDamage()));
				
				//Copy any NBT tags associated with the item
				if (item.hasTagCompound()) {
					entityItem.func_92014_d().setTagCompound((NBTTagCompound) item.getTagCompound().copy());
				}
				
				//Assign the velocity
				float factor = 0.05F;
				entityItem.motionX = rand.nextGaussian() * factor;
				entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
				entityItem.motionZ = rand.nextGaussian() * factor;
				
				//Spawn the item
				world.spawnEntityInWorld(entityItem);
				
				//Set the local slot to empty
				item.stackSize = 0;
			}
		}
	}
	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		if(metadata > 0 && metadata <= TombStone.instance.tombOwnerList.size())
		{
			return new TombStoneTileEntity((String) TombStone.instance.tombOwnerList.get(metadata-1), (String) TombStone.instance.tombTextList.get(metadata-1));
		} else {
			return new TombStoneTileEntity();
		}
	}
	
	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TombStoneTileEntity();
	}
	
    public boolean renderAsNormalBlock()
    {
        return false;
    }
    
    public int getRenderType()
    {
        return -1;
    }
    
    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        return false;
    }

}
