package com.cubeia.backend.cashgame;

import com.cubeia.backend.cashgame.callback.AnnounceTableCallback;
import com.cubeia.backend.cashgame.callback.OpenSessionCallback;
import com.cubeia.backend.cashgame.callback.ReserveCallback;
import com.cubeia.backend.cashgame.dto.AnnounceTableRequest;
import com.cubeia.backend.cashgame.dto.BalanceUpdate;
import com.cubeia.backend.cashgame.dto.BatchHandRequest;
import com.cubeia.backend.cashgame.dto.BatchHandResponse;
import com.cubeia.backend.cashgame.dto.CloseSessionRequest;
import com.cubeia.backend.cashgame.dto.OpenSessionRequest;
import com.cubeia.backend.cashgame.dto.ReserveRequest;
import com.cubeia.backend.cashgame.exceptions.BatchHandFailedException;
import com.cubeia.backend.cashgame.exceptions.CloseSessionFailedException;
import com.cubeia.backend.cashgame.exceptions.GetBalanceFailedException;

/**
 * Cash game backend abstraction. 
 * 
 * This interface contains methods needed by a cash game (poker for example) 
 * when interacting with a backend system (wallet). 
 * 
 * Methods annotated with {@link Async} are asynchronous and will return 
 * immediately and respond to the callback when finished. 
 * 
 * @author w
 */
public interface CashGamesBackend {
	
    /**
     * Returns true if the system is shutting down.
     * @return true if shutting down
     */
	boolean isSystemShuttingDown();
	
	/**
	 * Generate a new hand ID. This method is synchronous and
	 * should be implemented to return as swiftly as possible as it
	 * will be called between all hands. 
	 * 
	 * @return A new hand ID, never null
	 */
	String generateHandId();
	
	/**
	 * Returns true if the player is allowed to join tables.
	 * @param playerId player
	 * @return true if allowed
	 */
	AllowJoinResponse allowJoinTable(int playerId);

    /**
     * Asynchronous call to announce a table created by the game. 
     * This call can be used if tables needs to be populated with external
     * data (external table ids for example) before use.
     * @param request announce request
     * @param callback called when done
     */
	@Async
	void announceTable(AnnounceTableRequest request, AnnounceTableCallback callback);

	/**
	 * Open a player session.
	 * @param request 
	 * @param callback
	 */
	@Async
	void openSession(OpenSessionRequest request, OpenSessionCallback callback);

	/**
	 * Close a player session previously opened with {@link #openSession(OpenSessionRequest, OpenSessionCallback)}.
	 * @param request
	 * @throws CloseSessionFailedException
	 */
	void closeSession(CloseSessionRequest request) throws CloseSessionFailedException;

	/**
	 * Reserve currency for a game. An open session is needed.
	 * @param request
	 * @param callback
	 */
	@Async
	void reserve(ReserveRequest request, ReserveCallback callback);

	/**
	 * Report the result of a hand.
	 * @param request
	 * @return
	 * @throws BatchHandFailedException
	 */
	BatchHandResponse batchHand(BatchHandRequest request) throws BatchHandFailedException;

	/**
	 * Returns the main account balance for the given player. This amount
	 * does not include currency locked up in open sessions.
	 * @param playerId
	 * @return
	 * @throws GetBalanceFailedException
	 */
	long getMainAccountBalance(int playerId) throws GetBalanceFailedException;
	
	/**
	 * Returns the balance of the given session.
	 * @param sessionId
	 * @return
	 * @throws GetBalanceFailedException
	 */
	BalanceUpdate getSessionBalance(PlayerSessionId sessionId) throws GetBalanceFailedException;
}
