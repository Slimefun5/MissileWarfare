package me.kaiyan.missilewarfare.items;

import io.github.thebusybiscuit.slimefun5.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun5.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun5.core.handlers.ItemDropHandler;
import io.github.thebusybiscuit.slimefun5.core.handlers.ItemUseHandler;
import me.kaiyan.missilewarfare.MissileWarfare;
import me.kaiyan.missilewarfare.util.PlayerID;
import me.kaiyan.missilewarfare.util.Messages;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * A Slimefun item that manages a named list of players, used for
 * missile launcher targeting permissions via the Conversation API.
 *
 * @author MissileWarfare contributors
 */
public class PlayerList extends SlimefunItem {

    /**
     * Creates a new player list item with use and drop handlers.
     *
     * @param itemGroup  the item group this item belongs to
     * @param item       the Slimefun item stack
     * @param recipeType the recipe type
     * @param recipe     the crafting recipe
     */
    public PlayerList(@Nonnull ItemGroup itemGroup, @Nonnull SlimefunItemStack item,
                      @Nonnull RecipeType recipeType, @Nonnull ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);

        ItemUseHandler itemUseHandler = this::itemUse;
        addItemHandler(itemUseHandler);

        ItemDropHandler itemDropHandler = this::itemDrop;
        addItemHandler(itemDropHandler);
    }

    private boolean itemDrop(PlayerDropItemEvent event, Player player, Item item) {
        if (SlimefunItem.getByItem(item.getItemStack()) == null) {
            return true;
        }
        if (!player.isSneaking() && SlimefunItem.getByItem(item.getItemStack()).getId().equals("PLAYERLIST")) {
            event.setCancelled(true);

            NamespacedKey key = new NamespacedKey(MissileWarfare.getInstance(), "id");
            ItemMeta meta = item.getItemStack().getItemMeta();
            PersistentDataContainer cont = meta.getPersistentDataContainer();

            cont.remove(key);
            Messages.send(player, "playerlist.reset-id", "Reset Key");
            return false;
        }
        return true;
    }

    private void itemUse(PlayerRightClickEvent event) {
        NamespacedKey key = new NamespacedKey(MissileWarfare.getInstance(), "id");
        ItemMeta meta = event.getItem().getItemMeta();
        PersistentDataContainer cont = meta.getPersistentDataContainer();

        try {
            if (event.getSlimefunBlock().isPresent()) {
                if (event.getSlimefunBlock().get().getId().equals("ANTIELYTRALAUNCHER")) {
                    Messages.send(event.getPlayer(), "playerlist.added-id", "Added ID ",
                            line -> line + cont.get(key, PersistentDataType.STRING));
                    return;
                }
            }
        } catch (NullPointerException e) {
            Messages.send(event.getPlayer(), "playerlist.no-id",
                    "Couldn't add ID to the launcher, No ID assigned to this PlayerList");
            return;
        }
        if (!event.getSlimefunBlock().isPresent()) {
            Prompt askToWrite = new StringPrompt() {
                @Override
                public String getPromptText(ConversationContext conversationContext) {
                    return Messages.get(Messages.viewer(conversationContext.getForWhom()),
                            "playerlist.ask-add", "What player would you like to add? Say exit to exit");
                }

                @Override
                public Prompt acceptInput(ConversationContext conversationContext, String s) {
                    try {
                        List<OfflinePlayer> players = PlayerID.players.get(cont.get(key, PersistentDataType.STRING));
                        players.add(MissileWarfare.getInstance().getServer().getPlayerExact(s));
                        PlayerID.players.put(cont.get(key, PersistentDataType.STRING), players);
                        Messages.sendRaw(conversationContext.getForWhom(), "playerlist.added-player", "Added player ",
                                line -> line + MissileWarfare.getInstance().getServer().getPlayerExact(s));
                        return END_OF_CONVERSATION;
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        Messages.sendRaw(conversationContext.getForWhom(), "playerlist.invalid-player", "INVALID PLAYER");
                        return END_OF_CONVERSATION;
                    }
                }
            };
            Prompt askToRemove = new StringPrompt() {
                @Override
                public String getPromptText(ConversationContext conversationContext) {
                    return Messages.get(Messages.viewer(conversationContext.getForWhom()),
                            "playerlist.ask-remove", "What player would you like to remove?");
                }

                @Override
                public Prompt acceptInput(ConversationContext conversationContext, String s) {
                    try {
                        List<OfflinePlayer> players = PlayerID.players.get(cont.get(key, PersistentDataType.STRING));
                        players.remove(MissileWarfare.getInstance().getServer().getPlayerExact(s));
                        PlayerID.players.put(cont.get(key, PersistentDataType.STRING), players);
                        Messages.sendRaw(conversationContext.getForWhom(), "playerlist.removed-player", "Removed Player ",
                                line -> line + MissileWarfare.getInstance().getServer().getPlayerExact(s));
                        return END_OF_CONVERSATION;
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        Messages.sendRaw(conversationContext.getForWhom(), "playerlist.ask-remove", "What player would you like to remove?");
                        return END_OF_CONVERSATION;
                    }
                }
            };

            Prompt askToRead = new StringPrompt() {
                @Override
                public String getPromptText(ConversationContext conversationContext) {
                    Player viewer = Messages.viewer(conversationContext.getForWhom());
                    return Messages.get(viewer, "playerlist.ask-action",
                            "Say '%read%' to see the players associated with this ID, Say '%add%' to add a player, Say '%remove%' to remove a player, Say 'exit' to exit")
                            .replace("%read%", Messages.get(viewer, "playerlist.input-read", "read"))
                            .replace("%add%", Messages.get(viewer, "playerlist.input-add", "add"))
                            .replace("%remove%", Messages.get(viewer, "playerlist.input-remove", "remove"));
                }

                @Override
                public Prompt acceptInput(ConversationContext conversationContext, String s) {
                    Player viewer = Messages.viewer(conversationContext.getForWhom());
                    if (Messages.matchesInput(viewer, s, "playerlist.input-read", "read")) {
                        String out = Messages.get(viewer, "playerlist.players", "Players: ");
                        List<String> players = new ArrayList<>();
                        for (OfflinePlayer player : PlayerID.players.get(cont.get(key, PersistentDataType.STRING))) {
                            players.add(player.getName());
                        }
                        out += players;
                        conversationContext.getForWhom().sendRawMessage(out);
                        return END_OF_CONVERSATION;
                    } else if (Messages.matchesInput(viewer, s, "playerlist.input-add", "add")) {
                        return askToWrite;
                    } else if (Messages.matchesInput(viewer, s, "playerlist.input-remove", "remove")) {
                        return askToRemove;
                    }
                    Messages.sendRaw(conversationContext.getForWhom(), "playerlist.incorrect-input", "Incorrect Input");
                    return END_OF_CONVERSATION;
                }
            };

            Prompt askForID = new StringPrompt() {
                @Override
                public String getPromptText(ConversationContext conversationContext) {
                    return Messages.get(Messages.viewer(conversationContext.getForWhom()),
                            "playerlist.ask-id", "Input ID to create, say exit to leave");
                }

                @Override
                public Prompt acceptInput(ConversationContext conversationContext, String s) {
                    if (cont.get(key, PersistentDataType.STRING) != null) {
                        return askToRead;
                    }
                    if (PlayerID.players.get(s) == null) {
                        PlayerID.players.put(s, new ArrayList<>());
                        Messages.sendRaw(conversationContext.getForWhom(), "playerlist.created-id",
                                "Created ID : {id}, You can now write to it", line -> line.replace("{id}", s));
                        cont.set(key, PersistentDataType.STRING, s);
                        event.getItem().setItemMeta(meta);
                        return END_OF_CONVERSATION;
                    } else {
                        Messages.sendRaw(conversationContext.getForWhom(), "playerlist.got-id", "Gotten ID");
                        cont.set(key, PersistentDataType.STRING, s);
                        List<String> lore = meta.getLore() != null ? meta.getLore() : new ArrayList<>();
                        lore.add("ID: " + s);
                        event.getItem().setItemMeta(meta);
                        return askToRead;
                    }
                }
            };

            if (cont.get(key, PersistentDataType.STRING) == null) {
                ConversationFactory cf = new ConversationFactory(MissileWarfare.getInstance());
                Conversation conversation = cf.withFirstPrompt(askForID)
                        .withLocalEcho(false)
                        .withEscapeSequence("exit")
                        .withTimeout(20)
                        .buildConversation(event.getPlayer());
                conversation.begin();
                event.cancel();
            } else {
                ConversationFactory cf = new ConversationFactory(MissileWarfare.getInstance());
                Conversation conversation = cf.withFirstPrompt(askToRead)
                        .withLocalEcho(false)
                        .withEscapeSequence("exit")
                        .withTimeout(20)
                        .buildConversation(event.getPlayer());
                conversation.begin();
                event.cancel();
            }
        }
    }
}

