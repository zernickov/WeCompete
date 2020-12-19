package com.example.wecompete.global;

import java.util.HashMap;
import java.util.Map;

/**
 * FORMÅL: at gemme et objekt til brug på tværs af klasser.
 * BRUG: i klasserne HomeActivity, CurrentGroupActivity, CurrentGroupMatchesActivity, CurrentGroupLeaderboardActivity.
 */
public class Global {
    public static Map<String, Object> map = new HashMap<>();
    public static final String GROUP_KEY = "group_key";
}
