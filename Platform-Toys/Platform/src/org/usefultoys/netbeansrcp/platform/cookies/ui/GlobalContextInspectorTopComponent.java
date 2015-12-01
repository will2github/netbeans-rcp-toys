/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2015 Daniel Felix Ferber
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.usefultoys.netbeansrcp.platform.cookies.ui;

import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;

/**
 * Top component which displays the current global context. Useful for debugging
 * which cookies are currently exposed by platforms active top component.
 * 
 * @see https://github.com/useful-toys/netbeans-rcp-toys/wiki/Global-Context-Inspector
 */
@TopComponent.Description(
        preferredID = "GlobalContextInspectorTopComponent",
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "output", openAtStartup = false)
@ActionID(category = "Window", id = "org.usefultoys.netbeansrcp.platform.cookies.ui.GlobalContextInspectorTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_ContextAction",
        preferredID = "GlobalContextInspectorTopComponent"
)
@Messages({
    "CTL_ContextAction=Global Context Inspector",
    "CTL_GlobalContextInspectorTopComponent=Global Context Inspector",
    "HINT_GlobalContextInspectorTopComponent=Shows the current global context"
})
public final class GlobalContextInspectorTopComponent extends TopComponent {

    private final org.openide.util.Lookup.Result<Object> globalLookupResult = 
            Utilities.actionsGlobalContext().lookupResult(Object.class);
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

    public GlobalContextInspectorTopComponent() {
        initComponents();
        setName(Bundle.CTL_GlobalContextInspectorTopComponent());
        setToolTipText(Bundle.HINT_GlobalContextInspectorTopComponent());

    }

    @Override
    public void componentOpened() {
        globalLookupResult.addLookupListener(listener);
    }

    @Override
    public void componentClosed() {
        globalLookupResult.removeLookupListener(listener);
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
}