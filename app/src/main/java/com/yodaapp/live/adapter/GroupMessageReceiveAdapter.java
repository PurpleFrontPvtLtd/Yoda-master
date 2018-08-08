package com.yodaapp.live.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.yodaapp.live.R;
import com.yodaapp.live.model.Group_Message_Receive_Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GroupMessageReceiveAdapter extends RecyclerView.Adapter<GroupMessageReceiveAdapter.MyViewHolder> {
	List<Group_Message_Receive_Model> persons;
	String date,contact,message_created,name,chatData;
	String copytext;
	Context context;
	Integer cbPos=0,selctedPos=0;


	public GroupMessageReceiveAdapter(List<Group_Message_Receive_Model> persons, Context context) {
		this.persons = persons;
		Collections.reverse(persons);
		this.context=context;
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_list_list_item_by_same_user, parent, false);
		MyViewHolder myViewHolder = new MyViewHolder(v);
		return myViewHolder;
	}

	@Override
	public void onBindViewHolder(final MyViewHolder holder, final int position) {


		name=persons.get(position).getSent_by();
		chatData=persons.get(position).getMessage();

		SimpleDateFormat srcDf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		try {
			Date date = srcDf.parse(persons.get(position).getCreated());
			SimpleDateFormat destDf = new SimpleDateFormat("MMM dd, hh:mm a", Locale.getDefault());
			message_created = destDf.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		holder.name.setText(name);
		holder.chat_text.setText(chatData);

		holder.chat_text.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {

				copytext = holder.chat_text.getText().toString();

				Log.d("CPYTXT","CPYTXT"+copytext);

				ClipboardManager clipboardManager = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
				ClipData clip = ClipData.newPlainText("copy_data",copytext);

				Toast.makeText(context, "Copied the Text!", Toast.LENGTH_SHORT).show();
				clipboardManager.setPrimaryClip(clip);

				return false;
			}
		});





		holder.messagetime.setText(message_created);
		holder.date.setVisibility(View.GONE);


	}

	@Override
	public int getItemCount() {
		return persons.size();
	}

	@Override
	public void onAttachedToRecyclerView(RecyclerView recyclerView) {
		super.onAttachedToRecyclerView(recyclerView);
	}
	public static class MyViewHolder extends ViewHolder{
		CardView cv;

		TextView name,chat_text,messagetime,date;


		public MyViewHolder(View itemView){
			super(itemView);
			name = (TextView) itemView.findViewById(R.id.chat_name);
			chat_text = (TextView) itemView.findViewById(R.id.chat_text);
			messagetime = (TextView) itemView.findViewById(R.id.messagetime);
			date = (TextView) itemView.findViewById(R.id.datetext);

		}
	}
}


