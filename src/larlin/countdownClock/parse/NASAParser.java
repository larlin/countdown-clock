package larlin.countdownClock.parse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;

public class NASAParser {
	//Keys used both in json data and in returned bundles...
	public static final String KEY_KSC = "ksc", KEY_VAFB = "vafb", KEY_TIMES = "times",
							   KEY_GMT = "gmt", KEY_LOCAL = "local", KEY_WINDOW_OPENS = "windowOpens",
							   KEY_EXPECTED = "expected", KEY_CUSTOM = "custom", KEY_LABEL = "label",
							   KEY_TIME = "time", KEY_COUNTDOWNS = "countdowns", KEY_TZ = "tz",
							   KEY_HOLD = "hold", KEY_VEHICLE = "vehicle", KEY_SPACECRAFT = "spacecraft",
							   KEY_EVENTS = "events", KEY_GENERATED = "generated";
	
	private final String keyPrefix;
	
	public NASAParser(String keyPrefix){
		this.keyPrefix = keyPrefix;
	}
	
	/**
	 * Parses all properties of the top level json object. Top level is two sub Bundles
	 * with KEY_KSC and KEY_VAFB. The sub bundle are keyed with the rest of KEY_ publicly
	 * declared in NASAParser.
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public Bundle parse(String json) throws JSONException{
		Bundle result = new Bundle();
		
		JSONObject pads = new JSONObject(json);
		
		JSONObject ksc = pads.getJSONObject(KEY_KSC);
		JSONObject vafb = pads.getJSONObject(KEY_VAFB);
		
		result.putBundle(keyPrefix+"."+KEY_KSC, parsePad(ksc));
		result.putBundle(keyPrefix+"."+KEY_VAFB, parsePad(vafb));
		
		return result;
	}
	
	/**
	 * Parses all properties of the pad JSONObject and return each complex property
	 * as a sub Bundle or primitives properties as corresponding property.
	 * 
	 * Does not currently provide raw properties.
	 * 
	 * @param pad
	 * @return
	 * @throws JSONException
	 */
	private Bundle parsePad(JSONObject pad) throws JSONException{
		Bundle result = new Bundle();
		
		//TODO: Add raw property and parse it.
		
		result.putBundle(keyPrefix+"."+KEY_TIMES, parseTimes(pad.getJSONObject(KEY_TIMES)));
		result.putString(keyPrefix+"."+KEY_TZ, pad.getString(KEY_TZ));
		result.putString(keyPrefix+"."+KEY_HOLD, pad.getString(KEY_HOLD));
		result.putString(keyPrefix+"."+KEY_VEHICLE, pad.getString(KEY_VEHICLE));
		result.putString(keyPrefix+"."+KEY_SPACECRAFT, pad.getString(KEY_SPACECRAFT));
		result.putBundle(keyPrefix+"."+KEY_EVENTS, parseTimeLabelArray(pad.getJSONArray(KEY_EVENTS)));
		result.putLong(keyPrefix+"."+KEY_GENERATED, pad.getLong(KEY_GENERATED));
		
		return result;
	}
	
	/**
	 * Parses a JSONObject that contains gmt, local, windowOpens default properties as
	 * Longs and custom as a JSONArray property.
	 * 
	 * Returns a Bundle with KEY_GMT, KEY_LOCAL, KEY_WINDOW_OPENS with each long and
	 * a KEY_CUSTOMS as a sub Bundle for customs events.
	 *  
	 * @param times
	 * @return
	 * @throws JSONException
	 */
	private Bundle parseTimes(JSONObject times) throws JSONException{
		Bundle result = new Bundle();
		
		result.putLong(KEY_GMT, times.getLong(KEY_GMT));
		result.putLong(KEY_LOCAL, times.getLong(KEY_LOCAL));
		result.putLong(KEY_WINDOW_OPENS, times.getLong(KEY_WINDOW_OPENS));
		
		result.putBundle(KEY_CUSTOM, parseTimeLabelArray(times.getJSONArray(KEY_CUSTOM)));
		result.putBundle(KEY_COUNTDOWNS, parseTimeLabelArray(times.getJSONArray(KEY_COUNTDOWNS)));;
		
		return result;
	}
	
	/**
	 * Parses a JSONArray object representing a array of JSONObject with two properties
	 * a String called label and a Long called time. The result is a bundle with keys
	 * like prefix.custom.key.label and prefix.custom.key.time.
	 * 
	 * Where key is a keyified version of label.
	 * 
	 * @param timeLabelArray
	 * @return
	 * @throws JSONException
	 */
	private Bundle parseTimeLabelArray(JSONArray timeLabelArray) throws JSONException{
		Bundle result = new Bundle();
		
		for(int i = 0; i<timeLabelArray.length();i++){
			//TODO: This solution got really ugly... We have a sea of messy keys...
			// Seems very overkill to create a bundle for each but maybe....
			JSONObject row = timeLabelArray.getJSONObject(i);
			String label = row.getString(KEY_LABEL);
			String key = convertLabelToKey(label);
			row.put(keyPrefix+"."+KEY_CUSTOM+"."+key+"."+KEY_LABEL, label);
			row.put(keyPrefix+"."+KEY_CUSTOM+"."+key+"."+KEY_TIME, row.getLong(KEY_TIME));
		}
		
		return result;
	}
	
	/**
	 * Converts strings like "ACTUAL LIFTOFF" to "actualLiftoff" for use as keys
	 * in bundles and such.
	 * 
	 * @param label
	 * @return
	 */
	private String convertLabelToKey(String label){
		StringBuilder builder = new StringBuilder();
		
		boolean first = true;
		
		for(String part:label.split("\\s")){
			if(first){
				builder.append(part.substring(0, 1).toUpperCase());
			}else{
				builder.append(part.substring(0, 1).toLowerCase());
			}
			builder.append(part.substring(1).toLowerCase());
			first = false;
		}
		return builder.toString();
	}
}
