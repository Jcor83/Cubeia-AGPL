package com.cubeia.backend.cashgame.callback;

import com.cubeia.backend.cashgame.dto.AnnounceTableFailedResponse;
import com.cubeia.backend.cashgame.dto.AnnounceTableResponse;

public interface AnnounceTableCallback {

    void requestSucceeded(AnnounceTableResponse response);

    void requestFailed(AnnounceTableFailedResponse response);
}
