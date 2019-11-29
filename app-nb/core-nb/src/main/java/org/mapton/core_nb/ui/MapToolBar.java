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
package org.mapton.core_nb.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.prefs.PreferenceChangeEvent;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javax.swing.SwingUtilities;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.PopOver.ArrowLocation;
import org.controlsfx.control.action.Action;
import org.controlsfx.control.action.ActionUtils;
import org.mapton.api.MDict;
import org.mapton.api.MDocumentInfo;
import org.mapton.api.MEngine;
import org.mapton.api.MKey;
import org.mapton.api.MOptions;
import org.mapton.api.Mapton;
import static org.mapton.api.Mapton.getIconSizeContextMenu;
import static org.mapton.api.Mapton.getIconSizeToolBarInt;
import org.mapton.base.ui.AttributionView;
import org.mapton.base.ui.SearchView;
import org.mapton.base.ui.TemporalView;
import org.mapton.base.ui.bookmark.BookmarksView;
import org.mapton.base.ui.grid.GridView;
import org.openide.awt.Actions;
import se.trixon.almond.util.Dict;
import se.trixon.almond.util.GlobalStateChangeEvent;
import se.trixon.almond.util.fx.FxActionSwing;
import se.trixon.almond.util.fx.FxActionSwingCheck;
import se.trixon.almond.util.fx.FxHelper;
import se.trixon.almond.util.icons.material.MaterialIcon;

/**
 *
 * @author Patrik Karlström
 */
public class MapToolBar extends BaseToolBar {

    private Action mAttributionAction;
    private PopOver mAttributionPopOver;
    private AttributionView mAttributionView;
    private Action mBookmarkAction;
    private PopOver mBookmarkPopOver;
    private Action mGridAction;
    private PopOver mGridPopOver;
    private FxActionSwing mHomeAction;
    private Action mLayerAction;
    private PopOver mLayerPopOver;
    private Action mRulerAction;
    private PopOver mRulerPopOver;
    private SearchView mSearchView;
    private Action mStyleAction;
    private PopOver mStylePopOver;
    private FxActionSwingCheck mSysViewMapAction;
    private Action mTemporalAction;
    private PopOver mTemporalPopOver;
    private TemporalView mTemporalView;
    private Action mToolboxAction;
    private PopOver mToolboxPopOver;

    public MapToolBar() {
        initPopOvers();
        initActionsFx();
        initActionsSwing();
        init();
        initListeners();
    }

    public void activateSearch() {
        Platform.runLater(() -> {
            getScene().getWindow().requestFocus();
            mSearchView.getPresenter().requestFocus();
            ((TextField) mSearchView.getPresenter()).clear();
        });
    }

    public void toogleAttributionPopOver() {
        tooglePopOver(mAttributionPopOver, mAttributionAction);
    }

    public void toogleBookmarkPopOver() {
        tooglePopOver(mBookmarkPopOver, mBookmarkAction);
    }

    public void toogleGridPopOver() {
        tooglePopOver(mGridPopOver, mGridAction);
    }

    public void toogleLayerPopOver() {
        tooglePopOver(mLayerPopOver, mLayerAction);
    }

    public void toogleRulerPopOver() {
        tooglePopOver(mRulerPopOver, mRulerAction);
    }

    public void toogleStylePopOver() {
        tooglePopOver(mStylePopOver, mStyleAction);
    }

    public void toogleTemporalPopOver() {
        tooglePopOver(mTemporalPopOver, mTemporalAction);
    }

    public void toogleToolboxPopOver() {
        tooglePopOver(mToolboxPopOver, mToolboxAction);
    }

    void refreshEngine(MEngine engine) {
        mStyleAction.setDisabled(engine.getStyleView() == null);
    }

    private void init() {
        setStyle("-fx-spacing: 0px;");
        setPadding(Insets.EMPTY);

        ArrayList<Action> actions = new ArrayList<>();
        actions.addAll(Arrays.asList(
                mHomeAction,
                mBookmarkAction,
                mLayerAction,
                mGridAction,
                mAttributionAction,
                mStyleAction,
                ActionUtils.ACTION_SPAN,
                mRulerAction,
                mTemporalAction,
                mToolboxAction,
                mSysViewMapAction
        ));

        Platform.runLater(() -> {
            ActionUtils.updateToolBar(this, actions, ActionUtils.ActionTextBehavior.HIDE);

            storeButtonWidths(mStyleAction, mRulerAction, mTemporalAction, mToolboxAction);
            FxHelper.adjustButtonWidth(getItems().stream(), getIconSizeContextMenu() * 1.5);
            setTextFromActions();

            getItems().stream().filter((item) -> (item instanceof ButtonBase))
                    .map((item) -> (ButtonBase) item).forEachOrdered((buttonBase) -> {
                FxHelper.undecorateButton(buttonBase);
            });

            mSearchView = new SearchView();
            getItems().add(1, mSearchView.getPresenter());
        });
    }

    private void initActionsFx() {
        //Bookmark
        mBookmarkAction = new Action(Dict.BOOKMARKS.toString(), event -> {
            if (usePopOver()) {
                if (shouldOpen(mBookmarkPopOver)) {
                    mBookmarkPopOver.show((Node) event.getSource());
                }
            } else {
                SwingUtilities.invokeLater(() -> {
                    Actions.forID("Mapton", "org.mapton.core_nb.actions.BookmarkAction").actionPerformed(null);
                });
            }
        });
        mBookmarkAction.setGraphic(MaterialIcon._Action.BOOKMARK_BORDER.getImageView(getIconSizeToolBarInt()));
        mBookmarkAction.setSelected(mOptions.isBookmarkVisible());
        setTooltip(mBookmarkAction, new KeyCodeCombination(KeyCode.B, KeyCombination.SHORTCUT_DOWN));

        //Layer
        mLayerAction = new Action(Dict.LAYERS.toString(), event -> {
            if (usePopOver()) {
                if (shouldOpen(mLayerPopOver)) {
                    mLayerPopOver.show((Node) event.getSource());
                }
            } else {
                SwingUtilities.invokeLater(() -> {
                    Actions.forID("Mapton", "org.mapton.core_nb.actions.LayerAction").actionPerformed(null);
                });
            }
        });
        mLayerAction.setGraphic(MaterialIcon._Maps.LAYERS.getImageView(getIconSizeToolBarInt()));
        mLayerAction.setSelected(mOptions.isBookmarkVisible());
        setTooltip(mLayerAction, new KeyCodeCombination(KeyCode.L, KeyCombination.SHORTCUT_DOWN));

        //Grid
        mGridAction = new Action(MDict.GRIDS.toString(), event -> {
            if (usePopOver()) {
                if (shouldOpen(mGridPopOver)) {
                    mGridPopOver.show((Node) event.getSource());
                }
            } else {
                SwingUtilities.invokeLater(() -> {
                    Actions.forID("Mapton", "org.mapton.core_nb.actions.GridAction").actionPerformed(null);
                });
            }
        });
        mGridAction.setGraphic(MaterialIcon._Image.GRID_ON.getImageView(getIconSizeToolBarInt()));
        setTooltip(mGridAction, new KeyCodeCombination(KeyCode.G, KeyCombination.SHORTCUT_DOWN));

        //mToolbox
        mToolboxAction = new Action(Dict.TOOLBOX.toString(), event -> {
            if (usePopOver()) {
                if (shouldOpen(mToolboxPopOver)) {
                    mToolboxPopOver.show((Node) event.getSource());
                }
            } else {
                SwingUtilities.invokeLater(() -> {
                    Actions.forID("Mapton", "org.mapton.core_nb.actions.ToolboxAction").actionPerformed(null);
                });
            }
        });
        mToolboxAction.setGraphic(MaterialIcon._Places.BUSINESS_CENTER.getImageView(getIconSizeToolBarInt()));
        setTooltip(mToolboxAction, new KeyCodeCombination(KeyCode.T, KeyCombination.SHORTCUT_DOWN));

        //Style
        mStyleAction = new Action(String.format("%s & %s", Dict.TYPE.toString(), Dict.STYLE.toString()), event -> {
            if (shouldOpen(mStylePopOver)) {
                BorderPane pane = (BorderPane) mStylePopOver.getContentNode();
                pane.setCenter(Mapton.getEngine().getStyleView());
                mStylePopOver.show((Node) event.getSource());
            }
        });
        mStyleAction.setGraphic(MaterialIcon._Image.COLOR_LENS.getImageView(getIconSizeToolBarInt()));
        mStyleAction.setDisabled(true);
        setTooltip(mStyleAction, new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN));

        //Ruler
        mRulerAction = new Action(Dict.RULER.toString(), event -> {
            toogleRulerPopOver();
        });
        mRulerAction.setGraphic(MaterialIcon._Editor.SPACE_BAR.getImageView(getIconSizeToolBarInt()));
        setTooltip(mRulerAction, new KeyCodeCombination(KeyCode.R, KeyCombination.SHORTCUT_DOWN));

        //Temporal
        mTemporalAction = new Action(Dict.Time.DATE.toString(), event -> {
            toogleTemporalPopOver();
        });
        mTemporalAction.setGraphic(MaterialIcon._Action.DATE_RANGE.getImageView(getIconSizeToolBarInt()));
        setTooltip(mTemporalAction, new KeyCodeCombination(KeyCode.D, KeyCombination.SHORTCUT_DOWN));

        //Copyright
        mAttributionAction = new Action("Copyright", event -> {
            if (shouldOpen(mAttributionPopOver)) {
                mAttributionPopOver.show((Node) event.getSource());
            }
        });
        mAttributionAction.setGraphic(MaterialIcon._Action.COPYRIGHT.getImageView(getIconSizeToolBarInt()));
        mAttributionAction.setDisabled(true);
        setTooltip(mAttributionAction, new KeyCodeCombination(KeyCode.I, KeyCombination.SHORTCUT_DOWN));
    }

    private void initActionsSwing() {
        //Home
        mHomeAction = new FxActionSwing(Dict.HOME.toString(), () -> {
            Actions.forID("Mapton", "org.mapton.core_nb.actions.HomeAction").actionPerformed(null);
        });
        mHomeAction.setGraphic(MaterialIcon._Action.HOME.getImageView(getIconSizeToolBarInt()));
        setTooltip(mHomeAction, new KeyCodeCombination(KeyCode.H, KeyCombination.SHORTCUT_DOWN));

        //Map
        mSysViewMapAction = new FxActionSwingCheck(Dict.MAP.toString(), () -> {
            Actions.forID("Mapton", "org.mapton.core_nb.actions.OnlyMapAction").actionPerformed(null);
        });
        mSysViewMapAction.setGraphic(MaterialIcon._Maps.MAP.getImageView(getIconSizeToolBarInt()));
        mSysViewMapAction.setAccelerator(KeyCombination.keyCombination("F12"));
        mSysViewMapAction.setSelected(mOptions.isMapOnly());
    }

    private void initListeners() {
        mOptions.getPreferences().addPreferenceChangeListener((PreferenceChangeEvent evt) -> {
            switch (evt.getKey()) {
                case MOptions.KEY_MAP_ONLY:
                    mSysViewMapAction.setSelected(mOptions.isMapOnly());
                    break;

                default:
                    break;
            }
        });

        Mapton.getGlobalState().addListener((GlobalStateChangeEvent evt) -> {
            Platform.runLater(() -> {
                updateDocumentInfo(evt);
            });
        }, MKey.MAP_DOCUMENT_INFO);
    }

    private void initPopOvers() {
        mBookmarkPopOver = new PopOver();
        initPopOver(mBookmarkPopOver, Dict.BOOKMARKS.toString(), new BookmarksView());

        mGridPopOver = new PopOver();
        initPopOver(mGridPopOver, MDict.GRIDS.toString(), new GridView());

        mLayerPopOver = new PopOver();
        initPopOver(mLayerPopOver, Dict.LAYERS.toString(), null);
        mLayerPopOver.setOnShowing(event -> {
            mLayerPopOver.setContentNode(new LayerView());//TODO Why this?
            setPopOverWidths(FxHelper.getUIScaled(DEFAULT_POP_OVER_WIDTH), mLayerPopOver);
        });

        mStylePopOver = new PopOver();
        initPopOver(mStylePopOver, String.format("%s & %s", Dict.TYPE.toString(), Dict.STYLE.toString()), new BorderPane());

        mToolboxPopOver = new PopOver();
        initPopOver(mToolboxPopOver, Dict.TOOLBOX.toString(), new ToolboxView());
        mToolboxPopOver.setArrowLocation(ArrowLocation.TOP_RIGHT);

        mRulerPopOver = new PopOver();
        initPopOver(mRulerPopOver, Dict.RULER.toString(), new RulerView());
        mRulerPopOver.setArrowLocation(ArrowLocation.TOP_RIGHT);
        mRulerPopOver.setAutoHide(false);
        mRulerPopOver.setCloseButtonEnabled(true);
        mRulerPopOver.setDetachable(true);

        mTemporalPopOver = new PopOver();
        mTemporalView = new TemporalView();
        initPopOver(mTemporalPopOver, Dict.Time.DATE.toString(), mTemporalView);
        mTemporalPopOver.setArrowLocation(ArrowLocation.TOP_RIGHT);
        mTemporalPopOver.setAutoHide(false);
        mTemporalPopOver.setCloseButtonEnabled(true);
        mTemporalPopOver.setDetachable(true);

        setPopOverWidths(FxHelper.getUIScaled(DEFAULT_POP_OVER_WIDTH), mBookmarkPopOver, mGridPopOver, mToolboxPopOver);

        Platform.runLater(() -> {
            mAttributionPopOver = new PopOver();
            mAttributionView = new AttributionView(mAttributionPopOver);
            initPopOver(mAttributionPopOver, Dict.COPYRIGHT.toString(), mAttributionView);
            mAttributionPopOver.setArrowLocation(ArrowLocation.TOP_LEFT);
        });
    }

    private void updateDocumentInfo(GlobalStateChangeEvent evt) {
        MDocumentInfo documentInfo = evt.getValue();
        mAttributionAction.setDisabled(false);
        mStyleAction.setText(documentInfo.getName());
    }
}
