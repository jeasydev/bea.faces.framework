package org.omnifaces.util;

import java.util.HashSet;
import java.util.Set;
import javax.servlet.ServletContext;

/**
 * Utility class for working with (Servlet) resource paths, providing methods to
 * handle their prefixes, extensions etc.
 * 
 * @author Arjan Tijms
 * @since 1.4
 */
public final class ResourcePaths {

    /**
     * Filters away every resource in the given set that has an extension.
     * 
     * @param resources A set of resources to be filtered
     * @return A set where no resource has an extension. May be empty, but never
     *         null.
     */
    public static Set<String> filterExtension(final Set<String> resources) {
        final Set<String> filteredResources = new HashSet<String>();
        for (final String resource : resources) {
            if (ResourcePaths.isExtensionless(resource)) {
                filteredResources.add(resource);
            }
        }

        return filteredResources;
    }

    /**
     * Gets the extension of a resource if any. This extension is defined as
     * everything after the last occurrence of a period, including the period
     * itself. E.g. input "index.xhtml" will return ".xhtml'.
     * 
     * 
     * @param resource
     * @return the extension of the resource, or null if it doesn't have an
     *         extension.
     */
    public static String getExtension(final String resource) {
        String extension = null;
        final int lastPeriod = resource.lastIndexOf('.');
        if (lastPeriod != -1) {
            extension = resource.substring(lastPeriod);
        }

        return extension;
    }

    /**
     * Checks if the given resource path obtained from
     * {@link ServletContext#getResourcePaths(String)} represents a directory.
     * 
     * @param resourcePath the resource path to check
     * @return true if the resource path represents a directory, false otherwise
     */
    public static boolean isDirectory(final String resourcePath) {
        return resourcePath.endsWith("/");
    }

    public static boolean isExtensionless(final String viewId) {
        return viewId != null && !viewId.contains(".");
    }

    /**
     * Strips the extension from a resource if any. This extension is defined as
     * everything after the last occurrence of a period, including the period
     * itself. E.g. input "index.xhtml" will return "index".
     * 
     * @param resource
     * @return the resource without its extension, of as-is if it doesn't have
     *         an extension.
     */
    public static String stripExtension(final String resource) {
        String normalizedResource = resource;
        final int lastPeriod = resource.lastIndexOf('.');
        if (lastPeriod != -1) {
            normalizedResource = resource.substring(0, lastPeriod);
        }

        return normalizedResource;
    }

    /**
     * Strips the special 'faces-views' prefix path from the resource if any.
     * 
     * @param resource
     * @return the resource without the special prefix path, or as-is if it
     *         didn't start with this prefix.
     */
    public static String stripPrefixPath(final String prefix, final String resource) {
        String normalizedResource = resource;
        if (normalizedResource.startsWith(prefix)) {
            normalizedResource = normalizedResource.substring(prefix.length() - 1);
        }

        return normalizedResource;
    }

    private ResourcePaths() {
        // Hide constructor
    }

}
