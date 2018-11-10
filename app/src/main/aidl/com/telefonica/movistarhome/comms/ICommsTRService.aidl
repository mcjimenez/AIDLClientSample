// ICommsTRService.aidl
package com.telefonica.movistarhome.comms;

import com.telefonica.movistarhome.comms.service.CommsTRCall;

interface ICommsTRService {
    String getParameterValues(String paramName);
    List<CommsTRCall> getCallState();
}
