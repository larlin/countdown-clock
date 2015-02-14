package larlin.countdownClock;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class Countdown extends Activity {
	
	private static final String[] TEST_STATUS_LABELS = new String[]{"GMT", "EST", "BUILT-IN-HOLD", "WINDOW TIME REMAINING", "WINDOW OPENING TIME", "EXPECTED LIFTOFF", "WINDOW CLOSE", "L-TIME", "T-TIME"},
								  TEST_EVENTS_LABELS = new String[]{"L-TIME", "START PAD CLEAR", "NOMINAL SV POLL", "NEMO ON STATION", "SPACEX CD POLL", "NOMINAL SV POLL", "RDY TO CONTINUE", "NOMINAL SV POLL", "PAD CLEAR COMPLETE", "HE SPIN FILL START", "RNG HOLDFIRE CKS"};
	private static final String TEST_MISSION = "FALCON 9/DSCOVR LCH", TEST_STATUS_HEADER = "MISSION STATUS", TEST_EVENTS_HEADER = "COUNTDOWN EVENTS";
	
	private InformationFragment statusFragment, eventsFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_countdown);
		if (savedInstanceState == null) {
			TextView missionName = (TextView) findViewById(R.id.missionName);
			missionName.setText(TEST_MISSION);
			FragmentTransaction trans = getFragmentManager().beginTransaction();
			statusFragment = InformationFragment.newInstance(TEST_STATUS_HEADER, TEST_STATUS_LABELS);
			eventsFragment = InformationFragment.newInstance(TEST_EVENTS_HEADER, TEST_EVENTS_LABELS);
			trans.add(R.id.container, statusFragment);
			trans.add(R.id.container, eventsFragment);
			trans.commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.countdown, menu);
		Date launchDate = null;
		try {
			launchDate = new SimpleDateFormat("DDD:HH:mm:ss").parse("041:17:46:45");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		statusFragment.setDateRow(0, launchDate);
		statusFragment.setDateRow(1, launchDate);
		statusFragment.setDateRow(4, launchDate);
		statusFragment.setDateRow(5, launchDate);
		statusFragment.setDateRow(6, launchDate);
		statusFragment.setTimeRow(7, launchDate);
		statusFragment.setTimeRow(8, launchDate);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
