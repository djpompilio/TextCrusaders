/*
 *
 *
 *
 *
 *
 *         _____          _       ___                         _
 *        /__   \_____  _| |_    / __\ __ _   _ ___  __ _  __| | ___ _ __ ___
 *          / /\/ _ \ \/ / __|  / / | '__| | | / __|/ _` |/ _` |/ _ \ '__/ __|
 *         / / |  __/>  <| |_  / /__| |  | |_| \__ \ (_| | (_| |  __/ |  \__ \
 *         \/   \___/_/\_\\__| \____/_|   \__,_|___/\__,_|\__,_|\___|_|  |___/
 *
 *
 *                          ---Text Crusaders---
 *                             Text Based RPG
 *
 *                        © Dominic Pompilio - 2025
 *
 *
 *
 *
 *
 */

/*
        General Notes & To do's
    --------------------------------


        TODO clean up code a bit
        TODO when in classic mode change slog font to a terminal font
        TODO add "are you sure?" dialogs before quiting/new game if current game isn't saved
        TODO -Game Mechanic- at higher difficulty levels roll with disadvantage as health depletes when fighting enemies
        TODO -Game Mechanic- Make the story for the default character "unknown" slightly different than if the player chooses to make their own build
        ??? TODO basic text mode/cleaner text log ui with font  -- toggle in settings
        TODO -Future- Create a mouse only mode for accessibility
        TODO add toggle to disable welcome message in slog

        commands - (quit, exit) save, new, load
*/

package com.djpompilio.textcrusaders;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@SuppressWarnings("SpellCheckingInspection")

public class textCrusaders extends ApplicationAdapter {
    private Stage stage;
    private Skin skin;
    private String currentCmd;
    private String version = "0.2 Alpha";
    private ScreenViewport sVp;
    private Window storyLog;
    private Window statsWindow;
    private Window mapWindow;
    private Window inventoryWindow;
    private Window mainMenu;
    private Window newGameWindow;
    private Table bottomBar;
    private int NGSP = 5;
    private int currNGSP = 5;
    private int strSliHist = 0;
    private int speeSliHist = 0;
    private int charSliHist = 0;
    private int lucSliHist = 0;
    private Cursor cursor;
    private float csVol = 0.5f;



    @Override
    public void create() {
        sVp = new ScreenViewport();
        stage = new Stage(sVp);
        Gdx.input.setInputProcessor(stage);
        Graphics.DisplayMode primaryMode = Gdx.graphics.getDisplayMode();
        sVp.apply();

        // --Cursor Setup--
        Pixmap cur = new Pixmap(Gdx.files.internal("Cursor.png"));
        Pixmap ccur = new Pixmap(Gdx.files.internal("Cursor_Click.png"));
        cursor = Gdx.graphics.newCursor(cur, 0, 0);
        Cursor cursorClick = Gdx.graphics.newCursor(ccur, 0, 0);
        Gdx.graphics.setCursor(cursor);
        cur.dispose();
        ccur.dispose();

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        Sound clickSound = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/mouse-click.mp3"));


        /*
         *                             --> UI Layout <--
         *
         *
         *                   Note: Positioning is from bottom left corner!
         * ______________________________________________________________________________
         */


        skin = new Skin(Gdx.files.internal("pixthulhu/pixthulhu-ui.json"));


        /*
         *                               Main UI Setup
         *                   All Game Windows and Bottom Control Bar
         * ______________________________________________________________________________
         */


        //    ----Story Log Window----
        TextArea sLogText = new TextArea("Welcome to Text Crusaders!\n\nVer. " + getVersion() + "\n\n\n           Ready to start\n            your journey?\n\n\ntesting... ;) -- testingfdghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhbbbbbbbbbbbbbbbbbbbbbbbfffffffffffffffffffffffffaaaaaaaaaaaaaaaaaaaaaaaabbbbbbbbbbbbbbbcccccccccccccccccdddddddddddddddeeeeeeeeeeeeeeeeeeffffffffffffffffggggggggggggggggggggggggggghhhhhhhhhhhhhhhiiiiiiiiiiiiiiiiijjjjjjjjjjjjjjjjjjjjjjkkkkkkkkkkkkkklllllllllllll \n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n test", skin);
        sLogText.setPrefRows(25);
        sLogText.setDisabled(true);

        ScrollPane sLogScroll = new ScrollPane(sLogText, skin);
        sLogScroll.setSmoothScrolling(true);
        sLogScroll.setScrollBarPositions(false, true);
        sLogScroll.setFadeScrollBars(false);
        sLogScroll.setFlickScroll(false);
        sLogScroll.setVariableSizeKnobs(true);

        storyLog = new Window("Story Log", skin);
        storyLog.setSize((float) (stage.getWidth() * .35), (float) (stage.getHeight() * .83));
        storyLog.setPosition((float) (stage.getWidth() * .01), (float) (stage.getHeight() * .16));
        storyLog.setResizable(true);
        storyLog.setBackground("window-round");
        storyLog.getTitleTable().pad(0, 15, 0, 0);

        storyLog.add(sLogScroll).grow().pad(20, 5, 5, 5);


        //  --Status Window--
        statsWindow = new Window("Status", skin);
        statsWindow.setBackground("window-round");
        statsWindow.setSize((float) (stage.getWidth() * .45), (float) (stage.getHeight() * .40));
        statsWindow.setPosition((float) (stage.getWidth() * .368), (float) (stage.getHeight() * .16));
        Button statClose = new Button(skin, "arcade");
        statsWindow.getTitleTable().add(statClose).maxSize(30, 30).pad(35, 0, 0, 10);
        ProgressBar hp = new ProgressBar(0, 100, 1, false, skin, "health");
        ProgressBar mp = new ProgressBar(0, 100, 1, false, skin, "mana");
        ProgressBar xp = new ProgressBar(0, 100, 1, false, skin);
        Label charismaStatLabel = new Label("Charisma", skin);
        Label strengthStatLabel = new Label("Strength", skin);
        Label speedStatLabel = new Label("Speed", skin);
        Label luckStatLabel = new Label("Luck", skin);
        Label coinsStatLabel = new Label("Coins", skin);
        Label levelStatLabel = new Label("Level", skin);
        Table barTable = new Table();
        Table lvlCoinTable = new Table();
        Table skillTable = new Table();
        hp.setValue(100);
        mp.setValue(50);
        xp.setValue(40);
        xp.setSize(50, 20);
        statsWindow.getTitleTable().pad(0, 15, 0, 0);
        barTable.add(hp).growX().pad(20, 10, 0, 20).prefWidth((float) (barTable.getWidth() * .60));
        barTable.row();
        barTable.add(mp).growX().pad(5, 10, 0, 20).prefWidth((float) (barTable.getWidth() * .60));
        statsWindow.add(barTable).growX();
        statsWindow.add(lvlCoinTable);
        statsWindow.row();
        skillTable.add(strengthStatLabel);
        skillTable.add(speedStatLabel);
        skillTable.row();
        skillTable.add(charismaStatLabel);
        skillTable.add(luckStatLabel);
        statsWindow.add(skillTable).grow();
        lvlCoinTable.add(coinsStatLabel);
        lvlCoinTable.add(levelStatLabel);
        lvlCoinTable.row();
        lvlCoinTable.add(xp);


        //  --Map Window--
        mapWindow = new Window("Map", skin);
        mapWindow.setBackground("window-round");
        mapWindow.setSize((float) (stage.getWidth() * .25), (float) (stage.getHeight() * .43));
        mapWindow.setPosition((float) (stage.getWidth() * .726), (float) (stage.getHeight() * .562));
        mapWindow.getTitleTable().pad(0, 15, 0, 0);
        Button mapClose = new Button(skin, "arcade");
        mapWindow.getTitleTable().add(mapClose).maxSize(30, 30).pad(35, 0, 0, -20);

        //  --Inventory Window--
        inventoryWindow = new Window("Inventory", skin);
        Label test = new Label("━━━━━━━━━━━━━━", skin);
        //inventoryWindow.setDebug(true, true);
        inventoryWindow.setBackground("window-round");
        inventoryWindow.setSize((float) (stage.getWidth() * .35), (float) (stage.getHeight() * .43));
        inventoryWindow.setPosition((float) (stage.getWidth() * .368), (float) (stage.getHeight() * .562));
        inventoryWindow.getTitleTable().pad(0, 15, 0, 0);
        Button invClose = new Button(skin, "arcade");
        inventoryWindow.getTitleTable().add(invClose).maxSize(30, 30).pad(100, 0, 0, 10).align(Align.topRight);
        inventoryWindow.add(test).align(Align.topLeft).growY().pad(-240, -100, 0, 0);

        //  --Bottom Control Bar--
        bottomBar = new Table(skin);
        TextButton menuButton = new TextButton("Menu", skin);
        TextField commandText = new TextField("", skin);
        commandText.setMessageText("Input Commands Here");
        bottomBar.setPosition(0, 0, Align.bottomLeft);
        bottomBar.setSize(Gdx.graphics.getWidth(), 110);
        bottomBar.setBackground("window-round");
        bottomBar.add(commandText).prefSize(1280, 50).pad(12, 10, 0, 5);
        bottomBar.add(menuButton).pad(12, 5, 0, 10).maxSize(200, 90);


        /*
         * ______________________________________________________________________________
         */


        /*
         *       Menus Setup
         */


        //  --Game Menu--
        Table gameMenu = new Table(skin);
        gameMenu.setBackground("window");
        TextButton saveBtn = new TextButton("Save", skin);
        TextButton loadBtn = new TextButton("Load", skin); // TODO: Check current game is saved first
        TextButton newBtn = new TextButton("New", skin);  // Check current game is saved first
        TextButton quitBtn = new TextButton("Quit", skin); // Check current game is saved first
        gameMenu.add(saveBtn).pad(10, 10, 10, 10);
        gameMenu.add(newBtn).pad(10, 10, 10, 10);
        gameMenu.row();
        gameMenu.add(loadBtn).pad(10, 10, 10, 10);
        gameMenu.add(quitBtn).pad(10, 10, 10, 10);
        gameMenu.setVisible(true);


        //  --Main Menu--
        mainMenu = new Window("", skin);
        Table menuNav = new Table(skin);
        Table menuScreen = new Table(skin);
        menuScreen.setHeight((float) (mainMenu.getHeight() * .95));
        Button menuClose = new Button(skin, "arcade");
        mainMenu.getTitleTable().add(menuClose).maxSize(40, 40).pad(35, 0, 0, -20);
        mainMenu.setBackground("window-round");
        mainMenu.setSize((float) (stage.getWidth() * .9), (float) (stage.getHeight() * .85));
        mainMenu.setPosition((float) (stage.getWidth() * .05), (float) (stage.getHeight() * .08));
        mainMenu.setModal(true);
        mainMenu.setVisible(false);
        mainMenu.align(Align.top);
        mainMenu.setDebug(true);
        Label menuLabel = new Label("Main Menu", skin, "title");
        TextButton gameBtn = new TextButton("Game", skin);
        TextButton settingsBtn = new TextButton("Settings", skin);
        TextButton aboutBtn = new TextButton("About", skin);
        TextButton creditsBtn = new TextButton("Credits", skin);
        Table menuBtnRow = new Table();


        menuBtnRow.add(gameBtn).pad(0, 0, 0, 10);
        menuBtnRow.add(settingsBtn).pad(0, 10, 0, 10);
        menuBtnRow.add(aboutBtn).pad(0, 10, 0, 10);
        menuBtnRow.add(creditsBtn).pad(0, 10, 0, 0);
        menuNav.add(menuLabel).align(Align.top).pad(-20, 0, 0, 0);
        menuNav.row();
        menuNav.add(menuBtnRow).align(Align.top);
        mainMenu.add(menuNav).align(Align.top).growX().pad(10);
        mainMenu.row();
        mainMenu.add(menuScreen).grow();
        menuScreen.add(gameMenu);


        //  --Settings Menu--
        Table settingsMenu = new Table(skin);
        settingsMenu.setBackground("window");
        Label viewLabel = new Label("Window Visibility", skin);
        CheckBox invCheckBox = new CheckBox("Inventory", skin);
        CheckBox statCheckBox = new CheckBox("Status", skin);
        CheckBox mapCheckBox = new CheckBox("Map", skin);
        CheckBox classicModeCheckBox = new CheckBox("Classic Mode", skin);

        invCheckBox.setChecked(true);
        statCheckBox.setChecked(true);
        mapCheckBox.setChecked(true);

        Label diffLabel = new Label("Difficulty", skin);
        SelectBox<String> diffSelect = new SelectBox<>(skin);

        String[] difficultyList = new String[]{"Easy", "Normal", "Hard", "Impossible"};
        diffSelect.setItems(difficultyList);
        diffSelect.setSelectedIndex(1);

        Graphics.DisplayMode[][] supportedDispModes = new Graphics.DisplayMode[][]{Gdx.graphics.getDisplayModes()};
        String dispModeRes = Arrays.deepToString(supportedDispModes);
        String[] separateDispModeResList = dispModeRes.split(",");

        List<String> supportedResList = new ArrayList<>();

        for (int i = 0; i < separateDispModeResList.length; i = i + 3){
            supportedResList.add(separateDispModeResList[i]);
        }
        System.out.println(supportedResList);
        String[] supportedResolutions = supportedResList.toArray(new String[0]);

        Label graphicsLabel = new Label("Graphics", skin);
        Label resLabel = new Label("Resolution", skin);
        SelectBox<String> resSelect = new SelectBox<>(skin);
        resSelect.setItems(supportedResolutions);
        CheckBox winModeCheckBox = new CheckBox("Windowed", skin);
        CheckBox fullModeCheckBox = new CheckBox("Fullscreen", skin);

        Label apperanceLabel = new Label("Appearance", skin);
        Label fontsLabel = new Label("Font", skin);
        SelectBox<String> fontSelect = new SelectBox<>(skin);
        Label themesLabel = new Label("Theme", skin);
        SelectBox<String> themeSelect = new SelectBox<>(skin);
        Label audioLabel = new Label("Audio", skin);
        Label sfxLabel = new Label("Sound Effects", skin);
        CheckBox clickSoundCheckbox = new CheckBox("Click", skin);
        Label musicLabel = new Label("Music", skin);
        Slider sfxVol = new Slider(0, 100, 1, false, skin);
        Slider musicVol = new Slider(0, 100, 1, false, skin);
        sfxVol.setValue(100);
        sfxVol.setWidth(200);
        Label sfxVolLabel = new Label((int)sfxVol.getValue() + "%", skin);
        Label musicVolLabel = new Label((int)musicVol.getValue() + "%", skin);

        clickSoundCheckbox.setChecked(true);


        settingsMenu.add(viewLabel);
        settingsMenu.row();
        settingsMenu.add(invCheckBox, statCheckBox, mapCheckBox);
        settingsMenu.row();
        settingsMenu.add(classicModeCheckBox);
        settingsMenu.row();
        settingsMenu.add(diffLabel);
        settingsMenu.row();
        settingsMenu.add(diffSelect);
        settingsMenu.row();
        settingsMenu.add(graphicsLabel);
        settingsMenu.row();
        settingsMenu.add(resLabel);
        settingsMenu.row();
        settingsMenu.add(resSelect);
        settingsMenu.add(winModeCheckBox);
        settingsMenu.add(fullModeCheckBox);
        settingsMenu.row();
        settingsMenu.add(audioLabel);
        settingsMenu.row();
        settingsMenu.add(sfxLabel);
        settingsMenu.add(sfxVol);
        settingsMenu.add(sfxVolLabel);
        settingsMenu.add(clickSoundCheckbox);
        settingsMenu.row();
        settingsMenu.add(musicLabel);
        settingsMenu.add(musicVol);
        settingsMenu.add(musicVolLabel);
        settingsMenu.row();
        settingsMenu.add(apperanceLabel);
        settingsMenu.row();
        settingsMenu.add(fontsLabel);
        settingsMenu.row();
        settingsMenu.add(fontSelect);
        settingsMenu.row();
        settingsMenu.add(themesLabel);
        settingsMenu.row();
        settingsMenu.add(themeSelect);
        settingsMenu.setHeight(200);

        ScrollPane settingsScroll = new ScrollPane(settingsMenu, skin);
        settingsScroll.setScrollBarPositions(false, true);
        settingsScroll.setScrollbarsOnTop(false);
        settingsScroll.setSize(1000, 1000);
        settingsScroll.setFadeScrollBars(false);
        settingsScroll.setFlickScroll(false);
        settingsScroll.setScrollbarsVisible(true);
        settingsScroll.setSmoothScrolling(true);

        settingsScroll.setVisible(true);


        //  --New Game Menu--

        newGameWindow = new Window("New Game", skin);
        newGameWindow.setSize((float) (stage.getWidth() * .80), (float) (stage.getHeight() * .85));
        newGameWindow.setPosition((float) (stage.getWidth() * .1), (float) (stage.getHeight() * .08));
        newGameWindow.setModal(true);
        newGameWindow.setBackground("window-round");
        Table newGameTable = new Table(skin);
        Table newGameNavTable = new Table(skin);
        Table newGameStatsTable = new Table(skin);
        Table newGameNameTable = new Table(skin);
        Label playerNameLabel = new Label("Name", skin);
        TextField playerNameText = new TextField("", skin);

        playerNameText.setMessageText("Unknown");
        Label skillPtsLeftNG = new Label("Skill Points: " + NGSP, skin);
        Label strengthLabel = new Label("Strength", skin);
        Label speedLabel = new Label("Speed", skin);
        Label charismaLabel = new Label("Charisma", skin);
        Label luckLabel = new Label("Luck", skin);
        Slider strengthSlider = new Slider(0, 5, 1, false, skin);
        Slider speedSlider = new Slider(0, 5, 1, false, skin);
        Slider charismaSlider = new Slider(0, 5, 1, false, skin);
        Slider luckSlider = new Slider(0, 5, 1, false, skin);
        Label strengthSliderCurrSP = new Label("" + (int) (strengthSlider.getValue()), skin);
        Label speedSliderCurrSP = new Label("" + (int) (speedSlider.getValue()), skin);
        Label charismaSliderCurrSP = new Label("" + (int) (charismaSlider.getValue()), skin);
        Label luckSliderCurrSP = new Label("" + (int) (luckSlider.getValue()), skin);

        TextButton startGameBtn = new TextButton("Start", skin);
        TextButton cancelNewGameBtn = new TextButton("Cancel", skin);
        Table startGameBtnTable = new Table(skin);
        Table cancelNewGameBtnTable = new Table(skin);

        cancelNewGameBtnTable.add(cancelNewGameBtn).padLeft(50);
        startGameBtnTable.add(startGameBtn).padRight(50);
        newGameNavTable.layout();
        newGameNavTable.add(cancelNewGameBtnTable).align(Align.left).expand();
        newGameNavTable.add(startGameBtnTable).align(Align.right).expand();


        newGameNavTable.setDebug(true);
        //newGameWindow.setDebug(true);


        newGameNameTable.add(playerNameLabel).align(Align.left);
        newGameNameTable.row();
        newGameNameTable.add(playerNameText).prefWidth(500).align(Align.center).padBottom(15);
        newGameTable.add(newGameNameTable);
        newGameTable.row();
        newGameStatsTable.add(strengthLabel).align(Align.right).padRight(20);
        newGameStatsTable.add(strengthSlider).prefWidth(250).padBottom(10);
        newGameStatsTable.add(strengthSliderCurrSP);
        newGameStatsTable.add(skillPtsLeftNG).pad(20);
        newGameStatsTable.row();
        newGameStatsTable.add(speedLabel).align(Align.right).padRight(20);
        newGameStatsTable.add(speedSlider).prefWidth(250).padBottom(10);
        newGameStatsTable.add(speedSliderCurrSP);
        newGameStatsTable.row();
        newGameStatsTable.add(charismaLabel).align(Align.right).padRight(20);
        newGameStatsTable.add(charismaSlider).prefWidth(250).padBottom(10);
        newGameStatsTable.add(charismaSliderCurrSP);
        newGameStatsTable.row();
        newGameStatsTable.add(luckLabel).align(Align.right).padRight(20);
        newGameStatsTable.add(luckSlider).prefWidth(250).padBottom(10);
        newGameStatsTable.add(luckSliderCurrSP);
        newGameTable.add(newGameStatsTable).align(Align.center);
        newGameTable.row();

        //add difficulty setting too
        //newGameTable.add(classicModeCheckBox);

        newGameWindow.add(newGameTable);
        newGameWindow.row();

        newGameTable.row();
        newGameWindow.add(newGameNavTable).growX();

        newGameWindow.setVisible(false);


        //  --About Screen--
        Table aboutScreen = new Table(skin);
        Table aboutTitle = new Table();
        Table aboutBody = new Table();
        Table aboutFooter = new Table();
        Label aboutLabel = new Label("About Text Crusaders", skin, "subtitle");
        Label aboutAlphaLabel = new Label("Expect things to not work. ;p", skin);
        Label aboutRPGLabel = new Label("A (Mostly) Text Based RPG.", skin);
        Label aboutVerLabel = new Label("Version: " + getVersion(), skin);
        Label aboutCopyLabel = new Label("Copyright © 2025 - Dominic Pompilio", skin);
        aboutScreen.layout();
        aboutTitle.add(aboutLabel);
        aboutTitle.row();
        aboutTitle.add(aboutRPGLabel);
        aboutScreen.add(aboutTitle).top().pad(0, 0, 10, 0);
        aboutScreen.row();
        aboutScreen.add(aboutVerLabel);
        aboutScreen.row();
        aboutBody.add(aboutAlphaLabel).growY();
        aboutScreen.add(aboutBody).growY();
        aboutScreen.row();
        aboutFooter.add(aboutCopyLabel);
        aboutScreen.add(aboutFooter).bottom();
        aboutScreen.setVisible(false);

        //  --Credits Screen--

        Table creditsScreen = new Table(skin);
        Table creditsTitle = new Table();
        Table creditsBody = new Table();
        Label creditsLabel = new Label("Credits", skin, "subtitle");
        Label gameLabel = new Label("Text Crusaders game and story by Dominic Pompilio", skin);
        Label musicCredLabel = new Label("Music by Kevin MacLeod (incompetech.com)\n" +
            "Licensed under Creative Commons [CC BY 3.0]\n" +
            "http://creativecommons.org/licenses/by/3.0/", skin);
        Label uiCredLabel = new Label("Interface based on Pixthulhu UI by Raymond \"Raeleus\" Buckley \nModified by Dominic Pompilio. \nLicensed under Creative Commons [CC BY 4.0] \n(http://creativecommons.org/licenses/by/4.0/).", skin);

        creditsScreen.layout();

        creditsTitle.add(creditsLabel);

        creditsBody.add(gameLabel);
        creditsBody.row();
        creditsBody.add(musicCredLabel);
        creditsBody.row();
        creditsBody.add(uiCredLabel);

        creditsScreen.add(creditsTitle);
        creditsScreen.row();
        creditsScreen.add(creditsBody);




        /*
                            ---Input Handling---
        ______________________________________________________________________________

        */

        //Cursor Animation
        stage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play(csVol);
                Gdx.graphics.setCursor(cursorClick);
                executorService.schedule(new cursorAni(), 200, TimeUnit.MILLISECONDS);
            }
        });

        stage.addListener(new ClickListener() {
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                Gdx.graphics.setCursor(cursorClick);
            }
        });

        stage.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("release");
            }
        });


        // Bottom Bar
        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameBtn.setColor(.8F, .8F, .8F, 1);
                mainMenu.setVisible(true);
                mainMenu.toFront();
            }
        });


        commandText.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ENTER) {
                    setCurrentCmd(commandText.getText());
                    sLogText.appendText(getCurrentCmd() + "\n");
                    sLogText.setPrefRows(sLogText.getLines());
                    //System.out.println(sLogText.getLines());
                    commandText.setText("");
                    sLogScroll.invalidate();
                    sLogScroll.setScrollPercentY(100);

                    switch (getCurrentCmd()) {
                        case "Help":
                            System.out.println("help");
                            break;
                        case "exit":
                            System.out.println("exit");
                            break;
                        default:
                            System.out.println("send to corenlp");
                    }

                }
                return true;
            }
        });

        // Main Menu
        menuClose.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mainMenu.setVisible(false);
                if (gameBtn.getColor().equals(new Color(.8F, .8F, .8F, 1)))
                    gameBtn.setColor(1, 1, 1, 1);
                if (settingsBtn.getColor().equals(new Color(.8F, .8F, .8F, 1)))
                    settingsBtn.setColor(1, 1, 1, 1);
                if (creditsBtn.getColor().equals(new Color(.8F, .8F, .8F, 1)))
                    creditsBtn.setColor(1, 1, 1, 1);
                if (aboutBtn.getColor().equals(new Color(.8F, .8F, .8F, 1)))
                    aboutBtn.setColor(1, 1, 1, 1);
                menuScreen.reset();
                menuScreen.add(gameMenu);
                gameBtn.setColor(.8F, .8F, .8F, 1);

            }
        });

        quitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.exit(0);
            }
        });

        newBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                inventoryWindow.setVisible(false);
                statsWindow.setVisible(false);
                mapWindow.setVisible(false);
                storyLog.setVisible(false);
                mainMenu.setVisible(false);
                newGameWindow.setVisible(true);
                newGameWindow.toFront();

            }
        });

        // New Game Menu
        strengthSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                int v = (int) strengthSlider.getValue();
                strengthSliderCurrSP.setText(v);
            }
        });

        strengthSlider.addListener(new DragListener() {
            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer) {
                int v = (int) strengthSlider.getValue();
                strengthSliderCurrSP.setText(v);

                if (currNGSP != 0) {  // Check if skill points are available
                    if (v > strSliHist) {
                        // Up
                        int chg = v - strSliHist;
                        if (v > currNGSP) { //over skill points limit on value change
                            strengthSlider.setValue(currNGSP + (v -= chg));
                            System.out.println("test");
                            skillPtsLeftNG.setText("Skill Points: " + 0);
                        } else {
                            currNGSP -= chg;
                            skillPtsLeftNG.setText("Skill Points: " + currNGSP);
                        }
                    } else if (v == strSliHist) {
                        System.out.println("Equal" + strSliHist);
                    } else {
                        // Down
                        int chg = Math.abs(v - strSliHist);
                        currNGSP += chg;
                        skillPtsLeftNG.setText("Skill Points: " + currNGSP);
                    }
                } else {  // No skill points available
                    if (v > strSliHist) {
                        // Up
                        int chg = v - strSliHist;
                        strengthSlider.setValue(v -= chg);
                        skillPtsLeftNG.setText("Skill Points: " + currNGSP);
                    } else if (v == strSliHist) {
                        System.out.println("Equal" + strSliHist);
                    } else {
                        // Down
                        int chg = Math.abs(v - strSliHist);
                        currNGSP += chg;
                        skillPtsLeftNG.setText("Skill Points: " + currNGSP);
                    }

                }

                strSliHist = v;
            }
        });

        speedSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                int v = (int) speedSlider.getValue();
                speedSliderCurrSP.setText(v);
            }
        });

        speedSlider.addListener(new DragListener() {
            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer) {
                int v = (int) speedSlider.getValue();
                speedSliderCurrSP.setText(v);

                if (currNGSP != 0) {  // Check if skill points are available
                    if (v > speeSliHist) {
                        // Up
                        int chg = v - speeSliHist;
                        if (v > currNGSP) { //over skill points limit on value change
                            speedSlider.setValue(currNGSP + (v -= chg));
                            System.out.println("test");
                            skillPtsLeftNG.setText("Skill Points: " + 0);
                        } else {
                            currNGSP -= chg;
                            skillPtsLeftNG.setText("Skill Points: " + currNGSP);
                        }
                    } else if (v == speeSliHist) {
                        System.out.println("Equal" + speeSliHist);
                    } else {
                        // Down
                        int chg = Math.abs(v - speeSliHist);
                        currNGSP += chg;
                        skillPtsLeftNG.setText("Skill Points: " + currNGSP);
                    }
                } else {  // No skill points available
                    if (v > speeSliHist) {
                        // Up
                        int chg = v - speeSliHist;
                        speedSlider.setValue(v -= chg);
                        skillPtsLeftNG.setText("Skill Points: " + currNGSP);
                    } else if (v == speeSliHist) {
                        System.out.println("Equal" + speeSliHist);
                    } else {
                        // Down
                        int chg = Math.abs(v - speeSliHist);
                        currNGSP += chg;
                        skillPtsLeftNG.setText("Skill Points: " + currNGSP);
                    }
                }
                speeSliHist = v;
            }
        });

        charismaSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                int v = (int) charismaSlider.getValue();
                charismaSliderCurrSP.setText(v);
            }
        });

        luckSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                int v = (int) luckSlider.getValue();
                luckSliderCurrSP.setText(v);
            }
        });

        mapClose.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mapWindow.setVisible(false);
                mapCheckBox.setChecked(false);
            }
        });

        invClose.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                inventoryWindow.setVisible(false);
                invCheckBox.setChecked(false);
            }
        });

        statClose.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                statsWindow.setVisible(false);
                statCheckBox.setChecked(false);
            }
        });

        // Settings Screen
        settingsBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menuScreen.reset();
                menuScreen.add(settingsScroll).growX();
                settingsScroll.setVisible(true);
                stage.setScrollFocus(settingsScroll);
                if (gameBtn.getColor().equals(new Color(.8F, .8F, .8F, 1)))
                    gameBtn.setColor(1, 1, 1, 1);
                if (aboutBtn.getColor().equals(new Color(.8F, .8F, .8F, 1)))
                    aboutBtn.setColor(1, 1, 1, 1);
                if (creditsBtn.getColor().equals(new Color(.8F, .8F, .8F, 1)))
                    creditsBtn.setColor(1, 1, 1, 1);
                settingsBtn.setColor(.8F, .8F, .8F, 1);
            }
        });

        invCheckBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (classicModeCheckBox.isChecked() || invCheckBox.isChecked()) {
                    classicModeCheckBox.setChecked(false);
                    storyLog.setSize((float) (stage.getWidth() * .35), (float) (stage.getHeight() * .83));
                    inventoryWindow.setVisible(true);
                } else {
                    inventoryWindow.setVisible(false);
                }
            }
        });

        statCheckBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (classicModeCheckBox.isChecked() || statCheckBox.isChecked()) {
                    classicModeCheckBox.setChecked(false);
                    storyLog.setSize((float) (stage.getWidth() * .35), (float) (stage.getHeight() * .83));
                    statsWindow.setVisible(true);
                } else {
                    statsWindow.setVisible(false);
                }
            }
        });

        mapCheckBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (classicModeCheckBox.isChecked() || mapCheckBox.isChecked()) {
                    classicModeCheckBox.setChecked(false);
                    storyLog.setSize((float) (stage.getWidth() * .35), (float) (stage.getHeight() * .83));
                    mapWindow.setVisible(true);
                } else {
                    mapWindow.setVisible(false);
                }
            }
        });

        fullModeCheckBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.graphics.setFullscreenMode(primaryMode);
                ScreenUtils.clear(0.15f, 0.2f, 0.2f, 1f);
                System.out.println(sVp.getScreenWidth());
                bottomBar.invalidate();
                winModeCheckBox.setChecked(false);
            }
        });

        winModeCheckBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.graphics.setWindowedMode(1280, 720); //fix to be not hardcoded
                System.out.println(sVp.getScreenWidth());
                fullModeCheckBox.setChecked(false);
            }
        });

        clickSoundCheckbox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (clickSoundCheckbox.isChecked()) {
                    csVol = 0.5f;
                }
                else {
                    csVol = 0;
                }
            }
        });

        sfxVol.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                sfxVolLabel.setText((int)sfxVol.getValue() + "%");
            }
        });

        musicVol.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                musicVolLabel.setText((int)musicVol.getValue() + "%");
            }
        });

        classicModeCheckBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (classicModeCheckBox.isChecked()) {
                    invCheckBox.setChecked(false);
                    statCheckBox.setChecked(false);
                    mapCheckBox.setChecked(false);
                    inventoryWindow.setVisible(false);
                    statsWindow.setVisible(false);
                    mapWindow.setVisible(false);
                    storyLog.setSize((float) (stage.getWidth() * .98), (float) (stage.getHeight() * .83));
                    sLogScroll.setSize((float) (stage.getWidth() * .98), (float) (stage.getHeight() * .83));
                    //sLogScroll.invalidate();
                    storyLog.invalidateHierarchy();
                } else {
                    Gdx.app.log("Button classic Clicked", "Button was successfully clicked!");
                    storyLog.setSize((float) (stage.getWidth() * .35), (float) (stage.getHeight() * .83));
                    sLogScroll.setSize((float) (stage.getWidth() * .35), (float) (stage.getHeight() * .83));
                    invCheckBox.setChecked(true);
                    statCheckBox.setChecked(true);
                    mapCheckBox.setChecked(true);
                    inventoryWindow.setVisible(true);
                    statsWindow.setVisible(true);
                    mapWindow.setVisible(true);
                    sLogScroll.invalidate();
                }
            }
        });

        aboutBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menuScreen.reset();
                menuScreen.add(aboutScreen);
                aboutScreen.setVisible(true);
                if (gameBtn.getColor().equals(new Color(.8F, .8F, .8F, 1)))
                    gameBtn.setColor(1, 1, 1, 1);
                if (settingsBtn.getColor().equals(new Color(.8F, .8F, .8F, 1)))
                    settingsBtn.setColor(1, 1, 1, 1);
                if (creditsBtn.getColor().equals(new Color(.8F, .8F, .8F, 1)))
                    creditsBtn.setColor(1, 1, 1, 1);
                aboutBtn.setColor(.8F, .8F, .8F, 1);
            }
        });

        gameBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menuScreen.reset();
                menuScreen.add(gameMenu);
                aboutScreen.setVisible(true);
                if (aboutBtn.getColor().equals(new Color(.8F, .8F, .8F, 1)))
                    aboutBtn.setColor(1, 1, 1, 1);
                if (settingsBtn.getColor().equals(new Color(.8F, .8F, .8F, 1)))
                    settingsBtn.setColor(1, 1, 1, 1);
                if (creditsBtn.getColor().equals(new Color(.8F, .8F, .8F, 1)))
                    creditsBtn.setColor(1, 1, 1, 1);
                gameBtn.setColor(.8F, .8F, .8F, 1);
            }
        });

        creditsBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menuScreen.reset();
                menuScreen.add(creditsScreen);
                creditsScreen.setVisible(true);
                if (gameBtn.getColor().equals(new Color(.8F, .8F, .8F, 1)))
                    gameBtn.setColor(1, 1, 1, 1);
                if (settingsBtn.getColor().equals(new Color(.8F, .8F, .8F, 1)))
                    settingsBtn.setColor(1, 1, 1, 1);
                if (aboutBtn.getColor().equals(new Color(.8F, .8F, .8F, 1)))
                    aboutBtn.setColor(1, 1, 1, 1);
                creditsBtn.setColor(.8F, .8F, .8F, 1);
            }
        });


        // New Game Menu

        cancelNewGameBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                newGameWindow.setVisible(false);
                inventoryWindow.setVisible(true);
                statsWindow.setVisible(true);
                mapWindow.setVisible(true);
                storyLog.setVisible(true);
                mainMenu.setVisible(true);
                mainMenu.toFront();
            }
        });


        /*
         * ______________________________________________________________________________
         */


        // Adding main UI components to stage
        stage.addActor(storyLog);
        stage.addActor(statsWindow);
        stage.addActor(mapWindow);
        stage.addActor(inventoryWindow);
        stage.addActor(bottomBar);
        stage.addActor(mainMenu);
        stage.addActor(newGameWindow);


    }

    // Main-ish Method
    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.2f, 0.2f, 1f);
        stage.act();
        stage.draw();
        //System.out.println("testing"); //loops @ 60 fps ;)



    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        // Remember to add sizing of everything here or else it won't work
        bottomBar.setSize(Gdx.graphics.getWidth(), 110);
        mainMenu.setSize((float) (stage.getWidth() * .9), (float) (stage.getHeight() * .85));
        mainMenu.setPosition((float) (stage.getWidth() * .05), (float) (stage.getHeight() * .08));
        newGameWindow.setSize((float) (stage.getWidth() * .80), (float) (stage.getHeight() * .85));
        newGameWindow.setPosition((float) (stage.getWidth() * .1), (float) (stage.getHeight() * .08));
        storyLog.setSize((float) (stage.getWidth() * .35), (float) (stage.getHeight() * .83));
        storyLog.setPosition((float) (stage.getWidth() * .01), (float) (stage.getHeight() * .16));
        statsWindow.setSize((float) (stage.getWidth() * .45), (float) (stage.getHeight() * .40));
        statsWindow.setPosition((float) (stage.getWidth() * .368), (float) (stage.getHeight() * .16));
        inventoryWindow.setSize((float) (stage.getWidth() * .35), (float) (stage.getHeight() * .43));
        inventoryWindow.setPosition((float) (stage.getWidth() * .368), (float) (stage.getHeight() * .562));
        mapWindow.setSize((float) (stage.getWidth() * .25), (float) (stage.getHeight() * .43));
        mapWindow.setPosition((float) (stage.getWidth() * .726), (float) (stage.getHeight() * .562));
    }

    @Override
    public void dispose() {
        skin.dispose();
        stage.dispose();
    }


    public String getCurrentCmd() {
        return currentCmd;
    }

    public void setCurrentCmd(String currentCmd) {
        this.currentCmd = currentCmd;
    }

    public String getVersion() {
        return version;
    }

    public class cursorAni implements Runnable {

        @Override
        public void run() {
            Gdx.graphics.setCursor(cursor);
        }

    }
}


