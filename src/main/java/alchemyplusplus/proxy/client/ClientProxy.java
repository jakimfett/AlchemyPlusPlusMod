package alchemyplusplus.proxy.client;

import alchemyplusplus.block.BlockRegistry;
import alchemyplusplus.proxy.CommonProxy;
import alchemyplusplus.proxy.client.render.block.RenderDiffuserBlock;
import alchemyplusplus.proxy.client.render.block.RenderDistilleryBlock;
import alchemyplusplus.proxy.client.render.block.RenderLiquidMixerBlock;
import alchemyplusplus.proxy.client.render.block.RenderPotionJugBlock;
import alchemyplusplus.proxy.client.render.item.RenderPotionJugItem;
import alchemyplusplus.tileentities.diffuser.TileEntityDiffuser;
import alchemyplusplus.tileentities.distillery.TileEntityDistillery;
import alchemyplusplus.tileentities.mixer.TileEntityLiquidMixer;
import alchemyplusplus.tileentities.potionjug.TileEntityPotionJug;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.registry.ClientRegistry;
import net.minecraft.item.Item;

public class ClientProxy extends CommonProxy
{

    @Override
    public void registerRenderers()
    {
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(BlockRegistry.potionJug), new RenderPotionJugItem());
        
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPotionJug.class, new RenderPotionJugBlock());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDistillery.class, new RenderDistilleryBlock());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDiffuser.class, new RenderDiffuserBlock());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLiquidMixer.class, new RenderLiquidMixerBlock());
    }
}