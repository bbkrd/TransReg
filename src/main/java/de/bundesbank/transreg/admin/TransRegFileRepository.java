/* 
 * Copyright 2018 Deutsche Bundesbank
 * 
 * Licensed under the EUPL, Version 1.1 or – as soon they
 * will be approved by the European Commission - subsequent 
 * versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the
 * Licence.
 * You may obtain a copy of the Licence at:
 * 
 * http://ec.europa.eu/idabc/eupl.html
 * 
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the Licence is
 * distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the Licence for the specific language governing
 * permissions and limitations under the Licence.
 */
package de.bundesbank.transreg.admin;

import ec.demetra.workspace.WorkspaceFamily;
import static ec.demetra.workspace.WorkspaceFamily.parse;
import ec.demetra.workspace.file.FileFormat;
import ec.demetra.workspace.file.spi.FamilyHandler;
import ec.demetra.workspace.file.util.InformationSetSupport;
import ec.nbdemetra.ws.AbstractFileItemRepository;
import ec.nbdemetra.ws.IWorkspaceItemRepository;
import ec.nbdemetra.ws.WorkspaceItem;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author s4504gn
 */
@ServiceProvider(service = IWorkspaceItemRepository.class)
public class TransRegFileRepository extends AbstractFileItemRepository<TransRegDocument> {

    public static final WorkspaceFamily TRANSREG = parse("Utilities@TransReg");

    @Override
    public boolean load(WorkspaceItem<TransRegDocument> item) {
        return loadFile(item, (TransRegDocument o) -> {
            item.setElement(o);
            item.resetDirty();
        });
    }

    @Override
    public boolean save(WorkspaceItem<TransRegDocument> item) {
        return storeFile(item, item.getElement(), () -> {
            item.resetDirty();
            item.getElement().resetDirty();
        });
    }

    @Override
    public boolean delete(WorkspaceItem<TransRegDocument> doc) {
        return deleteFile(doc);
    }

    public Class<TransRegDocument> getSupportedType() {
        return TransRegDocument.class;
    }
    
   @ServiceProvider(service = FamilyHandler.class)
    public static final class TransRegs implements FamilyHandler {

        @lombok.experimental.Delegate
        private final FamilyHandler delegate = InformationSetSupport.of(TransRegDocument::new, "TransReg").asHandler(TRANSREG, FileFormat.GENERIC);
    }
}
