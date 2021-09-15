package uz.ulabot.Regist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import uz.ulabot.Regist.controller.MainController;
import uz.ulabot.Regist.model.UsersRepo;

@SpringBootApplication
public class RegistApplication {

	private static UsersRepo usersRepo;

	public RegistApplication(UsersRepo usersRepo) {
		this.usersRepo = usersRepo;
	}

	public static void main(String[] args) throws TelegramApiException {
		SpringApplication.run(RegistApplication.class, args);

		TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
		telegramBotsApi.registerBot(new MainController(usersRepo));
	}
}
