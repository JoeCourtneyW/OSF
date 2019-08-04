package discord.bot.commands;

import discord4j.core.DiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.PrivateChannel;
import discord4j.core.object.entity.Role;
import discord4j.core.object.entity.TextChannel;
import discord4j.core.object.util.Snowflake;

import java.util.ArrayList;

public class DiscordCommandSupport implements DiscordCommand {

    public static ArrayList<Snowflake> requires_support = new ArrayList<>();

    @Override
    public void execute(MessageCreateEvent event) {
        PrivateChannel privateChannel = event.getMember().orElse(null).getPrivateChannel().block();
        privateChannel.createMessage("What do you need help with? Please explain your issue below and a staff member will be with you as soon as possible.");
        privateChannel.createMessage("Make sure you are connected to the \"Support Waiting Room\" channel");
        requires_support.add(event.getMessage().getAuthor().orElse(null).getId());
    }

    public static void registerSupportListener(DiscordClient client) {
        client.getEventDispatcher().on(MessageCreateEvent.class)
                .subscribe(event -> {
                    Snowflake user_snowflake = event.getMessage().getAuthor().orElse(null).getId();
                    if(!requires_support.contains(user_snowflake))
                        return;
                    TextChannel supportRequestChannel = (TextChannel) event.getGuild().block().getChannelById(Snowflake.of("606878090877992973")).block();
                    Role staffRole = event.getGuild().block().getRoleById(Snowflake.of("606878470135611402")).block();
                    supportRequestChannel.createMessage(staffRole.getMention() + "(Support Request: "
                            + event.getMessage().getAuthor().orElse(null).getMention()
                            + ") " + event.getMessage().getContent().orElse(""));
                    requires_support.remove(user_snowflake);
                });
    }
}
