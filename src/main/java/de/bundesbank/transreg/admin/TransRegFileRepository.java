/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
