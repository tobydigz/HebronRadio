package com.digzdigital.hebronradio.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.digzdigital.hebronradio.MainActivity;
import com.digzdigital.hebronradio.R;
import com.digzdigital.hebronradio.databinding.FragmentAboutBinding;


public class AboutFragment extends Fragment implements View.OnClickListener {

    private FragmentAboutBinding binding;
    public AboutFragment() {

    }


    @Override
    public void onCreate(Bundle a) {
        super.onCreate(a);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle a) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_about, container, false);


        binding.emailHeb.setOnClickListener(this);

        binding.instaHeb.setOnClickListener(this);

        binding.twitHeb.setOnClickListener(this);

        binding.dialHeb.setOnClickListener(this);

        binding.faceHeb.setOnClickListener(this);
        return binding.getRoot();
    }

    @Override
    public void onClick(View v) {
        MainActivity digz = (MainActivity) getActivity();
        switch (v.getId()){
            case R.id.emailHeb:
                Intent intent = new Intent(Intent.ACTION_SEND);
                String[] recipients = {"cuhebronfm@gmail.com"};
                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                intent.putExtra(Intent.EXTRA_SUBJECT, "Hello Hebron");
                intent.setType("text/html");
                startActivity(Intent.createChooser(intent, "Send mail"));
                break;
            case R.id.instaHeb:
                digz.getInstagram();
                break;
            case R.id.twitHeb:
                digz.getTwitter();
                break;
            case R.id.faceHeb:
                digz.getFacebook();
                break;
            case R.id.dialHeb:
                digz.call();
                break;
        }
    }
}
