package de.bundesbank.transreg.settings.admin;

import de.bundesbank.transreg.settings.TransRegSettings;
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
 * @author Nina Gonschorreck
 */

@ServiceProvider(service = IWorkspaceItemRepository.class)
public class TransRegSettingFileRepository extends AbstractFileItemRepository<TransRegSettings>{

    public static final WorkspaceFamily TRANSREGSPEC = parse("TransReg@specifications");

    @Override
    public boolean load(WorkspaceItem<TransRegSettings> item) {
        return loadFile(item, (TransRegSettings o) -> {
            item.setElement(o);
            item.resetDirty();
        });
    }

    @Override
    public boolean save(WorkspaceItem<TransRegSettings> item) {
        return storeFile(item, item.getElement(), item::resetDirty);
    }

    @Override
    public boolean delete(WorkspaceItem<TransRegSettings> doc) {
        return deleteFile(doc);
    }
    
    @Override
    public Class<TransRegSettings> getSupportedType() {
        return TransRegSettings.class;
    }
    @ServiceProvider(service = FamilyHandler.class)
    public static final class TransRegSpec implements FamilyHandler {

        @lombok.experimental.Delegate
        private final FamilyHandler delegate = InformationSetSupport.of(TransRegSettings::new, "TransReg").asHandler(TRANSREGSPEC, FileFormat.GENERIC);
    }
}
