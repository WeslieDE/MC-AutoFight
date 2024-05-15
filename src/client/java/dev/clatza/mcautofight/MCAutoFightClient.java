package dev.clatza.mcautofight;

import dev.clatza.mcautofight.Controllers.KeyBindingController;
import net.fabricmc.api.ClientModInitializer;

public class MCAutoFightClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		KeyBindingController.registerKeyBindings();
	}
}