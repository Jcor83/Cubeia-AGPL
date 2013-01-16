"use strict";
var Poker = Poker || {};


Poker.TablePacketHandler = Class.extend({

    /**
     * @type Poker.TableManager
     */
    tableManager : null,

    tableId : null,

    /**
     * @constructor
     * @param {Number} tableId
     */
    init : function(tableId) {
        this.tableManager = Poker.AppCtx.getTableManager();
        this.tableId = tableId;
    },
    handleSeatInfo:function (seatInfoPacket) {
        console.log(seatInfoPacket);
        console.log("seatInfo pid[" + seatInfoPacket.player.pid + "]  seat[" + seatInfoPacket.seat + "]");
        console.log(seatInfoPacket);
        this.tableManager.addPlayer(seatInfoPacket.tableid,seatInfoPacket.seat, seatInfoPacket.player.pid, seatInfoPacket.player.nick);
    },
    handleNotifyLeave:function (notifyLeavePacket) {
        if (notifyLeavePacket.pid === Poker.MyPlayer.id) {
            console.log("I left this table, closing it.");
            this.tableManager.leaveTable(notifyLeavePacket.tableid);
        } else {
            this.tableManager.removePlayer(notifyLeavePacket.tableid,notifyLeavePacket.pid);
        }
    },

    handleNotifyJoin:function (notifyJoinPacket) {
        this.tableManager.addPlayer(notifyJoinPacket.tableid,notifyJoinPacket.seat, notifyJoinPacket.pid, notifyJoinPacket.nick);
    },
    handleJoinResponse:function (joinResponsePacket) {
        console.log(joinResponsePacket);
        console.log("join response seat = " + joinResponsePacket.seat + " player id = " + Poker.MyPlayer.id);
        if (joinResponsePacket.status == "OK") {
            this.tableManager.addPlayer(joinResponsePacket.tableid,joinResponsePacket.seat, Poker.MyPlayer.id, Poker.MyPlayer.name);
        } else {
            console.log("Join failed. Status: " + joinResponsePacket.status);
        }
    },
    handleUnwatchResponse:function (unwatchResponse) {
        console.log("Unwatch response = ");
        console.log(unwatchResponse);
        this.tableManager.leaveTable(unwatchResponse.tableid);

    },
    handleLeaveResponse:function (leaveResponse) {
        console.log("leave response: ");
        console.log(leaveResponse);
        this.tableManager.leaveTable(leaveResponse.tableid);
        Poker.AppCtx.getViewManager().removeTableView(leaveResponse.tableid);

    },
    handleWatchResponse:function (watchResponse) {
        console.log("WATCH RESPONSE = " + watchResponse);
        if (watchResponse.status == "DENIED_ALREADY_SEATED") {
            new Poker.TableRequestHandler(this.tableId).joinTable();
        } else if (watchResponse.status == "CONNECTED") {
            this.tableManager.clearTable()
        }
    }
});