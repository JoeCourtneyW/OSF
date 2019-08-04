package discord.bot.commands;

import discord4j.core.DiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.PrivateChannel;
import discord4j.core.object.entity.User;
import discord4j.core.object.util.Snowflake;
import osf.player.PlayerModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.UUID;

public class DiscordCommandApply implements DiscordCommand {


    public static HashMap<Snowflake, StaffApplication> application_in_progress = new HashMap<>();

    @Override
    public void execute(MessageCreateEvent event) {
        User user = event.getMessage().getAuthor().orElse(null);

        //If PlayerModel getBySnowflake returns null, tell user they must link their account first
        PlayerModel playerModel = PlayerModel.getPlayerModel(UUID.randomUUID());
        PrivateChannel privateChannel = user.getPrivateChannel().block();
        StaffApplication application = new StaffApplication(user, privateChannel);
        application_in_progress.put(user.getId(), application);

        privateChannel.createMessage("**OSF Staff Application**"
                + "\n" + "*Minecraft Username: " + playerModel.getName() + "*");
        privateChannel.createMessage("If you wish to change an answer, type -return");
        privateChannel.createMessage("If you wish to cancel your submission, type -cancel");
        privateChannel.createMessage(application.getNextQuestionFormatted());

    }

    public static void registerApplicationQuestionListener(DiscordClient client) {
        client.getEventDispatcher().on(MessageCreateEvent.class)
                .subscribe(event -> {
                    Snowflake user_snowflake = event.getMessage().getAuthor().orElse(null).getId();
                    if(!application_in_progress.containsKey(user_snowflake))
                        return;
                    StaffApplication application = application_in_progress.get(user_snowflake);

                    if(!event.getMessage().getChannelId().equals(application.getPrivateChannel().getId()))
                        return;

                    final String response = event.getMessage().getContent().orElse("");
                    if(response.equalsIgnoreCase("-return")) {
                        application.setCurrentQuestion(application.getCurrentQuestion()-1);
                        application.getPrivateChannel().createMessage(application.getNextQuestionFormatted());
                        return;
                    } else if(response.equalsIgnoreCase("-cancel")) {
                        application.getPrivateChannel().createMessage("Your submission has been cancelled, to start again, type -apply in the proper channel");
                        application_in_progress.remove(user_snowflake);
                        return;
                    }
                    application.answerCurrentQuestion(response);

                    if(!application.isDone()) {
                        application.getPrivateChannel().createMessage(application.getNextQuestionFormatted());
                        return;
                    }
                    application_in_progress.remove(user_snowflake);
                    application.getPrivateChannel().createMessage("You have completed the OSF Staff application and your response has been submitted for review. Please be patient while waiting for a response from the staff team");
                    application.getPrivateChannel().createMessage("Please do not submit multiple applications");
                    File applicationFile = application.saveAnswers();
                    client.getUserById(Snowflake.of("298541788761686028")).block().getPrivateChannel().block().createMessage("A staff application has been submitted by " + event.getMessage().getAuthor().orElse(null).getUsername());
                    client.getUserById(Snowflake.of("298541788761686028")).block().getPrivateChannel().block().createMessage(messageCreateSpec -> {
                        try {
                            messageCreateSpec.addFile(applicationFile.getName(), new FileInputStream(applicationFile));
                        } catch(FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    });

                });
    }
}
