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

package net.wyvest.hytilities.handlers.chat.modules.modifiers;

import net.wyvest.hytilities.config.HytilitiesConfig;
import net.wyvest.hytilities.handlers.chat.ChatReceiveModule;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;

public class WhiteChat implements ChatReceiveModule {

    @Override
    public int getPriority() {
        return 5;
    }

    @Override
    public void onMessageReceived(@NotNull ClientChatReceivedEvent event) {
        final String message = event.message.getFormattedText();

        if (HytilitiesConfig.whiteChat) {
            final Matcher matcher = getLanguage().whiteChatNonMessageRegex.matcher(message);
            if (matcher.find(0)) {
                boolean foundStart = false;
                for (IChatComponent sibling : event.message.getSiblings()) {
                    if (sibling.getFormattedText().startsWith("§7: ")) foundStart = true;
                    if (foundStart && sibling.getChatStyle().getColor() == EnumChatFormatting.GRAY) {
                        sibling.getChatStyle().setColor(EnumChatFormatting.WHITE);
                    }
                }
            }
        }
    }
}
