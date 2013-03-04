package dustmod;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;

public class TileEntityDustTable extends TileEntity
{
    public int ticks;
    public float pageFlipping;
    public float prevPageFlipping;
    public float floatd;
    public float floate;
    public float floating;
    public float prevFloating;
    public float rotation;
    public float prevRotation;
    public float rotAmt;
    private static Random rand = new Random();

    public int page = 0;

    public int dir = -1;
//    private int page = 0;

    public TileEntityDustTable()
    {
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        tag.setInteger("page", page);
    }
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        page = tag.getInteger("page");
        pageFlipping = prevPageFlipping = floatd = (float)page / 2F;
    }

    public void updateEntity()
    {
//        if(dir == -1){
        dir = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
//        }
//        if(ticks%100 == 0) {
//            System.out.println("PAGE++");
//            page++;
//        }
        floatd = (float)page / 2F;
        super.updateEntity();
        prevFloating = floating;
        prevRotation = rotation;
        EntityPlayer entityplayer = worldObj.getClosestPlayer((float)xCoord + 0.5F, (float)yCoord + 0.5F, (float)zCoord + 0.5F, 3D);

        if (entityplayer != null)
        {
            floating += 0.1F;
        }
        else
        {
            floating -= 0.1F;
        }

//            double d = entityplayer.posX - (double)((float)xCoord + 0.5F);
//            double d1 = entityplayer.posZ - (double)((float)zCoord + 0.5F);
//            rotAmt = (float)Math.atan2(d1, d);
//            floating += 0.1F;
//            if (floating < 0.5F || rand.nextInt(40) == 0)
//            {
////                float f3 = floatd;
////                do
////                {
////                    floatd += rand.nextInt(4) - rand.nextInt(4);
////                }
////                while (f3 == floatd);
//            }
//        }
//        else
//        {
//            rotAmt += 0.02F;
//            floating -= 0.1F;
//        }
//        for (; rotation >= (float)Math.PI; rotation -= ((float)Math.PI * 2F)) { }
//        for (; rotation < -(float)Math.PI; rotation += ((float)Math.PI * 2F)) { }
//        for (; rotAmt >= (float)Math.PI; rotAmt -= ((float)Math.PI * 2F)) { }
//        for (; rotAmt < -(float)Math.PI; rotAmt += ((float)Math.PI * 2F)) { }
//        float f;
//        for (f = rotAmt - rotation; f >= (float)Math.PI; f -= ((float)Math.PI * 2F)) { }
//        for (; f < -(float)Math.PI; f += ((float)Math.PI * 2F)) { }
//        rotation += f * 0.4F;
        rotation = prevRotation = rotAmt = dir * ((float)Math.PI / 2); // (float)dir*(float)Math.PI/4;

        if (floating < 0.0F)
        {
            floating = 0.0F;
        }

        if (floating > 1.0F)
        {
            floating = 1.0F;
        }

        ticks++;
        prevPageFlipping = pageFlipping;
        float f1 = (floatd - pageFlipping) * 0.4F;
        float f2 = 0.2F;

        if (f1 < -f2)
        {
            f1 = -f2;
        }

        if (f1 > f2)
        {
            f1 = f2;
        }

        floate += (f1 - floate) * 0.9F;
        pageFlipping = pageFlipping + floate;
//        if(floatb - prevfloatb < 0.01) floatb = prevfloatb = 0;
//        floatb = (float)Math.PI;
//        String bstring = pageFlipping + "";
//        String dstring = floatd + "";
//        String estring = floate + "";
//        String fstring = floating + "";
//        String hstring = rotation + "";
//        String qstring = rotAmt + "";
//        bstring = bstring.substring(0,bstring.length() > 4 ? 4:bstring.length());
//        dstring = dstring.substring(0,dstring.length() > 4 ? 4:dstring.length());
//        estring = estring.substring(0,estring.length() > 4 ? 4:estring.length());
//        fstring = fstring.substring(0,fstring.length() > 4 ? 4:fstring.length());
//        hstring = hstring.substring(0,hstring.length() > 4 ? 4:hstring.length());
//        qstring = qstring.substring(0,qstring.length() > 4 ? 4:qstring.length());
//        System.out.println("B " + bstring + " \tD " + dstring + " \tE " + estring + " \tF " + fstring + " \tH " + hstring + " \tQ " + qstring + " " + dir);
    }

    @Override
    public Packet getDescriptionPacket()
    {
        return PacketHandler.getTELPacket(this);
    }
}
