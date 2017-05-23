/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bundesbank.transreg.admin;

import de.bundesbank.transreg.ui.TransRegVarOutlineView;
import ec.tss.TsCollection;
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
public class TransRegTransferHandler extends TransferHandler{
    
    TransRegVarOutlineView view;
    
    public TransRegTransferHandler(TransRegVarOutlineView c){
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
            TsCollection col = TssTransferSupport.getDefault().toTsCollection(support.getTransferable());
            if (col != null) {
                col.query(TsInformationType.All);
                if (!col.isEmpty()) {
                    TransRegVarOutlineView.appendTsVariables(col, view);
                    view.revalidate();
                }
                return true;
            }
            return false;
        }
}