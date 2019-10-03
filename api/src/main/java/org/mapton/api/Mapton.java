/*
 * Copyright 2019 Patrik Karlström.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mapton.api;

import java.io.File;
import java.text.DateFormat;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.web.WebView;
import javafx.util.Duration;
import javax.swing.SwingUtilities;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.action.Action;
import org.openide.modules.InstalledFileLocator;
import org.openide.util.Lookup;
import se.trixon.almond.util.GlobalState;
import se.trixon.almond.util.fx.FxHelper;
import se.trixon.almond.util.swing.SwingHelper;

/**
 *
 * @author Patrik Karlström
 */
public class Mapton {

    public static final int ICON_SIZE_MODULE = 32;
    public static final int ICON_SIZE_MODULE_TOOLBAR = 40;
    public static final int ICON_SIZE_PROFILE = 32;
    public static final int ICON_SIZE_TOOLBAR = 36;
    public static final int ICON_SIZE_DRAWER = ICON_SIZE_TOOLBAR / 2;
    public static final String LOG_TAG = "Mapton";
    public static final int MODULE_ICON_SIZE = 32;
    public static final double MYLAT = 57.66;
    public static final double MYLON = 12.0;

    private static final File CACHE_DIR;
    private static final File CONFIG_DIR;
    private static final Color ICON_COLOR = Color.BLACK;
    private static final GlobalState sGlobalState = new GlobalState();
    private static MOptions sOptions = MOptions.getInstance();
    private DoubleProperty mZoomProperty;

    static {
        CONFIG_DIR = new File(System.getProperty("netbeans.user"), "mapton-modules");
        CACHE_DIR = new File(FileUtils.getUserDirectory(), ".cache/mapton");
        System.setProperty("mapton.cache", CACHE_DIR.getAbsolutePath());//Used by WorldWind
    }

    public static void applyHtmlCss(WebView webView) {
        String path = "resources/css/attribution_dark.css";
        if (!isNightMode()) {
            path = StringUtils.remove(path, "_dark");
        }

        final String codeNameBase = Mapton.class.getPackage().getName();
        File file = InstalledFileLocator.getDefault().locate(path, codeNameBase, false);
        webView.getEngine().setUserStyleSheetLocation(file.toURI().toString());
        webView.setFontScale(SwingHelper.getUIScale());
    }

    public static Label createTitle(String title) {
        return createTitle(title, Mapton.getThemeBackground());
    }

    public static Label createTitle(String title, Background background) {
        Label label = new Label(title);

        label.setBackground(background);
        label.setAlignment(Pos.BASELINE_CENTER);
        label.setFont(new Font(FxHelper.getScaledFontSize() * 1.2));
        label.setTextFill(Color.WHITE);

        return label;
    }

    public static Label createTitleDev(String title) {
        return createTitle(title + "-dev", FxHelper.createBackground(Color.RED));
    }

    /**
     * Run in the thread of the map engine type
     *
     * @param runnable
     */
    public static void execute(Runnable runnable) {
        if (getEngine().isSwing()) {
            SwingUtilities.invokeLater(runnable);
        } else {
            Platform.runLater(runnable);
        }
    }

    public static File getCacheDir() {
        return CACHE_DIR;
    }

    public static File getConfigDir() {
        return CONFIG_DIR;
    }

    public static DateFormat getDefaultDateFormat() {
        return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM);
    }

    public static MEngine getEngine() {
        MEngine defaultEngine = null;

        for (MEngine mapEngine : Lookup.getDefault().lookupAll(MEngine.class)) {
            if (StringUtils.equalsIgnoreCase(mapEngine.getName(), MOptions.getInstance().getEngine())) {
                return mapEngine;
            } else {
                defaultEngine = mapEngine;
            }
        }

        return defaultEngine;
    }

    public static GlobalState getGlobalState() {
        return sGlobalState;
    }

    public static Color getIconColor() {
        return ICON_COLOR;
    }

    public static int getIconSizeContextMenu() {
        return (int) (getIconSizeToolBar() / 2.0);
    }

    public static int getIconSizeToolBar() {
        return ICON_SIZE_TOOLBAR;
    }

    public static int getIconSizeToolBarInt() {
        return (int) (getIconSizeToolBar() / 1.5);
    }

    public static Mapton getInstance() {
        return Holder.INSTANCE;
    }

    public static Background getThemeBackground() {
        return FxHelper.createBackground(getThemeColor());
    }

    public static Color getThemeColor() {
        return Color.web("#102039");
    }

    public static boolean isNightMode() {
        return MOptions2.getInstance().general().isNightMode();
    }

    public static void logLoading(String category, String item) {
        //NbLog.i("Loading", String.format("%s: %s ", category, item));
        System.out.println(String.format("Loading %s: %s ", category, item));
    }

    public static void logRemoving(String category, String item) {
        //NbLog.i("Removing", String.format("%s: %s ", category, item));
        System.out.println(String.format("Removing %s: %s ", category, item));
    }

    public static void notification(String type, String title, String text) {
        getGlobalState().send(type, Notifications.create().title(title).text(text));
    }

    public static void notification(String type, String title, String text, Action... actions) {
        Notifications notifications = Notifications.create()
                .title(title)
                .text(text)
                .hideAfter(Duration.INDEFINITE)
                .action(actions);

        getGlobalState().send(type, notifications);
    }

    public static void notification(String type, String title, String text, Duration hideDuration, Action... actions) {
        Notifications notifications = Notifications.create()
                .title(title)
                .text(text)
                .hideAfter(hideDuration)
                .action(actions);

        getGlobalState().send(type, notifications);
    }

    private Mapton() {
        //aaa  mZoomProperty = AppStatusView.getInstance().getZoomAbsoluteSlider().valueProperty();
    }

    public DoubleProperty zoomProperty() {
        return mZoomProperty;
    }

    private static class Holder {

        private static final Mapton INSTANCE = new Mapton();
    }
}
