package me.kaiyan.missilewarfare.util;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.conversations.Conversable;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import me.kaiyan.missilewarfare.MissileWarfare;

/**
 * Resolves this addon's chat text for one viewer, from {@code languages/<lang>/messages.yml}.
 * <p>
 * This addon already had a language pack ({@link Translations}), but a pack is chosen once in
 * {@code config.yml} and so reads the same for everybody on the server. These keys resolve per
 * viewer instead, and the pack stays authoritative wherever an operator edited it.
 */
public final class Messages {

    private Messages() {}

    /**
     * The shipped English pack, kept only to tell an operator's edited line from an untouched one.
     */
    private static YamlConfiguration shippedEnglishPack;

    /**
     * Maps a {@code messages.yml} key to the language-pack key it took over from.
     *
     * @implNote Only keys that existed in the pack appear here; anything absent is new to the
     *           per-viewer file and has no operator wording to respect. The pack's {@code {token}}
     *           placeholder spelling is deliberately kept in {@code messages.yml} so an operator's
     *           edited line substitutes without being rewritten.
     */
    private static final Map<String, String> PACK_KEYS = packKeys(
            "elytra.locking", "messages.elytraattack.locking",
            "elytra.locked", "messages.elytraattack.locked",
            "launcher.antiair-created", "messages.launchers.createantiair.success",
            "launcher.antiair-failure", "messages.launchers.createantiair.failure",
            "launcher.antielytra-created", "messages.launchers.createantielytra.success",
            "launcher.antielytra-failure", "messages.launchers.createantielytra.failure",
            "launcher.ground-created", "messages.launchers.ground.create.success",
            "launcher.ground-failure", "messages.launchers.ground.create.failure",
            "launcher.ground-coords", "messages.launchers.ground.coords",
            "launcher.cruise-altitude", "messages.launchers.ground.setting.cruisealt",
            "launcher.not-a-coordinate", "messages.launchers.ground.setting.notacoord",
            "launcher.not-a-number", "messages.launchers.ground.setting.notanumber",
            "manpad.locking", "messages.manpad.locking",
            "manpad.item-changed", "messages.manpad.itemchange",
            "manpad.no-target", "messages.manpad.notarget",
            "playerlist.reset-id", "messages.playerlist.resetkey",
            "playerlist.added-id", "messages.playerlist.addedkey",
            "playerlist.no-id", "messages.playerlist.noid",
            "playerlist.ask-add", "messages.playerlist.askwriteprompt",
            "playerlist.added-player", "messages.playerlist.addedplayer",
            "playerlist.invalid-player", "messages.playerlist.invalidplayer",
            "playerlist.ask-remove", "messages.playerlist.removeplayer",
            "playerlist.removed-player", "messages.playerlist.removedplayer",
            "playerlist.ask-action", "messages.playerlist.asktoread",
            "playerlist.input-read", "messages.playerlist.inputs.read",
            "playerlist.input-add", "messages.playerlist.inputs.add",
            "playerlist.input-remove", "messages.playerlist.inputs.remove",
            "playerlist.incorrect-input", "messages.playerlist.incorrectinput",
            "playerlist.ask-id", "messages.playerlist.askinputid",
            "playerlist.created-id", "messages.playerlist.createdid",
            "playerlist.got-id", "messages.playerlist.gottenid",
            "radar.usage", "messages.radar");

    @Nonnull
    private static Map<String, String> packKeys(@Nonnull String... pairs) {
        Map<String, String> keys = new HashMap<>();
        for (int i = 0; i < pairs.length; i += 2) {
            keys.put(pairs[i], pairs[i + 1]);
        }
        return Collections.unmodifiableMap(keys);
    }

    public static void setup() {
        try (InputStream shipped = MissileWarfare.getInstance().getResource("pack-EN.yml")) {
            if (shipped != null) {
                shippedEnglishPack = YamlConfiguration.loadConfiguration(
                        new InputStreamReader(shipped, StandardCharsets.UTF_8));
            }
        } catch (Exception e) {
            MissileWarfare.getInstance().getLogger().warning("Could not read the shipped English pack: " + e);
        }
    }

    /**
     * This addon's chat text in the viewer's own language.
     *
     * @implNote An operator who edited the line in their own language pack keeps their wording
     *           verbatim: the pack is copied into the data folder and documented as editable, so a
     *           translation may only stand in for a line nobody replaced. Without the shipped pack
     *           to compare against, every pack value counts as untouched rather than as an edit.
     */
    @Nonnull
    public static String get(@Nullable Player viewer, @Nonnull String key, @Nonnull String english) {
        String packKey = PACK_KEYS.get(key);

        if (packKey != null && shippedEnglishPack != null && Translations.pack != null) {
            String operator = Translations.pack.getString(packKey);
            if (operator != null && !operator.equals(shippedEnglishPack.getString(packKey))) {
                return operator;
            }
        }

        return Slimefun.getAddonMessageService().getMessage(viewer, MissileWarfare.getInstance(), key, english);
    }

    public static void send(@Nonnull Player recipient, @Nonnull String key, @Nonnull String english) {
        recipient.sendMessage(get(recipient, key, english));
    }

    public static void send(@Nonnull Player recipient, @Nonnull String key, @Nonnull String english,
                            @Nonnull UnaryOperator<String> placeholders) {
        recipient.sendMessage(placeholders.apply(get(recipient, key, english)));
    }

    /**
     * Sends a conversation prompt's reply, which goes out raw because Bukkit's conversation API owns
     * the formatting.
     */
    public static void sendRaw(@Nonnull Conversable whom, @Nonnull String key, @Nonnull String english) {
        whom.sendRawMessage(get(viewer(whom), key, english));
    }

    public static void sendRaw(@Nonnull Conversable whom, @Nonnull String key, @Nonnull String english,
                               @Nonnull UnaryOperator<String> placeholders) {
        whom.sendRawMessage(placeholders.apply(get(viewer(whom), key, english)));
    }

    /**
     * The conversation partner as a viewer, or {@code null} when whoever is at the prompt is not a
     * player and so has no language of their own.
     */
    @Nullable
    public static Player viewer(@Nonnull Conversable whom) {
        return whom instanceof Player ? (Player) whom : null;
    }

    /**
     * Whether the player typed the given input word, in their own language or in English.
     *
     * @implNote The English word stays accepted alongside the translation, so a server that switched
     *           language keeps working for players and macros that type the original. Case is ignored,
     *           which the {@code equals} comparison this replaced did not do.
     */
    public static boolean matchesInput(@Nullable Player viewer, @Nonnull String typed,
                                       @Nonnull String key, @Nonnull String english) {
        return typed.equalsIgnoreCase(english) || typed.equalsIgnoreCase(get(viewer, key, english));
    }
}
