package com.joshua.dias.gptutils.orchestration.service;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Registry for managing available tools.
 * This class provides a central point for tool registration and lookup.
 */
@ApplicationScoped
public class ToolRegistry {
    
    private static final Logger LOG = Logger.getLogger(ToolRegistry.class);
    
    private final Map<String, Tool> tools = new HashMap<>();
    
    @Inject
    private Instance<Tool> toolInstances;
    
    /**
     * Initializes the registry by discovering and registering all Tool implementations.
     */
    @PostConstruct
    void init() {
        LOG.info("Initializing ToolRegistry");
        
        // Discover and register all Tool implementations
        for (Tool tool : toolInstances) {
            registerTool(tool);
        }
        
        LOG.info("ToolRegistry initialized with " + tools.size() + " tools");
    }
    
    /**
     * Registers a tool with the registry.
     * 
     * @param tool The tool to register
     */
    public void registerTool(Tool tool) {
        String toolName = tool.getName();
        LOG.info("Registering tool: " + toolName);
        
        if (tools.containsKey(toolName)) {
            LOG.warn("Tool with name '" + toolName + "' is already registered. Overwriting.");
        }
        
        tools.put(toolName, tool);
    }
    
    /**
     * Gets a tool by name.
     * 
     * @param toolName The name of the tool
     * @return The tool, or null if not found
     */
    public Tool getTool(String toolName) {
        return tools.get(toolName);
    }
    
    /**
     * Checks if a tool is registered.
     * 
     * @param toolName The name of the tool
     * @return true if the tool is registered, false otherwise
     */
    public boolean hasTool(String toolName) {
        return tools.containsKey(toolName);
    }
    
    /**
     * Gets the names of all registered tools.
     * 
     * @return A set of tool names
     */
    public Set<String> getToolNames() {
        return Collections.unmodifiableSet(tools.keySet());
    }
    
    /**
     * Gets all registered tools.
     * 
     * @return A map of tool names to tools
     */
    public Map<String, Tool> getAllTools() {
        return Collections.unmodifiableMap(tools);
    }
}