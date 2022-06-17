package com.example.instagram;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.instagram.databinding.FragmentPostDetailsBinding;
import com.example.instagram.models.Post;
import com.example.instagram.utils.GetRelativeTime;

import org.parceler.Parcels;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostDetailsFragment extends Fragment {

    private TextView usernameTV;
    private ImageView profileIV;
    private ImageView postImage;
    private TextView postDescription;
    private TextView postBodyUsername;
    private TextView timestampText;
    private FragmentPostDetailsBinding binding;
    private static final String ARG_PARAM1 = "post";

    private Post post;

    public PostDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param post Parameter 1.
     * @return A new instance of fragment PostDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PostDetailsFragment newInstance(Post post) {
        PostDetailsFragment fragment = new PostDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, Parcels.wrap(post));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            post = (Post) Parcels.unwrap(getArguments().getParcelable(ARG_PARAM1));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPostDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        usernameTV = binding.usernameText;
        profileIV = binding.profileAvatarIV;
        postImage = binding.postImage;
        postDescription = binding.postBodyDescription;
        postBodyUsername = binding.postBodyUsername;
        timestampText = binding.timestampText;



        usernameTV.setText(post.getUser().getUsername());
        postBodyUsername.setText(post.getUser().getUsername());
        postDescription.setText(post.getDescription());
        System.out.println("post created at"+post.getCreatedAt().toString());
        timestampText.setText(GetRelativeTime.getRelativeTimeAgo(post.getCreatedAt().toString()));

        Glide.with(this).load(post.getUser().getParseFile("profilePicture").getUrl()).centerCrop().transform(new RoundedCorners(100)).into(profileIV);
        Glide.with(this).load(post.getImage().getUrl()).into(postImage);
    }
}