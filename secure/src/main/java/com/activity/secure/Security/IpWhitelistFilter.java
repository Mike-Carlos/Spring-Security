package com.activity.secure.Security;

import com.activity.secure.Config.IpWhitelistConfig;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.apache.commons.net.util.SubnetUtils;
import java.io.IOException;

@Component
@Order(1) // This filter will run before Spring Security filters
public class IpWhitelistFilter implements Filter {

    private final IpWhitelistConfig ipWhitelistConfig;

    public IpWhitelistFilter(IpWhitelistConfig ipWhitelistConfig) {
        this.ipWhitelistConfig = ipWhitelistConfig;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String path = httpRequest.getRequestURI();
        String clientIp = getClientIp(httpRequest);

        // Normalize localhost IP addresses
        if ("0:0:0:0:0:0:0:1".equals(clientIp)) {
            clientIp = "::1";
        }
        if ("127.0.0.1".equals(clientIp)) {
            clientIp = "::1"; // or keep as 127.0.0.1 if you prefer
        }

        // Skip IP check for Swagger and authentication endpoints
        if (path.startsWith("/api/authenticate") ||
                path.startsWith("/swagger-ui") ||
                path.startsWith("/v3/api-docs")) {
            chain.doFilter(request, response);
            return;
        }

        if (!isIpAllowed(clientIp)) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "IP address not allowed");
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isIpAllowed(String ip) {
        // Allow all if whitelist is empty
        if (ipWhitelistConfig.getWhitelist().isEmpty()) {
            return true;
        }

        // Check if IP matches any in whitelist (including CIDR ranges)
        return ipWhitelistConfig.getWhitelist().stream()
                .anyMatch(whitelistIp -> matchesIp(ip, whitelistIp));
    }

    private boolean matchesIp(String ip, String whitelistIp) {
        if (whitelistIp.contains("/")) {
            // Handle CIDR notation
            SubnetUtils subnetUtils = new SubnetUtils(whitelistIp);
            return subnetUtils.getInfo().isInRange(ip);
        } else {
            // Exact match
            return ip.equals(whitelistIp);
        }
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

}