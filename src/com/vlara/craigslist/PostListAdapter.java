package com.vlara.craigslist;

import java.util.List;

import com.threetaps.model.Posting;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PostListAdapter extends BaseAdapter {
	private final Activity activity;
	Context ctx;
	List<Posting> posts;

	public PostListAdapter(Context context, List<Posting> _posts, Activity activity) {
		this.activity = activity;
		ctx = context;
		posts = _posts;
	}

	@Override
	public int getCount() {
		if (posts != null)
			return posts.size();
		else
			return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return posts.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		PostView ps = null;
		if (rowView == null) {
			LayoutInflater inflator = activity.getLayoutInflater();
			rowView = inflator.inflate(R.layout.post_list_item, null);
			
			ps = new PostView();
			ps.postTitle = (TextView) rowView.findViewById(R.id.posttitle);
			ps.postDay = (TextView) rowView.findViewById(R.id.postday);
			
			rowView.setTag(ps);
		} else {
			ps = (PostView) rowView.getTag();
		}
		Posting currentPost = posts.get(position);
		ps.postTitle.setText(currentPost.getHeading());
		ps.postDay.setText(currentPost.getTimestamp().toString());
		return rowView;
	}

	protected static class PostView {
		protected TextView postTitle;
		protected TextView postDay;
	}

}
