"use strict";
var Poker = Poker || {};
Poker.HandHistoryRequestHandler = Class.extend({
    tableId : -1,
    init: function (tableId) {
        this.tableId = tableId;
    },
    requestHandIds : function(count) {
        var handIdRequest = new com.cubeia.games.poker.handhistoryservice.io.protocol.HandHistoryProviderRequestHandIds();
        handIdRequest.tableId = this.tableId;
        handIdRequest.count = count;
        handIdRequest.time = "" + new Date().getTime();
        this.sendPacket(handIdRequest);
    },
    requestHands : function(count) {

        var handsRequest = new com.cubeia.games.poker.handhistoryservice.io.protocol.HandHistoryProviderRequestHands();
        handsRequest.tableId = this.tableId;
        handsRequest.count = count;
        handsRequest.time = "" + new Date().getTime();
        this.sendPacket(handsRequest);
    },

    sendPacket : function(historyRequest) {
        var packet = new FB_PROTOCOL.ServiceTransportPacket();
        packet.pid = Poker.MyPlayer.id;
        packet.seq = 0;
        packet.idtype = 0; // namespace
        packet.service = "ns://www.cubeia.com/poker/handhistory/provider-service";
        packet.servicedata = FIREBASE.ByteArray.toBase64String(historyRequest.save().createGameDataArray(historyRequest.classId()));
        Poker.AppCtx.getConnector().sendProtocolObject(packet);
    }

});
