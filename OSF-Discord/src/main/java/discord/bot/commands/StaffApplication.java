package discord.bot.commands;

import discord.plugin.OSFDiscord;
import discord4j.core.object.entity.PrivateChannel;
import discord4j.core.object.entity.User;
import osf.player.PlayerModel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class StaffApplication {
    private static final String[] QUESTIONS = {"What is your name?",
            "How old are you?",
            "What timezone are you located in?",
            "Do you have any prior staff experience? If so, explain.",
            "Why do you want to be a staff member on OSF?",
            "How often can you be online?",
            "Do you have the ability to record video, audio, and take screenshots?",
            "The OSF team is currently looking for Helpers, Moderators, and Admins. What position are you applying for? **(This does not guarantee you will receive the position you apply for)*"};

    private User user;
    private PlayerModel minecraftPlayerModel;
    private PrivateChannel privateChannel;
    private int currentQuestion = -1;
    private String[] answers = new String[QUESTIONS.length];

    public StaffApplication(User user, PrivateChannel privateChannel) {
        this.user = user;
        this.minecraftPlayerModel = PlayerModel.getPlayerModelByDiscordSnowflake(user.getId().toString());
        this.privateChannel = user.getPrivateChannel().block();
    }

    public User getUser() {
        return user;
    }

    public PlayerModel getMinecraftPlayerModel() {
        return minecraftPlayerModel;
    }

    public PrivateChannel getPrivateChannel() {
        return privateChannel;
    }

    public String getNextQuestionFormatted() {
        currentQuestion++;
        return "`" + (currentQuestion+1) + ") " + QUESTIONS[currentQuestion] + "`";
    }

    public void answerCurrentQuestion(String answer) {
        answers[currentQuestion] = answer;
    }

    public void setCurrentQuestion(int currentQuestion) {
        this.currentQuestion = currentQuestion;
    }

    public int getCurrentQuestion() {
        return this.currentQuestion;
    }

    public boolean isDone() {
        return currentQuestion >= QUESTIONS.length-1;
    }

    public File saveAnswers() {
        File savedFile = new File(OSFDiscord.getInstance().getDataFolder() + File.separator + "applications" + File.separator + minecraftPlayerModel.getName() + ".txt");
        try {
            savedFile.createNewFile();
            PrintWriter writer = new PrintWriter(new FileWriter(savedFile, false));

            writer.println("Minecraft Username: " + minecraftPlayerModel.getName());
            writer.println("Discord Username: " + user.getUsername() + "#" + user.getDiscriminator());
            writer.println("Date submitted" + new Date().toString());
            writer.println();

            for(int i = 0; i < answers.length; i++) {
                writer.println( "Q) " + QUESTIONS[i] );
                writer.println( "A) " + answers[i]);
                writer.println();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return savedFile;
    }

}
