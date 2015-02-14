package larlin.countdownClock;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment
 * must implement the {@link InformationFragment.OnFragmentInteractionListener}
 * interface to handle interaction events. Use the
 * {@link InformationFragment#newInstance} factory method to create an instance
 * of this fragment.
 *
 */
public class InformationFragment extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_ROW_LABELS = "row_labels", ARG_HEADER = "header";
	private static final DateFormat DATE_FORMATER = new SimpleDateFormat("DDD:HH:mm:ss"),
									TIME_FORMATER = new SimpleDateFormat("HH:mm:ss");
	
	private String[] rowLabels;
	private String header;
	
	private ArrayList<TextView> rowDataViews = new ArrayList<TextView>();
	private int rows = 0;
	private TableLayout table;

	private OnFragmentInteractionListener mListener;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 *
	 * @param rowLabels
	 * 				Labels for all data rows.
	 * @return A new instance of fragment InformationFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static InformationFragment newInstance(String header, String... rowLabels) {
		InformationFragment fragment = new InformationFragment();
		Bundle args = new Bundle();
		args.putStringArray(ARG_ROW_LABELS, rowLabels);
		args.putString(ARG_HEADER, header);
		fragment.setArguments(args);
		return fragment;
	}

	public InformationFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			rowLabels = getArguments().getStringArray(ARG_ROW_LABELS);
			header = getArguments().getString(ARG_HEADER);
		}
	}
	
	public void setStringRow(int row, String data){
		TextView dataLabel = rowDataViews.get(row);
		
		dataLabel.setText(data);
	}
	
	public void setDateRow(int row, Date date){
		setStringRow(row, DATE_FORMATER.format(date));
	}
	
	public void setTimeRow(int row, Date time){
		setStringRow(row, TIME_FORMATER.format(time));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View master = inflater
				.inflate(R.layout.fragment_information, container, false);
		
		TextView headerView = (TextView) master.findViewById(R.id.infoHeader);
		headerView.setText(header);
		
		table = (TableLayout) master.findViewById(R.id.infoTable);
		
		for(String label:rowLabels){
			TableRow row = (TableRow) inflater.inflate(R.layout.information_row, table, false);
			((TextView) row.findViewById(R.id.rowLabel)).setText(label);
			rowDataViews.add((TextView) row.findViewById(R.id.rowData));
			table.addView(row);
		}
		return master;
	}

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onFragmentInteraction(uri);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
//		try {
//			mListener = (OnFragmentInteractionListener) activity;
//		} catch (ClassCastException e) {
//			throw new ClassCastException(activity.toString()
//					+ " must implement OnFragmentInteractionListener");
//		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
//		mListener = null;
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated to
	 * the activity and potentially other fragments contained in that activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		public void onFragmentInteraction(Uri uri);
	}

}
