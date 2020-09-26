/*
 * Copyright 2020 Patrik Karlström.
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
package org.mapton.addon.files;

import org.mapton.addon.files.api.CoordinateFileManager;
import org.mapton.addon.files.coordinate_file_openers.GeoCoordinateFileOpener;
import org.mapton.addon.files.coordinate_file_openers.KmlCoordinateFileOpener;
import org.mapton.api.Mapton;
import org.openide.modules.OnStart;
import org.openide.windows.WindowManager;
import se.trixon.almond.nbp.Almond;

/**
 *
 * @author Patrik Karlström
 */
@OnStart
public class Initializer implements Runnable {

    public Initializer() {
    }

    @Override
    public void run() {
        WindowManager.getDefault().invokeWhenUIReady(() -> {
            CoordinateFileManager.getInstance().load();
        });

        Mapton.getGlobalState().addListener(gsce -> {
            Almond.openAndActivateTopComponent("FilesTopComponent");
        }, GeoCoordinateFileOpener.class.getName(), KmlCoordinateFileOpener.class.getName()
        );
    }
}