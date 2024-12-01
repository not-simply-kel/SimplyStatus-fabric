package ru.kelcuprum.simplystatus.presence;

import com.jagrosh.discordipc.entities.ActivityType;
import com.jagrosh.discordipc.entities.RichPresence;
import ru.kelcuprum.simplystatus.SimplyStatus;
import ru.kelcuprum.simplystatus.config.Assets;

public class Flashback {
    /**
     * Состояние: В главном меню<br>
     * Причины: Игрок находится в главном меню; Игрок в ReplayMod сцене и параметр скрыт
     */
    public Flashback() {
        RichPresence.Builder presence = new RichPresence.Builder()
                .setActivityType(ActivityType.Watching)
                .setDetails(SimplyStatus.localization.getLocalization("mod.flashback", true))
                .setState(SimplyStatus.localization.getLocalization("mod.flashback.state", true))
                .setLargeImage(Assets.getSelected().getIcon("flashback"));
        SimplyStatus.updateContentPresenceByConfigs(presence);
        SimplyStatus.sendPresence(presence.build());
    }
}