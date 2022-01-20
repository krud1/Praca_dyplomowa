package com.example.firebase_android;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import Activities.GroupsPositionItemActivity;

public class RecyclerViewAdapter extends FirebaseRecyclerAdapter<GroupsPosition, RecyclerViewAdapter.viewHolder> {

    public RecyclerViewAdapter(@NonNull FirebaseRecyclerOptions<GroupsPosition> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final viewHolder viewHolder, int i, @NonNull final GroupsPosition groupsPosition) {

        viewHolder.item_name.setText(groupsPosition.getItem_name());
        viewHolder.additional_info.setText(groupsPosition.getAdditional_info());
        viewHolder.tableId = groupsPosition.getTableId();
        viewHolder.notificationDate.setText(groupsPosition.getNotificationDate());
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_position_recyclerview, parent,false);
        return new viewHolder(view);
    }

    static class viewHolder extends RecyclerView.ViewHolder{

        TextView item_name, additional_info, notificationDate;
        String tableId;
        public viewHolder(@NonNull View itemView){
            super(itemView);
            notificationDate = itemView.findViewById(R.id.notificationDate);
            item_name = itemView.findViewById(R.id.item_name);
            additional_info = itemView.findViewById(R.id.additional_info);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, GroupsPositionItemActivity.class);
                    intent.putExtra("tableId", tableId);
                    context.startActivity(intent);
                }
            });
        }
    }
}
