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
package de.bundesbank.transreg.ui.nodes;

import de.bundesbank.transreg.logic.TransRegVar;
import ec.nbdemetra.ui.properties.NodePropertySetBuilder;
import ec.tstoolkit.timeseries.simplets.TsData;
import ec.tstoolkit.timeseries.simplets.TsFrequency;
import ec.tstoolkit.timeseries.simplets.TsPeriod;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.openide.nodes.Children;
import org.openide.nodes.Sheet;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author s4504gn
 */
public class TransRegVarNode extends TransRegVarNodeAdapter {

//    protected Sheet updatedSheet;
    public TransRegVarNode(TransRegVar var) {
        this(var, new InstanceContent());

    }

    private TransRegVarNode(TransRegVar var, InstanceContent content) {
        super(Children.create(
                new TransRegVarChildrenFactory(var.getChildren()), false),
                new AbstractLookup(content));
        content.add(var);
        setName(var.getName());
        updatedSheet = createSheet();  // needs to be set here, because lookup is null in createSheet at startup
        var.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                setSheet(updatedSheet);
            }
        });
    }

    @Override
    protected final Sheet createSheet() {
        Sheet sheet = super.createSheet();
        Sheet.Set set = super.createDefaultSetter();
        sheet.put(set);

        TransRegVar item = getLookup().lookup(TransRegVar.class);
        NodePropertySetBuilder b = new NodePropertySetBuilder();
        sheet.put(getDataSheetSet(item, b));

        return sheet;
    }

    private static Sheet.Set getDataSheetSet(TransRegVar item, NodePropertySetBuilder b) {
        b.reset("Root Input Data");
        TransRegVar root = item.getRoot();

        b.with(String.class).selectConst("Name", root.getDescription(TsFrequency.Undefined)).display("Name").add();
        TsData data = root.getTsData();
        if (data != null) {
            b.withEnum(TsFrequency.class).select(data, "getFrequency", null).display("Frequency").add();
            b.with(TsPeriod.class).select(data, "getStart", null).display("First period").add();
            b.with(TsPeriod.class).select(data, "getLastPeriod", null).display("Last period").add();
            b.withInt().select(data, "getObsCount", null).display("Obs count").add();
            b.with(TsData.class).selectConst("values", data).display("Values").add();
        }
        return b.build();
    }

}
