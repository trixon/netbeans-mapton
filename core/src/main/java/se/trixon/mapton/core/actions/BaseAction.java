/*
 * Copyright 2018 Patrik Karlström.
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
package se.trixon.mapton.core.actions;

import java.awt.event.ActionListener;
import org.openide.awt.Actions;
import org.openide.windows.WindowManager;
import se.trixon.almond.nbp.fx.FxTopComponent;
import se.trixon.mapton.api.MOptions;

/**
 *
 * @author Patrik Karlström
 */
public abstract class BaseAction implements ActionListener {

    protected MOptions mOptions = MOptions.getInstance();

    protected void toggleTopComponent(String id) {
        FxTopComponent tc = (FxTopComponent) WindowManager.getDefault().findTopComponent(id);

        if (mOptions.isMapOnly()) {
            tc.open();
        } else {
            tc.toggleOpened();
        }

        if (tc.isOpened()) {
            tc.requestActive();
        } else {
            Actions.forID("Window", "se.trixon.mapton.core.ui.MapTopComponent").actionPerformed(null);
        }

    }

    protected boolean usePopover() {
        return mOptions.isPreferPopover() || mOptions.isMapOnly();
    }

}