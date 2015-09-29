/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usefultoys.platform.cookies.ui;

import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//org.usefultoys.rcp.platform.cookies.ui//Context//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "ContextTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "output", openAtStartup = false)
@ActionID(category = "Window", id = "org.usefultoys.rcp.platform.cookies.ui.ContextTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_ContextAction",
        preferredID = "ContextTopComponent"
)
@Messages({
    "CTL_ContextAction=Contexto (debug)",
    "CTL_ContextTopComponent=Contexto (debug)",
    "HINT_ContextTopComponent=Validar contexto (debug)"
})
public final class ContextTopComponent extends TopComponent {

//    private final org.openide.util.Lookup.Result<Object> globalLookupResult = Utilities.actionsGlobalContext().lookupResult(Object.class);
    private final org.openide.util.Lookup.Result<Object> globalLookupResult = Utilities.actionsGlobalContext().lookupResult(Object.class);
    private final LookupListener listener = new LookupListener() {
        @Override
        public void resultChanged(LookupEvent ev) {
            StringBuilder sb = new StringBuilder();
            for (Object col : globalLookupResult.allInstances()) {
                sb.append(col.toString()).append("\n");
            }
            jTextPane.setText(sb.toString());
        }
    };

    public ContextTopComponent() {
        initComponents();
        setName(Bundle.CTL_ContextTopComponent());
        setToolTipText(Bundle.HINT_ContextTopComponent());

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane = new javax.swing.JTextPane();

        setLayout(new java.awt.BorderLayout());

        jScrollPane1.setViewportView(jTextPane);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextPane jTextPane;
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        globalLookupResult.addLookupListener(listener);
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }
}