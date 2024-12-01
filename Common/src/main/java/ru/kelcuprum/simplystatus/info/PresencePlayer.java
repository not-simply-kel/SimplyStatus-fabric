package ru.kelcuprum.simplystatus.info;

import ru.kelcuprum.alinlib.AlinLib;
import ru.kelcuprum.alinlib.info.Player;
import ru.kelcuprum.simplystatus.SimplyStatus;
import ru.kelcuprum.simplystatus.mods.Voice;

public class PresencePlayer {
    static boolean lastMessageDeath = false;
    static String lastTextDeath = "";
    public static String getName(){
        if(SimplyStatus.userConfig.getBoolean("VIEW_PLAYER_NAME", true) || !SimplyStatus.CONNECTED || SimplyStatus.USER == null) return Player.getName();
        else return SimplyStatus.USER.getNickname();
    }
    public static String getURLAvatar(){
        if(Player.isLicenseAccount()){
           return switch (SimplyStatus.userConfig.getNumber("USE_API_RENDER", 0).intValue()){
                case 1 -> "https://api.kelcuprum.ru/skin/render/avatar?name="+AlinLib.MINECRAFT.getUser().getName()+"&api=0&sendfile=true";
                case 2 -> "https://api.kelcuprum.ru/skin/render?name="+AlinLib.MINECRAFT.getUser().getName()+"&api=0&head=true&sendfile=true";
                case 3 -> SimplyStatus.USER.getAvatarUrl();
                default -> "https://crafthead.net/helm/"+AlinLib.MINECRAFT.getUser().getProfileId()+"/512";
            };
        } else {
            if(SimplyStatus.CONNECTED) return SimplyStatus.USER.getAvatarUrl();
            else return "https://kelcuprum.ru/ass/other/error.png";
        }
    }
    public static String getState(){
        if(AlinLib.MINECRAFT.player == null) return "";
        if(AlinLib.MINECRAFT.player.isDeadOrDying()){
            double randomNumber = Math.floor(Math.random() * 2);
            if(!lastMessageDeath){
                String message;
                if(randomNumber == 0) message = SimplyStatus.localization.getLocalization("death.one", true);
                else if(randomNumber == 1) message = SimplyStatus.localization.getLocalization("death.two", true);
                else message = SimplyStatus.localization.getLocalization("death.three", true);
                lastTextDeath = message;
                lastMessageDeath = true;
                return message;
            } else {
                return lastTextDeath;
            }
        } else if(!SimplyStatus.userConfig.getBoolean("SHOW_ITEMS", true) || (Player.getItemName().isBlank() && SimplyStatus.userConfig.getBoolean("SHOW_ITEMS", true))){
            if(lastMessageDeath) lastMessageDeath = false;
            if(SimplyStatus.userConfig.getBoolean("VIEW_STATISTICS", true)){
                if(AlinLib.MINECRAFT.player.isSleeping()) return SimplyStatus.localization.getLocalization("player.sleep", true);
                else if(AlinLib.MINECRAFT.player.isCrouching()) return SimplyStatus.localization.getLocalization("player.sneak", true);
                else if(AlinLib.MINECRAFT.player.isOnFire()) return SimplyStatus.localization.getLocalization("player.on.fire", true);
                else if(AlinLib.MINECRAFT.player.isInLava()) return SimplyStatus.localization.getLocalization("player.on.lava", true);
                else if(AlinLib.MINECRAFT.player.isUnderWater()) return SimplyStatus.localization.getLocalization("player.on.water", true);
                else if(SimplyStatus.isVoiceModsEnable && SimplyStatus.userConfig.getBoolean("VIEW_VOICE_SPEAK", false)) {
                    Voice mod = new Voice();
                    if(mod.isSpeak){
                        if(mod.isSelfTalk) return SimplyStatus.localization.getLocalization("mod.voice", true);
                        else if(mod.isOnePlayer) return SimplyStatus.localization.getLocalization("mod.voice.players.one", true);
                        else return SimplyStatus.localization.getLocalization("mod.voice.players.more", true);
                    }
                    return SimplyStatus.localization.getLocalization("player.statistics", true);
                }
                else return SimplyStatus.localization.getLocalization("player.statistics", true);
            } else {
                return SimplyStatus.localization.getLocalization("item.air", true);
            }
        } else {
            return Player.getItemCount() >= 2 ? SimplyStatus.localization.getLocalization("item.count", true) : SimplyStatus.localization.getLocalization("item", true);
        }
    }
}