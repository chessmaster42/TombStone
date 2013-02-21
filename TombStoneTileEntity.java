package TombStone;

import java.util.logging.Level;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;

public class TombStoneTileEntity extends TileEntity implements IInventory {

	private ItemStack[] inv;
	
	//NBT tag(s)
	private String owner = "Nobody";
	private String deathText = "Nobody died\n Died Never";
	private boolean isCrafted = true;
	private boolean isAddedToTombList = false;
	
	public TombStoneTileEntity(){
		//A tombstone holds as much as a double-wide chest (54+)
		inv = new ItemStack[54];
		
	}
	
	public TombStoneTileEntity(String newOwner, String newDeathText, boolean newIsCrafted)
	{
		//A tombstone holds as much as a double-wide chest (54+)
		inv = new ItemStack[54];
		
		this.owner = newOwner;
		this.deathText = newDeathText;
		this.isCrafted = newIsCrafted;
	}
	
	public void finalize() throws Throwable
	{
		super.finalize();
		
		TombStone.instance.tombList.remove(this);
	}
	
	///////////////////////////////////////
	
	public boolean isCrafted()
	{
		return this.isCrafted;
	}
	
	public void setIsCrafted(boolean newIsCrafted)
	{
		this.isCrafted = newIsCrafted;
	}	
	
	public String getOwner()
	{
		return this.owner;
	}
	
	public void setOwner(String newOwner)
	{
		this.owner = newOwner;
	}
	
	public String getDeathText()
	{
		return this.deathText;
	}
	
	public void setDeathText(String newDeathText)
	{
		this.deathText = newDeathText;
	}
	
	///////////////////////////////////////
	
	@Override
	public int getSizeInventory() {
		return inv.length;
	}
	
	@Override
	public ItemStack getStackInSlot(int slot) {
		return inv[slot];
	}
	
	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		inv[slot] = stack;
		if (stack != null && stack.stackSize > getInventoryStackLimit()) {
			stack.stackSize = getInventoryStackLimit();
		}               
	}
	
	@Override
	public ItemStack decrStackSize(int slot, int amt) {
		ItemStack stack = getStackInSlot(slot);
		if (stack != null) {
			if (stack.stackSize <= amt) {
				setInventorySlotContents(slot, null);
			} else {
				stack = stack.splitStack(amt);
				if (stack.stackSize == 0) {
				setInventorySlotContents(slot, null);
				}
			}
		}
		return stack;
	}
	
	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		ItemStack stack = getStackInSlot(slot);
		if (stack != null) {
			setInventorySlotContents(slot, null);
		}
		return stack;
	}
	
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) == this &&
			player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64;
	}
	
	@Override
	public void openChest() {}
	
	@Override
	public void closeChest() {}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		
		owner = tagCompound.getString("owner");
		deathText = tagCompound.getString("deathText");
		isCrafted = tagCompound.getBoolean("isCrafted");
		
		//FMLLog.log(Level.WARNING, "Loaded Tombstone: owner=%s, deathText=%s, isCrafted=%b", owner, deathText, isCrafted);
	    
		NBTTagList tagList = tagCompound.getTagList("Inventory");
		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound tag = (NBTTagCompound) tagList.tagAt(i);
			byte slot = tag.getByte("Slot");
			if (slot >= 0 && slot < inv.length) {
				inv[slot] = ItemStack.loadItemStackFromNBT(tag);
			}
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		
		tagCompound.setString("owner", owner);
		tagCompound.setString("deathText", deathText);
		tagCompound.setBoolean("isCrafted", isCrafted);
		                
		//FMLLog.log(Level.WARNING, "Saved Tombstone: owner=%s, deathText=%s, isCrafted=%b", owner, deathText, isCrafted);

		NBTTagList itemList = new NBTTagList();
		for (int i = 0; i < inv.length; i++) {
			ItemStack stack = inv[i];
			if (stack != null) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setByte("Slot", (byte) i);
			stack.writeToNBT(tag);
			itemList.appendTag(tag);
			}
		}
		tagCompound.setTag("Inventory", itemList);
	}
	
	@Override
	public String getInvName() {
		return "tco.tombstonetileentity";
	}
	
	@Override
    public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt)
    {
		readFromNBT(pkt.customParam1);
		
		boolean foundDuplicate = false;
		for(int i=0; i<TombStone.instance.tombList.size(); i++)
		{
			TombStoneTileEntity item = TombStone.instance.tombList.get(i);
			if(item.xCoord == this.xCoord && item.yCoord == this.yCoord && item.zCoord == this.zCoord)
			{
				foundDuplicate = true;
			}
		}
		
		if(!isAddedToTombList && !foundDuplicate)
			TombStone.instance.tombList.add(this);
    }
	
	@Override
	public Packet getDescriptionPacket()
    {
        NBTTagCompound nbtData = new NBTTagCompound();
        this.writeToNBT(nbtData);
        
		boolean foundDuplicate = false;
		for(int i=0; i<TombStone.instance.tombList.size(); i++)
		{
			TombStoneTileEntity item = TombStone.instance.tombList.get(i);
			if(item.xCoord == this.xCoord && item.yCoord == this.yCoord && item.zCoord == this.zCoord)
			{
				foundDuplicate = true;
			}
		}
		
		if(!isAddedToTombList && !foundDuplicate)
			TombStone.instance.tombList.add(this);
        
        return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 0, nbtData);
    }
}