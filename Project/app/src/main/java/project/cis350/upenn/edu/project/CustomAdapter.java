package project.cis350.upenn.edu.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

public class CustomAdapter extends BaseAdapter{
    AllGoalsActivity activity;
    Goal[] goals;

    private static LayoutInflater inflater=null;
    public CustomAdapter(AllGoalsActivity allGoalsActivity, List<Goal> goalsList) {
        activity = allGoalsActivity;
        goals = new Goal[goalsList.size()];
        Iterator<Goal> iter = goalsList.iterator();
        int i = 0;
        while (iter.hasNext()) {
            goals[i] = iter.next();
            i++;
        }
        inflater = ( LayoutInflater )activity.getApplicationContext().
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return goals.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder
    {
        TextView goalName;
        ImageView reasonColor;
        TextView reason;
        TextView totalText;
        ProgressBar totalProgressBar;
        TextView monthText;
        ProgressBar monthProgressBar;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.goals_layout, null);
        holder.goalName = (TextView) rowView.findViewById(R.id.goal_name);
        holder.reasonColor = (ImageView) rowView.findViewById(R.id.reason_color);
        holder.goalName.setText(" " + goals[position].getName());
        holder.reasonColor.setBackgroundColor(goals[position].getReason().hashCode());
        holder.reason = (TextView) rowView.findViewById(R.id.reason);
        holder.reason.setText(" Reason: " + goals[position].getReason());
        holder.totalText = (TextView) rowView.findViewById(R.id.total_progress_text);
        holder.totalText.setText(" Total Progress: " + goals[position].getTotalCompletionPercent());
        holder.totalProgressBar = (ProgressBar) rowView.findViewById(R.id.total_progress_bar);
        holder.totalProgressBar.setProgress((int)(goals[position].getTotalCompletion()*100));
        holder.monthText = (TextView) rowView.findViewById(R.id.total_progress_text);
        holder.monthText.setText(" This Month's Progress: " + goals[position].getMonthlyCompletionPercent(
                Calendar.getInstance().get(Calendar.MONTH)));
        holder.monthProgressBar = (ProgressBar) rowView.findViewById(R.id.month_progres_bar);
        holder.monthProgressBar.setProgress((int)(goals[position].getMonthlyCompletion(
                Calendar.getInstance().get(Calendar.MONTH))*100));
        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.goToSingleGoal(goals[position]);
            }
        });
        return rowView;
    }

}