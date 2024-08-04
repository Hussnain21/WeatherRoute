package uk.ac.aston.cs3mdd.weatherroute.database;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import uk.ac.aston.cs3mdd.weatherroute.R;


public class RoutesAdapter extends RecyclerView.Adapter<RoutesAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<String> routes_id, route_name, start_point, end_point;

    public RoutesAdapter(Context context, ArrayList<String> routes_id, ArrayList<String> route_name,
                         ArrayList<String> start_point, ArrayList<String> end_point) {

        this.context = context;
        this.routes_id = routes_id;
        this.route_name = route_name;
        this.start_point = start_point;
        this.end_point = end_point;
    }

    @NonNull
    @Override
    public RoutesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.my_routes, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RoutesAdapter.MyViewHolder holder, int position) {

        holder.routes_id.setText(String.valueOf(routes_id.get(position)));
        holder.route_name.setText(String.valueOf(route_name.get(position)));
        holder.start_point.setText(String.valueOf(start_point.get(position)));
        holder.end_point.setText(String.valueOf(end_point.get(position)));
        holder.deleteButton.setOnClickListener(v -> {
            deleteItem(position);
        });

        holder.editButton.setOnClickListener(v -> {

            showEditPopup(position);
        });

    }

    //gets the id number of the data
    @Override
    public int getItemCount() {
        return routes_id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public Button deleteButton;
        private TextView routes_id, route_name, start_point, end_point;
        private ConstraintLayout mainLayout;
        public Button editButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            routes_id = itemView.findViewById(R.id.routes_id);
            route_name = itemView.findViewById(R.id.route_name);
            start_point = itemView.findViewById(R.id.start_point);
            end_point = itemView.findViewById(R.id.end_point);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            editButton = itemView.findViewById(R.id.editButton);
        }
    }

    //method to delete the data from the database by using the delete item method from mydatabase class
    private void deleteItem(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are you sure you want to delete this route?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String itemIdToDelete = routes_id.get(position);

                MyDatabase myDatabase = new MyDatabase(context);
                myDatabase.deleteItembyId(itemIdToDelete);

                routes_id.remove(position);
                route_name.remove(position);
                start_point.remove(position);
                end_point.remove(position);

                notifyItemRemoved(position);
                notifyDataSetChanged();

                dialog.dismiss();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //method for the edit popup that fetches data from the database and puts it on the text fields to be able to edit
    private void showEditPopup(int position) {

        String routeId = routes_id.get(position);
        String routeName = route_name.get(position);
        String startPoint = start_point.get(position);
        String endPoint = end_point.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);

        View editDialogView = inflater.inflate(R.layout.edit_popup_layout, null);
        builder.setView(editDialogView);

        EditText editRouteName = editDialogView.findViewById(R.id.editRouteName);
        EditText editStartPoint = editDialogView.findViewById(R.id.editStartPoint);
        EditText editEndPoint = editDialogView.findViewById(R.id.editEndPoint);

        editRouteName.setText(routeName);
        editStartPoint.setText(startPoint);
        editEndPoint.setText(endPoint);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String updatedRouteName = editRouteName.getText().toString();
                String updatedStartPoint = editStartPoint.getText().toString();
                String updatedEndPoint = editEndPoint.getText().toString();

                Log.d("CustomAdapter", "Updating item at position: " + position);
                Log.d("CustomAdapter", "Item ID to update: " + routeId);
                Log.d("CustomAdapter", "Item details - Route ID: " + routes_id.get(position) +
                        ", Route Name: " + route_name.get(position) +
                        ", Start Point: " + start_point.get(position) +
                        ", End Point: " + end_point.get(position));

                MyDatabase myDatabase = new MyDatabase(context);
                myDatabase.updateData(routeId, updatedRouteName, updatedStartPoint, updatedEndPoint);

                route_name.set(position, updatedRouteName);
                start_point.set(position, updatedStartPoint);
                end_point.set(position, updatedEndPoint);

                notifyItemChanged(position);

                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog editDialog = builder.create();
        editDialog.show();
    }
}
