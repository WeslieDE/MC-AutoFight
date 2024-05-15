package dev.clatza.mcautofight;

import dev.clatza.mcautofight.Controllers.KeyBindingController;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;

public class MCAutoFightClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		KeyBindingController.registerKeyBindings();
	}
}