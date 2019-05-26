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
package org.mapton.core.ui;

import java.util.LinkedHashMap;
import java.util.Map;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.control.PropertySheet.Item;
import org.mapton.api.MKey;
import org.mapton.api.MMapMagnet;
import org.mapton.api.MTopComponent;
import org.mapton.api.Mapton;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.windows.TopComponent;
import se.trixon.almond.util.Dict;
import se.trixon.almond.util.GlobalStateChangeEvent;
import se.trixon.almond.util.fx.control.LogPanel;

/**
 * Generic Property TopComponent
 */
@ConvertAsProperties(
        dtd = "-//org.mapton.core.ui//ObjectProperties//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "ObjectPropertiesTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE",
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "properties", openAtStartup = false)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_ObjectPropertiesAction",
        preferredID = "ObjectPropertiesTopComponent"
)
public final class ObjectPropertiesTopComponent extends MTopComponent implements MMapMagnet {

    private final Map<String, Object> mDummyMap = new LinkedHashMap<>();
    private LogPanel mLogPanel;
    private PropertySheet mPropertySheet;
    private BorderPane mRoot;

    public ObjectPropertiesTopComponent() {
        setName(Dict.OBJECT_PROPERTIES.toString());
    }

    @Override
    protected void initFX() {
        setScene(createScene());
        initListeners();
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    private Scene createScene() {
        mPropertySheet = new PropertySheet();
        mPropertySheet.setMode(PropertySheet.Mode.NAME);
        mLogPanel = new LogPanel();
        mRoot = new BorderPane(mPropertySheet);

        return new Scene(mRoot);
    }

    private void initListeners() {
        Mapton.getGlobalState().addListener((GlobalStateChangeEvent evt) -> {
            Platform.runLater(() -> {
                mRoot.setCenter(mPropertySheet);
                Object o = evt.getValue();

                if (o == null) {
                    mPropertySheet.getItems().clear();
                } else if (o.getClass().isInstance(mPropertySheet.getItems())) {
                    loadList(evt.getValue());
                } else if (o.getClass().isInstance(mDummyMap)) {
                    loadMap(evt.getValue());
                } else if (o instanceof Node) {
                    mRoot.setCenter(evt.getValue());
                } else if (o instanceof String) {
                    load(evt.getValue());
                } else {
                    load(ToStringBuilder.reflectionToString(o, ToStringStyle.MULTI_LINE_STYLE));
                }
            });
        }, MKey.OBJECT_PROPERTIES);
    }

    private void load(String text) {
        mRoot.setCenter(mLogPanel);
        mLogPanel.setText(text);
    }

    private void loadList(ObservableList<Item> propertyItems) {
        mPropertySheet.getItems().setAll(propertyItems);
    }

    private void loadMap(Map<String, Object> propertiesMap) {
        ObservableList<Item> propertyItems = FXCollections.observableArrayList();
        propertiesMap.keySet().forEach((key) -> {
            propertyItems.add(new MPropertyItem(propertiesMap, key));
        });

        loadList(propertyItems);
    }

}