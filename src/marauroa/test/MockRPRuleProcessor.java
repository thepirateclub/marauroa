package marauroa.test;

import java.sql.SQLException;
import java.util.List;

import marauroa.common.Log4J;
import marauroa.common.crypto.Hash;
import marauroa.common.game.RPAction;
import marauroa.common.game.RPObject;
import marauroa.common.game.RPObjectInvalidException;
import marauroa.common.game.RPObjectNotFoundException;
import marauroa.server.game.AccountResult;
import marauroa.server.game.CharacterResult;
import marauroa.server.game.Result;
import marauroa.server.game.db.DatabaseFactory;
import marauroa.server.game.db.IDatabase;
import marauroa.server.game.db.Transaction;
import marauroa.server.game.rp.IRPRuleProcessor;
import marauroa.server.game.rp.RPServerManager;

public class MockRPRuleProcessor implements IRPRuleProcessor{
	/** the logger instance. */
	private static final marauroa.common.Logger logger = Log4J.getLogger(MockRPRuleProcessor.class);

	private IDatabase db;

	public MockRPRuleProcessor() {
		db=DatabaseFactory.getDatabase();
	}

	private static MockRPRuleProcessor rules;

	/**
	 * This method MUST be implemented in other for marauroa to be able to load this World implementation.
	 * There is no way of enforcing static methods on a Interface, so just keep this in mind when
	 * writting your own game.
	 *
	 * @return an unique instance of world.
	 */
	public static IRPRuleProcessor get() {
		if(rules==null) {
			rules = new MockRPRuleProcessor();
		}

		return rules;
	}

	public void beginTurn() {
		// TODO Auto-generated method stub

	}

	public boolean checkGameVersion(String game, String version) {
		TestHelper.assertEquals("TestFramework", game);
		TestHelper.assertEquals("0.00", version);

		logger.info("Client uses:"+game+":"+version);

		return game.equals("TestFramework") && version.equals("0.00");
	}

	public AccountResult createAccount(String username, String password, String email) {
		Transaction trans=db.getTransaction();
		try {
			trans.begin();

			logger.info("Creating account: "+ username);
			if(db.hasPlayer(trans,username)) {
				logger.warn("Account already exist: "+username);
				return new AccountResult(Result.FAILED_PLAYER_EXISTS, username);
			}

			db.addPlayer(trans, username, Hash.hash(password), email);
			logger.info("Account '"+username+"' CREATED");

			trans.commit();
			return new AccountResult(Result.OK_CREATED, username);
		} catch(SQLException e) {
			try {
				trans.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			TestHelper.fail();
			return new AccountResult(Result.FAILED_EXCEPTION, username);
		}
	}

	public CharacterResult createCharacter(String username, String character, RPObject template) {
		// TODO Auto-generated method stub
		return null;
	}

	public void endTurn() {
		// TODO Auto-generated method stub

	}

	public void execute(RPObject object, RPAction action) {
		// TODO Auto-generated method stub

	}

	public boolean onActionAdd(RPObject object, RPAction action, List<RPAction> actionList) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean onExit(RPObject object) throws RPObjectNotFoundException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean onInit(RPObject object) throws RPObjectInvalidException {
		// TODO Auto-generated method stub
		return false;
	}

	public void onTimeout(RPObject object) throws RPObjectNotFoundException {
		// TODO Auto-generated method stub

	}

	public void setContext(RPServerManager rpman) {
		// TODO Auto-generated method stub

	}
}