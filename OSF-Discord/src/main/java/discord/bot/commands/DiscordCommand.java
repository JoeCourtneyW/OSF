package discord.bot.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;

public interface DiscordCommand {
    void execute(MessageCreateEvent event);
}
