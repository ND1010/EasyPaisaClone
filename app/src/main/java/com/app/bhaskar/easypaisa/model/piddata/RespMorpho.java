package com.app.bhaskar.easypaisa.model.piddata;

import org.simpleframework.xml.Attribute;


public class RespMorpho {

    public RespMorpho() {
    }

    @Attribute(name = "errCode", required = false)
    public String errCode;

    @Attribute(name = "errInfo")
    public String errInfo;

    @Attribute(name = "fCount", required = false)
    public String fCount;

    @Attribute(name = "fType", required = false)
    public String fType;

    @Attribute(name = "iCount", required = false)
    public String iCount;

    @Attribute(name = "iType", required = false)
    public String iType;

    @Attribute(name = "pCount", required = false)
    public String pCount;

    @Attribute(name = "pgCount")
    public String pgCount="";

    @Attribute(name = "pTimeout")
    public String pTimeout="";

    @Attribute(name = "pType", required = false)
    public String pType;

    @Attribute(name = "nmPoints", required = false)
    public String nmPoints;

    @Attribute(name = "qScore", required = false)
    public String qScore;

}
