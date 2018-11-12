// ICommsTRService.aidl
package com.telefonica.movistarhome.comms;

import com.telefonica.movistarhome.comms.service.CommsCall;
import com.telefonica.movistarhome.comms.service.CommsLastRegisterState;

interface ICommsTRService {
    CommsLastRegisterState getLastRegisterState();
    List<CommsCall> getCallState();
}
