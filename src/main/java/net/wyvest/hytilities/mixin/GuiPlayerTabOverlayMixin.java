/*
 * Hytilities Reborn - Hypixel focused Quality of Life mod.
 * Copyright (C) 2021  W-OVERFLOW
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.wyvest.hytilities.mixin;

import net.wyvest.hytilities.handlers.lobby.npc.NPCHider;
import net.wyvest.hytilities.handlers.lobby.tab.TabChanger;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(GuiPlayerTabOverlay.class)
public class GuiPlayerTabOverlayMixin {
    @Redirect(method = "renderPlayerlist", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/NetHandlerPlayClient;getPlayerInfoMap()Ljava/util/Collection;"))
    private Collection<NetworkPlayerInfo> hideNPCs(NetHandlerPlayClient instance) {
        return NPCHider.hideTabNpcs(instance.getPlayerInfoMap());
    }

    @Inject(method = "drawPing", at = @At("HEAD"), cancellable = true)
    private void checkPlayer(int p_175245_1_, int p_175245_2_, int p_175245_3_, NetworkPlayerInfo networkPlayerInfoIn, CallbackInfo ci) {
        if (TabChanger.hidePing(networkPlayerInfoIn)) ci.cancel();
    }
}
