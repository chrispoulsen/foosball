package com.dezide.fun.components;

import com.trsvax.bootstrap.annotations.Exclude;
import org.apache.tapestry5.*;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.ioc.annotations.*;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.SymbolConstants;

/**
 * Layout component for pages of application foosball.
 */
@Exclude(stylesheet={"core"})  //If you do not want Tapestry CSS
@Import(stylesheet={
        "classpath:/com/trsvax/bootstrap/assets/bootstrap/css/bootstrap.css",
        "classpath:/com/trsvax/bootstrap/assets/bootstrap/css/bootstrap-responsive.css",
        "context:css/foosball.css"
        },
        library={
                "classpath:/com/trsvax/bootstrap/assets/bootstrap/js/bootstrap.js"
        }
)
public class Layout
{
    /**
     * The page title, for the <title> element and the <h1> element.
     */
    @Property
    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
    private String title;

    @Property
    private String pageName;

    @Property
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String sidebarTitle;

    @Property
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private Block sidebar;

    @Inject
    private ComponentResources resources;

    @Property
    @Inject
    @Symbol(SymbolConstants.APPLICATION_VERSION)
    private String appVersion;


    public String getClassForPageName()
    {
        return resources.getPageName().equalsIgnoreCase(pageName)
                ? "active"
                : null;
    }

    public String[] getPageNames()
    {
        return new String[]{ "Match", "Team", "Person", "Admin"};
    }
}