package uz.ulabot.Regist.controller;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.ulabot.Regist.cache.UsersCache;
import uz.ulabot.Regist.model.Users;
import uz.ulabot.Regist.model.UsersRepo;
import uz.ulabot.Regist.service.SendEmail;

import java.security.SecureRandom;
import java.util.List;

public class MainController extends TelegramLongPollingBot {

    private static final String TOKEN = "1187840464:AAFWYDbrOS9YGYSKTUzZ2fHlJ4uFjbTDfjU";
    private static final String BOTNAME = "datot_bot";
    private static final String email = "ulusenderbot@gmail.com";
    private static final String emailPassword = "12345678Qq";
    private final UsersRepo usersRepo;

    public MainController(UsersRepo usersRepo) {
        this.usersRepo = usersRepo;

        List<Users> usersList = usersRepo.findAll();
        usersList.forEach(user ->{
            UsersCache.getUsersMap().put(user.getTgId(),user);
        });
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();

        Users users = UsersCache.getUsersMap().get(String.valueOf(message.getChatId()));

        if (users == null) {
            users = new Users();
            users.setTgId(String.valueOf(message.getChatId()));
            users.setStep("welcome");
            UsersCache.getUsersMap().put(String.valueOf(message.getChatId()), users);
        }

        switch (users.getStep()) {
            case "welcome":
                if (message.getText().equals("/start")) {
                    messageSender(message, "Введите свое имя");
                    users.setStep("setName");
                }
                break;
            case "setName":
                messageSender(message, "Введите свой email");
                UsersCache.getUsersMap().get(users.getTgId()).setUserName(message.getText());
                users.setStep("setEmail");
                break;
            case "setEmail":
                SecureRandom secureRandom = new SecureRandom();
                Integer confirmCode = Math.abs(secureRandom.nextInt());
                UsersCache.getUsersMap().get(users.getTgId()).setUserEmail(message.getText());
                UsersCache.getUsersMap().get(users.getTgId()).setCode(String.valueOf(confirmCode));


                if (SendEmail.sender(users, confirmCode, email, emailPassword)) {
                    messageSender(message, "Введите код подтверждения");
                    users.setStep("confirmEmailCode");
                } else {
                    messageSender(message, "Email не правильный!");
                }
                break;
            case "confirmEmailCode":
                if (users.getCode().equals(message.getText())) {
                    messageSender(message, "Отлично!");
                    users.setStep("success");
                    usersRepo.save(users);
                } else {
                    messageSender(message, "Не правильно!");
                }
                break;
            case "success":
                messageSender(message, "Вы зарегистрированы!");
                break;
        }
        UsersCache.getUsersMap().get(users.getTgId()).setStep(users.getStep());
    }

    public void messageSender(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        sendMessage.setText(text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return BOTNAME;
    }

    @Override
    public String getBotToken() {
        return TOKEN;
    }
}
