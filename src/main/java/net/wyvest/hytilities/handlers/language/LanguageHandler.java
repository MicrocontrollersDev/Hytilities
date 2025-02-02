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

package net.wyvest.hytilities.handlers.language;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gg.essential.api.utils.JsonHolder;
import gg.essential.api.utils.Multithreading;
import gg.essential.api.utils.WebUtil;
import net.minecraft.client.Minecraft;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Automatically switches the player's language based on their current
 * Hypixel language. It does so through the Sk1er API which caches the result
 * for a select period of time.
 *
 * @author Koding
 */
public class LanguageHandler {

    private final Gson gson = new GsonBuilder().create();
    private final LanguageData fallback = readData("en");
    private final Map<String, String> languageMappings = new HashMap<String, String>() {{
        put("ENGLISH", "en");
        put("FRENCH", "fr");
    }};

    private LanguageData current = fallback;

    public LanguageHandler() {
        Multithreading.runAsync(this::initialize);
    }

    private void initialize() {
        final String username = Minecraft.getMinecraft().getSession().getUsername();
        final JsonHolder json = WebUtil.fetchJSON("https://api.sk1er.club/player/" + username);
        final String language = json.optJSONObject("player").defaultOptString("userLanguage", "ENGLISH");
        current = loadData(languageMappings.getOrDefault(language, "en"));
    }

    private LanguageData loadData(String language) {
        final LanguageData data = readData(language);
        return data == null ? fallback : data;
    }

    private LanguageData readData(String language) {
        try (InputStream stream = LanguageHandler.class.getResourceAsStream("/languages/" + language + ".json")) {
            if (stream == null) return null;
            final LanguageData data = gson.fromJson(new InputStreamReader(stream), LanguageData.class);
            data.initialize();
            return data;
        } catch (IOException e) {
            return null;
        }
    }

    public LanguageData getCurrent() {
        return current;
    }
}
