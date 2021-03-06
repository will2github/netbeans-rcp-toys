/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usefultoys.netbeansrcp.platform.cookies.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.JToolBar;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ProxyLookup;
import org.usefultoys.netbeansrcp.platform.cookies.api.CookieService;
import org.usefultoys.netbeansrcp.platform.cookies.api.TopComponentCookieContext;
import org.usefultoys.netbeansrcp.platform.cookies.spi.LocalAndSelectionCookieProvider;
import org.usefultoys.netbeansrcp.platform.cookies.spi.LocalCookieProvider;
import org.usefultoys.netbeansrcp.platform.cookies.spi.SelectionCookieProvider;
import org.usefultoys.netbeansrcp.platform.cookies.core.ToolbarBuilder;

public class TopComponentCookieContextImpl implements TopComponentCookieContext {

    private static final Logger logger = Logger.getLogger(CookieService.class.getName());

    private final InstanceContent localContent = new InstanceContent();
    private final Lookup localLookup = new AbstractLookup(localContent);
    private final Lookup globalLookup;
    private final Lookup actionsLocalContext;

    private final Map<String, Object> localMap = new TreeMap<>();
    private final Set<Object> localSet = new HashSet<>();
    private final Set<Object> cookieSet = new HashSet<>();

    private final Map<String, Object> selectionMap = new TreeMap<>();
    private final Set<Object> selectionSet = new HashSet<>();

    private final CookieServiceImpl parent;

    public TopComponentCookieContextImpl(CookieServiceImpl parent) {
        this.parent = parent;
        this.globalLookup = new ProxyLookup(localLookup, parent.getStaticLookup());
        this.actionsLocalContext = new LookupWrapper(globalLookup);
    }

    @Override
    public Lookup getLookup() {
        return globalLookup;
    }

    @Override
    public Lookup getLocalLookup() {
        return globalLookup;
    }

    @Override
    public TopComponentCookieContext setSelection(Map<String, ? extends Object> newMap, Set<Object> newSet) {
        selectionMap.clear();
        if (newMap != null) {
            selectionMap.putAll(newMap);
        }
        selectionSet.clear();
        if (newSet != null) {
            selectionSet.addAll(newSet);
        }
        return this;
    }

    @Override
    public TopComponentCookieContext setLocalMap(Map<String, ? extends Object> newMap) {
        localMap.clear();
        if (newMap != null) {
            localMap.putAll(newMap);
        }
        return this;
    }

    @Override
    public TopComponentCookieContext setLocalSet(Set<Object> newSet) {
        localSet.clear();
        if (newSet != null) {
            localSet.addAll(newSet);
        }
        return this;
    }

    @Override
    public TopComponentCookieContext setCookieSet(Set<Object> newSet) {
        cookieSet.clear();
        if (newSet != null) {
            cookieSet.addAll(newSet);
        }
        return this;
    }

    @Override
    public TopComponentCookieContext clearLocalMap() {
        localMap.clear();
        return this;
    }

    @Override
    public TopComponentCookieContext clearSelection() {
        selectionMap.clear();
        selectionSet.clear();
        return this;
    }

    @Override
    public TopComponentCookieContext clearLocalSet() {
        localSet.clear();
        return this;
    }

    @Override
    public TopComponentCookieContext clearCookies() {
        cookieSet.clear();
        return this;
    }

    @Override
    public TopComponentCookieContext addLocal(String ket, Object newObject) {
        localMap.put(ket, newObject);
        return this;
    }

    @Override
    public TopComponentCookieContext addLocal(Object newObject) {
        localSet.add(newObject);
        return this;
    }

    @Override
    public TopComponentCookieContext addCookie(Object newObject) {
        cookieSet.add(newObject);
        return this;
    }

    @Override
    public TopComponentCookieContext addCookie(Object... newCookies) {
        cookieSet.addAll(Arrays.asList(newCookies));
        return this;
    }

    @Override
    public TopComponentCookieContext removeLocal(String ket, Object object) {
        localMap.remove(ket, object);
        return this;
    }

    @Override
    public TopComponentCookieContext removeLocal(Object object) {
        localSet.remove(object);
        return this;
    }

    @Override
    public TopComponentCookieContext removeCookie(Object object) {
        cookieSet.remove(object);
        return this;
    }

    @Override
    public TopComponentCookieContext removeCookie(Object... cookies) {
        cookieSet.remove(Arrays.asList(cookies));
        return this;
    }

    @Override
    public TopComponentCookieContext apply() {
        Collection<? extends LocalAndSelectionCookieProvider> localAndSelectionProviders = Lookup.getDefault().lookupAll(LocalAndSelectionCookieProvider.class);
        Collection<? extends SelectionCookieProvider> selectionProviders = Lookup.getDefault().lookupAll(SelectionCookieProvider.class);
        Collection<? extends LocalCookieProvider> localProviders = Lookup.getDefault().lookupAll(LocalCookieProvider.class);

        List<Object> cookies = new ArrayList<>();
        boolean cookiesCreated = false;
        for (LocalAndSelectionCookieProvider provider : localAndSelectionProviders) {
            cookiesCreated |= provider.createLocalAndSelectionCookies(localMap, localSet, selectionMap, selectionSet, cookies);
        }
        for (SelectionCookieProvider provider : selectionProviders) {
            cookiesCreated |= provider.createSelectionCookies(selectionMap, selectionSet, cookies);
        }
        for (LocalCookieProvider provider : localProviders) {
            cookiesCreated |= provider.createLocalCookies(localMap, localSet, cookies);
        }
        cookiesCreated |= cookies.addAll(cookieSet);
        if (cookiesCreated) {
            logger.info("Set " + cookies.size() + " local or selection cookies");
            localContent.set(cookies, null);
        } else {
            logger.info("Set zero local or selection cookies");
            localContent.set(Collections.EMPTY_LIST, null);
        }
        return this;
    }

    @Override
    public Lookup actionsLocalContext() {
        return actionsLocalContext;
    }

    @Override
    public void populateToolbar(JToolBar toolbar, List<? extends Action> actions) {
        ToolbarBuilder.build(toolbar, actions, this.globalLookup);
    }

    @Override
    public void populateToolbar(JToolBar toolbar, String actionsPath) {
        ToolbarBuilder.build(toolbar, actionsPath, this.globalLookup);
    }

    @Override
    public void update() {
        this.apply();
    }
}
