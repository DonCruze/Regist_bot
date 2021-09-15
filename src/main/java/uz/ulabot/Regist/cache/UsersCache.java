package uz.ulabot.Regist.cache;

import uz.ulabot.Regist.model.Users;

import java.util.HashMap;
import java.util.Map;

public class UsersCache {
    private static final Map<String, Users> usersMap = new HashMap<>();

    public static Map<String, Users> getUsersMap() {
        return usersMap;
    }
}
