package ch.endte.syncmatica.command;

import java.util.function.Predicate;
import java.util.Map;
import net.neoforged.neoforge.server.permission.PermissionAPI;
import net.neoforged.neoforge.server.permission.events.PermissionGatherEvent;
import net.neoforged.neoforge.server.permission.nodes.PermissionNode;
import net.neoforged.neoforge.server.permission.nodes.PermissionTypes;
import javax.annotation.Nonnull;


import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.permissions.PermissionLevel;
import net.minecraft.util.Mth;

public class PermsWrap
{
	private static final Map<String, PermissionNode<Boolean>> NODES = Map.of(
		"syncmatica.command", node("command"),
		"syncmatica.command.load", node("command.load"),
		"syncmatica.command.load_each", node("command.load_each"));

	private static PermissionNode<Boolean> node(String name) {
		// Matches upstream's default PermissionLevel.ALL.
		return new PermissionNode<>("syncmatica", name, PermissionTypes.BOOLEAN,
			(player, uuid, context) -> true);
	}

	public static void register(PermissionGatherEvent.Nodes event) {
		NODES.values().forEach(event::addNodes);
	}
	public static Predicate<CommandSourceStack> check(@Nonnull String name, PermissionLevel level)
	{
		return source -> {
			var node = NODES.get(name);
			if (source.getEntity() instanceof net.minecraft.server.level.ServerPlayer player
					&& node != null && PermissionAPI.getActivePermissionHandler() != null) {
				return PermissionAPI.getPermission(player, node);
			}
			return source.permissions().hasPermission(new net.minecraft.server.permissions.Permission.HasCommandLevel(level));
		};
	}

	public static Predicate<CommandSourceStack> check(@Nonnull String node, int level)
	{
		return check(node, PermissionLevel.byId(Mth.clamp(level, 0, PermissionLevel.OWNERS.id())));
	}
}
