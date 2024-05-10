package dev.clatza.mcautofight;

import net.fabricmc.api.ClientModInitializer;

public class MCAutoFightClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ViewHelper.registerGameEvents();
		MovementHelper.registerGameEvents();
		KeyBindingHelper.registerGameEvents();
		TeleportMonitor.registerGameEvents();
	}
}