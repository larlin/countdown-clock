package larlin.countdownClock.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

public class CountdownSyncAdapter extends AbstractThreadedSyncAdapter {
	public final static String ACTION_UPDATE_COUNTDOWN = "larlin.countdownClock.action.updateCountdown";
	public final static String EXTRA_STATUS_LABELS = "larlin.countdownClock.statusLabels",
							   EXTRA_EVENTS_LABELS = "larlin.countdownClock.eventsLabels",
							   EXTRA_STATUS_DATA = "larlin.countdownClock.statusData",
							   EXTRA_EVENTS_DATA = "larlin.countdownClock.eventsData",
							   EXTRA_MISSION_NAME = "larlin.countdownClock.eventsData";
	
	private static final String[] TEST_STATUS_LABELS = new String[]{"GMT", "EST", "BUILT-IN-HOLD", "WINDOW TIME REMAINING", "WINDOW OPENING TIME", "EXPECTED LIFTOFF", "WINDOW CLOSE", "L-TIME", "T-TIME"},
			  					  TEST_EVENTS_LABELS = new String[]{"L-TIME", "START PAD CLEAR", "NOMINAL SV POLL", "NEMO ON STATION", "SPACEX CD POLL", "NOMINAL SV POLL", "RDY TO CONTINUE", "NOMINAL SV POLL", "PAD CLEAR COMPLETE", "HE SPIN FILL START", "RNG HOLDFIRE CKS"};
	private static final String TEST_MISSION = "FALCON 9/DSCOVR LCH";


	public CountdownSyncAdapter(Context context, boolean autoInitialize,
			boolean allowParallelSyncs) {
		super(context, autoInitialize, allowParallelSyncs);
	}
	
	public CountdownSyncAdapter(Context context, boolean allowParallelSyncs) {
		super(context, allowParallelSyncs);
	}

	@Override
	public void onPerformSync(Account account, Bundle extras, String authority,
			ContentProviderClient provider, SyncResult syncResult) {
		
		//TODO: Fetching data and parsing it...
		
		//Sending the data out to who ever wants it:
		
		Intent updateIntent = new Intent(ACTION_UPDATE_COUNTDOWN);

		updateIntent.putExtra(EXTRA_STATUS_LABELS, TEST_STATUS_LABELS);
		updateIntent.putExtra(EXTRA_EVENTS_LABELS, TEST_EVENTS_LABELS);
		updateIntent.putExtra(EXTRA_MISSION_NAME, TEST_MISSION);
		
		Log.d("SyncAdapter", "Sending broadcast..");
		
		this.getContext().sendBroadcast(updateIntent);
	}

}
