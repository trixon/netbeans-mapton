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
package se.trixon.mapton.core.ui.context.open;

import java.util.Locale;
import org.openide.util.lookup.ServiceProvider;
import se.trixon.almond.util.MathHelper;
import se.trixon.mapton.api.MContextMenuItem;

/**
 *
 * @author Patrik Karlström
 */
@ServiceProvider(service = MContextMenuItem.class)
public class Eniro extends MContextMenuItem {

    @Override
    public String getName() {
        return "Eniro";
    }

    @Override
    public ContextType getType() {
        return ContextType.OPEN;
    }

    @Override
    public String getUrl() {
        return String.format(Locale.ENGLISH, "https://kartor.eniro.se/?c=%f,%f&z=%d",
                getLatitude(),
                getLongitude(),
                MathHelper.round(3 + 14 * getZoom())
        );
    }
}