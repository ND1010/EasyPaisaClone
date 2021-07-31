package com.app.bhaskar.easypaisa.model.piddata.uid;


import com.app.bhaskar.easypaisa.model.piddata.DeviceInfo;
import com.app.bhaskar.easypaisa.model.piddata.Resp;
import com.app.bhaskar.easypaisa.model.piddata.RespMorpho;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;


@Root(name = "PidData")
public class PidDataMorpho {

    public PidDataMorpho() {
    }

    @Element(name = "Resp", required = false)
    public RespMorpho _Resp;

    @Element(name = "DeviceInfo", required = false)
    public DeviceInfo _DeviceInfo;

    @Element(name = "Skey", required = false)
    public Skey _Skey;

    @Element(name = "Hmac", required = false)
    public String _Hmac;

    @Element(name = "Data", required = false)
    public Data _Data;

}
