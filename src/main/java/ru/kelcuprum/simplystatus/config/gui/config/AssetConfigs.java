package ru.kelcuprum.simplystatus.config.gui.config;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import ru.kelcuprum.alinlib.gui.InterfaceUtils;
import ru.kelcuprum.alinlib.gui.components.buttons.Button;
import ru.kelcuprum.alinlib.gui.components.text.TextBox;
import ru.kelcuprum.simplystatus.SimplyStatus;

import java.util.ArrayList;
import java.util.List;

public class AssetConfigs extends Screen {
    private Component TITLE = Component.translatable("simplystatus.name");
    private final Screen parent;
    private int scrolled = 0;
    private List<AbstractWidget> widgets = new ArrayList<AbstractWidget>();
    // Components
    private TextBox titleBox;

    //
    public AssetConfigs(Screen parent){
        super(Component.translatable("simplystatus.config.assets"));
        this.parent = parent;
    }

    @Override
    public void init(){
        this.scrolled = 0;
        initPanel();
        initCategory();

    }
    public void tick(){
        for(int i=0; i<widgets.size();i++){
            if(i==0) widgets.get(i).setY(15-scrolled);
            else widgets.get(i).setY(40 + (25*(i-1))-scrolled);
        }
    }
    private void initCategory(){
        int x = this.width - 150;
        widgets = new ArrayList<>();
        this.titleBox = this.addRenderableWidget(new TextBox(140, 15, x, 9, this.title, true));
        this.addRenderableWidget(new TextBox(140, height/2-10, x, 20, Component.literal("Coming soon..."), true));

    }
    public void initPanel(){
        addRenderableWidget(new TextBox(10, 15, 110, font.lineHeight, TITLE, true));

        addRenderableWidget(new Button(10, 40, 110, 20, InterfaceUtils.DesignType.VANILLA, Component.translatable("simplystatus.config.client"), (OnPress) -> {
            this.minecraft.setScreen(new ClientConfigs(this.parent));
        }));

        addRenderableWidget(new Button(10, 65, 110, 20, InterfaceUtils.DesignType.VANILLA, Component.translatable("simplystatus.config.server"), (OnPress) -> {
            this.minecraft.setScreen(new ServerConfigs(this.parent));
        })).setActive(this.minecraft.getCurrentServer() != null);

        addRenderableWidget(new Button(10, 90, 110, 20, InterfaceUtils.DesignType.VANILLA, Component.translatable("simplystatus.config.localization"), (OnPress) -> {
            this.minecraft.setScreen(new LocalizationConfigs(this.parent));
        }));

        addRenderableWidget(new Button(10, 115, 110, 20, InterfaceUtils.DesignType.VANILLA, Component.translatable("simplystatus.config.addons"), (OnPress) -> {
            this.minecraft.setScreen(new AddonsConfigs(this.parent));
        }));

        addRenderableWidget(new Button(10, 140, 110, 20, InterfaceUtils.DesignType.VANILLA, Component.translatable("simplystatus.config.assets"), (OnPress) -> {
            this.minecraft.setScreen(new AssetConfigs(this.parent));
        })).setActive(SimplyStatus.userConfig.getBoolean("USE_CUSTOM_ASSETS", false) || SimplyStatus.userConfig.getBoolean("USE_CUSTOM_APP_ID", false));

        addRenderableWidget(new Button(10, 165, 110, 20, InterfaceUtils.DesignType.VANILLA, Component.translatable("simplystatus.config.mods"), (OnPress) -> {
            this.minecraft.setScreen(new ModsConfigs(this.parent));
        }));

        addRenderableWidget(new Button(10, height-30, 110, 20, InterfaceUtils.DesignType.VANILLA, Component.translatable("simplystatus.config.exit"), (OnPress) -> {
            this.minecraft.setScreen(parent);
        }));
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int i, int j, float f) {
        InterfaceUtils.renderBackground(guiGraphics, this.minecraft);
        InterfaceUtils.renderTextureLeftPanel(guiGraphics, 130, this.height);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int i, int j, float f) {
        super.render(guiGraphics, i, j, f);
        guiGraphics.drawCenteredString(this.minecraft.font, TITLE, 65, 15, -1);
    }
}