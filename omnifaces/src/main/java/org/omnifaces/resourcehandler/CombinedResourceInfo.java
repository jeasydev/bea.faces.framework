/*
 * Copyright 2012 OmniFaces.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.omnifaces.resourcehandler;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.context.FacesContext;
import org.omnifaces.el.functions.Converters;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Hacks;
import org.omnifaces.util.Utils;

/**
 * This class is a wrapper which collects all combined resources and stores it
 * in the cache. A builder has been provided to create an instance of combined
 * resource info and put it in the cache if absent.
 * 
 * @author Bauke Scholtz
 */
final class CombinedResourceInfo {

    // Constants
    // ------------------------------------------------------------------------------------------------------

    /**
     * Use this builder to create an instance of combined resource info and put
     * it in the cache if absent.
     * 
     * @author Bauke Scholtz
     */
    public static final class Builder {

        // Constants
        // --------------------------------------------------------------------------------------------------

        private static final String ERROR_EMPTY_RESOURCES = "There are no resources been added. Use add() method to add them or use isEmpty() to check beforehand.";

        // Properties
        // -------------------------------------------------------------------------------------------------

        private final Set<ResourceIdentifier> resourceIdentifiers = new LinkedHashSet<ResourceIdentifier>();

        // Actions
        // ----------------------------------------------------------------------------------------------------

        /**
         * Add the resource represented by the given resource identifier
         * resources of this combined resource info. The insertion order is
         * maintained and duplicates are filtered.
         * 
         * @param resourceIdentifier The resource identifier of the resource to
         *            be added.
         * @return This builder.
         */
        public Builder add(final ResourceIdentifier resourceIdentifier) {
            resourceIdentifiers.add(resourceIdentifier);
            return this;
        }

        /**
         * Creates the CombinedResourceInfo instance in cache if absent and
         * return its ID.
         * 
         * @return The ID of the CombinedResourceInfo instance.
         * @throws IllegalStateException If there are no resources been added.
         *             So, to prevent it beforehand, use the {@link #isEmpty()}
         *             method to check if there are any resources been added.
         */
        public String create() {
            if (resourceIdentifiers.isEmpty()) {
                throw new IllegalStateException(Builder.ERROR_EMPTY_RESOURCES);
            }

            return CombinedResourceInfo.get(CombinedResourceInfo.toUniqueId(resourceIdentifiers)).id;
        }

        /**
         * Returns true if there are no resources been added. Use this method
         * before {@link #create()} if it's unknown if there are any resources
         * been added.
         * 
         * @return True if there are no resources been added, otherwise false.
         */
        public boolean isEmpty() {
            return resourceIdentifiers.isEmpty();
        }

    }

    // ConcurrentHashMap was considered, but duplicate inserts technically don't
    // harm and a HashMap is faster on read.
    private static final Map<String, CombinedResourceInfo> CACHE = new HashMap<String, CombinedResourceInfo>();
    private static final String MOJARRA_DEFAULT_RESOURCE_MAX_AGE = "com.sun.faces.defaultResourceMaxAge";
    private static final String MYFACES_DEFAULT_RESOURCE_MAX_AGE = "org.apache.myfaces.RESOURCE_MAX_TIME_EXPIRES";
    private static final long DEFAULT_RESOURCE_MAX_AGE = 604800000L; // 1 week.

    // Properties
    // -----------------------------------------------------------------------------------------------------

    private static final long MAX_AGE = CombinedResourceInfo
        .initMaxAge(CombinedResourceInfo.DEFAULT_RESOURCE_MAX_AGE,
                    CombinedResourceInfo.MOJARRA_DEFAULT_RESOURCE_MAX_AGE,
                    CombinedResourceInfo.MYFACES_DEFAULT_RESOURCE_MAX_AGE);

    /**
     * Create an ordered set of resource identifiers based on the given unique
     * ID. This does the reverse of {@link #toUniqueId(Map)}.
     * 
     * @param id The unique ID of the set of resource identifiers.
     * @return The set of resource identifiers based on the given unique ID, or
     *         <code>null</code> if the ID is not valid.
     */
    private static Set<ResourceIdentifier> fromUniqueId(final String id) {
        String resourcesId;

        try {
            resourcesId = Utils.unserializeURLSafe(id);
        } catch (final IllegalArgumentException e) {
            // This will occur when the ID has purposefully been manipulated for
            // some reason.
            // Just return null then so that it will end up in a 404.
            return null;
        }

        final Set<ResourceIdentifier> resourceIdentifiers = new LinkedHashSet<ResourceIdentifier>();

        for (final String resourceIdentifier : resourcesId.split("\\|")) {
            resourceIdentifiers.add(new ResourceIdentifier(resourceIdentifier));
        }

        return resourceIdentifiers;
    }

    /**
     * Returns the combined resource info identified by the given ID from the
     * cache. A new one will be created based on the given ID if absent in
     * cache.
     * 
     * @param id The ID of the combined resource info to be returned from the
     *            cache.
     * @return The combined resource info identified by the given ID from the
     *         cache.
     */
    public static CombinedResourceInfo get(final String id) {
        CombinedResourceInfo info = CombinedResourceInfo.CACHE.get(id);

        if (info == null) {
            final Set<ResourceIdentifier> resourceIdentifiers = CombinedResourceInfo.fromUniqueId(id);

            if (resourceIdentifiers != null) {
                info = new CombinedResourceInfo(id, Collections.unmodifiableSet(resourceIdentifiers));
                CombinedResourceInfo.CACHE.put(id, info);
            }
        }

        return info;
    }

    /**
     * Initializes the maximum age in milliseconds. The first non-null value
     * associated with the given context parameter names will be returned, else
     * the given default value will be returned.
     * 
     * @param defaultValue The default value.
     * @param initParameterNames The context parameter names to look for any
     *            predefinied maximum age.
     * @return The maximum age in milliseconds.
     */
    @SuppressWarnings("unchecked")
    private static long initMaxAge(final long defaultValue, final String... initParameterNames) {
        final Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getInitParameterMap();
        String param = null;

        for (final String name : initParameterNames) {
            param = params.get(name);

            if (param != null) {
                break;
            }
        }

        if (param == null || !param.matches("\\d+")) {
            return defaultValue;
        } else {
            return Long.valueOf(param);
        }
    }

    /**
     * Create an unique ID based on the given set of resource identifiers. The
     * current implementation converts the set to a <code>|</code>-delimited
     * string which is serialized using {@link Utils#serialize(String)}.
     * 
     * @param resourceIdentifiers The set of resource identifiers to create an
     *            unique ID for.
     * @return The unique ID of the given set of resource identifiers.
     */
    private static String toUniqueId(final Set<ResourceIdentifier> resourceIdentifiers) {
        return Utils.serializeURLSafe(Converters.joinCollection(resourceIdentifiers, "|"));
    }

    // Constructors
    // ---------------------------------------------------------------------------------------------------

    private final String id;

    private final Set<ResourceIdentifier> resourceIdentifiers;

    private Set<Resource> resources;

    // Actions
    // --------------------------------------------------------------------------------------------------------

    private int contentLength;

    private long lastModified;

    /**
     * Creates an instance of combined resource info based on the given ID and
     * ordered set of resource identifiers.
     * 
     * @param resourceIdentifiers Ordered set of resource identifiers, which are
     *            to be combined in a single resource.
     */
    private CombinedResourceInfo(final String id, final Set<ResourceIdentifier> resourceIdentifiers) {
        this.id = id;
        this.resourceIdentifiers = resourceIdentifiers;
    }

    /**
     * Returns true if the given object is also an instance of
     * {@link CombinedResourceInfo} and its ID equals to the ID of the current
     * combined resource info instance.
     */
    @Override
    public boolean equals(final Object other) {
        return (other instanceof CombinedResourceInfo) ? ((CombinedResourceInfo) other).id.equals(id) : false;
    }

    // Getters
    // --------------------------------------------------------------------------------------------------------

    /**
     * Returns the content length in bytes of this combined resource info.
     * 
     * @return The content length in bytes of this combined resource info.
     */
    public int getContentLength() {
        loadResources();
        return contentLength;
    }

    /**
     * Returns the last modified timestamp in milliseconds of this combined
     * resource info.
     * 
     * @return The last modified timestamp in milliseconds of this combined
     *         resource info.
     */
    public long getLastModified() {
        loadResources();
        return lastModified;
    }

    /**
     * Returns the maximum age in milliseconds of this combined resource info.
     * 
     * @return The maximum age in milliseconds of this combined resource info.
     */
    public long getMaxAge() {
        return CombinedResourceInfo.MAX_AGE;
    }

    /**
     * Returns the ordered set of resource identifiers of this combined resource
     * info.
     * 
     * @return the ordered set of resource identifiers of this combined resource
     *         info.
     */
    public Set<ResourceIdentifier> getResourceIdentifiers() {
        return resourceIdentifiers;
    }

    /**
     * Returns the ordered set of resources of this combined resource info.
     * 
     * @return The ordered set of resources of this combined resource info.
     */
    public Set<Resource> getResources() {
        loadResources();
        return resources;
    }

    // Helpers
    // --------------------------------------------------------------------------------------------------------

    /**
     * Returns the sum of the hash code of this class and the ID.
     */
    @Override
    public int hashCode() {
        return getClass().hashCode() + id.hashCode();
    }

    // Helpers
    // ----------------------------------------------------------------------------------------------------

    /**
     * Lazily load the combined resources so that the set of resources, the
     * total content length and the last modified are been initialized. If one
     * of the resources cannot be resolved, then this leaves the resources
     * empty.
     */
    private synchronized void loadResources() {
        if (resources != null) {
            return;
        }

        final FacesContext context = FacesContext.getCurrentInstance();
        final ResourceHandler handler = context.getApplication().getResourceHandler();
        resources = new LinkedHashSet<Resource>();
        contentLength = 0;
        lastModified = 0;

        for (final ResourceIdentifier resourceIdentifier : resourceIdentifiers) {
            final Resource resource = handler.createResource(resourceIdentifier.getName(), resourceIdentifier
                .getLibrary());

            if (resource == null) {
                resources.clear();
                return;
            }

            resources.add(resource);

            try {
                final URLConnection connection = !Hacks.isRichFacesResourceOptimizationEnabled() ? resource.getURL()
                    .openConnection() : new URL(Faces.getRequestDomainURL() + resource.getRequestPath())
                    .openConnection();

                contentLength += connection.getContentLength();
                final long lastModified = connection.getLastModified();

                if (lastModified > this.lastModified) {
                    this.lastModified = lastModified;
                }
            } catch (final IOException e) {
                // Can't and shouldn't handle it at this point.
                // It would be thrown during resource streaming anyway which is
                // a better moment.
            }
        }
    }

    /**
     * Returns the string representation of this combined resource info in the
     * format of
     * 
     * <pre>
     * CombinedResourceInfo[id,resourceIdentifiers]
     * </pre>
     * 
     * Where <code>id</code> is the unique ID and
     * <code>resourceIdentifiers</code> is the ordered set of all resource
     * identifiers as is been created with the builder.
     */
    @Override
    public String toString() {
        return String.format("CombinedResourceInfo[%s,%s]", id, resourceIdentifiers);
    }

}