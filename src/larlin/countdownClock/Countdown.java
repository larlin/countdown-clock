package larlin.countdownClock;

import larlin.countdownClock.sync.CountdownSyncAdapter;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class Countdown extends Activity {
	
	private static final String TEST_STATUS_HEADER = "MISSION STATUS", TEST_EVENTS_HEADER = "COUNTDOWN EVENTS";
	
    // Constants
    // The authority for the sync adapter's content provider
    public static final String AUTHORITY = "larlin.countdownClock.stubProvider";
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "larlin.stub";
    // The account name
    public static final String ACCOUNT = "stubAccount";
    
    private Account stubAccount;
	
	private InformationFragment statusFragment, eventsFragment;
	private TextView missionNameLabel;
	

	private int test = 1;
	
	//Broadcast recevier for reciving broadcasts with data from SyncAdapter.
	//TODO: We should probably change this to using bounding instead.
	private BroadcastReceiver bReceiver = new BroadcastReceiver() {
	    @Override
	    public void onReceive(Context context, Intent intent) {
	    	Log.d("Countdonw", "Recived broadcast!");
	        if(intent.getAction().equals(CountdownSyncAdapter.ACTION_UPDATE_COUNTDOWN)) {
	        	setMissionName(intent.getStringExtra(CountdownSyncAdapter.EXTRA_MISSION_NAME));
	        	if(!fragmentsCreated()){
	        		Log.d("Countdown", "creating fragments!");
	        		createFragments(intent.getStringArrayExtra(CountdownSyncAdapter.EXTRA_STATUS_LABELS),
	        						intent.getStringArrayExtra(CountdownSyncAdapter.EXTRA_EVENTS_LABELS));
	        	}
	        }
	    }
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_countdown);
		if (savedInstanceState == null) {
			missionNameLabel = (TextView) findViewById(R.id.missionName);
		}
		
		Log.d("Countdown", "Registering breciver");
		IntentFilter intentFilter = new IntentFilter(CountdownSyncAdapter.ACTION_UPDATE_COUNTDOWN);
		registerReceiver(bReceiver, intentFilter);
		
		stubAccount = createSyncAccount(this);
        // Turn on automatic syncing for the default account and authority
        ContentResolver.setSyncAutomatically(stubAccount, AUTHORITY, true);
	}
	
	private void setMissionName(String missionName){
		missionNameLabel.setText(missionName);
	}
	
	private void createFragments(String[] statusLabels, String[] eventsLabels){
		Log.d("Countdonw", "Status: "+statusLabels.length+" events: "+eventsLabels.length);
		FragmentTransaction trans = getFragmentManager().beginTransaction();
		statusFragment = InformationFragment.newInstance(TEST_STATUS_HEADER, statusLabels);
		eventsFragment = InformationFragment.newInstance(TEST_EVENTS_HEADER, eventsLabels);
		trans.add(R.id.container, statusFragment);
		trans.add(R.id.container, eventsFragment);
		trans.commit();
	}
	
	private boolean fragmentsCreated(){
		return statusFragment != null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.countdown, menu);
//		Date launchDate = null;
//		try {
//			launchDate = new SimpleDateFormat("DDD:HH:mm:ss").parse("041:17:46:45");
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		statusFragment.setDateRow(0, launchDate);
//		statusFragment.setDateRow(1, launchDate);
//		statusFragment.setDateRow(4, launchDate);
//		statusFragment.setDateRow(5, launchDate);
//		statusFragment.setDateRow(6, launchDate);
//		statusFragment.setTimeRow(7, launchDate);
//		statusFragment.setTimeRow(8, launchDate);
		
		//This is just for test purposes I don't know how often the automatic sync
		//will trigger so this is a way to at least get one sync when the app starts.
        // Pass the settings flags by inserting them in a bundle
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        /*
         * Request the sync for the default account, authority, and
         * manual sync settings
         */
        Log.d("Countdown", "requesting sync");
        ContentResolver.requestSync(stubAccount, AUTHORITY, settingsBundle);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		eventsFragment.setMarkedRow(test++);
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
    /**
     * Create a new dummy account for the sync adapter
     *
     * @param context The application context
     */
    public static Account createSyncAccount(Context context) {
        // Create the account type and default account
        Account newAccount = new Account(
                ACCOUNT, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(
                        ACCOUNT_SERVICE);
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
        	return newAccount;
        } else {
            /*
             * The account exists or some other error occurred. Log this, report it,
             * or handle it internally.
             */
        	Account[] accounts = accountManager.getAccountsByType(ACCOUNT_TYPE);
        	if(accounts != null && accounts.length > 0){
        		return accounts[0];
        	}
        	return null;
        }
    }
    
    @Override
    protected void onDestroy() {
      // Unregister since the activity is about to be closed.
      unregisterReceiver(bReceiver);
      super.onDestroy();
    }

}
