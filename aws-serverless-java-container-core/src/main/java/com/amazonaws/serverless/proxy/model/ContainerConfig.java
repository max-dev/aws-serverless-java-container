package com.amazonaws.serverless.proxy.model;


import com.amazonaws.serverless.proxy.internal.servlet.AwsProxyHttpServletRequest;


/**
 * Configuration parameters for the framework
 */
public class ContainerConfig {
    public static final String DEFAULT_URI_ENCODING = "UTF-8";

    public static ContainerConfig defaultConfig() {
        ContainerConfig configuration = new ContainerConfig();
        configuration.setStripBasePath(false);
        configuration.setUriEncoding(DEFAULT_URI_ENCODING);
        configuration.setConsolidateSetCookieHeaders(true);
        configuration.setUseStageAsServletContext(false);

        return configuration;
    }

    //-------------------------------------------------------------
    // Variables - Private
    //-------------------------------------------------------------

    private String serviceBasePath;
    private boolean stripBasePath;
    private String uriEncoding;
    private boolean consolidateSetCookieHeaders;
    private boolean useStageAsServletContext;


    //-------------------------------------------------------------
    // Methods - Getter/Setter
    //-------------------------------------------------------------


    /**
     * Returns the base path configured in the container. This configuration variable is used in conjuction with {@link #setStripBasePath(boolean)} to route
     * the request. When requesting the context path from an HttpServletRequest: {@link AwsProxyHttpServletRequest#getContextPath()} this base path is added
     * to the context even though it was initially stripped for the purpose of routing the request. We decided to add it to the context to address GitHub issue
     * #84 and allow framework's link builders to it.
     *
     * @return The base path configured for the container
     */
    public String getServiceBasePath() {
        return serviceBasePath;
    }


    /**
     * Configures a base path  that can be stripped from the request path before passing it to the framework-specific implementation. This can be used to
     * remove API Gateway's base path mappings from the request.
     * @param serviceBasePath The base path mapping to be removed.
     */
    public void setServiceBasePath(String serviceBasePath) {
        // clean up base path before setting it, we want a "/" at the beginning but not at the end.
        String finalBasePath = serviceBasePath;
        if (!finalBasePath.startsWith("/")) {
            finalBasePath = "/" + serviceBasePath;
        }
        if (finalBasePath.endsWith("/")) {
            finalBasePath = finalBasePath.substring(0, finalBasePath.length() - 1);
        }
        this.serviceBasePath = finalBasePath;
    }


    public boolean isStripBasePath() {
        return stripBasePath;
    }


    /**
     * Whether this framework should strip the base path mapping specified with the {@link #setServiceBasePath(String)} method from a request before
     * passing it to the framework-specific implementations
     * @param stripBasePath
     */
    public void setStripBasePath(boolean stripBasePath) {
        this.stripBasePath = stripBasePath;
    }


    public String getUriEncoding() {
        return uriEncoding;
    }


    /**
     * Sets the charset used to URLEncode and Decode request paths.
     * @param uriEncoding The charset. By default this is set to UTF-8
     */
    public void setUriEncoding(String uriEncoding) {
        this.uriEncoding = uriEncoding;
    }


    public boolean isConsolidateSetCookieHeaders() {
        return consolidateSetCookieHeaders;
    }


    /**
     * Tells the library to consolidate multiple Set-Cookie headers into a single Set-Cookie header with multiple, comma-separated values. This is allowed
     * by the RFC 2109 (https://tools.ietf.org/html/rfc2109). However, since not all clients support this, we consider it optional. When this value is set
     * to true the framework will consolidate all Set-Cookie headers into a single header, when it's set to false, the framework will only return the first
     * Set-Cookie header specified in a response.
     *
     * Because API Gateway needs header keys to be unique, we give an option to configure this.
     * @param consolidateSetCookieHeaders Whether to consolidate the cookie headers or not.
     */
    public void setConsolidateSetCookieHeaders(boolean consolidateSetCookieHeaders) {
        this.consolidateSetCookieHeaders = consolidateSetCookieHeaders;
    }


    /**
     * Tells whether the stage name passed in the request should be added to the context path: {@link AwsProxyHttpServletRequest#getContextPath()}.
     * @return true if the stage will be included in the context path, false otherwise.
     */
    public boolean isUseStageAsServletContext() {
        return useStageAsServletContext;
    }


    /**
     * Sets whether the API Gateway stage name should be included in the servlet context path.
     * @param useStageAsServletContext true if you want the stage to appear as the root of the context path, false otherwise.
     */
    public void setUseStageAsServletContext(boolean useStageAsServletContext) {
        this.useStageAsServletContext = useStageAsServletContext;
    }
}
