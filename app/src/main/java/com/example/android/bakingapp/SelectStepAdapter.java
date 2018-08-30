package com.example.android.bakingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingapp.dataobjects.Steps;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectStepAdapter extends RecyclerView.Adapter<SelectStepAdapter.ViewHolder> {
    public final StepItemClickListener mClickHandler;
    ArrayList<Steps> mSteps;
    Context mContext;
    private int stepId;

    SelectStepAdapter(Context context, ArrayList<Steps> passedSteps, int stepId, StepItemClickListener passedHandler) {
        this.stepId = stepId;
        this.mContext = context;
        this.mSteps = passedSteps;
        this.mClickHandler = passedHandler;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_step_card_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //set selected step
        if (position == stepId) {
            holder.itemView.setSelected(true);
            holder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.selected_color));
        }
        holder.mStepNumber.setText(String.valueOf(position + 1));
        holder.mStepDescription.setText(mSteps.get(position).shortDescription);
        if (mSteps.get(position).thumbnailURL.isEmpty()) {
            Picasso.with(mContext).load(R.drawable.recipe_default_image).into(holder.mStepPoster);
        }
    }

    @Override
    public int getItemCount() {
        if (mSteps == null)
            return 0;
        else
            return mSteps.size();
    }

    public interface StepItemClickListener {
        void onStepClick(int clickedStepId);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.step_poster)
        ImageView mStepPoster;
        @BindView(R.id.step_number)
        TextView mStepNumber;
        @BindView(R.id.short_description_text)
        TextView mStepDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onStepClick(mSteps.get(adapterPosition).id);
        }
    }
}