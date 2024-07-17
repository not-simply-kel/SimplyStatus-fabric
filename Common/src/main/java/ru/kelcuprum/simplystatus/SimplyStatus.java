package ru.kelcuprum.simplystatus;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.IPCListener;
import com.jagrosh.discordipc.entities.Packet;
import com.jagrosh.discordipc.entities.RichPresence;
import com.jagrosh.discordipc.entities.User;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.kelcuprum.alinlib.AlinLib;
import ru.kelcuprum.alinlib.api.events.client.ClientLifecycleEvents;
import ru.kelcuprum.alinlib.config.Config;
import ru.kelcuprum.alinlib.config.Localization;
import ru.kelcuprum.simplystatus.config.Assets;
import ru.kelcuprum.simplystatus.config.ModConfig;
import ru.kelcuprum.simplystatus.info.Client;
import ru.kelcuprum.simplystatus.info.SimplyPlayer;
import ru.kelcuprum.simplystatus.localization.StarScript;
import ru.kelcuprum.simplystatus.mods.WaterPlayerSupport;
import ru.kelcuprum.simplystatus.mods.Voice;
import ru.kelcuprum.simplystatus.presence.singleplayer.Loading;
import ru.kelcuprum.simplystatus.presence.LoadingGame;
import ru.kelcuprum.simplystatus.presence.MainMenu;
import ru.kelcuprum.simplystatus.presence.ReplayMod;
import ru.kelcuprum.simplystatus.presence.Unknown;
import ru.kelcuprum.simplystatus.presence.multiplayer.Connect;
import ru.kelcuprum.simplystatus.presence.multiplayer.Disconnect;
import ru.kelcuprum.simplystatus.presence.multiplayer.MultiPlayer;
import ru.kelcuprum.simplystatus.presence.singleplayer.SinglePlayer;

import java.text.DecimalFormat;
import java.util.*;

import static ru.kelcuprum.alinlib.WebAPI.getJsonArray;

public class SimplyStatus {
    private static final Timer TIMER = new Timer();

    // Mod Statud build
    public static String[] thanks = {};
    // User Configurations
    public static Config userConfig = new Config("config/SimplyStatus/config.json");
    public static Config serverConfig = new Config("config/SimplyStatus/servers/default.json");
    public static Localization localization = new Localization("simplystatus.presence", "config/SimplyStatus/lang");
    public static HashMap<String, Assets> assets = new HashMap<>();
    public static HashMap<String, Assets> modAssets = new HashMap<>();
    public static ArrayList<String> assetsNames = new ArrayList<>();
    // Another shit
    public static double isPEnable = Math.random();
    public static String[] apiNames = {
            "CraftHead",
            "Alina API: 2D",
            "Alina API: 3D",
            "Discord"
    };
    public static boolean useAnotherID = false;
    public static boolean useCustomID = false;
    public static String customID = "";
    public static DecimalFormat DF = new DecimalFormat("#.##");
    private static String lastException;
    public static Long TIME_STARTED_CLIENT;
    // Logs
    public static final Logger LOG = LogManager.getLogger("SimplyStatus");

    public static void log(String message) {
        log(message, Level.INFO);
    }

    public static void log(String message, Level level) {
        LOG.log(level, "[" + LOG.getName() + "] " + message);
    }

    // Mods is present
    public static Boolean replayMod = false;
    public static Boolean waterPlayer = false;
    public static Boolean svc = false;
    public static Boolean plasmo = false;
    public static Boolean isVoiceModsEnable = false;
    public static Boolean isMusicModsEnable = false;
    // Discord
    public static RichPresence lastPresence;
    public static IPCClient client;
    public static User USER;
    public static String APPLICATION_ID;
    public static boolean CONNECTED_DISCORD = false;

    public static boolean GAME_STARTED = false;

    public static void onInitializeClient() {
        userConfig.load();
        serverConfig.load();
        localization.setParser((s) -> StarScript.run(StarScript.compile(s)));
        try {
            ModConfig.load();
        } catch (Exception e) {
            log("The default configuration of the mod was not loaded, no launch possible!", Level.ERROR);
            log(e.getLocalizedMessage(), Level.ERROR);
            return;
        }
        Assets.loadFiles();
        useAnotherID = userConfig.getBoolean("USE_ANOTHER_ID", false);
        useCustomID = userConfig.getBoolean("USE_CUSTOM_APP_ID", false);
        TIME_STARTED_CLIENT = System.currentTimeMillis() / 1000;
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> SimplyStatus.startClient());
        ClientLifecycleEvents.CLIENT_FULL_STARTED.register(client -> SimplyStatus.GAME_STARTED = true);
        ClientLifecycleEvents.CLIENT_STOPPING.register(client1 -> SimplyStatus.stopClient());
        StarScript.init();
        registerApplications();
    }

    // -=-=-=-=-=-=-=-=-
    public static void startClient() {
        log(String.format("Client %s is up and running!", AlinLib.MINECRAFT.getLaunchedVersion()));
        try {
            JsonArray data = getJsonArray("https://api.kelcuprum.ru/boosty/thanks");
            thanks = ModConfig.jsonArrayToStringArray(data);
        } catch (Exception e) {
            log(e.getLocalizedMessage(), Level.ERROR);
        }
    }

    public static void stopClient() {
        log("Client stopped");
        client.close();
    }
    // -=-=-=-=-=-=-=-=-

    private static void registerApplications() {
        APPLICATION_ID = userConfig.getBoolean("USE_ANOTHER_ID", false) ? ModConfig.mineID : ModConfig.baseID;
        if (userConfig.getBoolean("USE_CUSTOM_APP_ID", false) && !userConfig.getString("CUSTOM_APP_ID", ModConfig.baseID).isBlank())
            APPLICATION_ID = userConfig.getString("CUSTOM_APP_ID", ModConfig.baseID);
        customID = APPLICATION_ID;
        if(userConfig.getBoolean("SHOW_RPC", true)) {
            client = new IPCClient(Long.parseLong(APPLICATION_ID));
            setupListener();
            try {
                client.connect();
            } catch (Exception ex) {
                log(ex.getLocalizedMessage(), Level.ERROR);
            }
        }
        start();
    }

    public static void setupListener() {
        client.setListener(new IPCListener() {
            @Override
            public void onPacketSent(IPCClient ipcClient, Packet packet) {

            }

            @Override
            public void onPacketReceived(IPCClient ipcClient, Packet packet) {

            }

            @Override
            public void onActivityJoin(IPCClient ipcClient, String s) {

            }

            @Override
            public void onActivitySpectate(IPCClient ipcClient, String s) {

            }

            @Override
            public void onActivityJoinRequest(IPCClient ipcClient, String s, User user) {

            }

            @Override
            public void onReady(IPCClient client) {
                log("The mod has been connected to Discord", Level.DEBUG);
                USER = client.getCurrentUser();
                CONNECTED_DISCORD = true;
            }

            @Override
            public void onClose(IPCClient ipcClient, JsonObject jsonObject) {
                CONNECTED_DISCORD = false;
            }

            @Override
            public void onDisconnect(IPCClient ipcClient, Throwable throwable) {
                log("The mod has been pulled from Discord", Level.DEBUG);
                log(String.format("Reason: %s", throwable.getLocalizedMessage()), Level.DEBUG);
                CONNECTED_DISCORD = false;
            }
        });
    }

    private static void start() {
        TIMER.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    if (userConfig.getBoolean("SHOW_RPC", true)) {
                        if (!lastShowStatus) {
                            try {
                                reconnectApp();
                                lastShowStatus = true;
                            } catch (Exception ex) {
                                log(ex.getMessage() == null ? ex.getClass().getName() : ex.getMessage());
                                return;
                            }
                        }
                    } else {
                        if (lastShowStatus) {
                            client.close();
                            lastShowStatus = false;
                        }
                    }
                    if (CONNECTED_DISCORD) updatePresence();
                    if (lastException != null) lastException = null;
                } catch (Exception ex) {
                    if (lastException == null || !lastException.equals(ex.getMessage())) {
                        log(ex.getLocalizedMessage(), Level.ERROR);
                        RichPresence.Builder presence = new RichPresence.Builder()
                                .setDetails("There was an error, look in the console")
                                .setState("And report the bug on GitHub")
                                .setLargeImage(Assets.getSelected().getIcon("unknown"));
                        client.sendRichPresence(presence.build());
                        lastException = ex.getMessage();
                    }
                }
            }
        }, 500, 500);
    }

    protected static boolean lastShowStatus = userConfig.getBoolean("SHOW_RPC", true);

    private static void updatePresence() {
        if (userConfig.getBoolean("SHOW_RPC", true)) {
            if (AlinLib.MINECRAFT.level == null || AlinLib.MINECRAFT.player == null) {
                switch (Client.getState()) {
                    case 1 -> new LoadingGame();
                    case 2 -> new Loading();
                    case 3 -> new Connect();
                    case 4 -> new Disconnect();
                    default -> new MainMenu();
                }
            } else {
                if (AlinLib.MINECRAFT.isSingleplayer() || AlinLib.MINECRAFT.hasSingleplayerServer()) new SinglePlayer();
                else if (AlinLib.MINECRAFT.getCurrentServer() != null) new MultiPlayer();
                else if (SimplyStatus.replayMod && userConfig.getBoolean("VIEW_REPLAY_MOD", true)) new ReplayMod();
                else new Unknown();
            }
        } else updateDiscordPresence(null);
    }

    public static void updateContentPresenceByConfigs(RichPresence.Builder presence) {
        updateContentPresenceByConfigs(presence, false);
    }

    public static void updateContentPresenceByConfigs(RichPresence.Builder presence, boolean isServer) {
        updateContentPresenceByConfigs(presence, isServer, false);
    }

    public static void updateContentPresenceByConfigs(RichPresence.Builder presence, boolean isServer, boolean isMenu) {

        if (SimplyStatus.userConfig.getBoolean("SHOW_GAME_TIME", true))
            presence.setStartTimestamp(SimplyStatus.TIME_STARTED_CLIENT);
        ///
        if (userConfig.getBoolean("VIEW_VOICE_SPEAK", false) && (isVoiceModsEnable && new Voice().isSpeak)) {
            Voice mod = new Voice();
            String info = mod.isSelfTalk ? localization.getLocalization("mod.voice", false) : mod.isOnePlayer ? localization.getLocalization("mod.voice.one", false) : localization.getLocalization("mod.voice.more", false);
            presence.setSmallImage(Assets.getSelected().getIcon("voice"), localization.getParsedText(info));
        } else if (userConfig.getBoolean("VIEW_MUSIC_LISTENER", false) && (isMusicModsEnable && !new WaterPlayerSupport().paused) && !isMenu) {
            presence.setSmallImage(Assets.getSelected().getIcon("music"), localization.getLocalization(new WaterPlayerSupport().artistIsNull ? "mod.music.noauthor" : "mod.music", true));
        } else if (isServer && (serverConfig.getBoolean("SHOW_ICON", false) && (!serverConfig.getString("ICON_URL", "").isEmpty()))) {
            presence.setSmallImage(serverConfig.getString("ICON_URL", "").replace("%address%", Objects.requireNonNull(AlinLib.MINECRAFT.getCurrentServer()).ip), localization.getParsedText("{player.scene}"));
        } else if (userConfig.getBoolean("SHOW_AVATAR_PLAYER", true)) {
            presence.setSmallImage(SimplyPlayer.getURLAvatar(), SimplyPlayer.getName());
        }

        if (SimplyStatus.userConfig.getBoolean("BUTTON.ENABLE", false)) {
            JsonArray buttons = new JsonArray();
            if (!SimplyStatus.userConfig.getString("BUTTON.LABEL", "").isBlank() && !SimplyStatus.userConfig.getString("BUTTON.URL", "").isBlank()) {
                JsonObject button = new JsonObject();
                button.addProperty("label", SimplyStatus.userConfig.getString("BUTTON.LABEL", ""));
                button.addProperty("url", SimplyStatus.userConfig.getString("BUTTON.URL", ""));
                buttons.add(button);
            }
            presence.setButtons(buttons);
        }
    }

    public static void updateDiscordPresence(RichPresence builder) {
        if (builder == null && ModConfig.debugPresence) LOG.info("Presence is null!");

        if (lastPresence == null || (builder != null && !lastPresence.toJson().toString().equalsIgnoreCase(builder.toJson().toString()))) {
            if (ModConfig.debugPresence) {
                if (lastPresence != null) log(lastPresence.toJson().toString());
                if(builder != null) log(builder.toJson().toString());
            }
            lastPresence = builder;
            try {
                if (CONNECTED_DISCORD) client.sendRichPresence(builder);
            } catch (Exception ex) {
                log(ex.getMessage() == null ? ex.getClass().getName() : ex.getMessage(), Level.ERROR);
            }
            if (ModConfig.debugPresence) LOG.info("Update presence");
        }
    }

    public static void reconnectApp() {
        if (SimplyStatus.CONNECTED_DISCORD) SimplyStatus.client.close();
        if (SimplyStatus.userConfig.getBoolean("USE_CUSTOM_APP_ID", false) && !SimplyStatus.customID.equals(SimplyStatus.userConfig.getString("CUSTOM_APP_ID", ModConfig.baseID))) {
            SimplyStatus.useCustomID = true;
            APPLICATION_ID = SimplyStatus.userConfig.getString("CUSTOM_APP_ID", ModConfig.baseID);
            if (APPLICATION_ID.isBlank()) {
                APPLICATION_ID = ModConfig.baseID;
                SimplyStatus.userConfig.setString("CUSTOM_APP_ID", APPLICATION_ID);
            }
            if (!SimplyStatus.customID.equals(APPLICATION_ID)) {
                SimplyStatus.customID = APPLICATION_ID;
            }
        } else if ((SimplyStatus.useAnotherID != SimplyStatus.userConfig.getBoolean("USE_ANOTHER_ID", false)) || (SimplyStatus.useCustomID != SimplyStatus.userConfig.getBoolean("USE_CUSTOM_APP_ID", false))) {
            SimplyStatus.useAnotherID = SimplyStatus.userConfig.getBoolean("USE_ANOTHER_ID", false);
            SimplyStatus.useCustomID = SimplyStatus.userConfig.getBoolean("USE_CUSTOM_APP_ID", false);
            SimplyStatus.customID = "";
            APPLICATION_ID = SimplyStatus.userConfig.getBoolean("USE_ANOTHER_ID", false) ? ModConfig.mineID : ModConfig.baseID;
        }
        if(!SimplyStatus.userConfig.getBoolean("SHOW_RPC", true)) return;
        SimplyStatus.lastPresence = null;
        SimplyStatus.client = new IPCClient(Long.parseLong(APPLICATION_ID));
        SimplyStatus.setupListener();
        try {
            SimplyStatus.client.connect();
        } catch (Exception ex) {
            log(ex.getLocalizedMessage(), Level.ERROR);
        }
    }
}