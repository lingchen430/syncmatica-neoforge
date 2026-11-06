package ch.endte.syncmatica.network.handler;

import javax.annotation.Nonnull;
import ch.endte.syncmatica.network.SyncmaticaPacket;
import ch.endte.syncmatica.network.actor.ActorClientPlayHandler;


import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Network packet senders / receivers (Client Context)
 */
public class ClientPlayHandler
{
    public static void decodeSyncData(@Nonnull SyncmaticaPacket data, ClientPacketListener handler)
    {
        CallbackInfo ci = new CallbackInfo("receiveSyncPacket", false);
        ActorClientPlayHandler.getInstance().packetEvent(data.getType(), data.getPacket(), handler, ci);
    }

    public static void encodeSyncData(@Nonnull SyncmaticaPacket data, ClientPacketListener handler)
    {
        SyncmaticaPacket.Payload payload = new SyncmaticaPacket.Payload(data);
        if (handler != null)
        {
            sendSyncPacket(payload, handler);
        }
        else
        {
            sendSyncPacket(payload);
        }
    }

    public static void receiveSyncPayload(@Nonnull SyncmaticaPacket data)
    {
        CallbackInfo ci = new CallbackInfo("receiveSyncPacket", false);
        ActorClientPlayHandler.getInstance().packetEvent(data.getType(), data.getPacket(), Minecraft.getInstance().getConnection(), ci);
    }

    public static <T extends CustomPacketPayload> void sendSyncPacket(@Nonnull T payload)
    {
        if (Minecraft.getInstance().getConnection() != null && Minecraft.getInstance().getConnection().hasChannel(payload.type().id()))
        {
            Minecraft.getInstance().getConnection().send(new ServerboundCustomPayloadPacket(payload));
        }
    }

    public static <T extends CustomPacketPayload> void sendSyncPacket(@Nonnull T payload, @Nonnull ClientPacketListener handler)
    {
        Packet<?> packet = new ServerboundCustomPayloadPacket(payload);

        if (handler.hasChannel(payload.type().id()))
        {
            handler.send(packet);
        }
    }
}
