// ICommsTRService.aidl
package com.telefonica.movistarhome.comms;

import com.telefonica.movistarhome.comms.service.CommsCall;

interface ICommsTRService {
    String getParameterValues(String paramName);
    List<CommsCall> getCallState();
}
