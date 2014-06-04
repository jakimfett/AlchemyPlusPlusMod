package alchemyplusplus.tileentities.mixer;

import alchemyplusplus.utility.MixingHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;

public class TileEntityLiquidMixer extends TileEntity implements IInventory
{

    public ItemStack[] mixerInventory = new ItemStack[5];

    public int mixingTicks = 0;

    @Override
    public void closeChest()
    {
    }

    public ItemStack decrStackSize(int slot, int amount)
    {
        if (this.mixerInventory[slot] != null)
        {
            ItemStack itemstack;

            if (this.mixerInventory[slot].stackSize <= amount)
            {
                itemstack = this.mixerInventory[slot];
                this.mixerInventory[slot] = null;
                return itemstack;
            } else
            {
                itemstack = this.mixerInventory[slot].splitStack(amount);

                if (this.mixerInventory[slot].stackSize == 0)
                {
                    this.mixerInventory[slot] = null;
                }

                return itemstack;
            }
        } else
        {
            return null;
        }
    }

    public Packet getDescriptionPacket()
    {
        NBTTagCompound nbtTag = new NBTTagCompound();
        this.writeToNBT(nbtTag);
        return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 1, nbtTag);
    }

    public String getInvName()
    {
        return "Potion_Mixer";
    }

    public int getInventoryStackLimit()
    {
        return 64;
    }

    public int getProgressPixels()
    {
        float percentage = this.mixingTicks / 400.0f;
        return (int) (percentage * 34);
    }

    public int getSizeInventory()
    {
        return this.mixerInventory.length;
    }

    public ItemStack getStackInSlot(int slot)
    {
        return this.mixerInventory[slot];
    }

    public ItemStack getStackInSlotOnClosing(int slot)
    {
        if (this.mixerInventory[slot] != null)
        {
            ItemStack itemstack = this.mixerInventory[slot];
            this.mixerInventory[slot] = null;
            return itemstack;
        } else
        {
            return null;
        }
    }

    public boolean isInvNameLocalized()
    {
        return true;
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack)
    {
        return true;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        return true;
    }

    public void onDataPacket(INetworkManager net, Packet132TileEntityData packet)
    {
        readFromNBT(packet.data);
    }

    @Override
    public void openChest()
    {
    }

    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        NBTTagList nbttaglist = par1NBTTagCompound.getTagList("Items");
        this.mixerInventory = new ItemStack[this.getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.tagAt(i);
            byte b0 = nbttagcompound1.getByte("Slot");

            if (b0 >= 0 && b0 < this.mixerInventory.length)
            {
                this.mixerInventory[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }

        this.mixingTicks = par1NBTTagCompound.getShort("mixingTicks");

    }

    public void setInventorySlotContents(int slot, ItemStack stack)
    {
        this.mixerInventory[slot] = stack;

        if (stack != null && stack.stackSize > this.getInventoryStackLimit())
        {
            stack.stackSize = this.getInventoryStackLimit();
        }
    }

    public void updateEntity()
    {

        if (MixingHelper.mixingPossible(mixerInventory[0], mixerInventory[1], mixerInventory[2], mixerInventory[3], mixerInventory[4]))
        {
            this.mixingTicks += 2;
        } else
        {
            this.mixingTicks = 0;
        }
        if (this.mixingTicks >= 400)
        {
            this.mixingTicks = 0;
            MixingHelper.mix(this);
        }

    }

    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setShort("mixingTicks", (short) this.mixingTicks);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.mixerInventory.length; ++i)
        {
            if (this.mixerInventory[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte) i);
                this.mixerInventory[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        par1NBTTagCompound.setTag("Items", nbttaglist);

    }

}
