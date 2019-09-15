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

import com.dlsc.preferencesfx.model.Category;
import com.dlsc.preferencesfx.model.Group;
import com.dlsc.preferencesfx.model.Setting;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.mapton.core.ui.options.OptionsPanel;
import org.openide.util.NbBundle;
import se.trixon.almond.util.Dict;

/**
 *
 * @author Patrik Karlström
 */
public class MOptionsGeneral {

    private final ResourceBundle mBundle = NbBundle.getBundle(OptionsPanel.class);

    private final Category mCategory;
    private final BooleanProperty mDisplayCrosshairProperty = new SimpleBooleanProperty(true);
    private final BooleanProperty mNightModeProperty = new SimpleBooleanProperty(true);
    private final BooleanProperty mPreferPopoverProperty = new SimpleBooleanProperty(false);

    public MOptionsGeneral() {
        mCategory = Category.of(Dict.GENERAL.toString(),
                Group.of(
                        Dict.LOOK_AND_FEEL.toString(), Setting.of(mBundle.getString("popover"), mPreferPopoverProperty).customKey("general.popover"),
                        Setting.of(Dict.NIGHT_MODE.toString(), mNightModeProperty).customKey("general.nightMode"),
                        Setting.of(mBundle.getString("croshair"), mDisplayCrosshairProperty).customKey("general.crosshair")
                ),
                Group.of()
        );
    }

    public BooleanProperty displayCrosshairProperty() {
        return mDisplayCrosshairProperty;
    }

    public Category getCategory() {
        return mCategory;
    }

    public boolean isDisplayCrosshair() {
        return mDisplayCrosshairProperty.get();
    }

    public boolean isNightMode() {
        return mNightModeProperty.get();
    }

    public boolean isPreferPopover() {
        return mPreferPopoverProperty.get();
    }

    public BooleanProperty nightModeProperty() {
        return mNightModeProperty;
    }

    public BooleanProperty preferPopoverProperty() {
        return mPreferPopoverProperty;
    }

}
