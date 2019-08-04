package discord.bot.commands;

import discord.plugin.commands.CommandLink;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.User;
import osf.player.PlayerModel;

public class DiscordCommandLink implements DiscordCommand {
    @Override
    public void execute(MessageCreateEvent event) {
        if (!event.getMessage().getContent().isPresent())
            return;
        if (!event.getMessage().getChannel().block().getId().asString().equalsIgnoreCase("600012436115816486"))
            return;
        String[] args = event.getMessage().getContent().get().split(" ");
        if (args.length == 2) {
            String verificationNumber = args[1];
            if (CommandLink.verification.containsKey(verificationNumber)) {
                PlayerModel playerModel = CommandLink.verification.get(verificationNumber);
                User discordUser = event.getMessage().getAuthor().orElse(null);
                playerModel.getDatabaseRow().getCell("discord_snowflake").setValue(discordUser.getId());
                playerModel.pushRowToDatabase();
                CommandLink.verification.remove(verificationNumber);
                event.getMessage().getChannel().block().createMessage(":white_check_mark: You have linked your minecraft account (" + playerModel.getName() + ") to your discord account!");
            } else {
                event.getMessage().getChannel().block().createMessage(":x: You have entered an invalid verification code, please try again.");
            }
        } else {
            event.getMessage().getChannel().block().createMessage(":x: Type /link in game to receive a verification code and follow the directions to link your account");
        }
    }
}
