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

import de.bundesbank.transreg.ui.TransRegVarOutlineView;
import ec.tss.TsInformationType;
import ec.tss.datatransfer.TssTransferSupport;
import java.awt.datatransfer.Transferable;
import javax.swing.JComponent;
import javax.swing.TransferHandler;
import static javax.swing.TransferHandler.COPY;

/**
 *
 * @author s4504gn
 */
public class TransRegTransferHandler extends TransferHandler {

    TransRegVarOutlineView view;

    public TransRegTransferHandler(TransRegVarOutlineView c) {
        view = c;
    }

    @Override
    public int getSourceActions(JComponent c) {
        return COPY;
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
        return super.createTransferable(c);
    }

    @Override
    public boolean canImport(TransferHandler.TransferSupport support) {
        boolean result = TssTransferSupport.getDefault().canImport(support.getDataFlavors());
        if (result && support.isDrop()) {
            support.setDropAction(COPY);
        }
        return result;
    }

    @Override
    public boolean importData(TransferHandler.TransferSupport support) {
        return TssTransferSupport.getDefault()
                .toTsCollectionStream(support.getTransferable())
                .peek(o -> o.load(TsInformationType.All))
                .filter(o -> !o.isEmpty())
                .peek(view::appendTsVariables)
                .count() > 0;

//        long count = TssTransferSupport.getDefault()
//                .toTsCollectionStream(support.getTransferable())
//                .peek(o -> o.load(TsInformationType.All))
//                .filter(o -> !o.isEmpty())
//                .peek(view::appendTsVariables)
//                .count();
//        if (count > 0) {
//            view.refresh();
//            return true;
//        }
//        return false;

    }
}
