//This is source code of favorite. Copyrightⓒ. Tarks. All Rights Reserved.
package com.tarks.favorite;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.actionbarsherlock.app.SherlockFragment;
import com.tarks.favorite.pulltorefresh.library.PullToRefreshBase;
import com.tarks.favorite.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.tarks.favorite.pulltorefresh.library.PullToRefreshListView;
import com.tarks.favorite.connect.AsyncHttpTask;
import com.tarks.favorite.connect.ImageDownloader;
import com.tarks.favorite.global.Global;
import com.tarks.favorite.page.ProfileActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class PagePopularFragment extends SherlockFragment implements
		OnItemLongClickListener {

	// Profile image local path
	String local_path;
	
	View rootView;

	// On déclare la HashMap qui contiendra les informations pour un item
	HashMap<String, String> map;

	// Like me or favorite
	String like_me_result;
	String favorite_result;
	
	//User content
	private ArrayList<String> user_srl_array = new ArrayList<String>();
	private ArrayList<String> user_name_array = new ArrayList<String>();
	// ListView
//	ListView listView;
	private PullToRefreshListView listView;
	// List
	ArrayList<List> m_orders = new ArrayList<List>();
	// Define ListAdapter
	ListAdapter m_adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.listview3, container, false);

		// + "/member/";
		try {
			local_path = getActivity().getCacheDir().toString() + "/member/";
		} catch (Exception e) {
		}

	//	setfavorite();

	
		setListAdapter();

		m_adapter = new ListAdapter(getActivity(), R.layout.profile_list,
				m_orders);
		listView.setAdapter(m_adapter);
	//	loadFavorite(Global.getSetting("user_srl", "0"));
		// Set Favorite

		// Import ListView

		return rootView;
	}
	
	public void refreshAct(){
		//User content
		 user_srl_array.clear();
		 user_name_array.clear();
		m_adapter.clear();
		loadPages(Global.getSetting("user_srl", "0"));
	}

	public void setListAdapter() {
		listView = (PullToRefreshListView) rootView.findViewById(R.id.listView1);

		// Set a listener to be invoked when the list should be refreshed.
		listView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
	refreshAct();
				
			}
		});
//		listView = (ListView) rootView.findViewById(R.id.listView1);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (arg0.getAdapter() instanceof HeaderViewListAdapter) {
					if (((HeaderViewListAdapter) arg0.getAdapter())
							.getWrappedAdapter() instanceof ListAdapter) {
						ListAdapter ca = (ListAdapter) ((HeaderViewListAdapter) arg0
								.getAdapter()).getWrappedAdapter();

						List ls = (List) ca.getItem(arg2 - 1);


						Intent intent = new Intent(getActivity(),
								ProfileActivity.class);
						intent.putExtra("member_srl", ls.getUserSrl());
						startActivity(intent);

				
					}
				}

			}
		});


	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		return false;
	}
	
	private void setListView(String result){
		
		try{
			String[] array = result.split("/LINE/.", user_srl_array.size() +1);
			for (int i = 0; i < user_srl_array.size(); i++) {
				getMemberInfo(String.valueOf(user_srl_array.get(i)));
				setList(user_srl_array.get(i), user_name_array.get(i), array[i]);
			}
		
			listView.onRefreshComplete();
		}catch(Exception e){
			listView.onRefreshComplete();
		}
		
	}
	
	private void setList(String user_srl, String title, String des) {
		m_orders.add(new List(user_srl, title, des, 0));
		m_adapter.notifyDataSetChanged();
	}

	public void loadPages(String user_srl) {

		ArrayList<String> Paramname = new ArrayList<String>();
		Paramname.add("authcode");
		Paramname.add("kind");
		Paramname.add("user_srl");
		Paramname.add("user_srl_auth");
		Paramname.add("start_num");
		Paramname.add("pages_number");

		ArrayList<String> Paramvalue = new ArrayList<String>();
		Paramvalue.add("642979");
		Paramvalue.add("popularity");
		Paramvalue.add(Global.getSetting("user_srl",
				Global.getSetting("user_srl", "0")));
		Paramvalue.add(Global.getSetting("user_srl_auth",
				Global.getSetting("user_srl_auth", "null")));
		Paramvalue.add("0");
		Paramvalue.add("30");

		new AsyncHttpTask(getActivity(), getString(R.string.server_path)
				+ "member/page_read_app.php", mHandler, Paramname,
				Paramvalue, null, 1, Integer.parseInt(user_srl));

	}
	
	public void loadUsers(ArrayList<String> users_srl) {

		ArrayList<String> Paramname = new ArrayList<String>();
		Paramname.add("authcode");
		Paramname.add("kind");
		Paramname.add("user_srl");
		Paramname.add("user_srl_auth");
		Paramname.add("users_srl");

		ArrayList<String> Paramvalue = new ArrayList<String>();
		Paramvalue.add("642979");
		Paramvalue.add("4");
		Paramvalue.add(Global.getSetting("user_srl",
				Global.getSetting("user_srl", "0")));
		Paramvalue.add(Global.getSetting("user_srl_auth",
				Global.getSetting("user_srl_auth", "null")));
		Paramvalue.add(Global.arraylistToString(users_srl, "//"));

		new AsyncHttpTask(getActivity(), getString(R.string.server_path)
				+ "board/documents_app_read.php", mHandler, Paramname,
				Paramvalue, null, 2, 0);

	}
	
	public void getMemberInfo(String user_srl) {
		if (Global.getUpdatePossible(user_srl)) {
			ArrayList<String> Paramname = new ArrayList<String>();
			Paramname.add("authcode");
			Paramname.add("user_srl");
			Paramname.add("user_srl_auth");
			Paramname.add("profile_user_srl");
			Paramname.add("member_info");

			ArrayList<String> Paramvalue = new ArrayList<String>();
			Paramvalue.add("642979");
			Paramvalue.add(Global.getSetting("user_srl",
					Global.getSetting("user_srl", "0")));
			Paramvalue.add(Global.getSetting("user_srl_auth",
					Global.getSetting("user_srl_auth", "null")));
			Paramvalue.add(String.valueOf(user_srl));
			Paramvalue.add("profile_pic//profile_update");

			new AsyncHttpTask(getActivity(), getString(R.string.server_path)
					+ "member/profile_info.php", mHandler, Paramname,
					Paramvalue, null, 3, Integer.parseInt(user_srl));

		}
	}
	
	public void ProfileUserImageDownload(String user_srl) {
		// Start Progressbar
	//	setSupportProgressBarIndeterminateVisibility(true);
		new ImageDownloader(getActivity(), getString(R.string.server_path)
				+ "files/profile/thumbnail/" + user_srl + ".jpg", mHandler, 4,
				Integer.parseInt(user_srl));
	}

	protected Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			// IF Sucessfull no timeout
			// rootView.setSupportProgressBarIndeterminateVisibility(false);
			if (msg.what == -1) {
				Global.ConnectionError(getActivity());
			}

			if (msg.what == 1) {
				String result = msg.obj.toString();
				
			//	Log.i("Result",result);
				try{
				 String[] profile = result.split("/PFILE/.");
					for (int i = 0; i < profile.length; i++) {
						 String[] users = profile[i].split("/LINE/.");
						user_srl_array.add(users[0]);
						user_name_array.add(users[1]);
					//	Log.i("Result",users[0] +  "ddf");
					}
			//	 Global.dumpArray(profile);
				loadUsers(user_srl_array);
				
				}catch(Exception e){
					
				}
              
			}
			
			if (msg.what == 2) {
	          setListView(msg.obj.toString());
			
			}
			
			if (msg.what == 3) {
				try {

					String[] array = msg.obj.toString().split("/LINE/.");
				//	Global.dumpArray(array);
					String profile_pic = array[0];
					String profile_update = array[1];

					String user_srl = String.valueOf(msg.arg1);
			//		Log.i("Gett", user_srl + "");
					Global.SaveUserSetting(user_srl, null, String.valueOf(Global.getCurrentTimeStamp()), profile_pic);
					

					if (profile_pic.matches("Y")) {
						// Global.SaveUserSetting(user_srl, profile_update);
						ProfileUserImageDownload(user_srl);
						// Log.i("test", "Let s profile image download");

					}
					if (profile_pic.matches("N")) {
						File file = new File(local_path + user_srl + ".jpg");
						file.delete();
						File file_thum = new File(local_path + "thumbnail/"
								+ user_srl + ".jpg");
						file_thum.delete();
					}
				} catch (Exception e) {
					// MemberInfoError();

				}
			}
			
			if (msg.what == 4) {
				try {
					//Log.i("Save", msg.arg1 + "");
					Global.SaveBitmapToFileCache((Bitmap) msg.obj, local_path +  "thumbnail/",
							msg.arg1 + ".jpg");

					m_adapter.notifyDataSetChanged();

					// Set Profile
					// profile.setImageDrawable(Drawable.createFromPath(local_path
					// + member_srl + ".jpg"));
					// Refresh();
				} catch (Exception e) {
				}
			}

		}
	};

	private class ListAdapter extends ArrayAdapter<List> {

		private ArrayList<List> items;

		public ListAdapter(Context context, int textViewResourceId,
				ArrayList<List> items) {
			super(context, textViewResourceId, items);
			this.items = items;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getActivity()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.profile_list, null);
			}
			final List p = items.get(position);
			if (p != null) {
				TextView tt = (TextView) v.findViewById(R.id.titre);
				TextView bt = (TextView) v.findViewById(R.id.description);
				ImageView image = (ImageView) v.findViewById(R.id.img);
				if (tt != null) {
					tt.setText(p.getTitle());
				}
				if (bt != null) {
					bt.setText(Global.getValue(p.getDes()));
				}
				if (image != null) {
					boolean state = Global.CheckFileState(local_path
							+ "thumbnail/" + p.getUserSrl() + ".jpg");

					if (state) {
						image.setImageDrawable(Drawable
								.createFromPath(local_path + "thumbnail/"
										+ p.getUserSrl() + ".jpg"));
					} else {
						image.setImageResource(R.drawable.person);
					}
					image.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent = new Intent(getActivity(),
									ProfileActivity.class);
							intent.putExtra("member_srl", p.getUserSrl());
							startActivity(intent);
						}
					});
				}
			}
			return v;
		}
	}

	class List {

		private String user_srl;
		private String Title;
		private String Description;
		private int Tag;
		private int Doc_srl;

		public List(String _user_Srl, String _Title, String _Description,
				int _Tag) {
			this.user_srl = _user_Srl;
			this.Title = _Title;
			this.Description = _Description;
			this.Tag = _Tag;
		}

		public String getUserSrl() {
			return user_srl;
		}

		public String getTitle() {
			return Title;
		}

		public String getDes() {
			return Description;
		}

		public int getTag() {
			return Tag;
		}

		public int getDocSrl() {
			return Doc_srl;
		}

	}
	

	  @Override
	    public void onPause() {
	        super.onPause();

	    }
	  
	  @Override
	    public void onResume() {
	        super.onResume();
	        if(m_adapter.isEmpty()) refreshAct();
	    }
	  
	  
}
