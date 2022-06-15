package com.example.instagram.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.instagram.PostDetailsActivity;
import com.example.instagram.databinding.ItemPostBinding;
import com.example.instagram.models.Post;

import org.parceler.Parcels;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {
    private Context context;
    private List<Post> posts;

    public PostsAdapter(Context context, List<Post> posts){
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // make sure to specify parent of the recyclerView item so the root element at item_tweet.xml has access to its parent
        ItemPostBinding binding = ItemPostBinding.inflate(LayoutInflater.from(this.context), parent, false);
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

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView usernameTV;
        private ImageView profileIV;
        private ImageView postImage;
        ItemPostBinding binding;

        public ViewHolder(@NonNull ItemPostBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            usernameTV = binding.usernameText;
            profileIV = binding.profileAvatarIV;
            postImage = binding.postImage;


        }

        public void bind(Post post){
            usernameTV.setText(post.getUser().getUsername());
            Glide.with(context).load(post.getUser().getParseFile("profilePicture").getUrl()).transform(new RoundedCorners(100)).into(profileIV);
            Glide.with(context).load(post.getImage().getUrl()).into(postImage);

            binding.getRoot().setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, PostDetailsActivity.class);
                            intent.putExtra("post", Parcels.wrap(post));
                            // show the activity
                            context.startActivity(intent);
                        }
                    }
            );

        }


        @Override
        public void onClick(View view) {
            System.out.println("clicked");
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                Post post = posts.get(position);
                Intent intent = new Intent(context, PostDetailsActivity.class);
                intent.putExtra("post", Parcels.wrap(post));
                // show the activity
                context.startActivity(intent);
            }

        }
    }
}
