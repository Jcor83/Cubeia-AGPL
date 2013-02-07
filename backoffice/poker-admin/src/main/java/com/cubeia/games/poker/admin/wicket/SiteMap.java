package com.cubeia.games.poker.admin.wicket;


import com.cubeia.games.poker.admin.wicket.jmx.Clients;
import com.cubeia.games.poker.admin.wicket.pages.history.HandHistory;
import com.cubeia.games.poker.admin.wicket.pages.history.ShowHand;
import com.cubeia.games.poker.admin.wicket.pages.rakes.CreateRake;
import com.cubeia.games.poker.admin.wicket.pages.rakes.EditRake;
import com.cubeia.games.poker.admin.wicket.pages.rakes.ListRakes;
import com.cubeia.games.poker.admin.wicket.pages.tables.CreateTable;
import com.cubeia.games.poker.admin.wicket.pages.tables.EditTable;
import com.cubeia.games.poker.admin.wicket.pages.tables.ListTables;
import com.cubeia.games.poker.admin.wicket.pages.timings.CreateTiming;
import com.cubeia.games.poker.admin.wicket.pages.timings.EditTiming;
import com.cubeia.games.poker.admin.wicket.pages.timings.ListTimings;
import com.cubeia.games.poker.admin.wicket.pages.tournaments.blinds.CreateOrEditBlindsStructure;
import com.cubeia.games.poker.admin.wicket.pages.tournaments.blinds.ListBlindsStructures;
import com.cubeia.games.poker.admin.wicket.pages.tournaments.history.SearchTournamentHistory;
import com.cubeia.games.poker.admin.wicket.pages.tournaments.payouts.CreateOrEditPayoutStructure;
import com.cubeia.games.poker.admin.wicket.pages.tournaments.payouts.ListPayoutStructures;
import com.cubeia.games.poker.admin.wicket.pages.tournaments.scheduled.CreateTournament;
import com.cubeia.games.poker.admin.wicket.pages.tournaments.scheduled.EditTournament;
import com.cubeia.games.poker.admin.wicket.pages.tournaments.scheduled.ListTournaments;
import com.cubeia.games.poker.admin.wicket.pages.tournaments.sitandgo.CreateSitAndGo;
import com.cubeia.games.poker.admin.wicket.pages.tournaments.sitandgo.EditSitAndGo;
import com.cubeia.games.poker.admin.wicket.pages.tournaments.sitandgo.ListSitAndGoTournaments;
import com.cubeia.network.shared.web.wicket.navigation.PageNode;

import java.util.ArrayList;
import java.util.List;

import static com.cubeia.network.shared.web.wicket.navigation.PageNodeUtils.add;
import static com.cubeia.network.shared.web.wicket.navigation.PageNodeUtils.node;
import static com.cubeia.network.shared.web.wicket.navigation.PageNodeUtils.nodeWithChildren;

public class SiteMap {

    private final static List<PageNode> pages = new ArrayList<PageNode>();

    static {
        add(pages, "Home", HomePage.class, "icon-home");

        add(pages,"Hand History", HandHistory.class, "icon-signal",
                node("Show Hand", ShowHand.class,false));

        add(pages,"Tournaments", ListTournaments.class, "icon-list-alt",
                nodeWithChildren("Scheduled Tournaments", ListTournaments.class, "icon-calendar",
                    node("Create tournament", CreateTournament.class, false),
                    node("Edit Tournament", EditTournament.class, false)),
                nodeWithChildren("Sit & Go Tournaments", ListSitAndGoTournaments.class, "icon-tags",
                    node("Create Sit & Go", CreateSitAndGo.class, false),
                    node("Edit Sit & Go", EditSitAndGo.class, false)),
                nodeWithChildren("Blinds Structures", ListBlindsStructures.class, "icon-list-alt",
                        node("Create/Edit Blinds Structure", CreateOrEditBlindsStructure.class, false)),
                nodeWithChildren("Payout Structures", ListPayoutStructures.class, "icon-gift",
                        node("Create/Edit Payout Structure", CreateOrEditPayoutStructure.class, false)),
                node("Tournament History", SearchTournamentHistory.class, "icon-book")
                );

        add(pages,"Table Templates", ListTables.class, "icon-list-alt",
                node("Create Table Template", CreateTable.class),
                node("Edit Table Template", EditTable.class, false));
        add(pages, "Timing Configurations", ListTimings.class, "icon-list-alt",
                node("Create Timing Configuration", CreateTiming.class),
                node("Edit Timing Configuration", EditTiming.class, false));

        add(pages, "Rake Configurations", ListRakes.class, "icon-list-alt",
                node("Create Rake Configuration", CreateRake.class),
                node("Edit Rake Configuration", EditRake.class, false));

        add(pages,"Live Players", Clients.class, "icon-user");


    }


    public static List<PageNode> getPages() {
        return pages;
    }




}
