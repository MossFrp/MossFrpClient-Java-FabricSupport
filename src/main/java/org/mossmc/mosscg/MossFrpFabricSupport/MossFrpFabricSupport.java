package org.mossmc.mosscg.MossFrpFabricSupport;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.argument.MessageArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.server.command.CommandManager.argument;

import java.lang.reflect.Method;

@SuppressWarnings("unused")
public class MossFrpFabricSupport implements ModInitializer {
	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) ->
				dispatcher.register(literal("mossfrp")
						.requires((serverCommandSource) -> serverCommandSource.hasPermissionLevel(2))
						.then(argument("sub",MessageArgumentType.message())
						.executes((MossFrpFabricSupport::commandRun)))));
	}

	@SuppressWarnings("unused")
	public static void sendPlayer(String info) {
		if (MinecraftClient.getInstance().player != null) {
			MinecraftClient.getInstance().player.sendMessage(Text.of(info.replaceAll("\r\n","\n")), false);
		}
	}

	@SuppressWarnings("deprecation")
	public static int commandRun(CommandContext<ServerCommandSource> context) {
		try {
			Class<?> commandClass = Class.forName("org.mossmc.mosscg.MossFrp.Command.CommandFabric");
			String[] cut = context.getInput().replace("/mossfrp ","").split("\\s+");
			Method method = commandClass.getDeclaredMethod("commandFabric", boolean.class, String[].class);
			method.invoke(commandClass.newInstance(),false,cut);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}
