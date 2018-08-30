package com.example.android.bakingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingapp.dataobjects.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecipeSelectAdapter extends RecyclerView.Adapter<RecipeSelectAdapter.ViewHolder> {
    public final recipeSelectOnClickHandler mClickHandler;
    Context context;
    private ArrayList<Recipe> mRecipe;

    RecipeSelectAdapter(Context c, recipeSelectOnClickHandler passedHandler) {
        this.context = c;
        this.mClickHandler = passedHandler;
    }

    public void setData(ArrayList<Recipe> result) {
        mRecipe = result;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context mContext = parent.getContext();
        //initialize views
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.select_recipe_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //binding values to card
        String name = mRecipe.get(position).name;
        holder.mRecipeName.setText(name);
        //if an image is not provided set default default image as poster
        if (mRecipe.get(position).image.isEmpty()) {
            Picasso.with(context).load(R.drawable.recipe_default_image).into(holder.mPosterImage);
        } else {
            Picasso.with(context).load(mRecipe.get(position).image).into(holder.mPosterImage);
        }
        holder.mServings.setText(String.valueOf(mRecipe.get(position).servings));
    }

    @Override
    public int getItemCount() {
        if (mRecipe == null)
            return 0;
        else
            return mRecipe.size();
    }

    public interface recipeSelectOnClickHandler {
        void onClickHandleMethod(Recipe selectedRecipe);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mRecipeName;
        ImageView mPosterImage;
        TextView mServings;

        public ViewHolder(View itemView) {
            super(itemView);
            mRecipeName = itemView.findViewById(R.id.recipe_name);
            mPosterImage = itemView.findViewById(R.id.recipe_poster);
            mServings = itemView.findViewById(R.id.servings_value);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClickHandleMethod(mRecipe.get(adapterPosition));
        }
    }
}
