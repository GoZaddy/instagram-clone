package com.example.instagram.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instagram.MainActivity;
import com.example.instagram.PostDetailsFragment;
import com.example.instagram.R;
import com.example.instagram.databinding.ItemProfilePostBinding;
import com.example.instagram.models.Post;

import org.parceler.Parcels;

import java.util.List;

public class ProfilePostsAdapter extends RecyclerView.Adapter<ProfilePostsAdapter.ViewHolder> {
    private Context context;
    private List<Post> posts;

    public ProfilePostsAdapter(Context context, List<Post> posts){
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // make sure to specify parent of the recyclerView item so the root element at item_tweet.xml has access to its parent
        ItemProfilePostBinding binding = ItemProfilePostBinding.inflate(LayoutInflater.from(this.context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView postImage;
        ItemProfilePostBinding binding;

        public ViewHolder(@NonNull ItemProfilePostBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            postImage = binding.postImage;


        }

        public void bind(Post post){
            Glide.with(context).load(post.getImage().getUrl()).into(postImage);

            binding.getRoot().setOnClickListener(
                    // todo: use bundle to open PostDetailsActivity
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            PostDetailsFragment postDetailsFragment = PostDetailsFragment.newInstance(post);
                            FragmentTransaction transaction =((MainActivity) context).getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.rlContainer, postDetailsFragment).addToBackStack(null).commit();
                        }
                    }
            );

        }

    }
}
