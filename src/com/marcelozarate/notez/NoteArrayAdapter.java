package com.marcelozarate.notez;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class NoteArrayAdapter extends ArrayAdapter<Note> {
	private Context contexto;
	
	public NoteArrayAdapter(Context context, int resourceId, List<Note> items){
		super(context, resourceId, items);
		contexto = context;
	}

	/*private view holder class*/
    private static class ViewHolder {
        TextView txtTitle;
        TextView txtDate;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Note noteItem = getItem(position);
         
        LayoutInflater mInflater = (LayoutInflater) contexto
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null || !(convertView.getTag() instanceof ViewHolder)) {
            convertView = mInflater.inflate(R.layout.note_layout, null);
            holder = new ViewHolder();
            holder.txtTitle = (TextView) convertView.findViewById(R.id.titlePreview);
            holder.txtDate = (TextView) convertView.findViewById(R.id.datePreview);
            
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
 
        holder.txtTitle.setText(noteItem.getTitle());
        holder.txtDate.setText(noteItem.getDate());
        
        // holder.imageView.setImageResource(rowItem.getImageId());
 
        return convertView;
    }
	
}
