package ch.endte.syncmatica;

import ch.endte.syncmatica.network.SyncmaticaPacket;
import ch.endte.syncmatica.network.handler.ServerPlayHandler;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

@Mod(Reference.MOD_ID)
public final class ModInit {
    public ModInit(IEventBus bus) {
        net.neoforged.neoforge.common.NeoForge.EVENT_BUS.addListener(
            net.neoforged.neoforge.server.permission.events.PermissionGatherEvent.Nodes.class,
            ch.endte.syncmatica.command.PermsWrap::register);
        bus.addListener(RegisterPayloadHandlersEvent.class, event -> {
            event.registrar("1").optional().playBidirectional(
                SyncmaticaPacket.Payload.ID, SyncmaticaPacket.Payload.CODEC,
                (payload, context) -> ServerPlayHandler.receiveSyncPayload(payload, context));
        });
    }
}
