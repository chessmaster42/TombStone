package TombStone;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.ForgeSubscribe;

public class ChatHandler extends CommandBase {

	@Override
	public String getCommandName() {
		return "tombstone";
	}
	
	@Override
    public List getCommandAliases()
    {
        return Arrays.asList(new String[] {"tombs"});
    }
	
	@Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

	@Override
	public void processCommand(ICommandSender var1, String[] var2) {
        int tombCounter = 1;
        String chatMessage = "";
        for(int i=0; i<TombStone.instance.tombList.size(); i++)
        {
        	TombStoneTileEntity item = TombStone.instance.tombList.get(i);
        	if(item.getOwner() == var1.getCommandSenderName())
        	{
        		//TODO - Output coordinates to chat
        		chatMessage += "Tomb " + tombCounter + ": " + item.xCoord + "," + item.yCoord + "," + item.zCoord + "\n";
        		tombCounter++;
        	}
        }
        if(!chatMessage.equals(""))
        	var1.sendChatToPlayer(chatMessage);
	}
}
