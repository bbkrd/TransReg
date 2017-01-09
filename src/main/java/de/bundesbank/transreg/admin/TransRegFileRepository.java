/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bundesbank.transreg.admin;

import de.bundesbank.transreg.logic.TransRegVar;
import ec.nbdemetra.ws.AbstractFileItemRepository;
import static ec.nbdemetra.ws.AbstractFileItemRepository.loadInfo;
import static ec.nbdemetra.ws.AbstractFileItemRepository.saveInfo;
import ec.nbdemetra.ws.IWorkspaceItemRepository;
import ec.nbdemetra.ws.WorkspaceItem;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author s4504gn
 */
@ServiceProvider(service = IWorkspaceItemRepository.class)
public class TransRegFileRepository extends AbstractFileItemRepository<TransRegDocument> {
    public static final String REPOSITORY = "TransReg";
    
    @Override
    public boolean load(WorkspaceItem<TransRegDocument> item) {
        String sfile = this.fullName(item, REPOSITORY, false);
        if (sfile == null) {
            return false;
        }
        TransRegDocument doc = loadInfo(sfile, TransRegDocument.class);
        item.setElement(doc);
        item.resetDirty();
        return doc != null;
    }

    @Override
    public boolean save(WorkspaceItem<TransRegDocument> item) {
        String sfile = this.fullName(item, REPOSITORY, true);
        if (sfile == null) {
            return false;
        }
        if (saveInfo(sfile, item.getElement())) {
            item.resetDirty();
            item.getElement().resetDirty();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean delete(WorkspaceItem<TransRegDocument> doc) {
        return delete(doc, REPOSITORY);
    }
    
    
    @Override
    public Class<TransRegDocument> getSupportedType() {
        return TransRegDocument.class;
    }
    
}
