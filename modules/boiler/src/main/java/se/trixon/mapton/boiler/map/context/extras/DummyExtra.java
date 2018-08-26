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
package se.trixon.mapton.boiler.map.context.extras;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import org.openide.util.lookup.ServiceProvider;
import se.trixon.mapton.core.api.MapContextMenuProvider;

/**
 *
 * @author Patrik Karlström
 */
@ServiceProvider(service = MapContextMenuProvider.class)
public class DummyExtra extends MapContextMenuProvider {

    @Override
    public EventHandler<ActionEvent> getAction() {
        return (ActionEvent event) -> {
            System.out.println("Excute dummy");
        };
    }

    @Override
    public String getName() {
        return "Dummy";
    }

    @Override
    public ContextType getType() {
        return ContextType.EXTRAS;
    }

}