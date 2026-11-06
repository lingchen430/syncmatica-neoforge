package ch.endte.syncmatica.mixin;

import ch.endte.syncmatica.Syncmatica;
import ch.endte.syncmatica.network.actor.IServerPlay;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public class MixinPlayerManager {
    @Inject(method = "placeNewPlayer", at = @At("TAIL"))
    private void syncmatica$join(Connection connection, ServerPlayer player,
                                 CommonListenerCookie cookie, CallbackInfo ci) {
        // Fabric Syncmatica advertises its receiver only after our version probe.
        // Vanilla peers safely discard this optional custom payload, as upstream does.
        if (player.connection.hasChannel(Syncmatica.NETWORK_ID)
                || player.connection.getConnectionType().isOther()) {
            IServerPlay actor = (IServerPlay) player.connection;
            actor.syncmatica$operateComms(manager ->
                manager.onPlayerJoin(actor.syncmatica$getExchangeTarget(), player));
        }
    }
}
