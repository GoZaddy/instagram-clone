package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.instagram.databinding.ActivityPostDetailsBinding;
import com.example.instagram.models.Post;
import com.example.instagram.utils.GetRelativeTime;

import org.parceler.Parcels;

public class PostDetailsActivity extends AppCompatActivity {
    private TextView usernameTV;
    private ImageView profileIV;
    private ImageView postImage;
    private TextView postDescription;
    private TextView postBodyUsername;
    private TextView timestampText;
    Post post;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPostDetailsBinding binding = ActivityPostDetailsBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        usernameTV = binding.usernameText;
        profileIV = binding.profileAvatarIV;
        postImage = binding.postImage;
        postDescription = binding.postBodyDescription;
        postBodyUsername = binding.postBodyUsername;
        timestampText = binding.timestampText;

        post = (Post) (Parcels.unwrap(getIntent().getParcelableExtra("post")));


        usernameTV.setText(post.getUser().getUsername());
        postBodyUsername.setText(post.getUser().getUsername());
        postDescription.setText(post.getDescription());
        System.out.println("post created at"+post.getCreatedAt().toString());
        timestampText.setText(GetRelativeTime.getRelativeTimeAgo(post.getCreatedAt().toString()));

        Glide.with(this).load(post.getImage().getUrl()).centerCrop().transform(new RoundedCorners(100)).into(profileIV);
        Glide.with(this).load(post.getImage().getUrl()).into(postImage);
    }
}