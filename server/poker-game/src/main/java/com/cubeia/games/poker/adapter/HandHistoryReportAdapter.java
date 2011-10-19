package com.cubeia.games.poker.adapter;

import static com.cubeia.games.poker.adapter.HandHistoryTranslator.translate;
import static com.cubeia.poker.adapter.HandEndStatus.CANCELED_TOO_FEW_PLAYERS;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import com.cubeia.firebase.api.game.table.Table;
import com.cubeia.firebase.api.game.table.TablePlayerSet;
import com.cubeia.firebase.guice.inject.Service;
import com.cubeia.games.poker.handhistory.HandHistoryCollectorService;
import com.cubeia.poker.PokerState;
import com.cubeia.poker.action.PokerAction;
import com.cubeia.poker.adapter.HandEndStatus;
import com.cubeia.poker.adapter.ServerAdapter;
import com.cubeia.poker.hand.Card;
import com.cubeia.poker.hand.Rank;
import com.cubeia.poker.handhistory.api.DeckInfo;
import com.cubeia.poker.handhistory.api.HandHistoryEvent;
import com.cubeia.poker.handhistory.api.HandIdentification;
import com.cubeia.poker.handhistory.api.Player;
import com.cubeia.poker.handhistory.api.PlayerAction;
import com.cubeia.poker.handhistory.api.PlayerCardsDealt;
import com.cubeia.poker.handhistory.api.PlayerCardsExposed;
import com.cubeia.poker.handhistory.api.PotUpdate;
import com.cubeia.poker.handhistory.api.Results;
import com.cubeia.poker.handhistory.api.RoundStarted;
import com.cubeia.poker.handhistory.api.TableCardsDealt;
import com.cubeia.poker.player.PokerPlayer;
import com.cubeia.poker.pot.Pot;
import com.cubeia.poker.pot.PotTransition;
import com.cubeia.poker.result.HandResult;
import com.cubeia.poker.result.Result;
import com.google.inject.Inject;

public class HandHistoryReportAdapter extends ServerAdapterProxy {

	@Inject 
	private FirebaseServerAdapter next;
	
	@Service 
	private HandHistoryCollectorService service;
	
	@Inject
	private Table table;
	
	@Inject
	private PokerState state;
	
	@Override
	public ServerAdapter getAdaptee() {
		return next;
	}
	
	@Override
	public void notifyPotUpdates(Collection<Pot> iterable, Collection<PotTransition> potTransitions) {
		super.notifyPotUpdates(iterable, potTransitions);
		PotUpdate ev = new PotUpdate();
		ev.getPots().addAll(translate(iterable));
		post(ev);
	}
	
	@Override
	public void notifyActionPerformed(PokerAction action, long resultingBalance) {
		super.notifyActionPerformed(action, resultingBalance);
		PlayerAction ev = translate(action);
		post(ev);
	}
	
	@Override
	public void exposePrivateCards(int playerId, List<Card> cards) {
		super.exposePrivateCards(playerId, cards);
		PlayerCardsExposed ev = new PlayerCardsExposed(playerId);
		ev.getCards().addAll(translate(cards));
		post(ev);
	}
	
	@Override
	public void notifyCommunityCards(List<Card> cards) {
		super.notifyCommunityCards(cards);
		TableCardsDealt ev = new TableCardsDealt();
		ev.getCards().addAll(translate(cards));
		post(ev);
	}
	
	@Override
	public void notifyDeckInfo(int size, Rank rankLow) {
		super.notifyDeckInfo(size, rankLow);
		service.reportDeckInfo(table.getId(), new DeckInfo(size, translate(rankLow)));
	}
	
	@Override
	public void notifyHandEnd(HandResult handResult, HandEndStatus handEndStatus) {
		super.notifyHandEnd(handResult, handEndStatus);
		if(handEndStatus == CANCELED_TOO_FEW_PLAYERS) {
			service.cancelHand(table.getId());
		} else {
			Map<PokerPlayer, Result> map = handResult.getResults();
			Results res = new Results();
			for (PokerPlayer pl : map.keySet()) {
				res.getResults().put(pl.getId(), translate(map.get(pl)));
			}
			service.reportResults(table.getId(), res);
			service.stopHand(table.getId());
		}
	}
	
	@Override
	public void notifyNewHand() {
		super.notifyNewHand();
		List<Player> seats = getSeatsFromState();
		String tableExtId = getIntegrationTableId();
		String handId = null; // TODO Add hand id...
		HandIdentification id = new HandIdentification(table.getId(), tableExtId, handId);
		this.service.startHand(id, seats); 
	}

	@Override
	public void notifyNewRound() {
		super.notifyNewRound();
		post(new RoundStarted());
	}
	
	@Override
	public void notifyPrivateCards(int playerId, List<Card> cards) {
		super.notifyPrivateCards(playerId, cards);
		PlayerCardsDealt ev = new PlayerCardsDealt(playerId, false);
		ev.getCards().addAll(translate(cards));
		post(ev);
	}
	
	@Override
	public void notifyPrivateExposedCards(int playerId, List<Card> cards) {
		super.notifyPrivateExposedCards(playerId, cards);
		PlayerCardsDealt ev = new PlayerCardsDealt(playerId, true);
		ev.getCards().addAll(translate(cards));
		post(ev);
	}
	
	
	// --- PRIVATE METHODS --- //
	
	private String getIntegrationTableId() {
		return state.getIntegrationId();
	}

	private void post(HandHistoryEvent ev) {
		service.reportEvent(table.getId(), ev);
	}
	
	private List<Player> getSeatsFromState() {
		List<Player> list = new ArrayList<Player>(6);
		SortedMap<Integer, PokerPlayer> plyrs = state.getCurrentHandSeatingMap();
		for (int seat : plyrs.keySet()) {
			PokerPlayer pl = plyrs.get(seat);
			if(!pl.isSittingOut()) {
				String name = getPlayerName(pl.getId());
				Player p = new Player(pl.getId());
				p.setInitialBalance(pl.getBalance());
				p.setName(name);
				p.setSeatId(seat);
				list.add(p);
			}
		}
		return list;
	}

	private String getPlayerName(int id) {
		TablePlayerSet set = table.getPlayerSet();
		return set.getPlayer(id).getName();
	}
}
