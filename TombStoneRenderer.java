package tombStone;

import java.util.StringTokenizer;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.relauncher.Side;

import net.minecraft.block.Block;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

@SideOnly(Side.CLIENT)
public class TombStoneRenderer extends TileEntitySpecialRenderer
{
	private TombStoneModel tombModel = new TombStoneModel();
	public static TombStoneRenderer tombRenderer;
	
	public void renderTombStoneTileEntity(TombStoneTileEntity tileEntity, double par2, double par4, double par6, float par8)
	{
		GL11.glPushMatrix();
		
        GL11.glTranslatef((float)par2 + 0.5F, (float)par4 + 0.75F * 1.0F, (float)par6 + 0.5F);
        GL11.glPushMatrix();
        GL11.glScalef(1.0F, 1.0F, 1.0F);
		
        this.bindTextureByName("/tombStone/tombstone.png");
		this.tombModel.renderBase();
        this.bindTextureByName("/tombStone/tombstone2.png");
		this.tombModel.renderHeadstone();
		
		GL11.glPopMatrix();
		
		//Render the death text
		FontRenderer var17 = this.getFontRenderer();
		float var12 = 0.008F; //0.016666668F
		GL11.glTranslatef(0.0F, 0.0F, 0.1375F);
		GL11.glScalef(var12, -var12, var12);
		GL11.glNormal3f(0.0F, 0.0F, -1.0F * var12);
		GL11.glDepthMask(false);
		
		//Draw the player name
		var17.drawString(tileEntity.getOwner(), -var17.getStringWidth(tileEntity.getOwner()) / 2, -10, 0, false);

		//Draw the death message
		StringTokenizer tok = new StringTokenizer(tileEntity.getDeathText(), " ");
	    StringBuilder output = new StringBuilder(tileEntity.getDeathText().length());
	    int lineLen = 0;
	    while (tok.hasMoreTokens()) {
	        String word = tok.nextToken() + " ";

	        if (lineLen + word.length() > 12) {
	            output.append("\n");
	            lineLen = 0;
	        }
	        output.append(word);
	        lineLen += word.length();
	    }
		String[] splitString = output.toString().split("\n");
		for(int i=0; i<splitString.length; i++)
		{
			var17.drawString(splitString[i], -var17.getStringWidth(splitString[i]) / 2, i * 10, 0, false);
		}
		GL11.glDepthMask(true);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);		
		GL11.glPopMatrix();
	}
	
	@Override
	public void renderTileEntityAt(TileEntity par1TileEntity, double par2, double par4, double par6, float par8)
	{
		this.renderTombStoneTileEntity((TombStoneTileEntity)par1TileEntity, par2, par4, par6, par8);
	}
}
