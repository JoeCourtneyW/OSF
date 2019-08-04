package discord.bot;

import discord.bot.commands.DiscordCommandApply;
import discord.bot.commands.DiscordCommand;
import discord.bot.commands.DiscordCommandLink;
import discord.bot.commands.DiscordCommandSupport;
import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.domain.message.MessageCreateEvent;

import java.util.HashMap;
import java.util.Map;

public class BotDaemon {

    private static DiscordClient client;
    public HashMap<String, DiscordCommand> commands;

    public BotDaemon(String secretToken) {
        registerCommands();
        client = new DiscordClientBuilder(secretToken).build();

        registerCommandListener();
        registerEventListeners();

        client.login().block();
    }

    private void registerCommandListener() {
        client.getEventDispatcher().on(MessageCreateEvent.class)
                .subscribe(event -> {
                    final String content = event.getMessage().getContent().orElse("");
                    if (content.startsWith("-")) {
                        for (final Map.Entry<String, DiscordCommand> entry : commands.entrySet()) {
                            // We will be using ! as our "prefix" to any command in the system.
                            if (content.toLowerCase().startsWith('-' + entry.getKey().toLowerCase())) {
                                entry.getValue().execute(event);
                                break;
                            }
                        }
                    }
                });
    }

    private void registerCommands() {
        commands = new HashMap<>();
        commands.put("apply", new DiscordCommandApply());
        commands.put("link", new DiscordCommandLink());
        commands.put("support", new DiscordCommandSupport());
    }

    private void registerEventListeners() {
        DiscordCommandApply.registerApplicationQuestionListener(client);
        DiscordCommandSupport.registerSupportListener(client);
    }

    public void login() {
        client.login().block();
    }

    public void logout() {
        client.logout();
    }

    public static DiscordClient getClient() {
        return client;
    }

    public static void main(String[] args) {
        new BotDaemon("NjAwNTQwNzE4NTQ4OTEwMDkw.XUGq8A.qNOiutR3YP9-feI4g7clzxpHKDY");
    }
}
