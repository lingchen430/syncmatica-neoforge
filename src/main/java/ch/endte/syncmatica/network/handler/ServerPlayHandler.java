package ch.endte.syncmatica.network.handler;

import javax.annotation.Nonnull;
import ch.endte.syncmatica.network.actor.IServerPlay;
import ch.endte.syncmatica.network.SyncmaticaPacket;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

/**
 * Network packet senders / receivers (Server Context)
 */
public abstract class ServerPlayHandler
{
    public static void decodeSyncData(@Nonnull SyncmaticaPacket data, @Nonnull ServerGamePacketListenerImpl handler)
    {
        IServerPlay iDo = ((IServerPlay) handler);

        iDo.syncmatica$operateComms(sm -> sm.onPacket(iDo.syncmatica$getExchangeTarget(), data.getType(), data.getPacket()));
    }

    public static void decodeSyncData(@Nonnull SyncmaticaPacket data, @Nonnull IServerPlay iDo)
    {
        iDo.syncmatica$operateComms(sm -> sm.onPacket(iDo.syncmatica$getExchangeTarget(), data.getType(), data.getPacket()));
    }

    public static void encodeSyncData(@Nonnull SyncmaticaPacket data, @Nonnull ServerGamePacketListenerImpl handler)
    {
        sendSyncPacket(new SyncmaticaPacket.Payload(data), handler);
    }

    public static void encodeSyncData(@Nonnull SyncmaticaPacket data, @Nonnull ServerPlayer player)
    {
        sendSyncPacket(new SyncmaticaPacket.Payload(data), player);
    }

    public static void receiveSyncPayload(SyncmaticaPacket.Payload payload, IPayloadContext context)
    {
        decodeSyncData(payload.data(), ((ServerPlayer) context.player()).connection);
    }

    public static <T extends CustomPacketPayload> void sendSyncPacket(@Nonnull T payload, @Nonnull ServerPlayer player)
    {
        sendSyncPacket(payload, player.connection);
    }

    public static <T extends CustomPacketPayload> void sendSyncPacket(@Nonnull T payload, @Nonnull ServerGamePacketListenerImpl handler)
    {
        Packet<?> packet = new ClientboundCustomPayloadPacket(payload);

        if (handler.hasChannel(payload.type().id()))
        {
            handler.send(packet);
        }
        else if (handler.getConnectionType().isOther())
        {
            // Upstream Fabric clients register their receiver after the first
            // version packet. Preserve that discovery protocol for non-Neo peers.
            handler.getConnection().send(packet);
        }
    }
}
