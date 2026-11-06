package ch.endte.syncmatica.network.handler;

import ch.endte.syncmatica.Reference;
import ch.endte.syncmatica.network.SyncmaticaPacket;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent;

@EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public final class ClientPayloadRegistration {
    @SubscribeEvent
    public static void register(RegisterClientPayloadHandlersEvent event) {
        event.register(SyncmaticaPacket.Payload.ID, (payload, context) ->
            ClientPlayHandler.receiveSyncPayload(payload.data()));
    }
}
