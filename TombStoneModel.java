package tombStone;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TombStoneModel extends ModelBase
{
    public ModelRenderer tombBase;
    public ModelRenderer tombHeadstone;

    public TombStoneModel()
    {
    	this.tombBase = new ModelRenderer(this, 0, 0);
    	this.tombBase.textureWidth = 64;
    	this.tombBase.textureHeight = 64;
        this.tombBase.addBox(-8.0F, -12.0F, -6.5F, 16, 3, 13, 0.0F);
        
    	this.tombHeadstone = new ModelRenderer(this, 0, 0);
    	this.tombHeadstone.textureWidth = 64;
    	this.tombHeadstone.textureHeight = 64;
        this.tombHeadstone.addBox(-5.0F, -9.0F, -2.0F, 10, 13, 4, 0.0F);
    }

    public void renderBase()
    {
        this.tombBase.render(0.0625F);
    }
    
    public void renderHeadstone()
    {
    	this.tombHeadstone.render(0.0625F);
    }
}

